package com.example.salaryproject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WriteToCSV {
    public static final String RESOURCE_DIRECTORY = "src/main/resources/com/example/salaryproject/";

    public static void writeDepartmentDataToCsv(Department department) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(RESOURCE_DIRECTORY + department.getName() + ".csv")
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
//                  [4] Hourly Salary: Start Date, End Date, Department Name, Status, Employee Type, Hourly Rate, Hours Worked",
//                  [5] Manager Salary: Start Date, End Date, Department Name, Status, Employee Type, Base Monthly Salary, Commission Rate, Department Net Profit, Shares Granted, Current Share Price, Bonus
//                
            
            data.add(new String[]{"Department:"});
            data.add(new String[]{department.getName(), String.valueOf(department.getHeadCount()), String.valueOf(department.getCapacity()), department.getDescription()});
            data.add(new String[]{""});  // Blank line

            // Write financial records
            data.add(new String[]{"Financial Records:"});
            for (FinancialRecord record : department.getFinancialRecords()) {
                data.add(new String[]{record.getStartDate().toString(), record.getEndDate() != null ? record.getEndDate().toString() : "", String.valueOf(record.getBudget()), record.getRevenue() != null ? record.getRevenue().toString() : "", record.getCosts() != null ? record.getCosts().toString() : ""});
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
            data.add(new String[]{""});  // Blank line after former employees

            // Write all the collected data to the CSV file
            writer.writeAll(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static ArrayList<String[]> employeeInfoToCSVLines(Employee employee) {
        ArrayList<String[]> neWEmployeeInfo = new ArrayList<>();

        // Write employee information
        neWEmployeeInfo.add(new String[]{employee.getFirstName(), employee.getLastName(), String.valueOf(employee.getNationalId()), employee.getDateOfBirth().toString(), employee.getEmail(), String.valueOf(employee.getPhoneNumber())});
        neWEmployeeInfo.add(new String[]{""});

        // Write salary records
        neWEmployeeInfo.add(new String[]{"Salary Records:"});
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

        if (record instanceof FixedSalary) {
            salaryData.add(String.valueOf(EmployeeType.FIXED_SALARY));
            salaryData.add(String.valueOf(((FixedSalary) record).getBaseMonthlySalary()));
            salaryData.add(String.valueOf(((FixedSalary) record).getOverTimeHours()));
            salaryData.add(String.valueOf(((FixedSalary) record).getOverTimeRate()));
        } else if (record.getClass() == CommissionPlusFixedSalary.class) {
            salaryData.add(String.valueOf(EmployeeType.COMMISSION_FIXED_SALARY));
            salaryData.add(String.valueOf(((CommissionPlusFixedSalary) record).getFixedAmount()));
            salaryData.add(String.valueOf(((CommissionPlusFixedSalary) record).getCommissionRate()));
            salaryData.add(String.valueOf(((CommissionPlusFixedSalary) record).getTotalSales()));
        } else if (record.getClass() == CommissionSalary.class) {
            salaryData.add(String.valueOf(EmployeeType.COMMISSION_SALARY));
            salaryData.add(String.valueOf(((CommissionSalary) record).getCommissionRate()));
            salaryData.add(String.valueOf(((CommissionSalary) record).getTotalSales()));
        } else if (record instanceof HourlySalary) {
            salaryData.add(String.valueOf(EmployeeType.HOURLY_SALARY));
            salaryData.add(String.valueOf(((HourlySalary) record).getHourlyRate()));
            salaryData.add(String.valueOf(((HourlySalary) record).getHoursWorked()));
        } else if (record instanceof ManagerSalary) {
            salaryData.add(String.valueOf(EmployeeType.MANAGER));
            salaryData.add(String.valueOf(((ManagerSalary) record).getCommissionRate()));
            salaryData.add(String.valueOf(((ManagerSalary) record).getNetProfitOfDepartment()));
            salaryData.add(String.valueOf(((ManagerSalary) record).getSharesGranted()));
            salaryData.add(String.valueOf(((ManagerSalary) record).getCurrentSharePrice()));
            salaryData.add(String.valueOf(((ManagerSalary) record).getBonus()));
        }
        return salaryData.toArray(new String[0]);
    }
    public static void updateFieldOfDepartment(Department department, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].startsWith("Department:")) {
                data.get(i + 1)[fieldIndex] = newValue;
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }

    public static void addFinancialRecordToDepartment(Department department, FinancialRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
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
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }

    public static void removeFinancialRecordFromDepartment(Department department, FinancialRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inFinancialRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length > 0 && data.get(i)[0].equals("Financial Records:")) {
                inFinancialRecordSection = true;
                continue;
            }
            if (inFinancialRecordSection &&  data.get(i).length > 2 && data.get(i)[0].equals(record.getStartDate().toString()) && data.get(i)[1].equals(record.getEndDate().toString()) && data.get(i)[2].equals(String.valueOf(record.getBudget()))) {
                data.remove(i);
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void updateFieldOfFinancialRecord(Department department, FinancialRecord record, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inFinancialRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length > 0 && data.get(i)[0].equals("Financial Records:")) {
                inFinancialRecordSection = true;
                continue;
            }
            if (inFinancialRecordSection && data.get(i).length > 2 && data.get(i)[0].equals(record.getStartDate().toString()) && data.get(i)[1].equals(record.getEndDate().toString()) && data.get(i)[2].equals(String.valueOf(record.getBudget()))) {
                data.get(i)[fieldIndex] = newValue;
                break;
            }

        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void addEmployeeToDepartment(Department department, Employee newEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");

        for (int i = 0; i < data.size(); i++){
            if (data.get(i)[0].startsWith("Former Managers:")) {
                List<String[]> employeeInfo = employeeInfoToCSVLines(newEmployee);
                if(data.get(i - 2)[0].startsWith("Former Employees:"))
                    data.addAll(i - 1 , employeeInfo);
                else {
                    employeeInfo.add(new String[]{""});
                    data.addAll(i  , employeeInfo);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void removeEmployeeFromDepartment(Department department, Employee employee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inEmployeesSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].startsWith("Employees:")) {
                inEmployeesSection = true;
            }
            if (inEmployeesSection && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(employee.getNationalId()))) {
                int blankLineCounter = 0;
                while (blankLineCounter < 2) {
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }

                    data.remove(i);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void updateFieldOfEmployee(Department department, Employee employee, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
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
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void addSalaryRecordToEmployee(Department department, Employee employee, SalaryRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals("Salary Records:") &&
                    i >= 2  && data.get(i - 2)[2].equals(String.valueOf(employee.getNationalId()))) {
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
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void removeSalaryRecordFromEmployee(Department department, Employee employee, SalaryRecord record){
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;
        boolean inCurrentManagerSection = false;

        for (int i = 0; i < data.size(); i++) {

            if(data.get(i)[0].equals("Current Manager:"))
                inCurrentManagerSection = true;
            else if(data.get(i)[0].equals("Employees:"))
                inCurrentManagerSection = false;
            else if (data.get(i)[0].equals("Salary Records:") &&
                    i >= 2 && data.get(i - 2).length > 0 && data.get(i - 2)[2].equals(String.valueOf(employee.getNationalId()))) {
                inSalaryRecordSection = true;
            }
            else if (inSalaryRecordSection && data.get(i).length > 4 && data.get(i)[0].equals(record.getStartDate().toString()) &&
                    data.get(i)[1].equals(record.getEndDate().toString()) &&
                    data.get(i)[2].equals(record.getDepartment().getName()) &&
                    data.get(i)[3].equals(record.getStatus().toString()) &&
                    data.get(i)[4].equals(record.getType().toString())) {
                data.remove(i);
                inSalaryRecordSection = false;
                if(!inCurrentManagerSection)
                    break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void updateFieldOfSalaryRecord(Department department, Employee employee, SalaryRecord record, int fieldIndex, String newValue) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inSalaryRecordSection = false;
        boolean inCurrentManagerSection = false;

        for (int i = 0; i < data.size(); i++) {
            if(data.get(i)[0].equals("Current Manager:"))
                inCurrentManagerSection = true;
            else if(data.get(i)[0].equals("Employees:"))
                inCurrentManagerSection = false;
            else if (data.get(i)[0].equals("Salary Records:") &&
                    i >= 2 && data.get(i - 2).length > 0 && data.get(i - 2)[2].equals(String.valueOf(employee.getNationalId()))) {
                inSalaryRecordSection = true;
            }
            else if (inSalaryRecordSection && data.get(i)[0].equals(record.getStartDate().toString()) &&
                    data.get(i)[1].equals(record.getEndDate().toString()) &&
                    data.get(i)[2].equals(record.getDepartment().getName()) &&
                    data.get(i)[3].equals(record.getStatus().toString())) {
                data.get(i)[fieldIndex] = newValue;
                inSalaryRecordSection = false;
                if(!inCurrentManagerSection)
                    break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void changeEmployeeDepartment(Department currentDepartment, Department newDepartment, Employee employee){
        removeEmployeeFromDepartment(currentDepartment, employee);
        addFormerEmployeeToDepartment(newDepartment, employee);
    }

    public static void changeManagerOfDepartment(Employee newManager, Employee formerManager, Department department) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inCurrentManagerSection = false;
        boolean inEmployeesSection = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals("Current Manager:")) {
                inCurrentManagerSection = true;
            }
            else if (inCurrentManagerSection && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(formerManager.getNationalId()))) {
                // Remove current manager details
                int blankLineCounter = 0;
                while (blankLineCounter < 2) {
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }

                    data.remove(i);
                }
                // Add new manager details
                List<String[]> newManagerInfo = employeeInfoToCSVLines(newManager);
                newManagerInfo.add(new String[]{""});
                data.addAll(i, newManagerInfo);
                inCurrentManagerSection = false;
            }
            else if (!inCurrentManagerSection && data.get(i)[0].equals("Employees:")) {
                inEmployeesSection = true;
            }
            else if (inEmployeesSection && data.get(i).length > 2 && data.get(i)[2].equals(String.valueOf(formerManager.getNationalId()))) {
                int blankLineCounter = 0;
                while (blankLineCounter < 2) {
                    data.remove(i);
                    if (data.get(i).length == 1 && data.get(i)[0].trim().isEmpty()) {
                        blankLineCounter++;
                    }
                }
                // Add new manager to employees section
                data.addAll(i, employeeInfoToCSVLines(newManager));
                inEmployeesSection = false;
            }
            else if (!inEmployeesSection && data.get(i)[0].equals("Former Employees:")) {
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
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }
    public static void addFormerEmployeeToDepartment(Department department, Employee formerEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");

        int size = data.size();
        List<String[]> employeeInfo = employeeInfoToCSVLines(formerEmployee);
        if(data.get(size - 2)[0].startsWith("Former Managers:"))
            data.addAll(size - 1, employeeInfo);
        else {
            employeeInfo.add(new String[]{""});
            data.addAll(employeeInfo);
        }

        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }

    public static void removeFormerEmployeeFromDepartment(Department department, Employee formerEmployee) {
        ArrayList<String[]> data = readCSV(RESOURCE_DIRECTORY + department.getName() + ".csv");
        boolean inEmployeesSection = false;

        for (int i = 0; i < data.size(); i++){
            if (data.get(i)[0].startsWith("Former Employees:")) {
                inEmployeesSection = true;
            }
            if (inEmployeesSection && data.get(i).length > 1 && data.get(i)[2].equals(String.valueOf(formerEmployee.getNationalId()))) {
                int blankLineCounter = 0;
                while (blankLineCounter <  2){
                    if(data.get(i).length == 1 && data.get(i)[0].trim().isEmpty())
                        blankLineCounter++;
                    data.remove(i);
                }
                break;
            }
        }
        writeCSV(RESOURCE_DIRECTORY + department.getName() + ".csv", data);
    }

    public static ArrayList<String[]> readCSV(String filePath) {
        ArrayList<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean dataSectionStarted = false;
            while ((line = reader.readNext()) != null) {
                if (line.length > 0 && (line[0].equals("Department:") || line[0].equals("Financial Records:") || line[0].equals("Employees:") || line[0].equals("Salary Records:"))) {
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
        // Create sample data
        Department department = new Department("Engineering", 50, 100, "Handles all engineering tasks");
        Employee currentManager = new Employee("John", "Doe", 123456789, LocalDate.of(1980, 5, 15), "john.doe@example.com", 5551234);
        Employee newManager = new Employee("Jane", "Smith", 987654321, LocalDate.of(1985, 8, 20), "jane.smith@example.com", 5555678);
        Employee employee1 = new Employee("Alice", "Brown", 111223344, LocalDate.of(1990, 10, 25), "alice.brown@example.com", 555-8765);
        FinancialRecord record1 = new FinancialRecord(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 1000000, (double) 1500000, (double) 500000);

        department.setCurrentManager(currentManager);
        department.addEmployee(employee1);
        department.addFinancialRecord(record1);

        // Write department data to CSV
        WriteToCSV.writeDepartmentDataToCsv(department);

        // Update department head count
        WriteToCSV.updateFieldOfDepartment(department, 1, "55");

        // Add new financial record
        FinancialRecord record2 = new FinancialRecord(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 1200000, (double) 1600000, (double) 600000);
        WriteToCSV.addFinancialRecordToDepartment(department, record2);

        // Remove a financial record
        WriteToCSV.removeFinancialRecordFromDepartment(department, record1);

        // Change current manager and add former manager
        department.changeManager(newManager, 2000, 2, 300, 0, 0,0 );
        changeManagerOfDepartment(newManager, currentManager, department);

        // Add and remove employees
        Employee employee2 = new Employee("Bob", "Carter", 222334455, LocalDate.of(1995, 7, 15), "bob.carter@example.com", 5557890);
        WriteToCSV.addEmployeeToDepartment(department, employee2);
        WriteToCSV.removeEmployeeFromDepartment(department, employee1);

        // Add and remove former employees
        WriteToCSV.addFormerEmployeeToDepartment(department, employee1);
        WriteToCSV.removeFormerEmployeeFromDepartment(department, employee1);

        System.out.println("All operations completed successfully.");
    }

}
