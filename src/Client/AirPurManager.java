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

    /********** LISTE DES ATTRIBUTS  **********/
    private final DAOManager dao = DAOManager.getInstance();
    private Cast cast;
    private ResultSet result;
    private ArrayList lister;
    private ArrayList select; //liste d'attributs
    private ArrayList where = null; //liste d'attributs et de valeurs ex:id=1
    private ArrayList into; //liste d'attributs
    private ArrayList values; //liste de valeurs | liste d'attributs et de valeurs ex:id=1
    private String table;

    static Materiel mat = new Materiel();
    static Exemplaire_location location;
    static Exemplaire_vente vente;
    static Emprunter emprun;
    static Site site;
    static Facture fact;
    static Payer payer;
    static Modepaiement mode;
    static TVA tva = new TVA();

    
    /*************** MATERIEL  ***************/
    
    //Liste des materiels
    // TEST -> OK
    public ArrayList<Materiel> listerMateriel() {
        ArrayList<Materiel> listMateriel = new ArrayList<Materiel>();
        try {
            this.result = dao.lister(this.mat, this.where);

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

    //Sélectionner un matériel
    // TEST -> OK
    public Materiel trouverMateriel(int id) throws SQLException {
        this.where = new ArrayList();
        this.where.add("id_materiel=" + id);
        try {

            this.result = dao.lister(this.mat, this.where);
            if (result.first()) {
                int idM = this.result.getInt("ID_MATERIEL");
                int idTva = this.result.getInt("ID_TVA");
                int idCat = this.result.getInt("ID_CATEGORIE");
                String nomMat = this.result.getString("NOM_MATERIEL");
                String modelMat = this.result.getString("MODELE_MATERIEL");
                String descr = this.result.getString("DESCRIPTION_MATERIEL");
                this.mat = new Materiel(idM, idTva, idCat, nomMat, modelMat, descr);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.mat;
    }

    //Ajout d'un materiel et verification de la TVA
    // TEST -> OK
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
    // TEST -> OK
    public boolean modifierUnMateriel(Materiel materiel) {
        boolean modifier = false;
        try {
            //Pour des raisons de securité On ne peut changer seulement le nom le modele et la description
            //verification si le materiel existe bien
            Materiel materielUpdate = new Materiel();
            materielUpdate = trouverMateriel(materiel.getId_materiel());

            if (materielUpdate != null) {
               
                this.dao.modifier(materiel);
                modifier = true;
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retour du materiel modifié
        return modifier;

    }

    //Suppression d'un matériel
    // TEST -> OK
    public boolean supprimerMateriel(Materiel materiel) {
        boolean result = false;
        try {
            if (trouverMateriel(materiel.getId_materiel()) != null) {
                this.dao.supprimer(materiel);
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /*************** /MATERIEL  ***************/
    
    
    
    /*************** TVA  ***************/
    
    //Sélectionner une TVA
    public TVA trouverTva(int id) throws SQLException {
        this.where = new ArrayList();
        this.where.add("id_tva = " + id);
        try {

            this.result = dao.lister(this.tva, this.where);
            if (result.first()) {
                this.tva = new TVA(id, this.result.getFloat("TAUX_TVA"), this.result.getString("DATEFINVALIDATION_TVA"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.tva;
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
    
     /*************** /TVA  ***************/
    
    
    
    /*************** EXEMPLAIRE LOCATION  ***************/
    
    //liste des exemplaires de location
    public ArrayList<Exemplaire_location> listerExemplaireLocation() {
        ArrayList<Exemplaire_location> listExemplaire = new ArrayList<Exemplaire_location>();

        try {

            this.result = dao.selectManager("select * from Exemplaire_location");
            if (result.first()) {
                while (result.next()) {
                    Exemplaire_location exemplaireLocation;
                    int idL = this.result.getInt("ID_LOCATION");
                    int idM = this.result.getInt("ID_MATERIEL");
                    int idS = this.result.getInt("ID_SITE");
                    Float tarif = this.result.getFloat("TARIF_LOCATION");
                    String etat = this.result.getString("ETATEMPRUNT_LOCATION");
                    exemplaireLocation = new Exemplaire_location(idL, idM, idS, tarif, etat);
                    listExemplaire.add(exemplaireLocation);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listExemplaire;
    }

    //Sélectionner un exemplaire de location
    public Exemplaire_location trouverExemplaireLocation(int id) {
        Exemplaire_location exemplaireLocation = null;

        try {

            this.result = dao.selectManager("select * from Exemplaire_location where ID_LOCATION=" + id);
            if (result.first()) {

                int idL = this.result.getInt("ID_LOCATION");
                int idM = this.result.getInt("ID_MATERIEL");
                int idS = this.result.getInt("ID_SITE");
                Float tarif = this.result.getFloat("TARIF_LOCATION");
                String etat = this.result.getString("ETATEMPRUNT_LOCATION");
                exemplaireLocation = new Exemplaire_location(idL, idM, idS, tarif, etat);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exemplaireLocation;
    }

    //Modification d'un exepemplaire location
    public Exemplaire_location modifierUnExemplaireLocation(Exemplaire_location exemplaireLocation) {
        Exemplaire_location locationlUpdate = null;
        try {

            locationlUpdate = trouverExemplaireLocation(exemplaireLocation.getId_location());

            if (locationlUpdate != null) {
                //requete Update  de l'exemplaire
                String update = "Update Exemplaire_location set TARIF_LOCATION = '" + exemplaireLocation.getTarif_location() + "' , "
                        + "ETATEMPRUNT_LOCATION = '" + exemplaireLocation.getEtatemprunt_location() + "' "
                        + "WHERE ID_LOCATION =" + exemplaireLocation.getId_location();
                //execution de la requete
                dao.updateManager(update);
                //recuperation du materiel modifié
                locationlUpdate = trouverExemplaireLocation(exemplaireLocation.getId_location());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retour de l'exemplaire modifié
        return locationlUpdate;

    }
    
    //Supprimer un exemplaire de location
    public void supprimerExemplaireLocation(Exemplaire_location exemplaireLocation) {
        Exemplaire_location locationlUpdate = null;
        try {
            //Pour des raisons de securité On ne peut changer seulement le nom le modele et la description
            //verification si le materiel existe bien
            locationlUpdate = trouverExemplaireLocation(exemplaireLocation.getId_location());

            if (locationlUpdate != null) {
                String delete = "Delete Exemplaire_location where ID_Location =" + exemplaireLocation.getId_location();
                //execution de la requete
                dao.updateManager(delete);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirPurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*************** /EXEMPLAIRE LOCATION  ***************/

    /**
     * ************* Fonctions de la Base de Données **************
     */
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
