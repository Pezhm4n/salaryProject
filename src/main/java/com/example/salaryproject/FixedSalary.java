package com.example.salaryproject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FixedSalary extends SalaryRecord {
    private double baseMonthlySalary;
    private double overTimeHours;
    private double overTimeRate;

    public FixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary){
        this(startDate, endDate, department, status, baseMonthlySalary, 0, 0);
    }
    public FixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double baseMonthlySalary, double overTimeHours, double overTimeRate) {
        super(startDate, endDate, department, status);
        this.baseMonthlySalary = baseMonthlySalary;
        this.overTimeHours = overTimeHours;
        this.overTimeRate = overTimeRate;
    }

    public double getBaseMonthlySalary() {
        return baseMonthlySalary;
    }

    public void setBaseMonthlySalary(double baseMonthlySalary) {
        this.baseMonthlySalary = baseMonthlySalary;
    }

    public double getOverTimeHours() {
        return overTimeHours;
    }

    public void setOverTimeHours(double overTimeHours) {
        this.overTimeHours = overTimeHours;
    }

    public double getOverTimeRate() {
        return overTimeRate;
    }

    public void setOverTimeRate(double overTimeRate) {
        this.overTimeRate = overTimeRate;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nBase Monthly Salary: %.2f\nOver Time Hours: %.2f\nOver Time Rate: %.2f\n",
                baseMonthlySalary, overTimeHours, overTimeRate);
    }

    public double calculateSalary(LocalDate periodStart, LocalDate periodEnd) {
        if (getStartDate().isAfter(periodEnd) || getEndDate().isBefore(periodStart)) {
            return 0;
        }

        LocalDate start = getStartDate().isAfter(periodStart) ? getStartDate() : periodStart;
        LocalDate end = getEffectiveEndDate().isBefore(periodEnd) ? getEffectiveEndDate() : periodEnd;

        long totalDays = ChronoUnit.DAYS.between(getStartDate(), getEffectiveEndDate().plusDays(1));
        long periodDays = ChronoUnit.DAYS.between(start, end.plusDays(1));

        return ((double) periodDays / totalDays) * (baseMonthlySalary * (totalDays / 30.0));
    }


}
