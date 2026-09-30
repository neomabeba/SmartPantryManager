package com.example.smartpantry;

import java.util.Locale;

public class UnitConverter {

    public static String normalizeUnit(String unit) {
        if (unit == null) return "";
        String u = unit.trim().toLowerCase(Locale.US);
        if (u.endsWith("s") && !u.equals("glass") && !u.equals("gas")) {
            u = u.substring(0, u.length() - 1);
        }
        return u;
    }

    private static Double toGram(double qty, String unit) {
        String u = normalizeUnit(unit);
        if (u.equals("g") || u.equals("gram")) return qty;
        if (u.equals("kg") || u.equals("kilogram")) return qty * 1000.0;
        return null;
    }

    private static Double toMl(double qty, String unit) {
        String u = normalizeUnit(unit);
        if (u.equals("ml") || u.equals("milliliter")) return qty;
        if (u.equals("l") || u.equals("liter") || u.equals("litre")) return qty * 1000.0;
        if (u.equals("cup")) return qty * 240.0;
        if (u.equals("tbsp") || u.equals("tablespoon")) return qty * 15.0;
        if (u.equals("tsp") || u.equals("teaspoon")) return qty * 5.0;
        return null;
    }

    public static boolean areCompatible(String unit1, String unit2) {
        String u1 = normalizeUnit(unit1);
        String u2 = normalizeUnit(unit2);
        if (u1.equals(u2)) return true;
        if (toGram(1, u1) != null && toGram(1, u2) != null) return true;
        if (toMl(1, u1) != null && toMl(1, u2) != null) return true;
        return false;
    }

    /**
     * Converts qty from unit1 to target unit2.
     * Returns converted quantity if compatible, or original quantity if units cannot be converted.
     */
    public static double convert(double qty, String fromUnit, String toUnit) {
        String u1 = normalizeUnit(fromUnit);
        String u2 = normalizeUnit(toUnit);
        if (u1.equals(u2)) return qty;

        Double g1 = toGram(qty, u1);
        Double g2 = toGram(1.0, u2);
        if (g1 != null && g2 != null) {
            return g1 / g2;
        }

        Double ml1 = toMl(qty, u1);
        Double ml2 = toMl(1.0, u2);
        if (ml1 != null && ml2 != null) {
            return ml1 / ml2;
        }

        return qty;
    }
}
