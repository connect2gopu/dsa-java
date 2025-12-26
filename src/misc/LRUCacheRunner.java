import java.util.Map;
import java.util.HashMap;

class LRUCache {
    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head;
    private final Node tail;
    private int size;

    public LRUCache(int capacity){
        if(capacity <= 0){
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.capacity = capacity;
        this.size = 0;
        this.map = new HashMap<>();

        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);

        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    // private helper methods

    private void addNodeToFront(Node node){
        if(node != null){
            head.next.prev = node;
            node.next = head.next;
            
            head.next = node;
            node.prev = head;
        }
    }

    private void removeNode(Node node){
        // head a b tail
        if(node != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    private Node evictLRUFromLast(){
        Node lruNode = tail.prev;
        removeNode(lruNode);
        return lruNode;
    }

    private void moveToFront(Node node){

        // check its a valid node
        if(node != null){
            // remove node from its position
            removeNode(node);
            addNodeToFront(node);
        }
    }

    public int get(int key){
        // get the node from cache
        Node node = map.get(key);

        // check if this key is present in cache
        if(node == null) {
            return -1;
        }

        // move node to head
        // make it MRU
        moveToFront(node);
        return node.value;
    }

    public void set(int key, int value){
        // check if node already exist
        Node foundNode = this.map.get(key);

        // if node dont exist
        if(foundNode == null){
            // create a cache node
            Node newNode = new Node(key, value);

            // check capacity
            // if capacity is full
            if(this.size >= this.capacity){
                // evict LRU
                Node lruNode = evictLRUFromLast();
                this.map.remove(lruNode.key);

                // addToFront
                addNodeToFront(newNode);
                this.map.put(key, newNode);
                
            } else {
            // else
                // addToFront
                addNodeToFront(newNode);

                this.size += 1;
                this.map.put(key, newNode);
            }

        } else {
        // else
            // set new value
            foundNode.value = value;
            // moveToFront
            moveToFront(foundNode);
        }
    }
}

public class LRUCacheRunner {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.set(1, 10);
        cache.set(2, 20);
        System.out.println(cache.get(1)); // 10

        cache.set(3, 30); // evicts key 2
        System.out.println(cache.get(2)); // -1

        cache.set(1, 100); // update
        System.out.println(cache.get(1)); // 100
    }
}
