package org.mavensample;
import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizV8Engine;
import guru.nidi.graphviz.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.junit.Assert;


public class DotGraphToDirectedGraph {
    private MutableGraph graph;

    // Parse the DOT file and create a directed graph
    public void parseGraph(String filepath) throws IOException {
        File file = new File(filepath);
        this.graph = new Parser().read(file);
        System.out.println(this.graph.toString());
        this.graph.setDirected(true);
    }

    // API to output the imported graph into a DOT file
    public void outputDOTGraph(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(graph.toString());
        }
    }

    // API to output the imported graph into a graphics format (e.g., PNG)
    public void outputGraphics(String path, String format) throws IOException {
        if (!format.equalsIgnoreCase("png")) {
            throw new IllegalArgumentException("Only PNG format is supported.");
        }

        Graphviz.useEngine(new GraphvizV8Engine());

        Graphviz.fromGraph(graph)
                .render(Format.PNG)
                .toFile(new File(path));
    }

    // Get number of nodes
    public int getNodeCount() {
        return graph.nodes().size();
    }

    // Get number of edges
    public int getEdgeCount() {
        int edgeCount = 0;
        for (MutableNode node : graph.nodes()) {
            edgeCount += node.links().size();
        }
        return edgeCount;
    }

    // Get all nodes as a list
    public List<String> getNodes() {
        List<String> nodes = new ArrayList<>();
        for (MutableNode node : graph.nodes()) {
            nodes.add(node.name().toString());
        }
        Collections.sort(nodes);
        return nodes;
    }

    // Get all edges in "a -> b" format
    public List<String> getEdges() {
        List<String> edges = new ArrayList<>();
        for (MutableNode node : graph.nodes()) {
            for (Link link : node.links()) {
                edges.add(node.name() + " -> " + link.to().name());
            }
        }
        Collections.sort(edges);
        return edges;
    }

    // API: Add a single node (check for duplicates)
    public void addNode(String label) {
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(label)) {
                System.out.println("Node " + label + " already exists.");
                return; // Avoid duplicates
            }
        }
        MutableNode newNode = Factory.mutNode(label); // Create a new node
        graph.add(newNode);
        System.out.println("Added node: " + label);
    }

    // API: Add multiple nodes
    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    @Override
    public String toString() {
        String information = "";
        information += "Number Of Nodes: " + getNodeCount() + "\n";
        information += "Nodes: " + getNodes() + "\n";
        information += "Number Of Edges: " + getEdgeCount() + "\n";
        information += "Edges\n";
        for (String edge : getEdges()) {
            information += edge + "\n";
        }
        return information;
    }

    // Output graph data to a text file
    public void outputGraph(String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(this.toString());
        }
    }

    // **FEATURE 3: Add Edge to the Graph**
    public void addEdge(String srcLabel, String dstLabel) {
        // Find the source node
        MutableNode srcNode = null;
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(srcLabel)) {
                srcNode = node;
                break;
            }
        }

        // If source node doesn't exist, create it
        if (srcNode == null) {
            srcNode = Factory.mutNode(srcLabel);
            graph.add(srcNode);
        }

        // Check if the edge already exists
        for (Link link : srcNode.links()) {
            if (link.to().name().toString().equals(dstLabel)) {
                System.out.println("Edge " + srcLabel + " -> " + dstLabel + " already exists.");
                return;
            }
        }

        // Find or create the destination node
        MutableNode dstNode = null;
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(dstLabel)) {
                dstNode = node;
                break;
            }
        }
        if (dstNode == null) {
            dstNode = Factory.mutNode(dstLabel);
            graph.add(dstNode);
        }

        // Add the new edge
        srcNode.addLink(dstNode);
        System.out.println("Added edge: " + srcLabel + " -> " + dstLabel);
    }

    public static void main(String[] args) {
        try {
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

// Assert that the expected and output strings are equal
            Assert.assertEquals("The file contents are not the same!", expected, output);

            System.out.println("Graph details saved to " + outputFilePath);

            // Add nodes
            graphParser.addNode("X");
            graphParser.addNode("Y");

            // Add multiple nodes
            graphParser.addNodes(new String[]{"Z", "W", "V"});

            graphParser.addEdge("X", "Y");
            graphParser.addEdge("X", "Y");


            // Save updated graph
            graphParser.outputDOTGraph("updated_output.dot");
            graphParser.outputGraphics("output.png", "png");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
