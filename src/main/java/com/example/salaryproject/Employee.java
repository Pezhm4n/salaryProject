package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Employee {
    private String firstName;
    private String lastName;
    private long nationalId;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private ObservableList<SalaryRecord> salaryRecords;

    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, String phoneNumber){
        this(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber, null);
    }

    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, String phoneNumber, ObservableList<SalaryRecord> salaryRecords) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.salaryRecords = salaryRecords == null ? FXCollections.observableArrayList() : salaryRecords;

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

    public String getPhoneNumber() {
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SalaryRecord getCurrentSalaryRecord() {
        if (!salaryRecords.isEmpty()) return salaryRecords.get(salaryRecords.size() - 1);
        return null;
    }

    public void addSalaryRecord(SalaryRecord salaryRecord){
        if(salaryRecord != null) salaryRecords.add(salaryRecord);
    }

    public void removeSalaryRecord(SalaryRecord salaryRecord){
        if(salaryRecord != null) salaryRecords.remove(salaryRecord);
    }
    public double getCurrentSalary() {
        return getCurrentSalaryRecord().calculateTotalSalary();
    }

    public boolean isCurrentManager() {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) return lastRecord instanceof ManagerSalary;
        return false;
    }

    public Department getCurrentDepartment() {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) return lastRecord.getDepartment();
        return null;
    }

    public Status getCurrentStatus() {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) return lastRecord.getStatus();
        return null;
    }

    public EmployeeType getCurrentEmployeeType() {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            if (lastRecord instanceof FixedSalary) return EmployeeType.FIXED_SALARY;
            else if (lastRecord instanceof CommissionPlusFixedSalary) return EmployeeType.COMMISSION_FIXED_SALARY;
            else if (lastRecord instanceof CommissionSalary) return EmployeeType.COMMISSION_SALARY;
            else if (lastRecord instanceof HourlySalary) return EmployeeType.HOURLY_SALARY;
            else if (lastRecord instanceof ManagerSalary) return EmployeeType.MANAGER;
            else return EmployeeType.TERMINATED;
        }
        return null;
    }

    public String getFormattedDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    public void newFixedSalaryRecord(Department newDepartment, Status newStatus, double baseMonthlySalary, double overTimeHours, double overTimeRate) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        boolean inSameDepartment = false;

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else if(lastRecord.getDepartment() != newDepartment){
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
                }
            else
                inSameDepartment = true;

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        FixedSalary newRecord = new FixedSalary(startDate, endDate, newDepartment, newStatus, baseMonthlySalary, overTimeHours, overTimeRate);
        salaryRecords.add(newRecord);

        if(inSameDepartment) {
            WriteToCSV.addSalaryRecordToEmployee(newDepartment, this, newRecord);
        }
        else{
            newDepartment.addEmployee(this);
            WriteToCSV.addEmployeeToDepartment(newDepartment, this);
        }
    }


    public void newCommissionSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        boolean inSameDepartment = false;

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else if(lastRecord.getDepartment() != newDepartment){
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
            }
            else
                inSameDepartment = true;

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        CommissionSalary newRecord = new CommissionSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales);
        salaryRecords.add(newRecord);

        if(inSameDepartment) {
            WriteToCSV.addSalaryRecordToEmployee(newDepartment, this, newRecord);
        }
        else {
            newDepartment.addEmployee(this);
            WriteToCSV.addEmployeeToDepartment(newDepartment, this);
        }
    }


    public void newCommissionPlusFixedSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales, double fixedAmount) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        boolean inSameDepartment = false;

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else if(lastRecord.getDepartment() != newDepartment){
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
            }
            else
                inSameDepartment = true;

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        CommissionPlusFixedSalary newRecord = new CommissionPlusFixedSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales, fixedAmount);
        salaryRecords.add(newRecord);

        if(inSameDepartment) {
            WriteToCSV.addSalaryRecordToEmployee(newDepartment, this, newRecord);
        }
        else {
            newDepartment.addEmployee(this);
            WriteToCSV.addEmployeeToDepartment(newDepartment, this);
        }
    }


    public void newHourlySalaryRecord(Department newDepartment, Status newStatus, double hourlyRate, double hoursWorked) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        boolean inSameDepartment = false;

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else if(lastRecord.getDepartment() != newDepartment){
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
            }
            else
                inSameDepartment = true;

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        HourlySalary newRecord = new HourlySalary(startDate, endDate, newDepartment, newStatus, hourlyRate, hoursWorked);
        salaryRecords.add(newRecord);

    if (newDepartment != null) {
        if (inSameDepartment) {
            WriteToCSV.addSalaryRecordToEmployee(newDepartment, this, newRecord);
        } else {
            newDepartment.addEmployee(this);
            WriteToCSV.addEmployeeToDepartment(newDepartment, this);
        }
    }
    }
    public void newManagerSalaryRecord(Department department, Status newStatus, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        boolean becameManagerInSameDepartment = false;

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (department == null) {
                department = lastRecord.getDepartment();
            } else if(lastRecord.getDepartment() != department){
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
            }
            else
                becameManagerInSameDepartment = true;

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (department == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        ManagerSalary newRecord = new ManagerSalary(startDate, endDate, department, newStatus, baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
        salaryRecords.add(newRecord);

        Employee formerManager = department.getCurrentManager();
        if (formerManager != null)
            department.addFormerManager(formerManager);

        department.setCurrentManager(this);
        WriteToCSV.changeManagerOfDepartment(this, formerManager, department, becameManagerInSameDepartment);

        WriteToCSV.writeDepartmentDataToCsv(department);
    }

    public ObservableList<SalaryRecord> getSalaryRecords() {
        return salaryRecords;
    }

    public void saveToCSV(Department newDepartment) {
        WriteToCSV.addEmployeeToDepartment(newDepartment, this);
    }

    public void setSalaryRecords(List<SalaryRecord> salaryRecords) {
        this.salaryRecords = (ObservableList<SalaryRecord>) salaryRecords;
    }

    public void setDepartment(Department selectedDepartment) {
    }
}



