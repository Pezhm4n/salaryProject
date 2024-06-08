package com.example.salaryproject;

import java.time.LocalDate;

public class Employee {
    private String firstName;
    private String lastName;
    private long nationalId;
    private LocalDate dateOfBirth;
    private String email;
    private long phoneNumber;

    public Employee(String firstName, String lastName, long nationalId, LocalDate dateOfBirth, String email, long phoneNumber) {
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
