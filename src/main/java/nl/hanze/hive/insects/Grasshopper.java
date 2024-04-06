package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public class Grasshopper implements Insect {
    private final Hive.Tile type;

    public Grasshopper() {
        this.type = Hive.Tile.GRASSHOPPER;
    }

    @Override
    public Hive.Tile getType() {
        return type;
    }

    @Override
    public int getAmountOfMoves() {
        return 0;
    }
}
