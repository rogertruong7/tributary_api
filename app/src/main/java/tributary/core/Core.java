package tributary.core;

import java.util.ArrayList;
import java.util.List;

public class Core {
    private Tributary tributary = new Tributary();
    private List<Producer> producers = new ArrayList<>();
    private ComponentFactory factory = new ComponentFactory();

    public void createTopic(String id, String eventType) {
        tributary.addTopic(factory.createTopic(id, eventType));
    }

    public void createPartition(String topicId, String partitionId) {
        Topic topic = tributary.getTopic(topicId);
        if (topic != null) {
            Partition partition = factory.createPartition(partitionId);
            topic.addPartition(partition);
        } else {
            System.err.println("Topic with ID " + topicId + " does not exist.");
        }
    }

    public void createConsumerGroup(String groupId, String topicId, String rebalancingMethod) {
        Topic topic = tributary.getTopic(topicId);
        if (topic != null) {
            ConsumerGroup group = factory.createConsumerGroup(groupId, topic, rebalancingMethod);
            tributary.addConsumerGroup(group, topic);
            System.out.println("Consumer group " + groupId + " created for topic " + topicId);
        } else {
            System.err.println("Topic with ID " + topicId + " does not exist.");
        }
    }

    public void createConsumer(String groupId, String consumerId) {
        ConsumerGroup group = tributary.getConsumerGroup(groupId);
        if (group != null) {
            Consumer consumer = factory.createConsumer(consumerId);
            tributary.addConsumer(consumer, group);
        } else {
            System.err.println("Consumer group with ID " + groupId + " does not exist.");
        }
    }

    public void deleteConsumer(String consumerId) {
        tributary.removeConsumer(consumerId);
    }

    public void createProducer(String id, String eventType, String allocation) {
        Producer producer = factory.createProducer(id, eventType, allocation, tributary);
        if (producer != null && !checkProducerIdExists(id)) {
            producers.add(producer);
            System.out.println("Producer added.");
        } else {
            System.err.println("Producer was not created.");
        }
    }

    public void produceEvent(String producerId, String topicId, String datetimeCreated, String id, String payloadType,
    String key, Object value, String partitionId) {
        Producer producer = findProducerById(producerId);
        Topic topic = tributary.getTopic(topicId);
        if (producer != null && topic != null) {
            MultiThreadProducer productThread = new MultiThreadProducer(topic, producer,
            factory.createEvent(datetimeCreated, id, payloadType, key, value), partitionId);
            productThread.start();
        } else {
            System.err.println("Producer or topic not found.");
        }
    }

    public void consumeEvent(String consumerId, String partitionId) {
        Consumer consumer = tributary.findConsumer(consumerId);
        if (consumer != null) {
            MultiThreadConsumer consumerThread = new MultiThreadConsumer(consumer, partitionId);
            consumerThread.start();
        } else {
            System.err.println("Consumer does not exist.");
        }
    }

    public void showTopic(String topicId) {
        Topic topic = tributary.getTopic(topicId);
        if (topic != null) {
            topic.showDetails();
        } else {
            System.err.println("Topic with ID " + topicId + " not found.");
        }
    }

    public void showConsumerGroup(String groupId) {
        ConsumerGroup group = tributary.getConsumerGroup(groupId);
        if (group != null) {
            group.showDetails();
        } else {
            System.err.println("Consumer group with ID " + groupId + " not found.");
        }
    }

    // Helper methods
    private Producer findProducerById(String producerId) {
        return producers.stream().filter(p -> p.getId().equals(producerId)).findFirst().orElse(null);
    }

    private boolean checkProducerIdExists(String id) {
        for (Producer producer: producers) {
            if (producer.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
