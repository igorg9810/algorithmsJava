/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF grid;
    private boolean[] gridOpenSites;
    private final int gridSize;
    private int numOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size of the grid can't be negative");
        }
        grid = new WeightedQuickUnionUF(n*n + 2);
        gridOpenSites = new boolean[n*n + 2];
        for (int i = 1; i < gridOpenSites.length - 1; i++) {
            gridOpenSites[i] = false;
        }
        gridOpenSites[0] = true;
        gridOpenSites[n*n + 1] = true;
        gridSize = n;
        numOfOpenSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) {
            throw new IllegalArgumentException("Arguments out of bounds");
        }
        if (!gridOpenSites[1 + (row - 1) * gridSize + col - 1]) numOfOpenSites++;
        gridOpenSites[1 + (row - 1) * gridSize + col - 1] = true;
        if (col < gridSize && gridOpenSites[1 + (row - 1) * gridSize + col]) {
            grid.union(1 + (row - 1) * gridSize + col - 1, 1 + (row - 1) * gridSize + col);
        }
        if (col > 1 && gridOpenSites[1 + (row - 1) * gridSize + col - 2]) {
            grid.union(1 + (row - 1) * gridSize + col - 1, 1 + (row - 1) * gridSize + col - 2);
        }
        if (row > 1 && gridOpenSites[1 + (row - 2) * gridSize + col - 1]) {
            grid.union(1 + (row - 1) * gridSize + col - 1, 1 + (row - 2) * gridSize + col - 1);
        }
        if (row < gridSize && gridOpenSites[1 + row * gridSize + col - 1]) {
            grid.union(1 + (row - 1) * gridSize + col - 1, 1 + row * gridSize + col - 1);
        }
        if (row == 1) {
            grid.union(1 + (row - 1) * gridSize + col - 1, 0);
        }
        if (row == gridSize) {
            grid.union(1 + (row - 1) * gridSize + col - 1, gridSize * gridSize + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) {
            throw new IllegalArgumentException("Arguments out of bounds");
        }
        return gridOpenSites[1 + (row - 1) * gridSize + col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) {
            throw new IllegalArgumentException("Arguments out of bounds");
        }
        return (grid.find(0) == grid.find(1 + (row - 1) * gridSize + col - 1));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        //int count = 0;
        //for (int i = 1; i < gridOpenSites.length - 1; i++) {
        //    if (gridOpenSites[i]) count++;
        //}
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (grid.find(0) == grid.find(gridSize*gridSize + 1));
    }

    // test client (optional)
    public static void main(String[] args) {
        // empty for a reason
    }
}
