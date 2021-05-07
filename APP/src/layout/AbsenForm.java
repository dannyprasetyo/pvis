/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layout;

import db.AbsenDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.AbsenModel;
import model.UserLogin;
import utils.DBConnection;

/**
 *
 * @author Danny
 */
public class AbsenForm extends javax.swing.JFrame {
    AbsenForm absenForm;
    public Connection cn;
    AbsenModel absenModel;
    DefaultTableModel modelAbsen;
    String filterbydate="";
    
    public static final String SUCCEED_SAVE_ABSEN = "Absen Berhasil";
    public static final String FAILED_SAVE_ABSEN = "Absen Gagal";
    public static final String DATA_NOT_FOUND = "Data tidak ada";
    public static final String FAILED_ABSEN = "Waktu absen dimulai Pukul 06.00";
    
    public static enum Act {
        QUERY,
        UPDATE,
        INSERT
    }
    
    AbsenDAO absenDao = new AbsenDAO();
    /**
     * Creates new form AbsenForm
     */
    public AbsenForm() {
        initComponents();
        UserLogin.setUserLogin(this.userName, this.userPosition);
        absenModel = new AbsenModel(UserLogin.getUserNik(), getTanggal(), getTanggalWaktu(), getTanggalWaktu());
        
        this.setVisible(true);
        
        DBConnection db = new DBConnection();
        modelAbsen = new DefaultTableModel();
        this.tableAbsen.setModel(modelAbsen);
        modelAbsen.addColumn("NO");
        modelAbsen.addColumn("TANGGAL");
        modelAbsen.addColumn("JAM MASUK");
        modelAbsen.addColumn("JAM PULANG");
        
        initAbsenTable();
//        getTanggal();
//        System.out.println(absenModel.getEmpNik());
        getAbsenPegawai();
        /* test */
//        ResultSet res = null;
//        AbsenDAO absenDAO = new AbsenDAO();
//        
//        res = absenDAO.getAbsenPegawai(absenModel.getEmpNik(), getTanggal());
//        try{
//            if (res.wasNull()){
//                JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
//            }else{
//                while(res.next()){
//                    txtJamMasuk.setText(res.getString("att_in"));
//                }
//            }
//        }catch (SQLException ex) {
//            ex.printStackTrace();
//        }
        /* end test*/
    }
    
    public void initAbsenTable(){
        AbsenModel[] absModel = absenDao.getAbsen(AbsenDAO.AB_ATT_DATE, filterbydate, absenModel.getEmpNik());
        String[] columnNames = {"NO", "TANGGAL", "JAM MASUK", "JAM PULANG"};
        
        String[][] data = new String[absModel.length][4];
        for(int i=0; i<absModel.length;i++){
            data[i][0] = String.valueOf(i+1);
            data[i][1] = absModel[i].getAttDate();
            data[i][2] = absModel[i].getAttIn();
            data[i][3] = absModel[i].getAttOut();
        }
        
        modelAbsen = new DefaultTableModel(data, columnNames);
        this.tableAbsen.setModel(modelAbsen);
    }
    
    public ResultSet getAbsenPegawai(){
        ResultSet res = null;
        AbsenDAO absenDAO = new AbsenDAO();
        
        res = absenDAO.getAbsenPegawai(absenModel.getEmpNik(), getTanggal());
        System.out.println("getAbsenPegawai"+res);
        try{
            if (res.wasNull()){
                JOptionPane.showMessageDialog(null, DATA_NOT_FOUND);
            }else{
                while(res.next()){
                    labelJamMasuk.setText(res.getString("att_in"));
//                    labelJamPulang.setText(res.getString("att_out"));
                    if (res.getString("att_out") != null){
                        labelJamPulang.setText(res.getString("att_out"));
//                        System.out.println("kok kambing");
                    }else if (res.getString("att_in") != null){
                        labelJamMasuk.setText(res.getString("att_in"));
                    }
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
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

        jPanel3 = new javax.swing.JPanel();
        panel4 = new java.awt.Panel();
        btnLogout = new javax.swing.JLabel();
        userName = new java.awt.Label();
        userPosition = new java.awt.Label();
        label2 = new java.awt.Label();
        jLabel14 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAbsen = new javax.swing.JTable();
        btnFilter = new javax.swing.JButton();
        datePeriodeStart = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAbsen = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelJamPulang = new javax.swing.JLabel();
        labelJamMasuk = new javax.swing.JLabel();
        btnHome1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        setForeground(java.awt.Color.black);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        panel4.setBackground(new java.awt.Color(217, 228, 250));

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_user2.png"))); // NOI18N
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });

        userName.setAlignment(java.awt.Label.RIGHT);
        userName.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        userName.setForeground(new java.awt.Color(56, 80, 119));
        userName.setPreferredSize(new java.awt.Dimension(160, 29));
        userName.setText("Desi Anggraeni");

        userPosition.setAlignment(java.awt.Label.RIGHT);
        userPosition.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N
        userPosition.setForeground(new java.awt.Color(56, 80, 119));
        userPosition.setText("Manager Keuangan");

        label2.setFont(new java.awt.Font("Verdana", 1, 33)); // NOI18N
        label2.setForeground(new java.awt.Color(56, 80, 119));
        label2.setText("PAYROLL SYSTEM");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_logo.png"))); // NOI18N

        javax.swing.GroupLayout panel4Layout = new javax.swing.GroupLayout(panel4);
        panel4.setLayout(panel4Layout);
        panel4Layout.setHorizontalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel4Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(userPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        panel4Layout.setVerticalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel4Layout.createSequentialGroup()
                        .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(userPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel4Layout.createSequentialGroup()
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout)
                    .addComponent(jLabel14))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("List of Absen");

        tableAbsen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
                "No", "Tanggal", "Jam Masuk", "Jam Pulang"
            }
        ));
        tableAbsen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAbsenMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableAbsen);

        btnFilter.setText("Filter");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(datePeriodeStart, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(btnFilter)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFilter)
                    .addComponent(datePeriodeStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Jam Masuk :");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Jam Pulang :");

        btnAbsen.setText("Absen");
        btnAbsen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbsenActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Absen Form");

        labelJamPulang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelJamPulang.setText("Anda Belum Absen");

        labelJamMasuk.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelJamMasuk.setText("Anda Belum Absen");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(101, 101, 101))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(btnAbsen))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelJamPulang)
                            .addComponent(labelJamMasuk))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labelJamMasuk))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(labelJamPulang))
                .addGap(37, 37, 37)
                .addComponent(btnAbsen)
                .addGap(95, 95, 95))
        );

        btnHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_home.png"))); // NOI18N
        btnHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHome1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnHome1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHome1)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private String getTanggal() {  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();  
        System.out.println(date);
        return dateFormat.format(date);  
    }
    
    private String getTanggalWaktu() {  
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");  
        Date date = new Date();  
        return dateFormat.format(date);  
    }
 
/* Batas Waktu Absen*/
    
        protected static String selisihDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        String selisih = selisihHari + " hari " + selisihJam + " Jam "
                + selisihMenit + " Menit " + selisihDetik + " Detik";
        return selisih;
    }
       
    protected static Date konversiStringkeDate(String tanggalDanWaktuStr,
            String pola, Locale lokal) {
        Date tanggalDate = null;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        try {
            tanggalDate = formatter.parse(tanggalDanWaktuStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return tanggalDate;
    }
    
    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int sec = Integer.parseInt(hourMin[2]);
        
        int hoursInMins = hour * 60;
        
        int hoursInSec = hour * 60 * 60;
        
        int minsInSec = mins * 60;
        
        return hoursInSec + minsInSec + sec;
    }
/* END of Batas Absen */
    
    private void btnAbsenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbsenActionPerformed
        
        absenModel = new AbsenModel(UserLogin.getUserNik(), getTanggal(), getTanggalWaktu(), getTanggalWaktu());
        
        boolean res;
        
        int waktuAbsen = toMins(getTanggalWaktu());
        System.out.println(waktuAbsen);
        if (waktuAbsen < 21600){
            JOptionPane.showMessageDialog(null, FAILED_ABSEN);
        }else{
            res = absenDao.addAbsen(absenModel);
        
            if(res)
                JOptionPane.showMessageDialog(null, SUCCEED_SAVE_ABSEN);
            else
                JOptionPane.showMessageDialog(null, FAILED_SAVE_ABSEN);
        }       
        
        initAbsenTable();
        getAbsenPegawai(); //tes
        
    }//GEN-LAST:event_btnAbsenActionPerformed

    private void btnHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHome1MouseClicked
        this.setVisible(false);
        HomePage home = new HomePage();
        home.setVisible(true);
    }//GEN-LAST:event_btnHome1MouseClicked

    private void tableAbsenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAbsenMouseClicked
//        int row = tableAbsen.getSelectedRow();
//        String tanggal = modelAbsen.getValueAt(row, 1).toString();
//        AbsenModel absenModel = absenDao.get
    }//GEN-LAST:event_tableAbsenMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        this.setVisible(false);
        UserLogin.doLogout();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if (datePeriodeStart.getDate() == null){
            filterbydate = "";
        }else{
            filterbydate = sdf.format(datePeriodeStart.getDate());
            System.out.println("start: " + filterbydate);
        }
        
        initAbsenTable();
    }//GEN-LAST:event_btnFilterActionPerformed

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
            java.util.logging.Logger.getLogger(AbsenForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AbsenForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AbsenForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AbsenForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AbsenForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbsen;
    private javax.swing.JButton btnFilter;
    private javax.swing.JLabel btnHome1;
    private javax.swing.JLabel btnLogout;
    private com.toedter.calendar.JDateChooser datePeriodeStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label2;
    private javax.swing.JLabel labelJamMasuk;
    private javax.swing.JLabel labelJamPulang;
    private java.awt.Panel panel4;
    private javax.swing.JTable tableAbsen;
    private java.awt.Label userName;
    private java.awt.Label userPosition;
    // End of variables declaration//GEN-END:variables
}
