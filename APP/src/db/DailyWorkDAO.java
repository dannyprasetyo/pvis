/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import static db.DailyWorkDAO.DAILY_WORK_TABLE;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.ChartModel;
import model.DailyWorkModel;
import model.DailyWorkModel;

/**
 *
 * @author desianggraenis
 */
public class DailyWorkDAO extends DBHelper{
    public static final String DAILY_WORK_TABLE = "tb_t_daily_work";

    //public static final String DW_WORKID = "work_id";
    public static final String DW_EMP_NIK = "emp_nik";
    public static final String DW_WORK_DAY = "work_day";
    public static final String DW_WORK_HOURS = "work_hours";
    public static final String DW_SPESIFICATION = "spesification";
    public static final String DW_CLASSIFICATION = "classification";
    public static final String DW_LOT_NO = "lot_no";
    public static final String DW_MATERIAL_INPUT_QTY = "material_input_qty";
    public static final String DW_PROCESSED_QTY = "processed_qty";
    public static final String DW_FAULTY_QTY = "faulty_qty";
    public static final String DW_NOTE = "note";
    public static final String DW_CONFIRM_FLAG = "confirm_flag";
    public static final String DW_CONFIRM_DATE = "confirm_date";
    public static final String DW_CONFIRM_USER = "confirm_user";
    public static final String[] KEY = {
        //DW_WORKID,
        DW_EMP_NIK,
        DW_WORK_DAY,
        DW_WORK_HOURS,
        DW_SPESIFICATION,
        DW_CLASSIFICATION,
        DW_LOT_NO,
        DW_MATERIAL_INPUT_QTY,
        DW_PROCESSED_QTY,
        DW_FAULTY_QTY,
        DW_NOTE,
        DW_CONFIRM_FLAG,
        DW_CONFIRM_DATE,
        DW_CONFIRM_USER
    };
    
    public ResultSet getAll(){
        ResultSet res = null;
        String sql = "select * from " + DAILY_WORK_TABLE ;
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
    
    public boolean deleteDailyWork(DailyWorkModel dailyWorkModel){
        boolean res = false;
        
        String sql = "Delete From "+DAILY_WORK_TABLE
                 + " where " + DW_EMP_NIK + " = '" + dailyWorkModel.getEmpNik() + "'"
                 + " AND " + DW_WORK_DAY + " = '" + dailyWorkModel.getWorkDay() + "'";
        
        try {
            db.fExecute(sql);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
        
    public boolean addOrUpdateDailyWork(DailyWorkModel dailyWorkModel){
        boolean res = false;
        
        String[] VAL = {String.valueOf(dailyWorkModel.getEmpNik()),
                        String.valueOf(dailyWorkModel.getWorkDay()),
                        String.valueOf(dailyWorkModel.getWorkHours()),
                        String.valueOf(dailyWorkModel.getSpesification()),
                        String.valueOf(dailyWorkModel.getClassification()),
                        String.valueOf(dailyWorkModel.getLotNo()),
                        String.valueOf(dailyWorkModel.getMaterialQty()),
                        String.valueOf(dailyWorkModel.getProcessedQty()),
                        String.valueOf(dailyWorkModel.getFaultyQty()),
                        String.valueOf(dailyWorkModel.getNote()),
                        String.valueOf(dailyWorkModel.getConfirmFlag()),
                        String.valueOf(dailyWorkModel.getConfirmDate()),
                        String.valueOf(dailyWorkModel.getConfirmUser())
                       };
 
        res = onDuplicateDailyWork(KEY,VAL);
        
        return res;
    }
    
    public boolean onDuplicateDailyWork(String[] iKey, String[] iVal){
        boolean res = false;
        
        String update = "",keys = "",vals = "",comma;
        int i=0;
        for (String key : iKey) {
            comma = (i==0)?"":",";

            update += comma +" "+ key + " = '" + iVal[i] + "'";
            keys += comma +" "+ key;
            vals += comma +" '"+ iVal[i] + "'";
            
            i++;
        }
        
        if(update != null && keys != null && vals != null){
            String sql = "Insert into "+DAILY_WORK_TABLE+" (" + keys + ")"
                     + " values (" + vals + " ) "
                     + " ON DUPLICATE KEY UPDATE " + update;
            System.out.println("sql : " + sql);
            try {
                db.fExecute(sql);
                res = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
        
    }
    
    public int getWorkHours(String nik, String dateStart, String dateEnd){
        ResultSet res = null;
        int hours = 0;
        String sql = "select sum(work_hours) as hours from " + DAILY_WORK_TABLE + " where " + DW_EMP_NIK + " = " + nik + " and "

                + "" + DW_WORK_DAY + " >= '" + dateStart + "' and " + DW_WORK_DAY + " <= '" + dateEnd + "' and " + DW_CONFIRM_FLAG + " = 1";
        
        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally{
            try {
                    if(res.wasNull()) 
                        hours = 0;
                    else 
                        while(res.next())
                            hours = Integer.parseInt(res.getString("hours"));
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return hours;
    }
    
    public DailyWorkModel[] getDailyWorkByField(String dateStart, String dateEnd, String field, String value, Boolean cfm){
        DailyWorkModel[] dailyWorkModel = null;
        int index = 0;
        ResultSet res = null;
        String sql = "select * from " + DAILY_WORK_TABLE + " where 1 ";
            
        if(!dateStart.isEmpty() && !dateEnd.isEmpty())
            sql += " and " + DW_WORK_DAY + " >= '" + dateStart + "' and " + DW_WORK_DAY + " <= '" + dateEnd + "'";
            
        System.out.println("field :" + field);
        System.out.println("value :" + value);
        
        if(!field.isEmpty() && !value.isEmpty())
            sql += " and " + field + " = '" + value +"' ";
            
        if(cfm == true)
            sql += " and " + DW_CONFIRM_FLAG + " = 1 ";
        
        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            
            if(!res.wasNull()){
                res.last();
                dailyWorkModel = new DailyWorkModel[res.getRow()];
                res.beforeFirst();
            
                while (res.next()) {
                    dailyWorkModel[index] = new DailyWorkModel( 
                                        (String) res.getString(DailyWorkDAO.DW_EMP_NIK),
                                        (String) res.getString(DailyWorkDAO.DW_WORK_DAY),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_WORK_HOURS)),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_SPESIFICATION)),
                                        (String) res.getString(DailyWorkDAO.DW_CLASSIFICATION),
                                        (String) res.getString(DailyWorkDAO.DW_LOT_NO),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_MATERIAL_INPUT_QTY)),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_PROCESSED_QTY)),
                                        Integer.valueOf( res.getString(DailyWorkDAO.DW_FAULTY_QTY)),
                                        (String) res.getString(DailyWorkDAO.DW_NOTE));
                    index++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dailyWorkModel;
        
    }
    
    
    public DailyWorkModel getDailyWorkByNikDay(String nik, String day){
        DailyWorkModel dailyWorkModel = null;
        ResultSet res = null;
        String sql = "select * from " + DAILY_WORK_TABLE + " where " + DW_EMP_NIK + " = '" + nik +"' and " + DW_WORK_DAY + " = '" + day + "'";

        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            if (res.next()) {
                dailyWorkModel = new DailyWorkModel( 
                                        (String) res.getString(DailyWorkDAO.DW_EMP_NIK),
                                        (String) res.getString(DailyWorkDAO.DW_WORK_DAY),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_WORK_HOURS)),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_SPESIFICATION)),
                                        (String) res.getString(DailyWorkDAO.DW_CLASSIFICATION),
                                        (String) res.getString(DailyWorkDAO.DW_LOT_NO),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_MATERIAL_INPUT_QTY)),
                                        Integer.valueOf(res.getString(DailyWorkDAO.DW_PROCESSED_QTY)),
                                        Integer.valueOf( res.getString(DailyWorkDAO.DW_FAULTY_QTY)),
                                        (String) res.getString(DailyWorkDAO.DW_NOTE),
                                        Integer.valueOf( res.getString(DailyWorkDAO.DW_CONFIRM_FLAG)),
                                        (String) res.getString(DailyWorkDAO.DW_CONFIRM_DATE),
                                        (String) res.getString(DailyWorkDAO.DW_CONFIRM_USER));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dailyWorkModel;
        
    }
    
    public ResultSet getYear(){
       
        ResultSet res = null;
        String sql = "select distinct(date_format(work_day,'%Y')) year from " + DAILY_WORK_TABLE + " order by 1 desc";
        
        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //System.out.println("daily work array : " + dailyWorkModel);
        return res;
        
    }
    
    
    public ChartModel[] getProductionQuantityByYear(String year){
        ChartModel[] chartModels = null;
        int index = 0;
        ResultSet res = null;
        String sql = "select date_format(work_day,'%m') monthid, date_format(work_day,'%M') month, sum(material_input_qty) miq, sum(processed_qty) pq, sum(faulty_qty) fq " 
                + " from " + DAILY_WORK_TABLE 
                + " where date_format(" + DW_WORK_DAY + ",'%Y') = '" + year + "'"
                + " and " + DW_CONFIRM_FLAG + " = 1 "
                + " group by month "
                + " order by monthid asc"; 
        
        System.out.println(sql);
        try {
            Statement stmt = con.createStatement(); 
            res = stmt.executeQuery(sql);
            
            if(!res.wasNull()){
                res.last();
                chartModels = new ChartModel[res.getRow()];
                res.beforeFirst();
            
                while (res.next()) {
                    chartModels[index] = new ChartModel( 
                                        (String) res.getString("month"),
                                        Integer.valueOf(res.getString("miq")),
                                        Integer.valueOf(res.getString("pq")),
                                        Integer.valueOf(res.getString("fq")));
                    //System.out.println("dailyWorkModel ke : "+ index + " - " + dailyWorkModel);
                    index++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //System.out.println("daily work array : " + dailyWorkModel);
        return chartModels;
        
    }
    
}
