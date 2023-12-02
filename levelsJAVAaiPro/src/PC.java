import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PC extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private JLabel statusLabel;
    private int[][] board;
    private int currentPlayer;
    private int difficultyLevel;
    private JLabel messageLabel;

    public PC() {
        setTitle("Tic Tac Toe");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the board and set the current player to X
        board = new int[3][3];
        currentPlayer = 1;

        // Create the buttons for the game board
        buttons = new JButton[3][3];
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 48));
                buttons[row][col].addActionListener(this);
                boardPanel.add(buttons[row][col]);
            }
        }

        // Create the status label
        statusLabel = new JLabel("Your turn");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 48));

        // Create the difficulty level combo box
        String[] levels = {"Easy", "Medium", "Difficult"};
        JComboBox<String> levelComboBox = new JComboBox<>(levels);
        levelComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficultyLevel = levelComboBox.getSelectedIndex();
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setBackground(Color.white);
            }
        }

        // Add the components to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.NORTH);
        add(levelComboBox, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        int row = -1, col = -1;

        // Find the row and column of the button that was clicked
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // If the selected button is already occupied, do nothing
        if (board[row][col] != 0) {
            return;
        }

        // Update the board and display the move on the button
        board[row][col] = currentPlayer;
        if (currentPlayer == 1) {
            button.setText("X");
            statusLabel.setText("The computer's turn");
        }
        else {
            button.setText("O");
            statusLabel.setText("Your turn");
        }

        // Check for a winner or tie
        int winner = checkWinner();
        if (winner != 0) {
            endGame(winner);
        } else if (isBoardFull()) {
            endGame(0);
        }

        // Switch to the other player
        currentPlayer = 3 - currentPlayer;

        // If it's the CPU player's turn, make a move
        if (currentPlayer == 2) {
            int[] move = makeMove();
            board[move[0]][move[1]] = currentPlayer;
            buttons[move[0]][move[1]].setText("O");
            statusLabel.setText("Your turn");

            winner = checkWinner();
            if (winner != 0) {
                endGame(winner);
            } else if (isBoardFull()) {
                endGame(0);
            }

            // Switch to the other player
            currentPlayer = 3 - currentPlayer;
        }
    }

    // Checks if the game board is full
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Determines the winner of the game, if any
    private int checkWinner() {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return board[row][0];
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col];
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return 0;
    }

    // Ends the game and displays the winner or tie message
    private void endGame(int winner) {
        if (winner == 0) {
            statusLabel.setText("Game over - it's a draw!");
        } else {
            statusLabel.setText((winner == 1 ? "You" : "Computer") + " win!");
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    // Makes a move for the CPU player using an alpha-beta algorithm
    private int[] makeMove() {
        int[] move = new int[2];
        if (difficultyLevel == 0) {
            move = makeRandomMove();
        } else if (difficultyLevel == 1) {
            move = makeMediumMove();
        } else if (difficultyLevel == 2) {
            int[] result = alphaBeta(board, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            move[0] = result[1] / 3;
            move[1] = result[1] % 3;
        }
        return move;
    }

    // Makes a random move for the CPU player
    private int[] makeRandomMove() {
        int[] move = new int[2];
        while (true) {
            move[0] = (int)(Math.random() * 3);
            move[1] = (int)(Math.random() * 3);
            if (board[move[0]][move[1]] == 0) {
                break;
            }
        }
        return move;
    }

    // Makes a medium-level move for the CPU player
    private int[] makeMediumMove() {
        // Check if the CPU player can win on the next move
        for (int row        = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == 0) {
                    board[row][col] = currentPlayer;
                    if (checkWinner() == currentPlayer) {
                        int[] move = {row, col};
                        board[row][col] = 0;
                        return move;
                    }
                    board[row][col] = 0;
                }
            }
        }

        // Check if the human player can win on the next move and block it
        int opponentPlayer = 3 - currentPlayer;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == 0) {
                    board[row][col] = opponentPlayer;
                    if (checkWinner() == opponentPlayer) {
                        int[] move = {row, col};
                        board[row][col] = 0;
                        return move;
                    }
                    board[row][col] = 0;
                }
            }
        }

        // If no winning or blocking moves are possible, make a random move
        return makeRandomMove();
    }

    // Implements the alpha-beta algorithm to determine the best move for the CPU player
    private int[] alphaBeta(int[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        // Check if the game is over or the maximum depth has been reached
        int winner = checkWinner();
        if (winner != 0 || depth == 0) {
            int score = evaluate(board);
            return new int[] {score, -1};
        }

        // Determine the best move using the alpha-beta algorithm
        int bestScore = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestMove = -1;
        int player = isMaximizingPlayer ? 2 : 1;
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            if (board[row][col] == 0) {
                board[row][col] = player;
                int[] result = alphaBeta(board, depth - 1, alpha, beta, !isMaximizingPlayer);
                int score = result[0];
                if (isMaximizingPlayer) {
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                    alpha = Math.max(alpha, score);
                } else {
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                    beta = Math.min(beta, score);
                }
                board[row][col] = 0;
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return new int[] {bestScore, bestMove};
    }

    // Evaluates the current state of the board for the alpha-beta algorithm
    private int evaluate(int[][] board) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == 2) {
                    return 10;
                } else if (board[row][0] == 1) {
                    return -10;
                }
            }
        }

        // Check columns
        for (int col = 0; col <3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 2) {
                    return 10;
                } else if (board[0][col] == 1) {
                    return -10;
                }
            }
        }
        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 2) {
                return 10;
            } else if (board[0][0] == 1) {
                return -10;
            }
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 2) {
                return 10;
            } else if (board[0][2] == 1) {
                return -10;
            }
        }

        // If no winner is found, return 0
        return 0;
    }

    // Resets the game board to its initial state
    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = 0;
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
        currentPlayer = 1;
        messageLabel.setText("Your turn");
    }

    // Displays a message dialog with the given message
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}


