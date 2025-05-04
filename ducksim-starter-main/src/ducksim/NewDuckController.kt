package ducksim

class NewDuckController(private var duckPond: DuckPond, private val view: DuckSimView) {
    fun createNewDuck() {
        if (duckPond.noSelectedDucks()) {
            val makeDuckDialog = MakeDuckDialog(duckPond, view)
            makeDuckDialog.setSize(300, 200)
            makeDuckDialog.isVisible = true
        } else {
            val ducks = duckPond.selectedDucks.filterNot { it is Flock }
            if (ducks.size <= 1) return
            val flock = Flock(ducks)
            duckPond.addNewDuck(flock)
            view.repaint()
        }
    }
}
