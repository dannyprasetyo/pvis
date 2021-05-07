import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
  
/*
 *  @Author Firman Hidayat
 */
  
public class Test {
    // Method menghitung selisih dua waktu
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
    
    private String getTanggalWaktu() {  
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");  
        Date date = new Date();  
        return dateFormat.format(date);  
    }
    
    
  
    public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test();
            }
        });
    }
    
    public Test(){
        System.out.println(getTanggalWaktu());
        System.out.println(toMins(getTanggalWaktu()));
        
        int kambing = toMins(getTanggalWaktu());
        if (kambing > 21600){
            System.out.println("melewati waktu");
        }else{
            System.out.println("bisa nih");
        }
    }
}