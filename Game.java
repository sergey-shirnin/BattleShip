package battleship;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;


public class Game {

    UserAssist playerAssist = new UserAssist();

    final int gameSize;
    final String boardFill;
    final String shipMark;
    final String shipHitMark;
    final String hitMsg;
    final String shipMissMark;
    final String missMsg;
    final String sankMsg;
    final String gameOverMsg;

    public Game(int gameSize, String boardFill,
                String shipMark, String shipHitMark, String hitMsg,
                String shipMissMark, String missMsg, String sankMsg, String gameOverMsg) {

        this.gameSize = gameSize;
        this.boardFill = boardFill;
        this.shipMark = shipMark;
        this.shipHitMark = shipHitMark;
        this.hitMsg = hitMsg;
        this.shipMissMark = shipMissMark;
        this.missMsg = missMsg;
        this.sankMsg = sankMsg;
        this.gameOverMsg = gameOverMsg;
    }

    static boolean gameOver = false;

    String[] colNames;
    Character[] rowNames;

    String[][] gameProgress;

    boolean startUp = true;
    boolean gameStart = true;

    final String space = " ";

    int numOfShips = Ships.values().length;
    int[][][] allShipsCoords = new int[numOfShips][][];

    int[] shipsHealth = new int[Ships.values().length];
    ArrayList<String> shipsSank = new ArrayList<>();


    public void printBoard(String mode) {

        if (startUp) {
            System.out.print(space.repeat(2));
            startUp = false;
        } else {
            System.out.println();
            System.out.print(space.repeat(2));
        }

        System.out.println(String.join(space, colNames));
        for (int i = 0; i < gameProgress.length; i++) {
            System.out.print(rowNames[i] + space);
            if (mode.equals("showShips")) {
                System.out.println(String.join(space, gameProgress[i]));
            } else if (mode.equals("hideShips")) {
                ArrayList<String> row = new ArrayList<>(Arrays.asList(gameProgress[i]));
                Collections.replaceAll(row, this.shipMark, this.boardFill);
                System.out.println(String.join(space, row));
            }
        }
    }

    public void setUpBoard() {

        this.gameProgress = new String[this.gameSize][this.gameSize];
        this.colNames = IntStream.rangeClosed(1, this.gameSize)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        this.rowNames = IntStream.range('A', 'A' + this.gameSize)
                .mapToObj(num -> (char) num)
                .toArray(Character[]::new);
        for (String[] row : gameProgress) {
            Arrays.fill(row, this.boardFill);
        }
        printBoard("showShips");
    }


    public void placeShip(Ships ship) {

        int[][] shipCoords = playerAssist.buildShip(allShipsCoords, ship);

        // draw on board
        for (int[] xyPair : shipCoords) {
            int row = xyPair[0], col = xyPair[1];
            gameProgress[row][col] = this.shipMark;
        }
        printBoard("showShips");

        // add ship coords to all ships 3d array [[[x, y],[x, y],[x, y]], ..., ..., ... ...]
        allShipsCoords[ship.ordinal()] = shipCoords;

        // add ship health(size) to all ships 1d array [5, 4, 3, ...]
        shipsHealth[ship.ordinal()] = Ships.values()[ship.ordinal()].getSize();

    }

    public void placeShot() {
        if (gameStart) {
            System.out.printf("%nThe game starts!%n");
            printBoard("hideShips");
            System.out.printf("%nTake a shot!%n%n");
            gameStart = false;
        }

        String[] shotOutcomes = new String[] { this.missMsg, this.hitMsg, this.sankMsg };
        String[] shotMarks = new String[] { this.shipMissMark, this.shipHitMark };

        int[] shotCoords = playerAssist.takeShot(gameSize);
        int row = shotCoords[0], col = shotCoords[1];

        int shotResult = 1 & Boolean.hashCode(Arrays.asList(this.shipMark, this.shipHitMark)
                                                    .contains(gameProgress[row][col])) >> 1;

        // draw on board
        gameProgress[row][col] = shotMarks[shotResult];
        printBoard("hideShips");

        // update ships health
        for (int i = 0; i < allShipsCoords.length; i++) {
            for (int j = 0; j < allShipsCoords[i].length; j++) {
                if (Arrays.equals(allShipsCoords[i][j], shotCoords)) {
                    allShipsCoords[i][j] = new int[] {-1, -1};
                    shipsHealth[i]--;
                    break;
                }
            }
        }

        // update sank ships list
        for (int i = 0; i < shipsHealth.length; i++) {
            if (shipsHealth[i] == 0) {
                shipsSank.add(Ships.values()[i].getType());
                shipsHealth[i]--;
                shotResult++;
                break;
            }
        }

        // shot result msg display
        gameOver = shipsSank.size() == Ships.values().length;
        if (!gameOver) {
            System.out.printf("%n%s%n%n", shotOutcomes[shotResult]);
        } else {
            System.out.printf("%n%s%n", this.gameOverMsg);
        }
    }
}
