/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DBConnection;

/**
 *
 * @author desianggraenis
 */
public class LoginDAO extends DBHelper{
    
    public static final String LOGIN_TABLE = "tb_m_login";
    public static final String LOGIN_USERNAME = "USERNAME";
    public static final String LOGIN_PASSWORD = "PASSWORD";
    public static final String LOGIN_AS = "LOGIN_AS";

    public ResultSet getUserLogin(String username, String password){
        ResultSet res = null;
        String sql = "select * from " + LOGIN_TABLE + " where " + LOGIN_USERNAME + " = '" + username + "' and " 
                + LOGIN_PASSWORD + " = '" + password + "' ";
        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally{
             return res;
        }
        
    }
}
