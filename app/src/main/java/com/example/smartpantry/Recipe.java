package com.example.smartpantry;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    public long id;
    public String name, steps;
    public List<RecipeIngredient> ingredients = new ArrayList<>();

    // Match status metrics
    public boolean isCookable = false;
    public double matchPercentage = 0.0;
    public int missingCount = 0;
    public List<String> missingIngredients = new ArrayList<>();

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }
}
