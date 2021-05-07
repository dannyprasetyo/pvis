/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author desianggraenis
 */
public class SalaryModel {
    
    private String empNik;
    private String periodeDateStart;
    private String periodeDateEnd;
    private int extension;
    private int nightShift;
    private int overtimeWeekend;
    private int transportFee;
    private int totalHours;
    private double hourlyWageAmount;
    private double extensionAmount;
    private double nightShiftAmount;
    private double overtimeWeekendAmount;
    private double transportFeeAmount;
    private BigDecimal normalFee;
    private double incomeTax;
    private BigDecimal payTotalAmount;
    private double pension;
    private double healthInsurance;
    private double longTermCareInsurance;
    private BigDecimal totalSalary;
    private BigDecimal totalDeduction;
    
    private static final double INCOME_TAX = 0.05;
    private static final double PENSION = 0.037;
    private static final double LONG_TERM_CARE_INSURANCE = 0.005;
    private static final double HEALTH_INSURANCE = 0.02;
    
    public SalaryModel(){
          this.empNik = null;
          this.periodeDateStart = null;
          this.periodeDateEnd = null;
          this.extension= 0;
          this.nightShift= 0;
          this.overtimeWeekend= 0;
          this.transportFee= 0;
          this.totalHours= 0;
          this.hourlyWageAmount= 0;
          this.extensionAmount= 0;
          this.nightShiftAmount= 0;
          this.overtimeWeekendAmount= 0;
          this.transportFeeAmount= 0;
          this.normalFee= BigDecimal.ZERO;
          this.incomeTax= 0;
          this.payTotalAmount= BigDecimal.ZERO;
          this.pension= 0;
          this.healthInsurance= 0;
          this.longTermCareInsurance= 0;
          this.totalSalary= BigDecimal.ZERO;
          this.totalDeduction= BigDecimal.ZERO;
    }
    
    /* start of getter and setter empNik */
    public void setEmpNik(String nik){
        this.empNik = nik;
    }
    
    public String getEmpNik(){
        return this.empNik;
    }
    /* end of getter and setter empNik */
    
    /* start of getter and setter periode date start */
    public void setPeriodeDateStart(String date){
        this.periodeDateStart = date;
    }
    
    public String getPeriodeDateStart(){
        return this.periodeDateStart;
    }
    /* end of getter and setter periode date */
    
    /* start of getter and setter periode date start */
    public void setPeriodeDateEnd(String date){
        this.periodeDateEnd = date;
    }
    
    public String getPeriodeDateEnd(){
        return this.periodeDateEnd;
    }
    /* end of getter and setter periode date */
   
    
     /* start of getter and setter hourly wage */
    public void setExtension(int extension){
        this.extension = extension;
    }
    
    public int getExtension(){
        return this.extension;
    }
    /* end of getter and setter extension */
    
     /* start of getter and setter night shift */
    public void setNightShift(int nightShift){
        this.nightShift = nightShift;
    }
    
    public int getNightShift(){
        return this.nightShift;
    }
    /* end of getter and setter night shift */
    
     /* start of getter and setter overtime */
    public void setOvertimeWeekend(int overtime){
        this.overtimeWeekend = overtime;
    }
    
    public int getOvertimeWeekend(){
        return this.overtimeWeekend;
    }
    /* end of getter and setter overtime */
    
     /* start of getter and setter transport fee*/
    public void setTransportFee(int amount){
        this.transportFee = amount;
    }
    
    public int getTransportFee(){
        return this.transportFee;
    }
    /* end of getter and setter transport fee */
    
      /* start of getter and setter hourly wage amount*/
    public void setHourlyWageAmount(){
        this.hourlyWageAmount = this.totalHours * MasterSalaryAmount.HOURLY_WAGE;
    }
    
    public double getHourlyWageAmount(){
        return this.hourlyWageAmount;
    }
    /* end of getter and setter hourly wage */
    
     /* start of getter and setter Extension amount */
    public void setExtensionAmount(){
        this.extensionAmount = this.extension * MasterSalaryAmount.EXTENSION;
    }
    
    public double getExtensionAmount(){
        return this.extensionAmount;
    }
    /* end of getter and setter extension */
    
     /* start of getter and setter night shift amount*/
    public void setNightShiftAmount(){
        this.nightShiftAmount = this.nightShift * MasterSalaryAmount.NIGHT_SHIFT;
    }
    
    public double getNightShiftAmount(){
        return this.nightShiftAmount;
    }
    /* end of getter and setter night shift */
    
     /* start of getter and setter overtime amount */
    public void setOvertimeWeekendAmount(){
        this.overtimeWeekendAmount = this.overtimeWeekend * MasterSalaryAmount.OVER_TIME_WEEKEND;
    }
    
    public double getOvertimeWeekendAmount(){
        return this.overtimeWeekendAmount;
    }
    /* end of getter and setter overtime amount */
    
     /* start of getter and setter transport fee*/
    public void setTransportFeeAmount(){
        this.transportFeeAmount = this.transportFee * MasterSalaryAmount.TRANSPORT_FEE;
    }
    
    public double getTransportFeeAmount(){
        return this.transportFeeAmount;
    }
    /* end of getter and setter transport fee */
    
     /* start of getter and setter income tax */
    public void setIncomeTax(){
        this.incomeTax =  this.payTotalAmount.doubleValue() * INCOME_TAX;
    }
    
    public double getIncomeTax(){
        return this.incomeTax;
    }
    /* end of getter and setter income tax */
    
     /* start of getter and setter total hours*/
    public void setTotalHours(int hours){
        this.totalHours = hours;
    }
    
    public int getTotalHours(){
        return this.totalHours;
    }
    /* end of getter and setter total hours*/
    
    /* start of getter and setter normal fee*/
    public void setNormalFee(){
        this.normalFee = BigDecimal.valueOf(this.totalHours * MasterSalaryAmount.HOURLY_WAGE);
    }
    
    public BigDecimal getNormalFee(){
        return this.normalFee;
    }
    /* end of getter and setter normal fee*/
    
    /* start of getter and setter pay total amount*/
    public void setPayTotalAmount(){
        this.payTotalAmount = BigDecimal.valueOf(this.normalFee.doubleValue() + this.extensionAmount + this.nightShiftAmount + this.overtimeWeekendAmount);
    }
    
    public BigDecimal getPayTotalAmount(){
        return this.payTotalAmount;
    }
    /* end of getter and setter pay total amount*/
    
    /* start of getter and setter pension */
    public void setPension(){
        this.pension = this.payTotalAmount.doubleValue() * PENSION;
    }
    
    public double getPension(){
        return this.pension;
    }
    /* end of getter and setter pension*/
    
    /* start of getter and setter health insurance*/
    public void setHealthInsurance(){
        this.healthInsurance = this.payTotalAmount.doubleValue() * HEALTH_INSURANCE;
    }
    
    public double getHealthInsurance(){
        return this.healthInsurance;
    }
    /* end of getter and setter health insurance*/
    
    /* start of getter and setter long term care insurance*/
    public void setLongTermCareInsurance(){
        this.longTermCareInsurance = this.payTotalAmount.doubleValue() * LONG_TERM_CARE_INSURANCE;
    }
    
    public double getLongTermCareInsurance(){
        return this.longTermCareInsurance;
    }
    /* end of getter and setter long term care insurance*/
    
    /* start of getter and setter total salary*/
    public void setTotalSalary(){
        this.totalSalary = BigDecimal.valueOf(this.normalFee.doubleValue() + this.transportFeeAmount + this.overtimeWeekendAmount + this.nightShiftAmount - this.totalDeduction.doubleValue());
    }
    
    public BigDecimal getTotalSalary(){
        return this.totalSalary;
    }
    /* end of getter and setter total salary*/
    
    /* start of getter and setter total deduction */
    public void setTotalDeduction(){
        this.totalDeduction = BigDecimal.valueOf(this.healthInsurance + this.pension + this.longTermCareInsurance);
    }
    
    public BigDecimal getTotalDedcuction(){
        return this.totalDeduction;
    }
    /* end of getter and setter total deduction*/
    
}
