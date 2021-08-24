package com.retail.starlee.utils;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 
 * @author akshath
 * @author Preetham
 * 
 */
public class HttpUtil {

	static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private static OkHttpClient client = null;
	private static Cache cache = null;
	private static boolean okHttpInit = false;
	private static Proxy httpProxy = null;
	private static String defaultCacheDirectoryName = "./cacheDir";
	private static int defaultCacheSizeinMB = 10;
	private static String defaultProxyIp;
	private static String defaultProxyPort;
	private static int defaultConnectionTimeoutInSec = 30;
	private static int defaultReadTimeoutInSec = 90;
	private static int defaultWriteTimeoutInSec = 90;
	private static boolean testMode = true;
	public static final String CHAR_SET = "UTF-8";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType WWW_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
	private static SSLSocketFactory sslSocketFactory;
	private static TrustManager[] trustManager;
	private static SSLContext sslContext;

	public static void init(String proxyIP, String proxyPort) {
		try {
			if (okHttpInit == false || cache == null) {
				logger.debug("Initialising httpUtil with default configuration");
				// Configure testMode
				client = new OkHttpClient();
				if (testMode) {
					configureTestMode();
				}
				configureCacheResponse(defaultCacheDirectoryName, defaultCacheSizeinMB);
				configureDefaultTimeouts(defaultConnectionTimeoutInSec, defaultReadTimeoutInSec,
						defaultWriteTimeoutInSec);
				// Configure proxy
				if (proxyIP != null && proxyPort != null) {
					defaultProxyIp = proxyIP;
					defaultProxyPort = proxyPort;
					configureProxy(defaultProxyIp, defaultProxyPort);
				}
				okHttpInit = true;
			} else {
				logger.debug("InMemory OKHTTPClient exists, hence skiping Client creation");
			}
		} catch (Exception e) {
			logger.error("Exception in OKHTTP Client creation : ", e);
			okHttpInit = false;
		}
	}

	public static void init(boolean isTestMode, boolean enableResponseCache, String cacheDirectoryName,
			int cacheSizeInMB, int connectionTimeOutInSec, int readTimeOutInSec, int writeTimeoutInSec, String proxyIP,
			String proxyPort) throws Exception {

		// TODO make changes for making singleton, skipped as not used.Dont use
		// unless made changes

		logger.debug("Initialising httpUtil...");
		// Configure testMode
		testMode = isTestMode;
		if (testMode) {
			configureTestMode();
		}

		defaultCacheDirectoryName = cacheDirectoryName;
		defaultCacheSizeinMB = cacheSizeInMB;
		configureCacheResponse(defaultCacheDirectoryName, defaultCacheSizeinMB);

		// Configure timeouts
		defaultConnectionTimeoutInSec = connectionTimeOutInSec;
		defaultReadTimeoutInSec = readTimeOutInSec;
		defaultWriteTimeoutInSec = writeTimeoutInSec;
		configureDefaultTimeouts(defaultConnectionTimeoutInSec, defaultReadTimeoutInSec, defaultWriteTimeoutInSec);

		// Configure proxy
		if (proxyIP != null && proxyPort != null) {
			defaultProxyIp = proxyIP;
			defaultProxyPort = proxyPort;
			configureProxy(defaultProxyIp, defaultProxyPort);
		}

	}

	private static void configureCacheResponse(String cacheDirectoryName, int cacheSizeInMB) throws Exception {
		logger.debug("Enabling response cache in dir:" + cacheDirectoryName + ", MaxSize: " + cacheSizeInMB + "MB");
		File cacheDirectory = new File(cacheDirectoryName);
		boolean dirExists = false;
		if (!cacheDirectory.exists()) {
			logger.debug("Cache dir doesn't exists, creating one");
			try {
				dirExists = cacheDirectory.mkdir();
			} catch (SecurityException se) {
				logger.error("Exception while creating directory for cache ", se);
			}
		} else {
			dirExists = true;
		}

		if (dirExists) {
			cache = new Cache(cacheDirectory, (cacheSizeInMB * 1024 * 1024));
			client.setCache(cache);
		}
	}

	// Setting testMode configuration. If set as testMode, the connection will
	// skip certification check
	private static boolean configureTestMode() {
		logger.debug("Test mode enabled in HttpUtil");
		try {
			makeSSLSocketFactory();
			client.setSslSocketFactory(sslSocketFactory);
			client.setHostnameVerifier(vf);
			return true;
		} catch (Exception e) {
			logger.error("Exception while setting testMode", e);
			return false;
		}
	}

	// Override the default timeout configuration
	public static void configureDefaultTimeouts(int connectionTimeOutInSec, int readTimeOutInSec, int writeTimeoutInSec)
			throws Exception {
		client.setConnectTimeout(connectionTimeOutInSec, TimeUnit.SECONDS);
		client.setWriteTimeout(writeTimeoutInSec, TimeUnit.SECONDS);
		client.setReadTimeout(readTimeOutInSec, TimeUnit.SECONDS);
	}

	// Setting default proxy configuration
	public static void configureProxy(String proxyIP, String proxyPort) {
		logger.debug("proxy configured to " + proxyIP + ":" + proxyPort);
		defaultProxyIp = proxyIP;
		defaultProxyPort = proxyPort;
		if (proxyIP != null && proxyPort != null) {
			try {
				SocketAddress addr = new InetSocketAddress(proxyIP, Integer.parseInt(proxyPort));
				httpProxy = new Proxy(Proxy.Type.HTTP, addr);
				client.setProxy(httpProxy);
			} catch (Exception e) {
				logger.error("Proxy set up warning  ", e);
			}
		} else {
			httpProxy = null;// reset for testing
		}
	}

	public static String postData(String posturl, byte postData[], boolean useCache, MediaType mediaType,
			boolean skipProxy, int connectionTimeoutInSec, int readTimeOutInSec) throws Exception {
		URL url;
		logger.debug("Requesting " + posturl + " with proxy : " + (!skipProxy));
		long stime = System.currentTimeMillis();
		try {
			url = new URL(posturl);
			RequestBody body = RequestBody.create(mediaType, postData);

			Request.Builder builder = new Request.Builder();
			builder.url(url);
			if (useCache == false) {
				builder.addHeader("Cache-Control", "no-cache");
			}
			builder.post(body);
			Request request = builder.build();

			Response res = httpConnectWithTimeouts(request, skipProxy, connectionTimeoutInSec, readTimeOutInSec);

			if (res == null) {
				logger.error("Null response received");
				return null;
			}

			if (res.code() != HttpURLConnection.HTTP_OK) {
				logger.debug("BAD response protocol " + res.protocol() + " code " + res.code() + " message "
						+ res.message());
				return null;
			} else {
				logger.debug("Protocol used " + res.protocol() + " code " + res.code());
			}

			return res.body().string();
		} finally {
			long etime = System.currentTimeMillis();
			logger.debug("Time taken : " + (etime - stime));
		}
	}

	public static String postData(String posturl, byte postData[], boolean useCache, MediaType mediaType,
			boolean skipProxy) throws Exception {
		URL url;
		logger.debug("Requesting " + posturl + " with proxy : " + (!skipProxy));
		long stime = System.currentTimeMillis();
		try {
			url = new URL(posturl);
			RequestBody body = RequestBody.create(mediaType, postData);

			Request.Builder builder = new Request.Builder();
			builder.url(url);
			if (useCache == false) {
				builder.addHeader("Cache-Control", "no-cache");
			}
			builder.post(body);

			Request request = builder.build();

			Response res = httpConnect(request, skipProxy);

			if (res == null) {
				logger.error("Null response received");
				return null;
			}

			if (res.code() != HttpURLConnection.HTTP_OK) {
				logger.debug("BAD response protocol " + res.protocol() + " code " + res.code() + " message "
						+ res.message());
				return null;
			} else {
				logger.debug("Protocol used " + res.protocol() + " code " + res.code());
			}

			return res.body().string();
		} catch (Exception e) {
			logger.error("Exception :", e);
			return null;
		} finally {
			long etime = System.currentTimeMillis();
			logger.debug("Time taken : " + (etime - stime));
		}
	}

	public static String postDataWithHeader(String posturl, byte postData[], boolean useCache, MediaType mediaType,
			boolean skipProxy, HashMap<String, String> headerMap, boolean isHttpsConnection, String readTimeoutStr,
			String connectionTimeoutStr, boolean isReponseBodySupported) {

		URL url;
		logger.debug("Requesting " + posturl + " with proxy : " + (!skipProxy));
		long stime = System.currentTimeMillis();

		int defaultConnectionTimeout = 30000;// Default time out
		int defaultReadTimeout = 15000;// Default time out
		int readTimeoutNumeric;
		int connectionTimeoutNumeric;
		try {
			connectionTimeoutNumeric = Integer.parseInt(connectionTimeoutStr);
		} catch (Exception e) {
			logger.error("Error in setting connectionTimeout from DB, setting default connectionTimeout(ms) : "+ defaultConnectionTimeout,e);
			connectionTimeoutNumeric = defaultConnectionTimeout;
		}
		try {
			readTimeoutNumeric = Integer.parseInt(readTimeoutStr);
		} catch (Exception e) {
			logger.error(
					"Error in setting readTimeout from DB, setting default readTimeout(ms) : " + defaultReadTimeout,e);
			readTimeoutNumeric = defaultReadTimeout;
		}

		try {
			url = new URL(posturl);
			RequestBody body = RequestBody.create(mediaType, postData);

			Request.Builder builder = new Request.Builder();
			builder.url(url);
			if (!useCache) {
				builder.addHeader("Cache-Control", "no-cache");
			}
			builder.post(body);

			// Setting request properties in the header
			for (String key : headerMap.keySet()) {
				builder.header(key, headerMap.get(key));
			}

			Request request = builder.build();
			logger.debug("sslfactory: "+sslSocketFactory);
			if (isHttpsConnection) {
				client.setSslSocketFactory(sslSocketFactory);
			}

			// Response res = httpConnect(request, skipProxy);
			Response res = httpConnectWithTimeouts(request, skipProxy, connectionTimeoutNumeric / 1000,
					readTimeoutNumeric / 1000);

			if (res == null) {
				logger.error("Null response received");
				return null;
			}

			if (res.code() != HttpURLConnection.HTTP_OK) {
				logger.debug("BAD response protocol " + res.protocol() + " code " + res.code() + " message "
						+ res.message());
				return null;
			} else {
				logger.debug("Protocol used " + res.protocol() + " code " + res.code());
			}

			if (isReponseBodySupported)
				return res.body().string();
			else
				return res.toString();
		} catch (SocketTimeoutException se) {
			logger.error("SocketTimeoutException, timing out " + se.getMessage(),se);
			return null;
		} catch (InterruptedIOException ie) {
			logger.error("InterruptedIOException, timing out " + ie.getMessage(),ie);
			return null;
		} catch (Exception e) {
			logger.error("Exception :", e);
			return null;
		} finally {
			long etime = System.currentTimeMillis();
			logger.debug("Time taken : " + (etime - stime));
		}

	}

	// Using provided timeout value instead of default
	public static Response httpConnectWithTimeouts(Request request, boolean skipProxy, int connectionTimeoutInSec,
			int readTimeOutInSec) throws IOException {
		Response res = null;
		OkHttpClient customClient = client.clone();
		customClient.setConnectTimeout(connectionTimeoutInSec, TimeUnit.SECONDS);
		customClient.setReadTimeout(readTimeOutInSec, TimeUnit.SECONDS);

		if (skipProxy) {
			logger.debug("Skipping proxy for the request");
			customClient.setProxy(null);
		} else {
			logger.debug("Passing through proxy " + defaultProxyIp + " : " + defaultProxyPort);
		}

		res = customClient.newCall(request).execute();
		return res;
	}

	// Using default client and proxy configuration. If have to skip the default
	// proxy pass true
	public static Response httpConnect(Request request, boolean skipProxy) throws IOException {
		Response res = null;
		if (skipProxy) {
			logger.debug("Skipping proxy for the request");
			res = client.clone().setProxy(null).newCall(request).execute();
		} else {
			logger.debug("Passing through proxy " + defaultProxyIp + " : " + defaultProxyPort);
			res = client.newCall(request).execute();
		}

		return res;
	}

	public static void makeSSLSocketFactory() throws Exception {
		if (sslContext == null && getSslSocketFactory() == null) {
			sslContext = SSLContext.getInstance("TLS");
			if (trustManager == null) {
				trustManager = new TrustManager[] { DummyTrustManager.getInstance() };
			}
			sslContext.init(new KeyManager[0], trustManager, new SecureRandom());
		}

		if (getSslSocketFactory() == null) {
			sslSocketFactory = sslContext.getSocketFactory();
		}
	}

	static HostnameVerifier vf = new HostnameVerifier() {
		public boolean verify(String hostName, SSLSession session) {
			logger.error("WARNING: hostname may not match the certificate host name :" + hostName);
			return true;
		}
	};

	public static SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	public static void setSslSocketFactory(SSLSocketFactory _sslSocketFactory) {
		sslSocketFactory = _sslSocketFactory;
	}

}
