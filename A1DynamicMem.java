// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {

    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return;
    }

    // In A1, you need to implement the Allocate and Free functions for the class
    // A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only
    // (A1List.java).

    public int Allocate(int blockSize) {
        if (blockSize < 1)
            return -1;
        Dictionary block = freeBlk.Find(blockSize, false);
        if (block != null) {
            if (block.size > blockSize) {
                freeBlk.Insert(block.address + blockSize, block.size - blockSize, block.size - blockSize);
            }
            allocBlk.Insert(block.address, blockSize, block.address);
            int blockAdd = block.address;
            freeBlk.Delete(block);
            return blockAdd;
        }
        return -1;
    }

    public int Free(int startAddr) {
        if (startAddr < 0)
            return -1;
        Dictionary block = allocBlk.Find(startAddr, true);
        if (block != null) {
            int blockSize = block.size;
            freeBlk.Insert(block.address, block.size, block.size);
            allocBlk.Delete(block);
            return 0;
        }
        return -1;
    }
}