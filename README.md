# Salary Project

A comprehensive Java application for managing and calculating employee salaries.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contribution](#contribution)
- [FAQ](#faq)

## Introduction

Salary Project is a Java-based application designed to help manage employee salary information efficiently. The project includes functionalities to add, update, delete, and calculate salaries for employees in an organization.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 11 or higher
- Maven 3.6.0 or higher
- An IDE such as IntelliJ IDEA or Eclipse

## Installation

To install and set up the project locally, follow these steps:

1. Clone the repository:

    ```bash
    git clone https://github.com/Pezhm4n/salaryProject.git
    ```

2. Navigate to the project directory:

    ```bash
    cd salaryProject
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

## Usage

To run the application, use the following command:

```bash
mvn exec:java -Dexec.mainClass="com.example.salaryproject.Main"
```

### Preview

![Screenshot (1165)](https://github.com/user-attachments/assets/7eac2fd3-c2aa-4526-a75c-6ba92f0ccd14)

![Screenshot (1166)](https://github.com/user-attachments/assets/5aff3876-0879-4ddc-8a86-0f6b9187a31f)

### Default Credentials

The default username and password for logging into the application are:

- **Username:** admin
- **Password:** admin

### Important Note

After creating a new department, you must assign a manager to the department to unlock other functionalities (buttons) on the page.

## Project Structure

Here is an overview of the project's structure:

```plaintext
salaryProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/salaryproject/
│   │   │       ├── AddEmployeePageController.java
│   │   │       ├── CommissionPlusFixedSalary.java
│   │   │       ├── CommissionSalary.java
│   │   │       ├── Department.java
│   │   │       ├── DepartmentPageController.java
│   │   │       ├── DepartmentReportPageController.java
│   │   │       ├── DepartmentSelectionController.java
│   │   │       ├── Employee.java
│   │   │       ├── EmployeeManagementController.java
│   │   │       ├── EmployeeReportPageController.java
│   │   │       ├── EmployeeType.java
│   │   │       ├── FileHandler.java
│   │   │       ├── FinancialRecord.java
│   │   │       ├── FixedSalary.java
│   │   │       ├── HourlySalary.java
│   │   │       ├── LoginPageController.java
│   │   │       ├── Main.java
│   │   │       ├── MainPageController.java
│   │   │       ├── ManagerSalary.java
│   │   │       ├── Organization.java
│   │   │       ├── OrganizationPageController.java
│   │   │       ├── OrganizationReportPageController.java
│   │   │       ├── OrganizationSelectionController.java
│   │   │       ├── RandomGenerator.java
│   │   │       ├── RandomUtil.java
│   │   │       ├── RegisterAdminPageController.java
│   │   │       ├── RegisterDepartmentPageController.java
│   │   │       ├── RegisterOrganizationPageController.java
│   │   │       ├── ReportPageController.java
│   │   │       ├── SalaryRecord.java
│   │   │       ├── Status.java
│   │   │       └── module-info.java
│   │   ├── resources/
│   │   │   └── com/example/salaryproject/
│   │   │       ├── AddEmployeePage.fxml
│   │   │       ├── DepartmentPage.fxml
│   │   │       ├── DepartmentReportPage.fxml
│   │   │       ├── DepartmentSelectionPage.fxml
│   │   │       ├── EmployeeManagement.fxml
│   │   │       ├── EmployeeReportPage.fxml
│   │   │       ├── lock-icon.png
│   │   │       ├── LoginPage.fxml
│   │   │       ├── MainPage.fxml
│   │   │       ├── OrganizationPage.fxml
│   │   │       ├── OrganizationReportPage.fxml
│   │   │       ├── OrganizationSelectionPage.fxml
│   │   │       ├── RegisterAdminPage.fxml
│   │   │       ├── RegisterDepartmentPage.fxml
│   │   │       ├── RegisterOrganizationPage.fxml
│   │   │       ├── ReportPage.fxml
│   │   │       └── user-icon.png
│   └── test/
│       ├── java/
│       │   └── com/example/salaryproject/
│       │       ├── model/
│       │       ├── service/
│       │       └── util/
├── admins.csv
├── pom.xml
└── README.md
```

## Contribution

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some feature'`)
5. Push to the branch (`git push origin feature-branch`)
6. Create a new Pull Request

## FAQ

**Q1: What Java version is required to run this project?**  
A1: The project requires Java 11 or higher.

**Q2: How do I add a new feature or fix a bug?**  
A2: Fork the repository, create a new branch, make your changes, and submit a pull request.

**Q3: Where can I find the main class to run the application?**  
A3: The main class is located in the `com.example.salaryproject` package, typically named `Main.java`.
