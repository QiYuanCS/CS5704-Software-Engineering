package river;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphical interface for the River application
 *
 * @author Gregory Kulczycki and Miles Spence
 */
public class RiverGUI extends JPanel implements MouseListener {

    // ==========================================================
    // Private Fields
    // ==========================================================

    private final Map<Item, Rectangle> itemRectangleMap;

    {
        itemRectangleMap = new HashMap<>();
        itemRectangleMap.put(Item.ITEM_0, new Rectangle(20, 215, 50, 50));
        itemRectangleMap.put(Item.ITEM_1, new Rectangle(80, 215, 50, 50));
        itemRectangleMap.put(Item.ITEM_2, new Rectangle(20, 155, 50, 50));
        itemRectangleMap.put(Item.ITEM_3, new Rectangle(80, 155, 50, 50));
        itemRectangleMap.put(Item.ITEM_4, new Rectangle(140, 155, 50, 50));
        itemRectangleMap.put(Item.ITEM_5, new Rectangle(140, 215, 50, 50));
    }
    private final int[] XOffset = {0, 60, 0, 60, 0, 60};
    private final int[] YOffset = {60, 60, 0, 0, 120, 120};
    private final int[] XOffsetOfboat = {0, 410};
    private int numItemsPaintedOnBoat = 0;
    private Rectangle boatRectangle = new Rectangle(140, 275, 110, 50);
    private final Rectangle restartFarmerGameRect = new Rectangle(235, 120, 100, 30);
    private final Rectangle restartMonsterGameRect = new Rectangle(345, 120, 100, 30);
    private final Rectangle restartScoutGameRect = new Rectangle(455, 120, 100, 30);
    private GameEngine engine;
    private boolean gameOver = false;

    // ==========================================================
    // Constructor
    // ==========================================================

    public RiverGUI() {
        engine = new FarmerGameEngine();
        addMouseListener(this);
    }

    // ==========================================================
    // Paint Methods (View)
    // ==========================================================

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked
     * from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window
        JFrame frame = new JFrame("RiverCrossing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create and set up the content pane
        RiverGUI newContentPane = new RiverGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        // Display the window
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    public static void main(String[] args){

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(RiverGUI::createAndShowGUI);
    }


    @Override
    public void paintComponent(Graphics g) {
        updateLocationsOfItemRectangles();
        updateLocationOfBoatRectangle();

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Item[] items = Item.values();
        int totalItems = Math.min(items.length, engine.numberOfItems());
        for (int i = 0; i < totalItems; i++) {
            Item item = items[i];
            paintRectangle(g, engine.getItemColor(item), engine.getItemLabel(item), itemRectangleMap.get(item));
        }

        paintRectangle(g, Color.ORANGE, "", boatRectangle);

        if (engine.gameIsLost() || engine.gameIsWon()) {
            paintMessage(engine.gameIsLost() ? "You Lost the Game!" : "You Won the Game!", g);
            gameOver = true;
        }

        if (gameOver) {
            drawRestartButton(g, restartFarmerGameRect, "Farmer");
            drawRestartButton(g, restartMonsterGameRect, "Monster");
            drawRestartButton(g, restartScoutGameRect, "Scout");
        }
    }
    private void drawRestartButton(Graphics g, Rectangle rect, String label) {
        g.setColor(Color.BLACK);
        g.fillRect(rect.x - 3, rect.y - 3, rect.width + 6, rect.height + 6);
        paintRectangle(g, Color.PINK, label, rect);
    }

    private void updateLocationsOfItemRectangles() {
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, engine.numberOfItems());
        numItemsPaintedOnBoat = 0;

        for (int i = 0; i < totalItems; i++) {
            Item item = items[i];
            Location location = engine.getItemLocation(item);
            Rectangle rect;

            if (location == Location.START) {
                rect = new Rectangle(20 + XOffset[i], 155 + YOffset[i], 50, 50);
            } else if (location == Location.FINISH) {
                rect = new Rectangle(670 + XOffset[i], 155 + YOffset[i], 50, 50);
            } else {
                rect = new Rectangle(140 + XOffsetOfboat[engine.getBoatLocation().ordinal()]
                        + XOffset[numItemsPaintedOnBoat], 215, 50, 50);
                numItemsPaintedOnBoat++;
            }
            itemRectangleMap.put(item, rect);
        }
    }

    private void updateLocationOfBoatRectangle() {
        boatRectangle = new Rectangle(140 + XOffsetOfboat[engine.getBoatLocation().ordinal()],
                275, 110, 50);
    }

    private void paintRectangle(Graphics g, Color itemColor, String itemLabel, Rectangle rectangle) {
        g.setColor(itemColor);
        g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        g.setColor(Color.BLACK);
        int fontSize = (rectangle.height >= 40) ? 36 : 18;
        if (itemLabel.equals("Mun") || itemLabel.equals("Mon")) {
            fontSize = 24;
        }
        g.setFont(new Font("Verdana", Font.BOLD, fontSize));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(itemLabel, rectangle.x + rectangle.width / 2 - fm.stringWidth(itemLabel) / 2,
                rectangle.y + rectangle.height / 2 + fontSize / 2 - 4);
    }

    public void paintMessage(String message, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(message, 400 - fm.stringWidth(message) / 2, 100);
    }

    // ==========================================================
    // MouseListener Methods (Controller)
    // ==========================================================


    private void clickItem(Item item, MouseEvent e) {
        if (!itemRectangleMap.get(item).contains(e.getPoint())) {
            return;
        }

        Location itemLocation = engine.getItemLocation(item);
        if (itemLocation == Location.START || itemLocation == Location.FINISH) {
            if (getBoatCapacity() > 0) {
                engine.loadBoat(item);
            }
        } else {
            engine.unloadBoat(item);
        }
    }

    private int getBoatCapacity() {
        int boatCapacity = 2;
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, engine.numberOfItems());

        for (int i = 0; i < totalItems; i++) {
            if (engine.getItemLocation(items[i]) == Location.BOAT) {
                boatCapacity--;
            }
        }
        return boatCapacity;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver) {
            if (this.restartFarmerGameRect.contains(e.getPoint())) {
                engine = new FarmerGameEngine();
            } else if (this.restartMonsterGameRect.contains(e.getPoint())) {
                engine = new MonsterGameEngine();
            } else if (this.restartScoutGameRect.contains(e.getPoint())){
                engine = new ScoutGameEngine();
            }

            engine.resetGame();
            gameOver = false;
            repaint();
            return;
        }

        for (Item item : Item.values()) {
            if (!(item.ordinal() < engine.numberOfItems())) break;
            clickItem(item, e);
        }
        if (boatRectangle.contains(e.getPoint())) {
            engine.rowBoat();
        }
        repaint();
    }


    // ----------------------------------------------------------
    // None of these methods will be used
    // ----------------------------------------------------------

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}
