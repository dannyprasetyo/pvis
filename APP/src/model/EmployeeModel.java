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
public class EmployeeModel {
    
    public static final String STATUS_TETAP = "1";
    public static final String STATUS_KONTRAK = "0";
    
    private String empNik;
    private String empName;
    private String empStatus;
    private int empPosition;
    private String empPassword;
    public static final String [] EmployeePosition = {
        "Operator Produksi",
        "Manager",
        "Keuangan",
        "Direktur"
    };
    
    
    public EmployeeModel(String pNik, String iName, String iStatus, int iPos){
        this.empNik = pNik;
        this.empName = iName;
        this.empStatus = iStatus;
        this.empPosition = iPos;
    }
    
    
    public EmployeeModel(String pNik, String iName, String iStatus, int iPos, String iPassword){
        this.empNik = pNik;
        this.empName = iName;
        this.empStatus = iStatus;
        this.empPosition = iPos;
        this.empPassword = iPassword;
    }

    public EmployeeModel() {
        
    }

    public void setEmpNik(String nik){
        this.empNik = nik;
    }
    
    public String getEmpNik(){
        return this.empNik;
    }
    
    public void setEmpName(String name){
        this.empName = name;
    }
    
    public String getEmpName(){
        return this.empName;
    }
    
    public void setEmpStatus(String status){
        this.empStatus = status;
    }
    
    public String getEmpStatus(){
        return this.empStatus;
    }
    
    public String getEmpStatusString(){
        String status = null;
        switch(this.empStatus){
            case STATUS_KONTRAK:
                status = "Contract";
               break;
            case STATUS_TETAP:
                status = "Permanent";
            default:
                break;
        }
        return status;
    }
        
    public static String getEmpStatusByName(String name){
        String res = null;
        switch(name){
            case "Tetap":
                res = "0";
                break;
            case "Kontrak":
                res = "1";
                break;
            default:
                break;
        }
         return res;
    }

     public String getEmpPositionString(){
        return EmployeePosition[this.getEmpPosition()]; 
    }

//    public static String getEmpStatusByName(String name){
//        String res = null;
//        switch(name){
//            case "Tetap":
//                res = "0";
//                break;
//            case "Kontrak":
//                res = "1";
//                break;
//            default:
//                break;
//        }
//         return res;
//    }
//    
//    public void setEmpPosition(int position){
//        this.empPosition = position;
//    }
    
    
    public int getEmpPosition(){
        return this.empPosition;
    }
    
//    public static int getEmpPositionByName(String name){
//        int pos = 99;
//        switch(name){
//            case "Operator Produksi":
//                pos = 0;
//                break;
//            
//            case "Manager":
//                pos = 1;
//                break;
//            
//            case "Keuangan":
//                pos = 2;
//                break;
//            
//            case "Direktur":
//                pos = 3;
//                break;
//            
//            default:
//                break;
//        }
//        
//        return pos;
//    }
    
    public void setEmpPassword(String password){
        this.empPassword = password;
    }
    
    public String getEmpPasword(){
        return this.empPassword;
    }
}
