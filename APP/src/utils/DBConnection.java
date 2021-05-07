/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.*;

/**
 *
 * @author desianggraenis
 */
public class DBConnection {
    public Connection con;
    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String DB = "db_woojin";
    public static final String USER = "root";
    public static final String PASS = "";
    
    public DBConnection(){
        this.connect();
    }
    
    public Connection getConnection()
    {
        return this.con;
    }

    public void connect()
    {
        String koneksi = "jdbc:mysql://"+ HOST + ":" + PORT + "/" + DB;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(koneksi,USER,PASS);

            System.out.println("Koneksi Berhasil");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Koneksi Gagal");
        }
    }

    public void disconnect()
    {
        try {
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet fQuery(String SQL_String) {
        ResultSet record;
        try {
            Statement st = con.createStatement();
            return st.executeQuery(SQL_String);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean fExecute(String SQL_String)
    {
        try {
            Statement st = con.createStatement();
            return  st.execute(SQL_String);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
