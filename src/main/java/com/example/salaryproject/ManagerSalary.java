package com.example.salaryproject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    public double calculateSalary(LocalDate periodStart, LocalDate periodEnd) {
        if (getStartDate().isAfter(periodEnd) || getEffectiveEndDate().isBefore(periodStart)) {
            return 0;
        }

        LocalDate start = getStartDate().isAfter(periodStart) ? getStartDate() : periodStart;
        LocalDate end = getEffectiveEndDate().isBefore(periodEnd) ? getEffectiveEndDate() : periodEnd;

        long totalDays = ChronoUnit.DAYS.between(getStartDate(), getEffectiveEndDate().plusDays(1));
        long periodDays = ChronoUnit.DAYS.between(start, end.plusDays(1));

        return ((double) periodDays / totalDays) * ((commissionRate * commissionRate) + (baseMonthlySalary * (totalDays / 30.0)) + (sharesGranted + currentSharePrice) + bonus);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nBase Monthly Salary: %s\nCommission Rate: %s\nNet Profit of Department: %s\nShares Granted: %s\nCurrent Share Price: %s\nBonus: %sf\n",
                formatNumber(baseMonthlySalary), formatNumber(commissionRate), formatNumber(netProfitOfDepartment), formatNumber(sharesGranted), formatNumber(currentSharePrice), formatNumber(bonus));
    }
}
