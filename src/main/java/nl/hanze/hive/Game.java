package nl.hanze.hive;

import nl.hanze.hive.insects.*;

public class Game implements Hive {
    private final APlayer black;
    private final APlayer white;
    private APlayer currentPlayer;
    private final Board board;

    public Game() {
        this.black = new APlayer(Player.BLACK);
        this.white = new APlayer(Player.WHITE);
        this.board = new Board();
        this.currentPlayer = white;
    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        if (currentPlayer.getTurn() != 1) {
            if (board.hasPlayableLocations(getOtherPlayer(currentPlayer.getColour()))) {
                if (turnFourIllegalMoveCheck(tile)) {
                    throw new IllegalMove("MAG NIET >:(");
                } else if (board.returnAllEmptyNeighbourSpots().contains(q + ":" + r)) {
                    makePlay(q, r, tile);
                } else {
                    throw new IllegalMove("MAG NIET >:(");
                }

            } else {
                throw new IllegalMove("No play options available for you.");
            }
        } else {
            if (board.getBoard().isEmpty()) {
                makePlay(q, r, tile);
            } else if (board.connectedToHive(q + ":" + r)) {
                makePlay(q, r, tile);
            } else {
                throw new IllegalMove("Must be placed against hive!");
            }
        }
    }


    private void makePlay(int q, int r, Hive.Tile tile) throws IllegalMove {
        board.placeTile(currentPlayer.createTileToPlay(makeTile(tile)), q, r);
        nextTurn();
    }

    private void nextTurn() {
        getCurrentPlayer().turnPlusOne();
        setCurrentPlayer(getOtherPlayer(getCurrentPlayer().getColour()));
    }

    private Insect makeTile(Tile tile) throws IllegalMove {
        switch (tile) {
            case QUEEN_BEE:
                return new QueenBee();
            case BEETLE:
                return new Beetle();
            case SOLDIER_ANT:
                return new SoldierAnt();
            case GRASSHOPPER:
                return new Grasshopper();
            case SPIDER:
                return new Spider();
            default:
                throw new IllegalMove("Hive tile type doesn't exist");
        }
    }

    public boolean hiveConnectionCheck(int q, int r) {
        if (!(currentPlayer.getColour() == Player.WHITE && currentPlayer.getTurn() == 1) && (!currentPlayer.getTilesInHand().isEmpty())) {
            return !getBoard().connectedToHive(q + ":" + r) ||
                    !getBoard().notConnectedToEnemyTile(q + ":" + r, getOtherPlayer(currentPlayer.getColour()));
        }
        return true;
    }

    public boolean turnFourIllegalMoveCheck(Tile tile) {
        return currentPlayer.getTurn() == 4 && !currentPlayer.hasQueenBeeInPlay() && tile != Tile.QUEEN_BEE;
    }

    private void moveToOwnLocation(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        if ((fromQ == toQ && fromR == toR) || board.checkIfTileEmpty(fromQ + ":" + fromR)) {
            throw new IllegalMove("Can't move to coordinates you're already on!");
        }
    }

    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        moveToOwnLocation(fromQ, fromR, toQ, toR);
        int foundPieceId = board.getIdByCoordinate(fromQ + ":" + fromR);

        if (!currentPlayer.getTilesInHand().containsKey(foundPieceId)) {
            throw new IllegalMove("Not your piece");
        }

        Insect foundInsect = currentPlayer.grabTileFromInt(foundPieceId);
        int movedPieceHolder = board.getBoard().get(fromQ + ":" + fromR).pop();
        boolean isBeetle = foundInsect.getType() == Tile.BEETLE;
        boolean connectedToHive = board.connectedToHive(toQ + ":" + toR);
        boolean onTopOfPiece = false;

        if (!board.checkIfTileEmpty(toQ + ":" + toR)) {
            onTopOfPiece = board.getBoard().get(toQ + ":" + toR).size() != 0;
        }

        if (currentPlayer.getTilesInHand().containsKey(foundPieceId) &&
                currentPlayer.hasQueenBeeInPlay() &&
                (board.connectedToHive(toQ + ":" + toR)) || (
                isBeetle && (connectedToHive || onTopOfPiece)
        )) {

            board.getBoard().get(fromQ + ":" + fromR).push(movedPieceHolder);
            if (foundInsect.getType() != Tile.GRASSHOPPER) {
                if (onTopOfPiece && isBeetle && (board.shift(fromQ, fromR, toQ, toR, isBeetle))) {
                    board.move(fromQ, fromR, toQ, toR);
                } else if (board.pathFinder(fromQ, fromR, toQ, toR, foundInsect)) {
                    board.move(fromQ, fromR, toQ, toR);
                } else {
                    board.getBoard().get(fromQ + ":" + fromR).push(movedPieceHolder);
                    throw new IllegalMove("No path found");
                }

            } else if (board.isFilledLine(fromQ, fromR, toQ, toR) && board.checkIfTileEmpty(toQ + ":" + toR)) {
                // grasshopper time :sunglasses:
                board.move(fromQ, fromR, toQ, toR);
            } else {
                board.getBoard().get(fromQ + ":" + fromR).push(movedPieceHolder);
                throw new IllegalMove("No path found");
            }
            nextTurn();
        } else {
            board.getBoard().get(fromQ + ":" + fromR).push(movedPieceHolder);
            throw new IllegalMove("Mag niet D:<");
        }

        if (currentPlayer.hasQueenBeeInPlay() || getOtherPlayer(currentPlayer.getColour()).hasQueenBeeInPlay()) {
            isWinner(currentPlayer.getColour());
        }
    }


    @Override
    public void pass() throws IllegalMove {
        if (board.hasPlayableLocations(getOtherPlayer(currentPlayer.getColour()))) {
            throw new IllegalMove("You can still make a play");
        } else if (board.hasPossibleMoves(currentPlayer)) {
            throw new IllegalMove("You can still move a piece");
        } else {
            nextTurn();
        }
    }


    @Override
    public boolean isWinner(Player player) {
        APlayer aplayer = getOtherPlayer(player);
        if (board.isSurrounded(aplayer.getQueenBeeId())) {
            return !isDraw();
        }
        return false;
    }

    @Override
    public boolean isDraw() {
        if (getBlackPlayer().hasQueenBeeInPlay() && getWhitePlayer().hasQueenBeeInPlay()) {
            return board.isSurrounded(getBlackPlayer().getQueenBeeId()) && board.isSurrounded(getWhitePlayer().getQueenBeeId());
        }
        return false;
    }

    public APlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(APlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public APlayer getWhitePlayer() {
        return white;
    }

    public APlayer getBlackPlayer() {
        return black;
    }

    public APlayer getOtherPlayer(Hive.Player colour) {
        if (colour == Player.WHITE) {
            return black;
        } else {
            return white;
        }
    }
}
