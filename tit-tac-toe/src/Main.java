import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[][] tic_tac_toe_board = new String[3][3];
        String currentPlayer = "X";
        boolean gameOver = false;
        ArrayList<ArrayList<Integer>> filledCells = new ArrayList<>();

        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Board");
        renderBoard(tic_tac_toe_board);

        while (!gameOver) {
            int[] selectedCell = selectCell(currentPlayer);

            updateBoard(tic_tac_toe_board, selectedCell, currentPlayer, filledCells);
            renderBoard(tic_tac_toe_board);
            gameOver = IsGameOver(tic_tac_toe_board);

            if (!gameOver) {
                currentPlayer = changeCurrentPlayer(currentPlayer);
            }
        }

        if(isBoardFull(tic_tac_toe_board)) {
            System.out.println("Board Full!");
            System.out.println("Nobody Won!");
        } else {
            System.out.println("Game Over!");
            System.out.println(currentPlayer + " wins!!");
        }
    }

    public static void renderBoard(String[][] board) {
        String r = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == null) {
                    r += " _";
                } else {
                    r += " " + board[i][j];
                }
            }
            r = r.trim();
            System.out.println(r);
            r = "";
        }
    }

    public static int[] selectCell(String player) {
        Scanner input = new Scanner(System.in);
        int row = 0;
        int col = 0;

        System.out.println("----- Put " + player + " -----");
        System.out.println("Enter row (1,2 or 3):");
        row = input.nextInt() - 1;
        System.out.println("Enter column (1,2 or 3):");
        col = input.nextInt() - 1;

        return new int[]{row, col};
    }

    public static void updateBoard(String[][] board, int[] selectedCell, String currentPlayer, ArrayList<ArrayList<Integer>> filledCells) {
        int row = selectedCell[0];
        int col = selectedCell[1];
        boolean cellSelected = !isEmpty(filledCells, selectedCell);

        while(cellSelected) {
            System.out.println("This cell is already occupied!\nSelect another one!");
            renderBoard(board);
            int[] newSelectedCell = selectCell(currentPlayer);
            row = newSelectedCell[0];
            col = newSelectedCell[1];

            cellSelected = !isEmpty(filledCells, newSelectedCell);
        }

        board[row][col] = currentPlayer;
        filledCells.add(new ArrayList<>(List.of(row, col)));
    }

    public static String changeCurrentPlayer(String prevPlayer) {
        return prevPlayer == "X" ? "O" : "X";
    }

    public static boolean isEmpty(ArrayList<ArrayList<Integer>> filledCells, int[] selectedCell) {
        for(ArrayList<Integer> list : filledCells) {
            if(list.get(0) == selectedCell[0] && list.get(1) == selectedCell[1]) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkRows(String[][] board) {
        String row = "";
        String[] list = row.split("");

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == null) {
                    row += " _";
                } else {
                row += " " + board[i][j];
                }
            }

            list = row.trim().split(" ");

            if(list[0].equals(list[1]) && list[1].equals(list[2]) && !list[2].equals("_")) {
                return true;
            }

            row = "";
        }

        return false;
    }

    public static boolean checkColumns(String[][] board) {
        String col = "";
        String[] list = col.split("");
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[j][i] == null) {
                    col += " _";
                } else {
                    col += " " + board[j][i];
                }
            }

            list = col.trim().split(" ");

            if(list[0].equals(list[1]) && list[1].equals(list[2]) && !list[2].equals("_")) {
                return true;
            }

            col = "";
        }

        return false;
    }

    public static boolean checkDiagonals(String[][] board) {
        String diagonal1 = "";
        String diagonal2 = "";
        String[] listOfDiagonal1 = diagonal1.split("");
        String[] listOfDiagonal2 = diagonal2.split("");

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i == j) {
                    if(board[i][j] == null) {
                        diagonal1 += " _";
                    } else {
                        diagonal1 += " " + board[i][j];
                    }
                }

                if((i == 0  &&  j == 2)  ||  (i == 2  &&  j == 0)  ||  (i == 1  &&  j == 1)) {
                    if(board[i][j] == null) {
                        diagonal2 += " _";
                    } else {
                        diagonal2 += " " + board[i][j];
                    }
                }
            }
        }

        listOfDiagonal1 = diagonal1.trim().split(" ");
        listOfDiagonal2 = diagonal2.trim().split(" ");

        if(listOfDiagonal1[0].equals(listOfDiagonal1[1]) && listOfDiagonal1[1].equals(listOfDiagonal1[2]) && !listOfDiagonal1[2].equals("_")) {
            return true;
        }

        if(listOfDiagonal2[0].equals(listOfDiagonal2[1]) && listOfDiagonal2[1].equals(listOfDiagonal2[2]) && !listOfDiagonal2[2].equals("_")) {
            return true;
        }

        return false;
    }

    public static boolean checkIfSomebodyWon(String[][] board) {
        return checkRows(board)  ||  checkColumns(board) ||  checkDiagonals(board);
    }

    public static boolean isBoardFull(String[][] board) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j] == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean IsGameOver(String[][] board) {
        return checkIfSomebodyWon(board) || isBoardFull(board);
    }
}