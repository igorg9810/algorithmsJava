/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */


import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private int boardSize;
    private int blankX, blankY;
    private int[][] boardTiles;
    private Board twinBoard;
    private boolean twinReady;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        this.boardSize = tiles.length;
        this.boardTiles = new int[this.boardSize][this.boardSize];
        this.twinReady = false;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                this.boardTiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0)
                {
                    this.blankX = j;
                    this.blankY = i;
                }
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder result = new StringBuilder(Integer.toString(this.boardSize));
        result.append(System.lineSeparator());
        for (int i = 0; i < this.boardSize; i++)
        {
            for (int j = 0; j < this.boardSize; j++)
            {
                result.append(Integer.toString(this.boardTiles[i][j]));
                if (j != this.boardSize - 1) result.append(" ");
            }
            if (i != this.boardSize - 1) result.append(System.lineSeparator());
        }
        return result.toString();
    }

    // board dimension n
    public int dimension()
    {
        return this.boardSize;
    }

    // number of tiles out of place
    public int hamming()
    {
        int hammingDistance = 0;
        for (int i = 0; i < this.boardSize; i++)
        {
            for (int j = 0; j < this.boardSize; j++)
            {
                if (this.boardTiles[i][j] == 0) continue;
                if (this.boardTiles[i][j] != this.boardSize * i + j + 1)
                {
                    hammingDistance += 1;
                }
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int manhattanDistance = 0;
        for (int i = 0; i < this.boardSize; i++)
        {
            for (int j = 0; j < this.boardSize; j++)
            {
                if (this.boardTiles[i][j] == 0) continue;
                if (this.boardTiles[i][j] != this.boardSize * i + j + 1)
                {
                    manhattanDistance += Math.abs((this.boardTiles[i][j] - 1) / this.boardSize - i);
                    manhattanDistance += Math.abs((this.boardTiles[i][j] - 1) % this.boardSize - j);
                }
            }
        }
        return manhattanDistance;

    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return (this.manhattan() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null) return false;
        if (y == this) return true;
        if (!(y.getClass().equals(this.getClass()))) return false;
        Board b = (Board) y;
        if (this.boardSize != b.boardSize) return false;
        for (int i = 0; i < this.boardSize; i++)
        {
            for (int j = 0; j < this.boardSize; j++)
            {
                if (this.boardTiles[i][j] != b.boardTiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        LinkedQueue<Board> neighbors = new LinkedQueue<Board>();
        if (this.blankX == 0 && this.blankY == 0)
        {
            Board n1, n2;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n1.boardTiles[0][0] = n1.boardTiles[0][1];
            n1.boardTiles[0][1] = 0;
            n1.blankY = 0;
            n1.blankX = 1;
            n2.boardTiles[0][0] = n2.boardTiles[1][0];
            n2.boardTiles[1][0] = 0;
            n2.blankY = 1;
            n2.blankX = 0;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
        }
        else if (this.blankX == 0 && this.blankY == this.boardSize - 1)
        {
            Board n1, n2;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n1.boardTiles[this.boardSize - 1][0] = n1.boardTiles[this.boardSize - 1][1];
            n1.boardTiles[this.boardSize - 1][1] = 0;
            n1.blankY = this.boardSize - 1;
            n1.blankX = 1;
            n2.boardTiles[this.boardSize - 1][0] = n2.boardTiles[this.boardSize - 2][0];
            n2.boardTiles[this.boardSize - 2][0] = 0;
            n2.blankY = this.boardSize - 2;
            n2.blankX = 0;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
        }
        else if (this.blankX == this.boardSize - 1 && this.blankY == 0)
        {
            Board n1, n2;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n1.boardTiles[0][this.boardSize - 1] = n1.boardTiles[0][this.boardSize - 2];
            n1.boardTiles[0][this.boardSize - 2] = 0;
            n1.blankY = 0;
            n1.blankX = this.boardSize - 2;
            n2.boardTiles[0][this.boardSize - 1] = n2.boardTiles[1][this.boardSize - 1];
            n2.boardTiles[1][this.boardSize - 1] = 0;
            n2.blankY = 1;
            n2.blankX = this.boardSize - 1;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
        }
        else if (this.blankX == this.boardSize - 1 && this.blankY == this.boardSize - 1)
        {
            Board n1, n2;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n1.boardTiles[this.boardSize - 1][this.boardSize - 1] = n1.boardTiles[this.boardSize - 1][this.boardSize - 2];
            n1.boardTiles[this.boardSize - 1][this.boardSize - 2] = 0;
            n1.blankY = this.boardSize - 1;
            n1.blankX = this.boardSize - 2;
            n2.boardTiles[this.boardSize - 1][this.boardSize - 1] = n2.boardTiles[this.boardSize - 2][this.boardSize - 1];
            n2.boardTiles[this.boardSize - 2][this.boardSize - 1] = 0;
            n2.blankY = this.boardSize - 2;
            n2.blankX = this.boardSize - 1;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
        }
        else if (this.blankX == 0)
        {
            Board n1, n2, n3;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n3 = new Board(this.boardTiles);
            n1.boardTiles[this.blankY][0] = n1.boardTiles[this.blankY - 1][0];
            n1.boardTiles[this.blankY - 1][0] = 0;
            n1.blankY = this.blankY - 1;
            n1.blankX = 0;
            n2.boardTiles[this.blankY][0] = n2.boardTiles[this.blankY + 1][0];
            n2.boardTiles[this.blankY + 1][0] = 0;
            n2.blankY = this.blankY + 1;
            n2.blankX = 0;
            n3.boardTiles[this.blankY][0] = n3.boardTiles[this.blankY][1];
            n3.boardTiles[this.blankY][1] = 0;
            n3.blankY = this.blankY;
            n3.blankX = 1;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
            neighbors.enqueue(n3);
        }
        else if (this.blankY == 0)
        {
            Board n1, n2, n3;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n3 = new Board(this.boardTiles);
            n1.boardTiles[0][this.blankX] = n1.boardTiles[0][this.blankX - 1];
            n1.boardTiles[0][this.blankX - 1] = 0;
            n1.blankY = 0;
            n1.blankX = this.blankX - 1;
            n2.boardTiles[0][this.blankX] = n2.boardTiles[0][this.blankX + 1];
            n2.boardTiles[0][this.blankX + 1] = 0;
            n2.blankY = 0;
            n2.blankX = this.blankX + 1;
            n3.boardTiles[0][this.blankX] = n3.boardTiles[1][this.blankX];
            n3.boardTiles[1][this.blankX] = 0;
            n3.blankY = 1;
            n3.blankX = this.blankX;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
            neighbors.enqueue(n3);
        }
        else if (this.blankX == this.boardSize - 1)
        {
            Board n1, n2, n3;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n3 = new Board(this.boardTiles);
            n1.boardTiles[this.blankY][this.boardSize - 1] = n1.boardTiles[this.blankY - 1][this.boardSize - 1];
            n1.boardTiles[this.blankY - 1][this.boardSize - 1] = 0;
            n1.blankY = this.blankY - 1;
            n1.blankX = this.boardSize - 1;
            n2.boardTiles[this.blankY][this.boardSize - 1] = n2.boardTiles[this.blankY + 1][this.boardSize - 1];
            n2.boardTiles[this.blankY + 1][this.boardSize - 1] = 0;
            n2.blankY = this.blankY + 1;
            n2.blankX = this.boardSize - 1;
            n3.boardTiles[this.blankY][this.boardSize - 1] = n3.boardTiles[this.blankY][this.boardSize - 2];
            n3.boardTiles[this.blankY][this.boardSize - 2] = 0;
            n3.blankY = this.blankY;
            n3.blankX = this.boardSize - 2;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
            neighbors.enqueue(n3);
        }
        else if (this.blankY == this.boardSize - 1)
        {
            Board n1, n2, n3;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n3 = new Board(this.boardTiles);
            n1.boardTiles[this.boardSize - 1][this.blankX] = n1.boardTiles[this.boardSize - 1][this.blankX - 1];
            n1.boardTiles[this.boardSize - 1][this.blankX - 1] = 0;
            n1.blankY = this.boardSize - 1;
            n1.blankX = this.blankX - 1;
            n2.boardTiles[this.boardSize - 1][this.blankX] = n2.boardTiles[this.boardSize - 1][this.blankX + 1];
            n2.boardTiles[this.boardSize - 1][this.blankX + 1] = 0;
            n2.blankY = this.boardSize - 1;
            n2.blankX = this.blankX + 1;
            n3.boardTiles[this.boardSize - 1][this.blankX] = n3.boardTiles[this.boardSize - 2][this.blankX];
            n3.boardTiles[this.boardSize - 2][this.blankX] = 0;
            n3.blankY = this.boardSize - 2;
            n3.blankX = this.blankX;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
            neighbors.enqueue(n3);
        }
        else
        {
            Board n1, n2, n3, n4;
            n1 = new Board(this.boardTiles);
            n2 = new Board(this.boardTiles);
            n3 = new Board(this.boardTiles);
            n4 = new Board(this.boardTiles);
            n1.boardTiles[this.blankY][this.blankX] = n1.boardTiles[this.blankY - 1][this.blankX];
            n1.boardTiles[this.blankY - 1][this.blankX] = 0;
            n1.blankY = this.blankY - 1;
            n1.blankX = this.blankX;
            n2.boardTiles[this.blankY][this.blankX] = n2.boardTiles[this.blankY + 1][this.blankX];
            n2.boardTiles[this.blankY + 1][this.blankX] = 0;
            n2.blankY = this.blankY + 1;
            n2.blankX = this.blankX;
            n3.boardTiles[this.blankY][this.blankX] = n3.boardTiles[this.blankY][this.blankX - 1];
            n3.boardTiles[this.blankY][this.blankX - 1] = 0;
            n3.blankY = this.blankY;
            n3.blankX = this.blankX - 1;
            n4.boardTiles[this.blankY][this.blankX] = n4.boardTiles[this.blankY][this.blankX + 1];
            n4.boardTiles[this.blankY][this.blankX + 1] = 0;
            n4.blankY = this.blankY;
            n4.blankX = this.blankX + 1;
            neighbors.enqueue(n1);
            neighbors.enqueue(n2);
            neighbors.enqueue(n3);
            neighbors.enqueue(n4);
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        if (this.twinReady) return this.twinBoard;
        else
        {
            this.twinBoard = new Board(this.boardTiles);
            int tempY, tempX, tempY2, tempX2;
            do {
                tempX = StdRandom.uniform(0, this.boardSize);
                tempY = StdRandom.uniform(0, this.boardSize);
                tempX2 = StdRandom.uniform(0, this.boardSize);
                tempY2 = StdRandom.uniform(0, this.boardSize);
            } while (this.twinBoard.boardTiles[tempY][tempX] == 0 || this.twinBoard.boardTiles[tempY2][tempX2] == 0 || (tempX == tempX2 && tempY == tempY2));
            int temp = this.twinBoard.boardTiles[tempY][tempX];
            this.twinBoard.boardTiles[tempY][tempX] = this.twinBoard.boardTiles[tempY2][tempX2];
            this.twinBoard.boardTiles[tempY2][tempX2] = temp;
            this.twinReady = true;
            return this.twinBoard;
        }
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {

    }

}
