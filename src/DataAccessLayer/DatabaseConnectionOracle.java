/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataAccessLayer;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class DatabaseConnectionOracle extends DatabaseConnection {    
    
    private final String sid;
    
    
    public DatabaseConnectionOracle(String login, String psw, String ip, String port, String sid) throws Exception {
        super(login, psw, ip, port);
        this.sid = sid;
    }
    /**
     *
     * @return 
     */
        public boolean getConnection() throws Exception {
        try {
            Class.forName(DRIVER_ORACLE);
        }
	catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,	"Classe de dirriver introuvable " + ex.getMessage());
            System.exit(0);
	}
        
        try {
            this.con = DriverManager.getConnection(INSTANCE_ORACLE + ip + ":" + port + ":" + sid, login, psw);
            System.out.println("Connexion Oracle ok");
            return true;
	}
        catch (SQLException ex) {
            System.out.println("Exception SQL : ");
            while (ex != null) {
               String message = ex.getMessage();
               String sqlState = ex.getSQLState();
               int errorCode = ex.getErrorCode();
               System.out.println("Message = " + message);
               System.out.println("SQLState = " + sqlState);
               System.out.println("ErrorCode = " + errorCode);
               ex.printStackTrace();
               ex = ex.getNextException();
            }
            return false;
	}
    }
    
}