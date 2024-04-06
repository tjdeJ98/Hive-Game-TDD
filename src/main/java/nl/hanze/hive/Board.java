package nl.hanze.hive;

import nl.hanze.hive.insects.Insect;

import java.util.*;

public class Board {
    final HashMap<String, Stack<Integer>> board;
    final List<String> directions = new ArrayList<>(Arrays.asList("1:0", "0:1", "1:-1", "-1:0", "-1:1", "0:-1"));

    public Board() {
        this.board = new HashMap<>();
    }

    private void addToBoard(int q, int r) {
        board.put(q + ":" + r, new Stack<>());
    }

    public void placeTile(Integer tileID, int q, int r) {
        if (!board.containsKey(q + ":" + r))
            addToBoard(q, r);
        board.get(q + ":" + r).push(tileID);
    }

    public boolean checkIfTileEmpty(String coordinate) {
        if (board.containsKey(coordinate)) {
            return board.get(coordinate).isEmpty();
        } else {
            return true;
        }
    }

    public boolean hasPossibleMoves(APlayer player) {
        for (Map.Entry<Integer, Insect> piece : player.getTilesInHand().entrySet()) {
            String pieceLocation = getIdKey(piece.getKey());
            if (movePossibilities(pieceLocation, piece.getValue()).size() > 0)
                return true;
        }

        return false;
    }

    public Set<String> movePossibilities(String location, Insect piece) {
        Set<String> foundValidLocations = new HashSet<>();
        Set<String> openSpots;
        int[] fromQR = getSplitCoordinate(location);

        if (piece.getType() == Hive.Tile.BEETLE) {
            openSpots = new HashSet<>(getNeighbours(location));
        } else {
            openSpots = returnAllEmptyNeighbourSpots();
        }

        for (String toLocation : openSpots) {
            int[] toQR = getSplitCoordinate(toLocation);

            if (piece.getType() == Hive.Tile.GRASSHOPPER) {
                if (isFilledLine(fromQR[0], fromQR[1], toQR[0], toQR[1]))
                    foundValidLocations.add(toQR[0] + ":" + toQR[1]);
            } else {
                if (pathFinder(fromQR[0], fromQR[1], toQR[0], toQR[1], piece))
                    foundValidLocations.add(toQR[0] + ":" + toQR[1]);
            }
        }

        return foundValidLocations;
    }

    private boolean legalMoveChecker(String neighbour, Boolean isBeetle, int[] fromQR, int[] toQR) {
        if (checkIfTileEmpty(neighbour) || isBeetle) {
            if (shift(fromQR[0], fromQR[1], toQR[0], toQR[1], isBeetle)) {
                return connectedToHive(neighbour);
            }
        }
        return false;
    }

    public boolean pathFinder(int fromQ, int fromR, int toQ, int toR, Insect piece) {
        int movesMade = 0;
        int pieceHolder;
        boolean isBeetle = piece.getType() == Hive.Tile.BEETLE;
        List<Integer> foundPaths = new LinkedList<>();
        List<String> walkablePaths = new ArrayList<>();
        List<String> pastLocations = new LinkedList<>();

        walkablePaths.add(fromQ + ":" + fromR);

        while (walkablePaths.size() > 0) {
            List<String> tempWalkablePaths = new ArrayList<>();

            for (String walkablePath : walkablePaths) {
                List<String> neighbours = getNeighbours(walkablePath);
                int[] fromQR = getSplitCoordinate(walkablePath);

                for (String neighbour : neighbours) {
                    int[] toQR = getSplitCoordinate(neighbour);

                    pieceHolder = board.get(fromQ + ":" + fromR).pop();
                    if (legalMoveChecker(neighbour, isBeetle, fromQR, toQR)) {
                        getBoard().get(fromQ + ":" + fromR).push(pieceHolder);

                        if (breaksHive(fromQR[0], fromQR[1], toQR[0], toQR[1])) {
                            pieceHolder = getBoard().get(fromQ + ":" + fromR).pop();

                            if (neighbour.equals(toQ + ":" + toR)) {
                                foundPaths.add(movesMade + 1);
                            } else {
                                tempWalkablePaths.add(toQR[0] + ":" + toQR[1]);
                            }
                        }
                    }
                    if (pieceHolder != -1)
                        getBoard().get(fromQ + ":" + fromR).push(pieceHolder);
                }

                pastLocations.add(walkablePath);
            }

            tempWalkablePaths.removeAll(walkablePaths);
            walkablePaths = tempWalkablePaths;
            walkablePaths.removeAll(pastLocations);
            movesMade += 1;
        }

        if (!foundPaths.isEmpty()) {
            if (piece.getAmountOfMoves() != 0) {
                if (piece.getType() != Hive.Tile.SPIDER) {
                    int leastSteps = Collections.min(foundPaths);
                    return piece.getAmountOfMoves() >= leastSteps;
                } else {
                    return foundPaths.contains(piece.getAmountOfMoves());
                }
            }
            return true;
        }
        return false;
    }

    public boolean notConnectedToEnemyTile(String coordinates, APlayer enemyPlayer) {
        int[] coordinate = getSplitCoordinate(coordinates);

        for (String direction : directions) {
            int[] directionQR = getSplitCoordinate(direction);
            String coords = ((coordinate[0] + directionQR[0]) + ":" + (coordinate[1] + directionQR[1]));

            if (!checkIfTileEmpty(coords)) {
                int key = board.get(coords).peek();
                if (enemyPlayer.getTilesInHand().containsKey(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSurrounded(Integer piece) {
        int[] coordinates = getSplitCoordinate(getIdKey(piece));

        for (String direction : directions) {
            int[] directs = getSplitCoordinate(direction);

            if (checkIfTileEmpty((coordinates[0] + directs[0]) + ":" + (coordinates[1] + directs[1])))
                return false;
        }
        return true;
    }

    public boolean connectedToHive(String coordinates) {
        // Mag zichzelf niet mee tellen als deel van de hive
        int[] coordinate = getSplitCoordinate(coordinates);

        for (String direction : directions) {
            int[] directs = getSplitCoordinate(direction);

            if (!checkIfTileEmpty((coordinate[0] + directs[0]) + ":" + (coordinate[1] + directs[1])))
                return true;
        }
        return false;
    }

    public void move(int fromQ, int fromR, int toQ, int toR) {
        String toKey = toQ + ":" + toR;
        if (!board.containsKey(toKey)) {
            addToBoard(toQ, toR);
        }

        board.get(toKey).push(board.get(fromQ + ":" + fromR).pop());
    }

    public HashMap<String, Stack<Integer>> getBoardCopy() {
        var boardCopy = new HashMap<String, Stack<Integer>>();

        for (Map.Entry<String, Stack<Integer>> coord : board.entrySet()) {
            boardCopy.put(coord.getKey(), new Stack<>());
            for (Integer i : coord.getValue())
                boardCopy.get(coord.getKey()).push(i);
        }

        return boardCopy;
    }

    public void putFromAndToInBoard(String fromKey, String toKey, HashMap<String, Stack<Integer>> board) {
        if (!board.containsKey(toKey)) {
            board.put(toKey, new Stack<>());
        }

        if (!board.containsKey(fromKey)) {
            board.put(fromKey, new Stack<>());
            board.get(fromKey).push(69);
        }
    }

    public String getOneCoordinateFromBoard(HashMap<String, Stack<Integer>> boardCopy) {
        String key = null;
        for (Map.Entry<String, Stack<Integer>> location : boardCopy.entrySet()) {
            if (location.getValue().size() > 0 && key == null) {
                key = location.getKey();
            }
        }
        return key;
    }

    public boolean breaksHive(int fromQ, int fromR, int toQ, int toR) {
        var finalFoundClusterSet = new LinkedHashSet<String>();
        var newFoundClusterSet = new LinkedHashSet<String>();
        var boardCopy = new HashMap<>(getBoardCopy());
        String toKey = toQ + ":" + toR;
        String fromKey = fromQ + ":" + fromR;

        putFromAndToInBoard(fromKey, toKey, boardCopy);

        var tempHolder = boardCopy.get(fromQ + ":" + fromR).pop();
        boardCopy.get(toKey).push(tempHolder);

        String key = getOneCoordinateFromBoard(boardCopy);

        newFoundClusterSet.add(key);

        // for each direction check if it has a neighbour and add to LinkedCoords
        while (!newFoundClusterSet.isEmpty()) {
            finalFoundClusterSet.addAll(newFoundClusterSet);

            newFoundClusterSet = neighboursAdded(newFoundClusterSet, boardCopy);

            for (String coorF : finalFoundClusterSet) {
                newFoundClusterSet.remove(coorF);
            }
        }

        boolean containsAll = true;
        for (String coordinate : boardCopy.keySet()) {
            if (boardCopy.get(coordinate).size() > 0) {
                if (!finalFoundClusterSet.contains(coordinate)) {
                    containsAll = false;
                }
            }
        }
        return containsAll;
    }

    private LinkedHashSet<String> neighboursAdded(LinkedHashSet<String> newFoundClusterSet, HashMap<String, Stack<Integer>> boardCopy) {
        var result = new LinkedHashSet<String>();

        if (!newFoundClusterSet.isEmpty()) {
            for (String coor : newFoundClusterSet) {
                int[] coors = getSplitCoordinate(coor);

                for (String direction : directions) {
                    int[] dir = getSplitCoordinate(direction);

                    String targetCoordinate = (coors[0] + dir[0]) + ":" + (coors[1] + dir[1]);

                    if (boardCopy.containsKey(targetCoordinate) && !boardCopy.get(targetCoordinate).isEmpty()) {
                        result.add(targetCoordinate);
                    }

                }
            }
        }
        return result;
    }

    public boolean shift(int fromQ, int fromR, int toQ, int toR, boolean isBeetle) {
        String fromA = fromQ + ":" + fromR;
        String toB = toQ + ":" + toR;
        List<String> matchingNeighbours = new ArrayList<>(findMatchingNeighbours(fromQ, fromR, toQ, toR));

        if (!getNeighbours(fromA).contains(toB))
            return false;

        String neighbourN1 = matchingNeighbours.get(0);
        String neighbourN2 = matchingNeighbours.get(1);

        if ((checkIfTileEmpty(neighbourN1) && checkIfTileEmpty(neighbourN2) && !isBeetle))
            return false;

        int min = Math.min(amountOfTilesInStack(neighbourN1), amountOfTilesInStack(neighbourN2));
        int max = Math.max(amountOfTilesInStack(fromA) - 1, amountOfTilesInStack(toB));

        return min <= max;
    }

    public boolean isFilledLine(int fromQ, int fromR, int toQ, int toR) {
        int biggestDiff = Math.max(Math.abs(fromQ - toQ), Math.abs(fromR - toR));

        if (biggestDiff <= 1)
            return false;

        for (String direction : directions) {
            int[] dir = getSplitCoordinate(direction);
            int qGoal = fromQ + (dir[0] * biggestDiff);
            int rGoal = fromR + (dir[1] * biggestDiff);

            if (qGoal == toQ && rGoal == toR) {
                for (int i = 1; i < biggestDiff; i++)
                    if (checkIfTileEmpty((fromQ + (dir[0] * i)) + ":" + (fromR + (dir[1] * i))))
                        return false;
            }
            break;
        }
        return true;
    }

    public List<String> findMatchingNeighbours(int fromQ, int fromR, int toQ, int toR) {
        String from = fromQ + ":" + fromR;
        String to = toQ + ":" + toR;
        LinkedList<String> neighboursFrom = new LinkedList<>(getNeighbours(from));
        LinkedList<String> neighboursTo = new LinkedList<>(getNeighbours(to));

        neighboursFrom.retainAll(neighboursTo);

        return neighboursFrom;
    }

    public Integer amountOfTilesInStack(String coordinate) {
        if (checkIfTileEmpty(coordinate))
            return 0;
        return board.get(coordinate).size();
    }

    public boolean hasPlayableLocations(APlayer enemyPlayer) {
        HashSet<String> openLocations = new HashSet<>(returnAllEmptyNeighbourSpots());

        return aTilePlayable(openLocations, enemyPlayer);
    }

    public HashSet<String> returnAllEmptyNeighbourSpots() {
        HashSet<String> openLocations = new HashSet<>();

        for (String location : board.keySet())
            for (String neighbourLocation : getNeighbours(location))
                if (checkIfTileEmpty(neighbourLocation))
                    openLocations.add(neighbourLocation);
        return openLocations;
    }

    public boolean aTilePlayable(HashSet<String> openLocations, APlayer enemyPlayer) {
        boolean tilePlayableFound = false;

        for (String location : openLocations) {
            if (!notConnectedToEnemyTile(location, enemyPlayer)) {
                tilePlayableFound = true;
            }
        }

        return tilePlayableFound;
    }

    public int[] getSplitCoordinate(String coordinate) {
        int[] coords = new int[2];

        String[] splitCoord = coordinate.split(":");
        coords[0] = Integer.parseInt(splitCoord[0]);
        coords[1] = Integer.parseInt(splitCoord[1]);

        return coords;
    }

    public List<String> getNeighbours(String coordinate) {
        List<String> neighbours = new ArrayList<>();
        int[] coord = getSplitCoordinate(coordinate);

        for (String direction : directions) {
            int[] dir = getSplitCoordinate(direction);
            neighbours.add((coord[0] + dir[0]) + ":" + (coord[1] + dir[1]));
        }

        return neighbours;
    }

    public String getIdKey(Integer piece) {
        for (Map.Entry<String, Stack<Integer>> value : board.entrySet()) {
            if (value.getValue().contains(piece)) {
                return value.getKey();
            }
        }
        return null;
    }

    public Integer getIdByCoordinate(String coordinate) {
        return board.get(coordinate).peek();
    }

    public HashMap<String, Stack<Integer>> getBoard() {
        return board;
    }
}


