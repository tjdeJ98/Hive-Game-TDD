package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public class SoldierAnt implements Insect{
    private final Hive.Tile type;

    public SoldierAnt() {
        this.type = Hive.Tile.SOLDIER_ANT;
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
