package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee {
    private String firstName;
    private String lastName;
    private long nationalId;
    private LocalDate dateOfBirth;
    private String email;
    private long phoneNumber;
    private ObservableList<SalaryRecord> salaryRecords;

    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, long phoneNumber){
        this(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber, null);
    }
    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, long phoneNumber, SalaryRecord[] salaryRecords) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.salaryRecords = salaryRecords == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(salaryRecords);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getNationalId() {
        return nationalId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNationalId(long nationalId) {
        this.nationalId = nationalId;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SalaryRecord getCurrentSalaryRecord(){
        if(!salaryRecords.isEmpty())
            return salaryRecords.get(salaryRecords.size() - 1);
        return null;
    }
    public double getCurrentSalary(){
        return getCurrentSalaryRecord().calculateTotalSalary();
    }
    public boolean isCurrentManager(){
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null)
            return lastRecord instanceof ManagerSalary;
        return false;
    }

    public Department getCurrentDepartment(){
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null)
            return lastRecord.getDepartment();
        return null;
    }
    public Status getCurrentStatus(){
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null)
            return lastRecord.getStatus();
        return null;
    }
    public EmployeeType getCurrentEmployeeType(){
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null){
            if(lastRecord instanceof FixedSalary)
                return EmployeeType.FIXED_SALARY;
            else if (lastRecord instanceof CommissionPlusFixedSalary)
                return EmployeeType.COMMISSION_FIXED_SALARY;
            else if(lastRecord instanceof CommissionSalary)
                return EmployeeType.COMMISSION_SALARY;
            else if(lastRecord instanceof HourlySalary)
                return EmployeeType.HOURLY_SALARY;
            else if(lastRecord instanceof ManagerSalary)
                return EmployeeType.MANAGER;
        }
        return null;
    }
    public String getFormattedDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
    public void newFixedSalaryRecord(Department newDepartment, Status newStatus, double baseMonthlySalary, double overTimeHours, double overTimeRate) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());
        }

        if (newDepartment == null) {
            if (lastRecord != null) {
                newDepartment = lastRecord.getDepartment();
            }
        } else {
            if (lastRecord != null) {
                lastRecord.getDepartment().removeEmployee(this);
            }
            newDepartment.addEmployee(this);
        }
        if (newStatus == null) {
            if (lastRecord != null) {
                newStatus = lastRecord.getStatus();
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        FixedSalary newRecord = new FixedSalary(startDate, endDate, newDepartment, newStatus, baseMonthlySalary, overTimeHours, overTimeRate);
        salaryRecords.add(newRecord);
    }

    public void newCommissionSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());
        }

        if (newDepartment == null) {
            if (lastRecord != null) {
                newDepartment = lastRecord.getDepartment();
            }
        }
        if (newStatus == null) {
            if (lastRecord != null) {
                newStatus = lastRecord.getStatus();
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        CommissionSalary newRecord = new CommissionSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales);
        salaryRecords.add(newRecord);
    }

    public void newCommissionPlusFixedSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales, double fixedAmount) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());
        }

        if (newDepartment == null) {
            if (lastRecord != null) {
                newDepartment = lastRecord.getDepartment();
            }
        }
        if (newStatus == null) {
            if (lastRecord != null) {
                newStatus = lastRecord.getStatus();
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        CommissionPlusFixedSalary newRecord = new CommissionPlusFixedSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales, fixedAmount);
        salaryRecords.add(newRecord);
    }

    public void newHourlySalaryRecord(Department newDepartment, Status newStatus, double hourlyRate, double hoursWorked) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());
        }

        if (newDepartment == null) {
            if (lastRecord != null) {
                newDepartment = lastRecord.getDepartment();
            }
        }
        if (newStatus == null) {
            if (lastRecord != null) {
                newStatus = lastRecord.getStatus();
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        HourlySalary newRecord = new HourlySalary(startDate, endDate, newDepartment, newStatus, hourlyRate, hoursWorked);
        salaryRecords.add(newRecord);
    }
    public void newManagerSalaryRecord(Department newDepartment, Status newStatus, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());
        }

        if (newDepartment == null) {
            assert lastRecord != null;
            newDepartment = lastRecord.getDepartment();
        }
        if (newStatus == null) {
            assert lastRecord != null;
            newStatus = lastRecord.getStatus();
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        ManagerSalary newRecord = new ManagerSalary(startDate, endDate, newDepartment, newStatus, baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
        salaryRecords.add(newRecord);
    }

    public ObservableList<SalaryRecord> getSalaryRecords() {
        return salaryRecords;
    }
}



