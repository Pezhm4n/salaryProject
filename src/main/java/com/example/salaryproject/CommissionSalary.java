package com.example.salaryproject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CommissionSalary extends SalaryRecord {
    private double commissionRate;
    private double totalSales;

    public CommissionSalary(LocalDate startDate, LocalDate endDate, Department department, Status status, double commissionRate, double totalSales) {
        super(startDate, endDate, department, status);
        this.commissionRate = commissionRate;
        this.totalSales = totalSales;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
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

        return ((double) periodDays / totalDays) * (commissionRate * totalSales);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nCommission Rate: %.2f\nTotal Sales: %s\n",
                commissionRate, formatNumber(totalSales));
    }
}

