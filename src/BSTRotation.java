/**
 * This class allows BSTs to rotate
 *
 * @param <T> any comparable type
 * @author Yuhan Guo
 */
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * Find nodes and return
   *
   * @param node node to be found
   * @return nodes in the tree
   */
  public BSTNode<T> findNode(BSTNode<T> node) {
    //if the tree is empty or the node to be found is null, then the tree cannot contain the data
    if (root == null || node == null) {
      return null;
    }
    return findNodeHelper(node, root);
  }

  /**
   * help find the data in the tree
   *
   * @param data wants to find
   * @param node the subtree
   * @return the node in the tree if found the data
   */
  private BSTNode<T> findNodeHelper(BSTNode<T> data, BSTNode<T> node) {
    //if found the data, return true
    if (node.getData().equals(data.getData())) {
      return node;
      //if the data is bigger than the current node, go to check the right part
    } else if (data.getData().compareTo(node.getData()) > 0) {
      if (node.getRight() != null) {
        return findNodeHelper(data, node.getRight());
      }

      //if the data is smaller than the current node, go to check the left part
    } else {
      if (node.getLeft() != null) {
        return findNodeHelper(data, node.getLeft());
      }

    }
    return null;
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided
   * child is a left child of the provided parent, this method will perform a right rotation. When
   * the provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will
   * either throw a NullPointerException: when either reference is null, or otherwise will throw an
   * IllegalArgumentException.
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException     when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent nodes are not initially
   *                                  (pre-rotation) related that way
   */
  protected void rotate(BSTNode<T> child, BSTNode<T> parent)
      throws NullPointerException, IllegalArgumentException {

    // if parent and child are null
    if (child == null || parent == null) {
      throw new NullPointerException("reference is null");
    }

    child = findNode(child);
    parent = findNode(parent);
    //if parent or child are not contained in the tree
    if (this.findNode(child) == null || this.findNode(parent) == null) {
       throw new IllegalArgumentException("no element");
    }

    //if the child is the root
    if (child.getUp() == null) {
      throw new IllegalArgumentException("not a child");
    }

    //if they are not parent and child
    if (!child.getUp().getData().equals(parent.getData())) {
      throw new IllegalArgumentException("not parent and child");
    }

    rotateHelper(child, parent);
  }

  /**
   * Help BST to rotate
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   */
  private void rotateHelper(BSTNode<T> child, BSTNode<T> parent) {
    BSTNode<T> temp;
    //child is bigger than parent, then rotate to left
    if (child.getData().compareTo(parent.getData()) > 0) {
      temp = child.getLeft();
      //if parent is not root which means parent has up
      if (parent.getUp() != null) {
        //at left part of subtree, then update the left reference of grand node
        if (parent.getUp().getData().compareTo(child.getData()) > 0) {
          parent.getUp().setLeft(child);
          //at right part of subtree, then update the right reference of grand node
        } else {
          parent.getUp().setRight(child);
        }
        child.setUp(parent.getUp());
      } else {
        // if the parent is root then update the new root
        root = child;
        child.setUp(null);
      }
      parent.setUp(child);
      child.setLeft(parent);
      parent.setRight(temp);
      //child is smaller than parent, then rotate to right
    } else {
      temp = child.getRight();
      if (parent.getUp() != null) {
        if (parent.getUp().getData().compareTo(child.getData()) > 0) {
          parent.getUp().setLeft(child);
        } else {
          parent.getUp().setRight(child);
        }
        child.setUp(parent.getUp());
      } else {
        root = child;
        child.setUp(null);
      }
      parent.setUp(child);
      child.setRight(parent);
      parent.setLeft(temp);
    }
  }

}
