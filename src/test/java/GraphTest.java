import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mavensample.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphTest {
    @org.junit.Test
    public void unittest1() throws IOException {
        String dotFilePath = "dot_files/input1.dot";
        String outputFilePath = "output1.txt";

        Graph graphParser = new Graph();
        graphParser.parseGraph(dotFilePath);

        // Print graph details
        System.out.println(graphParser.toString());

        // Output to file
        graphParser.outputGraph(outputFilePath);

        String expected = Files.readString(Paths.get("expected_files/expected1.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        //System.out.println("Expected: " + expected);

// Read the content of the output file
        String output = Files.readString(Paths.get("output1.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        //System.out.println("Output: " + output);

        Assert.assertEquals("The file contents are not the same!", expected, output);
    }

    @Test
    public void unittest2() throws IOException {
        String dotFilePath = "dot_files/color.dot";
        String outputFilePath = "output2.txt";

        Graph graphParser = new Graph();
        graphParser.parseGraph(dotFilePath);

        // Print graph details
        System.out.println(graphParser.toString());

        // Output to file
        graphParser.outputGraph(outputFilePath);

        String expected = Files.readString(Paths.get("expected_files/expected2.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        //System.out.println("Expected: " + expected);

// Read the content of the output file
        String output = Files.readString(Paths.get("output2.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        //System.out.println("Output: " + output);

        Assert.assertEquals("The file contents are not the same!", expected, output);
    }

}
