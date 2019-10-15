 /* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

 public class Permutation {
    public static void main(String[] args) {
        if (args[0] == null) { throw new IllegalArgumentException("integer value k expected..."); }
        int k = Integer.parseInt(args[0]);
        // System.out.println(k);
        RandomizedQueue<String> pqueue = new RandomizedQueue<String>();
        // System.out.println(pqueue.isEmpty());

        while (!StdIn.isEmpty()) {
            pqueue.enqueue(StdIn.readString());
        }

        Iterator<String> dequeItr = pqueue.iterator();
        for (int i = 0; i < k && dequeItr.hasNext(); i++) {
            System.out.println(dequeItr.next());
        }

        // for (int i = 0; i < k && ; i++) {
        //     System.out.println(pqueue.sample());
        //     // pqueue.enqueue(StdIn.readString());
        // }

        // for (String s : pqueue) System.out.println(s);

    }
}
