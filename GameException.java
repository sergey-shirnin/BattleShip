package battleship;

import java.util.Arrays;
import java.util.Objects;


public class GameException {

    public static String getExceptionCause(int[] shipRowRange,
                                           int[] shipColRange, int shipSize,
                                           int[][] shipCoords, int[][][] allShipsCoords) {

        boolean designException = getDesignException(shipRowRange, shipColRange);
        boolean lengthException = getLengthException(shipCoords, shipSize);
        boolean proximityException = getProximity(shipCoords, allShipsCoords);

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

    public static boolean getProximity(int[][] shipCoords, int[][][] allShipsCoords) {

        boolean proximity = false;

        outerLoop:
        for (int[] coordExisting : Arrays.stream(allShipsCoords)
                                                        .filter(Objects::nonNull)
                                                        .flatMap(Arrays::stream)
                                                        .toArray(int[][]::new)) {
            for (int[] coordNew : shipCoords) {
                int[] test = new int[2];
                for (int i = 0; i < 2; i++) {
                    test[i] = coordExisting[i] - coordNew[i];
                }
                int testSum = Arrays.stream(test).sum();
                for (int i = 0; i < test.length; i++) {
                    if (coordExisting[i] == coordNew[i]) {
                        if (Math.abs(testSum) == 1) {
                            proximity = true;
                            break outerLoop;
                        }
                    }
                }
            }
        }
        return proximity;
    }
}

class ShipDesignException extends Exception {

    public ShipDesignException() {
        super(String.format("%nError! Wrong ship location! Try again:%n"));
    }
}

class ShipLengthException extends Exception {

    public ShipLengthException(String shipType) {
        super(String.format("%nError! Wrong length of the %s! Try again:%n",
                shipType));
    }
}

class ShipsProximityException extends Exception {

    public ShipsProximityException() {
        super(String.format("%nError! You placed it too close to another one. Try again:%n"));
    }
}






