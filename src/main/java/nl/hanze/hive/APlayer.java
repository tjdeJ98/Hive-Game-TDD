package nl.hanze.hive;

import nl.hanze.hive.insects.*;

import java.util.*;

public class APlayer {
    private final Hive.Player colour;
    protected HashMap<Integer, Insect> tilesInHand;
    private int turn = 1;
    public APlayer(Hive.Player colour) {
        this.colour = colour;
        tilesInHand = new HashMap<>();
    }

    public Hive.Player getColour() {
        return colour;
    }

    public HashMap<Integer, Insect> getTilesInHand() {
        return tilesInHand;
    }

    public boolean maxAmountTileReached(Insect tile) {
        var i = 0;
        for (Insect existingTile : tilesInHand.values()) {
            if (existingTile.getType().equals(tile.getType())) {
                i++;
            }
        }

        switch (tile.getType()) {
            case QUEEN_BEE:
                return i == 1;
            case BEETLE:
            case SPIDER:
                return i == 2;
            case GRASSHOPPER:
            case SOLDIER_ANT:
                return i == 3;
        }
        return false;
    }

    public Integer createTileToPlay(Insect tile) throws Hive.IllegalMove {
        if (maxAmountTileReached(tile)) {
            throw new Hive.IllegalMove("Max amount of " + tile.getClass() + " reached");
        } else {
            return addTile(tile);
        }
    }

    public Integer addTile(Insect tile) {
        var start = (colour == Hive.Player.WHITE) ? 0 : 20;
        var i = 0;

        for (Integer key : tilesInHand.keySet()) {
            i = (key >= i) ? i + 1 : i;
        }

        tilesInHand.put(start + i, tile);
        return start + i;
    }

    public boolean hasQueenBeeInPlay() {
        for (Insect insect : tilesInHand.values())
            if (insect.getType() == Hive.Tile.QUEEN_BEE)
                return true;
        return false;
    }

    public Insect grabTileFromInt(int id) {
        return tilesInHand.get(id);
    }

    public Integer getQueenBeeId() {
        if (hasQueenBeeInPlay()) {
            for (Integer key : getTilesInHand().keySet()) {
                if (tilesInHand.get(key).getType() == Hive.Tile.QUEEN_BEE) {
                    return key;
                }
            }
        }
        return -1;
    }

    public void turnPlusOne() {
        turn += 1;
    }

    public int getTurn() {
        return turn;
    }
}
