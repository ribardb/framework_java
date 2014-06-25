/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import airpur.*;
import Tools.ConsoleReader;
import Tools.ImportXML;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edgar
 */
public class Menu {

    /**
     *
     */
    private AirPurManager apm;
    private ImportXML xml = new ImportXML("src/client/database.xml");
    private boolean verifMDP;
    private int choixMenu;

    public Menu() {
        try {
            this.apm = new AirPurManager();
            this.verifMDP = false;
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void afficherMenuPrincipal() throws Exception {

        if (!this.verifMDP) {
            verifierMotDePasse();
            this.verifMDP = true;
        }
        this.verifMDP = true;
        do {
            System.out.println("Menu principal");
            System.out.println("1  - Gestion des materiels");// TEST => OK
            System.out.println("2  - Gestion des exemplaires en location");
            System.out.println("3  - Gestion des exemplaires en vente");
            System.out.println("4  - Gestion des emprunts");
            System.out.println("5  - Gestion des sites");
            System.out.println("6  - Gestion des factures");
            System.out.println("7  - Gestion des paiements");
            System.out.println("8  - Gestion des modes de paiements");
            System.out.println("9  - Gestion des TVA");
            System.out.println("10 - Obtenir le montant HT d'une facture");// TEST => OK
            System.out.println("11 - Obtenir le montant TTC d'une facture");// TEST => OK
            System.out.println("12 - Obtenir le nombre d'exemplaires d'un materiel en location");// TEST => OK
            System.out.println("13 - Obtenir le nombre d'exemplaires d'un materiel en vente");// TEST => OK
            System.out.println("14 - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenu) {
                case 1:
                    menuMateriel();
                    break;
                case 2:
                    menuExemplaire_Vente();
                    break;
                case 3:
                    menuExemplaire_Vente();
                    break;
                case 4:
                    menuEmprunter();
                    break;
                case 5:
                    menuSite();
                    break;
                case 6:
                    menuFacture();
                    break;
                case 7:
                    menuPayer();
                    break;
                case 8:
                    menuModeDePaiement();
                    break;
                case 9:
                    menuTVA();
                    break;
                case 10:
                    menuTotalFactureHT();
                    break;
                case 11:
                    menuTotalFactureTTC();
                    break;
                case 12:
                    menuStockLocation();
                    break;
                case 13:
                    menuStockVente();
                    break;
                case 14:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Valeur incorrecte");
                    choixMenu = 0;

            }
        } while (choixMenu < 15);

    }

    public void verifierMotDePasse() {

        String mdpEntree;
        String mdpEnregistree = xml.getPsw();
        boolean mdpVerifie = false;

        do {
            mdpEntree = ConsoleReader.readString("Entrez le mot de passe: ");

            if (mdpEntree.equals(mdpEnregistree)) {
                System.out.println("Mot de passe accepte !");
                mdpVerifie = true;
            } else {
                System.out.println("Mot de passe incorrect ! Essayez a nouveau !");
            }
        } while (!mdpVerifie);

    }

    public void menuMateriel() {

        Materiel mat;
        int choixID;
        int choixTVA;
        int choixCategorie;
        String choixNom;
        String choixModele;
        String choixDescription;

        do {
            System.out.println("Menu Materiel");
            System.out.println("1  - Lister les materiels");
            System.out.println("2  - Trouver un materiel");
            System.out.println("3  - Ajouter un materiel");
            System.out.println("4  - Modifier un materiel");
            System.out.println("5  - Supprimer un materiel");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenu) {
                case 1:
                    for (Materiel listeMat : apm.listerMateriel()) {
                        System.out.println("ID du materiel : " + listeMat.getId_materiel());
                        System.out.println("ID de la TVA : " + listeMat.getId_tva());
                        System.out.println("ID de la categorie : " + listeMat.getId_categorie());
                        System.out.println("Nom du matériel : " + listeMat.getNom_materiel());
                        System.out.println("Modele du materiel : " + listeMat.getModele_materiel());
                        System.out.println("Description du matériel : " + listeMat.getDescription_materiel());
                        System.out.println("*****************************************************************");
                    }
                    break;
                case 2:
                    choixID = ConsoleReader.readInt("Entrez l'ID du materiel");
                    mat = null;
                    try {
                        mat = apm.trouverMateriel(choixID);
                    } catch (SQLException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("ID du materiel : " + mat.getId_materiel());
                    System.out.println("ID de la TVA : " + mat.getId_tva());
                    System.out.println("ID de la categorie : " + mat.getId_categorie());
                    System.out.println("Nom du matériel : " + mat.getNom_materiel());
                    System.out.println("Modele du materiel : " + mat.getModele_materiel());
                    System.out.println("Description du matériel : " + mat.getDescription_materiel());
                    break;
                case 3:
                    System.out.println("Ajout d'un materiel");
                    choixTVA = ConsoleReader.readInt("Entrez l'ID de la TVA");
                    choixCategorie = ConsoleReader.readInt("Entrez l'ID de la categorie");
                    choixNom = ConsoleReader.readString("Entrez le nom du materiel");
                    choixModele = ConsoleReader.readString("Entrez le modele du materiel");
                    choixDescription = ConsoleReader.readString("Entrez la description du materiel");
                    mat = new Materiel(0, choixTVA, choixCategorie, choixNom, choixModele, choixDescription);
                    this.apm.ajouterMateriel(mat);
                    System.out.println("Materiel ajoute");
                    break;
                case 4:
                    choixID = ConsoleReader.readInt("Entrez l'ID du materiel a modifier");
                    choixTVA = ConsoleReader.readInt("Entrez l'ID de la TVA");
                    choixCategorie = ConsoleReader.readInt("Entrez l'ID de la categorie");
                    choixNom = ConsoleReader.readString("Entrez le nom du materiel");
                    choixModele = ConsoleReader.readString("Entrez le modele du materiel");
                    choixDescription = ConsoleReader.readString("Entrez la description du materiel");
                    mat = new Materiel(choixID, choixTVA, choixCategorie, choixNom, choixModele, choixDescription);
                    this.apm.modifierMateriel(mat);
                    System.out.println("Materiel modifie");
                    break;
                case 5:
                    choixID = ConsoleReader.readInt("Entrez l'ID du materiel a supprimer");
                    mat = new Materiel(choixID, 0, 0, null, null, null);
                    this.apm.supprimerMateriel(mat);
                    System.out.println("Materiel supprimer");
                    break;
                case 6:
                    try {
                        afficherMenuPrincipal();
                    } catch (Exception ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 7:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Valeur incorrecte");
                    choixMenu = 0;

            }
        } while (choixMenu < 6);

    }

    public void menuExemplaire_Location() {

        Exemplaire_location exLoc;
        int choixID;
        int choixMateriel;
        int choixSite;
        float choixTarif;
        String choixEtat;
        do {
            System.out.println("Menu Exemplaire Location");
            System.out.println("1  - Lister les exemplaires en location");
            System.out.println("2  - Trouver un exemplaire en location");
            System.out.println("3  - Ajouter un exemplaire en location");
            System.out.println("4  - Modifier un exemplaire en location");
            System.out.println("5  - Supprimer un exemplaire en location");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenu) {
                case 1:
                    for (Exemplaire_location listeLocation : apm.listerExemplaireLocation()) {
                        System.out.println("ID du de l'exemplaire : " + listeLocation.getId_location());
                        System.out.println("ID du materiel : " + listeLocation.getId_materiel());
                        System.out.println("ID du site : " + listeLocation.getId_site());
                        System.out.println("Tarif de l'exemplaire : " + listeLocation.getTarif_location());
                        System.out.println("Etat de l'emprunt : " + listeLocation.getEtatemprunt_location());
                        System.out.println("*****************************************************************");
                    }
                    break;
                case 2:
                    choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire");
                    exLoc = null;
                    exLoc = apm.trouverExemplaireLocation(choixID);
                    System.out.println("ID du de l'exemplaire : " + exLoc.getId_location());
                    System.out.println("ID du materiel : " + exLoc.getId_materiel());
                    System.out.println("ID du site : " + exLoc.getId_site());
                    System.out.println("Tarif de l'exemplaire : " + exLoc.getTarif_location());
                    System.out.println("Etat de l'emprunt : " + exLoc.getEtatemprunt_location());
                    break;
                case 3:
                    System.out.println("Ajout d'un exemplaire en location");
                    choixMateriel = ConsoleReader.readInt("Entrez l'ID du materiel");
                    choixSite = ConsoleReader.readInt("Entrez l'ID du site");
                    choixTarif = ConsoleReader.readFloat("Entrez le tarif de l'exemplaire");
                    choixEtat = ConsoleReader.readString("Entrez l'état de l'exemplaire");
                    exLoc = new Exemplaire_location(0, choixMateriel, choixSite, choixTarif, choixEtat);
                    //this.apm.ajouterExemplaire_location(exLoc);
                    System.out.println("Exemplaire en location ajoute");
                    break;
                case 4:
                    System.out.println("Modification d'un exemplaire en location");
                    choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire a modifier");
                    choixMateriel = ConsoleReader.readInt("Entrez l'ID du materiel");
                    choixSite = ConsoleReader.readInt("Entrez l'ID du site");
                    choixTarif = ConsoleReader.readFloat("Entrez le tarif de l'exemplaire");
                    choixEtat = ConsoleReader.readString("Entrez l'état de l'exemplaire");
                    exLoc = new Exemplaire_location(choixID, choixMateriel, choixSite, choixTarif, choixEtat);
                    this.apm.modifierUnExemplaireLocation(exLoc);
                    System.out.println("Exemplaire en location modifie");
                    break;
                case 5:
                    System.out.println("Suppression d'un exemplaire en location");
                    choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire a supprimer :");
                    exLoc = new Exemplaire_location(choixID, 0, 0, 0, null);
                    this.apm.supprimerExemplaireLocation(exLoc);
                    System.out.println("Exemplaire en location supprime");
                    break;
                case 6:
                    try {
                        afficherMenuPrincipal();
                    } catch (Exception ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 7:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Valeur incorrecte");
                    choixMenu = 0;

            }
        } while (choixMenu < 6);

    }

    public void menuExemplaire_Vente() {

        Exemplaire_vente exVente;
        int choixID;
        int choixSite;
        int choixMateriel;
        int choixFacture;
        float choixTarif;
        float choixRemise;
        do {
            System.out.println("Menu Exemplaire Vente");
            System.out.println("1  - Lister les exemplaires en vente");
            System.out.println("2  - Trouver un exemplaire en vente");
            System.out.println("3  - Ajouter un exemplaire en vente");
            System.out.println("4  - Modifier un exemplaire en vente");
            System.out.println("5  - Supprimer un exemplaire en vente");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
            /*switch (choixMenu) {
             case 1:
             for (Exemplaire_vente listeVente : apm.listerExemplaireVente()) {
             System.out.println("ID de l'exemplaire : " + listeVente.getId_vente());
             System.out.println("ID du site : " + listeVente.getId_site());
             System.out.println("ID du materiel : " + listeVente.getId_materiel());
             System.out.println("ID de la facture : " + listeVente.getId_facture());
             System.out.println("Tarif de l'exemplaire : " + listeVente.getTarif_vente());
             System.out.println("Remise accordee a la vente : " + listeVente.getRemise_vente());
             System.out.println("*****************************************************************");
             }
             break;
             case 2:
             choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire");
             exVente = null;
             exVente = apm.trouverExemplaireVente(choixID);
             System.out.println("ID du de l'exemplaire : " + exVente.getId_vente());
             System.out.println("ID du site : " + exVente.getId_site());
             System.out.println("ID du materiel : " + exVente.getId_materiel());
             System.out.println("ID de la facture : " + exVente.getId_facture());
             System.out.println("Tarif de l'exemplaire : " + exVente.getTarif_vente());
             System.out.println("Remise accordee a la vente : " + exVente.getRemise_vente());
             break;
             case 3:
             System.out.println("Ajout d'un exemplaire en vente");
             choixMateriel = ConsoleReader.readInt("Entrez l'ID du materiel");
             choixSite = ConsoleReader.readInt("Entrez l'ID du site");
             choixFacture = ConsoleReader.readInt("Entrez l'ID de la facture");
             choixTarif = ConsoleReader.readFloat("Entrez le tarif de l'exemplaire");
             choixRemise = ConsoleReader.readFloat("Entrez la remise accordee a la vente");
             exVente = new Exemplaire_vente(0, choixMateriel, choixSite, choixFacture, choixTarif, choixRemise);
             this.apm.ajouterExemplaire_Vente(exVente);
             System.out.println("Exemplaire en vente ajoute");
             break;
             case 4:
             System.out.println("Modification d'un exemplaire en vente");
             choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire a modifier");
             choixMateriel = ConsoleReader.readInt("Entrez l'ID du materiel");
             choixSite = ConsoleReader.readInt("Entrez l'ID du site");
             choixFacture = ConsoleReader.readInt("Entrez l'ID de la facture");
             choixTarif = ConsoleReader.readFloat("Entrez le tarif de l'exemplaire");
             choixRemise = ConsoleReader.readFloat("Entrez la remise accordee a la vente");
             exVente = new Exemplaire_vente(choixID, choixMateriel, choixSite, choixFacture, choixTarif, choixRemise);
             this.apm.modifierUnExemplaireVente(exVente);
             System.out.println("Exemplaire en vente modifie");
             break;
             case 5:
             choixID = ConsoleReader.readInt("Entrez l'ID de l'exemplaire a supprimer :");
             exVente = new Exemplaire_vente(choixID, 0, 0, 0, null);
             this.apm.supprimerExemplaireVente(exVente);
             System.out.println("Exemplaire en Vente supprimer");
             break;
             case 6:
             try {
             afficherMenuPrincipal();
             } catch (Exception ex) {
             Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
             }
             break;
             case 7:
             System.exit(0);
             break;
             default:
             System.out.println("Valeur incorrecte");
             choixMenu = 0;
             }*/
        } while (choixMenu < 6);

    }

    public void menuEmprunter() {

        do {
            System.out.println("Menu Emprunt");
            System.out.println("1  - Lister les emprunts");
            System.out.println("2  - Trouver un emprunt");
            System.out.println("3  - Ajouter un emprunt");
            System.out.println("4  - Modifier un emprunt");
            System.out.println("5  - Supprimer un emprunt");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuSite() {

        do {
            System.out.println("Menu Site");
            System.out.println("1  - Lister les sites");
            System.out.println("2  - Trouver un site");
            System.out.println("3  - Ajouter un site");
            System.out.println("4  - Modifier un site");
            System.out.println("5  - Supprimer un site");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuFacture() {

        do {
            System.out.println("Menu Facture");
            System.out.println("1  - Lister les factures");
            System.out.println("2  - Trouver une facture");
            System.out.println("3  - Ajouter une facture");
            System.out.println("4  - Modifier une facture");
            System.out.println("5  - Supprimer une facture");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuModeDePaiement() {

        do {
            System.out.println("Menu Mode de Paiement");
            System.out.println("1  - Lister les modes de paiement");
            System.out.println("2  - Trouver un mode de paiement");
            System.out.println("3  - Ajouter un mode de paiement");
            System.out.println("4  - Modifier un mode de paiement");
            System.out.println("5  - Supprimer un mode de paiement");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuPayer() {

        do {
            System.out.println("Menu Paiement");
            System.out.println("1  - Lister les paiements");
            System.out.println("2  - Trouver un paiement");
            System.out.println("3  - Ajouter un paiement");
            System.out.println("4  - Modifier un paiement");
            System.out.println("5  - Supprimer un paiement");
            System.out.println("6  - Retour menu principal");
            System.out.println("7  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuTVA() throws SQLException {

        TVA tva;
        int choixID;
        float choixTaux;
        String choixDate;
        do {
            System.out.println("Menu TVA");
            System.out.println("1  - Lister les TVA valides");
            System.out.println("2  - Lister toutes les TVA");
            System.out.println("3  - Trouver une TVA");
            System.out.println("4  - Ajouter une TVA");
            System.out.println("5  - Modifier une TVA");
            System.out.println("6  - Supprimer une TVA");
            System.out.println("7  - Retour menu principal");
            System.out.println("8  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenu) {
                case 1:
                    for (TVA listeTVA : apm.listerTva(true)) {
                        System.out.println("ID de la TVA : " + listeTVA.getId_tva());
                        System.out.println("Taux de la TVA : " + listeTVA.getTaux_tva());
                        System.out.println("Date de fin de validité de la TVA : " + listeTVA.getDatefinvalidation_tva());
                        System.out.println("*****************************************************************");
                    }
                    break;
                case 2:
                    for (TVA listeTVA : apm.listerTva(true)) {
                        System.out.println("ID de la TVA : " + listeTVA.getId_tva());
                        System.out.println("Taux de la TVA : " + listeTVA.getTaux_tva());
                        System.out.println("Date de fin de validité de la TVA : " + listeTVA.getDatefinvalidation_tva());
                        System.out.println("*****************************************************************");
                    }
                    break;
                case 3:
                    choixID = ConsoleReader.readInt("Entrez l'ID de la TVA");
                    tva = null;
                    tva = apm.trouverTva(choixID);
                        System.out.println("ID de la TVA : " + tva.getId_tva());
                        System.out.println("Taux de la TVA : " + tva.getTaux_tva());
                        System.out.println("Date de fin de validité de la TVA : " + tva.getDatefinvalidation_tva());
                    break;
                case 4:
                    System.out.println("Ajout d'un exemplaire en vente");
                    choixTaux = ConsoleReader.readFloat("Entrez le taux de la TVA");
                    choixDate = ConsoleReader.readString("Entrez la date de fin de validation de la TVA");
                    tva = new TVA(0, choixTaux, choixDate);
                    this.apm.ajouterTVA(tva);
                    System.out.println("TVA ajoute");
                    break;
                case 5:
                    System.out.println("Modification d'un exemplaire en vente");
                    choixID = ConsoleReader.readInt("Entrez l'ID de la TVA a modifier");
                    choixTaux = ConsoleReader.readFloat("Entrez le taux de la TVA");
                    choixDate = ConsoleReader.readString("Entrez la date de fin de validation de la TVA");
                    tva = new TVA(0, choixTaux, choixDate);
                    //this.apm.modifierUneTVA(tva);
                    System.out.println("Exemplaire en vente modifie");
                    break;
                case 6:
                    choixID = ConsoleReader.readInt("Entrez l'ID de la a supprimer :");
                    tva = new TVA(choixID, 0, null);
                    //this.apm.supprimerTVA(tva);
                    System.out.println("TVA supprimer");
                    break;
                case 7:
                    try {
                        afficherMenuPrincipal();
                    } catch (Exception ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 8:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Valeur incorrecte");
                    choixMenu = 0;
            }
        } while (choixMenu < 6);

    }

    public void menuTotalFactureHT() {

        do {
            System.out.println("Menu Facture HT");
            choixMenu = ConsoleReader.readInt("Entrez l'ID de la facture");
            try {
                Float fact = this.apm.totalFactureHT(choixMenu);
                if (fact == 0.0) {
                    System.out.println("Facture inconnue !");

                }
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (choixMenu == 0);

    }

    public void menuTotalFactureTTC() {

        do {
            System.out.println("Menu Facture TTC");
            choixMenu = ConsoleReader.readInt("Entrez l'ID de la facture");
            try {
                System.out.println(this.apm.totalFactureVente(choixMenu) + "€");
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (choixMenu == 0);

    }

    public void menuStockLocation() {

        do {
            System.out.println("Menu Stock des exemplaires en location");
            choixMenu = ConsoleReader.readInt("Entrez l'ID du matériel");
            try {
                System.out.println(this.apm.totalStockLocation(choixMenu) + " exemplaires");
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (choixMenu == 0);

    }

    public void menuStockVente() {

        do {
            System.out.println("Menu Stock des exemplaires en vente");
            choixMenu = ConsoleReader.readInt("Entrez l'ID du matériel");
            try {
                System.out.println(this.apm.totalStockVente(choixMenu) + " exemplaires");
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (choixMenu == 0);

    }

}
