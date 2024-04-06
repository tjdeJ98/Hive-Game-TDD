package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public class Spider implements Insect{
    private final Hive.Tile type;

    public Spider() {
        this.type = Hive.Tile.SPIDER;
    }

    @Override
    public Hive.Tile getType() {
        return type;
    }


    @Override
    public int getAmountOfMoves() {
        return 3;
    }
}

