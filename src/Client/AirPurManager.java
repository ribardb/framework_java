/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import airpur.*;
import frameworkairpur.Cast;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edgar
 */
public class AirPurManager {

    private final DAOManager dao = new DAOManager("src/frameworkairpur/database.xml");
    private Cast cast;
    private ResultSet result;
    private ArrayList lister = new ArrayList();
    private ArrayList select = new ArrayList(); //liste d'attributs
    private ArrayList where = new ArrayList(); //liste d'attributs et de valeurs ex:id=1
    private ArrayList into = new ArrayList(); //liste d'attributs
    private ArrayList values = new ArrayList(); //liste de valeurs | liste d'attributs et de valeurs ex:id=1
    private String table;

    static Materiel mat = new Materiel(0,0,0,null,null,null);
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
            while (this.result.next()) {
                String tmp = "";
                for (int i = 0; i < this.select.size(); i++) {
                    switch (cast.getType(this.select.get(i))) {
                        case "Integer":
                            tmp = tmp + this.result.getInt(i) + "'";
                            break;
                        case "String":
                            tmp = tmp + this.result.getString(i) + "'";
                            break;
                        case "Date":
                            tmp = tmp + this.result.getDate(i) + "'";
                            break;
                        case "Float":
                            tmp = tmp + this.result.getFloat(i) + "'";
                            break;
                    }
                }
                this.lister.add(tmp.substring(0, tmp.length()-1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.lister;
    }
    
    public void ajoutMateriel(Materiel mat) {
        this.table = mat.getClasse();
        
        if (mat.getId_materiel() != 0) {
            this.into.add("id_materiel,");
            this.values.add(mat.getId_materiel() + ",");
        }
        if (mat.getId_tva() != 0) {
            this.into.add("id_tva");
            this.values.add(mat.getId_tva() + ",");
        }
        if (mat.getId_categorie() != 0) {
            this.into.add("id_categorie,");
            this.values.add("'" + mat.getId_categorie() + ",");
        }
        if (mat.getNom_materiel() != null) {
            this.into.add("nom_materiel,");
            this.values.add("'" + mat.getNom_materiel() + "',");
        }
        if (mat.getModele_materiel() != null) {
            this.into.add("modele_materiel,");
            this.values.add("'" + mat.getModele_materiel() + "',");
        }
        if (mat.getDescription_materiel() != null) {
            this.into.add("description_materiel,");
            this.values.add("'" + mat.getDescription_materiel() + "',");
        }
        
        this.into.set(
                this.into.size()-1, this.into.get(
                        this.into.size()-1).toString().substring(
                                0, this.into.size()-1).toString().length()-1);
        this.values.set(
                this.values.size()-1, this.values.get(
                        this.values.size()-1).toString().substring(
                                0, this.values.size()-1).toString().length()-1);
                
        try {
            dao.setInsert(this.into, this.table, this.values);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modifierMateriel(Materiel mat) {
        this.table = mat.getClasse();
        
        this.values.add("id_tva=" + mat.getId_tva());
        this.values.add("id_categorie=" + mat.getId_categorie());
        this.values.add("nom_materiel='" + mat.getNom_materiel() + "'");
        this.values.add("modele_materiel='" + mat.getModele_materiel() + "'");
        this.values.add("description_materiel='" + mat.getDescription_materiel() + "'");
        
        this.where.add("id_materiel=" + mat.getId_materiel());
        
        try {
            dao.setUpdate(this.values, this.table, this.where);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void supprimerMateriel(Materiel mat) {
        this.table = mat.getClasse();
        this.where = null;
        
        try {
            dao.setDelete(this.table, this.where);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
