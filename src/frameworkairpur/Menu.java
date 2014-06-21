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
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    System.out.println("Bye bye");
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
            System.out.println("2  - Lister un materiel");
            System.out.println("3  - Ajouter un materiel");
            System.out.println("4  - Modifier un materiel");
            System.out.println("5  - Supprimer un materiel");
            System.out.println("6  - Quitter");
            choixMenu = ConsoleReader.readInt("Quel est votre choix ?");
        } while (choixMenu < 6);
        
    }
    
}
