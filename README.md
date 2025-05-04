# 🧩 Project Requirements Summary
This document includes full requirement specifications for two software design projects:
1. Duck Simulator
2. River Crossing Game

---

## 1. Duck Simulator


# 🦆 Duck Simulator Requirements

## 🖱️ UI Enhancements

- **Duck Selection Color**:  
  When a duck is selected (left click), the square's background should change to the **duck's color**, instead of black.

- **Welcoming Committee Indicator**:  
  When a duck joins the **DuckSim Welcoming Committee (DSWC)**, a little **"w"** should appear in the bottom-left corner of its square.

## 🧠 Strategy Pattern

Encapsulate flying and quacking behaviors:

- `FlyBehavior` interface:
  - `FlyNoWay`
  - `FlyWithWings`

- `QuackBehavior` interface:
  - `QuackNoWay`
  - `QuackNormal`
  - `QuackSqueak`

### ➕ Behavior Rules

- When a duck is **captured**, it **cannot fly or quack**.
- When a duck is **released**, it regains its **original fly and quack behaviors**.

## 🦆 Decoy Duck

- A `DecoyDuck`:
  - Cannot fly or quack.
  - Is displayed in **orange** color.

## 💍 Decorator Pattern – Bling

- Add an abstract `Bling` class to decorate ducks.
- Types of `Bling`:
  - `StarBling` → `:*`
  - `MoonBling` → `:)`
  - `CrossBling` → `:+`

### ➕ Bling Display Example
`Mallard:*:*:)`

### 🎛️ Bling UI Validation
Update `MakeDuckDialog`:
- Bling counts **cannot be negative**.
- Total number of bling items **must not exceed 3**.
- Ensure bling is applied via the **DuckFactory** (see below).

## 🏭 Factory Pattern

- Implement a `DuckFactory` singleton.
- Method signature:

```kotlin
createDuck(baseDuck: Duck, starCount: Int, moonCount: Int, crossCount: Int): Duck
```

- The `baseDuck` must **not be a `Bling`**.

## 🔌 Adapter Pattern

Adapt the `Goose` class to be used as a duck:

```kotlin
class Goose {
    val honkText = "Honk"
    val name = "Goose"
}
```

- Create `GooseDuck` adapter.
- Use **normal fly and quack strategies**.
- The goose’s `quack()` should output `getHonk()`.
- The goose's button should show `getName()`.

## 👁️ Observer Pattern

- Implement your own `Subject` (can be abstract class) and `Observer` (must be an interface with `update()` method).
- `DuckFactory` acts as the **Subject**.
- Ducks act as **Observers**.

### Behavior:
- When a duck joins the DSWC, it registers as an observer.
- When a new duck is created:
  - Display `Welcome` over DSWC members' names.
- If a duck is captured:
  - Display `Beware!` instead of `Welcome`.

> These messages should **persist until the screen is repainted**.

## 🧩 Composite Pattern – Flock

- Create a `Flock` class:
  - Must extend `Duck`.
  - Contains a **collection of Ducks**.
  - Cannot have `Bling`.

### Flock Behavior:
- `display()` returns:  
  `"Flock"` followed by the first letter of each duck it contains (replaces bling)
- `fly()` uses Duck's default
- `quack()` outputs `"Noise!"`
- `joinDSWC()` and `quitDSWC()` applies to all contained ducks.
- `capture()` and `release()` applies to all contained ducks.

## 🧠 NewDuckController Integration

In `DuckSimController`, replace `New-Duck` button behavior:

```kotlin
when {
    view.newDuckButtonRectangle.contains(e.x, e.y) -> {
        NewDuckController(duckPond, view).createNewDuck()
    }
}
```

### New Class: `NewDuckController`

```kotlin
fun createNewDuck() {
    if (duckPond.noSelectedDucks()) {
        val makeDuckDialog = MakeDuckDialog(duckPond, view)
        makeDuckDialog.setSize(300, 200)
        makeDuckDialog.isVisible = true
    } else {
        // filter out flocks
        val flock = Flock(selectedDucks)
        duckPond.addNewDuck(flock)
        view.repaint()
    }
}
```

> The "New Duck" button is now used to create **Ducks** or **Flocks**, depending on whether any ducks are selected.


---

## 2. River Crossing Game


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
