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

    private String query;

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
            for (int i = 0; i < where.size(); i++) {
                clause = clause + where.get(i) + " AND ";
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

        if (where.isEmpty()) {
            String tmp = "";
            for (int i = 0; i < values.size(); i += 2) {
                tmp = tmp + values.get(i) + ", ";
            }
            this.query = "UPDATE " + table + "SET" + tmp.substring(0, tmp.length() - 1);
        } else {
            String tmp = "";
            for (int i = 0; i < values.size(); i++) {
                tmp = tmp + values.get(i) + ", ";
            }
            String clause = "";
            for (int j = 0; j < where.size(); j += 2) {
                clause = clause + where.get(j) + "=" + where.get(j + 1) + " AND ";
            }
            this.query = "UPDATE " + table + "SET " + tmp.substring(0, tmp.length() - 1) + "WHERE " + clause.substring(0, clause.length() - 5);
            
        }

        return this.query;
    }

    @Override
    public String delete(String table, ArrayList where) {
        if (where.isEmpty()) {
            this.query = "DELETE FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0; i < where.size(); i++) {
                clause = clause + where.get(i) + " AND ";
            }
            this.query = "DELETE FROM " + table + " WHERE" + clause.substring(0, clause.length() - 5);
        }
        return this.query;
    }

}
