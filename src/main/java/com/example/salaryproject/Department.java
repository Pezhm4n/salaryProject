package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class Department {
    private String name;
    private Employee manager;
    private int capacity;
    private int headCount;
    private ObservableList<FinancialRecord> financialRecords;
    private ObservableList<Employee> employees;
    private ObservableList<Employee> formerManagers;
    private ObservableList<Employee> formerEmployees;
    private String workField;
    private Organization organization;

    public Department(Organization organization, String name) {
        this(organization, name, 0, 0, null, null, null, null, null);
    }
    public Department(Organization organization, String name, int capacity, int headCount, String description) {
        this(organization, name, capacity, headCount, description, null, null, null, null);
    }

    public Department(Organization organization,String name, int capacity, int headCount, String description, FinancialRecord[] financialRecords, Employee[] employees, Employee[] formerManagers, Employee[] formerEmployees) {
        this.organization = organization;
        this.name = name;
        this.capacity = capacity;
        this.headCount = headCount;
        this.workField = description;
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
        return workField;
    }

    public void setDescription(String description) {
        this.workField = description;
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

    public void changeManager(Employee newManager, Status status, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus){
        newManager.newManagerSalaryRecord(this, status, baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
    }
    public ObservableList<Employee> getFormerManagers() {
        return formerManagers;
    }
    public void addFormerManager(Employee formerManager){
        if(formerManager != null)
            this.formerManagers.add(formerManager);
    }

    public void addEmployee(Employee employee){
        incrementHeadCount();
        employees.add(employee);
    }

    public void removeEmployee(Employee employee){
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
        this.getLastFinancialRecord().setEndDate(LocalDate.now());
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

    public double calculateTotalBudget() {
        return financialRecords.stream().mapToDouble(FinancialRecord::getBudget).sum();
    }

    public double calculateTotalRevenue() {
        return financialRecords.stream().mapToDouble(record -> record.getRevenue() != null ? record.getRevenue() : 0).sum();
    }

    public double calculateTotalCosts() {
        return financialRecords.stream().mapToDouble(record -> record.getCosts() != null ? record.getCosts() : 0).sum();
    }

    public ObservableList<Employee> getFormerEmployees() {
        return formerEmployees;
    }
    public  void addFormerEmployee(Employee formerEmployee){
        formerEmployees.add(formerEmployee);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = FXCollections.observableArrayList(employees);
    }

    public void setFormerEmployees(List<Employee> formerEmployees) {
        this.formerEmployees = FXCollections.observableArrayList(formerEmployees);
    }


    public void setFinancialRecords(List<FinancialRecord> financialRecords) {
        this.financialRecords = FXCollections.observableArrayList(financialRecords);
    }

}