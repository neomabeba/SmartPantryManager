package com.example.smartpantry;

public class RecipeIngredient {
    public String name, unit;
    public double quantity;

    // Matching status fields
    public double availableQuantity;
    public String availableUnit;
    public boolean isAvailable;

    public RecipeIngredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}
