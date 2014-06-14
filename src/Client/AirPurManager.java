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
        this.table = obj.getClass().getSimpleName();
        this.listeAttr = obj.getClass().getDeclaredFields();
        this.select = select;
        this.where = where;

        if (this.select == null) {
            this.select = new ArrayList<>();
            this.select.add("*");
        }

        try {
            this.result = dao.setSelect(this.select, this.table, this.where);
            this.metadata = this.result.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rowset = new CachedRowSetImpl();
            rowset.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowset.populate(this.result);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.lister = new Object[this.rowset.size()][this.metadata.getColumnCount()];
            System.out.println("Tableau : " + this.rowset.size() + "," + this.metadata.getColumnCount());
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (this.result != null) {
                int i = 0;
                this.rowset.isFirst();
                while (this.rowset.next()) {
                    if (this.select.contains("*")) {
                        for (int j = 0; j < this.listeAttr.length; j++) {
                            switch (this.listeAttr[j].getType().getSimpleName()) {
                                case "int":
                                    System.out.println(i + "," + j);
                                    this.lister[i][j] = this.rowset.getInt(j + 1);
                                    break;
                                case "String":
                                    System.out.println(i + "," + j);
                                    this.lister[i][j] = this.rowset.getString(j + 1);
                                    break;
                                case "Float":
                                    System.out.println(i + "," + j);
                                    this.lister[i][j] = this.rowset.getFloat(j + 1);
                                    break;
                            }
                        }
                    } else {
                        for (int k = 0; k < this.select.size(); k++) {
                            boolean trouve = false;
                            int l = 0;
                            while (trouve == false) {
                                if (this.listeAttr[l].getName().equalsIgnoreCase(this.select.get(k))) {
                                    switch (this.listeAttr[l].getType().getSimpleName()) {
                                        case "int":
                                            //System.out.println(i + "," + k);
                                            this.lister[i][k] = this.rowset.getInt(k + 1);
                                            trouve = true;
                                            break;
                                        case "String":
                                            //System.out.println(i + "," + k);
                                            this.lister[i][k] = this.rowset.getString(k + 1);
                                            trouve = true;
                                            break;
                                        case "Float":
                                            //System.out.println(i + "," + k);
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
            } else {
                lister[0][0] = "Aucun résultat";
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.select.clear();
        //this.where.clear();

        return this.lister;
    }

    public void ajouter(Object obj) {
        this.table = obj.getClass().getSimpleName();
        this.listeAttr = obj.getClass().getDeclaredFields();
        this.listeMethod = cast.getGetters(obj);

        for (int i = 0; i < listeAttr.length; i++) {
            this.into.add(listeAttr[i] + ",");
            if (this.listeAttr[i].getGenericType().equals("String")) {
                try {
                    this.values.add("'" + this.listeMethod.get(i).invoke(obj, null) + "',");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeMethod.get(i).invoke(obj, null) + ",");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            dao.setInsert(this.into, this.table, this.values);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.into.clear();
        this.values.clear();
        this.listeMethod.clear();
    }

    public void modifier(Object obj) {
        this.table = obj.getClass().getSimpleName();
        this.listeAttr = obj.getClass().getDeclaredFields();
        this.listeMethod = cast.getGetters(obj);

        for (int i = 1; i < listeAttr.length; i++) {
            this.into.add(listeAttr[i] + ",");
            if (this.listeAttr[i].getGenericType().equals("String")) {
                try {
                    this.values.add(this.listeAttr[i] + "='" + this.listeMethod.get(i).invoke(obj, null) + "',");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    this.values.add(this.listeAttr[i] + "=" + this.listeMethod.get(i).invoke(obj, null) + ",");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            this.where.add(this.listeAttr[0] + "=" + this.listeMethod.get(0).invoke(obj, null));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dao.setUpdate(this.values, this.table, this.where);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.where.clear();
        this.values.clear();
        this.listeMethod.clear();
    }

    public void supprimer(Object obj) {
        this.table = obj.getClass().getSimpleName();
        this.listeAttr = obj.getClass().getDeclaredFields();
        this.listeMethod = cast.getGetters(obj);

        try {
            this.where.add(this.listeAttr[0] + "=" + this.listeMethod.get(0).invoke(obj, null));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            dao.setDelete(this.table, this.where);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.where.clear();
        this.listeMethod.clear();
    }

    public void getLastId_partenaire() {
        try {
            System.out.println(dao.getLastId_partenaire());
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalFactureHT(int idFacture) {
        try {
            System.out.println(dao.totalFactureHT(idFacture));
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalFactureVente(int idFacture) {
        try {
            System.out.println(dao.totalFactureVente(idFacture));
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalStockLocation(int idMateriel) {
        try {
            System.out.println(dao.totalStockLocation(idMateriel));
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void totalStockVente(int idMateriel) {
        try {
            System.out.println(dao.totalStockVente(idMateriel));
        } catch (Exception ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
