    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import java.sql.*;

/**
 *
 * @author Edgar
 */
public abstract class DatabaseConnection {
 
    protected String login;
    protected String psw;
    protected String ip;
    protected String port;
    protected String base;
    protected Connection con = null;
    protected Statement stmt = null;
    
    public DatabaseConnection(String login, String psw, String ip, String port, String base) {
        this.login = login;
        this.psw = psw;
        this.ip = ip;
        this.port = port;
        this.base = base;
    }
    
    /**
     * Permet de ce connecter au SGBD
     *
     * @return boolean
     * @throws SQLException
     */
    public Statement getConnection() throws Exception {
        System.out.println("Connection de base ok");
        return null;
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
