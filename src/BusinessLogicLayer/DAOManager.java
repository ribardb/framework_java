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
    private ResultSet result;
    private ResultSetMetaData metadata;
    private CachedRowSet rowset;
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
     */
    public Object[][] lister(Object obj, ArrayList select, ArrayList where) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.select = select; //Récupération des champs demandés
        this.where = where; //Récupération des restrictions

        if (this.select == null) {
            this.select = new ArrayList<>();
            this.select.add("*");
        }

        try {
            // this.result = daoQuery.select(this.select, this.table, this.where); //Execution de la requête
            this.metadata = this.result.getMetaData(); //Création d'un metadata
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rowset = new CachedRowSetImpl(); //Création d'un rowset
            rowset.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowset.populate(this.result); //Remplissage du rowset par rapport au ResultSet
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.lister = new Object[this.rowset.size()][this.metadata.getColumnCount()]; //Récupération des dimensions du tableau
            //System.out.println("Tableau : " + this.rowset.size() + "," + this.metadata.getColumnCount());
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (this.result != null) { //La requête a renvoyé des résultats
                int i = 0; //Numéro de la ligne du tableau
                this.rowset.isFirst(); //Replacement du curseur au début du ResultSet
                while (this.rowset.next()) {
                    if (this.select.contains("*")) { //Si on sélectionne tout les champs
                        for (int j = 0; j < this.listeAttr.length; j++) {
                            switch (this.listeAttr[j].getType().getSimpleName()) { //Analyse du type de l'attribut
                                case "int":
                                    this.lister[i][j] = this.rowset.getInt(j + 1);
                                    break;
                                case "String":
                                    this.lister[i][j] = this.rowset.getString(j + 1);
                                    break;
                                case "Float":
                                    this.lister[i][j] = this.rowset.getFloat(j + 1);
                                    break;
                            }
                        }
                    } else { //Si on sélectionne des champs spécifiques
                        for (int k = 0; k < this.select.size(); k++) {
                            boolean trouve = false;
                            int l = 0; //Parcours de la liste des attributs
                            while (trouve == false) {
                                if (this.listeAttr[l].getName().equalsIgnoreCase(this.select.get(k))) {
                                    switch (this.listeAttr[l].getType().getSimpleName()) {
                                        case "int":
                                            this.lister[i][k] = this.rowset.getInt(k + 1);
                                            trouve = true;
                                            break;
                                        case "String":
                                            this.lister[i][k] = this.rowset.getString(k + 1);
                                            trouve = true;
                                            break;
                                        case "Float":
                                            this.lister[i][k] = this.rowset.getFloat(k + 1);
                                            trouve = true;
                                            break;
                                        default:
                                            System.out.println("Type inconnu");
                                            trouve = true;
                                    }
                                }
                                l++;
                            }

                        }
                    }
                    i++;
                }
            } else { //Si la requête ne renvoie aucun résultat
                lister[0][0] = "Aucun résultat";
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.select.clear(); //Ré-initialisation de l'ArrayList
        if (this.where != null) {
            this.where.clear(); //Ré-initialisation de l'ArrayList
        }
        return this.lister;
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
