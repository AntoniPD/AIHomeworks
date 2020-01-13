package com.company;

import java.util.*;

public class SecondHomework {
    private static final int SIZE = 3;
    private static final Board START = new Board(new int[][]{
            {6, 5, 3},
            {2, 4, 8},
            {7, 0, 1},
    });
    private static final Board GOAL = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0},
    });

    public static void findPathLength() {
        Queue<Board> q = new PriorityQueue<>();
        Set<Board> visited = new HashSet<>();
        q.add(START);

        while (!q.isEmpty()) {
            Board current = q.poll();

            if (current.equals(GOAL)) {
                System.out.println("Length is: " + current.pathLength);
                break;
            }
            visited.add(current);

            List<Point> points = getNeighbors(current);
            for (Point p : points) {
                if (p.isValidPoint()) {
                    Board b = new Board(current);
                    b.swap(current.findIndex(0), p);
                    if (!visited.contains(b)) {
                        b.pathLength = current.pathLength + 1;
                        b.heuristicFunction();
                        q.add(b);
                    }
                }
            }
        }
    }

    static List<Point> getNeighbors(Board board) {
        Point current = board.findIndex(0);
        List<Point> neighbors = new ArrayList<>();

        if(new Point(current.x - 1, current.y).isValidPoint()) {
            neighbors.add(new Point(current.x - 1, current.y));
        }

        if(new Point(current.x, current.y - 1).isValidPoint()) {
            neighbors.add(new Point(current.x, current.y - 1));
        }

        if(new Point(current.x + 1, current.y).isValidPoint()) {
            neighbors.add(new Point(current.x + 1, current.y));
        }

        if(new Point(current.x, current.y + 1).isValidPoint()) {
            neighbors.add(new Point(current.x, current.y + 1));
        }

        return neighbors;
    }

    private static class Board implements Comparable<Board> {
        int[][] board;
        int heuristic;
        int pathLength;

        Board(int[][] board) {
            this.board = board;
        }

        Board(Board board) {
            this.board = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    this.board[i][j] = board.board[i][j];
                }
            }
            heuristic = board.heuristic;
            pathLength = board.pathLength;
        }

        void heuristicFunction() {
            int manhattanDistance = 0;
            for (int i = 0; i <= SIZE * SIZE - 1; ++i) {
                Point goal = GOAL.findIndex(i);
                Point current = this.findIndex(i);
                manhattanDistance += Math.abs(goal.x - current.x) + Math.abs(goal.y - current.y);
            }
            this.heuristic = manhattanDistance + pathLength;
        }

        Point findIndex(int element) {
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    if (board[i][j] == element) {
                        return new Point(i, j);
                    }
                }
            }
            return null;
        }

        void swap(Point a, Point b) {
            int tmp = board[a.x][a.y];
            board[a.x][a.y] = board[b.x][b.y];
            board[b.x][b.y] = tmp;
        }

        @Override
        public int compareTo(Board o) {
            return this.heuristic - o.heuristic;
        }

        @Override
        public boolean equals(Object o) {
            Board b = (Board) o;
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    if (this.board[i][j] != b.board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(board);
        }
    }

    private static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isValidPoint() {
            return (x >= 0 && x < SIZE) && (y >= 0 && y < SIZE); }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {
        SecondHomework.findPathLength();
    }
}
