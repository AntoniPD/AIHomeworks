package com.company;

import java.util.*;


public class FirstHomework {

    private static final int SIZE = 6;
    private static final int[][] BOARD = {
            {1, 1, 0, 1, 1, 1},
            {1, 2, 0, 0, 1, 1},
            {1, 1, 1, 1, 2, 1},
            {1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1},
    };

    private static final Point STARTING_POINT = new Point(0, 0);
    private static final Point FINAL_POINT = new Point(4, 4);
    private static final Point FIRST_TELEPORT = new Point(1, 1);
    private static final Point SECOND_TELEPORT = new Point(4, 2);


    static void bfs() {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> path = new HashMap<>();

        queue.add(STARTING_POINT);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            visited.add(current);

            if (current.equals(FINAL_POINT)) {
                printPath(current, path);
                break;
            }

            if (current.isTeleport()) {
                Point p = teleport(current);
                path.put(p, current);
                queue.add(p);
                removeTeleports();
                continue;
            }

            for (Point nextPoint : getNeighbors(current)) {
                if (!visited.contains(nextPoint)) {
                    path.put(nextPoint, current);
                    visited.add(nextPoint);
                    queue.add(nextPoint);
                }
            }
        }
    }

    static void printPath(Point current, Map<Point, Point> path) {
        System.out.print("(" + current.x + "," + current.y + ")");
        while ((current = path.get(current)) != null) {
            System.out.println();
            System.out.print("(" + current.x + "," + current.y + ")");
        }
    }

    static Point teleport(Point current) {
        if (current.equals(FIRST_TELEPORT)) {
            return SECOND_TELEPORT;
        } else if (current.equals(SECOND_TELEPORT)) {
            return FIRST_TELEPORT;
        }
        return null;
    }

    static void removeTeleports() {
        BOARD[FIRST_TELEPORT.x][FIRST_TELEPORT.y] = 0;
        BOARD[SECOND_TELEPORT.x][SECOND_TELEPORT.y] = 0;
    }

    static List<Point> getNeighbors(Point current) {
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


    public static void main(String[] args) {
        FirstHomework.bfs();
    }

    private static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isValidPoint() {
            return (x >= 0 && x < SIZE) && (y >= 0 && y < SIZE) && (BOARD[x][y] != 0);
        }

        boolean isTeleport() {
            return BOARD[x][y] == 2;
        }



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
}


