package battleship;


import java.util.Arrays;
import java.util.stream.IntStream;


public class ShipBuildSupport {

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