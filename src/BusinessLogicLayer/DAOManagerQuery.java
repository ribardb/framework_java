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
    public String select(String table, ArrayList where) {

        if (where == null) {
            this.query = "SELECT * FROM " + table;

        } else {
            String clause = " ";
            for (int i = 0; i < where.size(); i++) {
                clause = clause + where.get(i) + " AND ";
            }
            this.query = "SELECT * FROM " + table
                    + " WHERE" + clause.substring(0, clause.length() - 5);
        }
        System.out.println(this.query);
        return this.query;
    }

    @Override
    public String insert(String table, ArrayList values) {
        System.out.println(values);

        String tmp = "";
        for (int i = 0; i < values.size(); i++) {
            tmp = tmp + values.get(i);
        }
        this.query = "INSERT INTO " + table + " VALUES (" + tmp.substring(0, tmp.length() - 1) + ")";

        System.out.println(this.query);
        return this.query;
    }

    @Override
    public String update(ArrayList values, String table, ArrayList where) {

        if (where == null) {
            String tmp = "";
            for (int i = 0; i < values.size(); i += 2) {
                tmp = tmp + values.get(i);
            }
            this.query = "UPDATE " + table + " SET" + tmp.substring(0, tmp.length() - 1);
            System.out.println(this.query);
        } else {
            System.out.println(values);
            System.out.println(where);
            String tmp = "";
            for (int i = 0; i < values.size(); i++) {
                tmp = tmp + values.get(i);
            }
            tmp = tmp.substring(0, tmp.length());
            this.query = "UPDATE " + table + " SET " + tmp.substring(0, tmp.length() - 1) + " WHERE " + where.get(0);
            System.out.println(this.query);
        }

        return this.query;
    }

    @Override
    public String delete(String table, ArrayList where) {
        if (where == null) {
            this.query = "DELETE FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0; i < where.size(); i++) {
                clause = clause + where.get(i) + " AND ";
            }
            this.query = "DELETE FROM " + table + " WHERE" + clause.substring(0, clause.length() - 5);
            System.out.println(this.query);
        }
        return this.query;
    }

}
