package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FourthHomework {


    private static class Square {

        int x, y;

        public Square(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    static class SquareWithScore {

        int score;
        Square square;

        SquareWithScore(int score, Square square) {
            this.score = score;
            this.square = square;
        }
    }

    private static class Board {

        List<Square> availableSquares;
        Scanner scan = new Scanner(System.in);
        int[][] board = new int[3][3];

        List<SquareWithScore> rootsChildrenScore = new ArrayList<>();

        public int evaluateBoard() {
            int score = 0;

            //Check all rows
            for (int i = 0; i < 3; ++i) {
                int blank = 0;
                int X = 0;
                int O = 0;
                for (int j = 0; j < 3; ++j) {
                    if (board[i][j] == 0) {
                        blank++;
                    } else if (board[i][j] == 1) {
                        X++;
                    } else {
                        O++;
                    }

                }
                score += changeInScore(X, O);
            }

            //Check all columns
            for (int j = 0; j < 3; ++j) {
                int blank = 0;
                int X = 0;
                int O = 0;
                for (int i = 0; i < 3; ++i) {
                    if (board[i][j] == 0) {
                        blank++;
                    } else if (board[i][j] == 1) {
                        X++;
                    } else {
                        O++;
                    }
                }
                score += changeInScore(X, O);
            }

            int blank = 0;
            int X = 0;
            int O = 0;

            //Check main diagonal
            for (int i = 0, j = 0; i < 3; ++i, ++j) {
                if (board[i][j] == 1) {
                    X++;
                } else if (board[i][j] == 2) {
                    O++;
                } else {
                    blank++;
                }
            }

            score += changeInScore(X, O);

            blank = 0;
            X = 0;
            O = 0;

            //Check second Diagonal
            for (int i = 2, j = 0; i > -1; --i, ++j) {
                if (board[i][j] == 1) {
                    X++;
                } else if (board[i][j] == 2) {
                    O++;
                } else {
                    blank++;
                }
            }

            score += changeInScore(X, O);

            return score;
        }

        private int changeInScore(int X, int O) {
            int change;
            if (X == 3) {
                change = 100;
            } else if (X == 2 && O == 0) {
                change = 10;
            } else if (X == 1 && O == 0) {
                change = 1;
            } else if (O == 3) {
                change = -100;
            } else if (O == 2 && X == 0) {
                change = -10;
            } else if (O == 1 && X == 0) {
                change = -1;
            } else {
                change = 0;
            }
            return change;
        }


        public int alphaBetaMinimax(int alpha, int beta, int depth, int turn) {

            if (beta <= alpha) {
                System.out.println("Pruning at depth = " + depth);
                if (turn == 1) return Integer.MAX_VALUE;
                else return Integer.MIN_VALUE;
            }

            if (isGameOver()) return evaluateBoard();

            List<Square> emptySquares = getEmptySquares();

            if (emptySquares.isEmpty()) return 0;

            if (depth == 0) rootsChildrenScore.clear();

            int maxValue = Integer.MIN_VALUE;
            int minValue = Integer.MAX_VALUE;
            System.out.println(depth);
            for (int i = 0; i < emptySquares.size(); ++i) {
                Square square = emptySquares.get(i);

                int currentScore = 0;

                if (turn == 1) {
                    playTurn(square, PlayerTurn.COMPUTER);
                    currentScore = alphaBetaMinimax(alpha, beta, depth + 1, 2);
                    maxValue = Math.max(maxValue, currentScore);

                    //Set alpha
                    alpha = Math.max(currentScore, alpha);

                    if (depth == 0) {
                        System.out.println("--------" + square.x + square.y + "---------------");
                        rootsChildrenScore.add(new SquareWithScore(currentScore, square));
                        System.out.println("The size is" + rootsChildrenScore.size());
                    }
                } else if (turn == 2) {
                    playTurn(square, PlayerTurn.USER);
                    currentScore = alphaBetaMinimax(alpha, beta, depth + 1, 1);
                    minValue = Math.min(minValue, currentScore);

                    //Set beta
                    beta = Math.min(currentScore, beta);
                }
                //reset board
                board[square.x][square.y] = 0;

                //If a pruning has been done, don't evaluate the rest of the states
                if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
            }
            return turn == 1 ? maxValue : minValue;
        }

        public boolean isGameOver() {
            return (hasComputerWon() || hasUserWon() || getEmptySquares().isEmpty());
        }

        public boolean hasComputerWon() {
            if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
                return true;
            }
            for (int i = 0; i < 3; ++i) {
                if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                        || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasUserWon() {
            if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
                return true;
            }
            for (int i = 0; i < 3; ++i) {
                if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
                        || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
                    return true;
                }
            }

            return false;
        }

        public List<Square> getEmptySquares() {
            availableSquares = new ArrayList<>();
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (board[i][j] == 0) {
                        availableSquares.add(new Square(i, j));
                    }
                }
            }
            return availableSquares;
        }

        public void playTurn(Square square, PlayerTurn player) {
            if (board[square.x][square.y] != 0) {
                System.out.println("Incorrect move. try again!");
                takeHumanInput();
                return;
            }
            board[square.x][square.y] = player.ordinal();   //player = 1 for X, 2 for O
        }

        public Square returnBestMove() {
            int MAX = -100000;
            int best = -1;

            for (int i = 0; i < rootsChildrenScore.size(); ++i) {
                if (MAX < rootsChildrenScore.get(i).score) {
                    MAX = rootsChildrenScore.get(i).score;
                    best = i;
                }
            }

            return rootsChildrenScore.get(best).square;
        }

        void takeHumanInput() {
            System.out.println("Your move: ");
            int x = scan.nextInt();
            int y = scan.nextInt();
            Square square = new Square(x, y);
            playTurn(square, PlayerTurn.USER);
        }

        public void displayBoard() {
            System.out.println();

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (board[i][j] == 2) {
                        System.out.print("O" + " ");
                    } else if(board[i][j] == 1) {
                        System.out.print("X" + " ");
                    } else {
                        System.out.print("-" + " ");
                    }
                }
                System.out.println();

            }
        }
    }

    enum PlayerTurn {
        FECTIVE, COMPUTER, USER;
    }


    public static void main(String[] args) {
        Board board = new Board();
        Random rand = new Random();

        board.displayBoard();
        System.out.println("You are O and computer is X .");
        System.out.println("Throwing a coin - who to go first..");
        int firstToGo = (Math.random() <= 0.5) ? 1 : 2;
        if (firstToGo == 1) {
            System.out.println("Computer goes first!");
            Square p = new Square(rand.nextInt(3), rand.nextInt(3));
            board.playTurn(p, PlayerTurn.COMPUTER);
            board.displayBoard();
        } else {
            System.out.println("You are first!");
        }
        while (!board.isGameOver()) {
            System.out.println("Your move: ");
            Square userMove = new Square(board.scan.nextInt(), board.scan.nextInt());

            board.playTurn(userMove, PlayerTurn.USER);
            board.displayBoard();
            if (board.isGameOver()) break;

            board.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
            for (SquareWithScore squareWithScore : board.rootsChildrenScore)
                System.out.println("Square: " + squareWithScore.square + " Score: " + squareWithScore.score);

            board.playTurn(board.returnBestMove(), PlayerTurn.COMPUTER);
            board.displayBoard();
        }
        if (board.hasComputerWon()) {
            System.out.println("You loose!");
        } else if (board.hasUserWon()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }

    }
}
