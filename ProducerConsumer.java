package Semaphores_Mutex;

import java.util.concurrent.Semaphore;

class SharedBuffer {
    static final int BUFFER_SIZE = 5;
    int[] buffer = new int[BUFFER_SIZE];
    int in = 0, out = 0;

    // Semaphores for synchronization
    Semaphore mutex = new Semaphore(1);        // acts as Mutex
    Semaphore empty = new Semaphore(BUFFER_SIZE); // counting semaphore for empty slots
    Semaphore full = new Semaphore(0);           // counting semaphore for filled slots

    // Producer puts item into the buffer
    public void produce(int item) throws InterruptedException {
        empty.acquire();   // wait if no empty slots
        mutex.acquire();   // enter critical section

        buffer[in] = item;
        System.out.println("Producer produced: " + item);
        in = (in + 1) % BUFFER_SIZE;

        mutex.release();   // leave critical section
        full.release();    // signal that buffer has more full slots
    }

    // Consumer takes item from buffer
    public int consume() throws InterruptedException {
        full.acquire();    // wait if buffer is empty
        mutex.acquire();   // enter critical section

        int item = buffer[out];
        System.out.println("Consumer consumed: " + item);
        out = (out + 1) % BUFFER_SIZE;

        mutex.release();   // leave critical section
        empty.release();   // signal that buffer has empty slot
        return item;
    }
}

class Producer extends Thread {
    SharedBuffer buffer;
    public Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int item = 0;
        try {
            while (true) {
                buffer.produce(item);
                item++;
                Thread.sleep(500); // simulate production time
            }
        } catch (InterruptedException e) {
            System.out.println("Producer interrupted.");
        }
    }
}

class Consumer extends Thread {
    SharedBuffer buffer;
    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(1000); // simulate consumption time
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer interrupted.");
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer();

        Producer p1 = new Producer(buffer);
        Consumer c1 = new Consumer(buffer);

        p1.start();
        c1.start();
    }
}



