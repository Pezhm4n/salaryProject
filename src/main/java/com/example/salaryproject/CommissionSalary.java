package com.example.salaryproject;

import java.time.LocalDate;

public class CommissionSalary extends SalaryRecord {
    private double commissionRate;
    private double totalSales;

    public CommissionSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double commissionRate, double totalSales) {
        super(startDate, endDate, department, status);
        this.commissionRate = commissionRate;
        this.totalSales = totalSales;
        department.setHeadCount(department.getHeadCount() + 1);
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    @Override
    public double calculateTotalSalary() {
        return commissionRate * totalSales;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nCommission Rate: %.2f\nTotal Sales: %.2f\n",
                commissionRate, totalSales);
    }
}

