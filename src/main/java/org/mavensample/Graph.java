package org.mavensample;
import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.*;

import java.io.*;
import java.util.*;


public class Graph {
    private MutableGraph graph;

    // Parse the DOT file and create a directed graph
    public void parseGraph(String filepath) throws IOException {
        File file = new File(filepath);
        // Reads the file using the graphviz parser
        this.graph = new Parser().read(file);
        //System.out.println(this.graph.toString());
        this.graph.setDirected(true);
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
}
