/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import static db.DailyWorkDAO.DAILY_WORK_TABLE;
import static db.DailyWorkDAO.DW_WORK_DAY;
import static db.LoginDAO.LOGIN_AS;
import static db.LoginDAO.LOGIN_PASSWORD;
import static db.LoginDAO.LOGIN_TABLE;
import static db.LoginDAO.LOGIN_USERNAME;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.DailyWorkModel;
import model.EmployeeModel;

/**
 *
 * @author desianggraenis
 */
public class EmployeeDAO extends DBHelper{
    public static final String EMPLOYEE_TABLE = "tb_m_employee";
    public static final String EMPLOYEE_NIK = "emp_nik";
    public static final String EMPLOYEE_NAME = "emp_name";
    public static final String EMPLOYEE_STATUS = "emp_status";
    public static final String EMPLOYEE_POSITION = "emp_position";
    public static final String EMPLOYEE_PASSWORD = "emp_password";
    public static final String[] KEY = {
        EMPLOYEE_NIK,
        EMPLOYEE_NAME,
        EMPLOYEE_STATUS,
        EMPLOYEE_POSITION,
        EMPLOYEE_PASSWORD};
    
    public ResultSet getAll(){
        ResultSet res = null;
        String sql = "select * from " + EMPLOYEE_TABLE ;
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
 
    public EmployeeModel[] getEmployee(String field, String value){
        EmployeeModel[] employeeModels = null;
        int index = 0;
        ResultSet res = null;
        String sql = "select * from " + EMPLOYEE_TABLE ;
        
        if(!value.isEmpty()){
            sql += " where " + field + " = '" + value + "'";
        }
        
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            
            if(!res.wasNull()){
                res.last();
                employeeModels = new EmployeeModel[res.getRow()];
                res.beforeFirst();
            
                while (res.next()) {
                    employeeModels[index] = new EmployeeModel(
                                        (String) res.getString(EMPLOYEE_NIK),
                                        (String) res.getString(EMPLOYEE_NAME),
                                        (String) res.getString(EMPLOYEE_STATUS),
                                        Integer.valueOf(res.getString(EMPLOYEE_POSITION)),
                                        (String) res.getString(EMPLOYEE_PASSWORD));
                    index++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return employeeModels;
        
    }
    
    public EmployeeModel getEmployeeByNik(String vNik){
        EmployeeModel employeeModel = null;
        ResultSet res = null;
        String sql = "select * from " + EMPLOYEE_TABLE + " where " + EMPLOYEE_NIK + " = '" + vNik +"'";

        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            if (res.next()) {
                employeeModel = new EmployeeModel(  (String) res.getString(EmployeeDAO.EMPLOYEE_NIK),
                        (String) res.getString(EmployeeDAO.EMPLOYEE_NAME),
                        (String) res.getString(EmployeeDAO.EMPLOYEE_STATUS),
                        Integer.valueOf((String)res.getString(EmployeeDAO.EMPLOYEE_POSITION)),
                        (String) res.getString(EmployeeDAO.EMPLOYEE_PASSWORD));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return employeeModel;
        
    }
    
    public ResultSet getEmployeeLogin(String nik, String password){
        ResultSet res = null;
        String sql = "select * from " + EMPLOYEE_TABLE 
                   + " where " + EMPLOYEE_NIK + " = '" + nik + "'"
                   + " and " + EMPLOYEE_PASSWORD + " = '" + password + "' ";
        
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
    
    public boolean deleteEmployee(EmployeeModel employeeModel){
        boolean res = false;
        
        String sql = "Delete From "+EMPLOYEE_TABLE
                 + " where " + EMPLOYEE_NIK + " = '" + employeeModel.getEmpNik() + "'";
        
        try {
            db.fExecute(sql);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public boolean addEmployee(EmployeeModel employeeModel){
        boolean res = false;
        
        String sql = "Insert into "+EMPLOYEE_TABLE+" (emp_nik, emp_name, emp_status, pos_id)"
                 + "values ('"+employeeModel.getEmpNik()+"', "
                 + "'"+employeeModel.getEmpName()+"', '"+employeeModel.getEmpStatus()+"', "
                 + ""+employeeModel.getEmpPosition()+")";
        
        try {
            db.fExecute(sql);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public boolean addOrUpdateEmployee(EmployeeModel employeeModel){
        boolean res = false;
        
        String[] VAL = {(String) employeeModel.getEmpNik(),
                        (String) employeeModel.getEmpName(),
                        (String) employeeModel.getEmpStatus(),
                         String.valueOf(employeeModel.getEmpPosition()),
                         (String) employeeModel.getEmpPasword(),
                       };
        System.out.println("val" + VAL);
        res = onDuplicateEmployee(KEY,VAL);
        
        return res;
    }
    
    public boolean onDuplicateEmployee(String[] iKey, String[] iVal){
        boolean res = false;
        
        String update = "",keys = "",vals = "",comma;
        int i=0;
        for (String key : iKey) {
            comma = (i==0)?"":",";
            
            if(!iVal[i].isEmpty()){
                update += comma +" "+ key + " = '" + iVal[i] + "'";
                keys += comma +" "+ key;
                vals += comma +" '"+ iVal[i] + "'";
            }
            i++;
        }
        
        if(update != null && keys != null && vals != null){
            String sql = "Insert into "+EMPLOYEE_TABLE+" (" + keys + ")"
                     + " values (" + vals + " ) "
                     + "ON DUPLICATE KEY UPDATE " + update;
            System.out.println("sql insert : " + sql);
            try {
                db.fExecute(sql);
                res = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
        
    }
}
