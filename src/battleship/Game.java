package battleship;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Game {
    private final Player player1;
    private final Player player2;
    private final Scanner scanner;
    private boolean isWinner;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        scanner = new Scanner(System.in);
        isWinner = false;
    }

    void play() throws IOException {
        System.out.println("Player 1, place your ships on the game field\n");
        placeShips(player1);
        System.out.println("Press Enter and pass the move to another player\n");

        System.in.read();

        System.out.println("Player 2, place your ships on the game field\n");
        placeShips(player2);

        while (true) {
            System.out.println("Press Enter and pass the move to another player\n");

            System.in.read();

            if (!isWinner) {
                Board.printHiddenBoard();
                takeAShot(player1, player2, "player1");
            } else break;

            System.out.println("Press Enter and pass the move to another player\n");

            System.in.read();

            if (!isWinner) {
                Board.printHiddenBoard();
                takeAShot(player2, player1, "player2");
            } else break;
        }
    }

    private void placeShips(Player player) {
        String input;
        boolean properInput;

        player.printBoard();

        for (Ship ship : Ship.values()) {
            properInput = false;
            String name = ship.getName();
            int cells = ship.getCells();
            System.out.println("Enter the coordinates of the " + name + " (" + cells + "):\n");

            do {
                input = scanner.nextLine();
                System.out.println();
                try {
                    properInput = checkInput(input, ship, player);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println();
                }
            } while (!properInput);

            placeShip(input, ship.getCells(), player);
            player.printBoard();
        }
    }

    private void takeAShot(Player player1, Player player2, String name) {
        String input;
        boolean properInput;
        player1.printBoard();
        System.out.println(name + ", it's your turn:\n");

        do {
            properInput = false;
            input = scanner.nextLine();
            System.out.println();
            try {
                properInput = checkCoords(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!properInput);

        markAShot(input, player2);
    }

    private void markAShot(String input, Player player) {
        String[] tokens = retrieveTokens(input);
        int x = tokens[0].charAt(0) - 'A';
        int y = Integer.parseInt(tokens[0].substring(1)) - 1;
        final int numOfShips = Ship.numOfShips();
        if (player.getChar(x, y) == 'O') {
            player.setChar(x, y, 'X');
            if (checkIfSunken(x, y, player)) {
                player.sinkShip();
                if (player.numOfSunkenShips() < numOfShips) {
                    System.out.println("You sank a ship!\n");
                }
                else {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    isWinner = true;
                }
            } else {
                System.out.println("You hit a ship!\n");
            }
        } else if (player.getChar(x, y) == '~') {
            player.setChar(x, y, 'M');
            System.out.println("You missed!\n");
        } else if (player.getChar(x, y) == 'M') {
            System.out.println("You missed!\n");
        } else if (player.getChar(x, y) == 'X') {
            System.out.println("You hit a ship!\n");
        }
    }

    private boolean checkIfSunken(int x, int y, Player player) {
        return player.checkIfSunken(x, y);
    }


    private boolean checkCoords(String input) {
        Pattern pattern = Pattern.compile("[A-J](\\d|10)$");
        boolean result = pattern.matcher(input).matches();
        if (!result) {
            throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:\n");
        }
        return true;
    }

    private boolean checkInput(String input, Ship ship, Player player) throws IllegalArgumentException {
        if (input == null || ship == null) {
            return false;
        }

        if (!checkFormat(input)) {
            throw new IllegalArgumentException("Error! Wrong format of input! Try again!:");
        }

        Coordinates coordinates = retrieveCoordinates(input);

        if (!checkLocation(coordinates)) {
            throw new IllegalArgumentException("Error! Wrong ship location! Try again:");
        }

        if (!checkLength(coordinates, ship.getCells())) {
            throw new IllegalArgumentException("Error! Wrong length of the " + ship.getName() + "! Try again:");
        }

        if (!checkCross(coordinates, ship.getCells(), player)) {
            throw new IllegalArgumentException("Error! Ships crossed each other! Try again:");
        }

        if (!checkTouch(coordinates, ship.getCells(), player)) {
            throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");
        }

        return true;
    }

    private boolean checkFormat(String input) {
        Pattern pattern = Pattern.compile("[A-J](\\d|10)\\s[A-J](\\d|10)$");
        return pattern.matcher(input).matches();
    }

    private boolean checkLocation(Coordinates coordinates) {
        return (coordinates.getX1() == coordinates.getX2() || coordinates.getY1() == coordinates.getY2()) &&
                ((coordinates.getX1() != coordinates.getX2() || coordinates.getY1() != coordinates.getY2()));
    }

    private boolean checkLength(Coordinates coordinates, int cells) {
        if (coordinates.getX1() == coordinates.getX2()) {
            if (Math.abs((coordinates.getY1() - coordinates.getY2())) == cells - 1) {
                return true;
            }
        } else {
            if (Math.abs((coordinates.getX1() - coordinates.getX2())) == cells - 1) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCross(Coordinates coordinates, int cells, Player player) {
        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                if (player.getChar(row, i) == 'O')
                    return false;
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                if (player.getChar(i, col) == 'O')
                    return false;
            }
        }

        return true;
    }

    private boolean checkTouch(Coordinates coordinates, int cells, Player player) {
        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                if (!checkField(row, i, player))
                    return false;
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                if (!checkField(i, col, player))
                    return false;
            }
        }
        return true;
    }

    private Coordinates retrieveCoordinates(String input) {
        String[] tokens = retrieveTokens(input);

        int x1 = tokens[0].charAt(0) - 65;
        int y1 = Integer.parseInt(tokens[0].substring((1))) - 1;
        int x2 = tokens[1].charAt(0) - 65;
        int y2 = Integer.parseInt(tokens[1].substring((1))) - 1;

        return new Coordinates(x1, y1, x2, y2);
    }

    private String[] retrieveTokens(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        final int size = tokenizer.countTokens();
        String[] tokens = new String[size];
        for (int i = 0; i < size; i++) {
            tokens[i] = tokenizer.nextToken();
        }
        return tokens;
    }

    private void placeShip(String input, int cells, Player player) {
        Coordinates coordinates = retrieveCoordinates(input);

        int row;
        int col;
        player.addShip();
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                player.setChar(row, i, 'O');
                String coords = "" + row + i;
                player.addCoordsToLastShip(coords);
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                player.setChar(i, col, 'O');
                String coords = "" + i + col;
                player.addCoordsToLastShip(coords);
            }
        }
    }

    private boolean checkField(int row, int col, Player player) {
        final int size = 8;
        int[][] coords = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        for (int i = 0; i < size; i++) {
            if (row + coords[i][0] >= 0 && row + coords[i][0] <= 9 &&
                    col + coords[i][1] >= 0 && col + coords[i][1] <= 9 &&
                    player.getChar(row + coords[i][0], col + coords[i][1]) == 'O') {
                return false;
            }
        }
        return true;
    }


}
