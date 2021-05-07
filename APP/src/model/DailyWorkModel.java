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
public class DailyWorkModel {
    
    private String empNik;
    private String workDay;
    private int workHours;
    private int spesification;
    private String classification;
    private String lotNo;
    private int materialInputQty;
    private int processedQty;
    private int faultyQty;
    private String note;
    private int cfmFlag;
    private String cfmDate;
    private String cfmUser;

    public static final String [] DwSpecification = {
        "BOX/COVER (GD)",          
        "D/GARNISH (DM,AN,VF, LF,UD D, MD D dan MD QTR)",          
        "PW FRAME (MD, GD)",          
        "SPOILER/BPR (FS,DM, A1)"
    };
    
    public DailyWorkModel(){
        
    }

    public DailyWorkModel(String iNik, String iWorkDay, int iWorkHours, int iSpec, String iClass, String iLotNo, int iMaterial, int iProcessed, int iFaulty, String iNote){
        
        this.empNik = iNik;
        this.workDay = iWorkDay;
        this.workHours = iWorkHours;
        this.spesification = iSpec;
        this.classification = iClass;
        this.lotNo = iLotNo;
        this.materialInputQty = iMaterial;
        this.processedQty = iProcessed;
        this.faultyQty = iFaulty;
        this.note = iNote;
    }
    
    public DailyWorkModel(String iNik, String iWorkDay, int iWorkHours, int iSpec, String iClass, String iLotNo, int iMaterial, int iProcessed, int iFaulty, String iNote, int iCfmFlag, String iCfmDate, String iCfmUser){
        
        this.empNik = iNik;
        this.workDay = iWorkDay;
        this.workHours = iWorkHours;
        this.spesification = iSpec;
        this.classification = iClass;
        this.lotNo = iLotNo;
        this.materialInputQty = iMaterial;
        this.processedQty = iProcessed;
        this.faultyQty = iFaulty;
        this.note = iNote;
        this.cfmFlag = iCfmFlag;
        this.cfmUser = iCfmUser;
        this.cfmDate = iCfmDate;
    }

    /* start of setter and getter emp nik */
    public void setEmpNik(String nik){
        this.empNik = nik;
    }
    
    public String getEmpNik(){
        return this.empNik;
    }
    /* end of setter and getter emp nik */
    
    /* start of setter and getter work day*/
    public void setWorkDay(String workDay){
        this.workDay = workDay;
    }
    
    public String getWorkDay(){
        return this.workDay;
    }
    /* end of setter and getter work day*/
    
    /* start of setter and getter emp nik */
    public void setWorkHours(int hours){
        this.workHours = hours;
    }
    
    public int getWorkHours(){
        return this.workHours;
    }
    /* end of setter and getter work hours*/
    
    /* start of setter and getter spesification*/
    public void setSpesification(int spec){
        this.spesification = spec;
    }
    
    public int getSpesification(){
        return this.spesification;
    }

     public String getSpesificationString(){
        return DwSpecification[this.getSpesification()]; 
    }
    /* end of setter and getter spesification*/
    
    /* start of setter and getter classification*/
    public void setClassification(String classification){
        this.classification = classification;
    }
    
    public String getClassification(){
        return this.classification;
    }
    /* end of setter and getter classification */
    
    /* start of setter and getter lot no*/
    public void setLotNo(String lotNo){
        this.lotNo = lotNo;
    }
    
    public String getLotNo(){
        return this.lotNo;
    }
    /* end of setter and getter lot no*/
    
    /* start of setter and getter material qty*/
    public void setMaterialQty(int qty){
        this.materialInputQty = qty;
    }
    
    public int getMaterialQty(){
        return this.materialInputQty;
    }
    /* end of setter and getter material qty*/
    
    /* start of setter and getter emp nik */
    public void setProcessedQty(int qty){
        this.processedQty = qty;
    }
    
    public int getProcessedQty(){
        return this.processedQty;
    }
    /* end of setter and getter processed qty*/
    
    /* start of setter and getter faulty qty*/
    public void setFaultyQty(int qty){
        this.faultyQty = qty;
    }
    
    public int getFaultyQty(){
        return this.faultyQty;
    }
    /* end of setter and getter faulty qty*/
    
    /* start of setter and getter emp nik */
    public void setNote(String note){
        this.note = note;
    }
    
    public String getNote(){
        return this.note;
    }
    /* end of setter and getter note*/

    /* start of setter and getter confirm flag */
    public void setConfirmFlag(int cfmFlag){
        this.cfmFlag = cfmFlag;
    }
    
    public int getConfirmFlag(){
        return this.cfmFlag;
    }
    /* end of setter and getter confirm flag */

    /* start of setter and getter confirm date */
    public void setConfirmDate(String cfmDate){
        this.cfmDate = cfmDate;
    }
    
    public String getConfirmDate(){
        return this.cfmDate;
    }
    /* end of setter and getter confirm date */

    /* start of setter and getter confirm user */
    public void setConfirmUser(String cfmUser){
        this.cfmUser = cfmUser;
    }
    
    public String getConfirmUser(){
        return this.cfmUser;
    }
    /* end of setter and getter confirm user */

}
