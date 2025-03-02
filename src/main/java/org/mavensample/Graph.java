package org.mavensample;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizV8Engine;
import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Graph {

    private MutableGraph graph;

    public Graph() {
        this.graph = Factory.mutGraph("graph").setDirected(true);
    }

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
        int edgeNo = 0;
        for (MutableNode n : graph.nodes()) {
            edgeNo += n.links().size();
        }
        return edgeNo;
    }

    public List<String> getNodes() {
        /*List<String> nodes = new ArrayList<>();
        for (MutableNode n : graph.nodes()) {
            nodes.add(n.name().toString());
        }
        Collections.sort(nodes);
        return nodes;*/
        return graph.nodes().stream()
                .map(n -> n.name().toString())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getEdges() {
        List<String> edges = new ArrayList<>();
        for (MutableNode n : graph.nodes()) {
            for (Link link : n.links()) {
                edges.add(n.name() + " -> " + link.to().name());
            }
        }
        Collections.sort(edges);
        return edges;
    }
    // Add a single node
    public void addNode(String label) {
        for (MutableNode n : graph.nodes()) {
            if (n.name().toString().equals(label)) {
                System.out.println("Node " + label + " already exists.");
                return; // Avoid duplicates
            }
        }
        MutableNode newNode = Factory.mutNode(label); // Create a new node
        graph.add(newNode);
        System.out.println("Added node: " + label);
    }

    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    public void addEdge(String srcLabel, String dstLabel) {
        MutableNode srcNode = null;
        for (MutableNode n : graph.nodes()) {
            if (n.name().toString().equals(srcLabel)) {
                srcNode = n;
                break;
            }
        }

        if (srcNode == null) {
            srcNode = Factory.mutNode(srcLabel);
            graph.add(srcNode);
        }

        for (Link link : srcNode.links()) {
            if (link.to().name().toString().equals(dstLabel)) {
                System.out.println("Edge " + srcLabel + " -> " + dstLabel + " already exists.");
                return;
            }
        }

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

        srcNode.addLink(dstNode);
        System.out.println("Added edge: " + srcLabel + " -> " + dstLabel);
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

    public void outputDOTGraph(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(graph.toString());
        }
    }

    public void outputGraphics(String path, String format) throws IOException {
        if (!format.equalsIgnoreCase("png")) {
            throw new IllegalArgumentException("Only PNG format is supported.");
        }

        Graphviz.useEngine(new GraphvizV8Engine());

        Graphviz.fromGraph(graph)
                .render(Format.PNG)
                .toFile(new File(path));
    }
}
