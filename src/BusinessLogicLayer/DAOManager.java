/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogicLayer;

import DataAccessLayer.DatabaseConnection;
import DataAccessLayer.DatabaseConnectionMySQL;
import DataAccessLayer.DatabaseConnectionOracle;
import DataAccessLayer.DatabaseConnectionSQLServer;
import frameworkairpur.ImportXML;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edgar
 */
public class DAOManager implements DAOManagerInterface {

    DatabaseConnection con = null;
    String query;
    

    public DAOManager(String url) throws Exception {
        ImportXML xml = new ImportXML(url);

        switch (xml.getTypeDB()) {
            case "Oracle":
                this.con = new DatabaseConnectionOracle(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
            case "SQLServer":
                this.con = new DatabaseConnectionSQLServer(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
            case "MySQL":
                this.con = new DatabaseConnectionMySQL(xml.getLogin(), xml.getPsw(), xml.getIp(), xml.getPort(), xml.getSid_base());
                break;
        }
        this.con.getConnection();
    }

    /**
     *
     * @param select
     * @param table
     * @param where
     * @return
     */
    @Override
    public String select(ArrayList<String> select, String table, ArrayList<String> where) {
        
        if (where.isEmpty()) {
            this.query = "SELECT (" + select.toString().substring(1, select.toString().length()-1) + ") FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0 ; i < where.size() ; i+=2) {
                clause = clause + where.get(i) + "=" + where.get(i+1) + " AND ";
            }
            this.query = "SELECT (" + select.toString().substring(1, select.toString().length()-1) + ") FROM " + table
                    + " WHERE" + clause.substring(0, clause.length()-5) + ";";
        }
        return this.query;
    }

    @Override
    public void update(Object table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String table, ArrayList<String> where) {
        if (where.isEmpty()) {
            this.query = "DELETE FROM " + table;
        } else {
            String clause = " ";
            for (int i = 0 ; i < where.size() ; i+=2) {
                clause = clause + where.get(i) + "=" + where.get(i+1) + " AND ";
            }
            this.query = "DELETE FROM " + table + " WHERE" + clause.substring(0, clause.length()-5);
        }
    }

    @Override
    public void insert(Object table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void grant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
/*public void setRequeteAjouter(Object table)
	{
            ArrayList<String> temp = this.cast.getParamQuery(table); //récupération d'une liste(nom de la table | liste des 
                                                                                //attributs entre parenthèses
            query = "INSERT INTO " + temp.get(0);
            String attr = temp.get(1); //sliste les champs
            String values ="VALUES("; //sous chaîne pour lister les ? coorespondant aux VALUES
            ArrayList<String> champs = cast.getAttr(temp);
            for(int i = 0 ; i < champs.size() ; i++) {
                values = values + "?,";
            }
            values = values.substring(0,values.length()-1);// enlever la derniere virgule après le dernier ?
            values = values + ")"; //on ferme la parenthèse de VALUES
            this.query = query + attr + values; //on reconstitue la totalité du texte de la requête et on stocke le texte de la requête dans this.query
	}// fin de la méthode getRequeteAjouter()
	
	public void setRequeteModifier(Object table)
	{
		ArrayList<String> temp = this.cast.getParamQuery(table);
		String where = " WHERE "; //sous chaîne pour la clause WHERE
		$params = array();
		$id;
		String query ="UPDATE " + temp.get(0) + " SET "; //début de la requête
			
		foreach($proprietes as $key=>$val)
		{
			if($i!=0) //j'écarte l'identifiant des champs à modifier
			{
				$sql=$sql.$key."=?,"; //on ajoute un ?
				$params[]=$val;
			}
			else
			{
				$where = $where . $key . "=?";
				$id=$val;
			}
			$i++;
		}
		$params[] = $id; //on ajoute l’id à la fin du tableau des paramètres
		// enlever la derniere virgule après le dernier ?
		$sql = rtrim($sql, ",");
		$sql = $sql . $where; //on ajoute la clause WHERE à la fin de la requête
		$this->requete = $sql; //on stocke le texte de la requête dans $this->requete
		$this->parametres = $params;//on stocke les valeurs des paramètres dans $this->parametres	
	}

	public function setRequeteSupprimer($classe, $primary, $valueid)//valable pour toutes les classes
	{
		$sql="DELETE FROM ".$classe."s WHERE ". $primary." ='".$valueid."'";//table=classe + "s"
		$this->requete = $sql;
	}
	
	public function setPSAjouter($obj)
	{
		//exemple ps = ajouterClient -> ajouter + nom de la classe
		$nomps=self::$_appel." ajouter". get_class($obj). " " .substr(self::$_syntaxe,0,1);//Appel et nom de la procedure stockée
		$proprietes=$obj->getproprietes();
		try
		{
			$i=0;
			$params = array();
			
			foreach($proprietes as $key=>$val)
			{	
				if($key!="id")
				{
					$nomps = $nomps. " ?,";
					$params[] = $val;
				}
			}
			$nomps = rtrim($nomps, ",");// enlever la derniere virgule
			$nomps = $nomps . substr(self::$_syntaxe,1,1);
			//echo $nomps;
			//print_r($params);
			$this->requete = $nomps;
			$this->parametres = $params;
		}
		catch (PDOException $e)
		{
			echo 'Error: ', $e->__toString();
		}
	}
	
	public function setPSModifier($obj)
	{
		//exemple ps = modifierClient
		$nomps=self::$_appel." modifier". get_class($obj). " " . substr(self::$_syntaxe,0,1);//Appel et nom de la procedure stockée
		$proprietes=$obj->getproprietes();
		try
		{
			$i=0;
			$params = array();
			foreach($proprietes as $key=>$val)
			{	
				$nomps = $nomps. "?,";
				$params[] = $val;
			}
			$nomps = rtrim($nomps, ",");// enlever la derniere virgule
			$nomps = $nomps . substr(self::$_syntaxe,1,1);
			//echo $nomps;
			//print_r($params);
			$this->requete = $nomps;
			$this->parametres = array();
			$this->parametres = $params;
		}
		catch (PDOException $e)
		{
			echo 'Error: ', $e->__toString();
		}
	}
	
	public function setPSSupprimer($classe, $valueid)//valable pour toutes les classes
	{
		$this->parametres = array();
		//exemple ps = supprimerClient
		$nomps=self::$_appel." supprimer". $classe. " " . substr(self::$_syntaxe,0,1)."?". substr(self::$_syntaxe,1,1);//Appel et nom de la procedure stockée
		$this->requete = $nomps;
		echo $nomps;
		$this->parametres[] = $valueid;
		print_r($this->parametres);
	}
	
	public function setPSLister($classe, $valueid=null)//valable pour toutes les classes
	{
		$this->parametres = array();
		$nomps;
		//exemple ps = listerClient
		$nomps=self::$_appel." lister". $classe  . " " .substr(self::$_syntaxe,0,1)."?". substr(self::$_syntaxe,1,1);//Appel et nom de la procedure stockée
		if($valueid==null)
		{
			$this->parametres[] =0;
		}
		else
		{				
			
			$this->parametres[] = $valueid; //si null ps doit en tenir compte en créant 2 SELECT
		}
		
		$this->requete = $nomps;
		//echo $nomps;
		
	}
	
	
	public function getRequete()
	{
		return $this->requete;
	}
	
	public function getParametres()
	{
		return $this->parametres;
	}
*/