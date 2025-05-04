package river;
import java.util.HashMap;
import java.awt.*;
public class FarmerGameEngine extends AbstractGameEngine{
    public static final Item BEANS = Item.ITEM_0;
    public static final Item GOOSE = Item.ITEM_1;
    public static final Item WOLF = Item.ITEM_2;
    public static final Item FARMER = Item.ITEM_3;

    public FarmerGameEngine() {
        itemMap = new HashMap<Item, GameObject>() {{
            put(BEANS, new GameObject("B", Location.START, Color.GREEN,false));
            put(GOOSE, new GameObject("G", Location.START, Color.YELLOW,false));
            put(WOLF, new GameObject("W", Location.START, Color.RED,false));
            put(FARMER, new GameObject("F", Location.START, Color.BLUE,true));
        }};
    }

    @Override
    public void rowBoat(){
        assert (boatLocation != Location.BOAT);
        if (getItemLocation(FARMER) == Location.BOAT) {
            boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
        }
    }

    @Override
    public boolean gameIsLost(){
        Location gooseLoc = getItemLocation(GOOSE);
        if (gooseLoc == getItemLocation(FARMER) || gooseLoc == Location.BOAT || gooseLoc == boatLocation) {
            return false;
        }
        return gooseLoc == getItemLocation(BEANS) || gooseLoc == getItemLocation(WOLF);
    }

    @Override
    public int numberOfItems(){
        return itemMap.size();
    }

}
