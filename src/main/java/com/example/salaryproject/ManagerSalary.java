package com.example.salaryproject;

import java.time.LocalDate;

public class ManagerSalary extends SalaryRecord{
    private double baseMonthlySalary;
    private double commissionRate;
    private double netProfitOfDepartment;
    private double sharesGranted;
    private double currentSharePrice;
    private double bonus;

    public ManagerSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment) {
        this(startDate, endDate, department, status, baseMonthlySalary, commissionRate, netProfitOfDepartment, 0, 0, 0);

    }
    public ManagerSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double commissionRate, double netProfitOfDepartment){
        this(startDate, endDate, department, status, 0, commissionRate, netProfitOfDepartment, 0, 0, 0);
    }
    public ManagerSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary){
        this(startDate, endDate, department, status, baseMonthlySalary, 0, 0, 0, 0, 0);
    }
    public ManagerSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus) {
        super(startDate, endDate, department, status);
        this.baseMonthlySalary = baseMonthlySalary;
        this.commissionRate = commissionRate;
        this.netProfitOfDepartment = netProfitOfDepartment;
        this.sharesGranted = sharesGranted;
        this.currentSharePrice = currentSharePrice;
        this.bonus = bonus;
    }

    public double getBaseMonthlySalary() {
        return baseMonthlySalary;
    }

    public void setBaseMonthlySalary(double baseMonthlySalary) {
        this.baseMonthlySalary = baseMonthlySalary;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public double getNetProfitOfDepartment() {
        return netProfitOfDepartment;
    }

    public void setNetProfitOfDepartment(double netProfitOfDepartment) {
        this.netProfitOfDepartment = netProfitOfDepartment;
    }

    public double getSharesGranted() {
        return sharesGranted;
    }

    public void setSharesGranted(double sharesGranted) {
        this.sharesGranted = sharesGranted;
    }

    public double getCurrentSharePrice() {
        return currentSharePrice;
    }

    public void setCurrentSharePrice(double currentSharePrice) {
        this.currentSharePrice = currentSharePrice;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double calculateCommission(){
        return commissionRate * netProfitOfDepartment;
    }
    public double calculateSharesValue(){
        return sharesGranted * currentSharePrice;
    }

    @Override
    public double calculateTotalSalary() {
        return baseMonthlySalary + calculateCommission() + bonus + calculateSharesValue();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nBase Monthly Salary: %.2f\nCommission Rate: %.2f\nNet Profit of Department: %.2f\nShares Granted: %.2f\nCurrent Share Price: %.2f\nBonus: %.2f\n",
                baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
    }
}
