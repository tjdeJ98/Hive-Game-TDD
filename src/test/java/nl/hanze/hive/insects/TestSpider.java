package nl.hanze.hive.insects;

import nl.hanze.hive.Game;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSpider {
    public TestSpider() {

    }

    /**
     * Requirement 10a, 10c
     */
    @Test
    void testIfSpiderMovesTooMuch() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.SPIDER, 0, 0);

        game.setCurrentPlayer(game.getWhitePlayer());
        game.getBoard().placeTile(1, 1, -1);
        game.getBoard().placeTile(2, 2, -1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 3, -2));
    }

    /**
     * Requirement 2f
     */
    @Test
    void testSpiderCantStack() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.SPIDER, 0, 0);

        game.setCurrentPlayer(game.getWhitePlayer());
        game.getBoard().placeTile(1, 1, -1);
        game.getBoard().placeTile(2, 2, -1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 1, -1));
    }

    /**
     * Requirement 10b
     */
    @Test
    void testMovesToOldPosition() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.SPIDER, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e) {
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }

}
