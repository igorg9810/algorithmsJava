/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] fractionOfOpenSites;
    private final int numOfExperiments;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Size of the grid or number of trials can't be negative");
        }
        fractionOfOpenSites = new double[trials];
        numOfExperiments = trials;
        for (int i = 0; i < trials; i++) {
            Percolation experiment = new Percolation(n);
            while (!experiment.percolates()) {
                int rowToOpen = StdRandom.uniform(n) + 1;
                int colToOpen = StdRandom.uniform(n) + 1;
                experiment.open(rowToOpen, colToOpen);
            }
            fractionOfOpenSites[i] = experiment.numberOfOpenSites()*1.0/(n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractionOfOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractionOfOpenSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96*stddev()/Math.sqrt(numOfExperiments));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96*stddev()/Math.sqrt(numOfExperiments));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        try {
            PercolationStats series = new PercolationStats(n, t);
            System.out.println("mean = " + series.mean());
            System.out.println("stddev = " + series.stddev());
            System.out.println("95% confidence interval = [" + series.confidenceLo() + ", "
                                       + series.confidenceHi() + "]");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
