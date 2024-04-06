package nl.hanze.hive.insects;

import nl.hanze.hive.Hive;

public interface Insect {
    Hive.Tile getType();

    int getAmountOfMoves();
}
