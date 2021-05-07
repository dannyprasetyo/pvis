/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import static db.EmployeeDAO.EMPLOYEE_TABLE;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.SalaryModel;

/**
 *
 * @author desianggraenis
 */
public class SalaryDAO extends DBHelper{

    public static final String SALARY_TABLE = "tb_t_salary";
    public static final String EMP_NIK = "emp_nik";
    public static final String DATE_START = "periode_date_start";
    public static final String DATE_END = "periode_date_end";
    
    public boolean insertOrUpdateSalary(SalaryModel salaryModel) {
        boolean res =false;
        
         String sql = "Replace into "+SALARY_TABLE+" "
                 + "values ('"+salaryModel.getEmpNik()+"', "
                 + "'"+salaryModel.getPeriodeDateStart()+"',"
                 + "'"+salaryModel.getPeriodeDateEnd()+"',"
                 + "'"+salaryModel.getTotalHours()+"',"
                 + "'"+salaryModel.getExtension()+"',"
                 + "'"+salaryModel.getNightShift()+"',"
                 + "'"+salaryModel.getOvertimeWeekend()+"',"
                 + "'"+salaryModel.getTransportFee()+"',"
                 + "'"+salaryModel.getNormalFee()+"',"
                 + "'"+salaryModel.getHourlyWageAmount()+"',"
                 + "'"+salaryModel.getExtensionAmount()+"',"
                 + "'"+salaryModel.getNightShiftAmount()+"',"
                 + "'"+salaryModel.getOvertimeWeekendAmount()+"',"
                 + "'"+salaryModel.getTransportFeeAmount()+"',"
                 + "'"+salaryModel.getIncomeTax()+"',"
                 + "'"+salaryModel.getPayTotalAmount()+"',"
                 + "'"+salaryModel.getPension()+"',"
                 + "'"+salaryModel.getHealthInsurance()+"',"
                 + "'"+salaryModel.getLongTermCareInsurance()+"',"
                 + "'"+salaryModel.getTotalSalary()+"',"
                 + "'"+salaryModel.getTotalDedcuction()+"',"
                 + ""+salaryModel.getTotalHours()+")";
        
        try {
            db.fExecute(sql);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
     public ResultSet getSalaryByEmp(String empNik, String dateStart, String dateEnd){
        ResultSet res = null;
        String sql = "select * from " + SALARY_TABLE + " where " +EMP_NIK+ " = '" + empNik + "'"
                + " and " + DATE_START + " = '" + dateStart + "' and " + DATE_END + " = '" +dateEnd + "' " ;
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

    public boolean updateSalary(SalaryModel salaryModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
