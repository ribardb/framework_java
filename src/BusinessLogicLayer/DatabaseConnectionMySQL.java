/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BusinessLogicLayer;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class DatabaseConnectionMySQL extends DatabaseConnection {
    
    private final String base;

    public DatabaseConnectionMySQL(String login, String psw, String ip, String port, String base) throws Exception {
        super(login, psw, ip, port);
        this.base = base;
    }
    
    /**
     *
     * @return 
     */
    @Override
    public boolean getConnection() {
        try {
            Class.forName(DRIVER_MYSQL);
        }
	catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,	"Classe de driver introuvable " + ex.getMessage());
            System.exit(0);
	}
        
        try {
            this.con = DriverManager.getConnection(INSTANCE_MYSQL + ip + ":" + port + ":" + base + "," + login + "," + psw);
            return true;
	}
        catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            return false;
	}
    }
    
}