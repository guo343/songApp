//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    BinarySearchTree
// Course:   CS 400  F2024
//
// Author:   Yuhan Guo
// Email:    guo343@wisc.edu
// Lecturer: Gray Dahl
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons:         No
// Online Sources:  No
//
///////////////////////////////////////////////////////////////////////////////


/**
 * This class creates a BST and the user can add, search, clear elements from it
 *
 * @param <T> Any types that are comparable
 * @author Yuhan Guo
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  protected BSTNode<T> root;

  /**
   * Performs the naive binary search tree insert algorithm to recursively insert the provided
   * newNode (which has already been initialized with a data value) into the provided tree/subtree.
   * When the provided subtree is null, this method does nothing.
   */
  protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
    //if the tree is empty, create the root in the insert method
    if (subtree == null) {
      return;
    }
    //if the data of the new node is bigger, go the check the right side of tree
    if (newNode.getData().compareTo(subtree.getData()) > 0) {
      //if the tree doesn't have a right child, then insert the new node here
      if (subtree.getRight() == null) {
        subtree.setRight(newNode);
        newNode.setUp(subtree);
      } else {
        //if the tree has a right child, then use a recursion to continue checking
        insertHelper(newNode, subtree.getRight());
      }
      //if the data of the new node is smaller, go the check the left side of tree
    } else {
      //if the tree doesn't have a left child, then insert the new node here
      if (subtree.getLeft() == null) {
        subtree.setLeft(newNode);
        newNode.setUp(subtree);
        //if the tree has a left child, then use a recursion to continue checking
      } else {
        insertHelper(newNode, subtree.getLeft());
      }
    }
  }

  /**
   * Inserts a new data value into the sorted collection.
   *
   * @param data the new value being inserted
   * @throws NullPointerException if data argument is null, we do not allow null values to be
   *                              stored within a SortedCollection
   */
  @Override
  public void insert(T data) throws NullPointerException {
    //if data is null, then throw exceptions
    if (data == null) {
      throw new NullPointerException("data is null");
    }
    //if the tree is empty, then the new node is the root
    if (root == null) {
      root = new BSTNode<>(data);
      return;
    }
    insertHelper(new BSTNode<>(data), root);
  }


  /**
   * Check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  @Override
  public boolean contains(Comparable<T> data) {
    //if the tree is empty, then the tree cannot contain the data
    if (root == null) {
      return false;
    }
    return containsHelper(data, root);
  }

  /**
   * Help check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @param node check from this node
   * @return true if the collection contains data one or more times, and false otherwise
   */

  private boolean containsHelper(Comparable<T> data, BSTNode<T> node) {
    //finding the data, return true
    if (node.getData().equals(data)) {
      return true;
      //if the data is bigger than the current node, go to check the right part
    } else if (data.compareTo(node.getData()) > 0) {
      if (node.getRight() != null) {
        return containsHelper(data, node.getRight());
      }

      //if the data is smaller than the current node, go to check the left part
    } else {
      if (node.getLeft() != null) {
        return containsHelper(data, node.getLeft());
      }

    }
    return false;
  }

  /**
   * return the elements number of this tree
   *
   * @return the elements number of this tree
   */
  @Override
  public int size() {
    //if the tree is empty, then return 0
    if (root == null) {
      return 0;
    }
    return sizeHelper(root);
  }

  /**
   * Help count the elements number of this tree
   *
   * @param node count from this node
   * @return the number of elements in this subtree
   */
  private int sizeHelper(BSTNode<T> node) {
    //if there is no more node, add 0 to the result
    if (node == null) {
      return 0;
    }
    // adding the current node to the result and counting the left and right part
    return 1 + sizeHelper(node.getRight()) + sizeHelper(node.getLeft());
  }

  /**
   * To check whether the tree is empty
   *
   * @return true if the tree is empty, and false otherwise
   */
  @Override
  public boolean isEmpty() {
    //if root == null then the tree is empty
    if (root == null) {
      return true;
    }
    return false;
  }

  /**
   * delete all the elements from this tree
   */
  @Override
  public void clear() {
    root = null;
  }
}

