package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Organization {
    private String name;
    private double totalShares;
    private double sharesAllocated;
    private double sharePrice;
    private ObservableList<Department> departments;

    public Organization(String name, double totalShares, double sharesAllocated, double sharePrice) {
        this(name, totalShares, sharesAllocated, sharePrice, null);
    }

    public Organization(String name, double totalShares, double sharesAllocated, double sharePrice, Department[] departments) {
        this.name = name;
        this.totalShares = totalShares;
        this.sharesAllocated = sharesAllocated;
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

    public double getSharesAllocated() {
        return sharesAllocated;
    }

    public void setSharesAllocated(double sharesAllocated) {
        this.sharesAllocated = sharesAllocated;
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