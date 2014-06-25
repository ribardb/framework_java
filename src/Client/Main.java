/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import BusinessLogicLayer.DAOManager;
import BusinessLogicLayer.DAOManager;
import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.text.Document;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Edgar
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        Menu menu = new Menu();
        menu.afficherMenuPrincipal();
        
         
        //AirPurManager ai = new AirPurManager();
        
        /*** TEST MATERIEL ***/
        //Materiel mat = new Materiel(121,2,46,"TEST INSERT","FFFFFF","AAAAAAA");
        //ai.supprimerMateriel(mat);
        //ArrayList<Materiel> listMateriel = ai.listerMateriel();
        //System.out.println(listMateriel.get(0).getDescription_materiel());
     
        //System.out.println(ai.trouverMateriel(41).getModele_materiel());
        
        /*** TEST PATIENT ***/
        //ai.ListerPatients();
        //System.out.println(ai.TrouverPatient(82).getNom_patient());
        //Partenaire_patient pat =  new Partenaire_patient (0, "Nom", "Prenom", "21/06/2014", "12345678", "Mail", "Password", "f", "AZERTY");
        //ai.AjouterPatient(pat);
        
        /*** Test Site OK***/
        
       /* for(Site site : ai.listerSite())
        {
            System.out.println("ID du site : "+ site.getId_site() );
             System.out.println("Nom du site : "+ site.getNom_site());
        
        }
        */
        //System.out.println(ai.trouverSite(41).getId_site());
       // Site site = ai.trouverSite(7);
       // System.out.println(ai.modifierSite(site));
        
        
        
    }

}
