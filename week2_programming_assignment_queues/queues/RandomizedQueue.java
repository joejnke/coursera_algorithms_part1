import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] randQueue;         // array of items
    private int n;            // number of elements on deque
    private int top;            // index of first element in the deque

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.n = 0;
        this.top = 0;
        this.randQueue = (Item[]) new Object[1];

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.n == 0;
    }

    // return the number of items on the randomized queue
    public int size() { return this.n; }

    // resize the array to the given size
    private void resize(int size) {
        // alternative implementation suggested on the sample code given
        this.randQueue = java.util.Arrays.copyOf(this.randQueue, size);
        this.top = this.n - 1;
    }

    // shift every element by one to the left starting from index start and replacing it with the one next to it.
    private void shiftLeft(int start) {
        for (int i = start; i < this.n; i++) {
            if (i+1 == this.randQueue.length) { this.randQueue[i] = null; }
            else  { this.randQueue[i] = this.randQueue[i+1]; }
        }
    }

    // add the item
    public void enqueue(Item item) {

        if (item == null) { throw new IllegalArgumentException("null items can't be enqueued..."); }

        // if queue is empty
        if (this.isEmpty()) {
            this.top = 0;
            this.randQueue[this.top] = item;
            this.n++;
        }

        // if queue is full
        else if (this.n == this.randQueue.length) {
            this.resize(this.randQueue.length * 2);
            this.top = (this.top + 1) % this.randQueue.length;
            this.randQueue[this.top] = item;
            this.n++;
        }

        // if queue is neither full nor empty
        else {
            this.top = (this.top + 1) % this.randQueue.length;
            this.randQueue[this.top] = item;
            this.n++;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) { throw new NoSuchElementException("queue is empty..."); }

        int randIndex = StdRandom.uniform(this.n);
        Item itemToRemove = this.randQueue[randIndex];
        this.shiftLeft(randIndex);
        this.randQueue[this.n - 1] = null; // set the previous top to null
        this.top--;
        this.n--;

        // shrink the array by half if the number of elements is 1/4th of the array size.
        if (this.n <= this.randQueue.length / 4 && this.n > 0) {
            this.resize(this.randQueue.length / 2);
        }

        return itemToRemove;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) { throw new NoSuchElementException("queue is empty..."); }
        return this.randQueue[StdRandom.uniform(this.n)];
    }

    // iterator class
    private class RandqueIterator implements Iterator<Item> {
        private Item[] tempRandQueue = java.util.Arrays.copyOf(randQueue, n);
        private int index;

        public RandqueIterator() {
            StdRandom.shuffle(this.tempRandQueue);
            this.index = 0;
        }

        public boolean hasNext() {
            return this.index != (this.tempRandQueue.length);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("already reached the last element...");
            return tempRandQueue[index++];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandqueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> testRandQueue = new RandomizedQueue<String>();
        System.out.println("isEmpty() working - true expected: " + testRandQueue.isEmpty());
        testRandQueue.enqueue("item 1");
        testRandQueue.enqueue("item 2");
        testRandQueue.enqueue("item 3");
        System.out.println(testRandQueue.dequeue());
        System.out.println("dequeue() working: " + (testRandQueue.size() ==  2));
        System.out.println("size() working: " + (testRandQueue.size() ==  testRandQueue.n));
        testRandQueue.enqueue("item 4");
        testRandQueue.sample();
        System.out.println("isEmpty() working - false expected: " + testRandQueue.isEmpty());

        for (String s : testRandQueue) System.out.println(s);
    }

}