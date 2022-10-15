package battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Game {
    private final Board initialBoard;
    private final Board battleBoard;
    private final Scanner scanner;
    private final ArrayList<HashSet<String>> coordsOfShips;
    private int sunkShips;

    public Game() {
        initialBoard = new Board();
        battleBoard = new Board();
        scanner = new Scanner(System.in);
        coordsOfShips = new ArrayList<>();
        sunkShips = 0;
    }

    void play() {
        placeShips();
        System.out.println("The game starts!\n");
        battleBoard.printBoard();
        takeAShot();
    }

     private void placeShips() {
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
            initialBoard.printBoard();
        }
    }

     private void takeAShot() {
        String input;
        boolean properInput;
        final int numOfShips = Ship.numOfShips();
        System.out.println(("Take a shot!\n"));
        while(sunkShips < numOfShips) {
            do {
                properInput = false;
                input = scanner.nextLine();
                try {
                    properInput = checkCoords(input);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (!properInput);

            markAShot(input);
        }

        System.out.println("You sank the last ship. You won. Congratulations!");

    }

    private  void markAShot(String input) {
        String[] tokens = retrieveTokens(input);
        int x = tokens[0].charAt(0) - 'A';
        int y = Integer.parseInt(tokens[0].substring(1)) - 1;
        final int numOfShips = Ship.numOfShips();
        if (initialBoard.getChar(x, y) == 'O') {
            battleBoard.setChar(x, y, 'X');
            battleBoard.printBoard();
            if(checkIfSunk(x, y)) {
                sunkShips++;
                if(sunkShips < numOfShips) {
                    System.out.println("You sank a ship! Specify a new target:\n");
                }
            }
            else {
                System.out.println("You hit a ship! Try again:\n");
            }
        } else if (initialBoard.getChar(x, y) == '~'){
            battleBoard.setChar(x, y, 'M');
            battleBoard.printBoard();
            System.out.println("You missed! Try again:\n");
        }
    }

    private boolean checkIfSunk(int x, int y) {
        String coords = "" + x + y;
        for(HashSet<String> set: coordsOfShips) {
            if (set.contains(coords)) {
                set.remove(coords);
                if(set.isEmpty()) {
                    coordsOfShips.remove(set);
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    private  boolean checkCoords(String input) {
        Pattern pattern = Pattern.compile("[A-J](\\d|10)$");
        boolean result = pattern.matcher(input).matches();
        if (!result) {
            throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:\n");
        }
        return true;
    }

     private boolean checkInput(String input, Ship ship) throws IllegalArgumentException {
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

    private  boolean checkFormat(String input) {
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

     private boolean checkCross(Coordinates coordinates, int cells) {
        int row;
        int col;
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                if (initialBoard.getChar(row, i) == 'O')
                    return false;
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                if (initialBoard.getChar(i, col) == 'O')
                    return false;
            }
        }

        return true;
    }

     private boolean checkTouch(Coordinates coordinates, int cells) {
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
        for(int i = 0; i < size; i++) {
            tokens[i] = tokenizer.nextToken();
        }
        return tokens;
    }

     private void placeShip(String input, int cells) {
        Coordinates coordinates = retrieveCoordinates(input);

        int row;
        int col;
        coordsOfShips.add(new HashSet<>());
        int size = coordsOfShips.size();
        if (coordinates.getX1() == coordinates.getX2()) {
            col = Math.min(coordinates.getY1(), coordinates.getY2());
            row = coordinates.getX1();
            for (int i = col; i < col + cells; i++) {
                initialBoard.setChar(row, i, 'O');
                String coords = "" + row + i;
                coordsOfShips.get(size - 1).add(coords);
            }
        } else {
            row = Math.min(coordinates.getX1(), coordinates.getX2());
            col = coordinates.getY1();
            for (int i = row; i < row + cells; i++) {
                initialBoard.setChar(i, col, 'O');
                String coords = "" + i + col;
                coordsOfShips.get(size - 1).add(coords);
            }
        }
    }

     private boolean checkField(int row, int col) {
        final int size = 8;
        int[][] coords = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        for (int i = 0; i < size; i++) {
            if (row + coords[i][0] >= 0 && row + coords[i][0] <= 9 &&
                    col + coords[i][1] >= 0 && col + coords[i][1] <= 9 &&
                    initialBoard.getChar(row + coords[i][0], col + coords[i][1]) == 'O') {
                return false;
            }
        }
        return true;
    }


}
