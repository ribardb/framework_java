/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworkairpur;

import BusinessLogicLayer.DAOManager;
import Client.AirPurManager;
import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import airpur.Materiel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

        /*Menu menu = new Menu();
        menu.afficherMenuPrincipal();*/
        
        AirPurManager gestion = new AirPurManager();
        Materiel mat = new Materiel(51,2,47,"hibou","chouette","caca");
        Materiel mat2 = new Materiel(80,3,46,"hiboux","chouettes","cacas");
        gestion.ajoutMateriel(mat);
        gestion.modifierMateriel(mat2);
        
    }

}
 