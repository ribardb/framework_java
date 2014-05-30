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

    private final DAOManager dao = new DAOManager("../framework/database.xml");
    private Cast cast;
    private ResultSet result;
    private ArrayList lister;
    private ArrayList select; //liste d'attributs
    private ArrayList where; //liste d'attributs et de valeurs ex:id=1
    private ArrayList into; //liste d'attributs
    private ArrayList values; //liste de valeurs | liste d'attributs et de valeurs ex:id=1
    private String table;

    static Materiel mat;
    static Exemplaire_location location;
    static Exemplaire_vente vente;
    static Emprunter emprun;
    static Site site;
    static Facture fact;
    static Payer payer;
    static Modepaiement mode;

    public ArrayList listerMateriel(ArrayList select, ArrayList where) {
        this.table = mat.getClasse();
        this.select = select;
        this.where = where;

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
}
