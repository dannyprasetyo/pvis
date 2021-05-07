/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package layout;

import java.sql.Connection;
import model.UserLogin;
import db.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DailyWorkModel;
import model.EmployeeModel;
import model.LoginModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;
import utils.DBConnection;

/**
 *
 * @author desianggraenis
 */
public class ReportForm extends javax.swing.JFrame {
    ReportForm reportForm;
    public Connection cnt;
    DailyWorkModel dailyWorkModel;
    DefaultTableModel modelReportAbsenPerEmployee;
    DefaultTableModel modelReportAbsenPerSpec;
    
    private String dateStartR1;
    private String dateEndR1;
    private String dateStartR2;
    private String dateEndR2;
    
    private String cmbnik;
    private String nik;
    private String spec;
    
    public static final String REPORT_SOURCE = System.getProperty("user.dir") + "\\src\\report\\DailyAbsentPerEmployee.jrxml";
    public static final String REPORT_SOURCE_2 = System.getProperty("user.dir") + "\\src\\report\\DailyAbsentPerSpec.jrxml";
    
    public static final String LOGO_SOURCE = System.getProperty("user.dir") + "\\src\\res\\ic_logo.png";
    
    public static final String DATA_NOT_FOUND = "Data not found!";
    private static final String EMPTY_FIELD = "Please fill empty fields!";
    
    DailyWorkDAO daoDw = new DailyWorkDAO();
    EmployeeDAO daoEmpl = new EmployeeDAO();
    
    /**
     * Creates new form ReportForm
     */
    public ReportForm() {
        initComponents();
        UserLogin.setUserLogin(this.userName, this.userPosition);
        
        cmbEmployee.setLightWeightPopupEnabled(false);
        cmbSpec.setLightWeightPopupEnabled(false);
        
        this.setVisible(true);
        initCmbEmployee();
        initCmbSpecification();
        printReport1.setEnabled(false);
        printReport2.setEnabled(false);
        
        DBConnection db = new DBConnection();
        modelReportAbsenPerEmployee = new DefaultTableModel();
        this.jTable1.setModel(modelReportAbsenPerEmployee);
        modelReportAbsenPerEmployee.addColumn("NO");
        modelReportAbsenPerEmployee.addColumn("DAY");
        modelReportAbsenPerEmployee.addColumn("SPECIFICATION");
        modelReportAbsenPerEmployee.addColumn("CLASSIFICATION");
        modelReportAbsenPerEmployee.addColumn("TOTAL PROCESSED");
        
        
        modelReportAbsenPerSpec = new DefaultTableModel();
        this.jTable2.setModel(modelReportAbsenPerSpec);
        modelReportAbsenPerSpec.addColumn("NO");
        modelReportAbsenPerSpec.addColumn("DAY");
        modelReportAbsenPerSpec.addColumn("EMPLOYEE");
        modelReportAbsenPerSpec.addColumn("CLASSIFICATION");
        modelReportAbsenPerSpec.addColumn("LOT NO");
        modelReportAbsenPerSpec.addColumn("MATERIAL INPUT");
        modelReportAbsenPerSpec.addColumn("ACTUAL INPUT");
        modelReportAbsenPerSpec.addColumn("FAULTY");
        
    }

    public void initCmbEmployee(){
        EmployeeDAO daoEmpl = new EmployeeDAO();
        ResultSet res = daoEmpl.getAll();
        
        try {
            while (res.next()) {                
                cmbEmployee.addItem(res.getString("emp_nik") +" - " +res.getString("emp_name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void initCmbSpecification(){   
        for(int i=0; i<DailyWorkModel.DwSpecification.length;i++){
            cmbSpec.addItem(DailyWorkModel.DwSpecification[i]);
        }
        
    }
    
    private void getDataReport1() {
        
        DailyWorkModel[] dailyWorkModel = daoDw.getDailyWorkByField(dateStartR1, dateEndR1, DailyWorkDAO.DW_EMP_NIK, nik, true);
        String[] columnNames = {"NO", "DAY", "SPECIFICATION", "CLASSIFICATION", "TOTAL PROCESSED"};
        String[][] data = new String[dailyWorkModel.length][5];
        if(dailyWorkModel.length == 0)
            JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
        for(int i=0; i<dailyWorkModel.length;i++){
            data[i][0] = String.valueOf(i+1);
            data[i][1] = dailyWorkModel[i].getWorkDay();
            data[i][2] = dailyWorkModel[i].getSpesificationString();
            data[i][3] = dailyWorkModel[i].getClassification();
            data[i][4] = String.valueOf(dailyWorkModel[i].getProcessedQty());
        }
        
        if(data.length > 0) 
           printReport1.setEnabled(true);
        
        modelReportAbsenPerEmployee = new DefaultTableModel(data, columnNames);
        this.jTable1.setModel(modelReportAbsenPerEmployee);
              
    }
    
    private void getDataReport2() {
        DailyWorkModel[] dailyWorkModel = daoDw.getDailyWorkByField(dateStartR2, dateEndR2, DailyWorkDAO.DW_SPESIFICATION, spec, true);
        String[] columnNames = {"NO", "DAY", "EMPLOYEE", "CLASSIFICATION", "LOT NO","MATERIAL INPUT","ACTUAL INPUT","FAULTY"};
        
        String[][] data = new String[dailyWorkModel.length][8];
        if(dailyWorkModel.length == 0)
            JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
        
        for(int i=0; i<dailyWorkModel.length;i++){
            data[i][0] = String.valueOf(i+1);
            data[i][1] = dailyWorkModel[i].getWorkDay();
            data[i][2] = dailyWorkModel[i].getEmpNik();
            data[i][3] = dailyWorkModel[i].getClassification();
            data[i][4] = dailyWorkModel[i].getLotNo();
            data[i][5] = String.valueOf(dailyWorkModel[i].getMaterialQty());
            data[i][6] = String.valueOf(dailyWorkModel[i].getProcessedQty());
            data[i][7] = String.valueOf(dailyWorkModel[i].getFaultyQty());
        }
       
        if(data.length > 0) 
           printReport2.setEnabled(true);
        
        modelReportAbsenPerSpec = new DefaultTableModel(data, columnNames);
        this.jTable2.setModel(modelReportAbsenPerSpec);
    }
    
    public void printReportDailyAbsentPerEmployee() {
        
        try {
            File file = new File(REPORT_SOURCE);
            JasperDesign jasperDesign = JasperManager.loadXmlDesign(REPORT_SOURCE);
            JasperReport jasperReport = JasperManager.compileReport(jasperDesign);
            Map parameters = new HashMap();

            EmployeeModel employeeModel = daoEmpl.getEmployeeByNik(nik);
            parameters.put("ic_logo", LOGO_SOURCE);
            parameters.put("periode_start", dateStartR1);
            parameters.put("periode_end", dateEndR1);
            parameters.put("nik", employeeModel.getEmpNik());
            parameters.put("name", employeeModel.getEmpName());
            parameters.put("status", employeeModel.getEmpStatusString());
            parameters.put("posisi", employeeModel.getEmpPositionString());
            
            DBConnection db = new DBConnection();
            Connection conn = db.getConnection(); 
            JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, parameters, new JRTableModelDataSource(modelReportAbsenPerEmployee));
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.viewReport(jasperPrint, false);
            jasperViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
    
    public void printReportDailyAbsentPerSpec() {
        
        try {
            File file = new File(REPORT_SOURCE_2);
            JasperDesign jasperDesign = JasperManager.loadXmlDesign(REPORT_SOURCE_2);
            JasperReport jasperReport = JasperManager.compileReport(jasperDesign);
            Map parameters = new HashMap();

            parameters.put("ic_logo", LOGO_SOURCE);
            parameters.put("periode_start", dateStartR2);
            parameters.put("periode_end", dateEndR2);
            parameters.put("specification", cmbSpec.getSelectedItem());
            
            DBConnection db = new DBConnection();
            Connection conn = db.getConnection(); 
            JasperPrint jasperPrint = JasperManager.fillReport(jasperReport, parameters, new JRTableModelDataSource(modelReportAbsenPerSpec));
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.viewReport(jasperPrint, false);
            jasperViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnHome = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        datePeriodStartR1 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        datePeriodEndR1 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        filterR1 = new javax.swing.JButton();
        resetR1 = new javax.swing.JButton();
        cmbEmployee = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        datePeriodStartR2 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        datePeriodEndR2 = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        filterR2 = new javax.swing.JButton();
        cmbSpec = new javax.swing.JComboBox();
        resetR2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        printReport1 = new javax.swing.JButton();
        printReport2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1366, 730));
        setMinimumSize(new java.awt.Dimension(1366, 730));
        setSize(new java.awt.Dimension(1366, 730));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1366, 730));
        jPanel2.setMinimumSize(new java.awt.Dimension(1366, 730));
        jPanel2.setPreferredSize(new java.awt.Dimension(1366, 730));

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

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NO", "DAY", "SPECIFICATION", "CLASSIFICATION", "TOTAL_PROCESSED"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Report Absensi per Employee");

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_home.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("NIK");

        datePeriodStartR1.setDateFormatString("yyyy-MM-dd");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("To");

        datePeriodEndR1.setDateFormatString("yyyy-MM-dd");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Periode");

        filterR1.setText("Filter");
        filterR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterR1ActionPerformed(evt);
            }
        });

        resetR1.setText("Reset");
        resetR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetR1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(datePeriodStartR1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(datePeriodEndR1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cmbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filterR1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resetR1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(datePeriodStartR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbEmployee))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(filterR1)
                                .addComponent(resetR1)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(datePeriodEndR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Period");

        datePeriodStartR2.setDateFormatString("yyyy-MM-dd");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("To");

        datePeriodEndR2.setDateFormatString("yyyy-MM-dd");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Specification");

        filterR2.setText("Filter");
        filterR2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterR2ActionPerformed(evt);
            }
        });

        resetR2.setText("Reset");
        resetR2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetR2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(datePeriodStartR2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(datePeriodEndR2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbSpec, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(filterR2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resetR2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(datePeriodEndR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datePeriodStartR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterR2)
                    .addComponent(cmbSpec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetR2))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "DAY", "EMPLOYEE", "CLASSIFICATION", "LOT NO", "MATERIAL INPUT", "ACTUAL INPUT", "FAULTY"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Report Absensi per Specification");

        printReport1.setText("Print Report Absensi per Employee");
        printReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReport1ActionPerformed(evt);
            }
        });

        printReport2.setText("Print Report Absensi per Specification");
        printReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReport2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(btnHome)
                        .addGap(94, 94, 94)
                        .addComponent(printReport1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printReport2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(209, 209, 209))))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 33, Short.MAX_VALUE)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(printReport1)
                            .addComponent(printReport2))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addComponent(btnHome)
                        .addGap(25, 25, 25))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 37, Short.MAX_VALUE))
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        this.setVisible(false);
        UserLogin.doLogout();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
        HomePage home = new HomePage();
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeMouseClicked

    private void filterR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterR1ActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        if(cmbEmployee.getSelectedItem().toString().isEmpty() || datePeriodStartR1.getDate() == null || datePeriodEndR1.getDate() == null ){
            JOptionPane.showMessageDialog(null, EMPTY_FIELD);
        }
        else {
            
        dateStartR1 = sdf.format(datePeriodStartR1.getDate());
        dateEndR1 = sdf.format(datePeriodEndR1.getDate());
        cmbnik = cmbEmployee.getSelectedItem().toString();
            String[] parts = cmbnik.split(" ");
            nik = parts[0]; // 004

            System.out.println("start: " + dateStartR1);
            System.out.println("end: " + dateEndR1);
            System.out.println("nik: " + nik);
            getDataReport1();
        }
    }//GEN-LAST:event_filterR1ActionPerformed

    private void filterR2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterR2ActionPerformed
        if(cmbSpec.getSelectedItem().toString().isEmpty() || datePeriodStartR2.getDate().toString().isEmpty() || datePeriodEndR2.getDate().toString().isEmpty()){
            JOptionPane.showMessageDialog(null, EMPTY_FIELD);
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            dateStartR2 = sdf.format(datePeriodStartR2.getDate());
            dateEndR2 = sdf.format(datePeriodEndR2.getDate());
            spec = String.valueOf(cmbSpec.getSelectedIndex());

            System.out.println("start: " + dateStartR1);
            System.out.println("end: " + dateEndR1);
            System.out.println("spec: " + spec);
            getDataReport2();
        }
    }//GEN-LAST:event_filterR2ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void printReport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReport1ActionPerformed
        printReportDailyAbsentPerEmployee();
    }//GEN-LAST:event_printReport1ActionPerformed

    private void printReport2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReport2ActionPerformed
        printReportDailyAbsentPerSpec();
    }//GEN-LAST:event_printReport2ActionPerformed

    private void resetR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetR1ActionPerformed
        datePeriodStartR1.setDate(null);
        datePeriodEndR1.setDate(null);
        cmbEmployee.setSelectedIndex(0);
    }//GEN-LAST:event_resetR1ActionPerformed

    private void resetR2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetR2ActionPerformed
        datePeriodStartR2.setDate(null);
        datePeriodEndR2.setDate(null);
        cmbSpec.setSelectedIndex(0);
    }//GEN-LAST:event_resetR2ActionPerformed

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
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JComboBox cmbEmployee;
    private javax.swing.JComboBox cmbSpec;
    private com.toedter.calendar.JDateChooser datePeriodEndR1;
    private com.toedter.calendar.JDateChooser datePeriodEndR2;
    private com.toedter.calendar.JDateChooser datePeriodStartR1;
    private com.toedter.calendar.JDateChooser datePeriodStartR2;
    private javax.swing.JButton filterR1;
    private javax.swing.JButton filterR2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private java.awt.Label label2;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private javax.swing.JButton printReport1;
    private javax.swing.JButton printReport2;
    private javax.swing.JButton resetR1;
    private javax.swing.JButton resetR2;
    private java.awt.Label userName;
    private java.awt.Label userPosition;
    // End of variables declaration//GEN-END:variables
}
