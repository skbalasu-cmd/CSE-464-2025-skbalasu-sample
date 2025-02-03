import org.junit.Assert;
import org.mavensample.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphTest {
    @org.junit.Test
    public void unittest1() throws IOException {
        String dotFilePath = "input.dot";
        String outputFilePath = "output.txt";

        Graph graphParser = new Graph();
        graphParser.parseGraph(dotFilePath);

        // Print graph details
        System.out.println(graphParser.toString());

        // Output to file
        graphParser.outputGraph(outputFilePath);

        String expected = Files.readString(Paths.get("expected1.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        System.out.println("Expected: " + expected);

// Read the content of the output file
        String output = Files.readString(Paths.get("output.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        System.out.println("Output: " + output);

        Assert.assertEquals("The file contents are not the same!", expected, output);
    }

}
