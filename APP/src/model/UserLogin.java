/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.awt.Label;
import javax.naming.Context;
import layout.LoginForm;

/**
 *
 * @author desianggraenis
 */
public class UserLogin {
        
    public static String getUserNik(){
        return System.getProperty("userNik");
    }
    
    public static String getUserName(){
        return System.getProperty("userName");
    }
        
    public static String getUserStatus(){
        return System.getProperty("userStatus");
    }
        
    public static int getUserPosition(){
        return Integer.valueOf(System.getProperty("userPosId"));
    }
        
    public static String getUserPosDesc(){
        return System.getProperty("userPosDesc");
    }
        
    public static String getUserPassword(){
        return System.getProperty("userPassword");
    }
    
    public static void setUserLogin(Label userName, Label userPosition) {
        userName.setText(System.getProperty("userName"));
        userPosition.setText(System.getProperty("userPosDesc"));
    }
    
    public static void doLogout() {
        System.setProperty("userNik", "");                
        System.setProperty("userName", "");                
        System.setProperty("userStatus", "");                
        System.setProperty("userPosId", "");                
        System.setProperty("userPosDesc", "");
        System.setProperty("userPassword", "");
        //JOptionPane.showMessageDialog(null, LOGIN_SUCCEED);
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
           
    }
}
