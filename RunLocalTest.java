import org.junit.Test;
import org.junit.After;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.Timeout;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import static org.junit.Assert.*;

/**
 *
 * A framework to run public test cases.
 *
 * <p>Purdue University -- CS18000 -- Spring 2025</p>
 *
 * @author Purdue CS
 * @version Feb 07, 2025
 */

public class RunLocalTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        System.out.printf("Test Count: %d.\n", result.getRunCount());
        if (result.wasSuccessful()) {
            System.out.printf("Excellent - all local tests ran successfully.\n");
        } else {
            System.out.printf("Tests failed: %d.\n",result.getFailureCount());
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     *
     * A framework to run public test cases.
     *
     * <p>Purdue University -- CS18000 -- Spring 2025</p>
     *
     * @author Purdue CS
     * @version Feb 07, 2025
     */

    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;
        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;
        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;
        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }
        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }
        private String getOutput() {
            return testOut.toString();
        }
        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void testOne() {

            // Set the input
            String input = "penguins" + System.lineSeparator() +
                    "red wings" + System.lineSeparator() +
                    "04-02,03-09,06-05,01-00,02-00,04-01,03-05" + System.lineSeparator() +
                    "02-00,01-04,02-02,00-00,00-00,02-00,01-02" + System.lineSeparator();

            // Pair the input with the expected result
            String expected = "Welcome!\nEnter the name of team 1.\nEnter the name of team 2.\n" +
                    "Enter hockey scores for seven games.\nEnter the number of power play goals for both teams in each game.\n" +
                    "The penguins won the series by a score of 5-2\nThe penguins scored 23 total goals\nThe red wings scored 22 total goals\n" +
                    "The penguins scored 8 power play goals\nThe red wings scored 8 power play goals\nThe penguins scored 15 standard goals\n" +
                    "The red wings scored 14 standard goals\nThe penguins recorded 2 shutouts\nThe red wings recorded 0 shutouts\n" +
                    "The maximum number of goals scored was 9 by the red wings in game 2\n";

            // Runs the program with the input values
            receiveInput(input);
            HockeyScores.main(new String[0]);

            // Retrieves the output from the program
            String output = getOutput();

            // Trims the output and verifies it is correct.
            output = output.replace("\r\n", "\n");
            assertEquals("Ensure that your results match the format of the ones given in the handout!",
                    expected.trim(), output.trim());

        }
    }
}
