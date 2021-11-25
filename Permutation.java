/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue myQueue = new RandomizedQueue();
        while (!StdIn.isEmpty())
        {
            myQueue.enqueue(StdIn.readString());
        }
        Iterator permutation = myQueue.iterator();
        for (int i = 0; i < k; i++)
        {
            System.out.println(myQueue.dequeue());
        }
    }
}
