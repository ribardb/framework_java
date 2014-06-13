/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import airpur.*;
import frameworkairpur.Cast;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edgar
 */
public class AirPurManager {

    private final DAOManager dao = new DAOManager("src/frameworkairpur/database.xml");
    private Cast cast = new Cast();
    private ResultSet result;
    private ArrayList lister = new ArrayList();
    private ArrayList select = new ArrayList(); //liste d'attributs
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

    public ArrayList listerMateriel(ArrayList select, ArrayList where) {
        System.out.println(mat.getClasse());
        this.table = mat.getClasse();
        this.select = select;
        System.out.println(select);
        System.out.println(this.select);
        this.where = where;

        if (this.select == null) {
            this.select = new ArrayList();
            this.select.add("*");
        }

        try {
            this.result = dao.setSelect(this.select, this.table, this.where);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (this.result != null) {
                while (this.result.next()) {
                    String tmp = "";
                    if (this.select.contains("*")) {
                        tmp = tmp + this.result.getInt(1) + ";";
                        tmp = tmp + this.result.getInt(2) + ";";
                        tmp = tmp + this.result.getInt(3) + ";";
                        tmp = tmp + this.result.getString(4) + ";";
                        tmp = tmp + this.result.getString(5) + ";";
                        tmp = tmp + this.result.getString(6) + ";";
                    } else {
                        for (int i = 0; i < this.select.size(); i++) {
                            switch (cast.getType(this.select.get(i))) {
                                case "Integer":
                                    tmp = tmp + this.result.getInt(i) + ";";
                                    break;
                                case "String":
                                    tmp = tmp + this.result.getString(i) + ";";
                                    break;
                                case "Date":
                                    tmp = tmp + this.result.getDate(i) + ";";
                                    break;
                                case "Float":
                                    tmp = tmp + this.result.getFloat(i) + ";";
                                    break;
                            }
                        }
                    }
                    this.lister.add(tmp.substring(0, tmp.length() - 1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.table = null;
        this.result = null;
        this.select.clear();
        this.where.clear();
        this.lister.clear();

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
