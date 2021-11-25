/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private boolean isSolvable;
    private int numberOfMoves;
    private LinkedStack<Board> solution = new LinkedStack<Board>();

    private class GameNode
    {
        Board board;
        GameNode prev;
        int moves;
        int manhattan;
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null) {
            throw new IllegalArgumentException("argument to Solver constructor is null");
        }

        MinPQ<GameNode> gameTree;
        MinPQ<GameNode> twinGameTree;
        this.isSolvable = true;
        this.numberOfMoves = 0;
        gameTree = new MinPQ<GameNode>(priorityOrder());
        twinGameTree = new MinPQ<GameNode>(priorityOrder());
        GameNode initialNode = new GameNode();
        GameNode initialTwinNode = new GameNode();
        initialNode.board = initial;
        initialNode.moves = 0;
        initialNode.prev = null;
        initialNode.manhattan = initial.manhattan();
        initialTwinNode.board = initial.twin();
        initialTwinNode.moves = 0;
        initialTwinNode.prev = null;
        initialTwinNode.manhattan = initialTwinNode.board.manhattan();
        gameTree.insert(initialNode);
        twinGameTree.insert(initialTwinNode);
        while (!gameTree.isEmpty() || !twinGameTree.isEmpty())
        {
            if (gameTree.min().board.isGoal())
            {
                prepareSolution(gameTree);
                this.numberOfMoves = gameTree.min().moves;
                return;
            }
            if (twinGameTree.min().board.isGoal())
            {
                this.isSolvable = false;
                return;
            }
            GameNode currentNode = gameTree.delMin();
            GameNode twinCurrentNode = twinGameTree.delMin();
            for (Board neighbor:
                 currentNode.board.neighbors()) {
                if (currentNode.prev != null && currentNode.prev.board.equals(neighbor)) continue;
                GameNode addedNode = new GameNode();
                addedNode.board = neighbor;
                addedNode.moves = currentNode.moves + 1;
                addedNode.manhattan = neighbor.manhattan();
                addedNode.prev = currentNode;
                gameTree.insert(addedNode);
            }
            for (Board neighbor:
                    twinCurrentNode.board.neighbors()) {
                if (twinCurrentNode.prev != null && twinCurrentNode.prev.board.equals(neighbor)) continue;
                GameNode addedNode = new GameNode();
                addedNode.board = neighbor;
                addedNode.moves = twinCurrentNode.moves + 1;
                addedNode.manhattan = neighbor.manhattan();
                addedNode.prev = twinCurrentNode;
                twinGameTree.insert(addedNode);
            }
        }
        this.isSolvable = false;
    }

    private Comparator<GameNode> priorityOrder() {
        /* YOUR CODE HERE */
        return new ByPriority();
    }

    private class ByPriority implements Comparator<GameNode>
    {
        public int compare(GameNode a, GameNode b)
        {
            int priorityA = a.moves + a.manhattan;
            int priorityB = b.moves + b.manhattan;
            if (priorityA < priorityB) return -1;
            else if (priorityA == priorityB) return 0;
            else return +1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!this.isSolvable()) return -1;
        else
        {
            return this.numberOfMoves;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!this.isSolvable()) return null;
        return this.solution;
    }

    private void prepareSolution(MinPQ<GameNode> tree)
    {
        this.solution = new LinkedStack<Board>();
        GameNode node = tree.min();
        while (node != null)
        {
            this.solution.push(node.board);
            node = node.prev;
        }
    }

    // test client (see below)
    public static void main(String[] args)
    {
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