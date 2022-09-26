package battleship;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // primary game set up
        final int gameSize = 10;
        final String boardFill = "~";
        final String shipMark = "O";
        final String shipHitMark = "X";
        final String hitMsg = "You hit a ship! Try again:";
        final String shipMissMark = "M";
        final String missMsg = "You missed. Try again:";
        final String sankMsg = "You sank a ship! Specify a new target:";
        final String gameOverMsg = "You sank the last ship. You won. Congratulations!";

        Game myGame = new Game(gameSize, boardFill, shipMark,
                shipHitMark, hitMsg, shipMissMark, missMsg, sankMsg, gameOverMsg);

        // set up initial game board
        myGame.setUpBoard();

        // place all ships on board
        for (Ships ship : Ships.values()) {
            myGame.placeShip(ship);
        }

        // taking shots till all ships in sank list
        while (!Game.gameOver) {
            myGame.placeShot();
        }
    }
}
