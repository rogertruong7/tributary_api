package tributary.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Topic {
    private String id;
    private String type;
    private List<Partition> partitions = new ArrayList<>();
    private List<ConsumerGroup> consumerGroups = new ArrayList<>();

    public Topic(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public void addPartition(Partition partition) {
        if (findPartition(partition.getId()) != null) {
            System.out.println("ERROR: Partition not added. Id already exists.");
            return;
        }
        if (!partitions.contains(partition) && partition != null) {
            partitions.add(partition);
            System.out.println("Partition " + partition.getId() + " created in topic " + getId());
        } else {
            System.err.println("ERROR: Partition already exists.");
        }
    }

    public synchronized void sendRandomEvent(Event<?> event) {
        int length = partitions.size();
        Random random = new Random();
        int randomInt = random.nextInt(length);
        partitions.get(randomInt).append(event);
        System.out.println("Added to partition " + partitions.get(randomInt).getId());
    }

    public synchronized void sendManualEvent(Event<?> event, String partitionId) {
        Partition partition = findPartition(partitionId);
        if (partition != null) {
            partition.append(event);
            System.out.println("Added event " + event.getId() + " to partition " + partitionId);
        } else {
            System.err.println("ERROR: Specified Partition is not in the topic");
            return;
        }
    }

    public Partition findPartition(String partitionId) {
        return partitions.stream().filter(p -> p.getId().equals(partitionId))
            .findFirst()
            .orElse(null);
    }

    public void addConsumerGroup(ConsumerGroup group) {
        if (!consumerGroups.contains(group) && group != null) {
            consumerGroups.add(group);
        }
    }

    public void allocateConsumer(Consumer consumer) {
        for (Partition partition: partitions) {
            if (partition.getConsumer() == null) {
                partition.setConsumer(consumer);
                consumer.addPartition(partition);
            }
        }
    }

    public void removeConsumer(Consumer consumer) {
        for (Partition partition: partitions) {
            if (partition.getConsumer().equals(consumer)) {
                partition.setConsumer(null);
            }
        }
    }

    public void showDetails() {
        System.out.println("------------------------");
        System.out.println("Topic: " + getId());
        System.out.println("Partitions: ");
        for (Partition partition: partitions) {
            System.out.println("- " + partition.getId() + " -> " + "Events: ");
            for (Event<?> event: partition.getEvents()) {
                System.out.println("  " + event.toString());
            }
            if (partition.getConsumer() != null) {
                System.out.println("  Connected consumer: " + partition.getConsumer().getConsumerId());
            } else {
                System.out.println("  Connected consumer: null");
            }
        }
        System.out.println("------------------------");
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<ConsumerGroup> getConsumerGroups() {
        return consumerGroups;
    }

}
