// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right; // Children.
    private BSTree parent; // Parent pointer.

    public BSTree() {
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root!
        // and left child will always be null.
    }

    public BSTree(int address, int size, int key) {
        super(address, size, key);
    }

    private BSTree getRootSentinel() {
        BSTree node = this;
        while (node.parent != null)
            node = node.parent;
        return node;
    }

    public BSTree Insert(int address, int size, int key) {
        BSTree node = this.getRootSentinel();
        // Now insert
        while (node != null) {
            if (node.parent == null || node.key <= key) {
                if (node.right == null) {
                    node.right = new BSTree(address, size, key);
                    node.right.parent = node;
                    return node.right;
                } else {
                    node = node.right;
                }
            } else {
                if (node.left == null) {
                    node.left = new BSTree(address, size, key);
                    node.left.parent = node;
                    return node.left;
                } else {
                    node = node.left;
                }
            }
        }
        return null;
    }

    public boolean Delete(Dictionary e) {
        BSTree node = this;
        // Check if node to be deleted is this.
        // If node which calls method is to be deleted, replace reference with sentinel.
        if (node.key == e.key && node.address == e.address && node.size == e.size && node.parent != null) {
            BSTree root = node.getRootSentinel();
            if (root.right == node) {
                root.right = node.right;
                root.left = node.left;
                if (root.right != null)
                    root.right.parent = root;
                if (root.left != null)
                    root.left.parent = root;
                root.parent = node;
                root.key = node.key;
                root.address = node.address;
                root.size = node.size;

                node.address = -1;
                node.size = -1;
                node.key = -1;
                node.parent = null;
                node.right = root;
                node.left = null;
            } else {
                BSTree rootRight = root.right;
                root.left = node.left;
                root.right = node.right;
                if (root.left != null)
                    root.left.parent = root;
                if (root.right != null)
                    root.right.parent = root;
                root.key = node.key;
                root.address = node.address;
                root.size = node.size;
                root.parent = node.parent;
                if (node.isLeftChild())
                    node.parent.left = root;
                else
                    node.parent.right = root;
                node.size = -1;
                node.address = -1;
                node.key = -1;
                node.left = null;
                node.right = rootRight;
                node.parent = null;
                rootRight.parent = node;
            }
        }

        // Go to root
        node = node.getRootSentinel();
        if (node.right == null)
            return false;
        node = node.right;

        // Start finding the node to be deleted
        while (node != null) {
            if (node.key <= e.key) {
                if (node.key == e.key) {
                    if (node.address == e.address && node.size == e.size) {
                        // Node found. Delete.

                        if (node.right != null && node.left != null) {
                            BSTree succ = node.getNext();
                            BSTree temp = new BSTree(succ.address, succ.size, succ.key);
                            succ.address = node.address;
                            succ.size = node.size;
                            succ.key = node.key;
                            node.address = temp.address;
                            node.size = temp.size;
                            node.key = temp.key;
                            node = succ;
                        }
                        boolean nodeIsLeftChild = node.isLeftChild();
                        if (node.left == null && node.right == null) {
                            if (nodeIsLeftChild) {
                                node.parent.left = null;
                                node = null;
                            } else {
                                node.parent.right = null;
                                node = null;
                            }
                        } else if (node.left == null) {
                            if (nodeIsLeftChild) {
                                node.parent.left = node.right;
                                node.right.parent = node.parent;
                            } else {
                                node.parent.right = node.right;
                                node.right.parent = node.parent;
                            }
                        } else if (node.right == null) {
                            if (nodeIsLeftChild) {
                                node.parent.left = node.left;
                                node.left.parent = node.parent;
                            } else {
                                node.parent.right = node.left;
                                node.left.parent = node.parent;
                            }
                        }
                        return true;
                    }
                }
                // Not found. Go right. If right null - node absent.
                node = node.right;
            } else {
                // Go left. If left null - node absent.
                node = node.left;
            }
        }
        return false;

    }

    public BSTree Find(int key, boolean exact) {
        BSTree node = this.getRootSentinel();
        node = node.right;

        while (node != null) {
            if (node.key > key && exact) {
                node = node.left;
            } else if (node.key > key && !exact) {
                return node;
            } else if (node.key < key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    public BSTree getFirst() {
        BSTree node = this.getRootSentinel();
        if (node.right == null) {
            return null;
        }
        node = node.right;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private boolean isLeftChild() {
        BSTree node = this;
        BSTree parent = node.parent;
        if (parent == null)
            return false;
        if (parent.left == node)
            return true;
        else
            return false;
    }

    public BSTree getNext() {
        BSTree node = this;
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }
        while (node.parent != null) {
            if (node.isLeftChild())
                return node.parent;
            else
                node = node.parent;
        }
        return null;
    }

    public boolean sanity() {

        return true;
    }

}
