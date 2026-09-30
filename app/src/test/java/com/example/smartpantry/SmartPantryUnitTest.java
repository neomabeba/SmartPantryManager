package com.example.smartpantry;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmartPantryUnitTest {

    @Test
    public void testUnitConverterMass() {
        // Grams to Kilograms
        assertEquals(0.2, UnitConverter.convert(200, "g", "kg"), 0.001);
        assertEquals(1000, UnitConverter.convert(1, "kg", "g"), 0.001);
        assertTrue(UnitConverter.areCompatible("g", "kg"));
    }

    @Test
    public void testUnitConverterVolume() {
        // Volume conversions
        assertEquals(1.0, UnitConverter.convert(1000, "ml", "l"), 0.001);
        assertEquals(240, UnitConverter.convert(1, "cup", "ml"), 0.001);
        assertEquals(2, UnitConverter.convert(30, "ml", "tbsp"), 0.001);
        assertTrue(UnitConverter.areCompatible("ml", "cups"));
        assertFalse(UnitConverter.areCompatible("g", "ml"));
    }

    @Test
    public void testDateUtilsExpiryStatus() {
        assertEquals(DateUtils.Status.EXPIRED, DateUtils.getExpiryStatus("2020-01-01"));
        assertEquals(DateUtils.Status.FRESH, DateUtils.getExpiryStatus("2035-12-31"));
        assertEquals(DateUtils.Status.UNKNOWN, DateUtils.getExpiryStatus(""));
    }

    @Test
    public void testUnitConverterPluralizationAndNormalization() {
        assertEquals("egg", UnitConverter.normalizeUnit("Eggs"));
        assertEquals("slice", UnitConverter.normalizeUnit("Slices "));
        assertEquals("g", UnitConverter.normalizeUnit("g"));
    }

    @Test
    public void testIngredientModel() {
        Ingredient i = new Ingredient(1, "Eggs", 12.0, "pieces", "2025-12-31");
        assertEquals(1, i.id);
        assertEquals("Eggs", i.name);
        assertEquals(12.0, i.quantity, 0.001);
        assertEquals("pieces", i.unit);
        assertEquals("2025-12-31", i.expiry);
    }

    @Test
    public void testRecipeModelAndMatchMetrics() {
        Recipe r = new Recipe(10, "Pancakes", "Mix flour, eggs and milk.");
        r.ingredients.add(new RecipeIngredient("flour", 1.0, "cups"));
        r.ingredients.add(new RecipeIngredient("eggs", 2.0, "pieces"));

        assertEquals(2, r.ingredients.size());
        assertEquals("Pancakes", r.name);
        assertFalse(r.isCookable);
    }
}
