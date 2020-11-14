// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List next; // Next Node
    private A1List prev; // Previous Node

    public A1List(int address, int size, int key) {
        super(address, size, key);
    }

    public A1List() {
        super(-1, -1, -1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1, -1, -1); // Intiate the tail sentinel

        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key) {
        A1List node = new A1List(address, size, key);
        node.next = this.next;
        node.prev = this;
        node.next.prev = node;
        node.prev.next = node;
        return node;
    }

    public boolean Delete(Dictionary d) {
        A1List node = this;
        if (node.key == d.key && node.address == d.address && node.size == d.size && node.next != null
                && node.prev != null) {
            node.key = node.next.key;
            node.address = node.next.address;
            node.size = node.next.size;
            node.next = node.next.next;
            return true;
        }

        A1List prevNode = this;

        while (prevNode.prev != null) {
            if (prevNode.key == d.key && prevNode.address == d.address && prevNode.size == d.size) {
                prevNode.prev.next = prevNode.next;
                prevNode.next.prev = prevNode.prev;
                prevNode = null;
                return true;
            } else {
                prevNode = prevNode.prev;
            }
        }

        A1List nextNode = this;

        while (nextNode.next != null) {
            if (nextNode.key == d.key && nextNode.address == d.address && nextNode.size == d.size) {
                nextNode.prev.next = nextNode.next;
                nextNode.next.prev = nextNode.prev;
                nextNode = null;
                return true;
            } else {
                nextNode = nextNode.next;
            }
        }
        return false;
    }

    public A1List Find(int k, boolean exact) {
        if (exact) {
            A1List node = this;

            if (node.key == k && node.prev != null && node.next != null) {
                return node;
            }

            A1List prevNode = node;
            while (prevNode.prev != null) {
                if (prevNode.key == k) {
                    return prevNode;
                } else {
                    prevNode = prevNode.prev;
                }
            }

            A1List nextNode = this;
            while (nextNode.next != null) {
                if (nextNode.key == k) {
                    return nextNode;
                } else {
                    nextNode = nextNode.next;
                }
            }
            return null;
        } else {
            A1List node = this;

            if (node.key >= k) {
                return node;
            }

            A1List prevNode = this;
            while (prevNode.prev != null) {
                if (prevNode.key >= k) {
                    return prevNode;
                } else {
                    prevNode = prevNode.prev;
                }
            }

            A1List nextNode = this;
            while (nextNode.next != null) {
                if (nextNode.key >= k) {
                    return nextNode;
                } else {
                    nextNode = nextNode.next;
                }
            }
            return null;
        }
    }

    public A1List getFirst() {
        A1List node = this;
        if (node.prev == null)
            node = node.next;

        while (node.prev.prev != null)
            node = node.prev;
        if (node.next == null)
            return node.prev;
        return node;
    }

    public A1List getNext() {
        A1List node = this;
        if (node.next == null || node.next.next == null)
            return null;
        return node.next;
    }

    public boolean sanity() {
        // Circular checks
        A1List first = this;

        A1List second = first.next;
        while (second != null && second.next != null) {
            if (first == second) {
                return false;
            }
            first = first.next;
            second = second.next.next;
        }

        first = this;
        second = first.prev;
        while (second != null && second.prev != null) {
            if (first == second) {
                return false;
            }
            first = first.prev;
            second = second.prev.prev;
        }

        // Check prev, next links
        A1List node = this;
        while (node.prev != null)
            node = node.prev;
        if (node.key != -1 || node.size != -1 || node.address != -1)
            return false;
        if (node.next.prev != node)
            return false;
        node = node.next;

        if (node.prev.prev != null)
            return false;

        while (node.next != null) {
            if (node.prev.next != node)
                return false;
            if (node.next.prev != node)
                return false;
            node = node.next;
        }
        if (node.prev.next != node)
            return false;
        if (node.key != -1 || node.size != -1 || node.address != -1)
            return false;
        return true;
    }

}
