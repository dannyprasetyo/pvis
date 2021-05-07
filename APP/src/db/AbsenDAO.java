/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.AbsenModel;

/**
 *
 * @author Danny
 */
public class AbsenDAO extends DBHelper{
    public static final String ABSEN_TABLE = "tb_t_attendance";
    
    public static final String AB_ATT_ID = "att_id";
    public static final String AB_EMP_NIK = "emp_nik";
    public static final String AB_ATT_DATE = "att_date";
    public static final String AB_ATT_IN = "att_in";
    public static final String AB_ATT_OUT = "att_out";
    public static final String[] KEY ={
        AB_ATT_ID,
        AB_EMP_NIK,
        AB_ATT_DATE,
        AB_ATT_IN,
        AB_ATT_OUT
    };
    
    public AbsenModel[] getAbsen(String field, String value, String nik){
        AbsenModel[] absenModels = null;
        int index = 0;
        ResultSet res = null;
//        String sql = "select * from " + ABSEN_TABLE;
//        
//        if(!value.isEmpty()){
//            sql += " where " + field + " = '" + value + "'";
//        }
        
        String sql = "select * from " + ABSEN_TABLE + " where emp_nik = '" + nik + "'";
        
        System.out.println("kambing:" + sql);
        
        if(!value.isEmpty()){
            sql += " and " + field + " = '" + value + "'";
        }
        
        sql += "ORDER BY att_date DESC";
//        String sql = "select * from " + ABSEN_TABLE + " where emp_nik = '" + absenModel.getEmpNik() 
//                                        + "' and att_date = '" + absenModel.getAttDate() + "'";
        try{
            Statement stmt = con.createStatement();
            res = stmt.executeQuery(sql);
            
            if(!res.wasNull()){
                res.last();
                absenModels =  new AbsenModel[res.getRow()];
                res.beforeFirst();
                
                while(res.next()){
                    absenModels[index] = new AbsenModel(
                                            (String) res.getString(AB_EMP_NIK),
                                            (String) res.getString(AB_ATT_DATE),
                                            (String) res.getString(AB_ATT_IN),
                                            (String) res.getString(AB_ATT_OUT));
                    index++;
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return absenModels;
    }
    
    public ResultSet getAbsenPegawai(String nik, String tanggal){
        ResultSet res = null;
        String sql = "select * from " + ABSEN_TABLE + " where emp_nik = '" + nik 
                        + "' and att_date = '" + tanggal + "'";
        System.out.println("HAHAHAA : " + sql);
        try{
            Statement stmt = con.createStatement();
            res = stmt.executeQuery(sql);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return res;
    }
    
    public boolean addAbsen(AbsenModel absenModel){
        boolean res = false;
        ResultSet hasil = null;

        String sql1 = "select * from " + ABSEN_TABLE + " where emp_nik = '" + absenModel.getEmpNik() 
                                        +"' and att_date = '" + absenModel.getAttDate() + "'";
        
        try{
            Statement stmt = con.createStatement(); 
            hasil = stmt.executeQuery(sql1);
            hasil.last();
//            System.out.println(hasil.getRow()+"kambing");
            if (hasil.getRow() > 0){
                String sql = "update " + ABSEN_TABLE + " set att_out = '" + absenModel.getAttOut() 
                                        + "' where emp_nik = '" + absenModel.getEmpNik() 
                                        + "' and att_date = '" + absenModel.getAttDate() + "'" ;
                System.out.println("update : " + sql);
//                System.out.println(sql);
                try{
                    db.fExecute(sql);
                    res = true;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("insert");
                String sql = "insert into " + ABSEN_TABLE + "(emp_nik, att_date, att_in, att_out)"
                    + "values ('" + absenModel.getEmpNik() + "', '"
                    + absenModel.getAttDate() + "', '" + absenModel.getAttIn() + "', " + null + ")"
                    ;
                try{
                    db.fExecute(sql);
                    res = true;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }

        return res;
    }
    
    
}
