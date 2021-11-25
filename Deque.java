/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int dequeSize;

    private class Node
    {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        dequeSize = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null && last == null;
    }

    // return the number of items on the deque
    public int size() {
        return dequeSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't pass null as an argument");
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (oldfirst == null) last = first;
        else oldfirst.prev = first;
        dequeSize++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't pass null as an argument");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (oldlast == null) first = last;
        else oldlast.next = last;
        dequeSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }
        Item item = first.item;
        first = first.next;
        if (first == null) last = null;
        else first.prev = null;
        dequeSize--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }
        Item item = last.item;
        last = last.prev;
        if (last == null) first = null;
        else last.next = null;
        dequeSize--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (hasNext())
                {
                    Item item = current.item;
                    current = current.next;
                    return item;
                }
                else
                {
                    throw new NoSuchElementException("Iterator has reached the end of deque");
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque myDeque = new Deque();
        System.out.println(myDeque.isEmpty());
        myDeque.addFirst("This");
        myDeque.addLast("is");
        myDeque.addLast("End");
        System.out.println(myDeque.size());
        myDeque.removeFirst();
        myDeque.removeLast();
        Iterator sequence = myDeque.iterator();
        while (sequence.hasNext())
        {
            System.out.println(sequence.next());
        }
    }

}
