import org.mavensample.DotGraphToDirectedGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphTest {
    @org.junit.Test
    public void testFunc() throws IOException {
        String dotFilePath = "C:/CSE_464_Course_Project_Part_1/CSE_464_Course_Project_Part_1_Sample/src/main/java/org/mavensample/input.dot";
        String outputFilePath = "output.txt";

        DotGraphToDirectedGraph graphParser = new DotGraphToDirectedGraph();
        graphParser.parseGraph(dotFilePath);

        // Print graph details
        System.out.println(graphParser.toString());

        // Output to file
        graphParser.outputGraph(outputFilePath);

        String expected = Files.readString(Paths.get("expected.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        System.out.println("Expected: " + expected);

// Read the content of the output file
        String output = Files.readString(Paths.get("output.txt"))
                .replaceAll("\r\n", "\n") // Normalize line endings
                .trim(); // Remove leading/trailing whitespace

        System.out.println("Output: " + output);

    }
}
