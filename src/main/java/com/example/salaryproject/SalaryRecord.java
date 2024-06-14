package com.example.salaryproject;

import java.time.LocalDate;

public abstract class SalaryRecord {
    private LocalDate startDate;
    private LocalDate endDate;
    private Department department;
    private Status status;

    public SalaryRecord(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public abstract double calculateTotalSalary();
}

