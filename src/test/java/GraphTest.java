import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mavensample.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

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

    @Test
    public void unittest8() throws IOException  {
        testFeature4("dot_files/input1.dot");
    }

    @Test
    public void unittest9() {
        testRemoveNode();
    }

    @Test
    public void unittest10() {
        testRemoveNodes();
    }

    @Test
    public void unittest11() {
        testRemoveEdge();
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

    public boolean compareImages(String expectedFilePath, String actualFilePath) throws IOException {
        BufferedImage imgA = ImageIO.read(new File(expectedFilePath));
        BufferedImage imgB = ImageIO.read(new File(actualFilePath));

        if (imgA == null || imgB == null) {
            System.out.println("One or both images could not be loaded.");
            return false;
        }

        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();
        int totalPixels = width * height;
        int mismatchedPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    mismatchedPixels++;
                }
            }
        }

        // Allow a small tolerance
        double mismatchPercentage = (double) mismatchedPixels / totalPixels;
        return mismatchPercentage < 0.01; // Allow 1% difference
    }


    private void testFeature4(String dotFilePath) throws IOException {
        String basePath = Paths.get("").toAbsolutePath().toString();
        String expectedDotFile = basePath + "/expected_files/expected_output.dot";
        String actualDotFile = basePath + "/output.dot";
        String expectedPngFile = basePath + "/expected_files/expected_output.png";
        String actualPngFile = basePath + "/output.png";
        graph.parseGraph(dotFilePath);
        graph.addEdge("b", "g");
        graph.addNode("z");
        graph.outputDOTGraph("output.dot");
        String expected = Files.readString(Paths.get("expected_files/expected_output.dot"))
                .replaceAll("\r\n", "\n")
                .trim();

        String output = Files.readString(Paths.get("output.dot"))
                .replaceAll("\r\n", "\n")
                .trim();
        Assert.assertEquals(expected, output);
        graph.outputGraphics("output.png", "png");

        boolean imagesAreEqual = compareImages(expectedPngFile, actualPngFile);
        Assert.assertTrue("The expected output image and the actual output image do not match.", imagesAreEqual);
    }

    private void testRemoveNode() {
        try {
            graph.removeNode("A");
        } catch (Exception e) {
            System.out.println("graph does not have node to remove.");
        }
        graph.addNode("A");
        Assert.assertEquals(1, graph.getNodeCount());
        graph.removeNode("A");
        Assert.assertEquals(0, graph.getNodeCount());
    }

    private void testRemoveNodes() {
        try {
            graph.removeNodes(new String[]{"A", "B", "C"});
        } catch (Exception e) {
            System.out.println("Nodes listed do not exist.");
        }
        graph.addNodes(new String[]{"A", "B", "C"});
        Assert.assertEquals(3, graph.getNodeCount());
        graph.removeNodes(new String[]{"A", "B", "C"});
        System.out.println("graph node count: " + graph.getNodeCount());
        Assert.assertEquals(0, graph.getNodeCount());
    }

    private void testRemoveEdge() {
        try {
            graph.removeEdge("A", "B");
        } catch (Exception e) {
            System.out.println("Nodes listed do not exist.");
        }
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        System.out.println("graph edge count: " + graph.getEdgeCount());
        graph.removeEdge("A", "B");
        System.out.println("graph edge count: " + graph.getEdgeCount());
        Assert.assertEquals(1, graph.getEdgeCount());
    }
}

