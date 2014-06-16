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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edgar
 */
public class DAOManager {
    
    private DatabaseConnection con = null;
    private DAOManagerQuery daoQuery = new DAOManagerQuery();
    private String query = null;
    private Statement stmt = null;
    private CallableStatement csStmt;
    private ResultSet rs = null;

    /**
     *
     * @param url
     */
    public DAOManager(String url) {
        try {
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
            this.stmt = this.con.getConnection().createStatement();
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() throws Exception {
        this.con.disconnect();
    }
    
    public ResultSet setSelect(ArrayList select, String table, ArrayList where) throws SQLException {
        this.query = daoQuery.select(select, table, where);
        System.out.println(this.query);
        System.out.println(this.stmt);
        System.out.println(this.rs);
        this.rs = this.stmt.executeQuery(this.query);
        return this.rs;
    }
    
    public void setInsert(ArrayList into, String table, ArrayList values) throws SQLException {
        this.query = daoQuery.insert(into, table, values);
        System.out.println(this.query);
        this.stmt.executeQuery(this.query);
    }
    
    public void setUpdate(ArrayList values, String table, ArrayList where) throws SQLException {
        this.query = daoQuery.update(values, table, where);
        System.out.println(this.query);
        this.stmt.executeQuery(this.query);
    }
    
    public void setDelete(String table, ArrayList where) throws SQLException {
        this.query = daoQuery.delete(table, where);
        System.out.println(this.query);
        this.stmt.executeQuery(this.query);
    }
    
    public Integer getLastId_partenaire() throws Exception {
        try {
            this.csStmt = this.con.getConnection().prepareCall("{ ? = call getLastId_partenaire() }"); //Création de la requête d'appel de la fonction sans paramètre
            this.csStmt.registerOutParameter(1, Types.INTEGER); //Définition du paramètre de sortie
            this.csStmt.execute(); //Execution de l'appel de la fonction
        } catch (SQLException e) {
            throw new SQLException(
                    "L'execution de la fonction est en erreur");
        }
        return this.csStmt.getInt(1);
    }
    
    public Float totalFactureHT(int idFacture) throws Exception {
        try {
            this.csStmt = this.con.getConnection().prepareCall("{ ? = call TotalFactureHT(?) }");//Création de la requête d'appel de la fonction avec paramètre
            this.csStmt.registerOutParameter(1, Types.FLOAT); //Définition du paramètre de sortie
            this.csStmt.setInt(2, idFacture); //Définition du paramètre d'entrée
            this.csStmt.execute(); //Execution de l'appel de la fonction
        } catch (SQLException e) {
            throw new SQLException(
                    "L'execution de la fonction est en erreur");//Création de la requête d'appel de la fonction avec paramètre
        }
        return this.csStmt.getFloat(1);
    }
    
    public Float totalFactureVente(int idFacture) throws Exception {
        try {
            this.csStmt = this.con.getConnection().prepareCall("{ ? = call TotalFactureVente(?) }");//Création de la requête d'appel de la fonction avec paramètre
            this.csStmt.registerOutParameter(1, Types.FLOAT); //Définition du paramètre de sortie
            this.csStmt.setInt(2, idFacture); //Définition du paramètre d'entrée
            this.csStmt.execute(); //Execution de l'appel de la fonction
        } catch (SQLException e) {
            throw new SQLException(
                    "L'execution de la fonction est en erreur");
        }
        return this.csStmt.getFloat(1);
    }
    
    public Integer totalStockLocation(int idMateriel) throws Exception {
        try {
            this.csStmt = this.con.getConnection().prepareCall("{ ? = call TotalStockLocation(?) }");//Création de la requête d'appel de la fonction avec paramètre
            this.csStmt.registerOutParameter(1, Types.INTEGER); //Définition du paramètre de sortie
            this.csStmt.setInt(2, idMateriel); //Définition du paramètre d'entrée
            this.csStmt.execute(); //Execution de l'appel de la fonction
        } catch (SQLException e) {
            throw new SQLException(
                    "L'execution de la fonction est en erreur");
        }
        return this.csStmt.getInt(1);
    }
    
    public Integer totalStockVente(int idMateriel) throws Exception {
        try {
            this.csStmt = this.con.getConnection().prepareCall("{ ? = call TotalStockVente(?) }");//Création de la requête d'appel de la fonction avec paramètre
            this.csStmt.registerOutParameter(1, Types.INTEGER); //Définition du paramètre de sortie
            this.csStmt.setInt(2, idMateriel); //Définition du paramètre d'entrée
            this.csStmt.execute(); //Execution de l'appel de la fonction
        } catch (SQLException e) {
            throw new SQLException(
                    "L'execution de la fonction est en erreur");
        }
        return this.csStmt.getInt(1);
    }
    
}
