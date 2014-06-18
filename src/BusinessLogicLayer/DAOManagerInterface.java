/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BusinessLogicLayer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edgar
 */
public interface DAOManagerInterface {
    
    public String select(ArrayList select, String table, ArrayList where);
    public String insert(String table, ArrayList values);
    public String update(ArrayList values, String table, ArrayList where);
    public String delete(String table, ArrayList where);
    /*public void create();
    public void drop();
    public void grant();*/
    
    
}
