/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataAccessLayer;

import java.util.ArrayList;

/**
 *
 * @author Edgar
 */
public interface DAOManagerInterface {
    
    public ArrayList<String> select(ArrayList<String> param);
    public void update(Object table);
    public void delete(Object table);
    public void insert(Object table);
    public void create();
    public void drop();
    public void grant();
    
    
}
