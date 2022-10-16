package battleship;

import java.util.Arrays;

public class Board {
    public static final int ROWS = 10;
    public static final int COLS = 10;
    private final char[][] board;

    Board() {
        board = new char[ROWS][COLS];
        fillBoard();
    }
    private void fillBoard() {
        for (int i = 0; i < ROWS; i++) {
            Arrays.fill(board[i], '~');
        }
    }

    public char getChar(int row, int col) {
        return board[row][col];
    }

    public void setChar(int row, int col, char c) {
        board[row][col] = c;
    }

    public void printBoard() {
        System.out.print("  ");
        for (int i = 1; i <= ROWS; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < ROWS; i++) {
            char c = (char) ('A' + i);
            System.out.print(c + " ");
            for (int j = 0; j < ROWS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printHiddenBoard() {
        System.out.print("  ");
        for (int i = 1; i <= ROWS; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < ROWS; i++) {
            char c = (char) ('A' + i);
            System.out.print(c + " ");
            for (int j = 0; j < ROWS; j++) {
                System.out.print("~" + " ");
            }
            System.out.println();
        }

        for(int i = 0; i < 21; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
