package battleship;

import java.util.Arrays;
import java.util.stream.IntStream;


public class UserAssist {
    /**
     * UserAssist class is used as midpoint between User and Game >> User<-UserAssist->Game
     * It's only purpose to handle exceptions by bad User input located at GameExceptions
     *
     * UserAssist follows this logic:
     * 1 > take input form User
     * 2 > keep throwing an exception kept in GameExceptions class if entry inappropriate
     * 3 > feed valid entry to Game class
     *
     * UserAssist provides 2 methods:
     * buildShip() - handling exceptions on pre-game stage
     * takeShot() - handling exceptions while game
     *
     * UserAssist has no self messages and only provides those by means of exceptions
     * UserAssist does not handle normal game messages (start game, shot results, etc.)
     */

    final int unicodeA = 65;

    int[] shipRowRange;
    int[] shipColRange;

    User player = new User();

    public int[][] buildShip(int[][][] allShipsCoords, String shipType, int shipSize) {

        int[][] shipCoords;

        String[] coords = player.initiateShipPlace();

        boolean validShip = false;
        do {
            // pre-fabricate ship per user entries
            shipRowRange = ShipBuildSupport.rowCoordsToRange(coords, unicodeA);
            shipColRange = ShipBuildSupport.colCoordsToRange(coords);
            shipCoords = ShipBuildSupport.rangesToCoords(shipRowRange, shipColRange);

            // check ship built is valid
            String cause = GameException.getPlacementExceptionCause(
                    shipRowRange, shipColRange, shipSize, shipCoords, allShipsCoords);
            try {
                switch (cause) {
                    case "design": throw new ShipDesignException();
                    case "length": throw new ShipLengthException(shipType);
                    case "proximity": throw new ShipsProximityException();
                    default: validShip = true;
                }
            } catch (ShipDesignException | ShipLengthException | ShipsProximityException e) {
                System.out.println(e.getMessage());
                coords = player.retryShipPlace();
            }
        } while (!validShip);

        // once ship built return 2d array of coords [[x, y],[x, y],[x, y]]
        return shipCoords;
    }

    public int[] takeShot(int gameSize) {

        int[] shotCoords;
        String coords = player.takeShot();

        boolean validShot = false;
        do {
            shotCoords = new int[] {
                    Character.toUpperCase(coords.charAt(0)) - unicodeA,
                    Integer.parseInt(coords.substring(1)) - 1
            };

            String cause = GameException.getShotExceptionCause(shotCoords, gameSize);
            try {
                switch (cause) {
                    case "beyond": throw new ShotBeyondException();
                    default: validShot = true;
                }
            } catch (ShotBeyondException e) {
                System.out.println(e.getMessage());
                coords = player.retryShot();
            }
        } while (!validShot);

        return shotCoords;
    }
}

class ShipBuildSupport {
    /**
     * UserAssist.buildShip() method support functions
     */

    public static int[][] rangesToCoords(int[] shipRowRange, int[] shipColRange) {

        int i = 0;
        int[][] shipCoords = new int[shipRowRange.length * shipColRange.length][2];
        for (int x : shipRowRange) {
            for (int y : shipColRange) {
                int[] xyPair = new int[] {x, y};
                shipCoords[i] = xyPair;
                i++;
            }
        }
        return shipCoords;
    }

    public static int[] colCoordsToRange(String[] coords) {

        int[] columnInts = {
                Integer.parseInt(coords[0].substring(1)) - 1,
                Integer.parseInt(coords[1].substring(1)) - 1
        };
        Arrays.sort(columnInts);

        return IntStream.rangeClosed(columnInts[0], columnInts[1]).toArray();
    }

    public static int[] rowCoordsToRange(String[] coords, int unicodeA) {

        int[] rowCharsAsInt = {
                Character.toUpperCase(coords[0].charAt(0)) - unicodeA,
                Character.toUpperCase(coords[1].charAt(0)) - unicodeA
        };
        Arrays.sort(rowCharsAsInt);

        return IntStream.rangeClosed(rowCharsAsInt[0], rowCharsAsInt[1]).toArray();
    }
}
