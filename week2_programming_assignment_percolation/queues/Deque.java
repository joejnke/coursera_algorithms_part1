import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] deque;         // array of items
    private int n;            // number of elements on deque
    private int first;            // index of first element in the deque
    private int last;            // index of last element in the deque
    // private int capacity;       // size of the array

    // construct an empty deque
    public Deque() {
        this.n = 0;
        this.first = 0;
        this.last = 0;
        this.deque = (Item[]) new Object[1];
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.n == 0;
    }

    // return the number of items on the deque
    public int size() { return this.n; }

    // resize the array to the given size
    private void resize(int size) {
        // alternative implementation suggested on the sample code given
        this.deque = java.util.Arrays.copyOf(this.deque, size);
    }

    // sifht the underlying array holding the elements to the right corner of the same array
    private void shiftRight() {
        // Item[] temp = java.util.Arrays.copyOf(this.deque, this.deque.length); // to be removed
        for (int j = this.deque.length-1, i = this.last; j >= 0; i--, j--) {
            if (i >= this.first) {
                this.deque[j]	 = this.deque[i];
            }
            else {
                this.deque[j] = null;
            }
        }

        // this.deque = temp;
        this.first = this.deque.length - this.n;
        this.last = this.deque.length-1;
    }

    // sifht the underlying array holding the elements to the left corner of the same array
    private void shiftLeft() {
        // Item[] temp = java.util.Arrays.copyOf(this.deque, this.deque.length); // to be removed
        for (int j = 0, i = this.first; j < this.deque.length; i++, j++) {
            if (i <= this.last) {
                this.deque[j]	 = this.deque[i];
            }
            else {
                this.deque[j] = null;
            }
        }

        // this.deque = temp;
        this.first = 0;
        this.last = this.n - 1;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // if the deque is full, resize the deque before adding item
        if (this.n == this.deque.length) { //(this.first + (this.capacity - 1)) % this.capacity == this.last) {
            this.resize(this.deque.length * 2);
            this.shiftRight();

            // copy the items to the right corner of the array

            this.first = ((this.first + (this.deque.length - 1)) % this.deque.length);
            this.deque[this.first] = item;
            this.n++;
        }

        // if deque is empty, then put the element in the Deque and keep both first and last refere to it.
        else  if (this.isEmpty()) {
            this.deque[this.first] = item;
            this.n++;
        }

        else {
            if (((this.first + (this.deque.length - 1)) % this.deque.length) >= this.last) {
                this.shiftRight();
            }
            this.first = ((this.first + (this.deque.length - 1)) % this.deque.length);
            this.deque[this.first] = item;
            this.n++;
        }

    }

    // add the item to the back
    public void addLast(Item item) {
        // if the deque is full, resize the deque before adding item
        if (this.n == this.deque.length) { //(this.first + (this.capacity - 1)) % this.capacity == this.last) {
            this.resize(this.deque.length * 2);
            this.shiftLeft();
            this.last = (this.last + 1) % this.deque.length;
            this.deque[this.last] = item;
            this.n++;
        }

        else  if (this.isEmpty()) {
            this.deque[this.last] = item;
            this.n++;
        }

        else {
            if ((this.last + 1) % this.deque.length <= this.first) {
                this.shiftLeft();
            }
            this.last = (this.last + 1) % this.deque.length;
            this.deque[this.last] = item;
            this.n++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // check if the deque is empty
        if (this.isEmpty()) throw new NoSuchElementException("deque is already empty...");

        Item itemToRemove = this.deque[this.first];
        this.deque[this.first] = null;
        this.n--;
        this.first = (this.first + 1) % this.deque.length;

        // shrink the array by half if the number of elements is 1/4th of the array size.
        if (this.n <= this.deque.length / 4 && this.n > 0) {
            this.resize(this.deque.length / 2);
        }

        return itemToRemove;
    }

    // remove and return the item from the back
    public Item removeLast() {
        // check if deque is empty
        if (this.isEmpty()) throw new NoSuchElementException("deque is already empty...");

        Item itemToRemove = this.deque[this.last];
        this.deque[this.last] = null;
        this.n--;
        this.last = (this.last + (this.deque.length - 1)) % this.deque.length;

        // shrink the array by half if the number of elements is 1/4th of the array size.
        if (this.n == this.deque.length / 4 && this.n > 0) {
            this.resize(this.deque.length);
        }

        return itemToRemove;
    }

    // iterator class
    private class DequeIterator implements Iterator<Item> {
        private int itFirst = first;
        private int remainingItems = n;
        public boolean hasNext() {
            return remainingItems != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("already reached the last element...");
            Item itemToRemove = deque[itFirst];
            itFirst = (itFirst + 1) % deque.length;
            remainingItems--;
            return itemToRemove;
        }
    }
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)

    public static void main(String[] args) {
        System.out.println("############ Unit est ###########");
        Deque<String> testDeque = new Deque<String>();
        testDeque.addFirst("first item");
        System.out.println("size() working: " + (testDeque.size() == 1));
        System.out.println("removeFirst() working: " + ("first item".equals(testDeque.removeFirst())));
        testDeque.addLast("last item");
        System.out.println("removeLast() working: " + ("last item".equals(testDeque.removeLast())));
        System.out.println("isEmpty() working: " + testDeque.isEmpty());

        for (int i = 1; i <= 2; i++) {
            testDeque.addFirst("first item " + i);
            testDeque.addLast("last item " + i);
        }

        // test the iterator
        System.out.println("############ iterator test ###########");
        Iterator<String> dequeItr = testDeque.iterator();
        while (dequeItr.hasNext()) {
            System.out.println(dequeItr.next());
        }

        // for (String s : testDeque) System.out.println(s);
        // System.out.println(testDeque.removeFirst());
        // System.out.println("capacity: " + testDeque.deque.length);
        // test the Deque constructor
        // System.out.printf("Passed %10s\n", "Test");
        // System.out.printf(" %-7b assignment of n\n", testDeque.n == 0);
        // System.out.printf(" %-7b assignment of first\n", testDeque.first == 0);
        // System.out.printf(" %-7b assignment of last\n", testDeque.last == 0);
        // System.out.printf(" %-7b assignment of capacity\n", testDeque.capacity == 1);
        // System.out.printf(" %-7b assignment of deque array\n", testDeque.deque != null);
        //
        // test isEmpty()
        // System.out.printf(" %-7b isEmpty()\n", testDeque.isEmpty());
        // System.out.printf(" %-7b size()\n", testDeque.size() == testDeque.n);

    }

}