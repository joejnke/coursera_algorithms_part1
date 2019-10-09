/******************************************************************************
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
    private int[] compIdArr;
    private int gridSize;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0 is not allowed...");
        }
        this.gridSize = n;
        this.siteGrid = new WeightedQuickUnionUF(gridSize * gridSize);
        this.numOpenSites = 0;
        this.isOpenArray = new boolean[gridSize * gridSize];
        this.isFullArray = new boolean[gridSize * gridSize];
        this.compIdArr = new int[gridSize * gridSize];
        for (int i = 0; i < this.gridSize; i++) {
            this.compIdArr[i] = i;
        }
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

            // create connection with neighbouring open sites.
            if (!(row == 1)) {
                if (isOpen(row - 1, col) && this.compIdArr[this.indexFrom2d(row, col)] != this.compIdArr[this.indexFrom2d(row - 1, col)]) {
                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row - 1, col));
                    this.compIdArr[this.indexFrom2d(row, col)] = siteGrid.find(this.indexFrom2d(row, col));
                    this.compIdArr[this.indexFrom2d(row - 1, col)] = siteGrid.find(this.indexFrom2d(row - 1, col));
                }
            }

            if (!(row == this.gridSize)) {
                if (isOpen(row+1, col) && this.compIdArr[this.indexFrom2d(row, col)] != this.compIdArr[this.indexFrom2d(row + 1, col)]) {
                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row+1, col));
                    this.compIdArr[this.indexFrom2d(row, col)] = siteGrid.find(this.indexFrom2d(row, col));
                    this.compIdArr[this.indexFrom2d(row + 1, col)] = siteGrid.find(this.indexFrom2d(row + 1, col));
                }
            }

            if (!(col == 1)) {
                if (isOpen(row, col-1) && this.compIdArr[this.indexFrom2d(row, col)] != this.compIdArr[this.indexFrom2d(row, col - 1)]) {
                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row, col-1));
                    this.compIdArr[this.indexFrom2d(row, col)] = siteGrid.find(this.indexFrom2d(row, col));
                    this.compIdArr[this.indexFrom2d(row, col - 1)] = siteGrid.find(this.indexFrom2d(row, col - 1));
                }
            }

            if (!(col == this.gridSize)) {
                if (isOpen(row, col+1) && this.compIdArr[this.indexFrom2d(row, col)] != this.compIdArr[this.indexFrom2d(row, col + 1)]) {
                    siteGrid.union(this.indexFrom2d(row, col), this.indexFrom2d(row, col+1));
                    this.compIdArr[this.indexFrom2d(row, col)] = siteGrid.find(this.indexFrom2d(row, col));
                    this.compIdArr[this.indexFrom2d(row, col + 1)] = siteGrid.find(this.indexFrom2d(row, col + 1));
                }
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

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > this.gridSize || row < 1 || col > this.gridSize || col < 1) {
            throw new IllegalArgumentException("value given for (row, col) is Out of range...");
        }
        int p = this.indexFrom2d(row, col);

        // if (!this.isFullArray[p]) {
        //     for (int i = 0; i < this.gridSize; i++) {
        //         if (this.siteGrid.connected(p, i)) {
        //             this.isFullArray[p] = true;
        //             this.isOpenArray[p] = true;
        //
        //             return this.isFullArray[p];
        //         }
        //     }
        //
        //     return this.isFullArray[p];
        // }
        //
        // return this.isFullArray[p];

        for (int i = 0; i < this.gridSize; i++) {
            if (isOpen(1, i + 1) && this.siteGrid.connected(p, i)) {
                return true;
            }
        }

        return false;
    }

    // is the site p full? where p is between 0 and n*n
    private boolean isFull(int p) {
        if (p < 0 || p >= (this.gridSize * this.gridSize)) {
            throw new IllegalArgumentException("value given for p is Out of range...");
        }

        // if (!this.isFullArray[p]) {
        //     for (int i = 0; i < this.gridSize; i++) {
        //         if (this.siteGrid.connected(p, i)) {
        //             this.isFullArray[p] = true;
        //             this.isOpenArray[p] = true;
        //
        //             return this.isFullArray[p];
        //         }
        //     }
        //
        //     return this.isFullArray[p];
        // }
        //
        // return this.isFullArray[p];

        for (int i = 0; i < this.gridSize; i++) {
            if (isOpen(1, i + 1) && this.siteGrid.connected(p, i)) {
                return true;
            }
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    // does the system percolate?
    // determine if any one of the bottom sites is fully connected.
    public boolean percolates() {
        int maxIndex = this.gridSize *this.gridSize;
        for (int i = maxIndex - this.gridSize; i < maxIndex; i++) {
            if (this.isFull(i)) {
                return true;
            }
        }
        return false;
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
            System.out.println((row-1)*(perc.gridSize - row));
            System.out.println((col-1)*(perc.gridSize - col));
            System.out.println((row-1)*(perc.gridSize - row) < 0 || (col-1)*(perc.gridSize - col) < 0);

            // StdOut.printf("%-23s = %f\n", "mean", percStat.mean());
            // StdOut.printf("%-23s = %f\n", "stddev", percStat.stddev());
            // StdOut.printf("%-23s = [%f, %f]\n", "95% confidence interval", percStat.confidenceLo(),
            //               percStat.confidenceHi());
        }

        catch (IllegalArgumentException e) {
            System.out.println(e.toString());
            System.out.println("isFull is working fine");
        }

    }
}
