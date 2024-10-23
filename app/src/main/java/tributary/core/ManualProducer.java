package tributary.core;

public class ManualProducer implements Producer {
    private String id;
    private String allocationMethod;
    private String eventType;
    private Tributary tributary;

    public ManualProducer(String id, String eventType, String allocationMethod, Tributary tributary) {
        this.id = id;
        this.allocationMethod = allocationMethod;
        this.eventType = eventType;
        this.tributary = tributary;
    }

    @Override
    public void produceEvent(Topic topic, Event<?> event, String partitionId) {
        if (!event.getPayloadType().equalsIgnoreCase(eventType)) {
            System.err.println("This producer cannot produce this topic of events.");
            return;
        } else if (!topic.getType().equalsIgnoreCase(event.getPayloadType())) {
            System.err.println("This event type is not the same type of the requested topic.");
            return;
        }
        Partition partition = topic.findPartition(partitionId);
        if (partition != null) {
            tributary.sendEvent(topic, event, partitionId);
        } else {
            System.out.println("ERROR: You must provide a valid partition.");
            return;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getAllocationMethod() {
        return allocationMethod;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

}
