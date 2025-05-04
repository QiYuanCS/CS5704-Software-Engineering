# RiverCrossing-GWK

# 🌊 RiverCrossing Project Instructions

## ✅ Setup

1. **GitHub Repo Setup**
   - Paste the URL of your GitHub repo (e.g., https://github.com/yourusername/RiverCrossing) into the "Project URL" field.
   - Name your project `RiverCrossing`.
   - Make sure the repo is **private**.
   - Import the project into **IntelliJ** as you did in Mini-Project 1.

## 🧭 Part 1 – Explore the Project

- Generate a **class diagram** of the `river` package (IntelliJ IDEA Ultimate can generate this).
- Run the GUI to:
  - Reach a **winning** state.
  - Reach a **losing** state.
- Visit Wikipedia’s page on **Model-View-Controller**.
  - Identify the **Model**, **View**, and **Controller** in this project.

---

## 🔧 Part 2 – Refactor the Code

### ✅ GameEngineTest
- Complete all `TODO` tests and ensure all pass.
- Play the game using `RiverGUI`.

### ✅ GameObject Refactor
- Eliminate subclasses of `GameObject`.
- Replace `getSound()` logic with a **field** (like `getName()`).
- Remove `setName()` and unused constructors.
- Add `isDriver` field: `true` for farmer, `false` for others.
- Make fields **private**, constants **final**.

### ✅ GameEngine Refactor
- Move `Location` class to its own file.
- Rename variables:
  - `top → wolf`, `mid → goose`, `bottom → beans`, `player → farmer`
- Add `Map<Item, GameObject>` to map game objects.
- Remove now-redundant fields.
- Rename methods:
  - `getName` → `getItemName`
  - `getLocation` → `getItemLocation`
  - `getSound` → `getItemSound`
- Rename "currentLocation" to "boatLocation".
- Use `private` access for all internal members.

### ✅ GameEngineTest Refactor
- Create private field `engine` in the test class.
- Refactor `testObject` → `testObjectCallThroughs`.
- Create a `transport(item)` helper.

### ✅ Location Enum Enhancements
Add:
```java
public boolean isOnBoat() { return this == BOAT; }
public boolean isAtFinish() { return this == FINISH; }
public boolean isAtStart() { return this == START; }
```

---

## 🧩 Part 3 – Preparing for the GameEngine Interface

- Eliminate all references to specific objects like `wolf`, `goose`, `beans`, and `farmer`.
- Refactor `Item`:
  - Move to its own file.
  - Rename:
    - `BEANS` → `ITEM_0`, `GOOSE` → `ITEM_1`, `WOLF` → `ITEM_2`, `FARMER` → `ITEM_3`
- In `GameObject`, replace `name`/`sound` with `label`/`color`.

---

## 🎨 Part 4 – Modify RiverGUI

### General Improvements
- Use `location.isOnBoat()` over `location == Location.BOAT`.
- Refactor GUI to only draw **5 rectangles**:
  - 4 items
  - 1 boat

### Code Suggestions:
```java
paintItem(g, Item.ITEM_0);
paintItem(g, Item.ITEM_1);
...
```

Use `Rectangle(leftBaseX + dx[item.ordinal()], leftBaseY + dy[item.ordinal()], 50, 50)`.

---

## 🔌 Part 5 – GameEngine Interface

- Rename `getCurrentLocation()` → `getBoatLocation()`.
- Rename `GameEngine` → `FarmerGameEngine`.
- Use the `GameEngine` interface exactly as specified.
- Refactor tests and GUI to **use the interface**, not the implementation.

---

## 🧱 Part 6 – AbstractGameEngine

- Create `AbstractGameEngine` with default method implementations.
- Leave `gameIsLost()` as **abstract**.

---

## 👹 Part 7 – MonsterGameEngine

- Implement `MonsterGameEngine`:
  - Add `ITEM_4`, `ITEM_5`.
  - Monster items: `ITEM_0`, `ITEM_2`, `ITEM_4`
  - Munchkin items: `ITEM_1`, `ITEM_3`, `ITEM_5`
- Update GUI to handle 5 or 6 items using `engine.getItems()` instead of `Item.values()`.

---

## 📦 Part 8 – Deliverables

- Add tests to **fully cover** `FarmerGameEngine`.
- Submit all `*.java` files including test and GUI classes to **Web-CAT**.
