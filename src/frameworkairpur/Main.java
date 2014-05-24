/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworkairpur;

import BusinessLogicLayer.DAOManager;
import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
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

        DAOManager dao = new DAOManager("src/frameworkairpur/database.xml") {};
        ArrayList tab = new ArrayList();
        String t = "re";
        String y = "ro";
        tab.add(t);
        tab.add(y);
        tab.add(t+1);
        tab.add(y+1);
        
        Iterator it = tab.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        System.out.println(tab);
        System.out.println(dao.select(tab,"bla",tab));
        System.out.println(dao.delete("blabl", tab));
        System.out.println(dao.insert(tab, "bla", tab));
                                
    }

}
