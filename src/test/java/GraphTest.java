import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mavensample.Graph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphTest {
    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
    }

   /* @After
    public void tearDown() throws IOException {
        // Cleanup generated output files
        Files.deleteIfExists(Paths.get("output1.txt"));
        Files.deleteIfExists(Paths.get("output2.txt"));
    }*/

    @Test
    public void unittest1() throws IOException {
        testFeature1("dot_files/input1.dot", "expected_files/expected1.txt", "output1.txt");
    }

    @Test
    public void unittest2() throws IOException {
        testFeature1("dot_files/color.dot", "expected_files/expected2.txt", "output2.txt");
    }

    @Test
    public void unittest3() {
        testInsertNode();
    }

    @Test
    public void unittest4() throws IOException {
        testInsertNodeWithDotGraphInitialization("dot_files/input1.dot");
    }

    @Test
    public void unittest5() {
        testInsertMultipleNodes();
    }

    @Test
    public void unittest6() throws IOException {
        testInsertMultipleNOdesWithDotGraphInitialization("dot_files/input1.dot");
    }

    @Test
    public void unittest7() {
        testInsertEdge();
    }

    private void testFeature1(String dotFilePath, String expectedFilePath, String outputFilePath) throws IOException {
        graph.parseGraph(dotFilePath);

        // Print graph details (for debugging)
        System.out.println(graph.toString());

        // Output to file
        graph.outputGraph(outputFilePath);

        String expected = Files.readString(Paths.get(expectedFilePath))
                .replaceAll("\r\n", "\n")
                .trim();

        String output = Files.readString(Paths.get(outputFilePath))
                .replaceAll("\r\n", "\n")
                .trim();

        Assert.assertEquals("File contents are not the same:\nExpected:\n" + expected + "\n\nActual:\n" + output, expected, output);
    }

    private void testInsertNode() {
        graph.addNode("A");
        Assert.assertEquals(1, graph.getNodeCount());
        graph.addNode("A");
        Assert.assertEquals(1, graph.getNodeCount());
    }

    private void testInsertMultipleNodes() {
        graph.addNodes(new String[]{"a", "b", "c"});
        Assert.assertEquals(3, graph.getNodeCount());
        graph.addNodes(new String[]{"b", "a", "z"});
        Assert.assertEquals(4, graph.getNodeCount());
    }

    private void testInsertNodeWithDotGraphInitialization(String dotFilePath)  throws IOException {
        graph.parseGraph(dotFilePath);
        graph.addNode("a");
        Assert.assertEquals(8, graph.getNodeCount());
        graph.addNode("b");
        Assert.assertEquals(8, graph.getNodeCount());
        graph.addNode("z");
        Assert.assertEquals(9, graph.getNodeCount());
    }

    private void testInsertMultipleNOdesWithDotGraphInitialization(String dotFilePath) throws IOException {
        graph.parseGraph(dotFilePath);
        graph.addNodes(new String[]{"a", "b", "c"});
        Assert.assertEquals(8, graph.getNodeCount());
        graph.addNodes(new String[]{"a", "b", "z"});
        Assert.assertEquals(9, graph.getNodeCount());
    }

    private void testInsertEdge() {
        graph.addEdge("A", "B");
        Assert.assertEquals(2, graph.getNodeCount());
        Assert.assertEquals(1, graph.getEdgeCount());
        graph.addEdge("A", "B");
        Assert.assertEquals(2, graph.getNodeCount());
        Assert.assertEquals(1, graph.getEdgeCount());
        graph.addEdge("B", "C");
        Assert.assertEquals(3, graph.getNodeCount());
        Assert.assertEquals(2, graph.getEdgeCount());
        graph.addEdge("D", "F");
        Assert.assertEquals(5, graph.getNodeCount());
        Assert.assertEquals(3, graph.getEdgeCount());
        graph.addEdge("G", "C");
        Assert.assertEquals(6, graph.getNodeCount());
        Assert.assertEquals(4, graph.getEdgeCount()); 
    }
}

