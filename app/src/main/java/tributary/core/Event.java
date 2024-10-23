package tributary.core;

public class Event<T> {
    private String datetimeCreated;
    private String id;
    private String payloadType;
    private String key;
    private T value;

    public Event(String datetimeCreated, String id, String payloadType, String key, T value) {
        this.datetimeCreated = datetimeCreated;
        this.id = id;
        this.payloadType = payloadType;
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Event{"
                + "datetimeCreated='" + datetimeCreated + '\''
                + ", id='" + id + '\''
                + ", payloadType='" + payloadType + '\''
                + ", key='" + key + '\''
                + ", value=" + value
                + '}';
    }
}
