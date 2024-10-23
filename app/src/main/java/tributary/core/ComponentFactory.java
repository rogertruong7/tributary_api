package tributary.core;

public class ComponentFactory {
    public Topic createTopic(String id, String eventType) {
        return new Topic(id, eventType);
    }

    public Partition createPartition(String id) {
        return new Partition(id);
    }

    public Event<?> createEvent(String datetimeCreated, String id, String payloadType, String key, Object value) {
        switch (payloadType) {
            case "integer":
                return new Event<Integer>(datetimeCreated, id, payloadType, key, (Integer) value);
            case "string":
                return new Event<String>(datetimeCreated, id, payloadType, key, (String) value);
            // Add more cases as needed for different types
            default:
                System.err.println("Other types of events are not yet implemented in this API.");
                return null;
        }
    }

    public Producer createProducer(String id, String eventType, String allocationMethod, Tributary tributary) {
        switch (allocationMethod.toLowerCase()) {
            case "random":
                return new RandomProducer(id, eventType, allocationMethod.toLowerCase(), tributary);
            case "manual":
                return new ManualProducer(id, eventType, allocationMethod.toLowerCase(), tributary);
            default:
                System.out.println("Producer allocation must be random or manual. Producer not created.");
                return null;
        }
    }

    public Consumer createConsumer(String id) {
        return new Consumer(id);
    }

    public ConsumerGroup createConsumerGroup(String groupId, Topic topic, String rebalancingMethod) {
        return new ConsumerGroup(groupId, topic, rebalancingMethod);
    }
}
