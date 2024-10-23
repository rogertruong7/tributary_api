package tributary.core;

public interface Producer {
    public void produceEvent(Topic topic, Event<?> event, String partitionId);

    public String getId();

    public String getAllocationMethod();

    public String getEventType();
}
