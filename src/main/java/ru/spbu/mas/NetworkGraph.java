package ru.spbu.mas;

public class NetworkGraph {
    private int nodeNumber;
    private int edgeNumber;
    private Integer[][] edges;
    private Integer[] nodeWeight;

    NetworkGraph(int nodeNumber, int edgeNumber, Integer[][] edges, Integer[] nodeWeight) {
        this.nodeNumber = nodeNumber;
        this.edgeNumber = edgeNumber;
        this.edges = edges;
        this.nodeWeight = nodeWeight;
    }

    public int getNodeNumber() {
        return this.nodeNumber;
    }

    public int getEdgeNumber() {
        return  this.edgeNumber;
    }

    public Integer[][] getEdges() {
        return this.edges;
    }

    public Integer[] getNodeWeight() {
        return this.nodeWeight;
    }
}
