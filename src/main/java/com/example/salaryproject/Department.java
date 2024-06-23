package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Department {
    private String name;
    private Employee manager;
    private int capacity;
    private int headCount;
    private ObservableList<FinancialRecord> financialRecords;
    private ObservableList<Employee> employees;
    private ObservableList<Employee> formerManagers;
    private ObservableList<Employee> formerEmployees;

    private String description;
    private Organization organization;

    public Department(String name, int capacity, int headCount, String description) {
        this(name, capacity, headCount, description, null, null, null, null);
    }

    public Department(String name, int capacity, int headCount, String description, FinancialRecord[] financialRecords, Employee[] employees, Employee[] formerManagers, Employee[] formerEmployees) {
        this.name = name;
        this.capacity = capacity;
        this.headCount = headCount;
        this.description = description;
        this.employees = employees == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(employees);
        this.financialRecords = financialRecords == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(financialRecords);
        this.formerManagers = formerManagers == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(formerManagers);
        this.formerEmployees = formerEmployees == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(formerEmployees);
        incrementHeadCount();
    }


    public void saveToCSV() {
        WriteToCSV.writeDepartmentDataToCsv(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }
    public void incrementHeadCount(){
        setHeadCount(getHeadCount() + 1);
    }
    public void decrementHeadCount(){
        setHeadCount(getHeadCount() - 1);
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

    public void setCurrentManager(Employee manager){
        this.manager = manager;
    }

    public Employee getCurrentManager(){
        for(Employee employee : employees){
            if(employee.isCurrentManager() && this.manager == employee)
                return employee;
        }
        return null;
    }

    public void changeManager(Employee newManager, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus){
        if (manager != null){
            if(!manager.getSalaryRecords().isEmpty())
                manager.getCurrentSalaryRecord().setEndDate(LocalDate.now());
            addFormerManager(manager);
        }

        newManager.newManagerSalaryRecord(this, newManager.getCurrentStatus(), baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
        manager = newManager;
    }
    public ObservableList<Employee> getFormerManagers() {
        return formerManagers;
    }
    public void addFormerManager(Employee formerManager){
        if(formerManager != null)
            this.formerManagers.add(formerManager);
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        decrementHeadCount();
        formerEmployees.add(employee);
        WriteToCSV.removeEmployeeFromDepartment(this, employee);
        WriteToCSV.addFormerEmployeeToDepartment(this, employee);
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
    public void createNewFinancialRecord(LocalDate startDate, double budget, double revenue, double costs){
        if(!getFinancialRecords().isEmpty())
            getLastFinancialRecord().setEndDate(LocalDate.now());
        FinancialRecord newRecord = new FinancialRecord(startDate, null, budget, revenue, costs);
        addFinancialRecord(newRecord);
    }
    public void addFinancialRecord(FinancialRecord newRecord){
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

    public ObservableList<Employee> getFormerEmployees() {
        return formerEmployees;
    }
}
