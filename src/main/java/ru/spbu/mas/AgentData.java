package ru.spbu.mas;

class AgentData {
    Integer value;
    Integer[] neighbors;
    boolean isInitialAgent = false;
    Integer numberOfNodes;

    AgentData(Integer value, Integer[] neighbors, Integer numberOfNodes, boolean isInitialAgent) {
        this.value = value;
        this.neighbors = neighbors;
        this.numberOfNodes = numberOfNodes;
        this.isInitialAgent = isInitialAgent;
    }
}
