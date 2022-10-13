import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private static void fillBoard(char[][] battleField) {
        final int size = 10;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                battleField[i][j] = '~';
            }
        }
    }

    static void enterCoordinates(char[][] battleField) {
        LinkedHashMap<String, Integer> ships = getShips();

        for (Map.Entry<String, Integer> ship : ships.entrySet()) {
            int cells = ship.getValue();
            String name = ship.getKey();
            String input;
            System.out.println("Enter the coordinates of the " + name + " (" + cells + "):\n");
            do {
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
            } while (!checkInput(input, ship, battleField));
            placeShip(input, ship.getValue(), battleField);
            display(battleField);
        }
    }

    private static LinkedHashMap<String, Integer> getShips() {
        LinkedHashMap<String, Integer> ships = new LinkedHashMap<>();
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
        return ships;
    }

    static private boolean checkInput(String input, Map.Entry<String, Integer> ship, char[][] battleField) {
        if (input == null || ship == null) {
            return false;
        }

        StringTokenizer tokenizer = new StringTokenizer(input);
        String firstCoords = tokenizer.nextToken();
        String secondCoords = tokenizer.nextToken();

        if (!(checkCoords(firstCoords) && checkCoords(secondCoords))) {
            System.out.println("Error! Wrong format of input! Try again!:");
            return false;
        }

        if (!checkLocation(input)) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }

        if (!checkLength(input, ship.getValue())) {
            System.out.println("Error! Wrong length of the " + ship.getKey() + "! Try again:");
            return false;
        }

        if (!checkCross(input, ship.getValue(), battleField)) {
            System.out.println("Error! Ships crossed each other! Try again:");
            return false;
        }

        if (!checkTouch(input, ship.getValue(), battleField)) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return false;
        }

        return true;
    }

    static private boolean checkCoords(String coords) {
        final char rowIndex;
        final int colIndex;
        try {
            rowIndex = coords.charAt(0);
            colIndex = Integer.parseInt(coords.substring((1)));
        } catch (NumberFormatException e) {
            return false;
        }

        return rowIndex >= 'A' && rowIndex <= 'J' && colIndex >= 1 && colIndex <= 10;
    }

    static private boolean checkLocation(String input) {
        int[] indexes = getIndexes(input);

        return (indexes[0] == indexes[2] || indexes[1] == indexes[3]) &&
                ((indexes[0] != indexes[2] || indexes[1] != indexes[3]));
    }

    static private boolean checkLength(String input, Integer cells) {
        int[] indexes = getIndexes(input);

        if (indexes[0] == indexes[2]) {
            if (Math.abs((indexes[1] - indexes[3])) == cells - 1) {
                return true;
            }
        } else {
            if (Math.abs((indexes[0] - indexes[2])) == cells - 1) {
                return true;
            }
        }
        return false;
    }

    static private boolean checkCross(String input, int cells, char[][] battleField) {
        int[] indexes = getIndexes(input);

        int row;
        int col;
        if (indexes[0] == indexes[2]) {
            col = Math.min(indexes[1], indexes[3]);
            row = indexes[0];
            for (int i = col; i < col + cells; i++) {
                if (battleField[row][i] == 'O')
                    return false;
            }
        } else {
            row = Math.min(indexes[0], indexes[2]);
            col = indexes[1];
            for (int i = row; i < row + cells; i++) {
                if (battleField[i][col] == 'O')
                    return false;
            }
        }

        return true;
    }

    static private boolean checkTouch(String input, int cells, char[][] battleField) {
        int[] indexes = getIndexes(input);

        int row;
        int col;
        if (indexes[0] == indexes[2]) {
            col = Math.min(indexes[1], indexes[3]);
            row = indexes[0];
            for (int i = col; i < col + cells; i++) {
                if (!checkField(row, i, battleField))
                    return false;
            }
        } else {
            row = Math.min(indexes[0], indexes[2]);
            col = indexes[1];
            for (int i = row; i < row + cells; i++) {
                if (!checkField(i, col, battleField))
                    return false;
            }
        }
        return true;
    }

    private static int[] getIndexes(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        String firstCoords = tokenizer.nextToken();
        String secondCoords = tokenizer.nextToken();

        final int size = 4;
        int[] indexes = new int[size];

        indexes[0] = firstCoords.charAt(0) - 65;
        indexes[1] = Integer.parseInt(firstCoords.substring((1))) - 1;
        indexes[2] = secondCoords.charAt(0) - 65;
        indexes[3] = Integer.parseInt(secondCoords.substring((1))) - 1;

        return indexes;
    }

    static private void placeShip(String input, int cells, char[][] battleField) {
        int[] indexes = getIndexes(input);

        int row;
        int col;
        if (indexes[0] == indexes[2]) {
            col = Math.min(indexes[1], indexes[3]);
            row = indexes[0];
            for (int i = col; i < col + cells; i++) {
                battleField[row][i] = 'O';
            }
        } else {
            row = Math.min(indexes[0], indexes[2]);
            col = indexes[1];
            for (int i = row; i < row + cells; i++) {
                battleField[i][col] = 'O';
            }
        }
    }

    static private boolean checkField(int row, int col, char[][] battleField) {
        final int size = 8;
        int[][] coords = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        for (int i = 0; i < size; i++) {
            if (row + coords[i][0] >= 0 && row + coords[i][0] <= 9 &&
                    col + coords[i][1] >= 0 && col + coords[i][1] <= 9 &&
                    battleField[row + coords[i][0]][col + coords[i][1]] == 'O') {
                return false;
            }
        }
        return true;
    }

    static void display(char[][] battleField) {
        final int size = 10;
        System.out.print("  ");
        for (int i = 1; i <= size; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            char c = (char) ('A' + i);
            System.out.print(c + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(battleField[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final int size = 10;
        char[][] battleField = new char[size][size];
        fillBoard(battleField);
        enterCoordinates(battleField);
    }
}