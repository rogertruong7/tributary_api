package tributary.core;

public class MultiThreadConsumer extends Thread {
    private Consumer consumer;
    private String partitionId;

    public MultiThreadConsumer(Consumer consumer, String partitionId) {
        this.consumer = consumer;
        this.partitionId = partitionId;
    }

    public void run() {
        consumer.consumeEvent(partitionId);
    }
}
