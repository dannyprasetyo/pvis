/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author desianggraenis
 */
public class ChartModel {
    
    private String month;
    private int miq;
    private int pq;
    private int fq;
    
    public ChartModel(String month, int miq, int pq, int fq){
        this.month = month;
        this.miq = miq;
        this.pq = pq;
        this.fq = fq;
    }
    
    public void setMonth(String month){
        this.month = month;
    }
    
    public String getMonth(){
        return this.month;
    }
    
    public void setMiq(int miq){
        this.miq = miq;
    }
    
    public int getMiq(){
        return this.miq;
    }
    
    public void setPq(int pq){
        this.pq = pq;
    }
    
    public int getPq(){
        return this.pq;
    }
    
    public void setFq(int fq){
        this.fq = fq;
    }
    
    public int getFq(){
        return this.fq;
    }
}
