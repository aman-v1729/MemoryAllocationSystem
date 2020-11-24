// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {

    private AVLTree left, right; // Children.
    private AVLTree parent; // Parent pointer.
    private int height; // The height of the subtree

    public AVLTree() {
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root!
        // and left child will always be null.

    }

    public AVLTree(int address, int size, int key) {
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions.
    // Some of the functions may be directly inherited from the BSTree class and
    // nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.

    public void print2DUtil(AVLTree root, int space) {
        // Base case
        if (root == null)
            return;

        // Increase distance between levels
        space += 5;

        // Process right child first
        print2DUtil(root.right, space);

        // Print current node after space
        // count
        System.out.print("\n");
        for (int i = 5; i < space; i++)
            System.out.print(" ");
        System.out.print("(" + root.address + "," + root.size + "," + root.key + ", " + root.height + ")" + "\n");

        // Process left child
        print2DUtil(root.left, space);
    }

    public void print2D() {
        // Pass initial space count as 0
        print2DUtil(this.getRootSentinel(), 0);
    }

    public AVLTree Insert(int address, int size, int key) {
        AVLTree node = this.getRootSentinel();
        while (node != null) {
            if (node.parent == null || node.key < key || (node.key == key && node.address <= address)) {
                if (node.right == null) {
                    node.right = new AVLTree(address, size, key);
                    node.right.parent = node;
                    // node.right.height = 1;
                    node = node.right;
                    checkBalance(node);
                    return node;
                } else {
                    node = node.right;
                }
            } else {
                if (node.left == null) {
                    node.left = new AVLTree(address, size, key);
                    node.left.parent = node;
                    // node.left.height = 1;
                    node = node.left;
                    checkBalance(node);
                    return node;
                } else {
                    node = node.left;
                }
            }
        }
        return null;
    }

    public boolean Delete(Dictionary e) {
        AVLTree node = this;
        AVLTree root = node.getRootSentinel();

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
                            AVLTree succ = node.getNext();
                            AVLTree temp = new AVLTree(succ.address, succ.size, succ.key);
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
                        checkBalance(node.parent);

                        node.address = -1;
                        node.size = -1;
                        node.key = -1;
                        node.parent = null;
                        node.left = null;
                        node.right = null;

                        if (node == this) {
                            node.right = root.right;
                            root.right = null;
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

    // Find(), getFirst(), getNext(): Same as BST
    public AVLTree Find(int k, boolean exact) {
        AVLTree node = this.getRootSentinel();
        if (node.right == null)
            return null;
        node = node.right;
        AVLTree bestNode = null;
        while (node != null) {
            if (node.key == k) {
                bestNode = node;
                node = node.left;
            } else if (node.key < k) {
                node = node.right;
            } else if (node.key > k) {
                if (!exact)
                    bestNode = node;
                node = node.left;
            }
        }
        return bestNode;
    }

    public AVLTree getFirst() {
        AVLTree node = this.getRootSentinel();
        if (node.right == null) {
            return null;
        }
        node = node.right;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public AVLTree getNext() {
        AVLTree node = this;
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
        AVLTree node = this;
        if (node.parent != null) {
            AVLTree first = this;
            AVLTree second = this.parent;
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
        AVLTree root = node;
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
        if (!checkHeightProperty(node))
            return false;
        return true;
    }

    // PRIVATE HELPERS
    private boolean checkPointers(AVLTree node) {
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

    private boolean checkBSTProperty(AVLTree node, int minkey, int minaddress, int maxkey, int maxaddress) {
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

    private boolean checkHeightProperty(AVLTree node) {
        if (node == null)
            return true;
        if (Math.abs(height(node.left) - height(node.right)) > 1)
            return false;
        if ((height(node) != height(node.left) + 1) && (height(node) != height(node.right) + 1))
            return false;
        if (!checkHeightProperty(node.left) || !checkHeightProperty(node.right))
            return false;
        return true;
    }

    private int height(AVLTree node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private boolean isLeftChild() {
        AVLTree node = this;
        AVLTree parent = node.parent;
        if (parent == null)
            return false;
        if (parent.left == node)
            return true;
        else
            return false;
    }

    private AVLTree getRootSentinel() {
        AVLTree node = this;
        while (node.parent != null)
            node = node.parent;
        return node;
    }

    private void checkBalance(AVLTree node) {
        if (node.parent == null) {
            return;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;

        if (Math.abs(height(node.left) - height(node.right)) > 1) {
            node = reBalance(node);
        }
        checkBalance(node.parent);
        return;
    }

    private AVLTree reBalance(AVLTree node) {
        if (height(node.left) - height(node.right) > 1) {
            if (height(node.left.left) > height(node.left.right)) {
                node = rightRotate(node);
            } else {
                node = leftRightRotate(node);
            }
        } else {
            if (height(node.right.left) > height(node.right.right)) {
                node = rightLeftRotate(node);
            } else {
                node = leftRotate(node);
            }
        }
        return node;
    }

    private AVLTree rightRotate(AVLTree node) {
        boolean nodeIsLeftChild = node.isLeftChild();
        AVLTree temp = node.left;
        node.left = temp.right;
        if (node.left != null)
            node.left.parent = node;
        temp.parent = node.parent;
        temp.right = node;
        node.parent = temp;
        if (nodeIsLeftChild) {
            temp.parent.left = temp;
        } else {
            temp.parent.right = temp;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        temp.height = Math.max(height(temp.left), height(temp.right)) + 1;
        return temp;
    }

    private AVLTree leftRotate(AVLTree node) {
        boolean nodeIsLeftChild = node.isLeftChild();
        AVLTree temp = node.right;
        node.right = temp.left;
        if (node.right != null)
            node.right.parent = node;
        temp.parent = node.parent;
        temp.left = node;
        node.parent = temp;
        if (nodeIsLeftChild) {
            temp.parent.left = temp;
        } else {
            temp.parent.right = temp;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        temp.height = Math.max(height(temp.left), height(temp.right)) + 1;
        return temp;
    }

    private AVLTree rightLeftRotate(AVLTree node) {
        node.right = rightRotate(node.right);
        return (leftRotate(node));
    }

    private AVLTree leftRightRotate(AVLTree node) {
        node.left = leftRotate(node.left);
        return (rightRotate(node));
    }

}
