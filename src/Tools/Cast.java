/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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
    String cast7 = "class ";
    String castTMP;

    public String getTable(Object obj) {
        String tab;
        tab = obj.getClass().toString();
        tab = tab.replaceAll(cast6, "");
        tab = tab.replaceAll(cast7, "");
        //System.out.println(tab);
        return tab;
    }

    public ArrayList getAttr(Object obj) {
        ArrayList listeAttr = new ArrayList();
        String attr;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            attr = fields[i].toString();
            attr = attr.replaceAll(cast1, "");
            attr = attr.replaceAll(cast2, "");
            attr = attr.replaceAll(cast3, "");
            attr = attr.replaceAll(cast4, "");
            attr = attr.replaceAll(cast5, "");
            attr = attr.replaceAll(cast6, "");
            attr = attr.replaceAll("\\]", ")");
            attr = attr.replaceAll("\\[", "(");
            int tp = attr.indexOf(".");
            tab = attr.substring(0, tp);
            castTMP = tab + ".";
            //System.out.println(tab);
            attr = attr.replaceAll(castTMP, "");
            //System.out.println(attr);
            listeAttr.add(attr);
        }

        return listeAttr;
    }

    public String getType(Object obj) {
        String type;
        type = obj.getClass().toString();
        type = type.replaceAll(cast2, "");
        type = type.replaceAll(cast7, "");
        return type;
    }

    public ArrayList<String> getTypeAttr(Object obj) {
        ArrayList<String> listeTypeAttr = new ArrayList<String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //System.out.println(fields[i].toString());
            if (fields[i].toString().contains("int")) {
                listeTypeAttr.add("int");
            } else if (fields[i].toString().contains("String")) {
                listeTypeAttr.add("String");
            } else {
                listeTypeAttr.add("float");
            }

        }
        //System.out.println(listeTypeAttr);
        return listeTypeAttr;
    }

    public ArrayList<Method> getGetters(Object obj) {
        Cast cast = new Cast();
        ArrayList listeAttr = cast.getAttr(obj);
        ArrayList<Method> listeMethod = new ArrayList<Method>();
        Method method = null;
        String attr = null;
        for (int i = 0; i < listeAttr.size(); i++) {
            attr = listeAttr.get(i).toString().substring(0, 1);
            attr = attr.toUpperCase();
            attr = attr.concat(listeAttr.get(i).toString().substring(1, listeAttr.get(i).toString().length()));
            //System.out.println(attr);
            try {
                method = obj.getClass().getMethod("get".concat(attr));
                listeMethod.add(method);
            } catch (NoSuchMethodException e) {
                System.out.println("No methods acquired");
            }
        }

        //System.out.println(listeMethod);

        return listeMethod;

    }
}
