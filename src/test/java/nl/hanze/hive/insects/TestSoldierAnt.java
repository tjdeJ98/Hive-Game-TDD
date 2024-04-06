package nl.hanze.hive.insects;

import nl.hanze.hive.Game;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSoldierAnt {
    public TestSoldierAnt() {

    }

    /**
     * Requirement 5d
     */
    @Test
    void testSoldierAntBreaksHive() throws Hive.IllegalMove {
        Game game = new Game();

        game.getBoard().placeTile(1, 1,-1);
        game.getBoard().placeTile(2, 2,-1);

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.setCurrentPlayer(game.getWhitePlayer());
        game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        game.setCurrentPlayer(game.getWhitePlayer());

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 3, 0));
    }

    /**
     * Requirement 9a, 9c
     */
    @Test
    void testSoldierAntArrivesAtLocation() {
        Game game = new Game();

        game.getBoard().placeTile(1, 1,-1);
        game.getBoard().placeTile(2, 2,-1);
        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.move(0, 0, 2, -2);
        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertEquals(1, game.getBoard().getBoard().get("2:-2").peek());
    }

    /**
     * Requirement 9b
     */
    @Test
    void testMovesToOldPosition() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e){
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }
}
