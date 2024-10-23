package tributary.core;

import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private String id;
    private List<Partition> partitions = new ArrayList<>();
    private List<Event<?>> consumedEvents = new ArrayList<>();

    public Consumer(String id) {
        this.id = id;
    }

    public synchronized void consumeEvent(String partitionId) {
        Partition partition = findPartition(partitionId);
        if (partition != null && !checkPartitionEmpty(partition)) {
            Event<?> consumedEvent = partition.pop();
            consumedEvents.add(consumedEvent);
            System.out.println("The consumer has received the event " + consumedEvent.getId() + ".");
            System.out.println(consumedEvent.toString());
            System.out.println("-----------------------------");
        } else {
            System.err.println("Partition is not allocated to consumer or partition is empty.");
        }
    }

    public boolean checkPartitionEmpty(Partition partition) {
        if (partition.getEvents().isEmpty()) {
            return true;
        }
        return false;
    }

    public Partition findPartition(String partitionId) {
        return partitions.stream().filter(p -> p.getId().equals(partitionId))
            .findFirst()
            .orElse(null);
    }

    public void addPartition(Partition partition) {
        if (!partitions.contains(partition) && partition != null) {
            partitions.add(partition);
        }
    }

    public String getConsumerId() {
        return id;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }
}
