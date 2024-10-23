package tributary.cli;

import tributary.api.API;

import java.util.Scanner;

import org.json.JSONObject;

public class TributaryCLI {

    private API api;

    public TributaryCLI() {
        api = new API();
    }

    public static void main(String[] args) {
        TributaryCLI cli = new TributaryCLI();
        cli.start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Welcome to the Tributary CLI!");
        System.out.println("Enter commands to interact with the system. Type 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            handleCommand(command);
        }

        scanner.close();
    }

    private void handleCommand(String command) {
        String[] parts = command.split("\\s+");

        if (parts.length == 0) {
            return;
        }

        switch (parts[0].toLowerCase()) {
            case "create":
                handleCreateCommand(parts);
                break;
            case "delete":
                handleDeleteCommand(parts);
                break;
            case "produce":
                handleProduceCommand(parts);
                break;
            case "consume":
                handleConsumeCommand(parts);
                break;
            case "show":
                handleShowCommand(parts);
                break;
            case "parallel":
                handleParallelCommand(parts, command);
                break;
            default:
                System.err.println("Unknown command: " + parts[0]);
        }
    }

    private void handleCreateCommand(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Invalid create command.");
            return;
        }

        switch (parts[1].toLowerCase()) {
            case "topic":
                if (parts.length == 4) {
                    String topicId = parts[2];
                    String eventType = parts[3];
                    if (!eventType.equalsIgnoreCase("integer") && !eventType.equalsIgnoreCase("string")) {
                        System.out.println("Topic type must be integer or string. Topic not created.");
                        return;
                    }
                    api.createTopic(topicId, eventType);
                } else {
                    System.err.println("Usage: create topic <id> <type>");
                }
                break;
            case "partition":
                if (parts.length == 4) {
                    String topicId = parts[2];
                    String partitionId = parts[3];
                    api.createPartition(topicId, partitionId);
                } else {
                    System.out.println("Usage: create partition <topicId> <partitionId>");
                }
                break;
            case "consumer":
                if (parts.length == 6 && parts[2].equals("group")) {
                    String groupId = parts[3];
                    String topicId = parts[4];
                    String rebalancingMethod = parts[5];
                    api.createConsumerGroup(groupId, topicId, rebalancingMethod);
                } else if (parts.length == 4) {
                    String groupId = parts[2];
                    String consumerId = parts[3];
                    api.createConsumer(groupId, consumerId);
                } else {
                    System.out.println("Usage: create consumer group <id> <topic> <rebalancing> | create consumer "
                    + "<group> <id>");
                }
                break;
            case "producer":
                if (parts.length == 5) {
                    String producerId = parts[2];
                    String eventType = parts[3];
                    String allocation = parts[4];
                    if (!eventType.equalsIgnoreCase("integer") && !eventType.equalsIgnoreCase("string")) {
                        System.out.println("Producer type must be integer or string. Producer not created.");
                        return;
                    }
                    api.createProducer(producerId, eventType, allocation);
                } else {
                    System.out.println("Usage: create producer <id> <type> <allocation>");
                }
                break;
            default:
                System.out.println("Unknown create command: " + parts[1]);
        }
    }

    private void handleDeleteCommand(String[] parts) {
        if (parts.length == 3 && parts[1].equalsIgnoreCase("consumer")) {
            String consumerId = parts[2];
            api.deleteConsumer(consumerId);
        } else {
            System.err.println("Usage: delete consumer <consumerId>");
        }
    }


    private void handleProduceCommand(String[] parts) {
        if (parts.length >= 5 && parts[1].equals("event")) {
            String producerId = parts[2];
            String topicId = parts[3];
            String eventFileName = parts[4];
            String partitionId = parts.length == 6 ? parts[5] : null;

            JSONObject jsonObject = FileLoader.getJSON(eventFileName);
            // Extract the values in the JSON file
            JSONObject headers = jsonObject.getJSONObject("headers");
            String datetimeCreated = headers.getString("datetime_created");
            String id = headers.getString("id");
            String payloadType = headers.getString("payload_type");
            String key = jsonObject.getString("key");
            Object value = jsonObject.get("value");

            api.produceEvent(producerId, topicId, datetimeCreated, id, payloadType, key, value, partitionId);
        } else {
            System.err.println("Usage: produce event <producer> <topic> <eventFilename> [<partition>]");
        }
    }

    private void handleConsumeCommand(String[] parts) {
        if (parts.length >= 4 && parts[1].equals("event")) {
            String consumerId = parts[2];
            String partitionId = parts[3];
            api.consumeEvent(consumerId, partitionId);
        } else if (parts.length == 5 && parts[1].equals("events")) {
            String consumerId = parts[2];
            String partitionId = parts[3];
            int numberOfEvents = Integer.parseInt(parts[4]);
            for (int i = 0; i < numberOfEvents; i++) {
                api.consumeEvent(consumerId, partitionId);
            }
        } else {
            System.err.println("Usage: consume event <consumer> <partition> [<number of events>]");
        }
    }

    private void handleShowCommand(String[] parts) {
        if (parts.length == 3 && parts[1].equalsIgnoreCase("topic")) {
            String topicId = parts[2];
            api.showTopic(topicId);
        } else if (parts.length == 4 && parts[1].equalsIgnoreCase("consumer") && parts[2].equalsIgnoreCase("group")) {
            String groupId = parts[3];
            api.showConsumerGroup(groupId);
        } else {
            System.err.println("Usage: show <topic|consumer group> <id>");
        }
    }

    private void handleParallelCommand(String[] parts, String command) {
        if (parts.length >= 4) {
            if (parts[1].equals("produce")) {
                String[] terms = command.split("\\(");
                for (int i = 1; i < terms.length; i++) {
                    String cleanedString = terms[i].replaceAll("[(),]", "");
                    String[] newParts = cleanedString.split("\\s+");
                    if (newParts.length < 3) {
                        return;
                    }
                    String producerId = newParts[0];
                    String topicId = newParts[1];
                    String eventFileName = newParts[2];
                    String partitionId = newParts.length == 4 ? newParts[3] : null;
                    JSONObject jsonObject = FileLoader.getJSON(eventFileName);

                    // Extract the values in the JSON file
                    JSONObject headers = jsonObject.getJSONObject("headers");
                    String datetimeCreated = headers.getString("datetime_created");
                    String id = headers.getString("id");
                    String payloadType = headers.getString("payload_type");
                    String key = jsonObject.getString("key");
                    Object value = jsonObject.get("value");

                    api.produceEvent(producerId, topicId, datetimeCreated, id, payloadType, key, value, partitionId);
                }
            }
            if (parts[1].equals("consume")) {
                String[] terms = command.split("\\(");
                for (int i = 1; i < terms.length; i++) {
                    String cleanedString = terms[i].replaceAll("[(),]", "");
                    String[] newParts = cleanedString.split("\\s+");
                    String consumerId = newParts[0];
                    String partitionId = newParts[1];

                    api.consumeEvent(consumerId, partitionId);
                }
            }
        } else {
            System.err.println("Usage: parallel <produce|consume> (<arg1>, <arg2>, ...) ...");
        }
    }
}
