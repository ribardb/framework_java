/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworkairpur;

import BusinessLogicLayer.DAOManager;
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
    public DAOManager dao;
    private ImportXML xml = new ImportXML("src/frameworkairpur/database.xml");

    public Menu() {
        try {
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void afficherMenuPrincipal() throws Exception {

        verifierMotDePasse();

        int choixMenuPrincipal;
        do {
            System.out.println("Menu principal");
            System.out.println("1  - Gestion des materiels");
            System.out.println("2  - Gestion des exemplaires en location");
            System.out.println("3  - Gestion des exemplaires en vente");
            System.out.println("4  - Gestion des emprunts");
            System.out.println("5  - Gestion des sites");
            System.out.println("6  - Gestion des factures");
            System.out.println("7  - Gestion des paiements");
            System.out.println("8  - Gestion des modes de paiements");
            System.out.println("9  - Gestion des TVA");
            System.out.println("10 - Obtenir le montant HT d'une facture");
            System.out.println("11 - Obtenir le montant TTC d'une facture");
            System.out.println("12 - Obtenir le nombre d'exemplaires d'un materiel en location");
            System.out.println("13 - Obtenir le nombre d'exemplaires d'un materiel en vente");
            System.out.println("14 - Quitter");
            choixMenuPrincipal = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenuPrincipal) {
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
                    ;
                    break;
                case 12:
                    ;
                    break;
                case 13:
                    ;
                    break;
                default:
                    System.out.println("Valeur incorrecte");

            }
        } while (choixMenuPrincipal < 14);

        dao.disconnect();

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

        int choixMenu;
        do {
            System.out.println("Menu Materiel");
            System.out.println("1  - Lister les materiels");
            System.out.println("2  - Trouver un materiel");
            System.out.println("3  - Ajouter un materiel");
            System.out.println("4  - Modifier un materiel");
            System.out.println("5  - Supprimer un materiel");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuExemplaire_Location() {

        int choixMenu;
        do {
            System.out.println("Menu Exemplaire Location");
            System.out.println("1  - Lister les exemplaires en location");
            System.out.println("2  - Trouver un exemplaire en location");
            System.out.println("3  - Ajouter un exemplaire en location");
            System.out.println("4  - Modifier un exemplaire en location");
            System.out.println("5  - Supprimer un exemplaire en location");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuExemplaire_Vente() {

        int choixMenu;
        do {
            System.out.println("Menu Exemplaire Vente");
            System.out.println("1  - Lister les exemplaires en vente");
            System.out.println("2  - Trouver un exemplaire en vente");
            System.out.println("3  - Ajouter un exemplaire en vente");
            System.out.println("4  - Modifier un exemplaire en vente");
            System.out.println("5  - Supprimer un exemplaire en vente");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuEmprunter() {

        int choixMenu;
        do {
            System.out.println("Menu Emprunt");
            System.out.println("1  - Lister les emprunts");
            System.out.println("2  - Trouver un emprunt");
            System.out.println("3  - Ajouter un emprunt");
            System.out.println("4  - Modifier un emprunt");
            System.out.println("5  - Supprimer un emprunt");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuSite() {

        int choixMenu;
        do {
            System.out.println("Menu Site");
            System.out.println("1  - Lister les sites");
            System.out.println("2  - Trouver un site");
            System.out.println("3  - Ajouter un site");
            System.out.println("4  - Modifier un site");
            System.out.println("5  - Supprimer un site");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuFacture() {

        int choixMenu;
        do {
            System.out.println("Menu Facture");
            System.out.println("1  - Lister les factures");
            System.out.println("2  - Trouver une facture");
            System.out.println("3  - Ajouter une facture");
            System.out.println("4  - Modifier une facture");
            System.out.println("5  - Supprimer une facture");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuModeDePaiement() {

        int choixMenu;
        do {
            System.out.println("Menu Mode de Paiement");
            System.out.println("1  - Lister les modes de paiement");
            System.out.println("2  - Trouver un mode de paiement");
            System.out.println("3  - Ajouter un mode de paiement");
            System.out.println("4  - Modifier un mode de paiement");
            System.out.println("5  - Supprimer un mode de paiement");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuPayer() {

        int choixMenu;
        do {
            System.out.println("Menu Paiement");
            System.out.println("1  - Lister les paiements");
            System.out.println("2  - Trouver un paiement");
            System.out.println("3  - Ajouter un paiement");
            System.out.println("4  - Modifier un paiement");
            System.out.println("5  - Supprimer un paiement");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuTVA() {

        int choixMenu;
        do {
            System.out.println("Menu TVA");
            System.out.println("1  - Lister les TVA");
            System.out.println("2  - Trouver une TVA");
            System.out.println("3  - Ajouter une TVA");
            System.out.println("4  - Modifier une TVA");
            System.out.println("5  - Supprimer une TVA");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);

    }

    public void menuTotalFactureHT() {

        int choixMenu = 0;
        do {
            System.out.println("Menu Facture HT");
            choixMenu = ConsoleReader.readInt("Entrez l'ID de la facture :");
        } while (choixMenu == 0);

    }

}
