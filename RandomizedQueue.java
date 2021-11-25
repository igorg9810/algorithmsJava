/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int head, tail, queueSize;

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        q = (Item[]) new Object[1];
        head = 0;
        tail = 0;
        queueSize = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return queueSize;
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = head; i < tail; i++)
        {
            // if (q[i] == null) continue;
            copy[j++] = q[i];
        }
        q = copy;
        head = 0;
        tail = j;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null) {
            throw new IllegalArgumentException("You can't pass null as an argument");
        }
        if (tail == q.length) resize(2*q.length);
        q[tail++] = item;
        queueSize++;
    }

    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("The queue is empty");
        }
        int j = StdRandom.uniform(head, tail);
        Item item = q[j];
        q[j] = null;
        if (tail - j <= (tail - head)/2)
        {
            for (int i = j + 1; i < tail; i++)
            {
                q[i - 1] = q[i];
            }
            tail--;
        }
        else
        {
            for (int i = j - 1; i >= head; i--)
            {
                q[i + 1] = q[i];
            }
            head++;
        }
        if (tail - head <= q.length/4 && q.length >= 4) resize(q.length/2);
        queueSize--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("The queue is empty");
        }
        int j = StdRandom.uniform(head, tail);
        return q[j];
    }

    private void swap(Item[] arr, int i, int j)
    {
        Item tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private Item[] makeRandomCopy(Item[] arr)
    {
        System.arraycopy(q, 0, arr, 0, tail - head);
        int j = 0;
        for (int i = head; i < tail; i++)
        {
            arr[j++] = q[i];
        }
        for (int i = j - 1; i >= 0; i--)
        {
            swap(arr, i, StdRandom.uniform(head, tail));
        }
        return arr;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new Iterator<Item>() {

            private Item[] randomCopy = makeRandomCopy((Item[]) new Object[tail - head]);
            private int i = 0;

            public boolean hasNext() {
                return i < randomCopy.length;
            }

            public Item next() {
                if (hasNext())
                {
                    return randomCopy[i++];
                }
                else
                {
                    throw new NoSuchElementException("Iterator has reached the end of queue");
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue myQueue = new RandomizedQueue();
        myQueue.enqueue(38);
        System.out.println(myQueue.dequeue());
        myQueue.enqueue(25);
        Iterator permutation = myQueue.iterator();
        while (permutation.hasNext())
        {
            System.out.println(permutation.next());
        }

    }

}
