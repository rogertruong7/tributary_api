package tributary.core;

import java.util.LinkedList;
import java.util.Queue;

public class Partition {
    private String id;
    private Consumer consumer;
    private Queue<Event<?>> events = new LinkedList<>();

    public Partition(String id) {
        this.id = id;
    }

    public synchronized void append(Event<?> event) {
        events.add(event);
    }

    public Event<?> pop() {
        return events.poll();
    }

    public String getId() {
        return id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
        if (consumer != null) {
            System.out.println("Partition " + id + " is now connected to consumer " + consumer.getConsumerId());
        } else {
            System.out.println("Partition " + id + " is now disconnected.");
        }
    }

    public Queue<Event<?>> getEvents() {
        return events;
    }
}
