/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogicLayer;

import BusinessLogicLayer.DAOManager;
import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import airpur.*;
import com.sun.rowset.CachedRowSetImpl;
import frameworkairpur.Cast;
import frameworkairpur.ImportXML;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Edgar
 */
public class DAOManager {

    private Cast cast = new Cast();
    private ResultSet result = null;
    private Object[][] lister;
    private ArrayList<String> select = new ArrayList<>(); //liste d'attributs
    private ArrayList where = new ArrayList(); //liste d'attributs et de valeurs ex:id=1
    private ArrayList into = new ArrayList(); //liste d'attributs
    private ArrayList values = new ArrayList(); //liste de valeurs | liste d'attributs et de valeurs ex:id=1
    private String table = null;
    private Field[] listeAttr;
    private ArrayList<Method> listeMethod = new ArrayList<>();

    private DatabaseConnection con = null;
    private DAOManagerQuery daoQuery = new DAOManagerQuery();
    private String query;
    private Statement stmt;
    private CallableStatement csStmt;
    private ResultSet rs;

    /**
     * Instance unique pré-initialisée
     */
    private static DAOManager INSTANCE;

    /**
     *
     * @param url
     */
    private DAOManager() {
        try {
            ImportXML xml = new ImportXML("src/frameworkairpur/database.xml");

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

            this.stmt = con.getConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     *
     * @return
     */
    public static DAOManager getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new DAOManager();
            }
        } catch (Exception e) {
            System.out.println("Erreur d'instanciation : " + e.toString());
        }
        return INSTANCE;
    }

    public void disconnect() throws Exception {
        this.con.disconnect();
    }

    /**
     * ************************* Gestion **************************
     * @param obj
     * @param where
     * @return 
     */
    public ResultSet lister(Object obj, ArrayList where) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.where = where; //Récupération des restrictions
        
        try {
            this.result = this.stmt.executeQuery(daoQuery.select(this.table, this.where));
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.select.clear(); //Ré-initialisation de l'ArrayList
        if (this.where != null) {
            this.where.clear(); //Ré-initialisation de l'ArrayList
        }
        
        return this.result;
    }

    public void ajouter(Object obj) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.listeMethod = cast.getGetters(obj); //Récupération de la liste des getters

        System.out.println(Arrays.toString(this.listeAttr));

        for (int i = 0; i < this.listeAttr.length; i++) {
            if (this.listeAttr[i].getType().getSimpleName().equalsIgnoreCase("String")) { //Création selon le type de l'attribut
                try {
                    this.values.add("'" + this.listeMethod.get(i).invoke(obj, null) + "',"); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeMethod.get(i).invoke(obj, null) + ","); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            this.stmt.executeQuery(daoQuery.insert(this.table, this.values));
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.into.clear(); //Ré-initialisation de l'ArrayList
        this.values.clear(); //Ré-initialisation de l'ArrayList
        this.listeMethod.clear(); //Ré-initialisation de l'ArrayList
    }

    public void modifier(Object obj) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.listeMethod = cast.getGetters(obj); //Récupération de la liste des getters

        for (int i = 1; i < listeAttr.length; i++) {
            if (this.listeAttr[i].getGenericType().equals("String")) { //Création selon le type d'attributs
                try {
                    this.values.add(this.listeAttr[i] + "='" + this.listeMethod.get(i).invoke(obj, null) + "',"); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeAttr[i] + "=" + this.listeMethod.get(i).invoke(obj, null) + ","); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            this.where.add(this.listeAttr[0] + "=" + this.listeMethod.get(0).invoke(obj, null)); //Remplissage de l'ArrayList des valeurs grace aux getters
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        daoQuery.update(this.values, this.table, this.where);

        this.where.clear(); //Ré-initialisation de l'ArrayList
        this.values.clear(); //Ré-initialisation de l'ArrayList
        this.listeMethod.clear(); //Ré-initialisation de l'ArrayList
    }

    public void supprimer(Object obj) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.listeMethod = cast.getGetters(obj); //Récupération de la liste des getters

        try {
            this.where.add(this.listeAttr[0] + "=" + this.listeMethod.get(0).invoke(obj, null)); //Remplissage de l'ArrayList des valeurs grace aux getters
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        daoQuery.delete(this.table, this.where);

        this.where.clear(); //Ré-initialisation de l'ArrayList
        this.listeMethod.clear(); //Ré-initialisation de l'ArrayList
    }

    /**
     * ************************* Fonctions de la base de données Oracle
     * **************************
     */
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


    public ResultSet selectManager(String select) {
        this.rs = null;
        try {
            this.rs = this.stmt.executeQuery(select);
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.rs;
    }

    public void insertManager(String insert) throws SQLException {
        try {
            this.stmt.executeUpdate(insert);
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteManager(String delete) throws SQLException {
        try {
            this.stmt.executeUpdate(delete);
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateManager(String update) throws SQLException {
        try {
            this.stmt.executeUpdate(update);
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
