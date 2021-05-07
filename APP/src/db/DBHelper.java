/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import utils.DBConnection;

/**
 *
 * @author desianggraenis
 */
public class DBHelper {
    public Connection con;
    DBConnection db = new DBConnection();
    
    public DBHelper(){
        this.con = db.getConnection();
    }
}
