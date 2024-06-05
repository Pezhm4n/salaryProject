package com.example.salaryproject;

import java.time.LocalDate;

public class Employee {
    private String firstName;
    private String lastName;
    private int nationalId;
    private LocalDate dateOfBirth;
    private String email;
    private int phoneNumber;

    public Employee(String firstName, String lastName, int nationalId, LocalDate dateOfBirth, String email, int phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    // ...
}
