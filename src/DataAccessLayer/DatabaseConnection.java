    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import frameworkairpur.Cast;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Edgar
 */
public abstract class DatabaseConnection {
 
    protected String login;
    protected String psw;
    protected String ip;
    protected String port;
    protected String url;
    protected Connection con = null;
    protected static final String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
    protected static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    protected static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    protected static final String INSTANCE_ORACLE = "jdbc:oracle:thin:@";
    protected static final String INSTANCE_SQLSERVER = "jdbc:sqlserver:thin:@";
    protected static final String INSTANCE_MYSQL = "jdbc:mysql:thin:@";
    
    
    public DatabaseConnection(String login, String psw, String ip, String port) {
        this.login = login;
        this.psw = psw;
        this.ip = ip;
        this.port = port;
    }
    
    /**
     * Permet de ce connecter au SGBD
     *
     * @return boolean
     * @throws SQLException
     */
    public boolean getConnection() throws Exception {
        System.out.println("Connection de base ok");
        return false;
    }

    /**
     * Permet de fermet la connexion au sgbd
     *
     * @return
     * @throws Exception
     */
    public boolean disconnect() throws Exception {
        if (this.con != null) {
            this.con.close();
            return true;
        }
        return false;
    }    

}
