/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import airpur.*;
import com.sun.rowset.CachedRowSetImpl;
import frameworkairpur.Cast;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Edgar
 */
public class AirPurManager {

    private final DAOManager dao = new DAOManager("src/frameworkairpur/database.xml");
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

    static Materiel mat = new Materiel(0, 0, 0, null, null, null);
    static Exemplaire_location location;
    static Exemplaire_vente vente;
    static Emprunter emprun;
    static Site site;
    static Facture fact;
    static Payer payer;
    static Modepaiement mode;

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
            this.result = dao.setSelect(this.select, this.table, this.where); //Execution de la requête
            this.metadata = this.result.getMetaData(); //Création d'un metadata
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rowset = new CachedRowSetImpl(); //Création d'un rowset
            rowset.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowset.populate(this.result); //Remplissage du rowset par rapport au ResultSet
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.lister = new Object[this.rowset.size()][this.metadata.getColumnCount()]; //Récupération des dimensions du tableau
            //System.out.println("Tableau : " + this.rowset.size() + "," + this.metadata.getColumnCount());
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.select.clear(); //Ré-initialisation de l'ArrayList
        if (this.where != null)
            this.where.clear(); //Ré-initialisation de l'ArrayList

        return this.lister;
    }

    public void ajouter(Object obj) {
        this.table = obj.getClass().getSimpleName(); //Récupération du nom de la classe
        this.listeAttr = obj.getClass().getDeclaredFields(); //Récupération de la liste des attributs
        this.listeMethod = cast.getGetters(obj); //Récupération de la liste des getters

        for (int i = 0; i < listeAttr.length; i++) {
            this.into.add(listeAttr[i] + ","); //Création de l'INTO de la requête
            if (this.listeAttr[i].getGenericType().equals("String")) { //Création selon le type de l'attribut
                try {
                    this.values.add("'" + this.listeMethod.get(i).invoke(obj, null) + "',"); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeMethod.get(i).invoke(obj, null) + ","); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            dao.setInsert(this.into, this.table, this.values); //Execution de la requête d'insertion
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeAttr[i] + "=" + this.listeMethod.get(i).invoke(obj, null) + ","); //Remplissage de l'ArrayList des valeurs grace aux getters
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            this.where.add(this.listeAttr[0] + "=" + this.listeMethod.get(0).invoke(obj, null)); //Remplissage de l'ArrayList des valeurs grace aux getters
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dao.setUpdate(this.values, this.table, this.where); //Execution de la requête de modification
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dao.setDelete(this.table, this.where); //Execution de la requête de suppression
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.where.clear(); //Ré-initialisation de l'ArrayList
        this.listeMethod.clear(); //Ré-initialisation de l'ArrayList
    }

    public void getLastId_partenaire() {
        try {
            System.out.println(dao.getLastId_partenaire()); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalFactureHT(int idFacture) {
        try {
            System.out.println(dao.totalFactureHT(idFacture)); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalFactureVente(int idFacture) {
        try {
            System.out.println(dao.totalFactureVente(idFacture)); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalStockLocation(int idMateriel) {
        try {
            System.out.println(dao.totalStockLocation(idMateriel)); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalStockVente(int idMateriel) {
        try {
            System.out.println(dao.totalStockVente(idMateriel)); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
