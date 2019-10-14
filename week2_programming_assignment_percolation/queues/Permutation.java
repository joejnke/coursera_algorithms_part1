 /* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args[0] == null) { throw new IllegalArgumentException("integer value k expected..."); }
        int k = Integer.parseInt(args[0]);
        System.out.println(k);
        RandomizedQueue<String> pqueue = new RandomizedQueue<String>();
        System.out.println(pqueue.isEmpty());

        for (int i = 0; i < k; i++) {
            pqueue.enqueue(StdIn.readString());
        }

        for (String s : pqueue) System.out.println(s);

    }
}
