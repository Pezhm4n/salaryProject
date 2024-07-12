package com.example.salaryproject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HourlySalary extends SalaryRecord {
    private double hourlyRate;

    private double hoursWorked;

    public HourlySalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double hourlyRate, double hoursWorked) {
        super(startDate, endDate, department, status);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        department.setHeadCount(department.getHeadCount() + 1);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary(LocalDate periodStart, LocalDate periodEnd) {
        if (getStartDate().isAfter(periodEnd) || getEffectiveEndDate().isBefore(periodStart)) {
            return 0;
        }

        LocalDate start = getStartDate().isAfter(periodStart) ? getStartDate() : periodStart;
        LocalDate end = getEffectiveEndDate().isBefore(periodEnd) ? getEffectiveEndDate() : periodEnd;

        long totalDays = ChronoUnit.DAYS.between(getStartDate(), getEffectiveEndDate().plusDays(1));
        long periodDays = ChronoUnit.DAYS.between(start, end.plusDays(1));

        return ((double) periodDays / totalDays) * (hourlyRate * hoursWorked);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nHourly Rate: %.2f\nHours Worked: %.2f\n",
                hourlyRate, hoursWorked);
    }
}
