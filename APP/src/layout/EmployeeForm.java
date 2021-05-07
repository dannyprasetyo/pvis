/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package layout;

import db.EmployeeDAO;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.EmployeeModel;
import model.LoginModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import utils.DBConnection;
import model.UserLogin;

/**
 *
 * @author desianggraenis
 */
public class EmployeeForm extends javax.swing.JFrame {
    EmployeeForm employeeForm;
    public Connection cnt;
    EmployeeModel employeeModel;
    DefaultTableModel modelEmployee;

    public static final String SUCCEED_SAVE_EMPLOYEE = "Data have been saved successfully!";
    public static final String FAILED_SAVE_EMPLOYEE = "Failed to save data!";
    public static final String SUCCEED_DELETE_EMPLOYEE = "Data have been deleted successfully!";
    public static final String FAILED_DELETE_EMPLOYEE = "Failed to delete data!";
    private static final String EMPTY_FIELD = "Please fill empty fields!";
    
    public static enum Act {
        QUERY,
        UPDATE,
        INSERT
    }
    
    EmployeeDAO dao = new EmployeeDAO();
    /**
     * Creates new form EmployeeForm
     */
    public EmployeeForm() {
        initComponents();
        UserLogin.setUserLogin(this.userName, this.userPosition);
        
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        
        DBConnection db = new DBConnection();
        modelEmployee = new DefaultTableModel();
        this.tblEmployee.setModel(modelEmployee);
        modelEmployee.addColumn("NO");
        modelEmployee.addColumn("NIK");
        modelEmployee.addColumn("NAME");
        modelEmployee.addColumn("STATUS");
        modelEmployee.addColumn("POSISI");
        initForm(Act.INSERT);
        initEmployeeTable();
        
        cmbPos.setLightWeightPopupEnabled(false);
        cmbStatus.setLightWeightPopupEnabled(false);
    }
    
    private void initEmployeeTable() {
        EmployeeModel[] emplModels = dao.getEmployee(EmployeeDAO.EMPLOYEE_NIK, filterNik.getText());
        String[] columnNames = {"NO", "NIK", "NAME", "STATUS", "POSISI"};
        
        String[][] data = new String[emplModels.length][5];
        for(int i=0; i<emplModels.length;i++){
            data[i][0] = String.valueOf(i+1);
            data[i][1] = emplModels[i].getEmpNik();
            data[i][2] = emplModels[i].getEmpName();
            data[i][3] = emplModels[i].getEmpStatusString();
            data[i][4] = emplModels[i].getEmpPositionString();
        }
       
        modelEmployee = new DefaultTableModel(data, columnNames);
        this.tblEmployee.setModel(modelEmployee);
    }
    
    private void initForm(Act act){
        boolean status1, status2;
       
        switch(act){
            case QUERY:
                status1 = status2 = false;
                break;
            case UPDATE:
                status1 = false;
                status2 = true;
                break;
            case INSERT:
                status1 = status2 = true;
                break;
            default:
                status1 = status2 = false;
        }   
            
        this.txtNik.setEditable(status1);
        this.txtName.setEditable(status2);
        this.txtPsswd.setEditable(status2);
       
        
//        this.cmbPos.setEnabled(status2);
//        this.cmbStatus.setEnabled(status2);
        //this.cmbPos.setEditable(status2);
        //this.cmbStatus.setEditable(status2);
        
        this.btnSave.setEnabled(status2);
        this.btnReset.setEnabled(status2);
        this.btnDelete.setEnabled(!status2);
        this.btnUpdate.setEnabled(!status2);
        this.txtNik.setText("");
        this.txtName.setText("");
        this.txtPsswd.setText("");
//        this.cmbPos.setSelectedIndex(0);
//        this.cmbStatus.setSelectedIndex(0);
    }
        
    private void setDataEmployeeForm(EmployeeModel employeeModel1, Act act){
        initForm(act);
        this.txtNik.setText(employeeModel1.getEmpNik());
        this.txtName.setText(employeeModel1.getEmpName());
        this.cmbPos.setSelectedIndex(employeeModel1.getEmpPosition());
        this.cmbStatus.setSelectedIndex(Integer.parseInt(employeeModel1.getEmpStatus()));
    }
    
    private EmployeeModel initEmployeeTableByTablePosition(int i){
        System.out.println("Nik : " + modelEmployee.getValueAt(i, 1));
        EmployeeModel employeeModel = dao.getEmployeeByNik((String) modelEmployee.getValueAt(i, 1));
        return employeeModel;        
    }
    
    private boolean valdateForm(){
        boolean valid = false;
        if(!txtNik.getText().isEmpty() && !txtName.getText().isEmpty() && !cmbPos.getSelectedItem().toString().isEmpty() && !cmbStatus.getSelectedItem().toString().isEmpty())
            valid = true;
        
        return valid;
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
        btnLogout = new javax.swing.JLabel();
        userName = new java.awt.Label();
        userPosition = new java.awt.Label();
        label2 = new java.awt.Label();
        jLabel14 = new javax.swing.JLabel();
        panel2 = new java.awt.Panel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNik = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        cmbStatus = new javax.swing.JComboBox();
        btnReset = new javax.swing.JButton();
        cmbPos = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        txtPsswd = new javax.swing.JPasswordField();
        btnHome1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        filterNik = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnFilter1 = new javax.swing.JButton();
        btnFilter2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1366, 730));
        setMinimumSize(new java.awt.Dimension(1366, 730));
        setResizable(false);
        setSize(new java.awt.Dimension(1366, 730));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1366, 730));
        jPanel2.setMinimumSize(new java.awt.Dimension(1366, 730));
        jPanel2.setPreferredSize(new java.awt.Dimension(1366, 730));

        panel1.setBackground(new java.awt.Color(217, 228, 250));

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

        jScrollPane2.setBackground(new java.awt.Color(217, 228, 250));

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
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
                "NO", "NIK", "NAME", "STATUS", "POSITION"
            }
        ));
        tblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmployee);
        if (tblEmployee.getColumnModel().getColumnCount() > 0) {
            tblEmployee.getColumnModel().getColumn(0).setMinWidth(35);
            tblEmployee.getColumnModel().getColumn(0).setPreferredWidth(35);
            tblEmployee.getColumnModel().getColumn(0).setMaxWidth(35);
            tblEmployee.getColumnModel().getColumn(1).setMinWidth(120);
            tblEmployee.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblEmployee.getColumnModel().getColumn(1).setMaxWidth(120);
            tblEmployee.getColumnModel().getColumn(2).setMinWidth(300);
            tblEmployee.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblEmployee.getColumnModel().getColumn(2).setMaxWidth(300);
            tblEmployee.getColumnModel().getColumn(3).setMinWidth(150);
            tblEmployee.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblEmployee.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("NIK");

        jLabel3.setText("Name");

        jLabel4.setText("Position");

        jLabel5.setText("Status");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contract", "Permanent" }));

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        cmbPos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Operator Produksi", "Manager", "Keuangan", "Direktur" }));
        cmbPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPosActionPerformed(evt);
            }
        });

        jLabel9.setText("Reset Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbPos, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNik, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPsswd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addGap(34, 34, 34)
                .addComponent(btnReset)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbPos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPsswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnReset))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        btnHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ic_home.png"))); // NOI18N
        btnHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHome1MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("List of Employee");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jButton4.setText("Refresh");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setText("Employee's Form ");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        filterNik.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                filterNikFocusLost(evt);
            }
        });
        filterNik.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                filterNikInputMethodTextChanged(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("NIK");

        btnFilter1.setText("Filter");
        btnFilter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter1ActionPerformed(evt);
            }
        });

        btnFilter2.setText("Reset");
        btnFilter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilter2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(35, 35, 35)
                .addComponent(filterNik, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addGap(60, 60, 60)
                .addComponent(btnFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(341, 341, 341))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilter1)
                    .addComponent(btnFilter2))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(31, 31, 31)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addComponent(btnHome1)
                        .addGap(136, 136, 136)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnUpdate)
                            .addComponent(btnDelete)
                            .addComponent(jButton4)))
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnHome1)))
                .addContainerGap(85, Short.MAX_VALUE))
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
                .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHome1MouseClicked
        this.setVisible(false);
        HomePage home = new HomePage();
        home.setVisible(true);
    }//GEN-LAST:event_btnHome1MouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean res = false;
        if(employeeModel != null)
        res = dao.deleteEmployee(employeeModel);
        
        if(res)
        JOptionPane.showMessageDialog(null, SUCCEED_DELETE_EMPLOYEE);
        else
        JOptionPane.showMessageDialog(null, FAILED_DELETE_EMPLOYEE);
        
        initEmployeeTable();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

        int i = this.tblEmployee.getSelectedRow();
        if(i==-1)
        return;
        employeeModel = initEmployeeTableByTablePosition(i);
        setDataEmployeeForm(employeeModel, Act.UPDATE);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        initForm(Act.INSERT);
    }//GEN-LAST:event_btnAddActionPerformed

    private void cmbPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPosActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        initForm(Act.INSERT);
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(valdateForm() == false){
            JOptionPane.showMessageDialog(null, EMPTY_FIELD);
        }
        else {
            employeeModel = new EmployeeModel(txtNik.getText(),
                txtName.getText(),
                String.valueOf(cmbStatus.getSelectedIndex()),
                Integer.valueOf(cmbPos.getSelectedIndex()),
                txtPsswd.getText());
            boolean res;

            res = dao.addOrUpdateEmployee(employeeModel);
            if(res)
            JOptionPane.showMessageDialog(null, SUCCEED_SAVE_EMPLOYEE);
            else
            JOptionPane.showMessageDialog(null, FAILED_SAVE_EMPLOYEE);

            setDataEmployeeForm(employeeModel, Act.QUERY);
            initEmployeeTable();
         }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void tblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeMouseClicked

        int row = tblEmployee.getSelectedRow();
        String nik = modelEmployee.getValueAt(row, 1).toString();
        EmployeeModel employeeModel = dao.getEmployeeByNik(nik); //new EmployeeModel(nik, name, status, posisi);
        setDataEmployeeForm(employeeModel, Act.QUERY);

    }//GEN-LAST:event_tblEmployeeMouseClicked

    private void filterNikFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filterNikFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_filterNikFocusLost

    private void filterNikInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_filterNikInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_filterNikInputMethodTextChanged

    private void btnFilter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilter1ActionPerformed
        initEmployeeTable();
    }//GEN-LAST:event_btnFilter1ActionPerformed

    private void btnFilter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilter2ActionPerformed
        filterNik.setText("");
        initEmployeeTable();
    }//GEN-LAST:event_btnFilter2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        initEmployeeTable();
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeForm().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter1;
    private javax.swing.JButton btnFilter2;
    private javax.swing.JLabel btnHome1;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cmbPos;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JTextField filterNik;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label label2;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtNik;
    private javax.swing.JPasswordField txtPsswd;
    private java.awt.Label userName;
    private java.awt.Label userPosition;
    // End of variables declaration//GEN-END:variables
    
}
