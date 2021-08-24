package com.retail.starlee.utils;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class DummyTrustManager  implements X509TrustManager {
    private static DummyTrustManager instance;

    static {
        try {
            instance = new DummyTrustManager();
        } catch (Throwable e) {
        }
    }
    private X509TrustManager sunJSSEX509TrustManager;

    public static DummyTrustManager getInstance() {
        return instance;
    }

    public DummyTrustManager() throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init((KeyStore) null);

        TrustManager tms[] = tmf.getTrustManagers();

        /*
         * Iterate over the returned trustmanagers, look for an instance of
         * X509TrustManager. If found, use that as our "default" trust manager.
         */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                break;
            }
        }
        if (sunJSSEX509TrustManager == null) {
            throw new Exception("Couldn't initialize");
        }
    }

    /*
     * The default X509TrustManager returned by SunX509. We'll delegate
     * decisions to it, and fall back to the logic in this class if the default
     * X509TrustManager doesn't trust it.
     */

    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.
            throw excep;
        }
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust the cert
             * chain.
             */
        }
    }

    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        // return sunJSSEX509TrustManager.getAcceptedIssuers();
        if (sunJSSEX509TrustManager == null) {
            return null;
        }

        return sunJSSEX509TrustManager.getAcceptedIssuers();

    }

}
