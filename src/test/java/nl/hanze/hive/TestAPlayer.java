package nl.hanze.hive;

import nl.hanze.hive.insects.*;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TestAPlayer {
    public TestAPlayer() {

    }

    /**
     * Requirement 1c
     */
    @Test
    public void testPlayerHasQueenBee() {
        APlayer mockPlayer = new APlayer(Hive.Player.WHITE);
        Insect mockQueenInsect = new QueenBee();

        assertFalse(mockPlayer.getTilesInHand().containsValue(mockQueenInsect));
    }

    /**
     * Requirement 1c
     */
    @Test
    public void testPlayerHasSpiders() throws Hive.IllegalMove {
        APlayer white = new APlayer(Hive.Player.WHITE);

        white.createTileToPlay(new Spider());
        white.createTileToPlay(new Spider());

        var i = 0;
        for (Insect insect : white.getTilesInHand().values()) {
            if (insect.getType() == Hive.Tile.SPIDER) {
                i += 1;
            }
        }

        assertEquals(2, i);
    }

    /**
     * Requirement 1c
     */
    @Test
    public void testPlayerHasTwoBeetles() throws Hive.IllegalMove {
        int i;
        APlayer white = new APlayer(Hive.Player.WHITE);

        white.createTileToPlay(new Beetle());
        white.createTileToPlay(new Beetle());

        i = 0;
        for (Insect insect : white.getTilesInHand().values()) {
            if (insect.getType() == Hive.Tile.BEETLE) {
                i += 1;
            }
        }

        assertEquals(2, i);
    }

    /**
     * Requirement 1c
     */
    @Test
    public void testPlayerHasSoldierAnts() throws Hive.IllegalMove {
        APlayer white = new APlayer(Hive.Player.WHITE);

        white.createTileToPlay(new SoldierAnt());
        white.createTileToPlay(new SoldierAnt());
        white.createTileToPlay(new SoldierAnt());

        var i = 0;
        for (Insect insect : white.getTilesInHand().values()) {
            if (insect.getType() == Hive.Tile.SOLDIER_ANT)
                i += 1;
        }

        assertEquals(3, i);
    }

    /**
     * Requirement 1c
     */
    @Test
    public void testPlayerHasGrasshoppers() throws Hive.IllegalMove {
        APlayer white = new APlayer(Hive.Player.WHITE);

        white.createTileToPlay(new Grasshopper());
        white.createTileToPlay(new Grasshopper());
        white.createTileToPlay(new Grasshopper());

        var i = 0;
        for (Insect insect : white.getTilesInHand().values()) {
            if (insect.getType() == Hive.Tile.GRASSHOPPER) {
                i += 1;
            }
        }

        assertEquals(3, i);
    }

    /**
     * Requirement 1c
     */
    @Test
    public void testMaxAmountTilesReached() {
        APlayer player = new APlayer(Hive.Player.WHITE);

        player.addTile(new QueenBee());
        player.addTile(new Beetle());
        player.addTile(new Beetle());
        player.addTile(new Spider());
        player.addTile(new Spider());
        player.addTile(new SoldierAnt());
        player.addTile(new SoldierAnt());
        player.addTile(new SoldierAnt());
        player.addTile(new Grasshopper());
        player.addTile(new Grasshopper());
        player.addTile(new Grasshopper());

        assertThrows(Hive.IllegalMove.class, () -> player.createTileToPlay(new Grasshopper()));
    }

    /**
     * Requirement 4e
     */
    @Test
    public void testIsQueenBeeInPlay() {
        Board board = new Board();
        APlayer white = new APlayer(Hive.Player.WHITE);

        white.tilesInHand.put(0, new QueenBee());
        board.board.put("0:0", new Stack<>());
        board.board.get("0:0").push(0);

        assertTrue(white.hasQueenBeeInPlay());
    }
}
