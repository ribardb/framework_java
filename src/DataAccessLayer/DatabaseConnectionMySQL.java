/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class DatabaseConnectionMySQL extends DatabaseConnection {

    private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    private static final String INSTANCE_MYSQL = "jdbc:mysql://";
    //private static final String INSTANCE_MYSQL = "jdbc:mysql:thin:@";

    public DatabaseConnectionMySQL(String login, String psw, String ip, String port, String base) throws Exception {
        super(login, psw, ip, port, base);
    }

    /**
     *
     * @return
     */
    @Override
    public Statement getConnection() {
        try {
            Class.forName(DRIVER_MYSQL);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Classe de driver introuvable " + ex.getMessage());
            System.exit(0);
        }

        try {
            this.con = DriverManager.getConnection(INSTANCE_MYSQL + ip + ":" + port + "/" + base, login, psw);
            System.out.println("Connexion MySQL ok");
            this.stmt = this.con.createStatement();
        } catch (SQLException ex) {
            System.out.println("Exception SQL : ");
            while (ex != null) {
                String message = ex.getMessage();
                String sqlState = ex.getSQLState();
                int errorCode = ex.getErrorCode();
                System.out.println("Message = " + message);
                System.out.println("SQLState = " + sqlState);
                System.out.println("ErrorCode = " + errorCode);
                ex = ex.getNextException();
            }
        }
        return this.stmt;
    }

}
