package battleship;

public class Main {

    public static void main(String[] args) {
        // primary game set up
        final int gameSize = 10;
        final String boardFill = "~";
        final String shipMark = "O";
        final String shipHitMark = "X";
        final String hitMsg = "hit a ship";
        final String shipMissMark = "M";
        final String missMsg = "missed";

        Game myGame = new Game(gameSize, boardFill,
                shipMark, shipHitMark, hitMsg, shipMissMark, missMsg);

        // set up initial game board
        myGame.setUpBoard();

        // place all ships on board
        for (Ships ship : Ships.values()) {
            myGame.placeShip(ship);
        }

        // taking shots
        myGame.placeShot();
    }
}
