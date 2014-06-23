/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Edgar
 */
public class ImportXML {

    private String typeDB;
    private String ip;
    private String port;
    private String base;
    private String login;
    private String psw;

    public ImportXML(String url) {

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.parse(new File(url));

            // normalize text representation
            doc.getDocumentElement().normalize();
            /*System.out.println("Root element of the doc is "
                    + doc.getDocumentElement().getNodeName());*/

            NodeList listDB = doc.getElementsByTagName("config");
            int totalDB = listDB.getLength();
            //System.out.println("Liste des DB : " + totalDB);

            for (int s = 0; s < listDB.getLength(); s++) {

                Node DataBaseNode = listDB.item(s);
                if (DataBaseNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element DataBaseElement = (Element) DataBaseNode;

                    //-------
                    NodeList databaseList = DataBaseElement.getElementsByTagName("database");
                    Element databaseElement = (Element) databaseList.item(0);

                    NodeList textDBList = databaseElement.getChildNodes();
                    /*System.out.println("Type de DB : "
                            + ((Node) textDBList.item(0)).getNodeValue().trim());*/
                    this.typeDB = ((Node) textDBList.item(0)).getNodeValue().trim();

                    //-------
                    NodeList ipList = DataBaseElement.getElementsByTagName("ip");
                    Element ipElement = (Element) ipList.item(0);

                    NodeList textIPList = ipElement.getChildNodes();
                    /*System.out.println("Adresse de la DB : "
                            + ((Node) textIPList.item(0)).getNodeValue().trim());*/
                    this.ip = ((Node) textIPList.item(0)).getNodeValue().trim();

                    //----
                    NodeList portList = DataBaseElement.getElementsByTagName("port");
                    Element portElement = (Element) portList.item(0);

                    NodeList textPortList = portElement.getChildNodes();
                    /*System.out.println("Port : "
                            + ((Node) textPortList.item(0)).getNodeValue().trim());*/
                    this.port = ((Node) textPortList.item(0)).getNodeValue().trim();

                    //----
                    NodeList baseList = DataBaseElement.getElementsByTagName("base");
                    Element baseElement = (Element) baseList.item(0);

                    NodeList textSid_BaseList = baseElement.getChildNodes();
                    /*System.out.println("Sid ou Base : "
                            + ((Node) textSid_BaseList.item(0)).getNodeValue().trim*/
                    this.base = ((Node) textSid_BaseList.item(0)).getNodeValue().trim();

                    //----
                    NodeList loginList = DataBaseElement.getElementsByTagName("login");
                    Element loginElement = (Element) loginList.item(0);

                    NodeList textLoginList = loginElement.getChildNodes();
                    /*System.out.println("Login : "
                            + ((Node) textLoginList.item(0)).getNodeValue().trim());*/
                    this.login = ((Node) textLoginList.item(0)).getNodeValue().trim();

                    //----
                    NodeList passwordList = DataBaseElement.getElementsByTagName("password");
                    Element passwordElement = (Element) passwordList.item(0);

                    NodeList textPasswordList = passwordElement.getChildNodes();
                    /*System.out.println("Psw : "
                            + ((Node) textPasswordList.item(0)).getNodeValue().trim());*/
                    this.psw = ((Node) textPasswordList.item(0)).getNodeValue().trim();

                    //------
                }//end of if clause

            }//end of for loop with s var

        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        //System.exit (0);

    }

    public String getTypeDB() {
        return typeDB;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getSid_base() {
        return base;
    }

    public String getLogin() {
        return login;
    }

    public String getPsw() {
        return psw;
    }

}
