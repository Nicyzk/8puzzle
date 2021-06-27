/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {

    private int[][] tiles;
    private int width;
    private int blankRow;
    private int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Tiles cannot be null.");
        width = tiles.length;
        this.tiles = new int[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(width);
        s.append('\n');
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append('\n');
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return width;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != (i * width + j + 1)) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) { // For any num, there is a correct row & col
                if (tiles[i][j] == 0) {
                    continue;
                }
                sum += steps(i, j, tiles[i][j]);
            }
        }
        return sum;
    }

    private int row(int num) {
        return (num - 1) / width;
    }

    private int col(int num) {
        return (num - 1) % width;
    }

    private int steps(int row, int col, int num) {
        int yDiff = Math.abs(row(num) - row);
        int xDiff = Math.abs(col(num) - col);
        return yDiff + xDiff;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() != 0) return false;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (tiles.length != that.tiles.length) return false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    private void swap(Board neighbour, int row1, int col1, int row2, int col2) {
        int temp = neighbour.tiles[row1][col1];
        neighbour.tiles[row1][col1] = neighbour.tiles[row2][col2];
        neighbour.tiles[row2][col2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbours = new LinkedList<>();
        if (blankRow > 0) {
            Board top = new Board(tiles); // Blank row value is not changed!!!!
            swap(top, blankRow, blankCol, blankRow - 1, blankCol);
            top.blankRow = blankRow - 1;
            neighbours.add(top);
        }
        if (blankRow < width - 1) {
            Board bottom = new Board(tiles);
            swap(bottom, blankRow, blankCol, blankRow + 1, blankCol);
            bottom.blankRow = blankRow + 1;
            neighbours.add(bottom);
        }
        if (blankCol > 0) {
            Board left = new Board(tiles);
            swap(left, blankRow, blankCol, blankRow, blankCol - 1);
            left.blankCol = blankCol - 1;
            neighbours.add(left);
        }
        if (blankCol < width - 1) {
            Board right = new Board(tiles);
            swap(right, blankRow, blankCol, blankRow, blankCol + 1);
            right.blankCol = blankCol + 1;
            neighbours.add(right);
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);
        if (blankRow != 0) {
            swap(twin, 0, 0, 0, 1);
        }
        else {
            swap(twin, 1, 0, 1, 1);
        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Tested: toString(), twin(), neighbors(), hamming(), isGoal(), manhattan(), equals()

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.print(initial.toString());

        // Board copy = initial.twin().twin(); DONE.
        // StdOut.println(initial.equals(copy));
        // StdOut.println(initial.manhattan()); DONE.
        // StdOut.print(initial.isGoal()); DONE.
        // Board twin = initial.twin(); DONE.
        // Iterable<Board> neighbors = initial.neighbors();
        // for (Board neighbor : neighbors) {
        //     StdOut.print(neighbor.toString());
        // }
        // StdOut.print(twin.toString()); DONE.
    }

}
