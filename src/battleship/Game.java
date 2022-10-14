package battleship;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Game {
    private static Board board;

    public Game() {
        board = new Board();
    }

    void play() {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean properInput;

        for (Ship ship : Ship.values()) {
            properInput = false;
            String name = ship.getName();
            int cells = ship.getCells();
            System.out.println("Enter the coordinates of the " + name + " (" + cells + "):\n");

            do {
                input = scanner.nextLine();
                try {
                    properInput = checkInput(input, ship);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (!properInput);

            placeShip(input, ship.getCells());
            board.printBoard();
        }
        scanner.close();
    }


    static private boolean checkInput(String input, Ship ship) throws IllegalArgumentException {
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

        if (!checkCross(coordinates, ship.getCells())) {
            throw new IllegalArgumentException("Error! Ships crossed each other! Try again:");
        }

        if (!checkTouch(coordinates, ship.getCells())) {
            throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");
        }

        return true;
    }

    private static boolean checkFormat(String input) {
        Pattern pattern = Pattern.compile("[A-J](\\d|10)\\s[A-J](\\d|10)$");
        return pattern.matcher(input).matches();
    }


    static private boolean checkLocation(Coordinates coordinates) {
        return (coordinates.getX1() == coordinates.getX2() || coordinates.getY1() == coordinates.getY2()) &&
                ((coordinates.getX1() != coordinates.getX2() || coordinates.getY1() != coordinates.getY2()));
    }

    static private boolean checkLength(Coordinates coordinates, int cells) {
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

    static private boolean checkCross(Coordinates coordinates, int cells) {
        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                if (board.getChar(row, i) == 'O')
                    return false;
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                if (board.getChar(i, col) == 'O')
                    return false;
            }
        }

        return true;
    }

    static private boolean checkTouch(Coordinates coordinates, int cells) {
        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                if (!checkField(row, i))
                    return false;
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                if (!checkField(i, col))
                    return false;
            }
        }
        return true;
    }

    private static Coordinates retrieveCoordinates(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        String firstCoords = tokenizer.nextToken();
        String secondCoords = tokenizer.nextToken();

        int x1 = firstCoords.charAt(0) - 65;
        int y1 = Integer.parseInt(firstCoords.substring((1))) - 1;
        int x2 = secondCoords.charAt(0) - 65;
        int y2 = Integer.parseInt(secondCoords.substring((1))) - 1;

        return new Coordinates(x1, y1, x2, y2);
    }

    static private void placeShip(String input, int cells) {
        Coordinates coordinates = retrieveCoordinates(input);

        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                board.setChar(row, i, 'O');
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                board.setChar(i, col, 'O');
            }
        }
    }

    static private boolean checkField(int row, int col) {
        final int size = 8;
        int[][] coords = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        for (int i = 0; i < size; i++) {
            if (row + coords[i][0] >= 0 && row + coords[i][0] <= 9 &&
                    col + coords[i][1] >= 0 && col + coords[i][1] <= 9 &&
                    board.getChar(row + coords[i][0], col + coords[i][1]) == 'O') {
                return false;
            }
        }
        return true;
    }


}

