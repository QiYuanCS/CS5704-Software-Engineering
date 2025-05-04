package river;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

public class ScoutGameEngine extends AbstractGameEngine {
    public static final Item BEAVER1 = Item.ITEM_0;
    public static final Item BEAVER2 = Item.ITEM_1;
    public static final Item EXPLORER1 = Item.ITEM_2;
    public static final Item EXPLORER2 = Item.ITEM_3;
    public static final Item BEAVER3 = Item.ITEM_4;

    public ScoutGameEngine() {
        itemMap = new HashMap<Item, GameObject>() {{
            put(EXPLORER1, new GameObject("E", Location.START, Color.red, true));
            put(EXPLORER2, new GameObject("E", Location.START, Color.red, true));
            put(BEAVER1, new GameObject("B", Location.START, Color.green, true));
            put(BEAVER2, new GameObject("B", Location.START, Color.green, true));
            put(BEAVER3, new GameObject("B", Location.START, Color.green,true));
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
        ArrayList<river.Item> boatItems = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            if (getItemLocation(items[i]) == Location.BOAT) {
                boatItems.add(items[i]);
//                boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
//                break;
            }
        }
        switch (boatItems.size())
        {
            case 0:
                return;
            case 1:
                if(boatItems.getFirst() == EXPLORER1 || boatItems.getFirst() == EXPLORER2){
                boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
                }
            case 2:
                boolean case1 = boatItems.stream().anyMatch(n -> (n==EXPLORER1|| n == EXPLORER2) );
                if(!case1)
                    boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;

        }
    }

}
