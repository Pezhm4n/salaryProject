package com.example.salaryproject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CommissionPlusFixedSalary extends CommissionSalary{
    private double fixedAmount;

    public CommissionPlusFixedSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double fixedAmount, double commissionRate, double totalSales){
        super(startDate, endDate, department, status, commissionRate, totalSales);
        this.fixedAmount = fixedAmount;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
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

        return ((double) periodDays / totalDays) * ((getCommissionRate() * getTotalSales()) + (fixedAmount * (totalDays / 30.0)));
    }
    @Override
    public String toString() {
        return super.toString() + String.format("Fixed Amount: %s\n", formatNumber(fixedAmount));
    }
}
