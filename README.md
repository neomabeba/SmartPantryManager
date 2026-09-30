# Smart Pantry Manager 🥗📱

A modern Java Android application for tracking household ingredients, reducing food waste, and discovering cookable recipes based on available pantry items.

---

## 🌟 Features

### 📦 Pantry Management
- **Full CRUD Operations**: Add, view, edit, and delete ingredients with confirmation dialogs.
- **Expiry Warnings & Status Badges**: Automatic date calculations with color-coded status badges:
  - 🔴 **Expired** (Red)
  - 🟡 **Expiring Soon** (Amber, ≤ 3 days)
  - 🟢 **Fresh** (Emerald Green)
- **DatePicker Integration**: Built-in calendar picker (`DatePickerDialog`) for expiry dates.
- **Search & Filters**: Instant search by ingredient name with filter chips (`All`, `Expiring Soon`, `Expired`, `Fresh`).

### 🍳 Recipe Matching & Cooking
- **Strict & Partial Matching**:
  - **Ready to Cook (100%)**: Only suggests recipes where all ingredients are available in required quantities.
  - **Partial Matches**: Displays recipes missing 1–2 items with missing quantity callouts.
- **Unit Conversion Engine**: Converts units across mass (`g` ↔ `kg`), volume (`ml` ↔ `l`, `tbsp`, `tsp`, `cups`), and counts (`pieces`, `slices`, `cloves`).
- **Pluralization Handling**: Normalizes singular/plural names (e.g., `egg` ↔ `eggs`, `avocado` ↔ `avocados`).
- **Interactive Recipe Cooking**: Deducts required ingredient quantities directly from the pantry storage when cooking a recipe.

### 🎨 Modern Material UI & Navigation
- **Bottom Navigation**: 4-tab bottom navigation bar (`Home`, `Pantry`, `Recipes`, `Settings`).
- **Dashboard Overview**: Stat summary cards, quick action buttons, and expiring item alerts.
- **Material 3 Design**: Card layouts, custom vector icons, inline form validation (`TextInputLayout`), and friendly empty state screens.

---

## 🛠️ Technology Stack
- **Language**: Java
- **Target SDK**: Android 35 (Min SDK 23)
- **UI Framework**: Android Material Components & Material 3
- **Database**: SQLite via `SQLiteOpenHelper`
- **Architecture**: Model-View-Adapter with helper utilities (`UnitConverter`, `DateUtils`)
- **Testing**: JUnit 4

---

## 🚀 How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/smart-pantry-manager.git
   ```
2. Open the project in **Android Studio**.
3. Allow Gradle to sync dependencies.
4. Select an Emulator or connected Android device.
5. Click **Run `app`** (`Shift + F10`).

---

## 📝 12-Commit Sequence History

This project was built iteratively following an incremental git development progression:

| Commit # | Commit Message | Description |
| :--- | :--- | :--- |
| **01** | `initial: scaffold Android project structure and Gradle build configuration` | Initialized base Android application module, namespace `com.example.smartpantry`, and dependencies. |
| **02** | `feat(db): implement SQLiteDatabase helper and initial schema` | Created `DatabaseHelper.java` for `pantry`, `recipes`, and `recipe_ingredients` tables. |
| **03** | `feat(model): add Pantry Ingredient and Recipe data models` | Created `Ingredient.java`, `Recipe.java`, and `RecipeIngredient.java` data structures. |
| **04** | `feat(pantry): add pantry item list view, item cards, and adapter` | Created `item_pantry.xml`, `PantryAdapter.java`, and layout views. |
| **05** | `feat(pantry): implement full pantry CRUD operations and database integration` | Wired insert, update, query, and delete operations in `PantryActivity` and `AddEditIngredientActivity`. |
| **06** | `feat(recipe): seed initial recipe database and create recipe adapter` | Added 18 seed recipes with step-by-step cooking instructions and created `RecipeAdapter.java`. |
| **07** | `feat(matching): implement UnitConverter and strict/partial recipe matching engine` | Added unit conversion logic (`g`↔`kg`, `ml`↔`l`↔`cups`) and singular/plural name normalization. |
| **08** | `feat(recipe): implement RecipeDetailActivity and cook ingredient deduction` | Displayed required ingredients checklist and implemented pantry ingredient deduction on cook action. |
| **09** | `feat(ui): implement DateUtils, expiry badges, and DatePickerDialog` | Calculated days remaining and added color-coded expiry status badges (🔴 Expired, 🟡 Warning, 🟢 Fresh). |
| **10** | `feat(ui): add search input bar and status filter chips for pantry and recipes` | Added dynamic text filtering and category filter chips (`All`, `Expiring Soon`, `Expired`, `Fresh`). |
| **11** | `feat(navigation): introduce BottomNavigationView and Home Dashboard screen` | Added 4-tab bottom navigation (`Home`, `Pantry`, `Recipes`, `Settings`) and dashboard summary cards. |
| **12** | `test(unit): add JUnit test suite for UnitConverter, DateUtils, and data models` | Added 6 unit tests in `SmartPantryUnitTest.java` verifying logic, conversions, and models. |
