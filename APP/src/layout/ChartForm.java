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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import model.ChartModel;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import utils.DBConnection;

/**
 *
 * @author desianggraenis
 */
public class ChartForm extends javax.swing.JFrame {
    ChartForm reportForm;
    public Connection cnt;
    DailyWorkModel dailyWorkModel;
    DefaultTableModel modelChart;
    
    private String dateStartR1;
    private String dateEndR1;
    private String dateStartR2;
    private String dateEndR2;
    
    private String cmbnik;
    private String nik;
    private String spec;
    
    public static final String REPORT_SOURCE = System.getProperty("user.dir") + "\\src\\report\\DailyAbsentPerEmployee.jrxml";
    public static final String REPORT_SOURCE_2 = System.getProperty("user.dir") + "\\src\\report\\DailyAbsentPerSpec.jrxml";
    
    DailyWorkDAO daoDw = new DailyWorkDAO();
    EmployeeDAO daoEmpl = new EmployeeDAO();
    
    /**
     * Creates new form ReportForm
     */
    public ChartForm() {
        initComponents();
        UserLogin.setUserLogin(this.userName, this.userPosition);
        
        this.setVisible(true);
        initCmbYear();
        cType.setEnabled(false);
        filterChart.setEnabled(false);
        
        DBConnection db = new DBConnection();
        modelChart = new DefaultTableModel();
        this.jTable1.setModel(modelChart);
        modelChart.addColumn("Month");
        modelChart.addColumn("Material Input Qty");
        modelChart.addColumn("Processed Qty");
        modelChart.addColumn("Fault Qty");
        
        cmbYear.setLightWeightPopupEnabled(false);
        cType.setLightWeightPopupEnabled(false);
        getDataChart();
    }

    public void initCmbYear(){
        DailyWorkDAO dailyWorkDAO = new DailyWorkDAO();
        ResultSet res = dailyWorkDAO.getYear();
        
        try {
            while (res.next()) {                
                cmbYear.addItem(res.getString("year"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void getDataChart() {
        
        ChartModel[] chartModel = daoDw.getProductionQuantityByYear(cmbYear.getSelectedItem().toString());
        String[] columnNames = {"Month", "Material Input Qty","Processed Qty","Fault Qty"};
                
        String[][] data = new String[chartModel.length][4];
        for(int i=0; i<chartModel.length;i++){
            data[i][0] = chartModel[i].getMonth();
            data[i][1] = String.valueOf(chartModel[i].getMiq());
            data[i][2] = String.valueOf(chartModel[i].getPq());
            data[i][3] = String.valueOf(chartModel[i].getFq());
        }
        
        if(chartModel.length > 0) {
           cType.setEnabled(true);
           lChart.setEnabled(true);
           filterChart.setEnabled(true);
        }
        
        modelChart = new DefaultTableModel(data, columnNames);
        this.jTable1.setModel(modelChart);
        generateChart();
              
    }
    
    private void generateChart() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset(); 
        ChartModel[] chartModel = daoDw.getProductionQuantityByYear(cmbYear.getSelectedItem().toString());
        String[] tahun = new String[chartModel.length];
        int[] MIQ = new int[chartModel.length];
        int[] PQ = new int[chartModel.length];
        int[] FQ = new int[chartModel.length];
        int totalMiq = 0;
        int totalPq = 0;
        int totalFq = 0;
        
//        String[] tahun = {"June","July"}; 
//        int[] MIQ = {100,45}; 
//        int[] PQ = {50,5}; 
//        int[] FQ = {40,4}; 
        
        for(int i=0; i<chartModel.length;i++){
            tahun[i] = chartModel[i].getMonth();
            MIQ[i] = chartModel[i].getMiq();
            PQ[i] = chartModel[i].getPq();
            FQ[i] = chartModel[i].getFq();
            totalMiq += MIQ[i];
            totalPq += PQ[i];
            totalFq += FQ[i];
        }
        
        System.out.println("MIQ : " + MIQ);
        int totalQty = totalMiq + totalPq + totalFq;
        
        for (int i=0; i<tahun.length; i++) { 
            String thn = String.valueOf(tahun[i]); 
            dataSet.addValue(MIQ[i], "Material Input Qty", thn); 
            dataSet.addValue(PQ[i], "Processed Qty", thn); 
            dataSet.addValue(FQ[i], "Faulty Qty", thn); 
            System.out.println("miq : " + MIQ[i]);
        } 
        

        JFreeChart chart = null; 

        if (cType.getSelectedIndex() == 0) {
            chart = ChartFactory.createBarChart("Bar", "Month", "Quantity", dataSet, PlotOrientation.VERTICAL, true, true, true);  
        } else if (cType.getSelectedIndex() == 1) {
            chart = ChartFactory.createLineChart("Line", "Month", "Quantity", dataSet, PlotOrientation.VERTICAL, true, true, true);    
        } else if (cType.getSelectedIndex() == 2) {
            DefaultPieDataset pieDataSet = new DefaultPieDataset();
            pieDataSet.setValue("Material Input Qty", new Double(totalMiq));
            pieDataSet.setValue("Processed Qty", new Double(totalPq));
            pieDataSet.setValue("Faulty Qty", new Double(totalFq));
//            pieDataSet.setValue("Material Input", new Double((totalMiq/totalQty)*100));
//            pieDataSet.setValue("Processed", new Double((totalPq/totalQty)*100));
//            pieDataSet.setValue("Faulty", new Double((totalFq/totalQty)*100));
            chart = ChartFactory.createPieChart("Pie", pieDataSet, true, true, false);     
        }  

        try {
            Calendar rightNow = Calendar.getInstance();
        //    fLahir.setText(String.valueOf(rightNow.get(rightNow.DATE)) + "/" + String.valueOf(rightNow.get(rightNow.MONTH)) + "/" + String.valueOf(rightNow.get(rightNow.YEAR)));
            String fName = "chart" + String.valueOf(rightNow.getTimeInMillis()) + ".jpg";
            ChartUtilities.saveChartAsJPEG(new File(fName), chart, 771, 318);
            lChart.setIcon(null);
            lChart.setIcon(new ImageIcon(fName));
        } catch (Exception e){ System.out.println(e);}
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
        filterR1 = new javax.swing.JButton();
        cmbYear = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        filterChart = new javax.swing.JButton();
        cType = new javax.swing.JComboBox();
        lChart = new javax.swing.JLabel();

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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Month", "Material Input Qty", "Processed Qty", "Faulty Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false
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
        jLabel1.setText("Production Quantity");

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_home.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Year");

        filterR1.setText("Filter");
        filterR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterR1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(filterR1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterR1))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Graph of Production Quantity");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Type of Graph");

        filterChart.setText("Filter");
        filterChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterChartActionPerformed(evt);
            }
        });

        cType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bar", "Line", "Pie" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cType, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(filterChart, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterChart))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(btnHome)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33))
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lChart, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addComponent(btnHome)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
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
        getDataChart();
    }//GEN-LAST:event_filterR1ActionPerformed

    private void filterChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterChartActionPerformed
        generateChart();
    }//GEN-LAST:event_filterChartActionPerformed

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
            java.util.logging.Logger.getLogger(ChartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChartForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JComboBox cType;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JButton filterChart;
    private javax.swing.JButton filterR1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lChart;
    private java.awt.Label label2;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private java.awt.Label userName;
    private java.awt.Label userPosition;
    // End of variables declaration//GEN-END:variables
}
