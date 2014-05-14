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
public class DatabaseConnectionSQLServer extends DatabaseConnection {
    
    private static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String INSTANCE_SQLSERVER = "jdbc:sqlserver://";
    //private static final String INSTANCE_SQLSERVER = "jdbc:sqlserver:thin:@";

    public DatabaseConnectionSQLServer(String login, String psw, String ip, String port, String base) throws Exception {
        super(login, psw, ip, port, base);
    }
    
    /**
     *
     * @return 
     */
    @Override
    public boolean getConnection() {
        try {
            Class.forName(DRIVER_SQLSERVER);
        }
	catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,	"Classe de driver introuvable " + ex.getMessage());
            System.exit(0);
	}
        
        try {
            this.con = DriverManager.getConnection(INSTANCE_SQLSERVER + ip + ":" + port + ";database=" + base + ";user=" + login + ";password" + psw);
            System.out.println("Connexion SQL Serveur ok");
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
