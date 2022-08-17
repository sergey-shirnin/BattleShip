package battleship;

public class Main {

    public static void main(String[] args) throws
                                            ShipDesignException,
                                            ShipLengthException,
                                            ShipsProximityException {
        // primary game set up
        final int gameSize = 10;
        final String boardFill = "~";

        Game myGame = new Game(gameSize, boardFill);

        // set up initial game board
        myGame.setUpBoard();
        myGame.printBoard();

        // place all ships on board
        for (Ships ship : Ships.values()) {
            myGame.placeShip(ship);
            myGame.printBoard();
        }
    }
}
