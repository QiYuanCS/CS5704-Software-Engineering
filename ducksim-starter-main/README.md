
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
