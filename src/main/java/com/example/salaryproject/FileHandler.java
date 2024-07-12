package com.example.salaryproject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class FileHandler {
    public static final String RESOURCE_DIRECTORY = "src/main/resources/com/example/salaryproject/";

    public static void creatOrganization(Organization organization){
        String directoryPath = RESOURCE_DIRECTORY + organization.getName();
        createFolderIfNotExists(Paths.get(directoryPath));

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(directoryPath + "/" + "organization_info" + ".csv"))
                .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                .build()) {

            List<String[]> data = new ArrayList<>();

            //data.add(new String[]{"Name", "Industry", "Foundation Year", "HeadQuarters", "CEO", "Total Shares", "Share Price"});
            data.add(new  String[]{"Organization:"});
            data.add(new String[]{organization.getName(), organization.getIndustry(), String.valueOf(organization.getFoundationYear()), organization.getHeadquarters(), organization.getCEO(), String.valueOf(organization.getTotalShares()), String.valueOf(organization.getSharePrice())});

            writer.writeAll(data);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!organization.getDepartments().isEmpty()){
        for(Department department : organization.getDepartments())
            writeDepartmentDataToCsv(department);
        }
    }
    private static void createFolderIfNotExists(Path folderPath) {
        if (folderPath != null) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void deleteOrganization(Organization organization) {
        if (organization != null) {
            Path directoryPath = Paths.get(RESOURCE_DIRECTORY + organization.getName());
            try {
                Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Organization folder deleted successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteDepartment(Department department) {
        if (department != null) {
            try {
                Files.deleteIfExists(Paths.get(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void updateFieldOfOrganization(Organization organization, int fieldIndex, String newValue){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + organization.getName() + "/" + "organization_info" + ".csv");
            data.get(1)[fieldIndex] = newValue;
        writeCSV(RESOURCE_DIRECTORY + organization.getName() + "/" + "organization_info" + ".csv", data);
    }

    public static void writeDepartmentDataToCsv(Department department) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv")
        )
                .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                .build()) {

            List<String[]> data = new ArrayList<>();
            
//            Each department is listed with its basic details, followed by its financial records, and then its current manager, employees, former managers, former employees with their salary records.",
//                Structure:
//                1. Department Information: Name, Head Count, Capacity, Description
//                2. Financial Records: Start Date, End Date, Budget, Revenue, Costs
//                3. Employee Information: First Name, Last Name, National ID, Date of Birth, Email, Phone Number
//                4. Salary Records:
//                  [1] Fixed Salary: Start Date, End Date, Department Name, Status, Employee Type, Base Monthly Salary, Overtime Hours, Overtime Rate
//                  [2] Commission Salary: Start Date, End Date, Department Name, Status, Employee Type, Commission Rate, Total Sales",
//                  [3] Commission Plus Fixed Salary: Start Date, End Date, Department Name, Status, Employee Type, Base Monthly Salary, Commission Rate, Total Sales
//                  [4] Hourly Salary: Start Date, End Date, Department Name, Status, Employee Type, Hourly Rate, Hours Worked
//                  [5] Manager Salary: Start Date, End Date, Department Name, Status, Employee Type, Base Monthly Salary, Commission Rate, Department Net Profit, Shares Granted, Current Share Price, Bonus
//                
            
            data.add(new String[]{"Department:"});
            data.add(new String[]{department.getName(), String.valueOf(department.getHeadCount()), String.valueOf(department.getCapacity()), department.getDescription()});
            data.add(new String[]{""});  // Blank line

            // Write financial records
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            data.add(new String[]{"Financial Records:"});
            for (FinancialRecord record : department.getFinancialRecords()) {
                data.add(new String[]{record.getStartDate().toString(), record.getEndDate() != null ? record.getEndDate().toString() : "", decimalFormat.format(record.getBudget()), record.getRevenue() != null ? decimalFormat.format(record.getRevenue()) : "", record.getCosts() != null ? decimalFormat.format(record.getCosts()) : ""});
            }
            data.add(new String[]{""});  // Blank line

            data.add(new String[]{"Current Manager:"});
            if (department.getCurrentManager() != null)
                data.addAll(employeeInfoToCSVLines(department.getCurrentManager()));
            data.add(new String[]{""});  // Blank line

            // Write employees and their salary records
            data.add(new String[]{"Employees:"});
            List<Employee> employees = department.getEmployees();
            for (Employee employee : employees) {
                data.addAll(employeeInfoToCSVLines(employee));
                data.add(new String[]{""});  // Blank line
            }
            if(employees.isEmpty())
                data.add(new String[]{""});  // Blank line

            data.add(new String[]{"Former Managers:"});
            List<Employee> formerManagers = department.getFormerManagers();
            for (int i = 0; i < formerManagers.size(); i++) {
                Employee formerManager = formerManagers.get(i);
                data.addAll(employeeInfoToCSVLines(formerManager));
                data.add(new String[]{""});  // Blank line
            }
            if(formerManagers.isEmpty())
                data.add(new String[]{""});  // Blank line

            data.add(new String[]{"Former Employees:"});
            List<Employee> formerEmployees = department.getFormerEmployees();
            for (Employee formerEmployee : formerEmployees) {
                data.addAll(employeeInfoToCSVLines(formerEmployee));
                data.add(new String[]{""});  // Blank line
            }
            if(formerEmployees.isEmpty())
                data.add(new String[]{""});  // Blank line

            // Write all the collected data to the CSV file
            writer.writeAll(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static ArrayList<String[]> employeeInfoToCSVLines(Employee employee) {
        ArrayList<String[]> neWEmployeeInfo = new ArrayList<>();

        // Write employee information
        neWEmployeeInfo.add(new String[]{employee.getFirstName(), employee.getLastName(), String.valueOf(employee.getNationalId()), employee.getDateOfBirth().toString(), employee.getEmail(), employee.getPhoneNumber()});
        for (SalaryRecord record : employee.getSalaryRecords()) {
            neWEmployeeInfo.add(salaryRecordToCSVLine(record));
        }

        return neWEmployeeInfo;
    }

    public static String[] salaryRecordToCSVLine(SalaryRecord record){
        ArrayList<String> salaryData = new ArrayList<>();
        salaryData.add(record.getStartDate().toString());
        salaryData.add(record.getEndDate() != null ? record.getEndDate().toString() : "");
        salaryData.add(record.getDepartment().getName());
        salaryData.add(record.getStatus().toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (record instanceof FixedSalary) {
            salaryData.add(String.valueOf(EmployeeType.FIXED_SALARY));
            salaryData.add(decimalFormat.format(((FixedSalary) record).getBaseMonthlySalary()));
            salaryData.add(decimalFormat.format(((FixedSalary) record).getOverTimeHours()));
            salaryData.add(decimalFormat.format(((FixedSalary) record).getOverTimeRate()));
        } else if (record.getClass() == CommissionPlusFixedSalary.class) {
            salaryData.add(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY));
            salaryData.add(decimalFormat.format(((CommissionPlusFixedSalary) record).getFixedAmount()));
            salaryData.add(decimalFormat.format(((CommissionPlusFixedSalary) record).getCommissionRate()));
            salaryData.add(decimalFormat.format(((CommissionPlusFixedSalary) record).getTotalSales()));
        } else if (record.getClass() == CommissionSalary.class) {
            salaryData.add(String.valueOf(EmployeeType.COMMISSION_SALARY));
            salaryData.add(decimalFormat.format(((CommissionSalary) record).getCommissionRate()));
            salaryData.add(decimalFormat.format(((CommissionSalary) record).getTotalSales()));
        } else if (record instanceof HourlySalary) {
            salaryData.add(String.valueOf(EmployeeType.HOURLY_SALARY));
            salaryData.add(decimalFormat.format(((HourlySalary) record).getHourlyRate()));
            salaryData.add(decimalFormat.format(((HourlySalary) record).getHoursWorked()));
        } else if (record instanceof ManagerSalary) {
            salaryData.add(String.valueOf(EmployeeType.MANAGER));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getBaseMonthlySalary()));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getCommissionRate()));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getNetProfitOfDepartment()));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getSharesGranted()));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getCurrentSharePrice()));
            salaryData.add(decimalFormat.format(((ManagerSalary) record).getBonus()));
        }
        return salaryData.toArray(new String[0]);
    }
    public static void updateFieldOfDepartment(Department department, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].startsWith("Department:")) {
                data.get(i + 1)[fieldIndex] = newValue;
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static void addFinancialRecordToDepartment(Department department, FinancialRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inFinancialRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals("Financial Records:")) {
                inFinancialRecordSection = true;
                continue;
            }
            if (inFinancialRecordSection && (data.get(i).length == 0 || (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()))) {
                data.add(i , new String[]{record.getStartDate().toString(), record.getEndDate() != null ? record.getEndDate().toString() : "", String.valueOf(record.getBudget()), record.getRevenue() != null ? record.getRevenue().toString() : "", record.getCosts() != null ? record.getCosts().toString() : ""});
                break;
            }

        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static void removeFinancialRecordFromDepartment(Department department, FinancialRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inFinancialRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length > 0 && data.get(i)[0].equals("Financial Records:")) {
                inFinancialRecordSection = true;
                continue;
            }
            if (inFinancialRecordSection &&  data.get(i).length > 2 && data.get(i)[0].equals(record.getStartDate().toString()) && data.get(i)[1].equals(record.getEndDate().toString())) {
                data.remove(i);
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void updateFieldOfFinancialRecord(Department department, FinancialRecord record, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inFinancialRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length > 0 && data.get(i)[0].equals("Financial Records:")) {
                inFinancialRecordSection = true;
                continue;
            }
            if (inFinancialRecordSection && data.get(i).length > 2 && data.get(i)[0].equals(record.getStartDate().toString())) {
                data.get(i)[fieldIndex] = newValue;
                break;
            }

        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void addEmployeeToDepartment(Department department, Employee newEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");

        for (int i = 0; i < data.size(); i++){
            if (data.get(i)[0].startsWith("Former Managers:")) {
                List<String[]> employeeInfo = employeeInfoToCSVLines(newEmployee);
                if(data.get(i - 2)[0].startsWith("Employees:"))
                    data.addAll(i - 1 , employeeInfo);
                else {
                    employeeInfo.add(new String[]{""});
                    data.addAll(i  , employeeInfo);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void removeEmployeeFromDepartment(Department department, Employee employee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inEmployeesSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].startsWith("Employees:")) {
                inEmployeesSection = true;
            }
            if (inEmployeesSection && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(employee.getNationalId()))) {
                int blankLineCounter = 0;
                while (blankLineCounter < 1) {
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }
                    if(!data.get(i + 1)[0].equals("Former Managers:") || !data.get(i - 1)[0].equals("Employees:"))
                        data.remove(i);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void updateFieldOfEmployee(Department department, Employee employee, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inCurrentManagerSection = false;
        boolean inEmployeesSection = false;
        boolean inFormerManagersSection = false;
        boolean inFormerEmployeesSection = false;

        for (String[] datum : data) {
            // Determine the section we are in
            if (datum.length == 1 && !datum[0].trim().isEmpty()) {
                switch (datum[0]) {
                    case "Current Manager:" -> {
                        inCurrentManagerSection = true;
                        inEmployeesSection = false;
                        inFormerManagersSection = false;
                        inFormerEmployeesSection = false;
                        continue;
                    }
                    case "Employees:" -> {
                        inCurrentManagerSection = false;
                        inEmployeesSection = true;
                        inFormerManagersSection = false;
                        inFormerEmployeesSection = false;
                        continue;
                    }
                    case "Former Managers:" -> {
                        inCurrentManagerSection = false;
                        inEmployeesSection = false;
                        inFormerManagersSection = true;
                        inFormerEmployeesSection = false;
                        continue;
                    }
                    case "Former Employees:" -> {
                        inCurrentManagerSection = false;
                        inEmployeesSection = false;
                        inFormerManagersSection = false;
                        inFormerEmployeesSection = true;
                        continue;
                    }
                }
            }

            // Update the field in the relevant section
            if ((inCurrentManagerSection || inEmployeesSection || inFormerManagersSection || inFormerEmployeesSection) &&
                    datum.length > 2 && datum[2].equals(String.valueOf(employee.getNationalId()))) {
                datum[fieldIndex] = newValue;

                // If we are updating the current manager, mark that we need to handle the employees section
                if (!inCurrentManagerSection)
                    break;

            }
        }

        // Write the updated data back to the CSV
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void addSalaryRecordToEmployee(Department department, Employee employee, SalaryRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(employee.getNationalId()))) {
                inSalaryRecordSection = true;
                continue;
            }
            if (inSalaryRecordSection && (data.get(i).length == 0 || (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()))) {
                data.add(i, salaryRecordToCSVLine(record));
                inSalaryRecordSection = false;

                if(!data.get(i + 2)[0].equals("Employees:"))
                    break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void removeSalaryRecordFromEmployee(Department department, Employee employee, SalaryRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;
        boolean inCurrentManagerSection = false;

        for (int i = 0; i < data.size(); i++) {

            if(data.get(i)[0].equals("Current Manager:"))
                inCurrentManagerSection = true;
            else if(data.get(i)[0].equals("Employees:"))
                inCurrentManagerSection = false;
            else if (data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(employee.getNationalId()))) {
                inSalaryRecordSection = true;
            }
            else if (inSalaryRecordSection && data.get(i).length >= 4 && data.get(i)[0].equals(record.getStartDate().toString()) &&
                    data.get(i)[2].equals(record.getDepartment().getName())) {
                data.remove(i);
                inSalaryRecordSection = false;
                if(!inCurrentManagerSection)
                    break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static void updateFieldOfSalaryRecord(Department department, Employee employee, SalaryRecord record, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if(employee != null) {
                if (data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(employee.getNationalId()))) {
                    inSalaryRecordSection = true;
                } else if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty())
                    inSalaryRecordSection = false;
                else if (inSalaryRecordSection && data.get(i).length >= 4 && data.get(i)[0].equals(record.getStartDate().toString()) &&
                        data.get(i)[2].equals(record.getDepartment().getName())) {

                    data.get(i)[fieldIndex] = newValue;
                }
            }
            else{
                if (data.get(i).length >= 4 && data.get(i)[0].equals(record.getStartDate().toString()) &&
                        data.get(i)[2].equals(record.getDepartment().getName())) {

                    data.get(i)[fieldIndex] = newValue;
                }
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static void changeEmployeeDepartment(Department currentDepartment, Department newDepartment, Employee employee){
        removeEmployeeFromDepartment(currentDepartment, employee);
        addFormerEmployeeToDepartment(newDepartment, employee);
    }

    public static void changeManagerOfDepartment(Employee newManager, Employee formerManager, Department department, boolean becameManagerInSameDepartment) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inCurrentManagerSection = false;
        boolean inEmployeesSection = false;
        boolean inFormerManagersSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals("Current Manager:")) {
                inCurrentManagerSection = true;
            } else if (inCurrentManagerSection && formerManager != null && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(formerManager.getNationalId()))) {
                // Remove current manager details
                int blankLineCounter = 0;
                while (blankLineCounter < 1) {
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }
                    data.remove(i);
                }
                // Add new manager details
                ArrayList<String[]> newManagerInfo = employeeInfoToCSVLines(newManager);
                newManagerInfo.add(new String[]{""});
                data.addAll(i, newManagerInfo);
                inCurrentManagerSection = false;
            }
            else if (!inCurrentManagerSection && data.get(i)[0].equals("Employees:")) {
                inEmployeesSection = true;
            } else if (inEmployeesSection && !becameManagerInSameDepartment && data.get(i)[0].startsWith("Former Managers:")) {
                    List<String[]> employeeInfo = employeeInfoToCSVLines(newManager);
                    if(data.get(i - 2)[0].startsWith("Employees:"))
                        data.addAll(i - 1 , employeeInfo);
                    else {
                        employeeInfo.add(new String[]{""});
                        data.addAll(i  , employeeInfo);
                    }
            }
            else if (inEmployeesSection && becameManagerInSameDepartment && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(newManager.getNationalId()))) {
                // add manager salary record to new manager that already exist in employees section
                int blankLineCounter = 0;
                while (blankLineCounter < 1) {
                    i++;
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }
                }
                data.add(i, salaryRecordToCSVLine(newManager.getCurrentSalaryRecord()));
            }
            else if (data.get(i)[0].equals("Former Managers:")) {
                inEmployeesSection = false;
                inFormerManagersSection = true;
            } else if (inFormerManagersSection && formerManager != null && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(formerManager.getNationalId()))) {
                // Remove former manager details if already present
                int blankLineCounter = 0;
                while (blankLineCounter < 1) {
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }
                    data.remove(i);
                }
            } else if (inFormerManagersSection && formerManager != null && data.get(i)[0].equals("Former Employees:")) {
                // Add former manager to former managers section
                List<String[]> formerManagerInfo = employeeInfoToCSVLines(formerManager);
                if (data.get(i - 2)[0].startsWith("Former Managers:")) {
                    data.addAll(i - 1, formerManagerInfo);
                } else {
                    formerManagerInfo.add(new String[]{""});
                    data.addAll(i, formerManagerInfo);
                }
                break;
            }
        }

        // Write updated data to the CSV
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }
    public static void addFormerEmployeeToDepartment(Department department, Employee formerEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");

        int size = data.size();
        List<String[]> employeeInfo = employeeInfoToCSVLines(formerEmployee);
        if(data.get(size - 2)[0].startsWith("Former Employees:"))
            data.addAll(size - 1, employeeInfo);
        else {
            employeeInfo.add(new String[]{""});
            data.addAll(employeeInfo);
        }

        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static void removeFormerEmployeeFromDepartment(Department department, Employee formerEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv");
        boolean inEmployeesSection = false;

        for (int i = 0; i < data.size(); i++){
            if (data.get(i)[0].startsWith("Former Employees:")) {
                inEmployeesSection = true;
            }
            if (inEmployeesSection && data.get(i).length > 1 && data.get(i)[2].equals(String.valueOf(formerEmployee.getNationalId()))) {
                int blankLineCounter = 0;
                while (blankLineCounter <  1){
                    if(data.get(i).length == 1 && data.get(i)[0].trim().isEmpty())
                        blankLineCounter++;
                    if(i + 1 != data.size() || !data.get(i - 1)[0].equals("Former Employees:"))
                        data.remove(i);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getOrganization().getName() + "/" + department.getName() + ".csv", data);
    }

    public static ArrayList<String[]> readCSV(String filePath) {
        ArrayList<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean dataSectionStarted = false;
            while ((line = reader.readNext()) != null) {
                if (line.length > 0 && (line[0].equals("Organization:") || line[0].equals("Department:") || line[0].equals("Financial Records:") || line[0].equals("Employees:") || line[0].equals("Salary Records:"))) {
                    dataSectionStarted = true;
                }
                if (dataSectionStarted) {
                    data.add(line);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static Organization readOrganizationAndDepartments(String organizationName){

        ArrayList<String[]> organization_info = readCSV(RESOURCE_DIRECTORY + organizationName + "/organization_info.csv");
        Organization organization = new Organization(organization_info.get(1)[0], organization_info.get(1)[1], Integer.parseInt(organization_info.get(1)[2]), organization_info.get(1)[3], organization_info.get(1)[4], Double.parseDouble(organization_info.get(1)[5]), Double.parseDouble(organization_info.get(1)[6]));

        Map<String, Department> departmentMap = new HashMap<>();
        List<Path> csvFilePaths = listCSVFilesInDirectory(RESOURCE_DIRECTORY + organizationName);

        for(Path path : csvFilePaths) {
            ArrayList<String[]> data = readCSV(path.toString());

            boolean inCurrentManagerSection = false;
            boolean inEmployeesSection = false;
            boolean inFormerManagersSection = false;
            boolean inFormerEmployeesSection = false;
            Department department = null;

            for (int i = 0; i < data.size(); i++){
                if (data.get(i)[0].startsWith("Department:")) {
                    if(departmentMap.get(data.get(i + 1)[0]) == null) {
                        department = new Department(organization, data.get(i + 1)[0], Integer.parseInt(data.get(i + 1)[1]), Integer.parseInt(data.get(i + 1)[2]), data.get(i + 1)[3]);
                        departmentMap.put(data.get(i + 1)[0], department);
                    }
                    else {
                        department = departmentMap.get(data.get(i + 1)[0]);
                        department.setCapacity(Integer.parseInt(data.get(i + 1)[1]));
                        department.setHeadCount(Integer.parseInt(data.get(i + 1)[2]));
                        department.setDescription(data.get(i + 1)[3]);
                    }
                }
                else if (data.get(i)[0].equals("Financial Records:")) {
                    i++;
                    while(data.get(i).length != 1 && !data.get(i)[0].trim().isEmpty()){
                            department.addFinancialRecord(new FinancialRecord(
                                    LocalDate.parse(data.get(i)[0]),
                                    LocalDate.parse(data.get(i)[1]),
                                    Double.parseDouble(data.get(i)[2]),
                                    Double.parseDouble(data.get(i)[3]),
                                    Double.parseDouble(data.get(i)[4])
                            ));
                        i++;
                    }
                }
                else if(data.get(i)[0].startsWith("Current Manager:")){
                    inCurrentManagerSection = true;
                }
                else if (inCurrentManagerSection && data.get(i).length == 6) {
                    Employee employee = new Employee(data.get(i)[0], data.get(i)[1], Long.parseLong(data.get(i)[2]), LocalDate.parse(data.get(i)[3]), data.get(i)[4], data.get(i)[5]);
                    department.setCurrentManager(employee);
                    i++;
                    while(data.get(i).length != 1 && !data.get(i)[0].trim().isEmpty()){
                        Department dept;
                        if(departmentMap.get(data.get(i)[2]) == null){
                            dept = new Department(organization, data.get(i)[2]);
                            departmentMap.put(data.get(i)[2], dept);
                        }
                        else
                            dept = departmentMap.get(data.get(i)[2]);
                        if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.FIXED_SALARY))){
                            employee.addSalaryRecord(new FixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]),  Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY))){
                            employee.addSalaryRecord(new CommissionPlusFixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_SALARY))){
                            employee.addSalaryRecord(new CommissionSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.HOURLY_SALARY))){
                            employee.addSalaryRecord(new HourlySalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.MANAGER))){
                            employee.addSalaryRecord(new ManagerSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7]), Double.parseDouble(data.get(i)[8]), Double.parseDouble(data.get(i)[9]), Double.parseDouble(data.get(i)[10])));
                        }
                        else if(data.get(i).length == 4){
                            employee.addSalaryRecord(new SalaryRecord(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3])));
                        }
                        i++;
                    }
                }
                else if(data.get(i)[0].startsWith("Employees:")){
                    inEmployeesSection = true;
                    inCurrentManagerSection = false;
                }
                else if (inEmployeesSection && data.get(i).length == 6) {
                    Employee employee = new Employee(data.get(i)[0], data.get(i)[1], Long.parseLong(data.get(i)[2]), LocalDate.parse(data.get(i)[3]), data.get(i)[4], data.get(i)[5]);
                    department.addEmployee(employee);
                    i++;
                    while(data.get(i).length != 1 && !data.get(i)[0].trim().isEmpty()){
                        Department dept;
                        if(departmentMap.get(data.get(i)[2]) == null){
                            dept = new Department(organization, data.get(i)[2]);
                            departmentMap.put(data.get(i)[2], dept);
                        }
                        else
                            dept = departmentMap.get(data.get(i)[2]);
                        if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.FIXED_SALARY))){
                            employee.addSalaryRecord(new FixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]),  Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY))){
                            employee.addSalaryRecord(new CommissionPlusFixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_SALARY))){
                            employee.addSalaryRecord(new CommissionSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.HOURLY_SALARY))){
                            employee.addSalaryRecord(new HourlySalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.MANAGER))){
                            employee.addSalaryRecord(new ManagerSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7]), Double.parseDouble(data.get(i)[8]), Double.parseDouble(data.get(i)[9]), Double.parseDouble(data.get(i)[10])));
                        }
                        else if(data.get(i).length == 4){
                            employee.addSalaryRecord(new SalaryRecord(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3])));
                        }
                        i++;
                    }
                }
                else if(data.get(i)[0].startsWith("Former Managers:")){
                    inFormerManagersSection = true;
                    inEmployeesSection = false;
                }
                else if(inFormerManagersSection && data.get(i).length == 6){
                    Employee employee = new Employee(data.get(i)[0], data.get(i)[1], Long.parseLong(data.get(i)[2]), LocalDate.parse(data.get(i)[3]), data.get(i)[4], data.get(i)[5]);
                    department.addFormerManager(employee);
                    i++;
                    while(data.get(i).length != 1 && !data.get(i)[0].trim().isEmpty()){
                        Department dept;
                        if(departmentMap.get(data.get(i)[2]) == null){
                            dept = new Department(organization, data.get(i)[2]);
                            departmentMap.put(data.get(i)[2], dept);
                        }
                        else
                            dept = departmentMap.get(data.get(i)[2]);
                        if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.FIXED_SALARY))){
                            employee.addSalaryRecord(new FixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]),  Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY))){
                            employee.addSalaryRecord(new CommissionPlusFixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_SALARY))){
                            employee.addSalaryRecord(new CommissionSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.HOURLY_SALARY))){
                            employee.addSalaryRecord(new HourlySalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.MANAGER))){
                            employee.addSalaryRecord(new ManagerSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7]), Double.parseDouble(data.get(i)[8]), Double.parseDouble(data.get(i)[9]), Double.parseDouble(data.get(i)[10])));
                        }
                        else if(data.get(i).length == 4){
                            employee.addSalaryRecord(new SalaryRecord(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3])));
                        }
                        i++;
                    }
                }
                else if(data.get(i)[0].startsWith("Former Employees:")){
                    inFormerEmployeesSection = true;
                    inFormerManagersSection = false;
                }
                else if(inFormerEmployeesSection && data.get(i).length == 6){
                    Employee employee = new Employee(data.get(i)[0], data.get(i)[1], Long.parseLong(data.get(i)[2]), LocalDate.parse(data.get(i)[3]), data.get(i)[4], data.get(i)[5]);
                    department.addFormerEmployee(employee);
                    i++;
                    while(data.get(i).length != 1 && !data.get(i)[0].trim().isEmpty()){
                        Department dept;
                        if(departmentMap.get(data.get(i)[2]) == null){
                            dept = new Department(organization, data.get(i)[2]);
                            departmentMap.put(data.get(i)[2], dept);
                        }
                        else
                            dept = departmentMap.get(data.get(i)[2]);
                        if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.FIXED_SALARY))){
                            employee.addSalaryRecord(new FixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]),  Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY))){
                            employee.addSalaryRecord(new CommissionPlusFixedSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.COMMISSION_SALARY))){
                            employee.addSalaryRecord(new CommissionSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.HOURLY_SALARY))){
                            employee.addSalaryRecord(new HourlySalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6])));
                        }
                        else if(data.get(i).length > 4 && data.get(i)[4].equals(String.valueOf(EmployeeType.MANAGER))){
                            employee.addSalaryRecord(new ManagerSalary(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3]), Double.parseDouble(data.get(i)[5]), Double.parseDouble(data.get(i)[6]), Double.parseDouble(data.get(i)[7]), Double.parseDouble(data.get(i)[8]), Double.parseDouble(data.get(i)[9]), Double.parseDouble(data.get(i)[10])));
                        }
                        else if(data.get(i).length == 4){
                            employee.addSalaryRecord(new SalaryRecord(LocalDate.parse(data.get(i)[0]), data.get(i)[1].isEmpty() ? null: LocalDate.parse(data.get(i)[1]), dept, Status.valueOf(data.get(i)[3])));
                        }
                        i++;
                    }
                }
            }
        organization.addDepartment(department);
        }
        return organization;
        }

    public static List<Path> listCSVFilesInDirectory(String directoryPath) {
        List<Path> csvFilePaths = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".csv") && !file.toString().endsWith("_info.csv")) {
                        csvFilePaths.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFilePaths;
    }

    public static ArrayList<String[]> readCSV(String filePath, boolean skipHeader) {
        ArrayList<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean headerSkipped = !skipHeader;
            while ((line = reader.readNext()) != null) {
                if (headerSkipped) {
                    data.add(line);
                } else {
                    headerSkipped = true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void writeCSV(String filePath, ArrayList<String[]> data) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(filePath))
                .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER)
                .build()) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
