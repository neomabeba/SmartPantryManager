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

## 📝 10-Commit Sequence History

| # | Commit Message |
| :---: | :--- |
| **1** | `Initial Smart Pantry Manager Android project` |
| **2** | `Added SQLite database and recipe seed data` |
| **3** | `Implemented pantry ingredient CRUD` |
| **4** | `Added strict recipe matching` |
| **5** | `Added recipe detail screen` |
| **6** | `Added settings and preferences` |
| **7** | `Redesigned Home dashboard` |
| **8** | `Redesigned Pantry management` |
| **9** | `Improved validation and empty states` |
| **10** | `Final UI, navigation and application polish` |
