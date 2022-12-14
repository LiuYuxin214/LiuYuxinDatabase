import java.util.ArrayList;

public class BST {
    protected TreeNode root;
    protected int size = 0;

    /**
     * Create a default binary tree
     */
    public BST() {
    }


    public TreeNode createNewNode(TreeNode node) {
        TreeNode newNode = new TreeNode(node.getKey());
        newNode.setValues(node.getValues());
        return newNode;
    }

    /**
     * Returns true if the element is in the tree
     */
    public TreeNode search(TreeNode e) {
        TreeNode current = root; // Start from the root

        while (current != null) {
            if (e.compareTo(current) < 0) {
                current = current.left;
            } else if (e.compareTo(current) > 0) {
                current = current.right;
            } else // element matches current
                return current; // Element is found
        }

        return null;
    }


    /**
     * Insert element into the binary tree
     * Return true if the element is inserted successfully
     */
    public boolean insert(TreeNode n) {
        if (root == null)
            root = createNewNode(n); // Create a new root
        else {
            // Locate the parent node
            TreeNode parent = null;
            TreeNode current = root;
            while (current != null)
                if (n.compareTo(current) < 0) {
                    parent = current;
                    current = current.left;
                } else if (n.compareTo(current) > 0) {
                    parent = current;
                    current = current.right;
                } else
                    return false; // Duplicate node not inserted

            // Create the new node and attach it to the parent node
            if (n.compareTo(parent) < 0)
                parent.left = createNewNode(n);
            else
                parent.right = createNewNode(n);
        }

        size++;
        return true; // Element inserted successfully
    }


    /**
     * Inorder traversal from the root
     */
    public ArrayList<TreeNode> inorder() {
        ArrayList<TreeNode> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }

    /**
     * Inorder traversal from a subtree
     */
    protected void inorder(TreeNode root, ArrayList<TreeNode> list) {
        if (root == null) return;
        inorder(root.left, list);
        list.add(root);
        inorder(root.right, list);
    }

    public int getSize() {
        return size;
    }

    public java.util.ArrayList<TreeNode> path(TreeNode e) {
        java.util.ArrayList<TreeNode> list = new java.util.ArrayList<>();
        TreeNode current = root; // Start from the root
        while (current != null) {
            list.add(current); // Add the node to the list
            if (e.compareTo(current) < 0) {
                current = current.left;
            } else if (e.compareTo(current) > 0) {
                current = current.right;
            } else
                break;
        }

        return list; // Return an array list of nodes
    }

    /**
     * Delete an element from the binary tree.
     * Return true if the element is deleted successfully
     * Return false if the element is not in the tree
     */
    public boolean delete(TreeNode n) {
        // Locate the node to be deleted and also locate its parent node
        TreeNode parent = null;
        TreeNode current = root;
        while (current != null) {
            if (n.compareTo(current) < 0) {
                parent = current;
                current = current.left;
            } else if (n.compareTo(current) > 0) {
                parent = current;
                current = current.right;
            } else
                break; // Element is in the tree pointed at by current
        }

        if (current == null)
            return false; // Element is not in the tree

        // Case 1: current has no left child
        if (current.left == null) {
            // Connect the parent with the right child of the current node
            if (parent == null) {
                root = current.right;
            } else {
                if (n.compareTo(parent) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;
            }
        } else {
            // Case 2: The current node has a left child
            // Locate the rightmost node in the left subtree of
            // the current node and also its parent
            TreeNode parentOfRightMost = current;
            TreeNode rightMost = current.left;

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
                // Special case: parentOfRightMost == current
                parentOfRightMost.left = rightMost.left;
        }

        size--;
        return true; // Element deleted successfully
    }

    /**
     * Remove all elements from the tree
     */
    public void clear() {
        root = null;
        size = 0;
    }
}