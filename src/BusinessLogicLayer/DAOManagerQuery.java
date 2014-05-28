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
import java.util.ArrayList;

/**
 *
 * @author Edgar
 */
public class DAOManagerQuery implements DAOManagerInterface {

    DatabaseConnection con = null;
    String query;

    public DAOManagerQuery(String url) throws Exception {
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

    /**
     *
     * @param select
     * @param table
     * @param where
     * @return
     */
    @Override
    public String select(ArrayList select, String table, ArrayList where) {

        if (where.isEmpty()) {
            this.query = "SELECT (" + select.toString().substring(1, select.toString().length() - 1) + ") FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0; i < where.size(); i += 2) {
                clause = clause + where.get(i) + "=" + where.get(i + 1) + " AND ";
            }
            this.query = "SELECT (" + select.toString().substring(1, select.toString().length() - 1) + ") FROM " + table
                    + " WHERE" + clause.substring(0, clause.length() - 5);
        }
        return this.query;
    }

    @Override
    public String insert(ArrayList into, String table, ArrayList values) {
        if (into.isEmpty()) {
            String tmp = "";
            for (int i = 0; i < values.size(); i++) {
                tmp = tmp + values.get(i) + ",";
            }
            this.query = "INSERT INTO " + table + " VALUES (" + tmp.substring(0, tmp.length() - 1) + ")";
        } else {
            String tmp = "";
            String tmp2 = "";
            for (int i = 0; i < values.size(); i++) {
                tmp = tmp + values.get(i) + ",";
                tmp2 = tmp2 + into.get(i) + ",";
            }
            this.query = "INSERT INTO " + table + " (" + tmp2.substring(0, tmp2.length() - 1) + ") VALUES (" + tmp.substring(0, tmp.length() - 1) + ")";
        }
        return this.query;
    }

    @Override
    public String update(ArrayList values, String table, ArrayList where) {

        String NewValues = "";
        for (int i = 0; i < values.size(); i += 2) {
            NewValues = NewValues + values.get(i) + "=" + values.get(i + 1) + ", ";
        }

        String Clause = "";
        for (int j = 0; j < where.size(); j += 2) {
            Clause = Clause + where.get(j) + "=" + where.get(j + 1) + " AND ";
        }

        this.query = "UPDATE " + table + "SET " + NewValues.substring(0, NewValues.length() - 2) + "WHERE " + Clause.substring(0, Clause.length() - 5);

        return this.query;
    }

    @Override
    public String delete(String table, ArrayList where) {
        if (where.isEmpty()) {
            this.query = "DELETE FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0; i < where.size(); i += 2) {
                clause = clause + where.get(i) + "=" + where.get(i + 1) + " AND ";
            }
            this.query = "DELETE FROM " + table + " WHERE" + clause.substring(0, clause.length() - 5);
        }
        return this.query;
    }

}
