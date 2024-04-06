package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public class Beetle implements Insect {
    private final Hive.Tile type;

    public Beetle() {
        this.type = Hive.Tile.BEETLE;
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
