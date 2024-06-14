package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Department {
    private String name;
    private Employee manager;
    private ObservableList<Employee> formerManagers;
    private int headCount;
    private ObservableList<Employee> employees;
    private ObservableList<FinancialRecord> financialRecords;
    private int capacity;
    private String description;
    private Organization organization;

    public Department(String name, int capacity, String description) {
        this(name, capacity, description, null, null, null);
    }

    public Department(String name, int capacity, String description, Employee[] employees, FinancialRecord[] financialRecords, Employee[] formerManagers) {
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.employees = employees == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(employees);
        this.financialRecords = financialRecords == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(financialRecords);
        this.formerManagers = formerManagers == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(formerManagers);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObservableList<Employee> getFormerManagers() {
        return formerManagers;
    }

    public Employee getCurrentManager(){
        for(Employee employee : employees){
            if(employee.isCurrentManager())
                return employee;
        }
        return null;
    }
    public void addFormerManager(Employee formerManager){
        this.formerManagers.add(formerManager);
    }
    public void changeManager(Employee newManager, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus){
        addFormerManager(manager);
        newManager.newManagerSalaryRecord(this, newManager.getCurrentStatus(), baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
        manager = newManager;
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
    }

    public void removeEmployee(Employee employee){
        employees.remove(employee);
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public ObservableList<FinancialRecord> getFinancialRecords() {
        return financialRecords;
    }

    public FinancialRecord getLastFinancialRecord(){
        return financialRecords.get(financialRecords.size() - 1);
    }
    public void addNewFinancialRecord(LocalDate startDate, double budget, double revenue, double costs){
        getLastFinancialRecord().setEndDate(LocalDate.now());
        FinancialRecord newRecord = new FinancialRecord(LocalDate.now(), null, budget, revenue, costs);
        financialRecords.add(newRecord);

    }
    public Organization getOrganization() {
        return organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    public double calculateNetProfit(double revenue, double cost) {
        return revenue - cost;
    }
    public double calculateVariance(double budget, double netProfit) {
        return budget - netProfit;
    }

}