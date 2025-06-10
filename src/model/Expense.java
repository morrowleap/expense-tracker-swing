package model;

import java.util.Date;

public abstract class Expense {
    private double amount;
    private Date date;
    private String description;

    public Expense(double amount, Date date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getType();
}
