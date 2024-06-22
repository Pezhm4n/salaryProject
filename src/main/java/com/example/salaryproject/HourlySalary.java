package com.example.salaryproject;

import java.time.LocalDate;

public class HourlySalary extends SalaryRecord {
    private double hourlyRate;

    private double hoursWorked;

    public HourlySalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double hourlyRate, double hoursWorked) {
        super(startDate, endDate, department, status);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        department.setHeadCount(department.getHeadCount() + 1);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateTotalSalary() {
        return hourlyRate * hoursWorked;
    }
}
