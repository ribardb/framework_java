/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import airpur.*;
import Tools.Cast;
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

    static Materiel mat = new Materiel(); // OK
    static Exemplaire_location location = new Exemplaire_location(); //OK
    static Exemplaire_vente vente = new Exemplaire_vente(); // OK
    static Emprunter emprunt = new Emprunter(); //OK
    static Site site;
    static Facture fact;
    static Payer payer;
    static Modepaiement mode;
    static TVA tva = new TVA();
    //static Partenaire_patient patient = new Partenaire_patient();

    
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
    public boolean ajouterMateriel(Materiel mat) {

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
    public boolean modifierMateriel(Materiel materiel) {
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
            
            this.result = this.dao.lister(this.location, this.where);
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
    // TEST ->
    public Exemplaire_location trouverExemplaireLocation(int id) {
        
        this.where = new ArrayList();
        this.where.add("id_location = " + id);

        try {

            this.result = dao.lister(this.location, this.where) ; 
            if (result.first()) {

                int idL = this.result.getInt("ID_LOCATION");
                int idM = this.result.getInt("ID_MATERIEL");
                int idS = this.result.getInt("ID_SITE");
                Float tarif = this.result.getFloat("TARIF_LOCATION");
                String etat = this.result.getString("ETATEMPRUNT_LOCATION");
                this.location = new Exemplaire_location(idL, idM, idS, tarif, etat);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.location;
    }

    //Ajout d'un exemplaire de location
    // TEST ->
    public boolean ajouterExemplaireLocation(Exemplaire_location loc)
    {
        boolean Result = false;
        
        this.dao.ajouter(loc);
        Result = true;
        
        return Result;
    }
    
    
    //Modification d'un exepemplaire location
    public boolean modifierUnExemplaireLocation(Exemplaire_location loc) {
        boolean Modifier = false;
        
        if (trouverExemplaireLocation(loc.getId_location()) != null) {
            //requete Update  de l'exemplaire
            this.dao.modifier(loc);
            Modifier = true;
        }
        //retour de l'exemplaire modifié
        return Modifier;

    }
    
    //Supprimer un exemplaire de location
    public boolean supprimerExemplaireLocation(Exemplaire_location loc) {
        
        boolean Result = false;
        if (trouverExemplaireLocation(loc.getId_location()) != null) {
            this.dao.supprimer(loc);
            Result = true;
        }
        
        return Result;
    }
    
    /*************** /EXEMPLAIRE LOCATION  ***************/
    
    
    /*************** EXEMPLAIRE VENTE  ***************/
    //liste des exemplaires de vente
    public ArrayList<Exemplaire_vente> listerExemplaireVente() {
        ArrayList<Exemplaire_vente> listExemplaire = new ArrayList<Exemplaire_vente>();

        try {
            
            this.result = this.dao.lister(this.location, this.where);
            if (result.first()) {
                while (result.next()) {
                    Exemplaire_vente exemplaireVente;
                    int idV = this.result.getInt("ID_VENTE");
                    int idS = this.result.getInt("ID_SITE");
                    int idM = this.result.getInt("ID_MATERIEL");
                    int idF = this.result.getInt("ID_FACTURE");                    
                    Float tarif = this.result.getFloat("TARIF_VENTE");
                    Float remise = this.result.getFloat("REMISE_VENTE");
                    exemplaireVente = new Exemplaire_vente(idV, idM, idS, idF, tarif, remise);
                    listExemplaire.add(exemplaireVente);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listExemplaire;
    }
    
    //Sélectionner un exemplaire de vente
    // TEST ->
    public Exemplaire_vente trouverExemplaireVente(int id) {
        
        this.where = new ArrayList();
        this.where.add("id_vente = " + id);

        try {

            this.result = dao.lister(this.vente, this.where) ; 
            if (result.first()) {

                int idV = this.result.getInt("ID_VENTE");
                int idS = this.result.getInt("ID_SITE");
                int idM = this.result.getInt("ID_MATERIEL");
                int idF = this.result.getInt("ID_FACTURE");                    
                Float tarif = this.result.getFloat("TARIF_VENTE");
                Float remise = this.result.getFloat("REMISE_VENTE");
                this.vente = new Exemplaire_vente(idV, idM, idS, idF, tarif, remise);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.vente;
    }
    
    //Ajout d'un exemplaire de vente
    // TEST ->
    public boolean ajouterExemplaireVente(Exemplaire_vente vente)
    {
        boolean Result = false;
        
        this.dao.ajouter(vente);
        Result = true;
        
        return Result;
    }
    
    //Modification d'un exepemplaire vente
    public boolean modifierUnExemplaireVente(Exemplaire_vente vente) {
        boolean Modifier = false;
        
        if (trouverExemplaireVente(vente.getId_vente()) != null) {
            //requete Update  de l'exemplaire
            this.dao.modifier(vente);
            Modifier = true;
        }
        //retour de l'exemplaire modifié
        return Modifier;

    }
    
    //Supprimer un exemplaire de vente
    public boolean supprimerExemplaireVente(Exemplaire_vente vente) {
        
        boolean Result = false;
        if (trouverExemplaireLocation(vente.getId_vente()) != null) {
            this.dao.supprimer(vente);
            Result = true;
        }
        
        return Result;
    }    
    /*************** /EXEMPLAIRE VENTE  ***************/
    
    /*************** EMPRUNT  ***************/
    //Liste des emprunts
    public ArrayList<Emprunter> listerEmprunter() {
        ArrayList<Emprunter> listEmprunts = new ArrayList<Emprunter>();

        try {
            
            this.result = this.dao.lister(this.emprunt, this.where);
            if (result.first()) {
                while (result.next()) {
                    Emprunter emprunter;
                    int idL = this.result.getInt("ID_LOCATION");
                    int idF = this.result.getInt("ID_FACTURE");
                    String DateDebut = this.result.getString("DATEDEBUT_EMPRUNTER");
                    String DateFin = this.result.getString("DATEFIN_EMPRUNTER");
                    String DateFinReel = this.result.getString("DATEFINREEL_EMPRUNTER");
                    emprunter = new Emprunter(idL, idF, DateDebut, DateFin, DateFinReel, null);
                    listEmprunts.add(emprunter);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listEmprunts;
    }
    
    //Sélectionner un emprunt
    // TEST ->
    public Emprunter trouverEmprunt(int id) {
        
        this.where = new ArrayList();
        this.where.add("id_location = " + id);

        try {

            this.result = dao.lister(this.emprunt, this.where) ; 
            if (result.first()) {

                int idL = this.result.getInt("ID_LOCATION");
                int idF = this.result.getInt("ID_FACTURE");
                String DateDebut = this.result.getString("DATEDEBUT_EMPRUNTER");
                String DateFin = this.result.getString("DATEFIN_EMPRUNTER");
                String DateFinReel = this.result.getString("DATEFINREEL_EMPRUNTER");
                this.emprunt = new Emprunter(idL, idF, DateDebut, DateFin, DateFinReel, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.emprunt;
    }
    
    //Ajout d'un emprunt
    // TEST ->
    public boolean ajouterEmprunt(Emprunter emp)
    {
        boolean Result = false;
        
        this.dao.ajouter(emp);
        Result = true;
        
        return Result;
    }
    
    
    //Modification d'un emprunt
    public boolean modifierEmprunt(Emprunter emp) {
        boolean Modifier = false;
       
        if (trouverEmprunt(emp.getId_location()) != null) {
            //requete Update  de l'exemplaire
            this.dao.modifier(emp);
            Modifier = true;
        }
        //retour de l'exemplaire modifié
        return Modifier;

    }
    
    //Supprimer un emprunt
    public boolean supprimerEmprunt(Emprunter emp) {
        
        boolean Result = false;
        if (trouverExemplaireLocation(emp.getId_location()) != null) {
            this.dao.supprimer(emp);
            Result = true;
        }
        
        return Result;
    }
    
    
    /*************** /EMPRUNT  ***************/

     
    
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
