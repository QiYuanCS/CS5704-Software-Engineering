package river;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class GameEngineTest {

    private GameEngine engine;
    private GameEngine AGEngine;
    private GameEngine MGEngine;

    public static final Item BEANS = Item.ITEM_0;
    public static final Item GOOSE = Item.ITEM_1;
    public static final Item WOLF = Item.ITEM_2;
    public static final Item FARMER = Item.ITEM_3;

    @Before
    public void setUp() throws Exception {
        engine = new FarmerGameEngine();
        AGEngine = new AbstractGameEngine() {
            @Override
            public int numberOfItems() {
                return itemMap.size();
            }
        };
        MGEngine = new MonsterGameEngine();
    }


    private void transport(Item id, GameEngine engine) {
        engine.loadBoat(id);
        engine.rowBoat();
        engine.unloadBoat(id);
    }

    private void gameIsNotWonOrLost() {
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());
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

    // Tests for the FarmerGameEngine
    @Test
    public void testObjectCallThroughs() {
        class ItemTest {
            Item item;
            String label;
            Location location;
            Color color;
            boolean isDriver;

            ItemTest(Item item, String label, Location location, Color color, boolean isDriver) {
                this.item = item;
                this.label = label;
                this.location = location;
                this.color = color;
                this.isDriver = isDriver;
            }
        }

        ItemTest[] tests = {
                new ItemTest(FARMER, "F", Location.START, Color.BLUE, true),
                new ItemTest(WOLF, "W", Location.START, Color.RED, false),
                new ItemTest(GOOSE, "G", Location.START, Color.YELLOW, false),
                new ItemTest(BEANS, "B", Location.START, Color.GREEN,false)
        };

        for (ItemTest test : tests) {
            Assert.assertEquals(test.label, engine.getItemLabel(test.item));
            Assert.assertEquals(test.location, engine.getItemLocation(test.item));
            Assert.assertEquals(test.color, engine.getItemColor(test.item));
        }
    }

    @Test
    public void testGooseTransport() {
        engine.loadBoat(FARMER);
        Assert.assertEquals(Location.START, engine.getItemLocation(GOOSE));
        transport(GOOSE, engine);
        Assert.assertEquals(Location.FINISH, engine.getItemLocation(GOOSE));
    }

    @Test
    public void testWinningGame() {
        // Transport the goose
        engine.loadBoat(FARMER);
        transport(GOOSE, engine);
        gameIsNotWonOrLost();
        // Go back alone
        engine.rowBoat();
        gameIsNotWonOrLost();
        // Transport the beans
        transport(BEANS, engine);
        gameIsNotWonOrLost();
        // Transport the goose back
        transport(GOOSE, engine);
        gameIsNotWonOrLost();
        // Transport the wolf
        transport(WOLF, engine);
        gameIsNotWonOrLost();
        // Go back alone
        engine.rowBoat();
        gameIsNotWonOrLost();
        // Transport the goose
        transport(GOOSE, engine);
        gameIsNotWonOrLost();
        // Unload farmer and win
        engine.unloadBoat(FARMER);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertTrue(engine.gameIsWon());
    }

    @Test
    public void testGameIsNotWon() {
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, 4);

        for (int i = 0; i < totalItems; i++) {
            if (engine.getItemLocation(items[i]) != Location.FINISH) {
                Assert.assertFalse(engine.gameIsWon());
            }
        }
    }

    @Test
    public void testLosingGame() {
        engine.loadBoat(FARMER);
        transport(GOOSE, engine);
        gameIsNotWonOrLost();
        engine.rowBoat();
        transport(WOLF, engine);
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsWon());
        Assert.assertTrue(engine.gameIsLost());
    }

    @Test
    public void testError() {
        // Transport the goose
        engine.loadBoat(FARMER);
        transport(GOOSE, engine);
        gameIsNotWonOrLost();
        // Save the state
        Location beansLocation = engine.getItemLocation(BEANS);
        Location gooseLocation = engine.getItemLocation(GOOSE);
        Location wolfLocation = engine.getItemLocation(WOLF);
        Location farmerLocation = engine.getItemLocation(FARMER);
        /* This action should do nothing since the wolf
         * is not on the same shore as the boat
         * */
        engine.loadBoat(WOLF);
        // Check that the state has not changed
        Assert.assertEquals(beansLocation, engine.getItemLocation(BEANS));
        Assert.assertEquals(gooseLocation, engine.getItemLocation(GOOSE));
        Assert.assertEquals(wolfLocation, engine.getItemLocation(WOLF));
        Assert.assertEquals(farmerLocation, engine.getItemLocation(FARMER));
    }

    @Test
    public void testBoatLocation() {
        Assert.assertEquals(Location.START, engine.getBoatLocation());
        transport(FARMER, engine);
        Assert.assertEquals(Location.FINISH, engine.getBoatLocation());
    }

    @Test
    public void testIfGooseOnBoatGameIsNotLost() {
        engine.loadBoat(GOOSE);
        Assert.assertFalse(engine.gameIsLost());
    }

    @Test
    public void testIfGooseAndBeansTogetherGameIsLost() {
        engine.loadBoat(FARMER);
        transport(WOLF, engine);
        Assert.assertTrue(engine.gameIsLost());
    }

    @Test
    public void testResetGame() {
        engine.loadBoat(FARMER);
        transport(WOLF, engine);
        Assert.assertTrue(engine.gameIsLost());

        engine.resetGame();
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, engine.numberOfItems());

        for (int i = 0; i < totalItems; i++) {
            Assert.assertEquals(Location.START, engine.getItemLocation(items[i]));
        }
        Assert.assertEquals(Location.START, engine.getBoatLocation());
        Assert.assertEquals(2, getBoatCapacity());
    }

    @Test
    public void testGetItemLabel() {
        class LabelTest {
            Item item;
            String expectedLabel;

            LabelTest(Item item, String expectedLabel) {
                this.item = item;
                this.expectedLabel = expectedLabel;
            }
        }

        LabelTest[] tests = {
                new LabelTest(BEANS, "B"),
                new LabelTest(GOOSE, "G"),
                new LabelTest(WOLF, "W"),
                new LabelTest(FARMER, "F")
        };

        for (LabelTest test : tests) {
            Assert.assertEquals(test.expectedLabel, engine.getItemLabel(test.item));
        }
    }

    @Test
    public void testSetItemLocation() {
        Assert.assertEquals(Location.START, engine.getItemLocation(BEANS));

        engine.setItemLocation(BEANS, Location.FINISH);
        Assert.assertEquals(Location.FINISH, engine.getItemLocation(BEANS));

        engine.setItemLocation(BEANS, Location.BOAT);
        Assert.assertEquals(Location.BOAT, engine.getItemLocation(BEANS));
    }

    @Test
    public void testGameIsNotWonForEachCheck() {
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, engine.numberOfItems());

        for (int i = 0; i < totalItems; i++) {
            Item item = items[i];
            engine.setItemLocation(item, Location.START);
            if (engine.getItemLocation(item) != Location.FINISH) {
                Assert.assertFalse(engine.gameIsWon());
            }
        }
    }

    @Test
    public void testGetBoatCapacity() {
        Assert.assertEquals(2, getBoatCapacity());
        engine.loadBoat(GOOSE);
        Assert.assertEquals(1, getBoatCapacity());
    }

    // Tests for the AbstractGameEngine
    @Test
    public void testAGEngineGameIsLostReturnsFalse() {
        Assert.assertFalse(AGEngine.gameIsLost());
    }

    @Test
    public void testAGEngineRowBoat() {
        Assert.assertEquals(Location.START, AGEngine.getBoatLocation());
        AGEngine.rowBoat();
        Assert.assertEquals(Location.FINISH, AGEngine.getBoatLocation());
        AGEngine.rowBoat();
        Assert.assertEquals(Location.START, AGEngine.getBoatLocation());
    }

    @Test
    public void testAGEngineNumberOfItems() {
        Assert.assertEquals(0, AGEngine.numberOfItems());
    }

    // Tests for the MonsterGameEngine
    public static final Item MONSTER1 = Item.ITEM_0;
    public static final Item MUNCHKIN1 = Item.ITEM_1;
    public static final Item MONSTER2 = Item.ITEM_2;
    public static final Item MUNCHKIN2 = Item.ITEM_3;
    public static final Item MONSTER3 = Item.ITEM_4;
    public static final Item MUNCHKIN3 = Item.ITEM_5;

    @Test
    public void testIfMonsterOnBoatGameIsNotLost() {
        MGEngine.loadBoat(MONSTER1);
        Assert.assertFalse(MGEngine.gameIsLost());
    }

    @Test
    public void testIfMonsterCountsAsOnShoreGameIsLost() {
        MGEngine.loadBoat(MONSTER1);
        transport(MUNCHKIN1, MGEngine);
        MGEngine.rowBoat();
        Assert.assertTrue(MGEngine.gameIsLost());
    }

    @Test
    public void testWinningGameMonster() {

        MGEngine.loadBoat(MUNCHKIN1);
        transport(MONSTER1, MGEngine);
        transport(MUNCHKIN1, MGEngine);
        gameIsNotWonOrLost();

        MGEngine.loadBoat(MONSTER3);
        transport(MONSTER2, MGEngine);
        transport(MONSTER3, MGEngine);
        gameIsNotWonOrLost();

        MGEngine.loadBoat(MUNCHKIN2);
        transport(MUNCHKIN1, MGEngine);
        transport(MONSTER2, MGEngine);
        gameIsNotWonOrLost();

        MGEngine.loadBoat(MUNCHKIN3);
        transport(MUNCHKIN3, MGEngine);
        MGEngine.unloadBoat(MUNCHKIN2);
        MGEngine.loadBoat(MONSTER1);
        MGEngine.rowBoat();
        gameIsNotWonOrLost();

        transport(MONSTER2, MGEngine);
        MGEngine.rowBoat();
        gameIsNotWonOrLost();

        transport(MONSTER3, MGEngine);
        MGEngine.unloadBoat(MONSTER1);

        Assert.assertFalse(MGEngine.gameIsLost());
        Assert.assertTrue(MGEngine.gameIsWon());
    }

    @Test
    public void testMonsterGameIsNotWonForEachCheck() {
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, 4);

        for (int i = 0; i < totalItems; i++) {
            if (MGEngine.getItemLocation(items[i]) != Location.FINISH) {
                Assert.assertFalse(MGEngine.gameIsWon());
            }
        }
    }

    @Test
    public void testLosingGameMonster() {
        transport(MUNCHKIN1, MGEngine);
        Assert.assertFalse(MGEngine.gameIsWon());
        Assert.assertTrue(MGEngine.gameIsLost());
    }

    @Test
    public void testMGEngineGetBoatLocation() {
        MGEngine.loadBoat(MONSTER1);
        MGEngine.rowBoat();
        Assert.assertEquals(Location.FINISH, MGEngine.getBoatLocation());
        gameIsNotWonOrLost();
        MGEngine.rowBoat();
        MGEngine.loadBoat(MUNCHKIN1);
        Assert.assertEquals(Location.START, MGEngine.getBoatLocation());
        gameIsNotWonOrLost();
        MGEngine.rowBoat();
        Assert.assertEquals(Location.FINISH, MGEngine.getBoatLocation());
        gameIsNotWonOrLost();
    }

    @Test
    public void testItemOnBoatAtTheFinish() {
        gameIsNotWonOrLost();
        MGEngine.loadBoat(MONSTER3);
        gameIsNotWonOrLost();
        MGEngine.loadBoat(MUNCHKIN2);
        gameIsNotWonOrLost();
        MGEngine.rowBoat();
        gameIsNotWonOrLost();
        Assert.assertEquals(Location.FINISH, MGEngine.getBoatLocation());
        Assert.assertNotEquals(Location.START, MGEngine.getBoatLocation());
        gameIsNotWonOrLost();
        Assert.assertEquals(Location.BOAT, MGEngine.getItemLocation(MONSTER3));
        Assert.assertNotEquals(Location.START, MGEngine.getItemLocation(MONSTER3));
        Assert.assertNotEquals(Location.FINISH, MGEngine.getItemLocation(MONSTER3));
        gameIsNotWonOrLost();
        Assert.assertEquals(Location.BOAT, MGEngine.getItemLocation(MUNCHKIN2));
        Assert.assertNotEquals(Location.START, MGEngine.getItemLocation(MUNCHKIN2));
        Assert.assertNotEquals(Location.FINISH, MGEngine.getItemLocation(MUNCHKIN2));
        gameIsNotWonOrLost();
        MGEngine.unloadBoat(MONSTER3);
        gameIsNotWonOrLost();
        MGEngine.rowBoat();
        gameIsNotWonOrLost();
        MGEngine.loadBoat(MONSTER2);
        gameIsNotWonOrLost();
        MGEngine.rowBoat();
        Assert.assertTrue(MGEngine.gameIsLost());
    }
}
