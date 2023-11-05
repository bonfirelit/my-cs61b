public class ArrayDeque<T> {
    private T[] arr;
    private int size;
    private int front;
    private int rear;
    private int len;

    public ArrayDeque() {
        arr = (T[]) new Object[9];
        front = 0;
        rear = 0;
        size = 8;
        len = 0;
    }

    private double getUsage() {
        return 1.0 * len / size;
    }

    private boolean isFull() {
        return len == size;
    }

    private T[] longer() {
        T[] resized = (T[]) new Object[size * 2 + 1];
        int i, j, cnt;
        i = j =front;
        cnt = 0;
        while (cnt < len) {
            resized[j] = arr[i];
            i = (i + 1) % arr.length;
            j = (j + 1) % resized.length;
            cnt += 1;
        }
        rear = j;
        size *= 2;
        return resized;
    }

    private T[] shorter() {
        int newsize = 4 * len;
        T[] resized = (T[]) new Object[newsize + 1];
        int i, j, cnt;
        i = front;
        j = cnt = 0;
        while (cnt < len) {
            resized[j] = arr[i];
            i = (i + 1) % arr.length;
            j += 1;
            cnt += 1;
        }
        front = 0;
        rear = j;
        size = newsize;
        return resized;
    }

    public void addFirst(T item) {
        if (isFull()) {
            arr = longer();
        }
        front -= 1;
        if (front < 0) front += (size + 1);
        arr[front] = item;
        len += 1;
    }

    public void addLast(T item) {
        if (isFull()) {
            arr = longer();
        }
        arr[rear] = item;
        rear = (rear + 1) % (size + 1);
        len += 1;
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public int size() {
        return len;
    }

    public void printDeque() {
        if (isEmpty()) {
            return ;
        }
        int cnt = 0;
        for (int i = front; cnt < len; cnt += 1, i = (i + 1) % (size + 1)) {
            System.out.print(arr[i] + " ");
        }
    }

    public T removeFirst() {
        if (isEmpty()) return null;
        T ret = arr[front];
        front = (front + 1) % (size + 1);
        len -= 1;
        if (size >= 16 && getUsage() < 0.25) {
            arr = shorter();
        }
        return ret;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        rear -= 1;
        if (rear < 0) rear += (size + 1);
        T ret = arr[rear];
        len -= 1;
        if (size >= 16 && getUsage() < 0.25) {
            arr = shorter();
        }
        return ret;
    }

    public T get(int index) {
        if (isEmpty() || index >= len) return null;
        return arr[(index + front) % arr.length];
    }

}