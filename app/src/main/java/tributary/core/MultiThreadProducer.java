package tributary.core;

public class MultiThreadProducer extends Thread {
    private Producer producer;
    private Event<?> event;
    private String partitionId;
    private Topic topic;

    public MultiThreadProducer(Topic topic, Producer producer, Event<?> event, String partitionId) {
        this.producer = producer;
        this.partitionId = partitionId;
        this.event = event;
        this.topic = topic;
    }

    public void run() {
        producer.produceEvent(topic, event, partitionId);
    }
}
