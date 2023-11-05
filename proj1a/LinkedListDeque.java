public class LinkedListDeque<T> {

    private static class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> pre;
    
        public Node(T item, Node<T> pre, Node<T> next) {
            this.item = item;
            this.next = next;
            this.pre = pre;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private Node<T> ptr;

    public LinkedListDeque() {
        head = new Node<T>(null, null, null);
        tail = new Node<T>(null, null, null);
        head.next = tail;
        tail.pre = head;
        ptr = head;
        size = 0;
    }

    public void addFirst(T item)  {
        Node<T> node = new Node<T>(item, head, head.next);
        head.next.pre = node;
        head.next = node;
        size += 1;
    }

    public void addLast(T item) {
        Node<T> node = new Node<T>(item, tail.pre, tail);
        tail.pre.next = node;
        tail.pre = node;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node<T> tmp = head.next;
        while(tmp != tail) {
            System.out.print(tmp.item + " ");
            tmp = tmp.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if(isEmpty())
            return null;
        Node<T> first = head.next;
        T ret = first.item;
        first.next.pre = head;
        head.next = first.next;
        first = null;
        size -= 1;
        return ret;
    }

    public T removeLast() {
        if(isEmpty())
            return null;
        Node<T> last = tail.pre;
        T ret = last.item;
        last.pre.next = tail;
        tail.pre = last.pre;
        last = null;
        size -= 1;
        return ret;
    }

    public T get(int index) {
        if (isEmpty() || index >= size) return null;
        while (index + 1 != 0) {
            ptr = ptr.next;
            index -= 1;
        }
        Node<T> ret = ptr;
        ptr = head;
        return ret.item;
    }

    public T getRecursive(int index) {
        if (isEmpty() || index >= size) return null;
        if (index + 1 == 0) {
            Node<T> ret = ptr;
            ptr = head;
            return ret.item;
        }
        ptr = ptr.next;
        return getRecursive(index - 1);
    }
}