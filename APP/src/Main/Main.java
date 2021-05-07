/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.sql.Connection;
import layout.DailyWorkForm;
import layout.EmployeeForm;
import layout.HomePage;
import layout.LoginForm;
import layout.PayrollForm;
import utils.DBConnection;

/**
 *
 * @author desianggraenis
 */
public class Main {
    public Connection cnt; 
    DBConnection db = new DBConnection();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //FChart fChart = new FChart();
        LoginForm loginForm = new LoginForm();
        //EmployeeForm employeeForm = new EmployeeForm();
        //HomePage homePage = new HomePage();
        //DailyWorkForm dailyWorkForm = new DailyWorkForm();
        //PayrollForm form = new PayrollForm();
    }
    
}
