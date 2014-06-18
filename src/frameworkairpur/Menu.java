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

    public Menu() {
        try {
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void afficherMenuPrincipal() throws Exception {
        
        int choixMenuPrincipal;
        do {
            System.out.println("Menu principal");
            System.out.println("1 - ");
            System.out.println("2 - ");
            System.out.println("3 - ");
            System.out.println("4 - ");
            System.out.println("5 - ");
            System.out.println("6 - ");
            System.out.println("7 - ");
            System.out.println("8 - Quitter");
            choixMenuPrincipal = ConsoleReader.readInt("Quel est votre choix ?");
            switch (choixMenuPrincipal) {
                case 1:
                    System.out.println("coucou");
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
        } while (choixMenuPrincipal != 8);
        
        dao.disconnect();
        
    }
    
}
