/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {
    private boolean solvable;
    private int leastMoves;
    private LinkedList<Board> solution = new LinkedList<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument cannot be null");
        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, null, 0));

        MinPQ<Node> twinMinPQ = new MinPQ<>();
        twinMinPQ.insert(new Node(initial.twin(), null, 0));

        while (true) {
            Node min = minPQ.delMin();
            Node twinMin = twinMinPQ.delMin();

            if (min.board.isGoal()) {
                leastMoves = min.moves();
                solvable = true;
                Node current = min;
                while (current != null) {
                    solution.addFirst(current.board());
                    current = current.parent();
                }
                break;
            }

            if (twinMin.board.isGoal()) {
                leastMoves = -1;
                solvable = false;
                break;
            }

            for (Board n : min.board.neighbors()) {
                if (min.parent() == null) {
                    minPQ.insert(new Node(n, min, min.moves() + 1));
                }
                else if (!min.parent().board
                        .equals(n)) { // Ensure neighbour is not same as board's parent.
                    minPQ.insert(new Node(n, min, min.moves() + 1));
                }
            }

            for (Board n : twinMin.board.neighbors()) {
                if (twinMin.parent() == null) {
                    twinMinPQ.insert(new Node(n, twinMin, twinMin.moves() + 1));
                }
                else if (!twinMin.parent().board
                        .equals(n)) { // Ensure neighbour is not same as board's parent.
                    twinMinPQ.insert(new Node(n, twinMin, twinMin.moves() + 1));
                }
            }
        }
    }

    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node parent;
        private final int moves;
        private final int priority;

        private Node(Board board, Node parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(Node n) {
            return priority() - n.priority();
        }

        public int priority() {
            return priority;
        }

        public Node parent() {
            return parent;
        }

        public int moves() {
            return moves;
        }

        public Board board() {
            return board;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return leastMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solution;
        }
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}


/* Priority: Manhattan dist + number of moves alr performed.
 * 1: Dequeue the first item in PQ. Check if isGoal().
 * 2: If not goal:
 *    Calculate neighbours and insert into PQ and rank according to priority.
 *    Impt! Each neighbour should have a link back to parent.
 *    Optimization: check if equal to parent of current board.
 *    repeat 1-2.
 * 3: If goal,input board and parent recursively into a data structure.
 * 4: If null, unsolvable.
 *
 * Potential optimization. If a neighbour corresponds to a board in PQ or already explored, compare priority.
 * If new board priority lower => Means you have found a faster way to get to a board you are considering.
 * Solution:
 *  - If repeated board in PQ, replace board with higher priority.
 *  - If board already explored, do not put board into PQ. Instead, update all boards in PQ that arose from this
 *    board and update their priority.
 * If you don't do this, it is fine, but a previously explored board with higher priority may be explored again.
 * BUT solution is the SAME because a duplicate board with higher priority will NEVER give the answer.
 */
