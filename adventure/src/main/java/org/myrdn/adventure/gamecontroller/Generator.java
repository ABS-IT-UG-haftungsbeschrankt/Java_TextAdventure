package org.myrdn.adventure.gamecontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Generator {

    private static final int MAX_ATTEMPTS = 2000000;
    private static final Random RANDOM    = new Random();

    private final Layout layoutObject;
    private final int xSize;
    private final int ySize;
    private final int[][] layout;
    private final Room[][] rooms;
    
    private int[] startPosition;
    
    public Generator(Layout layoutObject, ArrayList<GameObject> availableObjects, ArrayList<ItemObject> availableItems) {

        this.layoutObject = layoutObject;
        this.xSize  = layoutObject.getXSize();
        this.ySize  = layoutObject.getYSize();
        this.layout = layoutObject.getLayout();
        this.rooms  = new Room[ySize][xSize];
    
        buildMap(availableItems, availableObjects);
    
    }

    public int getXSize() {

        return this.xSize;

    }

    public int getYSize() {

        return this.ySize;

    }

    public Room[][] getRooms() {
    
        return this.rooms;
    
    }

    public int[] getStartPosition() {
    
        return this.startPosition;
    
    }

    private void buildMap(ArrayList<ItemObject> availableItems, ArrayList<GameObject> availableObjects) {
    
        int count = 0;
    
        do {

            count++;
            this.layoutObject.buildLayout();
    
            if(count > MAX_ATTEMPTS) {
    
                throw new RuntimeException("Es konnte keine vollverbundene Karte nach " + MAX_ATTEMPTS + " Versuchen erstellt werden.");
    
            }
    
        } while(!this.layoutObject.isFullyConnected());
    
        placeRooms(availableObjects, availableItems);
    
    }

    public void placeRooms(ArrayList<GameObject> availableObjects, ArrayList<ItemObject> availableItems) {
    
        ArrayList<GameObject> newObjects = fillObjects(availableObjects, availableItems);

        for(int y = this.layout.length - 1; y >= 0; y--) {
    
            for(int x = 0; x < this.layout[y].length; x++) {
    
                Collections.shuffle(newObjects);
                this.rooms[y][x] = new Room(this.layout[y][x], newObjects);
    
            }
    
        }

        while(!newObjects.isEmpty()) {
    
            for (int y = 0; y < this.layout.length; y++) {
    
                for (int x = 0; x < layout[y].length; x++) {
    
                    Collections.shuffle(newObjects);
    
                    if(this.rooms[y][x].getObjects().size() < 2) {
    
                        int chance = RANDOM.nextInt(10);
    
                        if(chance >= 4 && !newObjects.isEmpty()) {
    
                            this.rooms[y][x].getObjects().add(newObjects.get(0));
                            newObjects.remove(0);
    
                        }
    
                    }
    
                }
    
            }
    
        }
    
    }

    private ArrayList<GameObject> fillObjects(ArrayList<GameObject> availableObjects, ArrayList<ItemObject> availableItems) {
    
        Collections.shuffle(availableItems);
        ArrayList<GameObject> newObjects = new ArrayList<>();

        for(GameObject emptyObject : availableObjects) {
    
            int maxStashes = emptyObject.getHiddenStashes();
            int stashesUsed = 0;
            int chance = RANDOM.nextInt(10);
    
            for(int i = 0; i < maxStashes; i++) {
    
                if(chance >= 6) {
    
                    if(availableItems.isEmpty()) {
    
                        break;
    
                    }
    
                    emptyObject.addtHiddenItem(availableItems.getFirst());
                    availableItems.removeFirst();
                    stashesUsed++;
    
                }
    
            }
    
            emptyObject.setHiddenStashes(maxStashes - stashesUsed);
            newObjects.add(emptyObject);
    
        }

        while(!availableItems.isEmpty()) {
    
            for(GameObject object : newObjects) {
    
                if(availableItems.isEmpty()) {
    
                    break;
    
                }
    
                int chance = RANDOM.nextInt(10);
                Collections.shuffle(availableItems);
    
                if(chance >= 6) {
    
                    if(object.getHiddenStashes() > 0) {
    
                        object.addtHiddenItem(availableItems.getFirst());
                        object.setHiddenStashes(object.getHiddenStashes()-1);
                        availableItems.removeFirst();
    
                    }
    
                }
    
            }
    
        }
    
        return newObjects;
    
    }
    


}