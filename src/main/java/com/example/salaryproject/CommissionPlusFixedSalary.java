package com.example.salaryproject;

import java.time.LocalDate;

public class CommissionPlusFixedSalary extends CommissionSalary{
    private double fixedAmount;

    public CommissionPlusFixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double commissionRate, double totalSales, double fixedAmount){
        super(startDate, endDate, department, status, commissionRate, totalSales);
        this.fixedAmount = fixedAmount;
        department.setHeadCount(department.getHeadCount() + 1);
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }
    @Override
    public double calculateTotalSalary(){
        return super.calculateTotalSalary() + fixedAmount;
    }
}
