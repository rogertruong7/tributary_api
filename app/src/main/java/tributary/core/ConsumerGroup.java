package tributary.core;

import java.util.ArrayList;
import java.util.List;

public class ConsumerGroup {
    private String id;
    private Topic topic;
    private String rebalancingMethod;
    private List<Consumer> consumers = new ArrayList<>();

    public ConsumerGroup(String id, Topic topic, String rebalancingMethod) {
        this.id = id;
        this.topic = topic;
        this.rebalancingMethod = rebalancingMethod;
    }

    public void addConsumer(Consumer consumer) {
        if (consumers.stream().anyMatch(item -> item.getConsumerId().equals(consumer.getConsumerId()))) {
            System.out.println("Consumer Id already exists");
        }
        if (!consumers.contains(consumer) && consumer != null) {
            consumers.add(consumer);
            topic.allocateConsumer(consumer);
            System.out.println("Consumer " + consumer.getConsumerId() + " added to consumer group " + getId());
        }
    }

    public Consumer getConsumer(String consumerId) {
        return consumers.stream().filter(x -> x.getConsumerId().equals(consumerId)).findFirst().get();
    }

    public void removeConsumer(String consumerId) {
        Consumer consumer = getConsumer(consumerId);
        if (consumer != null) {
            consumers.remove(consumer);
            topic.removeConsumer(consumer);
        }
    }

    public boolean hasCustomer(String consumerId) {
        return consumers.stream().anyMatch(x -> x.getConsumerId().equals(consumerId));
    }

    public void showDetails() {
        System.out.println("------------------------");
        System.out.println("Consumer Group: " + getId());
        System.out.println("List of consumers");
        for (Consumer consumer: consumers) {
            System.out.println("Consumer " + consumer.getConsumerId());
            System.out.println("--> Has partitions: ");
            for (Partition partition: consumer.getPartitions()) {
                System.out.println("   Partition " + partition.getId());
            }
        }
        System.out.println("------------------------");
    }

    public String getId() {
        return id;
    }

    public String getRebalancingMethod() {
        return rebalancingMethod;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }
}
