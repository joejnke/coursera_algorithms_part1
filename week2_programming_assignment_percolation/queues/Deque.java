import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] deque;         // array of items
    private int n;            // number of elements on deque
    private int first;            // index of first element in the deque
    private int last;            // index of last element in the deque
    private int capacity;       // size of the array

    // construct an empty deque
    public Deque() {
        this.n = 0;
        this.first = 0;
        this.last = 0;
        this.capacity = 1;
        this.deque = (Item[]) new Object[this.capacity];
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.n;
    }

    // resize the array to the given size
    private void resize(int size) {
        // alternative implementation suggested on the sample code given
        this.deque = java.util.Arrays.copyOf(this.deque, size);
    }

    // add the item to the front
    public void addFirst(Item item) {
        // if the deque is full, resize the deque before adding item
        if ((this.first + (this.capacity - 1)) % this.capacity == this.last) {
            this.capacity = this.capacity * 2;
            this.resize(this.capacity);
            this.first = ((this.first + (this.capacity - 1)) % this.capacity);
            this.deque[this.first] = item;
        }

        else {
            this.first = ((this.first + (this.capacity - 1)) % this.capacity);
            this.deque[this.first] = item;
        }

    }

    // add the item to the back
    public void addLast(Item item) {
        // if the deque is full, resize the deque before adding item
        if ((this.last + 1) % this.capacity == this.first) {
            this.capacity = this.capacity * 2;
            this.resize(this.capacity);
            this.last = (this.last + 1) % this.capacity;
            this.deque[this.last] = item;
        }

        else {
            this.last = (this.last + 1) % this.capacity;
            this.deque[this.last] = item;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // check if the deque is empty
        if (this.isEmpty()) throw new NoSuchElementException("deque is already empty...");

        Item itemToRemove = this.deque[this.first];
        this.deque[this.first] = null;
        this.first = (this.first + 1) % this.capacity;
        return itemToRemove;
    }

    // remove and return the item from the back
    public Item removeLast() {
        // check if deque is empty
        if (this.isEmpty()) throw new NoSuchElementException("deque is already empty...");

        Item itemToRemove = this.deque[this.last];
        this.deque[this.last] = null;
        this.last = (this.last + (this.capacity - 1)) % this.capacity;
        return itemToRemove;
    }

    // iterator class
    private class DequeIterator implements Iterator<Item> {
        private int itFirst = first;
        public boolean hasNext() {
            return itFirst != last;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("already reached the last element...");
            Item itemToRemove = deque[itFirst];
            itFirst = (itFirst + 1) % capacity;
            return itemToRemove;
        }
    }
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}