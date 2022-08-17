package battleship;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;


public class User {

    Scanner scanner = new Scanner(System.in);

    public String[] initiateShip(String shipType, int shipSize) {

        System.out.printf(
                "%nEnter the coordinates of the %s (%d cells):%n%n", shipType, shipSize);
        return scanner.nextLine().split(" ");
    }

    public String[] tryAgain() {
        return scanner.nextLine().split(" ");
    }
}


class UserAssist {

    final int unicodeA = 65;

    int[] shipRowRange;
    int[] shipColRange;

    User player = new User();

    public int[][] buildShip(int[][][] allShipsCoords, Ships ship) {

        int[][] shipCoords;

        String shipType = ship.getType();
        int shipSize = ship.getSize();

        String[] coords = player.initiateShip(shipType, shipSize);

        boolean validShip = false;
        do {
            // pre-fabricate ship per user entries
            shipRowRange = rowCoordsToRange(coords);
            shipColRange = colCoordsToRange(coords);
            shipCoords = rangesToCoords(shipRowRange, shipColRange);

            // check ship built is valid
            String cause = GameException.getExceptionCause(
                    shipRowRange, shipColRange, shipSize,
                    shipCoords, allShipsCoords);
            try {
                switch (cause) {
                    case "design": throw new ShipDesignException();
                    case "length": throw new ShipLengthException(shipType);
                    case "proximity": throw new ShipsProximityException();
                    default: validShip = true;
                }
            } catch (ShipDesignException | ShipLengthException | ShipsProximityException e) {
                System.out.println(e.getMessage());
                coords = player.tryAgain();
            }
        } while (!validShip);

        // once ship built return 2d array of coords [[x, y],[x, y],[x, y]]
        return shipCoords;
    }

    public int[][] rangesToCoords(int[] shipRowRange, int[] shipColRange) {

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

    public int[] colCoordsToRange(String[] coords) {

        int[] columnInts = {
                Integer.parseInt(coords[0].substring(1)) - 1,
                Integer.parseInt(coords[1].substring(1)) - 1
        };
        Arrays.sort(columnInts);

        return IntStream.rangeClosed(columnInts[0], columnInts[1]).toArray();
    }

    public int[] rowCoordsToRange(String[] coords) {

        int[] rowCharsAsInt = {
                Character.toUpperCase(coords[0].charAt(0)) - unicodeA,
                Character.toUpperCase(coords[1].charAt(0)) - unicodeA
        };
        Arrays.sort(rowCharsAsInt);

        return IntStream.rangeClosed(rowCharsAsInt[0], rowCharsAsInt[1]).toArray();
    }
}