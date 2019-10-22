/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {
    private final Board initial;
    private boolean solvable;
    private boolean twinSolvable;
    private int numMoves = 0;
    private int twinNumMoves = 0;
    // private Queue<Board> solnQueue;
    private Stack<Board> solnStack;
    // private Queue<Board> twinSolnStack;
    // ArrayList<Object> gameTree;
    // ArrayList<Object> twinGameTree;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("initial board is null...");

        class SearchNode implements Comparable<SearchNode> {
            private Board board;
            private SearchNode prevSearchNode;
            private int manhattanPriority;

            // initialize the search board with the requiered attribute values
            public SearchNode(Board board, int numMoves, SearchNode prevSearchNode) {
                this.board = board;
                this.prevSearchNode = prevSearchNode;
                this.manhattanPriority = numMoves + this.board.manhattan();
            }

            // compare a SearchNode using the manhattan priority function
            public int compareTo(SearchNode that) {
                return Integer.compare(this.manhattanPriority, that.manhattanPriority);
            }
        }

        // initialize variables requiered by the solver
        this.initial = initial;
        this.solvable = false;
        this.twinSolvable = false;
        this.solnStack = new Stack<Board>();

        ArrayList<SearchNode> gameTree = new ArrayList<SearchNode>();
        ArrayList<SearchNode> twinGameTree = new ArrayList<SearchNode>();

        // int numMoves = 0;
        // int twinNumMoves = 0;
        Board twinInitial = this.initial.twin();
        MinPQ<SearchNode> mpq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinMpq = new MinPQ<SearchNode>();

        mpq.insert(new SearchNode(this.initial, this.numMoves, null));

        twinMpq.insert(new SearchNode(twinInitial, this.twinNumMoves, null));

        // A* search
        // untill either of initial or twin board is solved
        while (!(this.solvable || this.twinSolvable)) {
            SearchNode temp = mpq.delMin();
            SearchNode twinTemp = twinMpq.delMin();

            if (temp.board.isGoal()) {
                gameTree.add(temp);
                this.solvable = true;
            }

            else {
                this.numMoves++;
                gameTree.add(temp);

                if (temp.prevSearchNode == null) {
                    for (Board brd : temp.board.neighbors()) {
                        gameTree.add(new SearchNode(brd, this.numMoves, temp));
                        mpq.insert(new SearchNode(brd, this.numMoves, temp));
                    }
                }

                else {
                    for (Board brd : temp.board.neighbors()) {
                        if (!brd.equals(temp.prevSearchNode.board)) {
                            gameTree.add(new SearchNode(brd, this.numMoves, temp));
                            mpq.insert(new SearchNode(brd, this.numMoves, temp));
                        }
                    }
                }
            }

            if (twinTemp.board.isGoal()) {
                twinGameTree.add(twinTemp);
                this.twinSolvable = true;
            }

            else {
                this.twinNumMoves++;
                twinGameTree.add(twinTemp);
                if (twinTemp.prevSearchNode == null) {
                    for (Board brd : twinTemp.board.neighbors()) {
                        twinGameTree.add(new SearchNode(brd, this.twinNumMoves, twinTemp));
                        twinMpq.insert(new SearchNode(brd, this.twinNumMoves, twinTemp));
                    }
                }

                else {
                    for (Board brd : twinTemp.board.neighbors()) {
                        if (!brd.equals(twinTemp.prevSearchNode.board)) {
                            twinGameTree.add(new SearchNode(brd, this.twinNumMoves, twinTemp));
                            twinMpq.insert(new SearchNode(brd, this.twinNumMoves, twinTemp));
                        }
                    }
                }
            }
        }

        // fill the boards in the solution path to a queue of boards
        SearchNode currentSearchNode = gameTree.get(gameTree.size() - 1); // search node with the goal board
        while (currentSearchNode.prevSearchNode != null) {
            this.solnStack.push(currentSearchNode.board);
            currentSearchNode = currentSearchNode.prevSearchNode;
        }
        this.solnStack.push(currentSearchNode.board); // finally enqueue the initial board

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.numMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return this.solnStack; // check if it breaks immutability
    }

    // test client
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
