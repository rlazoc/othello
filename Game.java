import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    private char[][] board; // 8x8 board

    Game() {
        board = new char[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }
        // Initial location
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
    }

    private boolean isGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, 'B') || isValidMove(i, j, 'W')) {
                    return false;
                }
            }
        }
        return true;
    }

    private void announceWinner() {
        int bCount = 0;
        int wCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'B') {
                    bCount++;
                } else if (board[i][j] == 'W') {
                    wCount++;
                }
            }
        }

        System.out.println("Black: " + bCount + " White: " + wCount);
        if (bCount > wCount) {
            System.out.println("Black wins!");
        } else if (bCount < wCount) {
            System.out.println("White wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidMove(int row, int col, char player) {
        if (row < 0 || row >= 8 || col < 0 || row >= 8 || board[row][col] != '-') {
            return false;
        }
        boolean valid = false;
        char opponent = (player == 'B') ? 'W' : 'B';
        // Directions: N, NE, E, SE, S, SW, W, NW
        int[] checkRow = { -1, -1, 0, 1, 1, 1, 0, -1 };
        int[] checkCol = { 0, 1, 1, 1, 0, -1, -1, -1 };
        for (int dir = 0; dir < 8; dir++) {
            int nextRow = row + checkRow[dir];
            int nextCol = col + checkCol[dir];
            boolean hasOpponentBetween = false;
            while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8 && board[nextRow][nextCol] == opponent) {
                nextRow += checkRow[dir];
                nextCol += checkCol[dir];
                hasOpponentBetween = true;
            }
            if (hasOpponentBetween && nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8
                    && board[nextRow][nextCol] == player) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public void makeMove(int row, int col, char player) {
        if (isValidMove(row, col, player)) {
            board[row][col] = player;
            flipPieces(row, col, player);
        } else {
            System.out.println("Invalid move. Please choose a different location.");
        }
    }

    public void flipPieces(int row, int col, char player) {
        char opponent = (player == 'B') ? 'W' : 'B';
        // Directions: N, NE, E, SE, S, SW, W, NW
        int[] checkRow = { -1, -1, 0, 1, 1, 1, 0, -1 };
        int[] checkCol = { 0, 1, 1, 1, 0, -1, -1, -1 };
        for (int dir = 0; dir < 8; dir++) {
            int nextRow = row + checkRow[dir];
            int nextCol = row + checkCol[dir];
            boolean hasOpponentBetween = false;
            List<int[]> piecesToFlip = new ArrayList<>();
            while (nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8 && board[nextRow][nextCol] == opponent) {
                piecesToFlip.add(new int[] { nextRow, nextCol });
                nextRow += checkRow[dir];
                nextCol += checkCol[dir];
                hasOpponentBetween = true;
            }
            if (hasOpponentBetween && nextRow >= 0 && nextRow < 8 && nextCol >= 0 && nextCol < 8
                    && board[nextRow][nextCol] == player) {
                for (int[] piece : piecesToFlip) {
                    board[piece[0]][piece[1]] = player;
                }
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        char currPlayer = 'B';

        System.out.println("Welcome to Othello!");
        System.out.println("===================");
        System.out.println();

        while (!game.isGameOver()) {
            game.printBoard();

            boolean validMove = false;
            while (!validMove) {
                System.out.println("Player " + currPlayer + ", enter your move (row, column):");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                    if (game.isValidMove(row, col, currPlayer)) {
                        game.makeMove(row, col, currPlayer);
                        validMove = true;
                    } else {
                        System.out.println("Invalid move. Please choose a difference location.");
                    }
                } else {
                    System.out.println("Coordinates outside of the board. Please choose a different location.");
                }
            }
            currPlayer = (currPlayer == 'B') ? 'W' : 'B';
        }

        game.printBoard();
        System.out.println("Game over!");
        game.announceWinner();
        scanner.close();
    }
};