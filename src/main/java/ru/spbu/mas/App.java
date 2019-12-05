package ru.spbu.mas;

public class App {
    public static void main(String[] args) {
        // Sample graph
//        Integer[][] edges = {
//                {},
//                {2, 3}, // <- 1
//                {1, 3}, // <- 2
//                {1, 2, 4, 7}, // <- 3
//                {3, 5},    // <- 4
//                {4, 7, 6}, // <- 5
//                {5, 7},    // <- 6
//                {3, 5, 6}      // <- 7
//        };
//        Integer[] nodes = {0, 5, 4, 1, 3, 2, 3, 8};
//
//        NetworkGraph networkGraph = new NetworkGraph(7,9,  edges, nodes);

        // Simple sample
//        Integer[][] edges = {
//                {},
//                {2, 4}, // <- 1
//                {3, 1, 4}, // <- 2
//                {2}, // <- 3
//                {2, 1} // <- 4
//        };
//        Integer[] nodes = {0, 1, 2, 3, 4};
//
//        NetworkGraph networkGraph = new NetworkGraph(4,8,  edges, nodes);

        Integer[][] edges = {
                {},
                {2, 4}, // <- 1
                {3, 1}, // <- 2
                {2, 4}, // <- 3
                {1, 3} // <- 4
        };
        Integer[] nodes = {0, 1, 2, 3, 4};

        NetworkGraph networkGraph = new NetworkGraph(4,4,  edges, nodes);

        NetworkConfiguration networkConfiguration = new NetworkConfiguration(0.2, 0.3, 100, 10);

        NetworkController networkController = new NetworkController(networkGraph, networkConfiguration);
        networkController.initAgents();
    }
}
