/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Danny
 */
public class AbsenModel {
    private String attId;
    private String empNik;
    private String attDate;
    private String attIn;
    private String attOut;
    
    public AbsenModel(String pId, String pNik, String iDate, String iIn, String iOut){
        this.attId = pId;
        this.empNik = pNik;
        this.attDate = iDate;
        this.attIn = iIn;
        this.attOut = iOut;
    }
    
    public AbsenModel(String pNik, String iDate, String iIn, String iOut){
        
        this.empNik = pNik;
        this.attDate = iDate;
        this.attIn = iIn;
        this.attOut = iOut;
    }
    
    public AbsenModel(){
        
    }
    
    public void setAttId(String id){
        this.attId = id;
    }
    
    public String getAttId(){
        return this.attId;
    } 
    
    public void setEmpNik(String nik){
        this.empNik = nik;
    }
    
    public String getEmpNik(){
        return this.empNik;
    }    
    
    public void setAttDate(String date){
        this.attDate = date;
    }
    
    public String getAttDate(){
        return this.attDate;
    }
    
    public void setAttIn(String date){
        this.attIn = date;
    }
    
    public String getAttIn(){
        return this.attIn;
    }
    
    public void setAttOut(String date){
        this.attOut = date;
    }
    
    public String getAttOut(){
        return this.attOut;
    }
}
