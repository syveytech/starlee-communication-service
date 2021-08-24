package com.retail.starlee.data.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a utility class for closing all connection,statement and result
 * which is opened
 * 
 * @copyright enStage.com Inc
 */
public class ConnectionHelper {

	static Logger logger = LoggerFactory.getLogger(ConnectionHelper.class);

    public static void closeResultSetPreparedStatementConnection(ResultSet rs, PreparedStatement pstmt, Connection con) {

        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing result set");
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
                pstmt = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing prepared statement");
            }
        }
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing connection");
            }
        }
    }

    public static void closePrepareStatement(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
                pst = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing prepared statement");
            }
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing connection");
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException sql) {
                logger.debug("IGNORE - SQLException while closing result set");
            }
        }
    }
}