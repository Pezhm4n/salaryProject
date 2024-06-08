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
        this.name = name;
        this.totalShares = totalShares;
        this.sharesAllocated = sharesAllocated;
        this.sharePrice = sharePrice;
        this.departments = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public double getTotalShares() {
        return totalShares;
    }

    public double getSharesAllocated() {
        return sharesAllocated;
    }

    public double getSharePrice() {
        return sharePrice;
    }

    public ObservableList<Department> getDepartments() {
        return departments;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }
}