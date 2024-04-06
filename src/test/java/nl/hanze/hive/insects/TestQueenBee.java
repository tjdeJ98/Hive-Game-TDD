package nl.hanze.hive.insects;

import nl.hanze.hive.Game;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueenBee {
    public TestQueenBee() {

    }

    /**
     * Requirement 8a
     */
    @Test
    void testQueenBeeMovesTooMuch() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        game.setCurrentPlayer(game.getWhitePlayer());
        game.getBoard().placeTile(1, 1, -1);
        game.getBoard().placeTile(2, 2, -1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 2, 0));
    }

    /**
     * Requirement 8b
     */
    @Test
    void testQueenBeeCantStack() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.getBoard().placeTile(1, 1, -1);
        game.getBoard().placeTile(2, 2, -1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 1, -1));
    }
}
