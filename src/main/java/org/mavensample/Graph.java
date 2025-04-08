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

    public void removeNode(String label) {
        MutableGraph newGraph = Factory.mutGraph("graph").setDirected(true);

        for (MutableNode node : new ArrayList<>(graph.nodes())) {
            if (!node.name().toString().equals(label)) {
                newGraph.add(node);
            }
        }

        graph = newGraph;

        System.out.println("Remaining Nodes after removal:");
        for (MutableNode node : graph.nodes()) {
            System.out.println("Node: " + node.name().toString());
        }
    }


    public void removeNodes(String[] labels) {
        for(String label: labels) {
            removeNode(label);
        }
    }

    public void removeEdge(String srcLabel, String dstLabel) {
        MutableNode srcNode = null;
        MutableNode dstNode = null;

        for(MutableNode node: graph.nodes()) {
            if (node.name().toString().equals(srcLabel)) {
                srcNode = node;
            }
            if (node.name().toString().equals(dstLabel)) {
                dstNode = node;
            }
        }
            if(srcNode == null && dstNode != null) {
                throw new IllegalArgumentException("Source Node does not exist");
            }

            if(dstNode == null && srcNode != null) {
                throw new IllegalArgumentException("Destination Node does not exist.");
            }

            if(srcNode == null && dstNode == null) {
                throw new IllegalArgumentException("Source and Destination Nodes do not exist.");
            }

        boolean edgeRemoved = false;
        List<Link> newLinks = new ArrayList<>();

        for (Link linkedNode : srcNode.links()) {
            if (!linkedNode.to().name().toString().equals(dstLabel)) {
                newLinks.add(linkedNode);
            } else {
                edgeRemoved = true;
            }
        }

        if (!edgeRemoved) {
            throw new IllegalArgumentException("Edge from " + srcLabel + " to " + dstLabel + " does not exist.");
        }

        srcNode.links().clear();
        srcNode.links().addAll(newLinks);

    }

    public enum Algorithm { BFS, DFS };

    public Path GraphSearch(String src, String dst, Algorithm algo) {
        if (algo == Algorithm.BFS) {
            return bfsSearch(src, dst);
        } else {
            return dfsSearch(src, dst);
        }
    }

    // Adding comment as test
    public Path dfsSearch(String src, String dst) {
        if (src.equals(dst)) return new Path(Collections.singletonList(src));

        Stack<List<String>> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(Collections.singletonList(src));

        while (!stack.isEmpty()) {
            List<String> path = stack.pop();
            String lastNode = path.get(path.size() - 1);

            if (visited.contains(lastNode)) continue;
            visited.add(lastNode);

            MutableNode currentNode = getNodeByName(lastNode);
            if (currentNode == null) continue;

            for (Link link : currentNode.links()) {
                String neighborName = link.to().name().toString();

                if (!visited.contains(neighborName)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighborName);
                    stack.push(newPath);

                    if (neighborName.equals(dst)) {
                        return new Path(newPath);
                    }
                }
            }
        }
        return null;
    }

    // Adding a new comment for bfs search
    public Path bfsSearch(String src, String dst) {
        if (src.equals(dst)) return new Path(Collections.singletonList(src));

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(Collections.singletonList(src));
        visited.add(src);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastNode = path.get(path.size() - 1);

            MutableNode currentNode = getNodeByName(lastNode);
            if (currentNode == null) continue;

            // Iterate over links instead of assuming MutableNode
            for (Link link : currentNode.links()) {
                String neighborName = link.to().name().toString(); // Get the destination node's name

                if (!visited.contains(neighborName)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighborName);
                    queue.add(newPath);
                    visited.add(neighborName);

                    if (neighborName.equals(dst)) {
                        return new Path(newPath);
                    }
                }
            }
        }
        return null;
    }

    private MutableNode getNodeByName(String nodeName) {
        System.out.println("Hello World.");
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(nodeName)) {
                return node;
            }
        }
        return null;
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

    // This is a baseline for main, dfs, bfs code.
}
