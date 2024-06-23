package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private long nationalId;
    private LocalDate dateOfBirth;
    private String email;
    private long phoneNumber;
    private ObservableList<SalaryRecord> salaryRecords;

    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, long phoneNumber) {
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

    public SalaryRecord getCurrentSalaryRecord() {
        if (!salaryRecords.isEmpty()) return salaryRecords.get(salaryRecords.size() - 1);
        return null;
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

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else {
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
                lastDepartment.setHeadCount(lastDepartment.getHeadCount() - 1);
            }

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        if (newDepartment != null) {
            newDepartment.addEmployee(this);
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        FixedSalary newRecord = new FixedSalary(startDate, endDate, newDepartment, newStatus, baseMonthlySalary, overTimeHours, overTimeRate);
        salaryRecords.add(newRecord);
        saveToCSV(newDepartment);
    }


    public void newCommissionSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else {
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
                lastDepartment.setHeadCount(lastDepartment.getHeadCount() - 1);
            }

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        if (newDepartment != null) {
            newDepartment.addEmployee(this);
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        CommissionSalary newRecord = new CommissionSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales);
        salaryRecords.add(newRecord);
        saveToCSV(newDepartment);
    }


    public void newCommissionPlusFixedSalaryRecord(Department newDepartment, Status newStatus, double commissionRate, double totalSales, double fixedAmount) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else {
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
                lastDepartment.setHeadCount(lastDepartment.getHeadCount() - 1);
            }

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        if (newDepartment != null) {
            newDepartment.addEmployee(this);
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        CommissionPlusFixedSalary newRecord = new CommissionPlusFixedSalary(startDate, endDate, newDepartment, newStatus, commissionRate, totalSales, fixedAmount);
        salaryRecords.add(newRecord);
        saveToCSV(newDepartment);
    }


    public void newHourlySalaryRecord(Department newDepartment, Status newStatus, double hourlyRate, double hoursWorked) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();

        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (newDepartment == null) {
                newDepartment = lastRecord.getDepartment();
            } else {
                Department lastDepartment = lastRecord.getDepartment();
                lastDepartment.removeEmployee(this);
                lastDepartment.setHeadCount(lastDepartment.getHeadCount() - 1);
            }

            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        } else {
            // اگر این اولین رکورد حقوق کارمند است
            if (newDepartment == null || newStatus == null) {
                throw new IllegalArgumentException("For the first salary record, both department and status must be provided.");
            }
        }

        if (newDepartment != null) {
            newDepartment.addEmployee(this);
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null; // This should be set to null for new records
        HourlySalary newRecord = new HourlySalary(startDate, endDate, newDepartment, newStatus, hourlyRate, hoursWorked);
        salaryRecords.add(newRecord);
        saveToCSV(newDepartment);
    }

    public void newManagerSalaryRecord(Department department, Status newStatus, double baseMonthlySalary, double commissionRate, double netProfitOfDepartment, double sharesGranted, double currentSharePrice, double bonus) {
        SalaryRecord lastRecord = getCurrentSalaryRecord();
        if (lastRecord != null) {
            lastRecord.setEndDate(LocalDate.now());

            if (department == null) {
                department = Objects.requireNonNull(lastRecord).getDepartment();
            } else {
                department = lastRecord.getDepartment();
            }
            if (newStatus == null) {
                newStatus = lastRecord.getStatus();
            }
        }

        Employee formerManager = department.getCurrentManager();
        department.removeEmployee(this);
        department.decrementHeadCount();
        department.addEmployee(this);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = null;
        ManagerSalary newRecord = new ManagerSalary(startDate, endDate, department, newStatus, baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
        salaryRecords.add(newRecord);

        // انتقال مدیر قبلی به بخش "Former Managers"
        if (formerManager != null) {
            department.addFormerManager(formerManager);
        }

        // فراخوانی متد مناسب برای ذخیره اطلاعات مدیر
        WriteToCSV.changeManagerOfDepartment(this, formerManager, department);
        department.setCurrentManager(this);

        // ذخیره تغییرات در فایل CSV
        WriteToCSV.writeDepartmentDataToCsv(department);
    }


    public ObservableList<SalaryRecord> getSalaryRecords() {
        return salaryRecords;
    }

    public void saveToCSV(Department newDepartment) {
        WriteToCSV.addEmployeeToDepartment(newDepartment, this);
    }

}



