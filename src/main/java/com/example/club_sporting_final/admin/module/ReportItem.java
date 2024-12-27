package com.example.club_sporting_final.admin.module;

public class ReportItem {
    private String name;
    private String category;
    private double amount;
    private String date;
    private String details;

    public ReportItem(String name, String category, double amount, String date, String details) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.details = details;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
