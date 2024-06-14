package com.example.salaryproject;

import java.time.LocalDate;

public class FixedSalary extends SalaryRecord {
    private double baseMonthlySalary;
    private double overTimeHours;
    private double overTimeRate;

    public FixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary){
        this(startDate, endDate, department, status, baseMonthlySalary, 0, 0);
    }
    public FixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary, double overTimeHours, double overTimeRate) {
        super(startDate, endDate, department, status);
        this.baseMonthlySalary = baseMonthlySalary;
        this.overTimeHours = overTimeHours;
        this.overTimeRate = overTimeRate;
    }

    public double getBaseMonthlySalary() {
        return baseMonthlySalary;
    }

    public void setBaseMonthlySalary(double baseMonthlySalary) {
        this.baseMonthlySalary = baseMonthlySalary;
    }

    public double getOverTimeHours() {
        return overTimeHours;
    }

    public void setOverTimeHours(double overTimeHours) {
        this.overTimeHours = overTimeHours;
    }

    public double getOverTimeRate() {
        return overTimeRate;
    }

    public void setOverTimeRate(double overTimeRate) {
        this.overTimeRate = overTimeRate;
    }

    @Override
    public double calculateTotalSalary() {
        return baseMonthlySalary;
    }
}
