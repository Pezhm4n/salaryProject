package com.example.salaryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class RandomGenerator {
    private static final SecureRandom RANDOM = RandomUtil.getInstance();
    private static final List<String> ORGANIZATION_NAMES = List.of("TechCorp", "InnovaTech", "NextGen_Solutions", "InfoMatrix", "FutureWorks");
    private static final List<String> INDUSTRIES = List.of("Software", "Hardware", "IT_Services", "Consulting", "Telecom");
    private static final List<String> HEADQUARTERS = List.of("Tehran", "Mashhad", "Isfahan", "Shiraz", "Tabriz");
    private static final List<String> CEOS = List.of("Ali Rezaei", "Hossein Ahmadi", "Nima Yousefi", "Farhad Kamrani", "Parsa Tavakoli");

    private static final Set<String> USED_ORGANIZATION_NAMES = new HashSet<>();

    public static Organization createRandomOrganization(int numDepartments, int minEmployees, int maxEmployees) {
        String name;
        do {
            name = ORGANIZATION_NAMES.get(RANDOM.nextInt(ORGANIZATION_NAMES.size()));
        } while (USED_ORGANIZATION_NAMES.contains(name));
        USED_ORGANIZATION_NAMES.add(name);
        String industry = INDUSTRIES.get(RANDOM.nextInt(INDUSTRIES.size()));
        int foundationYear = 2010 + RANDOM.nextInt(6); // Random year between 2010 and 2016
        String headquarters = HEADQUARTERS.get(RANDOM.nextInt(HEADQUARTERS.size()));
        String CEO = CEOS.get(RANDOM.nextInt(CEOS.size()));
        double totalShares = 100000 + generateRandomTwoDecimals() * 900000; // Random between 100000 and 1000000
        double sharePrice = 10 + generateRandomTwoDecimals() * 90; // Random between 10 and 100

        Organization organization = new Organization(name, industry, foundationYear, headquarters, CEO, totalShares, sharePrice);
        ObservableList<Department> departments = FXCollections.observableArrayList();
        for (int i = 0; i < numDepartments; i++) {
            departments.add(createRandomDepartmentWithFinancialRecords(organization));
        }
        organization.getDepartments().addAll(departments);
        for(Department department : departments){
            // Adding Manager
            Employee manager = getRandomUniqueEmployee();
            ObservableList<SalaryRecord> mangerSalaryRecords = getRandomSalaryRecords(organization, department, true);
            manager.setSalaryRecords(mangerSalaryRecords);
            department.setCurrentManager(manager);
            // Adding Employees
            ObservableList<Employee> employees = FXCollections.observableArrayList();
            int numEmployees = minEmployees + RANDOM.nextInt(maxEmployees - minEmployees + 1);
            for (int i = 0; i < numEmployees; i++) {
                Employee employee = getRandomUniqueEmployee();
                employees.add(employee);
                ObservableList<SalaryRecord> salaryRecords = getRandomSalaryRecords(organization, department, false);
                employee.setSalaryRecords(salaryRecords);
            }
            department.setEmployees(employees);
            department.addEmployee(manager);
        }

        organization.getDepartments().addAll(departments);

        adjustSalaryRecordsAcrossDepartments(organization);

        FileHandler.creatOrganization(organization);
        return organization;
    }

    private static void adjustSalaryRecordsAcrossDepartments(Organization organization) {
        for (Department department : organization.getDepartments()) {
            ObservableList<Employee> employees = department.getEmployees();
            Employee manager = department.getCurrentManager();

            for (Employee employee : employees) {
                adjustEmployeeSalaryRecords(employee, department);
            }

            if (manager != null) {
                adjustEmployeeSalaryRecords(manager, department);
            }
        }
    }

    private static void adjustEmployeeSalaryRecords(Employee employee, Department currentDepartment) {
        ObservableList<SalaryRecord> salaryRecords = employee.getSalaryRecords();
        for (SalaryRecord record : salaryRecords) {
            Department recordDepartment = record.getDepartment();
            if (!recordDepartment.equals(currentDepartment)) {
                if (employee.equals(currentDepartment.getCurrentManager())) {
                    if (!recordDepartment.getFormerManagers().contains(employee)) {
                        recordDepartment.addFormerManager(employee);
                    }
                } else {
                    if (!recordDepartment.getFormerEmployees().contains(employee)) {
                        recordDepartment.addFormerEmployee(employee);
                    }
                }
            }
        }
    }
    private static final List<String> DEPARTMENTS = Arrays.asList(
            "Engineering", "Product", "Marketing", "Sales", "Human_Resources",
            "Finance", "IT_Support", "Design", "Customer_Support", "Legal",
            "Operations", "Business_Development", "Data_Science", "DevOps",
            "Quality_Assurance", "Research_and_Development", "Security",
            "Public_Relations", "Administration", "Content"
    );

    private static final List<String> DESCRIPTIONS = Arrays.asList(
            "Responsible for product development and engineering.",
            "Manages the product lifecycle from concept to launch.",
            "Drives brand awareness and customer acquisition.",
            "Handles sales strategy and revenue generation.",
            "Manages recruitment, training, and employee relations.",
            "Oversees financial planning, budgeting, and reporting.",
            "Provides technical support and maintenance.",
            "Creates visual and UX designs for products.",
            "Handles customer inquiries and support.",
            "Manages legal issues and ensures compliance.",
            "Optimizes operational efficiency and logistics.",
            "Develops business strategies and partnerships.",
            "Analyzes data to drive decision-making.",
            "Ensures system reliability and continuous integration.",
            "Tests and ensures product quality.",
            "Conducts research and develops new technologies.",
            "Ensures the security of data and systems.",
            "Manages public image and media relations.",
            "Handles administrative tasks and office management.",
            "Creates and manages content for marketing."
    );

    private static final Set<String> USED_DEPARTMENTS = new HashSet<>();

    private static Department createRandomDepartmentWithFinancialRecords(Organization organization) {
        Department department = getRandomUniqueDepartment(organization);

        // Adding Financial Records
        ObservableList<FinancialRecord> financialRecords = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusYears(5).minusDays(RANDOM.nextInt(14)); // Starting from 5 years ago
        LocalDate endDate = currentDate.plusMonths(RANDOM.nextInt(4)).plusDays(RANDOM.nextInt(14)); // Ensure the last end date is around three months after now

        int n = 3 + RANDOM.nextInt(3); // 3 to 5 financial records
        for (int i = 0; i < n; i++) {
            LocalDate tempEndDate = startDate.plusMonths(9 + RANDOM.nextInt(3)).plusDays(RANDOM.nextInt(14)); // 9 to 12 months later
            if (i == n - 1) { // For the last record, ensure it ends around one year after now
                tempEndDate = endDate;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            double budget = 150_000_000 + generateRandomTwoDecimals() * 200_000_000; // Random budget between 150,000,000 and 300,000,000 Toman
            double revenue = 300_000_000 + generateRandomTwoDecimals() * 300_000_000; // Random revenue between 200,000,000 and 600,000,000 Toman
            double costs = 150_000_000 + generateRandomTwoDecimals() * 100_000_000; // Random costs between 150,000,000 and 250,000,000 Toman
            financialRecords.add(new FinancialRecord(startDate, tempEndDate, budget, revenue, costs));
            startDate = tempEndDate.plusDays(1); // Next record's start date after end date of previous record
        }
        department.setFinancialRecords(financialRecords);

        return department;
    }

    public static Department getRandomUniqueDepartment(Organization organization) {
        String name;
        String description;
        do {
            int index = RANDOM.nextInt(DEPARTMENTS.size());
            name = DEPARTMENTS.get(index);
            description = DESCRIPTIONS.get(index);
        } while (USED_DEPARTMENTS.contains(name));
        USED_DEPARTMENTS.add(name);
        return new Department(organization, name, RANDOM.nextInt(50) + 20, 0, description);
    }

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Ali", "Mohammad", "Hassan", "Hossein", "Reza", "Mehdi", "Saeed", "Ehsan", "Morteza", "Yasin",
            "Amir", "Farhad", "Behzad", "Shahin", "Ramin", "Arman", "Pouya", "Nima", "Babak", "Kian",
            "Sara", "Narges", "Fatemeh", "Zahra", "Maryam", "Leila", "Neda", "Mina", "Roya", "Nasim",
            "Parisa", "Shirin", "Farnaz", "Elham", "Samira", "Shadi", "Setareh", "Ladan", "Niloufar", "Simin",
            "Hamideh", "Saba", "Shiva", "Arezoo", "Afsaneh", "Azadeh", "Pegah", "Elaheh", "Sahar", "Taraneh",
            "Majid", "Parviz", "Vahid", "Farshad", "Bahman", "Kaveh", "Dariush", "Peyman", "Shapour", "Kourosh",
            "Fariborz", "Nader", "Kamran", "Kayvan", "Arash", "Behnam", "Pouria", "Houshang", "Bahador", "Soroush",
            "Niloofar", "Kiana", "Firoozeh", "Shaghayegh", "Atila", "Vida", "Sepideh", "Parvaneh", "Farzaneh", "Mahboubeh",
            "Shokoufeh", "Nasim", "Sepideh", "Yasamin", "Taraneh", "Marjan", "Soraya", "Forough", "Bahar", "Nahid",
            "Hasti", "Mahsa", "Kimiya", "Yas", "Mahnaz", "Parichehr", "Roudabeh", "Mandana", "Tina", "Lida"
    );
    static int firstNames_index = 0;

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Ahmadi", "Mohammadi", "Hosseini", "Rahimi", "Ebrahimi", "Rezaei", "Alavi", "Sadeghi", "Karimi", "Mousavi",
            "Jafari", "Gholami", "Pour", "Farhadi", "Azizi", "Fazeli", "Noori", "Naseri", "Ahmadian", "Salehi",
            "Hosseinzadeh", "Bagheri", "Sharifi", "Najafi", "Mohammadzadeh", "Rastegar", "Bahrami", "Khazaei", "Eskandari", "Zarei",
            "Abbasi", "Kazemi", "Khosravi", "Akbari", "Shahbazi", "Tabrizi", "Shirazi", "Kermani", "Mashhadi", "Ranjbar",
            "Khalili", "Ghaffari", "Ashrafi", "Soltani", "Danesh", "Majidi", "Vahidi", "Haghighi", "Rafiei", "Kamali",
            "Moghaddam", "Tavakoli", "Yazdani", "Amini", "Rouhani", "Dolatabadi", "Shokouhi", "Rostami", "Ganjavi", "Faraji",
            "Saberi", "Babaei", "Zamani", "Jamshidi", "Tajik", "Masoumi", "Nikzad", "Javadi", "Golzar", "Abedi",
            "Raeisi", "Kashani", "Asghari", "Kazemzadeh", "Yousefi", "Mazaheri", "Sharifi", "Samimi", "Mostafavi", "Akhlaghi",
            "Azimi", "Sanaei", "Jalali", "Esmaeili", "Amiri", "Etemadi", "Shokri", "Amjad", "Kaviani", "Khoshkhoo",
            "Shadmehr", "Pirouz", "Daryanavard", "Kamrani", "Hemmat", "Rastegari", "Shirazi", "Shahriari", "Aslani", "Javan"
    );
    static int lastNames_index = 0;

        private static final Set<String> USED_NAMES = new HashSet<>();
        private static final Set<String> USED_LAST_NAMES = new HashSet<>();
        private static final Set<String> USED_NATIONAL_IDS = new HashSet<>();

    public static String getRandomUniqueFirstName() {
        String firstName;
        do {
            firstName = FIRST_NAMES.get(firstNames_index);
            firstNames_index = (firstNames_index + 1) % FIRST_NAMES.size(); // Increment and wrap around
        } while (USED_NAMES.contains(firstName));
        USED_NAMES.add(firstName);
        return firstName;
    }

    public static String getRandomUniqueLastName() {
        String lastName;
        do {
            lastName = LAST_NAMES.get(lastNames_index);
            lastNames_index = (lastNames_index + 1) % LAST_NAMES.size(); // Increment and wrap around
        } while (USED_LAST_NAMES.contains(lastName));
        USED_LAST_NAMES.add(lastName);
        return lastName;
    }

        public static String getRandomUniqueNationalId() {
            String nationalId;
            do {
                nationalId = RANDOM.nextInt(9) + 1 + String.format("%09d", RANDOM.nextInt(1_000_000_000));
            } while (USED_NATIONAL_IDS.contains(nationalId));
            USED_NATIONAL_IDS.add(nationalId);
            return nationalId;
        }

        public static String getRandomPhoneNumber() {
            return "09" + String.format("%09d", RANDOM.nextInt(1_000_000_000));
        }

        public static LocalDate getRandomBirthdate() {
            int year = 1970 + RANDOM.nextInt(28); // Random year between 1970 and 1998
            int dayOfYear = 1 + RANDOM.nextInt(365);
            return LocalDate.ofYearDay(year, dayOfYear);
        }

        public static String getRandomEmail(String firstName, String lastName) {
            return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
        }

    private static double generateRandomTwoDecimals() {
            double roundedValue = 0;
        int attempts = 0;
        while (roundedValue == 0 && attempts < 100) { // Limiting attempts to avoid infinite loop
            roundedValue = Math.round(RANDOM.nextDouble() * 100.0) / 100.0;
            attempts++;
        }
        if (roundedValue == 0) {
            return 0.45; // Fallback if all attempts fail
        }
        return roundedValue;
    }

    public static SalaryRecord getRandomFixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        double baseMonthlySalary = 0.0;
        double overTimeHours = 0.0;
        double overTimeRate = 0.0;
        if (Status.UNPAID_LEAVE != status && Status.TERMINATED != status) {
            baseMonthlySalary = 15_000_000 + generateRandomTwoDecimals() * 25_000_000; // Random between 15,000,000 and 40,000,000 Toman
            overTimeHours = 5 + generateRandomTwoDecimals() * 10; // Random between 5 and 15 hours
            overTimeRate = 100_000 + generateRandomTwoDecimals() * 50_000; // Random between 100,000 and 150,000 Toman
        } else if (Status.TERMINATED == status) {
            return new SalaryRecord(startDate, endDate, department, status);
        }
        return new FixedSalary(startDate, endDate, department, status, baseMonthlySalary, overTimeHours, overTimeRate);
    }

    static SalaryRecord getRandomCommissionSalary(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        double commissionRate = 0.0;
        double totalSales = 0.0;
        if (Status.UNPAID_LEAVE != status && Status.TERMINATED != status) {
            commissionRate =  5 + generateRandomTwoDecimals() * 5; // Random between 5% and 10%
            totalSales = 20_000_000 + generateRandomTwoDecimals() * 70_000_000; // Random between 20,000,000 and 90,000,000 Toman
        } else if (Status.TERMINATED == status)
            return new SalaryRecord(startDate, endDate, department, status);
        return new CommissionSalary(startDate, endDate, department, status, commissionRate, totalSales);
    }

    public static SalaryRecord getRandomCommissionPlusFixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        double commissionRate = 0.0;
        double totalSales = 0.0;
        double fixedAmount = 0.0;
        if (Status.UNPAID_LEAVE != status && Status.TERMINATED != status) {
            commissionRate = 5 + generateRandomTwoDecimals() * 5; // Random between 5% and 10%
            totalSales = 20_000_000 + generateRandomTwoDecimals() * 70_000_000; // Random between 20,000,000 and 90,000,000 Toman
            fixedAmount = 15_000_000 + generateRandomTwoDecimals() * 15_000_000; // Random between 15,000,000 and 30,000,000 Toman
        } else if (Status.TERMINATED == status)
            return new SalaryRecord(startDate, endDate, department, status);
        return new CommissionPlusFixedSalary(startDate, endDate, department, status, commissionRate, totalSales, fixedAmount);
    }

    public static SalaryRecord getRandomHourlySalary(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        double hourlyRate = 0.0;
        double hoursWorked = 0.0;
        if (Status.UNPAID_LEAVE != status && Status.TERMINATED != status) {
            hourlyRate = 150_000 + generateRandomTwoDecimals() * 100; // Random between 150,000 and 250,000 Toman
            hoursWorked = 100 + generateRandomTwoDecimals() * 200; // Random between 100 and 200 hours
        } else if (Status.TERMINATED == status)
            return new SalaryRecord(startDate, endDate, department, status);
        return new HourlySalary(startDate, endDate, department, status, hourlyRate, hoursWorked);
    }

    public static SalaryRecord getRandomManagerSalary(LocalDate startDate, LocalDate endDate, Department department, Status status) {
        double baseMonthlySalary = 0.0;
        double commissionRate = 0.0;
        double netProfitOfDepartment = 0.0;
        double sharesGranted = 0.0;
        double currentSharePrice = 0.0;
        double bonus = 0.0;
        if (Status.UNPAID_LEAVE != status && Status.TERMINATED != status) {
            baseMonthlySalary = 40_000_000 + generateRandomTwoDecimals() * 30_000_000; // Random between 40,000,000 and 70,000,000 Toman
            commissionRate = 5 + generateRandomTwoDecimals() * 10; // Random between 5% and 15%
            netProfitOfDepartment = 400_000_000 + generateRandomTwoDecimals() * 700_000_000; // Random between 400,000,000 and 700,000,000 Toman
            sharesGranted = 100 + generateRandomTwoDecimals() * 900; // Random between 100 and 1000 shares
            currentSharePrice = 10 + generateRandomTwoDecimals() * 90; // Random between 10 and 100 Toman per share
            bonus = 5_000_000 + generateRandomTwoDecimals() * 5_000_000; // Random between 5_000,000 and 10,000,000 Toman
        } else if (Status.TERMINATED == status)
            return new SalaryRecord(startDate, endDate, department, status);
        return new ManagerSalary(startDate, endDate, department, status, baseMonthlySalary, commissionRate, netProfitOfDepartment, sharesGranted, currentSharePrice, bonus);
    }

    public static Status getRandomStatus() {
        int roll = RANDOM.nextInt(100);
        if (roll < 70) { // 70% probability for ACTIVE
            return Status.ACTIVE;
        } else if (roll < 85) { // 15% probability for PAID_LEAVE
            return Status.PAID_LEAVE;
        } else if (roll < 95) { // 10% probability for UNPAID_LEAVE
            return Status.UNPAID_LEAVE;
        } else { // 5% probability for TERMINATED
            return Status.TERMINATED;
        }
    }
private static ObservableList<SalaryRecord> getRandomSalaryRecords(Organization organization, Department department, boolean isManager) {
    ObservableList<SalaryRecord> salaryRecords = FXCollections.observableArrayList();
    LocalDate startDate = LocalDate.now().minusYears(3).minusDays(RANDOM.nextInt(14)); // Start 3  years ago
    int recordCount = 2 + RANDOM.nextInt(2); // Between 2 to 3 salary records
    Status status = null;
    for (int i = 0; i < recordCount; i++) {
        do {
            status = getRandomStatus();
        }while(status == Status.TERMINATED && (i == recordCount - 1 || i == 0));
        LocalDate endDate = null;
        if (status != Status.TERMINATED && (status != Status.ACTIVE || i != recordCount - 1)){
            if(i == recordCount - 1)
                endDate = LocalDate.now().plusMonths(RANDOM.nextInt(3)).plusDays(RANDOM.nextInt(14));
            else
                endDate = startDate.plusMonths(9 + RANDOM.nextInt(6)).plusDays(RANDOM.nextInt(14));; // Random end date between 9 to 15 months
        }
        Department recordDepartment;
        if (i == recordCount - 1 || status == Status.TERMINATED || isManager) {
            // Last record should be from the given department
            recordDepartment = department;
        } else {
            // Most records should be from the given department, but some can be from other departments
            if (RANDOM.nextInt(100) < 75) { // 75% chance to be from the given department
                recordDepartment = department;
            } else {
                recordDepartment = getRandomDepartmentFromOrganization(organization, department);
            }
        }
        SalaryRecord salaryRecord;
        if (i == recordCount - 1 && isManager) {
            salaryRecord = getRandomManagerSalary(startDate, endDate, recordDepartment, status);
        } else {
            salaryRecord = getRandomSalaryRecord(startDate, endDate, recordDepartment, status, i == recordCount - 1);
        }
        salaryRecords.add(salaryRecord);

        if (status == Status.TERMINATED || (status == Status.ACTIVE && i == recordCount - 1)) {
            break;
        }
        startDate = endDate.plusDays(1); // Next record's start date after end date of previous record
    }
    return salaryRecords;
}

    private static Department getRandomDepartmentFromOrganization(Organization organization, Department currentDepartment) {
        ObservableList<Department> departments = organization.getDepartments();
        Department randomDepartment;
        do {
            randomDepartment = departments.get(RANDOM.nextInt(departments.size()));
        } while (randomDepartment.equals(currentDepartment)); // Ensure it's not the current department
        return randomDepartment;
    }

    private static SalaryRecord getRandomSalaryRecord(LocalDate startDate, LocalDate endDate, Department department, Status status, boolean isLastRecord) {
        if (isLastRecord) {
            int salaryType = RANDOM.nextInt(100);
            if (salaryType < 65) { // 60% probability for FixedSalary
                return getRandomFixedSalary(startDate, endDate, department, status);
            } else if (salaryType < 85) { // 20% probability for CommissionSalary
                return getRandomCommissionSalary(startDate, endDate, department, status);
            } else if (salaryType < 95) { // 10% probability for CommissionPlusFixedSalary
                return getRandomCommissionPlusFixedSalary(startDate, endDate, department, status);
            } else
                return getRandomHourlySalary(startDate, endDate, department, status);
        } else {
            int salaryType = RANDOM.nextInt(100);
            if (salaryType < 60) { // 60% probability for FixedSalary
                return getRandomFixedSalary(startDate, endDate, department, status);
            } else if (salaryType < 80) { // 20% probability for CommissionSalary
                return getRandomCommissionSalary(startDate, endDate, department, status);
            } else if (salaryType < 90) { // 10% probability for CommissionPlusFixedSalary
                return getRandomCommissionPlusFixedSalary(startDate, endDate, department, status);
            } else if (salaryType < 95) { // 5% probability for HourlySalary
                return getRandomHourlySalary(startDate, endDate, department, status);
            } else { // 5% probability for ManagerSalary
                return getRandomManagerSalary(startDate, endDate, department, status);
            }
        }
    }

    public static Employee getRandomUniqueEmployee() {
        String firstName = getRandomUniqueFirstName();
        String lastName = getRandomUniqueLastName();
        long nationalId = Long.parseLong(getRandomUniqueNationalId());
        LocalDate dateOfBirth = getRandomBirthdate();
        String email = getRandomEmail(firstName, lastName);
        String phoneNumber = getRandomPhoneNumber();

        return new Employee(firstName, lastName, nationalId, dateOfBirth, email, phoneNumber);
    }

    public static void main(String[] args) {
        Organization organization = createRandomOrganization(3, 29, 30);
    }
}

