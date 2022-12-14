public class AVLTree extends BST {
    /**
     * Create an empty AVL tree
     */
    public AVLTree() {
    }

    /**
     * Override createNewNode to create an AVLTreeNode
     */
    public AVLTreeNode createNewNode(AVLTreeNode node) {
        AVLTreeNode newNode = new AVLTreeNode(node.getKey());
        newNode.setValues(node.getValues());
        newNode.height = node.height;
        return newNode;
    }

    /**
     * Insert an element and rebalance if necessary
     */
    public boolean insert(AVLTreeNode n) {
        boolean successful = super.insert(n);
        if (!successful)
            return false; // n is already in the tree
        else {
            balancePath(n); // Balance from n to the root if necessary
        }

        return true; // n is inserted
    }

    /**
     * Update the height of a specified node
     */
    private void updateHeight(AVLTreeNode node) {
        if (node.left == null && node.right == null) // node is a leaf
            node.height = 0;
        else if (node.left == null) // node has no left subtree
            node.height = 1 + node.right.height;
        else if (node.right == null) // node has no right subtree
            node.height = 1 + node.left.height;
        else
            node.height = 1 + Math.max(node.right.height, node.left.height);
    }

    /**
     * Balance the nodes in the path from the specified
     * node to the root if necessary
     */
    private void balancePath(AVLTreeNode e) {
        java.util.ArrayList<AVLTreeNode> path = path(e);
        for (int i = path.size() - 1; i >= 0; i--) {
            AVLTreeNode A = path.get(i);
            updateHeight(A);
            AVLTreeNode parentOfA = (A == root) ? null : (path.get(i - 1));
            switch (balanceFactor(A)) {
                case -2:
                    if (balanceFactor(A.left) <= 0) {
                        balanceLL(A, parentOfA); // Perform LL rotation
                    } else {
                        balanceLR(A, parentOfA); // Perform LR rotation
                    }
                    break;
                case +2:
                    if (balanceFactor(A.right) >= 0) {
                        balanceRR(A, parentOfA); // Perform RR rotation
                    } else {
                        balanceRL(A, parentOfA); // Perform RL rotation
                    }
            }
        }
    }

    /**
     * Return the balance factor of the node
     */
    private int balanceFactor(AVLTreeNode node) {
        if (node.right == null) // node has no right subtree
            return -node.height;
        else if (node.left == null) // node has no left subtree
            return +node.height;
        else
            return node.right.height -
                    node.left.height;
    }

    /**
     * Balance LL (see Figure 26.3)
     */
    private void balanceLL(AVLTreeNode A, AVLTreeNode parentOfA) {
        AVLTreeNode B = A.left; // A is left-heavy and B is left-heavy

        if (A == root) {
            root = B;
        } else {
            if (parentOfA.left == A) {
                parentOfA.left = B;
            } else {
                parentOfA.right = B;
            }
        }

        A.left = B.right; // Make T2 the left subtree of A
        B.right = A; // Make A the left child of B
        updateHeight(A);
        updateHeight(B);
    }

    /**
     * Balance LR (see Figure 26.5)
     */
    private void balanceLR(AVLTreeNode A, AVLTreeNode parentOfA) {
        AVLTreeNode B = A.left; // A is left-heavy
        AVLTreeNode C = B.right; // B is right-heavy

        if (A == root) {
            root = C;
        } else {
            if (parentOfA.left == A) {
                parentOfA.left = C;
            } else {
                parentOfA.right = C;
            }
        }

        A.left = C.right; // Make T3 the left subtree of A
        B.right = C.left; // Make T2 the right subtree of B
        C.left = B;
        C.right = A;

        // Adjust heights
        updateHeight(A);
        updateHeight(B);
        updateHeight(C);
    }

    /**
     * Balance RR (see Figure 26.4)
     */
    private void balanceRR(AVLTreeNode A, AVLTreeNode parentOfA) {
        AVLTreeNode B = A.right; // A is right-heavy and B is right-heavy

        if (A == root) {
            root = B;
        } else {
            if (parentOfA.left == A) {
                parentOfA.left = B;
            } else {
                parentOfA.right = B;
            }
        }

        A.right = B.left; // Make T2 the right subtree of A
        B.left = A;
        updateHeight(A);
        updateHeight(B);
    }

    /**
     * Balance RL (see Figure 26.6)
     */
    private void balanceRL(AVLTreeNode A, AVLTreeNode parentOfA) {
        AVLTreeNode B = A.right; // A is right-heavy
        AVLTreeNode C = B.left; // B is left-heavy

        if (A == root) {
            root = C;
        } else {
            if (parentOfA.left == A) {
                parentOfA.left = C;
            } else {
                parentOfA.right = C;
            }
        }

        A.right = C.left; // Make T2 the right subtree of A
        B.left = C.right; // Make T3 the left subtree of B
        C.left = A;
        C.right = B;

        // Adjust heights
        updateHeight(A);
        updateHeight(B);
        updateHeight(C);
    }

    @Override
    /** Delete an element from the binary tree.
     * Return true if the element is deleted successfully
     * Return false if the element is not in the tree */
    public boolean delete(AVLTreeNode e) {
        if (root == null)
            return false; // Element is not in the tree

        // Locate the node to be deleted and also locate its parent node
        AVLTreeNode parent = null;
        AVLTreeNode current = root;
        while (current != null) {
            if (e.compareTo(current) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Element is in the tree pointed by current
        }

        if (current == null)
            return false; // Element is not in the tree

        // Case 1: current has no left children (See Figure 23.6)
        if (current.left == null) {
            // Connect the parent with the right child of the current node
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;

                // Balance the tree if necessary
                balancePath(parent);
            }
        } else {
            // Case 2: The current node has a left child
            // Locate the rightmost node in the left subtree of
            // the current node and also its parent
            AVLTreeNode parentOfRightMost = current;
            AVLTreeNode rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; // Keep going to the right
            }

            // Replace the element in current by the element in rightMost
            current = rightMost;

            // Eliminate rightmost node
            if (parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                // Special case: parentOfRightMost is current
                parentOfRightMost.left = rightMost.left;

            // Balance the tree if necessary
            balancePath(parentOfRightMost);
        }

        size--;
        return true; // Element inserted
    }
}