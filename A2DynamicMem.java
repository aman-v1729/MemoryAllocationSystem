// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {

    public A2DynamicMem() {
        super();
    }

    public A2DynamicMem(int size) {
        super(size);
    }

    public A2DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    // In A2, you need to test your implementation using BSTrees and AVLTrees.
    // No changes should be required in the A1DynamicMem functions.
    // They should work seamlessly with the newly supplied implementation of BSTrees
    // and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test
    // using BSTrees and AVLTrees.

    public void Defragment() {
        Dictionary node = freeBlk.getFirst();

        // 1. Create a new BST/AVT Tree indexed by address. Use AVL/BST depending on the
        // type.
        Tree temp;
        if (type == 2)
            temp = new BSTree();
        else if (type == 3)
            temp = new AVLTree();
        else
            return;
        // 2. Traverse all the free blocks and add them to the tree indexed by address
        while (node != null) {
            temp.Insert(node.address, node.size, node.address);
            node = node.getNext();
        }

        // 3. Find the first block in the new tree (indexed by address) and then find
        // the next block
        Tree temp1 = temp.getFirst();
        if (temp1 == null)
            return;
        Tree temp2 = temp.getNext();
        // System.out.println("-------------------------------------------------------------");

        // 4. If the two blocks are contiguous, then
        // 4.1 Merge them into a single block
        // 4.2 Remove the free blocks from the free list and the new dictionary
        // 4.3 Add the merged block into the free list and the new dictionary
        // 5. Continue traversing the new dictionary

        while (temp2 != null) {
            if (temp2.address == temp1.address + temp1.size) {
                Tree t1 = new BSTree(temp1.address, temp1.size, temp1.key);
                Tree t2 = new BSTree(temp2.address, temp2.size, temp2.key);

                freeBlk.Delete(new BSTree(t1.address, t1.size, t1.size));
                freeBlk.Delete(new BSTree(t2.address, t2.size, t2.size));
                freeBlk.Insert(t1.address, t1.size + t2.size, t1.size + t2.size);
                // Tree t = temp.Insert(temp1.address, temp1.size + temp2.size, temp1.address);

                temp.Delete(t1);
                temp.Delete(t2);

                Tree t = temp.Insert(t1.address, t1.size + t2.size, t1.address);

                temp1 = t;
                temp2 = temp1.getNext();
            } else {
                temp1 = temp2;
                temp2 = temp1.getNext();
            }
        }

        // 6. Once the traversal is complete, delete the new dictionary
        // System.out.println("-------------------------------------------------------------");
        // ((BSTree) freeBlk).print2D();
        // System.out.println("-------------------------------------------------------------");
        // ((BSTree) temp).print2D();

        temp = null;
        return;
    }
}