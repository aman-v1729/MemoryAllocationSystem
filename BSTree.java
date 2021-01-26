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
            if (node.parent == null || node.key < key || (node.key == key && node.address <= address)) {
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
        BSTree root = node.getRootSentinel();

        if (root.right == null)
            return false;
        node = root.right;

        // Start finding the node to be deleted
        while (node != null) {
            if (node.key < e.key || (node.key == e.key && node.address <= e.address)) {
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
                            } else {
                                node.parent.right = null;
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
                        node.address = -1;
                        node.size = -1;
                        node.key = -1;
                        node.parent = null;
                        node.left = null;
                        node.right = null;

                        if (node == this) {
                            node.right = root.right;
                            root.right = null;
                            if (node.right != null)
                                node.right.parent = node;
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
        if (node.right == null)
            return null;
        node = node.right;
        BSTree bestNode = null;
        while (node != null) {
            if (node.key == key) {
                bestNode = node;
                node = node.left;
            } else if (node.key < key) {
                node = node.right;
            } else if (node.key > key) {
                if (!exact)
                    bestNode = node;
                node = node.left;
            }
        }
        return bestNode;
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
        // if (node.parent == null)
        // return null;
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

    private boolean checkPointers(BSTree node) {
        if (node.parent == null)
            return false;

        if (node.parent.left != node && node.parent.right != node)
            return false;

        if (node.right == null && node.left == null)
            return true;

        if (node.left == node.right)
            return false;

        if (node.left != null) {
            if (node.left.parent != node)
                return false;
            if (!checkPointers(node.left))
                return false;
        }

        if (node.right != null) {
            if (node.right.parent != node)
                return false;
            if (!checkPointers(node.right))
                return false;
        }

        return true;
    }

    private boolean checkBSTProperty(BSTree node, int minkey, int minaddress, int maxkey, int maxaddress) {
        if (node == null)
            return true;
        if (node.key > maxkey || node.key < minkey)
            return false;
        if (node.key == maxkey && node.address >= maxaddress) {
            return false;
        }
        if (node.key == minkey && node.address <= minaddress) {
            return false;
        }
        if (!checkBSTProperty(node.left, minkey, minaddress, node.key, node.address))
            return false;
        if (!checkBSTProperty(node.right, node.key, node.address, maxkey, maxaddress))
            return false;
        return true;
    }

    public boolean sanity() {
        BSTree node = this;
        if (node.parent != null) {
            BSTree first = this;
            BSTree second = this.parent;
            while (second != null && second.parent != null) {
                if (first == second)
                    return false;
                first = first.parent;
                second = second.parent.parent;
            }
        }
        while (node.parent != null) {
            if (node.parent.left != node && node.parent.right != node) {
                return false;
            }
            node = node.parent;
        }
        BSTree root = node;
        if (root.left != null || root.parent != null || root.size != -1 || root.key != -1 || root.address != -1) {
            return false;
        }
        if (root.right == null)
            return true;

        node = root.right;
        if (!checkPointers(node))
            return false;
        if (!checkBSTProperty(node, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE))
            return false;
        return true;
    }

}
