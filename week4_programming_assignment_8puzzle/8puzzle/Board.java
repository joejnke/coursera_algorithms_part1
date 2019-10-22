/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // handle passing of valid size but null valued 2D-array argument to the constructor
    public Board(int[][] tiles) {
        this.n  = tiles.length;
        this.tiles = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], this.n);
        }
    }

    // string representation of this board
    public String toString() {
        String tileString = this.n + "\n";
        for (int i = 0; i < this.n; i++) {
            tileString = tileString.concat(Arrays.toString(this.tiles[i])
                                    .replace(",", " ")
                                    .replace("[", "")
                                    .replace("]", "") + "\n");
        }

        return  tileString;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDist = 0; // to compensate the miss counting of the empty tile
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != 0 && (this.n * i + j +1) != this.tiles[i][j]) {
                    hammingDist++;
                }
            }
        }

        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDist = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != 0) {
                    // System.out.println(manhattanDist + " " + "+= Math.abs((" + i + " + " + j + ") - ((" + this.tiles[i][j] + " - 1 ) / " + this.n + " + ((" + this.tiles[i][j] + " - 1) % " + this.n + ")))");
                    manhattanDist += Math.abs(i - (this.tiles[i][j] - 1) / this.n) + Math.abs(j - (this.tiles[i][j] - 1) % this.n);
                    // System.out.println("manhattanDist: " + manhattanDist + "\n");
                }
            }
        }

        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.n != that.n) return false;

        // check if every row is equal
        // for (int i = 0; i < this.n; i++) {
        //     if (!Arrays.equals(this.tiles[i], that.tiles[i])) return  false;
        // }

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    private class NeighbourBoardSet implements Iterable<Board> {
        private final ArrayList<Board> neighbours = new ArrayList<Board>();

        // find all the neighbours of the constructor argument board and fill them into neighbours array.
        public NeighbourBoardSet(Board board) {
            int row0 = 0;
            int col0 = 0;

            // find the indeces of the blank tile
            for (int i = 0; i < board.n; i++) {
                for (int j = 0; j < board.n; j++) {
                    if (board.tiles[i][j] == 0) {
                        row0 = i;
                        col0 = j;
                        break;
                    }
                }
            }

            // find the neighbours
            int[][] neighbourTile = new int[board.n][board.n];
            for (int i = 0; i < board.n; i++) {
                neighbourTile[i] = Arrays.copyOf(board.tiles[i], board.n);
            }

            if (row0 + 1 < board.n) {
                // exchange tiles
                neighbourTile[row0][col0] = neighbourTile[row0 + 1][col0];
                neighbourTile[row0 + 1][col0] = 0;

                // add neighbour board
                neighbours.add(new Board(neighbourTile));

                // revert the exchange
                neighbourTile[row0 + 1][col0] = neighbourTile[row0][col0];
                neighbourTile[row0][col0] = 0;
            }

            if (row0 - 1 >= 0) {
                // exchange tiles
                neighbourTile[row0][col0] = neighbourTile[row0 - 1][col0];
                neighbourTile[row0 - 1][col0] = 0;

                // add neighbour board
                neighbours.add(new Board(neighbourTile));

                // revert the exchange
                neighbourTile[row0 - 1][col0] = neighbourTile[row0][col0];
                neighbourTile[row0][col0] = 0;
            }

            if (col0 + 1 < board.n) {
                // exchange tiles
                neighbourTile[row0][col0] = neighbourTile[row0][col0 + 1];
                neighbourTile[row0][col0 + 1] = 0;

                // add neighbour board
                neighbours.add(new Board(neighbourTile));

                // revert the exchange
                neighbourTile[row0][col0 + 1] = neighbourTile[row0][col0];
                neighbourTile[row0][col0] = 0;
            }

            if (col0 - 1 >= 0) {
                // exchange tiles
                neighbourTile[row0][col0] = neighbourTile[row0][col0 - 1];
                neighbourTile[row0][col0 - 1] = 0;

                // add neighbour board
                neighbours.add(new Board(neighbourTile));

                // revert the exchange
                neighbourTile[row0][col0 - 1] = neighbourTile[row0][col0];
                neighbourTile[row0][col0] = 0;
            }
        }

        private class BoardIterator implements Iterator<Board> {
            private int numNeighbours = neighbours.size();

            public boolean hasNext() { return numNeighbours != 0; }

            public void remove() { throw new UnsupportedOperationException(); }

            public Board next() {
                if (!hasNext()) throw new NoSuchElementException("already returned the last neighbour...");
                return neighbours.get(--numNeighbours);
            }
        }

        // return an iterator over items in order from front to back
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new NeighbourBoardSet(this);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int temp;
        int[][] twinTile = new int[this.n][this.n];

        // Random rand = new Random();
        int h = this.n - 1;
        int j = this.n - 1;
        int k = 0;
        int m = 0;

        for (int i = 0; i < this.n; i++) {
            twinTile[i] = Arrays.copyOf(this.tiles[i], this.n);
        }

        if (twinTile[h][j] == 0) { h--; }
        else if (twinTile[k][m] == 0) { k++; }

        temp = twinTile[h][j];
        twinTile[h][j] = twinTile[k][m];
        twinTile[k][m] = temp;

        // while ((h == k && j == m) || twinTile[h][j] == 0 || twinTile[k][m] == 0) {
        //     Random rand2 = new Random();
        //     h = rand2.nextInt(this.n);
        //     j = rand2.nextInt(this.n);
        //     k = rand2.nextInt(this.n);
        //     m = rand2.nextInt(this.n);
        // }
        //
        // temp = twinTile[h][j];
        // twinTile[h][j] = twinTile[k][m];
        // twinTile[k][m] = temp;

        // if (twinTile[h][j] == 0) {
        //     temp = twinTile[++h][j];
        // }
        //
        // else { temp = twinTile[h][j]; }
        //
        // if (twinTile[k][m] == 0) {
        //     twinTile[h][j] = twinTile[++k][m];
        // }
        //
        // else { twinTile[h][j] = twinTile[k][m]; }
        // twinTile[k][m] = temp;

        return new Board(twinTile);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // initialize board
        Board testBoard = new Board(tiles);

        System.out.println("Dimension: " + testBoard.dimension());
        System.out.println("Hamming distance: " + testBoard.hamming());
        System.out.println("Manhattan distance: " + testBoard.manhattan());
        System.out.println("Board solved: " + testBoard.isGoal());
        System.out.println("Board is equal to itself: " + testBoard.equals(testBoard));

        Iterable<Board> neighbours = testBoard.neighbors();
        System.out.println("############## PRINTING NEIGHBOUR BOARDS ##############");
        for (Board brd : neighbours) System.out.println(brd.toString());

        System.out.println("Twin board: \n" + testBoard.twin());
    }
}
