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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edgar
 */
public class AirPurManager {

    private final DAOManager dao = DAOManager.getInstance();
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

    //Liste des materiels
    public ArrayList<Materiel> listerMateriel() {
        ArrayList<Materiel> listMateriel = new ArrayList<Materiel>();
        try {

            this.result = dao.selectManager("Select * from Materiel");

            Materiel materiel;
            int idM;
            int idTva;
            int idCat;
            String nomMat;
            String modelMat;
            String descr;
            while (result.next()) {
                idM = this.result.getInt("ID_MATERIEL");
                idTva = this.result.getInt("ID_TVA");
                idCat = this.result.getInt("ID_CATEGORIE");
                nomMat = this.result.getString("NOM_MATERIEL");
                modelMat = this.result.getString("MODELE_MATERIEL");
                descr = this.result.getString("DESCRIPTION_MATERIEL");
                materiel = new Materiel(idM, idTva, idCat, nomMat, modelMat, descr);
                listMateriel.add(materiel);
            }
            this.result.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMateriel;
    }

    public Materiel trouverMateriel(int id) throws SQLException {
        Materiel materiel = null;
        try {

            this.result = dao.selectManager("Select * from MATERIEL WHERE ID_MATERIEL =" + id);
            if (result.first()) {
                int idM = this.result.getInt("ID_MATERIEL");
                int idTva = this.result.getInt("ID_TVA");
                int idCat = this.result.getInt("ID_CATEGORIE");
                String nomMat = this.result.getString("NOM_MATERIEL");
                String modelMat = this.result.getString("MODELE_MATERIEL");
                String descr = this.result.getString("DESCRIPTION_MATERIEL");
                materiel = new Materiel(idM, idTva, idCat, nomMat, modelMat, descr);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiel;
    }

    //Ajout d'un materiel et verification de la TVA
    public boolean ajouterMariel(Materiel mat) {

        boolean result = false;
        try {

            //Format de la date de fin de validation pour la TVA
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //Date du jour
            Date dateNow = new Date();
            //recherche de la tva dans
            TVA tva = trouverTva(mat.getId_tva());
            //si la tva existe
            if (tva != null) {
                //parsing de la date de fin de validation de la TVA (format string en format Date)
                Date dateTva = sdf.parse(tva.getDatefinvalidation_tva());
                //si la tva est valide
                if (dateTva.compareTo(dateNow) > 0) {
                    //insertion du materiel et on retourne true
                    this.dao.ajouter(mat);
                    result = true;
                }

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //Modification d'un Materiel
    public Materiel modifierUnMateriel(Materiel materiel) {
        Materiel materielUpdate = null;
        try {
            //Pour des raisons de securité On ne peut changer seulement le nom le modele et la description
            //verification si le materiel existe bien
            materielUpdate = trouverMateriel(materiel.getId_materiel());

            if (materielUpdate != null) {
                //requete Update  du materiel
                String update = "Update Materiel set NOM_MATERIEL = '" + materiel.getNom_materiel() + "' , MODELE_MATERIEL = '" + materiel.getModele_materiel() + "' , DESCRIPTION_MATERIEL = '" + materiel.getDescription_materiel() + "' "
                        + "WHERE ID_MATERIEL =" + materiel.getId_materiel();
                //execution de la requete
                dao.updateManager(update);
                //recuperation du materiel modifié
                materielUpdate = trouverMateriel(materiel.getId_materiel());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retour du materiel modifié
        return materielUpdate;

    }

    public void supprimer(int id, String nomTable, String nomChampClePrimaire) {
        try {
            String delete = "Delete " + nomTable + " where " + nomChampClePrimaire + "= '" + id + "'";
            //execution de la requete
            dao.updateManager(delete);
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean supprimerMateriel(int id) {
        boolean result = false;
        try {
            if (trouverMateriel(id) != null) {
                String delete = "Delete Materiel WHERE id_materiel= '" + id + "'";
                //execution de la requete
                dao.updateManager(delete);
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //revoi une TVA
    public TVA trouverTva(int id) throws SQLException {
        TVA tva = null;
        try {

            this.result = dao.selectManager("Select * from TVA WHERE ID_TVA =" + id);
            if (result.first()) {
                tva = new TVA(id, this.result.getFloat("TAUX_TVA"), this.result.getString("DATEFINVALIDATION_TVA"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tva;
    }

    //Liste des Tva
    /*
     *Parametre = bool
     *Si le parametre est a vrai alors on filtre les TVA encore valide
     *Sinon on renvoi toutes les données de la table TVA
     */
    public ArrayList<TVA> listerTva(boolean dateValide) {
        ArrayList<TVA> listTva = new ArrayList<TVA>();
        ArrayList where = new ArrayList();
        TVA tvasel = new TVA();
        try {
            if (dateValide) {
                where.add("datefinvalidation_tva > SYSDATE");
            } else {
                where = null;
            }

            this.result = dao.lister(tvasel, where);

            TVA tva;
            int idTva;
            float tauxtva;
            Date dateFinValidite;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (result.next()) {
                idTva = this.result.getInt("ID_TVA");
                tauxtva = this.result.getFloat("taux_tva");
                dateFinValidite = this.result.getDate("datefinvalidation_tva");
                tva = new TVA(idTva, tauxtva, formatter.format(dateFinValidite));
                listTva.add(tva);
            }

            this.result.close();
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTva;
    }


    /*
     *Ajout d'une TVA
     *Renvoi Vrai si la date a bien été saisie
     *faux si la date est inferieur à la date du jour
     */
    public boolean ajouterTVA(TVA tva) {

        boolean result = false;
        try {

            //Format de la date de fin de validation pour la TVA
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //Date du jour
            Date dateNow = new Date();

            //si la tva existe
            if (tva != null) {
                //parsing de la date de fin de validation de la TVA (format string en format Date)
                Date dateTva = sdf.parse(tva.getDatefinvalidation_tva());
                //si la tva est valide
                if (dateTva.compareTo(dateNow) > 0) {
                    //insertion du materiel et on retourne true
                    String insert = "insert into TVA (Taux_TVA, datefinvalidation_tva)"
                            + "Values(" + tva.getTaux_tva() + " ,'" + dateTva + "')";
                    System.out.println(insert);
                    dao.insertManager(insert);
                    result = true;
                }

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    public int getLastId_partenaire() {
        int lastId = 0;
        try {
            lastId = dao.getLastId_partenaire(); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastId;
    }

    public float totalFactureHT(int idFacture) {
        float totalFacture = 0;
        try {
            totalFacture = dao.totalFactureHT(idFacture); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalFacture;
    }

    public float totalFactureVente(int idFacture) {
        float totalFacture = 0;
        try {
            totalFacture = dao.totalFactureVente(idFacture); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalFacture;
    }

    public int totalStockLocation(int idMateriel) {
        int totalStock = 0;
        try {
            totalStock = (dao.totalStockLocation(idMateriel)); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalStock;
    }

    public int totalStockVente(int idMateriel) {
        int totalStock = 0;
        try {
            totalStock = dao.totalStockVente(idMateriel); //Execution de la fonction
        } catch (Exception ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalStock;
    }

}
