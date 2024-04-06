package nl.hanze.hive.insects;

import nl.hanze.hive.Game;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBeetle {
    public TestBeetle() {

    }

    /**
     * Requirement 7a
     */
    @Test
    void testBeetleMovesTooMuch() throws Hive.IllegalMove {
        Game game = new Game();

        game.getBoard().placeTile(2, 1, -1);
        game.getBoard().placeTile(3, 2, -1);

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.setCurrentPlayer(game.getWhitePlayer());


        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 2, 0));
    }

    /**
     * Requirement 2f
     */
    @Test
    void testBeetleCanMoveOnOtherTile() throws Hive.IllegalMove {
        Game game = new Game();

        game.getBoard().placeTile(2, 1, -1);
        game.getBoard().placeTile(3, 2, -1);

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.move(0, 0, 0, -1);

        assertEquals(2, (int) game.getBoard().amountOfTilesInStack("0:-1"));
    }

    /**
     * Requirement 4b
     */
    @Test
    void testBeetleCantBePlayedOnOtherTile() {
        Game game = new Game();

        game.getBoard().placeTile(0, 0, 0);

        assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 0));
    }

    /**
     * Requirement 2f
     */
    @Test
    void testBeetleDoubleStack() throws Hive.IllegalMove {
        Game game = new Game();
        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(1, 0, 0);

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.BEETLE, 0, 1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.move(0, 1, 0, 0);


        assertEquals(3, (int) game.getBoard().amountOfTilesInStack("0:0"));
    }
}
