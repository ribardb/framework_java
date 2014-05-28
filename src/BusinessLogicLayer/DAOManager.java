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

/**
 *
 * @author Edgar
 */
public class DAOManager {
    
    DatabaseConnection con = null;

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
    
}
