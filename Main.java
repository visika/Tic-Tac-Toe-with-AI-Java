package tictactoe;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    // private static int fc = 0;

    public static void main(String[] args) {
        boolean appRunning = true;
        boolean inGame = false;
        Scanner scanner = new Scanner(System.in);
        while(appRunning) {
            System.out.print("Input command: ");
            String command = scanner.nextLine();
            String mode = "";
            String player1 = "";
            String player2 = "";
            if (command.split(" ").length == 1) mode = command.split(" ")[0];
            if (command.split(" ").length == 3) {
                mode = command.split(" ")[0];
                player1 = command.split(" ")[1];
                player2 = command.split(" ")[2];
            }
            // analyze verb
            boolean badInput = false;
            switch (mode) {
                case "start":
                    inGame = true;
                    for (String player : new String[]{player1, player2}) {
                        // analyze players
                        switch (player) {
                            case "easy": case "medium": case "hard": case "user":
                                inGame = true;
                                break;
                            default:
                                badInput = true;
                                inGame = false;
                        }
                    }
                    break;
                case "exit":
                    appRunning = false;
                    badInput = false;
                    break;
                default:
                    badInput = true;
                    inGame = false;
            }
            if (badInput) {
                System.out.println("Bad parameters!");
                inGame = false;
            }

            // create matrix representation of the game board
            char[][] board = new char[3][3];
            for (char[] row : board) {
                Arrays.fill(row, ' ');
            }
            // print the board at the present time
            if (inGame) display(board);

            while (inGame) {
                switch (player1) {
                    case "user":
                        playerMove(board, 1);
                        break;
                    case "easy":
                        easyMove(board, 1);
                        break;
                    case "medium":
                        mediumMove(board, 1);
                        break;
                    case "hard":
                        hardMove(board, 1);
                        break;
                }
                display(board);
                inGame = gameState(board);
                if (!inGame) break;
                switch (player2) {
                    case "user":
                        playerMove(board, 2);
                        break;
                    case "easy":
                        easyMove(board, 2);
                        break;
                    case "medium":
                        mediumMove(board, 2);
                        break;
                    case "hard":
                        hardMove(board, 2);
                        break;
                }
                display(board);
                inGame = gameState(board);
            }
        }

    }

    private static void playerMove(char[][] board, int player) {
        Scanner scanner = new Scanner(System.in);
        // player's move
        boolean validMove = false;
        int move1, move2;
        int index1, index2;
        while (!validMove) {
            System.out.print("Enter the coordinates: ");
            if (scanner.hasNextInt()) {
                move1 = scanner.nextInt();
            } else {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            if (scanner.hasNextInt()) {
                move2 = scanner.nextInt();
            } else {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            index1 = 3 - move2;
            index2 = move1 - 1;
            if (index1 < 0 || index1 > 2 || index2 < 0 || index2 > 2) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (board[index1][index2] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            validMove = true;

            char symbol = 'X';

            switch (player) {
                case 1:
                    symbol = 'X';
                    break;
                case 2:
                    symbol = 'O';
                    break;
            }

            board[index1][index2] = symbol;
        }
    }

    private static void display(char[][] stato) {
        System.out.println("---------");
        System.out.println("| " + stato[0][0] + ' ' + stato[0][1] + ' ' + stato[0][2] + " |");
        System.out.println("| " + stato[1][0] + ' ' + stato[1][1] + ' ' + stato[1][2] + " |");
        System.out.println("| " + stato[2][0] + ' ' + stato[2][1] + ' ' + stato[2][2] + " |");
        System.out.println("---------");

    }

    private static void easyMove(char[][] board, int player) {
        System.out.println("Making move level \"easy\"");
        Random random = new Random();
        int choice;
        do {
            choice = random.nextInt(9);
        } while (board[choice/3][choice%3] != ' ');

        char symbol = 'O';

        switch (player) {
            case 1:
                symbol = 'X';
                break;
            case 2:
                symbol = 'O';
                break;
        }

        board[choice/3][choice%3] = symbol;
    }

    private static void mediumMove(char[][] board, int player) {
        System.out.println("Making move level \"medium\"");
        int choice;
        int [] coordinate = new int[2];
        boolean choiceMade = false;

        char symbol = 'O';
        char enemySimbol = 'X';
        switch (player) {
            case 1:
                symbol = 'X';
                enemySimbol = 'O';
                break;
            case 2:
                symbol = 'O';
                enemySimbol = 'X';
                break;
        }

        // la prima cosa da fare è vedere se si sta vincendo e fare la mossa decisiva nel caso
        // osserviamo il campo di gioco e vediamo se ci sono delle configurazioni interessanti

        int[] bestMove = seekWin(board, symbol);
        if (bestMove[0] != -1) {
            choiceMade = true;
            coordinate = bestMove;
        }

        // se questa è la scelta possiamo fermarci qui
        // se non abbiamo la mossa vincente, vediamo se possiamo impedire all'avversario di vincere

        if (!choiceMade) {
            bestMove = seekWin(board, enemySimbol);
            if (bestMove[0] != -1) {
                choiceMade = true;
                coordinate = bestMove;
            }
        }

        // se ancora non abbiamo una mossa pronta eseguiamo una mossa a caso

        if (!choiceMade) {
            Random random = new Random();

            do {
                choice = random.nextInt(9);
            } while (board[choice / 3][choice % 3] != ' ');

            board[choice / 3][choice % 3] = symbol;
        } else {
            board[coordinate[0]][coordinate[1]] = symbol;
        }
    }

    private static void hardMove(char[][] board, int player) {
        System.out.println("Making move level \"hard\"");

        char symbol = ' ';
        if (player == 1) {
            symbol = 'X';
        } else if (player == 2) {
            symbol = 'O';
        }
        // fc = 0;
        Move bestMove = minimax(board, player, player);
        // System.out.println("index: " + bestMove.index[0] + " " + bestMove.index[1]);
        // System.out.println("function calls: " + fc);
        board[bestMove.index[0]][bestMove.index[1]] = symbol;
    }

    // ogni volta che chiamo minimax il ruolo di giocatore ed avversario si inverte,
    // devo fare in modo di sapere chi è stato il primo a chimarlo
    private static Move minimax(char[][] board, int callingPlayer, int currentPlayer) {
        // fc++;
        // teniamo sempre chiaro chi è che sta cercando di vincere
        char enemySymbol = ' ';
        char callingSymbol = ' ';
        if (callingPlayer == 1) {
            callingSymbol = 'X';
            enemySymbol = 'O';
        } else if (callingPlayer == 2) {
            callingSymbol = 'O';
            enemySymbol = 'X';
        }

        char symbol = ' ';
        int enemyNumber = 0;
        if (currentPlayer == 1) {
            symbol = 'X';
            enemyNumber = 2;
        } else if (currentPlayer == 2) {
            symbol = 'O';
            enemyNumber = 1;
        }

        // find available spots
        int[][] availableSpots = emptyIndexes(board);

        if (winning(board, enemySymbol)) {
            return new Move(-10);
        } else if (winning(board, callingSymbol)) {
            return new Move(10);
        } else if (!areThereEmptyIndexes(board)) {
            return new Move(0);
        }

        // making the list of possible moves
        // moves.add(thingToAdd) appends to the list
        // moves.get(index) to get an element from the list
        List<Move> moves = new ArrayList<>();
        // preparing a temporary state to guess the next moves
        /*
        char[][] newState = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newState[i][j] = stato[i][j];
            }
        }
        */

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (availableSpots[i][j] == 1) {
                    // let's make a possible move
                    Move move = new Move();
                    move.index = new int[]{i, j};
                    board[i][j] = symbol;
                    Move result = minimax(board, callingPlayer, enemyNumber);
                    // save the score for the minimax
                    move.score = result.score;
                    // then revert the occupied place back to empty, so next guesses can go on
                    board[i][j] = ' ';
                    moves.add(move);
                }
            }
        }

        // when the moves loop has ended, choose the move with the highest score
        int bestMove = 0;

        if (currentPlayer == callingPlayer) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }

        // minimax returns the best move to the latest function caller
        return moves.get(bestMove);
    }

    private static int[][] emptyIndexes(char[][] stato) {
        int[][] empties = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (stato[i][j] == ' ') {
                    empties[i][j] = 1;
                } else {
                    empties[i][j] = 0;
                }
            }
        }
        return empties;
    }

    private static boolean areThereEmptyIndexes(char[][] stato) {
        boolean empties = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (stato[i][j] == ' ') {
                    empties = true;
                }
            }
        }
        return empties;
    }

    private static boolean gameState(char[][] board) {
        // analizzare lo stato di gioco
        boolean xWins = false;
        boolean oWins = false;
        boolean impossible = false;
        boolean draw = false;
        boolean inGame = true;
        // vittoria per colonne
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                // System.out.println(stato[0][i] + " wins");
                if (board[0][i] == 'X') {
                    xWins = true;
                } else if (board[0][i] == 'O') {
                    oWins = true;
                }
            }
        }
        // vittoria per righe
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                //System.out.println(stato[0][i] + " wins");
                if (board[i][0] == 'X') {
                    xWins = true;
                } else if (board[i][0] == 'O'){
                    oWins = true;
                }
            }
        }
        // vittoria per diagonali
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[2][0] == board[1][1] && board[1][1] == board[0][2])) {
            if (board[1][1] == 'X') {
                xWins = true;
            } else if (board[1][1] == 'O') {
                oWins = true;
            }
        }
        // patta e impossibile
        int xCount = 0;
        int oCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') {
                    xCount++;
                } else if (board[i][j] == 'O') {
                    oCount++;
                }
            }
        }
        if (((xCount == 4 && oCount == 5) || (xCount == 5 && oCount == 4)) && (!xWins && !oWins)) {
            // System.out.println("Draw");
            draw = true;
        } else if (xCount >= oCount + 2 || oCount >= xCount + 2) {
            // System.out.println("Impossible");
            impossible = true;
        }
        if (xWins && oWins) {
            impossible = true;
        } else if (xWins) {
            System.out.println("X wins");
        } else if (oWins) {
            System.out.println("O wins");
        }
        if (impossible) {
            System.out.println("Impossible");
        } else if (draw) {
            System.out.println("Draw");
        }
        //if (!impossible && !oWins && !xWins && !draw) {
            // System.out.println("Game not finished");
        //}

        if (xWins || oWins || draw || impossible) inGame = false;

        return inGame;
    }

    // possible winning combinations
    private static boolean winning(char[][] board, char player) {
        return (board[0][0] == player && board[0][1] == player && board[0][2] == player) ||
                (board[1][0] == player && board[1][1] == player && board[1][2] == player) ||
                (board[2][0] == player && board[2][1] == player && board[2][2] == player) ||
                (board[0][0] == player && board[1][0] == player && board[2][0] == player) ||
                (board[0][1] == player && board[1][1] == player && board[2][1] == player) ||
                (board[0][2] == player && board[1][2] == player && board[2][2] == player) ||
                (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static int[] seekWin(char[][] board, char symbol) {
        int row = -1;
        int column = -1;
        // int [] coordinate = {riga, colonna};

        // vediamo condizioni per diagonali
        if ((board[0][0] == symbol && board[2][2] == symbol && board[1][1] == ' ') || (board[2][0] == symbol && board[0][2] == symbol && board[1][1] == ' ')) {
            row = 1;
            column = 1;
        } else if (board[1][1] == symbol) {
            if (board[0][0] == symbol && board[2][2] == ' ') {
                row = 2;
                column = 2;
            } else if (board[2][2] == symbol && board[0][0] == ' ') {
                row = 0;
                column = 0;
            } else if (board[0][2] == symbol && board[2][0] == ' ') {
                row = 2;
                column = 0;
            } else if (board[2][0] == symbol && board[0][2] == ' ') {
                row = 0;
                column = 2;
            }
        }

            for (int i = 0; i < 3; i++) {
                if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == ' ') {
                    // vediamo condizioni per riga
                    row = i;
                    column = 2;
                } else if (board[i][1] == symbol && board[i][2] == symbol && board[i][0] == ' ') {
                    row = i;
                    column = 0;
                } else if (board[i][0] == symbol && board[i][2] == symbol && board[i][1] == ' ') {
                    row = i;
                    column = 1;
                } else if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == ' ') {
                    // vediamo condizioni per colonna
                    row = 2;
                    column = i;
                } else if (board[1][i] == symbol && board[2][i] == symbol && board[0][i] == ' ') {
                    row = 0;
                    column = i;
                } else if (board[0][i] == symbol && board[2][i] == symbol && board[1][i] == ' ') {
                    row = 1;
                    column = i;
                }
            }

        return new int[] {row, column};
    }

}

class Move {
    int[] index;
    int score;

    Move() {

    }

    Move(int s) {
        score = s;
    }
}

/*
// scrivere a mano lo stato attuale
System.out.print("Enter cells: ");
String input = scanner.nextLine();
*/

/*
// tradurre lo stato di gioco in matrice
char[][] stato = new char[3][3];
int count = 0;
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        stato[i][j] = input.charAt(++count);
    }
}
*/

/*
System.out.println("---------");
System.out.println("| " + input.charAt(1) + ' ' + input.charAt(2) + ' ' + input.charAt(3) + " |");
System.out.println("| " + input.charAt(4) + ' ' + input.charAt(5) + ' ' + input.charAt(6) + " |");
System.out.println("| " + input.charAt(7) + ' ' + input.charAt(8) + ' ' + input.charAt(9) + " |");
System.out.println("---------");
*/