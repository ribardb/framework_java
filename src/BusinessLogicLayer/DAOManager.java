/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BusinessLogicLayer;

import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import frameworkairpur.ImportXML;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Edgar
 */
public class DAOManager {
    
    private DatabaseConnection con = null;
    private DAOManagerQuery daoQuery = new DAOManagerQuery();
    private String query;
    private Statement stmt;
    private ResultSet rs;

    public DAOManager(String url) throws Exception {
        ImportXML xml = new ImportXML(url);

        switch (xml.getTypeDB()) {
            case "Oracle":
                this.con = new DatabaseConnectionOracle(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
            case "SQLServer":
                this.con = new DatabaseConnectionSQLServer(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
            case "MySQL":
                this.con = new DatabaseConnectionMySQL(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
        }
        this.con.getConnection();
    }
    
    public void disconnect() throws Exception {
        this.con.disconnect();
    }
    
    public ResultSet setSelect(ArrayList select, String table, ArrayList where) throws SQLException {
        this.query = daoQuery.select(select, table, where);
        this.rs = this.stmt.executeQuery(this.query);
        return this.rs;
    }
    
    public void setInsert(ArrayList into, String table, ArrayList values) throws SQLException {
        this.query = daoQuery.insert(into, table, values);
        this.stmt.executeQuery(this.query);
    }
    
    public void setUpdate(ArrayList values, String table, ArrayList where) throws SQLException {
        this.query = daoQuery.update(values, table, values);
        this.stmt.executeQuery(this.query);
    }
    
    public void setDelete(String table, ArrayList where) throws SQLException {
        this.query = daoQuery.delete(table, where);
        this.stmt.executeQuery(this.query);
    }
    
}
