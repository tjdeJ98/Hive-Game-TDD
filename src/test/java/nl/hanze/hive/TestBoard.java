package nl.hanze.hive;

import nl.hanze.hive.insects.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {
    public TestBoard() {

    }

    /**
     * Requirement 4, 5, 6, 12
     */
    @Test
    public void testCheckTileOccupied() throws Hive.IllegalMove {
        Game game = new Game();
        int q = 0;
        int r = 0;

        game.getBoard().placeTile(game.getCurrentPlayer().createTileToPlay(new Beetle()), 0, 0);

        assertFalse(game.getBoard().checkIfTileEmpty(q + ":" + r));
    }

    /**
     * Requirement 3c, 3d
     */
    @Test
    public void testIfQueenBeeIsNotSurrounded() throws Hive.IllegalMove {
        Board board = new Board();
        APlayer white = new APlayer(Hive.Player.WHITE);

        board.placeTile(white.createTileToPlay(new QueenBee()), 0, 0);
        board.placeTile(white.createTileToPlay(new SoldierAnt()), -1, 0);
        board.placeTile(white.createTileToPlay(new SoldierAnt()), 0, -1);
        board.placeTile(white.createTileToPlay(new Spider()), 1, -1);
        board.placeTile(white.createTileToPlay(new Spider()), 1, 0);
        board.placeTile(white.createTileToPlay(new Grasshopper()), 0, 1);

        assertFalse(board.isSurrounded(white.getQueenBeeId()));
    }

    /**
     * Requirement 4c
     */
    @Test
    public void testWhenTilePlayedIsNotConnectedToHive() {
        Board board = new Board();

        board.placeTile(0, 0, 0);
        board.placeTile(1, 1, 0);

        assertFalse(board.connectedToHive("3:-3"));
    }

    /**
     * Requirement 4d
     */
    @Test
    public void testWhenTilePlayedIsConnectedToEnemyTile() {
        Board board = new Board();
        APlayer white = new APlayer(Hive.Player.WHITE);
        APlayer black = new APlayer(Hive.Player.BLACK);

        white.tilesInHand.put(0, new QueenBee());
        board.placeTile(0, 0, 0);

        black.tilesInHand.put(0, new QueenBee());
        board.placeTile(0, 0, 1);

        assertTrue(board.notConnectedToEnemyTile("0:2", black));
    }

    /**
     * Requirement 4d
     */
    @Test
    public void testWhenTilePlayedConnectedToEnemyAndOwnTile() {
        Board board = new Board();
        APlayer white = new APlayer(Hive.Player.WHITE);
        APlayer black = new APlayer(Hive.Player.BLACK);

        white.tilesInHand.put(0, new QueenBee());
        board.placeTile(0, 0, 0);

        black.tilesInHand.put(20, new QueenBee());
        board.placeTile(20, 0, 1);

        assertTrue(board.notConnectedToEnemyTile("1:0", black));
    }

    /**
     * Requirement 3b
     */
    @Test
    public void testMoveTile() {
        Board board = new Board();

        board.placeTile(0, 0, 0);
        board.move(0, 0, 0, -1);

        assertFalse(board.checkIfTileEmpty("0:-1"));
    }

    /**
     * Requirement 5c, 5d
     */
    @Test
    public void testBreaksHiveInTwo() {
        Game game = new Game();

        game.getBoard().placeTile(20, 0, 0);
        game.getBoard().placeTile(0, -2, -1);
        game.getBoard().placeTile(1, -1, -1);
        game.getBoard().placeTile(2, -3, 0);
        game.getBoard().placeTile(3, -2, 0);
        game.getBoard().placeTile(4, -1, -0);
        game.getBoard().placeTile(5, -3, 1);
        game.getBoard().placeTile(6, -2, 1);
        game.getBoard().placeTile(7, 1, 0);
        game.getBoard().placeTile(8, 2, -1);

        assertFalse(game.getBoard().breaksHive(0, 0, 0, 1));
    }

    /**
     * Requirement 6
     */
    @Test
    public void testShiftIsIllegal() {
        Game game = new Game();

        game.getBoard().placeTile(0, 1, 1);
        game.getBoard().placeTile(1, 1, 2);
        game.getBoard().placeTile(2, 2, 0);

        assertFalse(game.getBoard().shift(1, 1, 2, 1, false));
    }

    /**
     * Requirement 6
     */
    @Test
    public void testTwoMoveShiftIsIllegal(){
        Game game = new Game();

        game.getBoard().placeTile(0, 0,0);

        assertFalse(game.getBoard().shift(0, 0, -2, 2, false));
    }

    /**
     * Requirement 4, 5, 6, 12
     */
    @Test
    public void testFindMatchingNeighbours() {
        Game game = new Game();
        List<String> neighbours = new ArrayList<>();

        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(1, 0, 1);
        game.getBoard().placeTile(2, -1, 0);
        neighbours.add("0:1");
        neighbours.add("-1:0");

        assertEquals(neighbours, game.getBoard().findMatchingNeighbours(0, 0, -1, 1));
    }

    /**
     * Requirement 2f
     */
    @Test
    public void testAmountOfTilesInStack() {
        Game game = new Game();

        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(1, 0, 0);
        game.getBoard().placeTile(2, 0, 0);

        assertEquals(3, game.getBoard().amountOfTilesInStack("0:0"));
    }

    /**
     * Requirement 4, 5, 6, 12
     */
    @Test
    public void testGetNeighbours() {
        Game game = new Game();
        List<String> neighbours = new ArrayList<>((Arrays.asList("1:0", "0:1", "1:-1", "-1:0", "-1:1", "0:-1")));

        game.getBoard().placeTile(0, 0, 0);

        assertEquals(neighbours, game.getBoard().getNeighbours("0:0"));
    }

    /**
     * Requirement 6
     */
    @Test
    public void testNotConnectedShiftIllegal() {
        Game game = new Game();

        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(1, 0, -1);
        game.getBoard().placeTile(2, 2, -1);

        assertFalse(game.getBoard().shift(0, 0, 1, 0, false));
    }

    /**
     * Requirement 5
     */
    @Test
    public void testMoveToCurrentCoordinatesIsIllegal(){
        Game game = new Game();

        game.getBoard().placeTile(0, 0, 0);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }

    /**
     * Requirement 11, 6
     */
    @Test
    public void testIsLineFilled() {
        Game game = new Game();

        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(1, 0, -1);
        game.getBoard().placeTile(2, 0, -2);
        game.getBoard().placeTile(3, 0, -3);

        assertTrue(game.getBoard().isFilledLine(0, 0, 0, -4));
    }

    /**
     * Requirement 12
     */
    @Test
    public void testWhiteNoTilePlayable() {
        Game game = new Game();

        game.getWhitePlayer().tilesInHand.put(0, new Spider());
        game.getBlackPlayer().tilesInHand.put(20, new Spider());
        game.getBlackPlayer().tilesInHand.put(21, new Spider());
        game.getBlackPlayer().tilesInHand.put(22, new Spider());
        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(20, -1, 0);
        game.getBoard().placeTile(21, 1, -1);
        game.getBoard().placeTile(22, 0, 1);

        assertFalse(game.getBoard().hasPlayableLocations(game.getBlackPlayer()));
    }

    /**
     * Requirement 2f, 4, 5, 12
     */
    @Test
    public void testWhiteTilePlayable() {
        Game game = new Game();

        game.getWhitePlayer().tilesInHand.put(0, new Spider());
        game.getBlackPlayer().tilesInHand.put(20, new Spider());
        game.getBlackPlayer().tilesInHand.put(21, new Spider());
        game.getBoard().placeTile(0, 0, 0);
        game.getBoard().placeTile(20, -1, 0);
        game.getBoard().placeTile(21, 1, -1);

        assertTrue(game.getBoard().hasPlayableLocations(game.getBlackPlayer()));
    }

    /**
     * Requirement 2f, 12
     */
    @Test
    public void testHasNoPossibleMoves() {
        Game game = new Game();

        game.getBoard().placeTile(0, 1, 0);
        game.getBoard().placeTile(1, 0, 1);
        game.getBoard().placeTile(2, 1, -1);
        game.getBoard().placeTile(3, -1, 0);
        game.getBoard().placeTile(4, -1, 1);
        game.getBoard().placeTile(5, 0, -1);

        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new SoldierAnt());
        game.getWhitePlayer().addTile(new SoldierAnt());

        game.getBoard().placeTile(20, 0, 0);
        game.getBlackPlayer().addTile(new QueenBee());

        game.setCurrentPlayer(game.getBlackPlayer());
        assertFalse(game.getBoard().hasPossibleMoves(game.getBlackPlayer()));
    }

    /**
     * Requirement 7, 12
     */
    @Test
    public void testHasPossibleMoveBecauseBeetle() {
        Game game = new Game();

        game.getBoard().placeTile(0, 1, 0);
        game.getBoard().placeTile(1, 0, 1);
        game.getBoard().placeTile(2, 1, -1);
        game.getBoard().placeTile(3, -1, 0);
        game.getBoard().placeTile(4, -1, 1);

        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new SoldierAnt());

        game.getBoard().placeTile(20, 0, 0);
        game.getBlackPlayer().addTile(new Beetle());

        game.setCurrentPlayer(game.getBlackPlayer());
        assertTrue(game.getBoard().hasPossibleMoves(game.getBlackPlayer()));
    }

    /**
     * Requirement 11, 12
     */
    @Test
    public void testHasPossibleMoveBecauseGrasshopper() {
        Game game = new Game();

        game.getBoard().placeTile(0, 1, 0);
        game.getBoard().placeTile(1, 0, 1);
        game.getBoard().placeTile(2, 1, -1);
        game.getBoard().placeTile(3, -1, 0);
        game.getBoard().placeTile(4, -1, 1);
        game.getBoard().placeTile(5, 0, -1);

        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new SoldierAnt());
        game.getWhitePlayer().addTile(new SoldierAnt());

        game.getBoard().placeTile(20, 0, 0);
        game.getBlackPlayer().addTile(new Grasshopper());

        game.setCurrentPlayer(game.getBlackPlayer());
        assertTrue(game.getBoard().hasPossibleMoves(game.getBlackPlayer()));
    }
}
