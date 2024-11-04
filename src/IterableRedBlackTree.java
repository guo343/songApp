import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;


/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it stores in
 * sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T>
    implements IterableSortedCollection<T> {

  private Comparable<T> maxIterator = null;
  private Comparable<T> minIterator = null;

  /**
   * Allows setting the start (minimum) value of the iterator. When this method is called, every
   * iterator created after it will use the minimum set by this method until this method is called
   * again to set a new minimum value.
   *
   * @param min the minimum for iterators created for this tree, or null for no minimum
   */
  public void setIteratorMin(Comparable<T> min) {
    this.minIterator = min;
  }

  /**
   * Allows setting the stop (maximum) value of the iterator. When this method is called, every
   * iterator created after it will use the maximum set by this method until this method is called
   * again to set a new maximum value.
   *
   * @param max the maximum for iterators created for this tree, or null for no maximum
   */
  public void setIteratorMax(Comparable<T> max) {
    this.maxIterator = max;
  }

  /**
   * Returns an iterator over the values stored in this tree. The iterator uses the start (minimum)
   * value set by a previous call to setIteratorMin, and the stop (maximum) value set by a previous
   * call to setIteratorMax. If setIteratorMin has not been called before, or if it was called with
   * a null argument, the iterator uses no minimum value and starts with the lowest value that
   * exists in the tree. If setIteratorMax has not been called before, or if it was called with a
   * null argument, the iterator uses no maximum value and finishes with the highest value that
   * exists in the tree.
   */
  public Iterator<T> iterator() {
    return new RBTIterator<>(this.root, this.minIterator, this.maxIterator);
  }

  /**
   * Nested class for Iterator objects created for this tree and returned by the iterator method.
   * This iterator follows an in-order traversal of the tree and returns the values in sorted,
   * ascending order.
   */
  protected static class RBTIterator<R> implements Iterator<R> {

    // stores the start point (minimum) for the iterator
    Comparable<R> min = null;
    // stores the stop point (maximum) for the iterator
    Comparable<R> max = null;
    // stores the stack that keeps track of the inorder traversal
    Stack<BSTNode<R>> stack = null;

    /**
     * Constructor for a new iterator if the tree with root as its root node, and min as the start
     * (minimum) value (or null if no start value) and max as the stop (maximum) value (or null if
     * no stop value) of the new iterator.
     *
     * @param root root node of the tree to traverse
     * @param min  the minimum value that the iterator will return
     * @param max  the maximum value that the iterator will return
     */
    public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
      this.max = max;
      this.min = min;
      stack = new Stack<>();
      buildStackHelper(root);
    }

    /**
     * Helper method for initializing and updating the stack. This method both - finds the next
     * data value stored in the tree (or subtree) that is bigger than or equal to the specified
     * start point (maximum), and - builds up the stack of ancestor nodes that contain values
     * larger than or equal to the start point so that those nodes can be visited in the future.
     *
     * @param node the root node of the subtree to process
     */
    private void buildStackHelper(BSTNode<R> node) {
      //return when node is null
      if (node == null) {
        return;
      }

      //if the current node is smaller than the min, go to check right node
      if (min != null && min.compareTo(node.getData()) > 0) {
        buildStackHelper(node.getRight());
      }

      //the value in the argument node is larger than or equal to min
      if (min == null || min.compareTo(node.getData()) <= 0) {
        stack.push(node);
        buildStackHelper(node.getLeft());
      }

    }

    /**
     * Returns true if the iterator has another value to return, and false otherwise.
     */
    public boolean hasNext() {
      //if stack is empty return false
      if (stack.isEmpty()) {
        return false;
      }
      //if the stack is not empty then there are still elements
      for (BSTNode<R> element : stack) {
        if ((max == null || max.compareTo(
            stack.peek().getData()) >= 0) && (min == null || min.compareTo(
            stack.peek().getData()) <= 0)) {
          return true;
        }
      }
      return false;
    }

    /**
     * Returns the next value of the iterator.
     *
     * @throws NoSuchElementException if the iterator has no more values to return
     */
    public R next() {
      R toReturn;
      //if the iterator has no more values to return then throw NoSuchElementException
      //by hasNext, we already make sure that the next node is larger than min and smaller than max
      if (!hasNext()) {
        throw new NoSuchElementException("the iterator has no more values to return");
      }

      //pop the next node and return the value
      BSTNode<R> node = stack.pop();
      toReturn = node.getData();

      //check the right node. in build method, it would automatically add all left child
      if (node.getRight() != null) {
        buildStackHelper(node.getRight());
      }

      return toReturn;
    }

  }

}
