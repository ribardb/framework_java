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
    
    public String select(ArrayList<String> select, String table, ArrayList<String> where);
    public void update(Object table);
    public void delete(String table, ArrayList<String> where);
    public void insert(Object table);
    public void create();
    public void drop();
    public void grant();
    
    
}
