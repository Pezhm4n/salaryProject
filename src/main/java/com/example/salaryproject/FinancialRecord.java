package com.example.salaryproject;

import java.time.LocalDate;

public class FinancialRecord {
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private Double revenue;
    private Double costs;

    public FinancialRecord(LocalDate startDate, LocalDate endDate, double budget){
        this(startDate, endDate, budget, null, null);
    }
    public FinancialRecord(LocalDate startDate, LocalDate endDate, double budget, Double revenue, Double costs) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.revenue = revenue;
        this.costs = costs;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getCosts() {
        return costs;
    }

    public void setCosts(Double costs) {
        this.costs = costs;
    }
}
