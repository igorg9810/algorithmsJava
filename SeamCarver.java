import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;


public class SeamCarver {

    private Color[][] currentPictureGrid;
    private int currentPictureHeight;
    private int currentPictureWidth;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if (picture == null) {
            throw new IllegalArgumentException("argument to SeamCarver constructor is null");
        }

        currentPictureHeight = picture.height();
        currentPictureWidth = picture.width();

        energy = new double[currentPictureHeight][currentPictureWidth];
        currentPictureGrid = new Color[currentPictureHeight][currentPictureWidth];
        for (int i = 0; i < energy.length; i++)
        {
            energy[i][0] = 1000;
            energy[i][currentPictureWidth - 1] = 1000;
            currentPictureGrid[i][0] = picture.get(0, i);
            currentPictureGrid[i][currentPictureWidth - 1] = picture.get(currentPictureWidth - 1, i);
        }
        for (int j = 0; j < currentPictureWidth; j++)
        {
            energy[0][j] = 1000;
            energy[currentPictureHeight - 1][j] = 1000;
            currentPictureGrid[0][j] = picture.get(j, 0);
            currentPictureGrid[currentPictureHeight - 1][j] = picture.get(j, currentPictureHeight - 1);
        }
        for (int i = 1; i < energy.length - 1; i++)
        {
            for (int j = 1; j < energy[i].length - 1; j++)
            {
                energy[i][j] = -1;
                currentPictureGrid[i][j] = picture.get(j, i);
            }
        }
    }

    // current picture
    public Picture picture()
    {
        Picture currentPicture = new Picture(currentPictureWidth, currentPictureHeight);
        for (int i = 0; i < currentPictureGrid.length; i++)
        {
            for (int j = 0; j < currentPictureGrid[i].length; j++)
            {
                currentPicture.set(j, i, currentPictureGrid[i][j]);
            }
        }
        return currentPicture;
    }

    // width of current picture
    public int width()
    {
        return currentPictureWidth;
    }

    // height of current picture
    public int height()
    {
        return currentPictureHeight;
    }

    private int[] extractRGB(Color pixel)
    {

        int rgb = pixel.getRGB();
        return new int[]{
                (rgb >> 16) & 0xFF, // Red
                (rgb >> 8) & 0xFF,  // Green
                rgb & 0xFF          // Blue
        };
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (x < 0 || x > currentPictureWidth - 1 || y < 0 || y > currentPictureHeight - 1){
            throw new IllegalArgumentException("Some given coordinate is outside of picture's boundaries");
        }
        if (energy[y][x] != -1) {
            return energy[y][x];
        }
        else{
            Color pixelColorXLeft = currentPictureGrid[y][x - 1];
            Color pixelColorXRight = currentPictureGrid[y][x + 1];
            Color pixelColorYAbove = currentPictureGrid[y - 1][x];
            Color pixelColorYBelow = currentPictureGrid[y + 1][x];

            int [] colorsXLeft = extractRGB(pixelColorXLeft);
            int [] colorsXRight = extractRGB(pixelColorXRight);
            int [] colorsYAbove = extractRGB(pixelColorYAbove);
            int [] colorsYBelow = extractRGB(pixelColorYBelow);

            double deltaXSquare = 0;
            double deltaYSquare = 0;
            for (int i = 0; i < 3; i++)
            {
                deltaXSquare += Math.pow(colorsXLeft[i] - colorsXRight[i], 2);
                deltaYSquare += Math.pow(colorsYAbove[i] - colorsYBelow[i], 2);
            }
            energy[y][x] = Math.sqrt(deltaXSquare + deltaYSquare);
            return energy[y][x];
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {

        int[] horizontalSeam = new int[currentPictureWidth];
        double[][] dp = new double[currentPictureHeight][currentPictureWidth];
        int[][] edgeTo = new int[currentPictureHeight][currentPictureWidth];  // Stores the path for backtracking

        // Initialize the first column of dp with energy values
        for (int y = 0; y < currentPictureHeight; y++) {
            dp[y][0] = energy[y][0];
        }

        // Fill the dp array (Dynamic Programming)
        for (int x = 1; x < currentPictureWidth; x++) {
            for (int y = 0; y < currentPictureHeight; y++) {
                // Find the minimum energy path from the previous column
                dp[y][x] = dp[y][x - 1];  // Middle row
                edgeTo[y][x] = y;

                if (y > 0 && dp[y - 1][x - 1] < dp[y][x]) {
                    dp[y][x] = dp[y - 1][x - 1];  // Upper row
                    edgeTo[y][x] = y - 1;
                }
                if (y < currentPictureHeight - 1 && dp[y + 1][x - 1] < dp[y][x]) {
                    dp[y][x] = dp[y + 1][x - 1];  // Lower row
                    edgeTo[y][x] = y + 1;
                }

                dp[y][x] += energy(x, y);  // Add the current pixel's energy
            }
        }

        // Find the row in the last column with the minimum energy
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int y = 0; y < currentPictureHeight; y++) {
            if (dp[y][currentPictureWidth - 1] < minEnergy) {
                minEnergy = dp[y][currentPictureWidth - 1];
                minIndex = y;
            }
        }

        // Reconstruct the seam path
        horizontalSeam[currentPictureWidth - 1] = minIndex;
        for (int x = currentPictureWidth - 2; x >= 0; x--) {
            horizontalSeam[x] = edgeTo[horizontalSeam[x + 1]][x + 1];
        }

        return horizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        int[] verticalSeam = new int[currentPictureHeight];
        double[][] dp = new double[currentPictureHeight][currentPictureWidth];
        int[][] edgeTo = new int[currentPictureHeight][currentPictureWidth];  // Stores the path for backtracking

        // Initialize the top row of dp with energy values
        System.arraycopy(energy[0], 0, dp[0], 0, currentPictureWidth);

        // Fill the dp array (Dynamic Programming)
        for (int y = 1; y < currentPictureHeight; y++) {
            for (int x = 0; x < currentPictureWidth; x++) {
                // Find the minimum energy path from the row above
                dp[y][x] = dp[y - 1][x];  // Middle column
                edgeTo[y][x] = x;

                if (x > 0 && dp[y - 1][x - 1] < dp[y][x]) {
                    dp[y][x] = dp[y - 1][x - 1];  // Left column
                    edgeTo[y][x] = x - 1;
                }
                if (x < currentPictureWidth - 1 && dp[y - 1][x + 1] < dp[y][x]) {
                    dp[y][x] = dp[y - 1][x + 1];  // Right column
                    edgeTo[y][x] = x + 1;
                }

                dp[y][x] += energy(x, y);  // Add the current pixel's energy
            }
        }

        // Find the column in the bottom row with the minimum energy
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int x = 0; x < currentPictureWidth; x++) {
            if (dp[currentPictureHeight - 1][x] < minEnergy) {
                minEnergy = dp[currentPictureHeight - 1][x];
                minIndex = x;
            }
        }

        // Reconstruct the seam path
        verticalSeam[currentPictureHeight - 1] = minIndex;
        for (int y = currentPictureHeight - 2; y >= 0; y--) {
            verticalSeam[y] = edgeTo[y + 1][verticalSeam[y + 1]];
        }

        return verticalSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null) {
            throw new IllegalArgumentException("argument to removeHorizontalSeam method is null");
        }
        currentPictureHeight = currentPictureHeight - 1;

        // Create a new 2D array with one less row (height)
        Color[][] newPictureGrid = new Color[currentPictureHeight][currentPictureWidth];
        double[][] newEnergy = new double[currentPictureHeight][currentPictureWidth];

        // Iterate through each column and shift pixels up, skipping the seam row
        for (int x = 0; x < currentPictureWidth; x++) {
            int seamY = seam[x];  // The row to be removed in this column
            for (int y = 0; y < seamY; y++) {
                newPictureGrid[y][x] = currentPictureGrid[y][x];  // Copy rows above the seam
                if (x == 0 || x == currentPictureWidth - 1 || y == 0 || y == currentPictureHeight - 1) {
                    newEnergy[y][x] = 1000;
                }
                else if (y == seamY - 1) {
                    newEnergy[y][x] = -1;
                }
                else {
                    newEnergy[y][x] = energy[y][x];
                }
            }
            for (int y = seamY + 1; y <= currentPictureHeight; y++) {
                newPictureGrid[y - 1][x] = currentPictureGrid[y][x];  // Shift rows below the seam up
                if (x == 0 || x == currentPictureWidth - 1 || y == currentPictureHeight) {
                    newEnergy[y - 1][x] = 1000;
                }
                else if (y - 1 == seamY) {
                    newEnergy[y - 1][x] = -1;
                }
                else {
                    newEnergy[y - 1][x] = energy[y][x];
                }
            }
        }
        currentPictureGrid = newPictureGrid;
        energy = newEnergy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null) {
            throw new IllegalArgumentException("argument to removeVerticalSeam method is null");
        }
        currentPictureWidth = currentPictureWidth - 1;

        // Create a new 2D array with one less column (width)
        Color[][] newPictureGrid = new Color[currentPictureHeight][currentPictureWidth];
        double[][] newEnergy = new double[currentPictureHeight][currentPictureWidth];

        // Iterate through each row and shift pixels left, skipping the seam column
        for (int y = 0; y < currentPictureHeight; y++) {
            int seamX = seam[y];  // The row to be removed in this column
            for (int x = 0; x < seamX; x++) {
                newPictureGrid[y][x] = currentPictureGrid[y][x];  // Copy columns left of the seam
                if (x == 0 || x == currentPictureWidth - 1 || y == 0 || y == currentPictureHeight - 1) {
                    newEnergy[y][x] = 1000;
                }
                else if (x == seamX - 1) {
                    newEnergy[y][x] = -1;
                }
                else {
                    newEnergy[y][x] = energy[y][x];
                }
            }
            for (int x = seamX + 1; x <= currentPictureWidth; x++) {
                newPictureGrid[y][x - 1] = currentPictureGrid[y][x];  // Shift columns right of the seam to the left
                if (x == currentPictureWidth || y == 0 || y == currentPictureHeight - 1) {
                    newEnergy[y][x - 1] = 1000;
                }
                else if (x - 1 == seamX) {
                    newEnergy[y][x - 1] = -1;
                }
                else {
                    newEnergy[y][x - 1] = energy[y][x];
                }
            }
        }
        currentPictureGrid = newPictureGrid;
        energy = newEnergy;

    }

    //  unit testing (optional)
    public static void main(String[] args)
    {

    }

}
