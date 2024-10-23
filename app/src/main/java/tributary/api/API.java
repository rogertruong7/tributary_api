package tributary.api;

import tributary.core.Core;

public class API {
    private Core core = new Core();
    /**
     * Creates a new topic in the tributary system.
     *
     * @param id        The identifier for the topic.
     * @param eventType The type of events that go through the topic. Can be either Integer or String.
     */
    public void createTopic(String id, String eventType) {
        core.createTopic(id, eventType);
    }

    /**
     * Creates a new partition in the specified topic.
     *
     * @param topicId    The ID of the topic.
     * @param partitionId The identifier for the partition.
     */
    public void createPartition(String topicId, String partitionId) {
        core.createPartition(topicId, partitionId);
    }

    /**
     * Creates a new consumer group with the given identifier and rebalancing method.
     *
     * @param groupId           The identifier for the consumer group.
     * @param topicId           The topic that the consumer group is subscribed to.
     * @param rebalancingMethod The rebalancing method, either "Range" or "RoundRobin".
     */
    public void createConsumerGroup(String groupId, String topicId, String rebalancingMethod) {
        core.createConsumerGroup(groupId, topicId, rebalancingMethod);
    }

    /**
     * Creates a new consumer within the specified consumer group.
     *
     * @param groupId The identifier for the consumer group.
     * @param consumerId The identifier for the consumer.
     */
    public void createConsumer(String groupId, String consumerId) {
        core.createConsumer(groupId, consumerId);
    }

    /**
     * Deletes the consumer with the given identifier.
     *
     * @param consumerId The identifier for the consumer to be deleted.
     */
    public void deleteConsumer(String consumerId) {
        core.deleteConsumer(consumerId);
    }

    /**
     * Creates a new producer which produces events of the given type.
     *
     * @param producerId  The identifier for the producer.
     * @param eventType   The type of events produced by the producer.
     * @param allocation  The allocation method, either "Random" or "Manual".
     */
    public void createProducer(String producerId, String eventType, String allocation) {
        core.createProducer(producerId, eventType, allocation);
    }

    /**
     * Produces a new event from the given producer to the specified topic and partition.
     *
     * @param producerId The identifier for the producer. This must match an existing producer in the system.
     * @param topicId The identifier for the topic to which the event is sent. This must match an existing topic.
     * @param datetimeCreated The datetime when the event was created.
     * @param id The unique identifier for the event.
     * @param payloadType The type of the payload (e.g., "integer" or "string").
     * @param key The key associated with the event
     * @param value The value of the event. This is the actual data carried by the event and should match the payload.
     * @param partitionId (Optional) The identifier for the partition to which the event is sent.
     *                     This is used when the producer's allocation method is manual.
     */
    public void produceEvent(String producerId, String topicId, String datetimeCreated, String id, String payloadType,
    String key, Object value, String partitionId) {
        core.produceEvent(producerId, topicId, datetimeCreated, id, payloadType, key, value, partitionId);
    }


    /**
     * Consumes an event from the specified partition by the given consumer.
     *
     * @param consumerId  The identifier for the consumer.
     * @param partitionId The identifier for the partition.
     */
    public void consumeEvent(String consumerId, String partitionId) {
        core.consumeEvent(consumerId, partitionId);
    }

    /**
     * Displays the details of the specified topic, including all partitions and events.
     *
     * @param topicId The identifier for the topic.
     */
    public void showTopic(String topicId) {
        core.showTopic(topicId);
    }

    /**
     * Displays all consumers in the specified consumer group and their assigned partitions.
     *
     * @param groupId The identifier for the consumer group.
     */
    public void showConsumerGroup(String groupId) {
        core.showConsumerGroup(groupId);
    }
}
