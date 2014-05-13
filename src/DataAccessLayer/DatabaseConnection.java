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
