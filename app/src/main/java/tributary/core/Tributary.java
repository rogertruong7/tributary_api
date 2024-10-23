package tributary.core;

import java.util.ArrayList;
import java.util.List;

public class Tributary {
    private List<Topic> topics = new ArrayList<>();
    private List<ConsumerGroup> consumerGroups = new ArrayList<>();

    public synchronized void sendEvent(Topic topic, Event<?> event) {
        topic.sendRandomEvent(event);
    }

    public synchronized void sendEvent(Topic topic, Event<?> event, String partitionId) {
        topic.sendManualEvent(event, partitionId);
    }

    public void addConsumerGroup(ConsumerGroup group, Topic topic) {
        topic.addConsumerGroup(group);
        consumerGroups.add(group);
    }

    public ConsumerGroup getConsumerGroup(String id) {
        return consumerGroups.stream().filter(x -> x.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public Consumer findConsumer(String consumerId) {
        for (ConsumerGroup group : consumerGroups) {
            if (group.hasCustomer(consumerId)) {
                return group.getConsumer(consumerId);
            }
        }
        return null;
    }

    public void addConsumer(Consumer consumer, ConsumerGroup group) {
        group.addConsumer(consumer);
    }

    public void removeConsumer(String consumerId) {
        for (ConsumerGroup group : consumerGroups) {
            if (group.hasCustomer(consumerId)) {
                group.removeConsumer(consumerId);
                System.out.println("Consumer " + consumerId + " removed from group " + group.getId());
                return;
            }
        }
    }

    public void addTopic(Topic topic) {
        if (topic != null) {
            topics.add(topic);
            System.out.println("Topic " + topic.getId() + " was added successfully.");
        }
    }

    public Topic getTopic(String id) {
        return topics.stream().filter(x -> x.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<ConsumerGroup> getConsumerGroups() {
        return consumerGroups;
    }

}
