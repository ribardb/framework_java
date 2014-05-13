/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frameworkairpur;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Edgar
 */
public class Cast {
    
    private String tab;
    private String attr;
    String cast1 = "private ";
    String cast2 = "java.lang.";
    String cast3 = "int ";
    String cast4 = "String ";
    String cast5 = "float ";
    String cast6 = "airpur.";
    String cast7;

    public Cast() {
        
    }
    
    public ArrayList<String> getParamQuery(Object obj) {
        ArrayList<String> result = new ArrayList();
        attr = Arrays.toString(obj.getClass().getDeclaredFields());
        attr = attr.replaceAll(cast1,"");
        attr = attr.replaceAll(cast2,"");
        attr = attr.replaceAll(cast3,"");
        attr = attr.replaceAll(cast4,"");
        attr = attr.replaceAll(cast5,"");
        attr = attr.replaceAll(cast6,"");
        attr = attr.replaceAll("\\]",")");
        attr = attr.replaceAll("\\[","(");
        int tp = attr.indexOf(".");
        tab = attr.substring(1, tp);
        cast7 = tab + ".";
        //System.out.println(tab);
        attr = attr.replaceAll(cast7,"");
        result.add(tab);
        result.add(attr);
        
        return result;
    }
    
    public ArrayList<String> getAttr(ArrayList<String> paramQuery) {//Passer en paramètre le retour de getParamQuery
        ArrayList<String> listeAttr = new ArrayList();
        String temp = paramQuery.get(1);//contient le déclaratif des attributes de la classe
        ArrayList<Integer> emplacement = new ArrayList();//contient la position des ,
        //Définition de l'emplacement des ";"
	for (int a = 0; a < temp.length(); a++) {
            if ((temp.substring(a, a+1)).equalsIgnoreCase(",")) {
		emplacement.add(a);	
            }
	}
        listeAttr.add(temp.substring(1,emplacement.get(0)));
        //System.out.println(listeAttr);
        int i = 1;
        for (i = 1 ; i < emplacement.size() - 1 ; i++) {
            listeAttr.add(temp.substring(emplacement.get(i)+2,emplacement.get(i+1)));
            //System.out.println(listeAttr);
        }
        listeAttr.add(temp.substring(emplacement.get(i)+2,temp.length()-1));
        
        return listeAttr;
    }
    
}
