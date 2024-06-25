package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Organization {
    private String name;
    private String industry;
    private int foundationYear;
    private String headquarters;
    private String CEO;
    private double totalShares;
    private double sharePrice;
    private ObservableList<Department> departments;

    public Organization(String name, String industry, int foundationYear, String headquarters, String CEO, double totalShares, double sharePrice) {
        this(name, industry, foundationYear, headquarters, CEO, totalShares, sharePrice, null);
    }

    public Organization(String name, String industry, int foundationYear, String headquarters, String CEO, double totalShares, double sharePrice, Department[] departments) {
        this.name = name;
        this.industry = industry;
        this.foundationYear = foundationYear;
        this.headquarters = headquarters;
        this.CEO = CEO;
        this.totalShares = totalShares;
        this.sharePrice = sharePrice;
        this.departments = departments == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(departments);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(double totalShares) {
        this.totalShares = totalShares;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public ObservableList<Department> getDepartments() {
        return departments;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
    }

    public double calculateMarketCapitalization() {
        return totalShares * sharePrice;
    }

    public ObservableList<Employee> getCurrentManagers() {
        ObservableList<Employee> managers = FXCollections.observableArrayList();
        for (Department department : departments) {
            managers.add(department.getCurrentManager());
        }
        return managers;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getCEO() {
        return CEO;
    }

    public void setCEO(String CEO) {
        this.CEO = CEO;
    }

    public double calculateTotalCosts() {
        return departments.stream()
                .flatMap(department -> department.getFinancialRecords().stream())
                .mapToDouble(record -> record.getCosts() != null ? record.getCosts() : 0)
                .sum();
    }

    public double calculateTotalRevenue() {
        return departments.stream()
                .flatMap(department -> department.getFinancialRecords().stream())
                .mapToDouble(record -> record.getRevenue() != null ? record.getRevenue() : 0)
                .sum();
    }

    public double calculateTotalBudget() {
        return departments.stream()
                .flatMap(department -> department.getFinancialRecords().stream())
                .mapToDouble(FinancialRecord::getBudget)
                .sum();
    }
    public Employee getEmployeeByNationalID(int nationalID) {
        for (Department department : departments) {
            for (Employee employee : department.getEmployees()) {
                if (employee.getNationalId() == nationalID)
                    return employee;
            }
        }
        return null;
    }
}