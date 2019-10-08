/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    static final double CONFIDENCE_95 = 1.96E00;
    private double mean;
    private double stddev;
    private double[] pThreshArray;
    private int t;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("values of n <= 0 and trials <= 0 are not allowed...");
        }

        this.t = trials;
        this.pThreshArray = new double[t];
        for (int i = 0; i < t; i++) {
            try {
                Percolation perc = new Percolation(n);

                while (!(perc.percolates())) { //|| Perc.numberOfOpenSites() == n*n)){
                    int row = StdRandom.uniform(1, n+1); // random integers in the range [1, n]
                    int col = StdRandom.uniform(1, n+1); // random integers in the range [1, n]
                    perc.open(row, col);
                }

                this.pThreshArray[i] = ((double) perc.numberOfOpenSites()) / (n*n);
            }

            catch (IllegalArgumentException e) {
                System.out.println(e.toString());
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        this.mean = StdStats.mean(this.pThreshArray);
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        this.stddev = StdStats.stddev(this.pThreshArray);
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - PercolationStats.CONFIDENCE_95*(Math.sqrt(stddev/this.t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + PercolationStats.CONFIDENCE_95*(Math.sqrt(stddev/this.t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        try {
            PercolationStats percStat = new PercolationStats(n, trials);
            StdOut.printf("%-23s = %f\n", "mean", percStat.mean());
            StdOut.printf("%-23s = %f\n", "stddev", percStat.stddev());
            StdOut.printf("%-23s = [%f, %f]\n", "95% confidence interval", percStat.confidenceLo(),
                          percStat.confidenceHi());
        }

        catch (IllegalArgumentException e) {
            System.out.println(e.toString());
        }

    }

}

