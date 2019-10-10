/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF siteGrid;
    private int numOpenSites;
    private boolean[] isOpenArray;
    private boolean[] isFullArray;
    private boolean[] isBottomArray;
    private boolean isPercolated;
    // private int[] compIdArr;
    private int gridSize;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0 is not allowed...");
        }
        this.gridSize = n;
        this.siteGrid = new WeightedQuickUnionUF(gridSize * gridSize);
        this.numOpenSites = 0;
        this.isPercolated = false;
        this.isOpenArray = new boolean[gridSize * gridSize];
        this.isFullArray = new boolean[gridSize * gridSize];
        this.isBottomArray = new boolean[gridSize * gridSize];
    }

    // convert a given (row,col) pair into an index of [0, n*n-1] where both row and col are integers between 1 and n;
    private int indexFrom2d(int row, int col) {
        return ((col-1) + (row-1)*this.gridSize);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > this.gridSize || row < 1 || col > this.gridSize || col < 1) {
            throw new IllegalArgumentException("value given for (row, col) is Out of range...");
        }
        else if (!isOpen(row, col)) {
            this.isOpenArray[this.indexFrom2d(row, col)] = true;    // open the site.

            // a top site and component containing this site will be full when top site is opened.
            if (row == 1) {
                this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))] = true; // make isFullArray true at the component identifier index
            }

            if (row == this.gridSize) {
                this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))] = true; // make isBottomArray true at the component identifier index
            }

            // create connection with neighbouring open sites and make it full if any one of its neighbours is full.
            if (!(row == 1)) {
                if (isOpen(row - 1, col)) {
                    // make the site full if neighbour above is full.
                    if (this.isFullArray[siteGrid.find(this.indexFrom2d(row - 1, col))] || this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row - 1, col))] = true;
                    }

                    if (this.isBottomArray[siteGrid.find(this.indexFrom2d(row - 1, col))] || this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row - 1, col))] = true;
                    }

                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row - 1, col));
                }
            }

            if (!(row == this.gridSize)) {
                if (isOpen(row+1, col)) {
                    // make the site full if neighbour below is full.
                    if (this.isFullArray[siteGrid.find(this.indexFrom2d(row+1, col))] || this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row+1, col))] = true;
                    }

                    if (this.isBottomArray[siteGrid.find(this.indexFrom2d(row + 1, col))] || this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row + 1, col))] = true;
                    }

                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row + 1, col));
                }
            }

            if (!(col == 1)) {
                if (isOpen(row, col-1)) {
                    // make the site full if left neighbour is full.
                    if (this.isFullArray[siteGrid.find(this.indexFrom2d(row, col - 1))] || this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col - 1))] = true;
                    }

                    if (this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col - 1))] || this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col - 1))] = true;
                    }
                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row, col-1));
                }
            }

            if (!(col == this.gridSize)) {
                if (isOpen(row, col+1)) {
                    // make the site full if right neighbour is full.
                    if (this.isFullArray[siteGrid.find(this.indexFrom2d(row, col + 1))] || this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isFullArray[siteGrid.find(this.indexFrom2d(row, col + 1))] = true;
                    }

                    if (this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col + 1))] || this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))]) {
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col))] = true;
                        this.isBottomArray[siteGrid.find(this.indexFrom2d(row, col + 1))] = true;
                    }

                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row, col+1));
                }
            }

            if (this.isBottomArray[this.siteGrid.find(this.indexFrom2d(row, col))] && this.isFullArray[this.siteGrid.find(this.indexFrom2d(row, col))]) {
                this.isPercolated = true;
            }
            this.numOpenSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > this.gridSize || row < 1 || col > this.gridSize || col < 1) {
            throw new IllegalArgumentException("value given for (row, col) is Out of range...");
        }
        return this.isOpenArray[this.indexFrom2d(row, col)];
    }

    // is the site (p) open?
    private boolean isOpen(int p) {
        if (p < 0 || p >= (this.gridSize * this.gridSize)) {
            throw new IllegalArgumentException("value given for p is Out of range...");
        }
        return this.isOpenArray[p];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > this.gridSize || row < 1 || col > this.gridSize || col < 1) {
            throw new IllegalArgumentException("value given for (row, col) is Out of range...");
        }

        return this.isFullArray[this.siteGrid.find(this.indexFrom2d(row, col))];
    }

    // is the site p full? where p is between 0 and n*n
    private boolean isFull(int p) {
        if (p < 0 || p >= (this.gridSize * this.gridSize)) {
            throw new IllegalArgumentException("value given for p is Out of range...");
        }

        return this.isFullArray[this.siteGrid.find(p)];
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.isPercolated;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        // int trials = Integer.parseInt(args[1]);
        int row = -2147483648;
        int col = -2147483648;

        try {
            Percolation perc = new Percolation(n);
            System.out.println(perc.isFull(row, col));

            System.out.println("isFull isn't working fine");
            System.out.println(perc.gridSize);

        }

        catch (IllegalArgumentException e) {
            System.out.println(e.toString());
            System.out.println("isFull is working fine");
        }

    }
}
