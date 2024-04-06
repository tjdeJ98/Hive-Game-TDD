package nl.hanze.hive;

import org.junit.jupiter.api.Test;
import nl.hanze.hive.insects.*;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {
    public TestGame() {

    }

    /**
     * Helper function
     */
    private void addTurns(APlayer player, int turns) {
        for (int i = 0; i < turns; i++) {
            player.turnPlusOne();
        }
    }

    /**
     * Helper function
     */
    private void createDraw(Game game, APlayer white, APlayer black) {
        white.tilesInHand.put(0, new QueenBee());
        game.getBoard().placeTile(0, 0, 0);
        white.turnPlusOne();

        white.tilesInHand.put(1, new Beetle());
        game.getBoard().placeTile(1, 0, 1);
        white.turnPlusOne();

        white.tilesInHand.put(2, new Spider());
        game.getBoard().placeTile(2, 1, 0);
        white.turnPlusOne();

        white.tilesInHand.put(3, new Grasshopper());
        game.getBoard().placeTile(3, 0, -1);
        white.turnPlusOne();

        white.tilesInHand.put(5, new Spider());
        game.getBoard().placeTile(5, -1, 1);
        white.turnPlusOne();

        white.tilesInHand.put(5, new Beetle());
        game.getBoard().placeTile(6, -1, 0);
        white.turnPlusOne();

        black.tilesInHand.put(20, new QueenBee());
        game.getBoard().placeTile(20, 2, -2);
        black.turnPlusOne();

        black.tilesInHand.put(21, new Beetle());
        game.getBoard().placeTile(21, 1, -2);
        black.turnPlusOne();

        black.tilesInHand.put(22, new Spider());
        game.getBoard().placeTile(22, 2, -3);
        black.turnPlusOne();

        black.tilesInHand.put(23, new Grasshopper());
        game.getBoard().placeTile(23, 3, -3);
        black.turnPlusOne();

        black.tilesInHand.put(24, new Spider());
        game.getBoard().placeTile(24, 3, -2);
        black.turnPlusOne();

        black.tilesInHand.put(25, new Beetle());
        game.getBoard().placeTile(25, 2, -1);
        black.turnPlusOne();
    }

    /**
     * Requirement 1
     */
    @Test
    public void testIfGameExists() {
        Game game;

        game = new Game();

        assertEquals(Game.class, game.getClass());
    }

    /**
     * Requirement
     */
    @Test
    public void testCreatePlayerWhite() {
        Game game = new Game();

        assertEquals(game.getWhitePlayer().getColour(), Hive.Player.WHITE);
    }

    /**
     * Requirement
     */
    @Test
    public void testCreatePlayerBlack() {
        Game game = new Game();

        assertEquals(game.getBlackPlayer().getColour(), Hive.Player.BLACK);
    }

    /**
     * Requirement
     */
    @Test
    public void testSetCurrentPlayer() {
        Game game = new Game();
        APlayer mockWhiteAPlayer = new APlayer(Hive.Player.WHITE);

        game.setCurrentPlayer(mockWhiteAPlayer);

        assertNotNull(game.getCurrentPlayer());
    }

    /**
     * Requirement
     */
    @Test
    public void testIsCurrentPlayerAPlayer() {
        Game game = new Game();
        APlayer mockWhiteAPlayer = new APlayer(Hive.Player.WHITE);

        game.setCurrentPlayer(mockWhiteAPlayer);

        assertEquals(game.getCurrentPlayer().getClass(), APlayer.class);
    }

    /**
     * Requirement
     */
    @Test
    public void testGetCurrentPlayer() {
        Game game = new Game();
        APlayer currentPlayer = new APlayer(Hive.Player.WHITE);

        game.setCurrentPlayer(currentPlayer);
        currentPlayer = game.getCurrentPlayer();

        assertEquals(currentPlayer.getClass(), APlayer.class);
    }

    /**
     * Requirement 3
     */
    @Test
    public void testGameMakesBoard() {
        Game game = new Game();

        assertNotNull(game.getBoard());
    }

    /**
     * Requirement 2
     */
    @Test
    public void testPlayCoordinatePiece() throws Hive.IllegalMove {
        int q = 0;
        int r = 0;
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        } catch (Hive.IllegalMove e) {
            throw new Hive.IllegalMove("Illegal move was made, niet doen");
        }

        assertFalse(game.getBoard().board.get(q + ":" + r).isEmpty());
    }

    /**
     * Requirement 2
     */
    @Test
    public void testPiecePlaced() throws Hive.IllegalMove {
        int q = 0;
        int r = 0;
        Game game = new Game();
        Hive.Tile tile = Hive.Tile.QUEEN_BEE;
        Stack<Integer> tiles;

        try {
            game.play(tile, 0, 0);
        } catch (Hive.IllegalMove e) {
            throw new Hive.IllegalMove("Illegal move was made, niet doen");
        }

        tiles = game.getBoard().getBoard().get(q + ":" + r);

        assertFalse(tiles.isEmpty());
    }

    /**
     * Requirement 2
     */
    @Test
    public void testIsPlayedPiece() throws Hive.IllegalMove {
        int q = 0;
        int r = 0;
        Game game = new Game();
        Hive.Tile tile = Hive.Tile.QUEEN_BEE;
        Stack<Integer> tiles;

        try {
            game.play(tile, 0, 0);
        } catch (Hive.IllegalMove e) {
            throw new Hive.IllegalMove("Illegal move was made, niet doen");
        }

        tiles = game.getBoard().getBoard().get(q + ":" + r);
        Insect playedTile = game.getWhitePlayer().getTilesInHand().get(tiles.peek());

        assertEquals(playedTile.getType(), tile);
    }

    /**
     * Requirement 4
     */
    @Test
    public void testPlayFailsWhenTileOccupied() throws Hive.IllegalMove {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        } catch (Hive.IllegalMove e) {
            throw new Hive.IllegalMove("Illegal move was made, niet doen");
        }

        assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 0));
    }

    /**
     * Requirement 4
     */
    @Test
    public void testGotTileInHand() throws Hive.IllegalMove {
        APlayer whitePlayer = new APlayer(Hive.Player.WHITE);
        Insect queenBee = new QueenBee();
        whitePlayer.createTileToPlay(queenBee);

        assertTrue(whitePlayer.getTilesInHand().containsValue(queenBee));
    }

    /**
     * Requirement 3
     */
    @Test
    public void testIsWinner() {
        Game game = new Game();
        APlayer white = game.getWhitePlayer();

        // Turn 1
        white.tilesInHand.put(0, new QueenBee());
        game.getBoard().placeTile(0, 0, 0);
        white.turnPlusOne();

        // Turn 2
        white.tilesInHand.put(1, new Beetle());
        game.getBoard().placeTile(1, 0, 1);
        white.turnPlusOne();

        // Turn 3
        white.tilesInHand.put(2, new Spider());
        game.getBoard().placeTile(2, 1, 0);
        white.turnPlusOne();

        // Turn 4
        white.tilesInHand.put(3, new Grasshopper());
        game.getBoard().placeTile(3, 0, -1);
        white.turnPlusOne();

        // Turn 5
        white.tilesInHand.put(4, new Grasshopper());
        game.getBoard().placeTile(4, 1, -1);
        white.turnPlusOne();

        // Turn 6
        white.tilesInHand.put(5, new Spider());
        game.getBoard().placeTile(5, -1, 1);
        white.turnPlusOne();

        // Turn 7
        white.tilesInHand.put(5, new Beetle());
        game.getBoard().placeTile(6, -1, 0);
        white.turnPlusOne();

        assertTrue(game.isWinner(game.getBlackPlayer().getColour()));
    }

    /**
     * Requirement 3
     */
    @Test
    public void testIsNotWinner() {
        Game game = new Game();
        APlayer white = game.getWhitePlayer();

        // Turn 1
        white.tilesInHand.put(0, new QueenBee());
        game.getBoard().placeTile(0, 0, 0);
        white.turnPlusOne();

        // Turn 2
        white.tilesInHand.put(1, new SoldierAnt());
        game.getBoard().placeTile(1, -1, 0);
        white.turnPlusOne();

        assertFalse(game.isWinner(game.getBlackPlayer().getColour()));
    }

    /**
     * Requirement 3
     */
    @Test
    public void testIsNotWinnerBecauseOfDraw() {
        Game game = new Game();

        createDraw(game, game.getWhitePlayer(), game.getBlackPlayer());

        game.getWhitePlayer().tilesInHand.put(7, new Grasshopper());
        game.getBoard().placeTile(7, 1, -1);
        game.getWhitePlayer().turnPlusOne();

        assertFalse(game.isWinner(game.getWhitePlayer().getColour()));
    }

    /**
     * Requirement 3
     */
    @Test
    public void testIsDraw() {
        Game game = new Game();

        createDraw(game, game.getWhitePlayer(), game.getBlackPlayer());

        game.getWhitePlayer().tilesInHand.put(7, new Grasshopper());
        game.getBoard().placeTile(7, 1, -1);
        game.getWhitePlayer().turnPlusOne();

        assertTrue(game.isDraw());
    }

    /**
     * Requirement 4e
     */
    @Test
    public void testQueenBeeIsPlayedByFourthTurn() throws Hive.IllegalMove {
        Game game = new Game();

        try {
            //White 1
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            //Black 1
            game.play(Hive.Tile.SOLDIER_ANT, 0, -1);
            //White 2
            game.play(Hive.Tile.SOLDIER_ANT, 0, 1);
            //Black 2
            game.play(Hive.Tile.SPIDER, 1, -2);
            //White 3
            game.play(Hive.Tile.SPIDER, -1, 1);
            //Black 3
            game.play(Hive.Tile.GRASSHOPPER, 0, -2);

        } catch (Hive.IllegalMove e) {
            throw new Hive.IllegalMove("Illegal move was made, niet doen");
        }
        //White 4
        assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.GRASSHOPPER, -2, 1));
    }

    /**
     * Requirement 4
     */
    @Test
    public void testHiveConnectionCheck() {
        Game game = new Game();
        APlayer white = game.getWhitePlayer();

        white.tilesInHand.put(0, new QueenBee());
        game.getBoard().placeTile(0, 0, 0);

        assertTrue(game.hiveConnectionCheck(1, 0));
    }

    /**
     * Requirement 4e
     */
    @Test
    public void testTurnFourNoWhiteQueenInPlay() {
        Game game = new Game();
        APlayer white = game.getWhitePlayer();

        // Hard code queen bee on board black player
        // up turn to 4 for blackplayer
        white.tilesInHand.put(10, new Grasshopper());
        game.getBoard().placeTile(10, 2, -2);
        addTurns(white, 3);

        // Black 4
        assertTrue(game.turnFourIllegalMoveCheck(Hive.Tile.GRASSHOPPER));
    }

    /**
     * Requirement 4e
     */
    @Test
    public void testTurnFourBlackQueenInPlayCheck() {
        Game game = new Game();

        // Hard code queen bee on board black player
        // up turn to 4 for blackplayer
        game.getBlackPlayer().tilesInHand.put(10, new QueenBee());
        game.getBoard().board.put("2:-2", new Stack<>());
        game.getBoard().board.get("2:-2").push(10);
        addTurns(game.getBlackPlayer(), 3);

        // Black 4
        assertFalse(game.turnFourIllegalMoveCheck(Hive.Tile.GRASSHOPPER));
    }

    /**
     * Requirement 5b
     */
    @Test
    public void testQueenBeeNotPlayedBeforeTurn4() {
        Game game = new Game();

        addTurns(game.getWhitePlayer(), 3);

        assertTrue(game.turnFourIllegalMoveCheck(Hive.Tile.GRASSHOPPER));
    }

    /**
     * Requirement 5
     */
    @Test
    public void testNoStoneFoundOnStartGivenMove() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 2, -2);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 1));
    }

    /**
     * Requirement 3, 5
     */
    @Test
    public void testWrongPlayerStoneFoundOnGivenMove() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }

    /**
     * Requirement 5b
     */
    @Test
    public void testCantMoveNoQueenBeeInPlay() throws Hive.IllegalMove {
        Game game = new Game();

        game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, 1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 1, 0));
    }

    /**
     * Requirement 5d
     */
    @Test
    public void testAfterMoveNotConnectedToHive() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.play(Hive.Tile.QUEEN_BEE, 2, -1);
            game.setCurrentPlayer(game.getWhitePlayer());
        } catch (Hive.IllegalMove e) {
            System.out.println(e);
        }
        game.getBoard().placeTile(1, 0, -1);
        game.getBoard().placeTile(2, 0, 1);
        game.getBoard().placeTile(3, 1, -1);

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, -1, 0));
    }

    /**
     * TODO slechte TDD?
     * Requirement 9
     */
    @Test
    public void testPathFinderWalkIsLegal() {
        Game game = new Game();
        Insect tile = null;

        game.getBoard().placeTile(1, 1, 0);
        game.getBoard().placeTile(2, 2, 0);
        game.getBoard().placeTile(3, 2, 1);

        try {
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            tile = game.getWhitePlayer().tilesInHand.get(0);
        } catch (Hive.IllegalMove e) {
            System.out.println("mag niet");
        }

        assertTrue(game.getBoard().pathFinder(0, 0, 2, 2, tile));
    }

    /**
     * Requirement 5, 9, 10
     */
    @Test
    public void testPathFinderWalkIsIllegal() {
        Game game = new Game();
        Insect tile = null;

        game.getBoard().placeTile(1, 1, 0);
        game.getBoard().placeTile(2, 2, 0);
        game.getBoard().placeTile(3, 2, -1);

        try {
            game.play(Hive.Tile.SOLDIER_ANT, 0, 0);
            tile = game.getWhitePlayer().tilesInHand.get(0);
        } catch (Hive.IllegalMove e) {
            System.out.println("mag niet");
        }

        assertFalse(game.getBoard().pathFinder(0, 0, 2, 2, tile));
    }

    /**
     * Requirement 5, 10, 12
     */
    @Test
    public void testPathFinderWalkTooLongThusIllegal() {
        Game game = new Game();
        Insect tile = null;

        game.getBoard().placeTile(1, 1, 0);
        game.getBoard().placeTile(2, 2, 0);
        game.getBoard().placeTile(3, 3, 0);
        game.getBoard().placeTile(4, 2, 1);
        game.getBoard().placeTile(5, 0, 3);

        try {
            game.play(Hive.Tile.SPIDER, 0, 0);
            tile = game.getWhitePlayer().tilesInHand.get(0);
        } catch (Hive.IllegalMove e) {
            System.out.println("mag niet");
        }

        assertFalse(game.getBoard().pathFinder(0, 0, -1, 3, tile));
    }

    /**
     * Requirement 5
     */
    @Test
    void testCantMoveToCurrentCoordinates() {
        Game game = new Game();
        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());

        } catch (Hive.IllegalMove e) {
            System.out.println("mag niet");
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, 0, 0));
    }

    /**
     * Requirement 5c
     */
    @Test
    void testOneQueenBeeCanMove() {
        Game game = new Game();

        game.getBoard().placeTile(1, 0, 1);

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, 0);
            game.setCurrentPlayer(game.getWhitePlayer());
            game.move(0, 0, 1, 0);

        } catch (Hive.IllegalMove e) {
            System.out.println("mag niet");
        }
        assertEquals(1, game.getBoard().amountOfTilesInStack("1:0"));
    }

    /**
     * Requirement 2f
     */
    @Test
    void testUnderlyingTileCanNotMove() {
        Game game = new Game();

        try {
            game.play(Hive.Tile.QUEEN_BEE, 0, 0);        //white
            game.play(Hive.Tile.QUEEN_BEE, 0, -1);       //black
            game.setCurrentPlayer(game.getBlackPlayer());
            game.play(Hive.Tile.BEETLE, -1, -1);          //black
            game.setCurrentPlayer(game.getBlackPlayer());
            game.move(-1, -1, 0, -1);
            game.setCurrentPlayer(game.getBlackPlayer());
            game.move(0, -1, 0, 0);     //black
        } catch (Hive.IllegalMove e) {
            System.out.println(e);
        }

        assertThrows(Hive.IllegalMove.class, () -> game.move(0, 0, -1, 1)); //white
    }

    /**
     * Requirement 12a
     */
    @Test
    void testPassAllowed() throws Hive.IllegalMove {
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
        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Beetle());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new Spider());
        game.getWhitePlayer().addTile(new SoldierAnt());
        game.getWhitePlayer().addTile(new SoldierAnt());
        game.getWhitePlayer().addTile(new SoldierAnt());
        game.getWhitePlayer().addTile(new Grasshopper());
        game.getWhitePlayer().addTile(new Grasshopper());
        game.getWhitePlayer().addTile(new Grasshopper());

        game.setCurrentPlayer(game.getBlackPlayer());
        game.pass();

        assertEquals(Hive.Player.WHITE, game.getCurrentPlayer().getColour());
    }

}
