package nl.hanze.hive.insects;

import nl.hanze.hive.Game;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGrasshopper {
    public TestGrasshopper() {

    }

    /**
     * Requirement 11a, 11e
     */
    @Test
    void testGrasshopperJumpsOverTile() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 1, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.GRASSHOPPER, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.move(0, 0, 2, -2);
        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertEquals(1, game.getBoard().getBoard().get("2:-2").peek());
    }

    /**
     * Requirement 11b
     */
    @Test
    void testMovesToOldPosition() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.GRASSHOPPER, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }

    /**
     * Requirement 11b
     */
    @Test
    void testJumpsToNeighbourTile() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.GRASSHOPPER, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 1, -1));
    }

    /**
     * Requirement 11d
     */
    @Test
    void testJumpsOnTakenTile() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.getBoard().placeTile(20, 0, -2);
            game.play(Hive.Tile.GRASSHOPPER, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, -2));
    }
}
