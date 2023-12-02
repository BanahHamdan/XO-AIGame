import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class humans extends JFrame implements ActionListener {
    // Constants
    private static final int BOARD_SIZE = 3;
    private static final char EMPTY = ' ';
    private static final char PLAYER_ONE = 'X';
    private static final char PLAYER_TWO = 'O';

    // GUI components
    private JButton[][] boardButtons;
    private JLabel messageLabel;
    private JButton newGameButton;

    // Game state
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;

    // Constructor
    public humans() {
        // Set window properties
//        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");


        // Initialize components
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardButtons[row][col] = new JButton(" ");
                boardButtons[row][col].setBackground(Color.white);
                boardButtons[row][col].setFont(new Font("Arial", Font.PLAIN, 48));
                boardButtons[row][col].addActionListener(this);
                boardPanel.add(boardButtons[row][col]);
            }
        }
        messageLabel = new JLabel("Player X's turn");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 30));
//        newGameButton = new JButton("New Game");
//        newGameButton.setFont(new Font("Arial", Font.PLAIN, 30));
//        newGameButton.addActionListener(this);

        // Add components to window
        add(boardPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.NORTH);
//        add(newGameButton, BorderLayout.SOUTH);

        // Initialize game state
        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = EMPTY;
            }
        }
        currentPlayer = PLAYER_ONE;
        gameOver = false;

        // Show window
        pack();
        setVisible(true);
        setSize(450, 450);
        setLocationRelativeTo(null);
    }

    // ActionListener implementation
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
//        if (e.getSource() == newGameButton) {
//            // Start a new game
//            for (int row = 0; row < BOARD_SIZE; row++) {
//                for (int col = 0; col < BOARD_SIZE; col++) {
//                    board[row][col] = EMPTY;
//                    boardButtons[row][col].setText(" ");
//                }
//            }
//            currentPlayer = PLAYER_ONE;
//            messageLabel.setText("Player X's turn");
//            gameOver = false;
//        }
        if (e.getSource() != newGameButton) {
// Handle board button clicks
            if (gameOver) {
                return;
            }
            JButton clickedButton = (JButton) e.getSource();
            int row = -1;
            int col = -1;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (boardButtons[i][j] == clickedButton) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }
            if (row == -1 || col == -1 || board[row][col] != EMPTY) {
                return;
            }
            board[row][col] = currentPlayer;
            clickedButton.setText(String.valueOf(currentPlayer));
            if (checkForWin(row, col)) {
                messageLabel.setText("Player " + currentPlayer + " wins!");
                gameOver = true;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boardButtons[i][j].setEnabled(false);
                    }
                }
            }
            else if (checkForDraw()) {
                messageLabel.setText("Game over - it's a draw!");
                gameOver = true;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boardButtons[i][j].setEnabled(false);
                    }
                }
            }
            else {
                currentPlayer = (currentPlayer == PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
                messageLabel.setText("Player " + currentPlayer + "'s turn");
            }
        }
    }

    // Utility methods
    private boolean checkForWin(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
            return true;
        }
        // Check column
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
            return true;
        }
        // Check diagonal
        if (row == col && board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (row + col == BOARD_SIZE - 1 && board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    private boolean checkForDraw() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}