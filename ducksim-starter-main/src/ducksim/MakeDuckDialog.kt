package ducksim

import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.*

class MakeDuckDialog(
    private val model: DuckPond,
    private val view: DuckSimView
    ) : JDialog() {

    enum class Decoration {
        STAR,
        CROSS,
        MOON
    }

    private val blingCount = mutableMapOf(
        Decoration.STAR to 0,
        Decoration.CROSS to 0,
        Decoration.MOON to 0)

    private val blingCountLabel = mapOf(
        Decoration.STAR to JLabel("0"),
        Decoration.CROSS to JLabel("0"),
        Decoration.MOON to JLabel("0"),
    )

    // Duck panel
    private val duckPanel = JPanel()
    private val duckLabel = JLabel("Duck")
    private val duckStrings = listOf("Mallard", "Redhead", "Rubber", "Decoy", "Goose")
    private val duckOptions: JComboBox<*> = JComboBox<Any?>(duckStrings.toTypedArray())

    // Stored Data
    var duckType = "Mallard"

    // Bling panel
    private val blingPanel = JPanel(GridLayout(3, 4, 5, 5))

    // Button panel
    private val buttonPanel = JPanel()
    private val okayButton = JButton("Okay")
    private val cancelButton = JButton("Cancel")

    // Public Methods

    // Constructor
    init {
        this.contentPane.layout = BoxLayout(this.contentPane, BoxLayout.Y_AXIS)

        // add duck panel
        duckPanel.add(duckLabel)
        duckOptions.addActionListener { e: ActionEvent ->
            val cb = e.source as JComboBox<*>
            duckType = cb.selectedItem as String
        }
        duckPanel.add(duckOptions)
        this.add(duckPanel)

        // add Bling Panel
        // add star row
        addBlingRow(Decoration.STAR, blingPanel)
        addBlingRow(Decoration.MOON, blingPanel)
        addBlingRow(Decoration.CROSS, blingPanel)

        blingPanel.border = BorderFactory.createEmptyBorder(0, 10, 0, 0)
        this.add(blingPanel);

        // add button panel
        cancelButton.addActionListener { dispose() }
        buttonPanel.add(cancelButton)
        okayButton.addActionListener {
            // makeDuckDialog
            val duck: Duck? = when (duckType) {
                "Mallard" -> MallardDuck()
                "Redhead" -> RedheadDuck()
                "Rubber" -> RubberDuck()
                "Decoy" -> DecoyDuck()
                "Goose" -> GooseDuck(Goose())
                else -> null
            }
            if (duck != null) {
                val star = blingCount[Decoration.STAR] ?: 0
                val moon = blingCount[Decoration.MOON] ?: 0
                val cross = blingCount[Decoration.CROSS] ?: 0
                val duck = DuckFactory.createDuck(duck, star, moon, cross)
                model.addNewDuck(duck)
            }
            view.repaint()
            dispose()
        }
        buttonPanel.add(okayButton)
        this.add(buttonPanel)
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }

    private fun addBlingRow(decoration: Decoration, blingPanel: JPanel) {

        blingPanel.add(JLabel(decoration.name.toLowerCase().capitalize()));
        blingPanel.add(blingCountLabel[decoration])

        val incrementButton = JButton("+")

        incrementButton.addActionListener {
            val current = blingCount[decoration] ?: 0
            val total = blingCount.values.sum()
            if (current < 3 && total < 3) {
                blingCount[decoration] = current + 1
                blingCountLabel[decoration]!!.text = blingCount[decoration].toString()
                blingCountLabel[decoration]!!.repaint()
            }
        }

        blingPanel.add(incrementButton)

        val decrementButton = JButton("-")

        decrementButton.addActionListener {
            val current = blingCount[decoration] ?: 0
            if (current > 0) {
                blingCount[decoration] = current - 1
                blingCountLabel[decoration]!!.text = blingCount[decoration].toString()
                blingCountLabel[decoration]!!.repaint()
            }
        }
        blingPanel.add(decrementButton)
    }
}
