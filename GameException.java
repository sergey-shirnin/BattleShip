package battleship;


import java.util.Arrays;
import java.util.Objects;


public class GameException {

    public static String getPlacementExceptionCause(int[] shipRowRange,
                                                    int[] shipColRange, int shipSize,
                                                    int[][] shipCoords, int[][][] allShipsCoords) {

        boolean designException = getDesignException(shipRowRange, shipColRange);
        boolean lengthException = getLengthException(shipCoords, shipSize);
        boolean proximityException = getProximityException(shipCoords, allShipsCoords);

        return designException ? "design"
                : lengthException ? "length"
                : proximityException ? "proximity"
                : "SWITCH DEFAULT -> VALID ENTRY";
    }

    public static boolean getDesignException(int[] shipRowRange, int[] shipColRange) {
        return shipRowRange.length > 1 && shipColRange.length > 1;
    }

    public static boolean getLengthException(int[][] shipCoords, int shipSize) {
        return shipCoords.length != shipSize;
    }

    public static boolean getProximityException(int[][] shipCoords, int[][][] allShipsCoords) {

        boolean proximity = false;

        outerLoop:
        for (int[] coordExisting : Arrays.stream(allShipsCoords)
                                                    .filter(Objects::nonNull)
                                                    .flatMap(Arrays::stream)
                                                    .toArray(int[][]::new)) {
            for (int[] coordNew : shipCoords) {
                for (int i = 0; i < 2; i++) {
                    if (proximity) break outerLoop;
                    proximity = coordExisting[i ^ 0] == coordNew[i ^ 0] &&
                            Math.abs(coordExisting[i ^ 1] - coordNew[i ^ 1]) == 1;
                }
            }
        }
        return proximity;
    }

    public static String getShotExceptionCause(int[] shotCoords, int gameSize) {

        boolean shotBeyondException = getBeyondException(shotCoords, gameSize);

        return shotBeyondException ? "beyond"
                : "SWITCH DEFAULT -> VALID ENTRY";
    }

    public static boolean getBeyondException(int[] shotCoords, int gameSize) {

        boolean beyond = false;

        for (int xy : shotCoords) {
            if (beyond) break;
            beyond = Math.max(0, xy) != Math.min(xy, gameSize - 1);
        }
        return beyond;
    }
}


class ShipDesignException extends Exception {

    public ShipDesignException() {
        super(String.format("" +
                "%nError! Wrong ship location! Try again:%n"));
    }
}

class ShipLengthException extends Exception {

    public ShipLengthException(String shipType) {
        super(String.format("" +
                        "%nError! Wrong length of the %s! Try again:%n", shipType));
    }
}

class ShipsProximityException extends Exception {

    public ShipsProximityException() {
        super(String.format("" +
                "%nError! You placed it too close to another one. Try again:%n"));
    }
}

class ShotBeyondException extends Exception {

    public ShotBeyondException() {
        super(String.format("" +
                "%nError! You entered the wrong coordinates! Try again:%n"));
    }
}
