/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package layout;

import db.DailyWorkDAO;
import db.EmployeeDAO;
import db.LoginDAO;
import db.SalaryDAO;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.EmployeeModel;
import model.LoginModel;
import model.SalaryModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import utils.DBConnection;
import model.UserLogin;
/**
 *
 * @author desianggraenis
 */
public class PayrollForm extends javax.swing.JFrame {

    public static final String SUCCEED_SAVE_SALARY = "Data have been saved successfully!";
    public static final String FAILED_SAVE_SALARY = "Failed to save data!";
    private static final String EMPTY_FIELD = "Please fill empty fields!";
    public static final String DATA_NOT_FOUND = "Data Not Found!";
    public static final String PERIODE_NOT_VALID = "Perioe Not Valid!";
    
    public static final String REPORT_SOURCE = System.getProperty("user.dir") + "\\src\\report\\SalaryReport.jrxml";
    public static final String LOGO_SOURCE = System.getProperty("user.dir") + "\\src\\res\\ic_logo.png";
    
    PayrollForm employeeForm;
    public Connection cnt;
    EmployeeModel employeeModel;
    DefaultTableModel model;
    
    private String dateStart;
    private String dateEnd;
    private String cmbnik;
    private String nik;
    
    private boolean update;
    private DefaultTableModel tableModel;
    
    private SalaryModel salaryModel;

    public void saveSalary() {
        SalaryDAO salaryDAO = new SalaryDAO();
        boolean res = false;
    
        res = salaryDAO.insertOrUpdateSalary(salaryModel);
        
        if(res){
            JOptionPane.showMessageDialog(null, SUCCEED_SAVE_SALARY);
            initTxt(false);
        }
        else{
            JOptionPane.showMessageDialog(null, FAILED_SAVE_SALARY);
        }
    }

    private void initTxt(boolean b) {
        txtExtension.setEnabled(b);
        txtNightShift.setEnabled(b);
        txtOverTime.setEnabled(b);
        txtTransport.setEnabled(b);
    }

    private void printReport() {
        System.out.println(REPORT_SOURCE);
        TableModelData();
        try {
           File file = new File(REPORT_SOURCE);
            JasperDesign jasperDesign = JasperManager.loadXmlDesign(REPORT_SOURCE);
            JasperReport jasperReport = JasperManager.compileReport(jasperDesign);
            Map parameters = new HashMap();
            parameters.put("nik", salaryModel.getEmpNik());
            parameters.put("date_start", salaryModel.getPeriodeDateStart());
            parameters.put("data_end", salaryModel.getPeriodeDateEnd());
            parameters.put("ic_logo", LOGO_SOURCE);
            
            System.out.println("param nik: " +salaryModel.getEmpNik());
            System.out.println("param start: " +salaryModel.getPeriodeDateStart());
            System.out.println("param nik: " +salaryModel.getPeriodeDateEnd());
            
            DBConnection db = new DBConnection();
            Connection conn = db.getConnection(); 
            JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, parameters, new JRTableModelDataSource(tableModel));
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.viewReport(jasperPrint, false);
            jasperViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        
    }
    
     private void TableModelData() {
         EmployeeModel employeeModel = new EmployeeModel();
         EmployeeDAO dao = new EmployeeDAO();
         employeeModel = dao.getEmployeeByNik(salaryModel.getEmpNik());
         
        String[] columnNames = {"EMP_NIK", "EMP_NAME", "EMP_STATUS", "EMP_POSITION", "PERIODE_DATE_START", "PERIODE_DATE_END" ,
            "NORMAL_FEE", "HOURLY_WAGE_AMOUNT", "EXTENSION_AMOUNT", "NIGHT_SHIFT_AMOUNT", 
            "OVER_TIME_WEEKEND_AMOUNT", "TRANSPORT_FEE_AMOUNT", "INCOME_TAX", "PAY_TOTAL_AMOUNT", "PENSION", "HEALTH_INSURANCE",
            "LONG_TERM_CARE_INSURANCE", "TOTAL_SALARY", "TOTAL_DEDUCTION"};
        
        String[][] data = {
            {salaryModel.getEmpNik(), employeeModel.getEmpName(), employeeModel.getEmpStatusString(), EmployeeModel.EmployeePosition[employeeModel.getEmpPosition()],
                salaryModel.getPeriodeDateStart(), salaryModel.getPeriodeDateEnd(), 
                String.valueOf(salaryModel.getNormalFee()), String.valueOf(salaryModel.getHourlyWageAmount()), String.valueOf(salaryModel.getExtensionAmount()), String.valueOf(salaryModel.getNightShiftAmount()),
                String.valueOf(salaryModel.getOvertimeWeekendAmount()), String.valueOf(salaryModel.getTransportFeeAmount()), String.valueOf(salaryModel.getIncomeTax()), String.valueOf(salaryModel.getPayTotalAmount()),
                String.valueOf(salaryModel.getPension()), String.valueOf(salaryModel.getHealthInsurance()), String.valueOf(salaryModel.getLongTermCareInsurance()), String.valueOf(salaryModel.getTotalSalary()),
                String.valueOf(salaryModel.getTotalDedcuction())
            }
            };
        tableModel = new DefaultTableModel(data, columnNames);
    }

    
    public static enum Act {
        QUERY,
        UPDATE,
        INSERT
    }
    
    EmployeeDAO dao = new EmployeeDAO();
    /**
     * Creates new form EmployeeForm2
     */
    public PayrollForm() {
        
        update = false;
        initComponents();
        cmbEmployee.setLightWeightPopupEnabled(false);
        UserLogin.setUserLogin(this.userName, this.userPosition);
        initCmbEmployee();
        initBtn(false);
        btnUpdate.setVisible(false);
        this.setVisible(true);
        this.setExtendedState(JFrame.NORMAL);
        
        salaryModel = new SalaryModel();
        
    }
    
    public void initBtn(boolean init){
        btnPrintSalary.setEnabled(init);
        btnSaveSalary.setEnabled(init);
        btnSubmitSalary.setEnabled(init);
        btnClearSalary.setEnabled(init);
    }
    
    public void initCmbEmployee(){
        EmployeeDAO dao = new EmployeeDAO();
        ResultSet res = dao.getAll();
        
        try {
            while (res.next()) {                
                cmbEmployee.addItem(res.getString("emp_nik") +" - " +res.getString("emp_name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void getWorkHours(){
        DailyWorkDAO dao = new DailyWorkDAO();
        
        int hours = 0;
        hours = dao.getWorkHours(nik, dateStart, dateEnd);
        
        System.out.println("hours : " + hours);
        lblHours.setText(String.valueOf(hours));
        
        salaryModel.setTotalHours(hours);
    }

    public void countSalary() {
        
        salaryModel.setExtension(Integer.parseInt(txtExtension.getText().toString()));
        salaryModel.setNightShift(Integer.parseInt(txtNightShift.getText().toString()));
        salaryModel.setOvertimeWeekend(Integer.parseInt(txtOverTime.getText().toString()));
        salaryModel.setTransportFee(Integer.parseInt(txtTransport.getText().toString()));
       
        salaryModel.setHourlyWageAmount();
        salaryModel.setExtensionAmount();
        salaryModel.setNightShiftAmount();
        salaryModel.setOvertimeWeekendAmount();
        salaryModel.setTransportFeeAmount();
        
        salaryModel.setNormalFee();
        salaryModel.setPayTotalAmount();
        
        salaryModel.setIncomeTax();
        salaryModel.setPension();
        salaryModel.setHealthInsurance();
        salaryModel.setLongTermCareInsurance();
        
        salaryModel.setTotalDeduction();
        salaryModel.setTotalSalary();
        
        lblHourlyWageAmount.setText(Double.toString(salaryModel.getHourlyWageAmount()));
        lblExtensionAmount.setText(Double.toString(salaryModel.getExtensionAmount()));
        lblNightShiftAmount.setText(Double.toString(salaryModel.getNightShiftAmount()));
        lblOvertimeAmount.setText(Double.toString(salaryModel.getOvertimeWeekendAmount()));
        lblTransportAmount.setText(Double.toString(salaryModel.getTransportFeeAmount()));
        
        lblNormalFee.setText(salaryModel.getNormalFee().toString());
        lblPayTotalAmount.setText((salaryModel.getPayTotalAmount().setScale(0,RoundingMode.HALF_UP)).toString());
        
        lblIncomeTax.setText(Double.toString(salaryModel.getIncomeTax()));
        lblPension.setText(Double.toString(salaryModel.getPension()));
        lblHealthInsurance.setText(Double.toString(salaryModel.getHealthInsurance()));
        lblLongTermInsurance.setText(Double.toString(salaryModel.getLongTermCareInsurance()));
        
        lblTotalDeduction.setText((salaryModel.getTotalDedcuction().setScale(0,RoundingMode.HALF_UP)).toString());
        lblTotalSalary.setText((salaryModel.getTotalSalary().setScale(0,RoundingMode.HALF_UP)).toString());
    }
    
    public ResultSet getSalaryByEmp(){
        ResultSet res = null;
        SalaryDAO salaryDAO = new SalaryDAO();
        
        res = salaryDAO.getSalaryByEmp(nik, dateStart, dateEnd);
        
        return res;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        label2 = new java.awt.Label();
        jLabel14 = new javax.swing.JLabel();
        userName = new java.awt.Label();
        userPosition = new java.awt.Label();
        btnLogout = new javax.swing.JLabel();
        panel2 = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        btnHome = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbEmployee = new javax.swing.JComboBox();
        datePeriodeStart = new com.toedter.calendar.JDateChooser();
        datePeriodeEnd = new com.toedter.calendar.JDateChooser();
        btnClear = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblTransportAmount = new javax.swing.JLabel();
        lblOvertimeAmount = new javax.swing.JLabel();
        lblNightShiftAmount = new javax.swing.JLabel();
        lblExtensionAmount = new javax.swing.JLabel();
        lblHourlyWageAmount = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblPayTotalAmount = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblIncomeTax = new javax.swing.JLabel();
        lblPension = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lblHealthInsurance = new javax.swing.JLabel();
        lblLongTermInsurance = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lblTotalDeduction = new javax.swing.JLabel();
        lblTotalSalary = new javax.swing.JLabel();
        btnPrintSalary = new javax.swing.JButton();
        btnSaveSalary = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        lblHours = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtExtension = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtNightShift = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtOverTime = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtTransport = new javax.swing.JTextField();
        btnSubmitSalary = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        btnClearSalary = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lblNormalFee = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 730));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1090, 700));
        jPanel2.setMinimumSize(new java.awt.Dimension(1090, 700));
        jPanel2.setPreferredSize(new java.awt.Dimension(1090, 800));

        panel1.setBackground(new java.awt.Color(217, 228, 250));

        label2.setFont(new java.awt.Font("Verdana", 1, 33)); // NOI18N
        label2.setForeground(new java.awt.Color(56, 80, 119));
        label2.setText("PAYROLL SYSTEM");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_logo.png"))); // NOI18N

        userName.setAlignment(java.awt.Label.RIGHT);
        userName.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        userName.setForeground(new java.awt.Color(56, 80, 119));
        userName.setPreferredSize(new java.awt.Dimension(160, 29));
        userName.setText("Desi Anggraeni");

        userPosition.setAlignment(java.awt.Label.RIGHT);
        userPosition.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N
        userPosition.setForeground(new java.awt.Color(56, 80, 119));
        userPosition.setText("Manager Keuangan");

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_user2.png"))); // NOI18N
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(userPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(userPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout)
                    .addComponent(jLabel14))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Payroll");

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_home.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Period");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("To");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Employee");

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(35, 35, 35)
                .addComponent(cmbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datePeriodeStart, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(datePeriodeEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94)
                .addComponent(btnSubmit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClear)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSubmit)
                        .addComponent(btnClear))
                    .addComponent(datePeriodeEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePeriodeStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(cmbEmployee))
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addGap(23, 23, 23))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Hourly wage");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Extension");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Night shift");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Over time");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Transport");

        lblTransportAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransportAmount.setText("0");

        lblOvertimeAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOvertimeAmount.setText("0");

        lblNightShiftAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNightShiftAmount.setText("0");

        lblExtensionAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExtensionAmount.setText("0");

        lblHourlyWageAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHourlyWageAmount.setText("0");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("₩ ");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setText("Pay total amount");

        lblPayTotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPayTotalAmount.setText("0");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setText("Income tax");

        lblIncomeTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncomeTax.setText("0");

        lblPension.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPension.setText("0");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setText("Pension");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setText("Health insurance");

        lblHealthInsurance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHealthInsurance.setText("0");

        lblLongTermInsurance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLongTermInsurance.setText("0");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Long term care insurance");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setText("Total deduction");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setText("Total salary");

        lblTotalDeduction.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalDeduction.setText("0");

        lblTotalSalary.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalSalary.setText("0");

        btnPrintSalary.setText("Print");
        btnPrintSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSalaryActionPerformed(evt);
            }
        });

        btnSaveSalary.setText("Save");
        btnSaveSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSalaryActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("₩ ");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("₩ ");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("₩ ");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setText("₩ ");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("₩ ");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("₩ ");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("₩ ");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("₩ ");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("₩ ");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setText("₩ ");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setText("₩ ");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblTransportAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel45))
                                    .addGap(42, 42, 42))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel33)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel48)
                                        .addComponent(jLabel17))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblOvertimeAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lblHourlyWageAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel16))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblExtensionAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                        .addComponent(lblNightShiftAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel49)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lblPayTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel50)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTotalSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(81, 81, 81)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(jLabel51)
                                .addGap(18, 18, 18)
                                .addComponent(lblTotalDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblLongTermInsurance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12))
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel30))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(lblIncomeTax, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblPension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblHealthInsurance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(170, 170, 170))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnPrintSalary)
                                .addGap(18, 18, 18)
                                .addComponent(btnSaveSalary))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel29))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblHourlyWageAmount)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(lblExtensionAmount))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(lblNightShiftAmount))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(lblOvertimeAmount))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel48)
                                    .addComponent(lblTransportAmount))))
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(lblPayTotalAmount)
                            .addComponent(jLabel33))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(lblTotalSalary)
                            .addComponent(jLabel50)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel39)
                                    .addComponent(jLabel20)
                                    .addComponent(lblPension)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel30)
                                    .addComponent(lblHealthInsurance))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel31)
                                    .addComponent(lblLongTermInsurance)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(lblIncomeTax))))
                        .addGap(45, 45, 45)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(jLabel51)
                            .addComponent(lblTotalDeduction))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrintSalary)
                            .addComponent(btnSaveSalary))))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        lblHours.setText("0");

        jLabel5.setText("Work hours");

        jLabel6.setText("hours");

        jLabel22.setText("hours");

        txtExtension.setText("0");
        txtExtension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExtensionActionPerformed(evt);
            }
        });

        jLabel7.setText("Extension");

        jLabel23.setText("Night shift");

        txtNightShift.setText("0");
        txtNightShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNightShiftActionPerformed(evt);
            }
        });

        jLabel24.setText("hours");

        jLabel25.setText("hours");

        txtOverTime.setText("0");
        txtOverTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOverTimeActionPerformed(evt);
            }
        });

        jLabel26.setText("Over time");

        jLabel27.setText("Transport fee");

        txtTransport.setText("0");
        txtTransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransportActionPerformed(evt);
            }
        });

        btnSubmitSalary.setText("Submit");
        btnSubmitSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitSalaryActionPerformed(evt);
            }
        });

        jLabel28.setText("days");

        btnClearSalary.setText("Clear");
        btnClearSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSalaryActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel32.setText("Normal fee");

        jLabel34.setText("₩ ");

        lblNormalFee.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNormalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel23)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHours)
                            .addComponent(txtExtension, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNightShift, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOverTime, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(77, 77, 77)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel6)
                        .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel28))
                .addGap(57, 57, 57))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(btnUpdate)
                .addGap(26, 26, 26)
                .addComponent(btnClearSalary)
                .addGap(37, 37, 37)
                .addComponent(btnSubmitSalary)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblHours)
                    .addComponent(jLabel6))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtExtension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtNightShift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtOverTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(lblNormalFee)
                    .addComponent(jLabel34))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmitSalary)
                    .addComponent(btnClearSalary)
                    .addComponent(btnUpdate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHome)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnHome)
                .addContainerGap(465, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        this.setVisible(false);
        UserLogin.doLogout();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        HomePage home = new HomePage();
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeMouseClicked

    private void txtExtensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExtensionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExtensionActionPerformed

    private void txtNightShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNightShiftActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNightShiftActionPerformed

    private void txtOverTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOverTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOverTimeActionPerformed

    private void txtTransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTransportActionPerformed

    private void btnSubmitSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitSalaryActionPerformed
        countSalary();
    }//GEN-LAST:event_btnSubmitSalaryActionPerformed

    private void btnClearSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSalaryActionPerformed
        lblHours.setText("0");
        lblHourlyWageAmount.setText("0");
        lblExtensionAmount.setText("0");
        lblNightShiftAmount.setText("0");
        lblOvertimeAmount.setText("0");
        lblTransportAmount.setText("0");
        
        lblNormalFee.setText("0");
        lblPayTotalAmount.setText("0");
        
        lblIncomeTax.setText("0");
        lblPension.setText("0");
        lblHealthInsurance.setText("0");
        lblLongTermInsurance.setText("0");
        
        lblTotalDeduction.setText("0");
        lblTotalSalary.setText("0");
        
        initTxt(true);
        
        txtExtension.setText("0");
        txtNightShift.setText("0");
        txtOverTime.setText("0");
        txtTransport.setText("0");
        
        salaryModel = new SalaryModel();
    }//GEN-LAST:event_btnClearSalaryActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update = true;
        initTxt(true);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnPrintSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSalaryActionPerformed
        // TODO add your handling code here:
        printReport();
    }//GEN-LAST:event_btnPrintSalaryActionPerformed

    private void btnSaveSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSalaryActionPerformed
        // TODO add your handling code here:
        saveSalary();
    }//GEN-LAST:event_btnSaveSalaryActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        cmbEmployee.setSelectedIndex(0);
        datePeriodeStart.setDate(null);
        datePeriodeEnd.setDate(null);
        salaryModel = new SalaryModel();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        
        if(cmbEmployee.getSelectedItem().toString().isEmpty() || datePeriodeStart.getDate() == null || datePeriodeEnd.getDate() == null){
            JOptionPane.showMessageDialog(null, EMPTY_FIELD);
        }
        else {
            
            dateStart = sdf.format(datePeriodeStart.getDate());
            dateEnd = sdf.format(datePeriodeEnd.getDate());
            
            YearMonth ym = YearMonth.of(Integer.valueOf(year.format(datePeriodeStart.getDate())), Integer.valueOf(month.format(datePeriodeStart.getDate())));
            String endDay = ym.atEndOfMonth().toString();
            
            String fDay = day.format(datePeriodeStart.getDate());
            System.out.println("fDay : " + fDay + " - endDay : " + endDay + " = " + dateEnd);
            if(!fDay.equals("01") || !dateEnd.equals(endDay)){
                JOptionPane.showMessageDialog(null, PERIODE_NOT_VALID);
            }
                
            cmbnik = cmbEmployee.getSelectedItem().toString();
            String[] parts = cmbnik.split(" ");
            nik = parts[0]; // 004

            System.out.println("start: " + dateStart);
            System.out.println("end: " + dateEnd);
            System.out.println("nik: " + nik);

            salaryModel.setEmpNik(nik);
            salaryModel.setPeriodeDateStart(dateStart);
            salaryModel.setPeriodeDateEnd(dateEnd);

            getWorkHours();
            
            if(lblHours.getText().toString().equals("0")){
                JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
            }
            ResultSet res = null;
            //res = getSalaryByEmp();
            SalaryDAO salaryDAO = new SalaryDAO();

            res = salaryDAO.getSalaryByEmp(nik, dateStart, dateEnd);

            try {
                if(res.wasNull()){
                    System.out.println("tes masuk null ");
                    JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
                    txtExtension.setText("0");
                    txtNightShift.setText("0");
                    txtOverTime.setText("0");
                    txtTransport.setText("0");
                }else{
                    try {
                        while(res.next()){
                            salaryModel.setEmpNik(res.getString("emp_nik"));
                            salaryModel.setPeriodeDateStart(res.getString("periode_date_start"));
                            salaryModel.setPeriodeDateEnd(res.getString("periode_date_end"));
                            salaryModel.setTotalHours(Integer.parseInt(res.getString("hourly_wage")));

                            txtExtension.setText(res.getString("extension"));
                            txtNightShift.setText(res.getString("night_shift"));
                            txtOverTime.setText(res.getString("over_time_weekend"));
                            txtTransport.setText(res.getString("transport_fee"));

                            initTxt(false);

                        }
                        btnUpdate.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                   }
                    countSalary();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            initBtn(true);
            
        }
//        if(res == null){
//            System.out.println("tes masuk null ");
//            JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
//            txtExtension.setText("0");
//            txtNightShift.setText("0");
//            txtOverTime.setText("0");
//            txtTransport.setText("0");
//        }else{
//            try {
//                while(res.next()){
//                    salaryModel.setEmpNik(res.getString("emp_nik"));
//                    salaryModel.setPeriodeDateStart(res.getString("periode_date_start"));
//                    salaryModel.setPeriodeDateEnd(res.getString("periode_date_end"));
//                    salaryModel.setTotalHours(Integer.parseInt(res.getString("hourly_wage")));
//
//                    txtExtension.setText(res.getString("extension"));
//                    txtNightShift.setText(res.getString("night_shift"));
//                    txtOverTime.setText(res.getString("over_time_weekend"));
//                    txtTransport.setText(res.getString("transport_fee"));
//
//                    initTxt(false);
//
//                }
//                btnUpdate.setVisible(true);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            countSalary();
//        }

    }//GEN-LAST:event_btnSubmitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PayrollForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PayrollForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearSalary;
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JButton btnPrintSalary;
    private javax.swing.JButton btnSaveSalary;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnSubmitSalary;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cmbEmployee;
    private com.toedter.calendar.JDateChooser datePeriodeEnd;
    private com.toedter.calendar.JDateChooser datePeriodeStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private java.awt.Label label2;
    private javax.swing.JLabel lblExtensionAmount;
    private javax.swing.JLabel lblHealthInsurance;
    private javax.swing.JLabel lblHourlyWageAmount;
    private javax.swing.JLabel lblHours;
    private javax.swing.JLabel lblIncomeTax;
    private javax.swing.JLabel lblLongTermInsurance;
    private javax.swing.JLabel lblNightShiftAmount;
    private javax.swing.JLabel lblNormalFee;
    private javax.swing.JLabel lblOvertimeAmount;
    private javax.swing.JLabel lblPayTotalAmount;
    private javax.swing.JLabel lblPension;
    private javax.swing.JLabel lblTotalDeduction;
    private javax.swing.JLabel lblTotalSalary;
    private javax.swing.JLabel lblTransportAmount;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private javax.swing.JTextField txtExtension;
    private javax.swing.JTextField txtNightShift;
    private javax.swing.JTextField txtOverTime;
    private javax.swing.JTextField txtTransport;
    private java.awt.Label userName;
    private java.awt.Label userPosition;
    // End of variables declaration//GEN-END:variables
}
