public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation.
   *
   * @param newRedNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RBTNode<T> newRedNode) {
    if (newRedNode == null) {
      return;
    }
    //if newRedNode is root
    if (root == newRedNode) {
      if (newRedNode.isRed()) {
        newRedNode.flipColor();
        return;
      }
      return;
    }

    RBTNode<T> superNode = newRedNode.getUp();
    //if newRedNode is the child of root
    if (superNode == root) {
      ensureRedProperty(superNode);
      return;
    }

    RBTNode<T> grandNode = superNode.getUp();
    //if super node is a left child
    if (!superNode.isRightChild()) {
      //case2: super is red and uncle is black or null
      if ((superNode.isRed() && grandNode.getRight() == null) || (superNode.isRed() && !grandNode.getRight()
          .isRed())) {
        //current node is left. then rotate to right
        if (!newRedNode.isRightChild()) {
          rotate(superNode, grandNode);
          superNode.flipColor();
          grandNode.flipColor();
          ensureRedProperty(grandNode);
        } else {
          //current node is right
          rotate(newRedNode, superNode);
          rotate(newRedNode, grandNode);
          newRedNode.flipColor();
          grandNode.flipColor();
          ensureRedProperty(grandNode);
        }
      } else if (superNode.isRed() && grandNode.getRight().isRed()) {
        //case1: super is red and uncle is red
        superNode.flipColor();
        grandNode.getRight().flipColor();
        grandNode.flipColor();
        ensureRedProperty(grandNode);
      }

      //if super node is a right child
    } else {
      // current node is a right node

      // case2: super is red and uncle is black or null
      if ((superNode.isRed() && grandNode.getLeft() == null) || (superNode.isRed() && !grandNode.getLeft()
          .isRed())) {
        //current node is a right child
        if (newRedNode.isRightChild()) {
          rotate(superNode, grandNode);
          superNode.flipColor();
          grandNode.flipColor();
          ensureRedProperty(grandNode);
          //current child is the left child
        } else {
          rotate(newRedNode, superNode);
          rotate(newRedNode, grandNode);
          newRedNode.flipColor();
          grandNode.flipColor();
          ensureRedProperty(grandNode);
        }

      } else if (superNode.isRed() && grandNode.getLeft().isRed()) {
        //case1: super is red and uncle is red
        superNode.flipColor();
        grandNode.getLeft().flipColor();
        grandNode.flipColor();
        ensureRedProperty(grandNode);
      }

    }

  }

  @Override
  public void insert(T data) throws NullPointerException {
    //if data is null, then throw exceptions
    if (data == null) {
      throw new NullPointerException("data is null");
    }

    //if the tree is empty, then the new node is the root. the node is red.
    RBTNode<T> toBeInsert = new RBTNode<>(data);

    if (root == null) {
      root = toBeInsert;
      toBeInsert.flipColor();
    } else {
      super.insertHelper(toBeInsert, root);
      ensureRedProperty(toBeInsert);
    }

  }

}

