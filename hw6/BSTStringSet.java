import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Implementation of a BST based String Set.
 * @author Sally Peng
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        // FIXME: PART A
        _root = put(s, _root);
    }

    /** Helper method for put, at specific Node n. */
    private Node put(String s, Node n) {
        if (n == null) {
            return new Node(s);
        } else {
            if (s.compareTo(n.s) > 0) {
                n.right = put(s, n.right);
            }
            else if (s.compareTo(n.s) < 0){
                n.left = put(s, n.left);
            }
            return n;
        }
    }

    @Override
    public boolean contains(String s) {
        // FIXME: PART A
        return contains(s, _root);
    }

    /** helper method for contains, at specific node n. */
    private boolean contains(String s, Node n) {
        if (n == null) {
            return false;
        }
        else if (n.s.equals(s)) {
            return true;
        } else {
            if (s.compareTo(n.s) > 0) {
                return contains(s, n.right);
            } else {
                return contains(s, n.left);
            }
        }
    }



    @Override
    public List<String> asList() {
        // FIXME: PART A
        List<String> toReturn = new ArrayList<>();
        if (_root == null) {
            return toReturn;
        }
        asList(_root, toReturn);
        return toReturn;
    }

    /** Helper method for asList, at specific node n. */
    private void asList(Node n, List<String> toReturn) {
        if (n == null) {
            return;
        }
        else {
            if (n.left != null) {
                asList(n.left, toReturn);
            }
            toReturn.add(n.s);
            if (n.right != null) {
                asList(n.right, toReturn);
            }
        }
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B

    private static class BSTIter implements Iterator<String> {

        BSTIter(Node node, String low, String high) {
            _low = low;
            _high = high;
            _toDo = new Stack<>();
            addTree(node);
        }

        public void addTree(Node n) {
            while (n != null && n.s.compareTo(_low) >= 0) {
                _toDo.push(n);
                n = n.left;
            }
            if (n != null) {
                addTree(n.right);
            }
        }

        @Override
        public boolean hasNext() {
            return !_toDo.isEmpty() && _toDo.peek().s.compareTo(_high) <= 0;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node n = _toDo.pop();
            addTree(n.right);
            return n.s;
        }

        private String _low;
        private String _high;
        private Stack<Node> _toDo;

    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        // FIXME: PART B
        return new BSTIter(_root, low, high);
    }


    /** Root node of the tree. */
    private Node _root;
}
