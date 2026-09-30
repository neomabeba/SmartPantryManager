package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB = "smart_pantry.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL)");
        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pantry");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }

    private void seed(SQLiteDatabase db) {
        addRecipe(db, "Egg Fried Rice", "Cook rice, scramble eggs, add vegetables and soy sauce, then stir-fry until hot.",
                new String[]{"rice", "eggs", "carrots", "soy sauce"}, new double[]{2, 2, 1, 1}, new String[]{"cups", "pieces", "pieces", "tbsp"});
        addRecipe(db, "Tomato Omelette", "Whisk eggs with tomato and onion. Cook in a pan until set.",
                new String[]{"eggs", "tomatoes", "onion"}, new double[]{2, 2, 1}, new String[]{"pieces", "pieces", "pieces"});
        addRecipe(db, "Chicken Pasta", "Cook pasta. Fry chicken and tomato, combine with cooked pasta.",
                new String[]{"pasta", "chicken", "tomatoes"}, new double[]{200, 200, 2}, new String[]{"g", "g", "pieces"});
        addRecipe(db, "Vegetable Soup", "Simmer vegetables in stock until tender and season to taste.",
                new String[]{"potatoes", "carrots", "onion", "stock"}, new double[]{2, 2, 1, 500}, new String[]{"pieces", "pieces", "pieces", "ml"});
        addRecipe(db, "Peanut Butter Toast", "Toast bread and spread peanut butter.",
                new String[]{"bread", "peanut butter"}, new double[]{2, 2}, new String[]{"slices", "tbsp"});
        addRecipe(db, "Banana Oatmeal", "Cook oats with milk and top with banana.",
                new String[]{"oats", "milk", "banana"}, new double[]{1, 250, 1}, new String[]{"cups", "ml", "pieces"});
        addRecipe(db, "Tuna Sandwich", "Mix tuna with mayonnaise and place in bread.",
                new String[]{"bread", "tuna", "mayonnaise"}, new double[]{2, 1, 1}, new String[]{"slices", "can", "tbsp"});
        addRecipe(db, "Chicken Salad", "Combine chicken, lettuce, tomato and dressing.",
                new String[]{"chicken", "lettuce", "tomatoes", "dressing"}, new double[]{150, 1, 1, 2}, new String[]{"g", "head", "pieces", "tbsp"});
        addRecipe(db, "Pancakes", "Mix flour, eggs and milk. Cook small portions in a hot pan.",
                new String[]{"flour", "eggs", "milk"}, new double[]{1, 2, 250}, new String[]{"cups", "pieces", "ml"});
        addRecipe(db, "Garlic Pasta", "Cook pasta and toss with garlic and olive oil.",
                new String[]{"pasta", "garlic", "olive oil"}, new double[]{200, 2, 2}, new String[]{"g", "cloves", "tbsp"});
        addRecipe(db, "Grilled Cheese", "Place cheese between bread slices and grill until golden.",
                new String[]{"bread", "cheese", "butter"}, new double[]{2, 2, 1}, new String[]{"slices", "slices", "tbsp"});
        addRecipe(db, "Potato Hash", "Dice potatoes and onion and fry until browned.",
                new String[]{"potatoes", "onion", "oil"}, new double[]{3, 1, 2}, new String[]{"pieces", "pieces", "tbsp"});
        addRecipe(db, "Fruit Smoothie", "Blend banana, berries and milk until smooth.",
                new String[]{"banana", "berries", "milk"}, new double[]{1, 1, 250}, new String[]{"pieces", "cup", "ml"});
        addRecipe(db, "Bean Rice Bowl", "Combine cooked rice with beans and tomato.",
                new String[]{"rice", "beans", "tomatoes"}, new double[]{1, 1, 1}, new String[]{"cups", "cup", "pieces"});
        addRecipe(db, "French Toast", "Dip bread in beaten eggs and milk, then pan-fry.",
                new String[]{"bread", "eggs", "milk"}, new double[]{2, 2, 100}, new String[]{"slices", "pieces", "ml"});
        addRecipe(db, "Creamy Mushroom Pasta", "Cook mushrooms and cream, then combine with pasta.",
                new String[]{"pasta", "mushrooms", "cream"}, new double[]{200, 150, 100}, new String[]{"g", "g", "ml"});
        addRecipe(db, "Chicken Wrap", "Fill a wrap with chicken, lettuce and tomato.",
                new String[]{"wrap", "chicken", "lettuce", "tomatoes"}, new double[]{2, 150, 1, 1}, new String[]{"pieces", "g", "head", "pieces"});
        addRecipe(db, "Avocado Toast", "Mash avocado and spread over toasted bread.",
                new String[]{"bread", "avocado"}, new double[]{2, 1}, new String[]{"slices", "pieces"});
    }

    private void addRecipe(SQLiteDatabase db, String name, String steps, String[] ingredientNames, double[] quantities, String[] units) {
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("steps", steps);
        long recipeId = db.insert("recipes", null, v);
        for (int i = 0; i < ingredientNames.length; i++) {
            ContentValues x = new ContentValues();
            x.put("recipe_id", recipeId);
            x.put("name", ingredientNames[i]);
            x.put("quantity", quantities[i]);
            x.put("unit", units[i]);
            db.insert("recipe_ingredients", null, x);
        }
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> out = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM pantry ORDER BY name", null);
        while (c.moveToNext()) {
            out.add(new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
        }
        c.close();
        return out;
    }

    public long insertIngredient(String n, double q, String u, String e) {
        ContentValues v = new ContentValues();
        v.put("name", n);
        v.put("quantity", q);
        v.put("unit", u);
        v.put("expiry", e);
        return getWritableDatabase().insert("pantry", null, v);
    }

    public int updateIngredient(long id, String n, double q, String u, String e) {
        ContentValues v = new ContentValues();
        v.put("name", n);
        v.put("quantity", q);
        v.put("unit", u);
        v.put("expiry", e);
        return getWritableDatabase().update("pantry", v, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteIngredient(long id) {
        getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    public Ingredient getIngredient(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM pantry WHERE id=?", new String[]{String.valueOf(id)});
        Ingredient x = null;
        if (c.moveToFirst()) {
            x = new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        c.close();
        return x;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> out = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM recipes ORDER BY name", null);
        while (c.moveToNext()) {
            Recipe r = new Recipe(c.getLong(0), c.getString(1), c.getString(2));
            Cursor d = getReadableDatabase().rawQuery("SELECT name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?", new String[]{String.valueOf(r.id)});
            while (d.moveToNext()) {
                r.ingredients.add(new RecipeIngredient(d.getString(0), d.getDouble(1), d.getString(2)));
            }
            d.close();
            out.add(r);
        }
        c.close();
        return out;
    }

    public Recipe getRecipe(long id) {
        List<Recipe> evaluated = getEvaluatedRecipes();
        for (Recipe r : evaluated) {
            if (r.id == id) return r;
        }
        return null;
    }

    /**
     * Evaluates all recipes against the current pantry items using UnitConverter.
     */
    public List<Recipe> getEvaluatedRecipes() {
        List<Recipe> all = getAllRecipes();
        List<Ingredient> pantryList = getIngredients();

        Map<String, Ingredient> pantryMap = new HashMap<>();
        for (Ingredient i : pantryList) {
            pantryMap.put(normalize(i.name), i);
        }

        for (Recipe r : all) {
            int total = r.ingredients.size();
            int matched = 0;
            r.missingIngredients.clear();

            for (RecipeIngredient ri : r.ingredients) {
                Ingredient pantryItem = findPantry(pantryMap, ri.name);
                if (pantryItem != null) {
                    double convertedQty = UnitConverter.convert(pantryItem.quantity, pantryItem.unit, ri.unit);
                    ri.availableQuantity = convertedQty;
                    ri.availableUnit = ri.unit;

                    if (convertedQty + 0.0001 >= ri.quantity) {
                        ri.isAvailable = true;
                        matched++;
                    } else {
                        ri.isAvailable = false;
                        r.missingIngredients.add(ri.name + " (need " + (ri.quantity - convertedQty) + " " + ri.unit + " more)");
                    }
                } else {
                    ri.availableQuantity = 0;
                    ri.availableUnit = ri.unit;
                    ri.isAvailable = false;
                    r.missingIngredients.add(ri.name + " (" + ri.quantity + " " + ri.unit + ")");
                }
            }

            r.missingCount = total - matched;
            r.isCookable = (r.missingCount == 0);
            r.matchPercentage = total > 0 ? ((double) matched / total) * 100.0 : 0.0;
        }

        return all;
    }

    public List<Recipe> getStrictMatches() {
        List<Recipe> evaluated = getEvaluatedRecipes();
        List<Recipe> cookable = new ArrayList<>();
        for (Recipe r : evaluated) {
            if (r.isCookable) cookable.add(r);
        }
        return cookable;
    }

    public List<Recipe> getPartialMatches() {
        List<Recipe> evaluated = getEvaluatedRecipes();
        List<Recipe> partial = new ArrayList<>();
        for (Recipe r : evaluated) {
            if (!r.isCookable && (r.missingCount <= 2 || r.matchPercentage >= 50.0)) {
                partial.add(r);
            }
        }
        return partial;
    }

    /**
     * Deducts the ingredient amounts required by recipe from the user's pantry.
     */
    public boolean cookRecipe(long recipeId) {
        Recipe r = getRecipe(recipeId);
        if (r == null || !r.isCookable) return false;

        Map<String, Ingredient> pantryMap = new HashMap<>();
        for (Ingredient i : getIngredients()) {
            pantryMap.put(normalize(i.name), i);
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (RecipeIngredient ri : r.ingredients) {
                Ingredient pantryItem = findPantry(pantryMap, ri.name);
                if (pantryItem != null) {
                    double requiredInPantryUnit = UnitConverter.convert(ri.quantity, ri.unit, pantryItem.unit);
                    double remaining = pantryItem.quantity - requiredInPantryUnit;

                    if (remaining <= 0.0001) {
                        db.delete("pantry", "id=?", new String[]{String.valueOf(pantryItem.id)});
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put("quantity", remaining);
                        db.update("pantry", cv, "id=?", new String[]{String.valueOf(pantryItem.id)});
                    }
                }
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    // --- Statistics and Summary Queries ---

    public int getPantryCount() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM pantry", null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int getExpiringCount() {
        List<Ingredient> list = getIngredients();
        int count = 0;
        for (Ingredient i : list) {
            DateUtils.Status s = DateUtils.getExpiryStatus(i.expiry);
            if (s == DateUtils.Status.EXPIRED || s == DateUtils.Status.EXPIRING_SOON) {
                count++;
            }
        }
        return count;
    }

    public List<Ingredient> getExpiringIngredients() {
        List<Ingredient> list = getIngredients();
        List<Ingredient> expiring = new ArrayList<>();
        for (Ingredient i : list) {
            DateUtils.Status s = DateUtils.getExpiryStatus(i.expiry);
            if (s == DateUtils.Status.EXPIRED || s == DateUtils.Status.EXPIRING_SOON) {
                expiring.add(i);
            }
        }
        return expiring;
    }

    public int getCookableRecipeCount() {
        return getStrictMatches().size();
    }

    private Ingredient findPantry(Map<String, Ingredient> map, String n) {
        Ingredient x = map.get(normalize(n));
        if (x != null) return x;
        String key = normalize(n);
        if (key.endsWith("s")) return map.get(key.substring(0, key.length() - 1));
        return map.get(key + "s");
    }

    private String normalize(String s) {
        return s == null ? "" : s.toLowerCase(Locale.US).trim().replaceAll("\\s+", " ");
    }
}
