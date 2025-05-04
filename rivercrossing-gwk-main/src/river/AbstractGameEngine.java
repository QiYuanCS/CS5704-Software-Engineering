package river;
import java.util.*;
import java.awt.*;
import java.util.List;

public abstract class AbstractGameEngine implements GameEngine{
    protected Location boatLocation = Location.START;
    protected int boatCapacity = 2;
    protected Map<Item, GameObject> itemMap = new HashMap<>();

    @Override
    public String getItemLabel(Item item){
        return itemMap.get(item).getLabel();
    }

    @Override
    public Color getItemColor(Item item){
        return itemMap.get(item).getColor();
    }

    @Override
    public void setItemLocation(Item item, Location location) {
        itemMap.get(item).setLocation(location);
    }

    @Override
    public Location getItemLocation(Item item) {
        return itemMap.get(item).getLocation();
    }

    @Override
    public Location getBoatLocation() {
        return boatLocation;
    }

    @Override
    public abstract int numberOfItems();


    @Override
    public void loadBoat(Item item ){
        if(boatCapacity > 0 && itemMap.get(item).getLocation() == boatLocation){
            itemMap.get(item).setLocation(Location.BOAT);
            boatCapacity--;
        }
    }

    @Override
    public void unloadBoat(Item item){
        if(itemMap.get(item).getLocation() == Location.BOAT){
            itemMap.get(item).setLocation(boatLocation);
            boatCapacity++;
        }
    }

    @Override
    public void rowBoat(){
        assert(boatLocation != Location.BOAT);
        boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
    }

    @Override
    public boolean gameIsWon(){
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, numberOfItems());
        for (int i = 0; i < totalItems; i++) {
            if (getItemLocation(items[i]) != Location.FINISH) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean gameIsLost(){
        return false;
    }

    @Override
    public void resetGame(){
        Item[] items = Item.values();
        int totalItems = Math.min(items.length, numberOfItems());
        for (int i = 0; i < totalItems; i++) {
            itemMap.get(items[i]).setLocation(Location.START);
        }
        boatCapacity = 2;
        boatLocation = Location.START;
    }

    @Override
    public List<Item> getItems() {
        // Return a sorted list of items based on their ordinal values
        List<Item> itemList = new ArrayList<>(itemMap.keySet());
        itemList.sort((a, b) -> a.ordinal() - b.ordinal());  // Ensure items are sorted by ordinal value
        return itemList;
    }
}
