/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author desianggraenis
 */
public class LoginModel {
    private String username;
    private String password;
    private int loginAs;
    
    public LoginModel(String username, String password, int loginAs){
        this.username = username;
        this.password = password;
        this.loginAs = loginAs;
    }
    
    public LoginModel(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setLoginAs(int loginAs){
        this.loginAs = loginAs;
    }
    
    public int getLoginAs(){
        return this.loginAs;
    }
}
