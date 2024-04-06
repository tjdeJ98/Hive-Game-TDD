package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public class QueenBee implements Insect{
    private final Hive.Tile type;

    public QueenBee() {
        this.type = Hive.Tile.QUEEN_BEE;
    }

    @Override
    public Hive.Tile getType() {
        return type;
    }

    @Override
    public int getAmountOfMoves() {
        return 1;
    }
}

