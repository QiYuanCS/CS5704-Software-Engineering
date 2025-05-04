package river;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MonsterGameEngine extends AbstractGameEngine {
    public static final Item MONSTER1 = Item.ITEM_0;
    public static final Item MUNCHKIN1 = Item.ITEM_1;
    public static final Item MONSTER2 = Item.ITEM_2;
    public static final Item MUNCHKIN2 = Item.ITEM_3;
    public static final Item MONSTER3 = Item.ITEM_4;
    public static final Item MUNCHKIN3 = Item.ITEM_5;

    public MonsterGameEngine() {
        itemMap = new HashMap<Item, GameObject>() {{
            put(MONSTER1, new GameObject("Mon", Location.START, Color.red, true));
            put(MUNCHKIN1, new GameObject("Mun", Location.START, Color.green, true));
            put(MONSTER2, new GameObject("Mon", Location.START, Color.red, true));
            put(MUNCHKIN2, new GameObject("Mun", Location.START, Color.green, true));
            put(MONSTER3, new GameObject("Mon", Location.START, Color.red,true));
            put(MUNCHKIN3, new GameObject("Mun", Location.START, Color.green,true));
        }};
    }
    @Override
    public int numberOfItems() {
        return itemMap.size();
    }


    @Override
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, numberOfItems());
        for (int i = 0; i < totalItems; i++) {
            if (getItemLocation(items[i]) == Location.BOAT) {
                boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
                break;
            }
        }
    }



    @Override
    public boolean gameIsLost() {
        int[] numOfMonsters = {0, 0};
        int[] numOfMunchkins = {0, 0};

        Item[] items = Item.values();
        int totalItems = Math.min(items.length, numberOfItems());
        for (int i = 0; i < totalItems; i++) {
            checkItemLocation(items[i], (i % 2 == 0) ? numOfMonsters : numOfMunchkins);
        }

        return (numOfMunchkins[0] > 0 && numOfMonsters[0] > numOfMunchkins[0]) ||
                (numOfMunchkins[1] > 0 && numOfMonsters[1] > numOfMunchkins[1]);
    }






    private void checkItemLocation(Item item, int[] numOfItems) {
        Location itemLocation = getItemLocation(item);
        if (itemLocation == Location.START) {
            numOfItems[0]++;
        } else if (itemLocation == Location.FINISH) {
            numOfItems[1]++;
        } else if (itemLocation == Location.BOAT) {
            numOfItems[getBoatLocation() == Location.START ? 0 : 1]++;
        }
    }

}
