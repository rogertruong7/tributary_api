package tributary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tributary.cli.TributaryCLI;


public class TributaryTest {
    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    public void testCreateTopicCommand() {
        String input = "create topic hello integer\n"
            + "create topic hello1 WRONG\n"
            + "hello\n"
            + "create topic hello integer\n"
            + "create partition hello partition1\n"
            + "create partition dwadawdwa partition0000\n"
            + "create partition hello partition1\n"
            + "create consumer group cg1 hello lol\n"
            + "create consumer group dwdadwadaw hello lol\n"
            + "create consumer cg1 consumer1\n"
            + "create consumer cg1 consumer2\n"
            + "delete consumer consumer2\n"
            + "show consumer group cg1\n"
            + "show topic hello\n"
            + "delete consumer consumer1\n"
            + "show topic hello\n"
            + "show consumer group cg1\n"
            + "create consumer cg1 newConsumer\n"
            + "create producer lol integer random\n"
            + "create producer lol integer dwadawdawd\n"
            + "produce event lol hello integer_1 partition1\n"
            + "show topic hello\n"
            + "create producer producer1 string manual\n"
            + "produce event producer1 hello integer_1\n"
            + "create topic goodbye string\n"
            + "produce event producer1 goodbye string_1\n"
            + "create partition goodbye partition2\n"
            + "create partition goodbye partition3\n"
            + "create consumer group cg2 goodbye any\n"
            + "create consumer cg2 stringConsumer1\n"
            + "create consumer cgdwadawd2 stringConsumer1\n"
            + "create partition goodbye partition4\n"
            + "create consumer cg2 stringConsumer2\n"
            + "show consumer group cg2\n"
            + "show consumer group dwadawdawdhawuidioa\n"
            + "show topic goodbye"
            + "show topic dwadawdawdawd"
            + "produce event producer1 goodbye string_1 partition3\n"
            + "produce event producer1 goodbye string_2 partition3\n"
            + "produce event producer1 goodbye string_1 partition2\n"
            + "show topic goodbye\n"
            + "produce event producer1 hello string_1 partition2\n"
            + "produce event producerdwadawdawdwa1 hello string_1 partition2\n"
            + "produce event producer1 dwadawdawd string_1 partition2\n"
            + "produce event producer1 goodbye string_1 partition1\nexit\n"
            + "parallel produce (producer1, goodbye, string_1, partition2), (producer1, goodbye,"
            + "string_2, partition2), (producer1, goodbye, string_3, partition2), (producer1, goodbye, string_4,"
            + "partition2), (producer1, goodbye, string_5, partition3), (producer1, goodbye, string_4, partition3),"
            + "(producer1, goodbye, string_3, partition3), (producer1, goodbye, string_2, partition3)\n"
            + "parallel consume (stringConsumer1, partition2), (stringConsumer1, partition2), (stringConsumer1,"
            + " partition3), (stringConsumer1, partition2), (stringConsumer1, partition3), (stringConsumer1,"
            + " partition2), (stringConsumer1, partition3), (stringConsumer1, partition3)\n"
            + "consume event stringConsumer1 partition2"
            + "consume events stringConsumer1 partition3 3\nexit\n";

        simulateUserInput(input);

        TributaryCLI cli = new TributaryCLI();
        cli.start();

        String output = testOut.toString();
        System.out.println(output);
        assertNotNull(output);
    }

    private void simulateUserInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }
}
