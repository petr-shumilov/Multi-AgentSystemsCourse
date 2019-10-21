package ru.spbu.mas;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

class NetworkController {

    private NetworkGraph networkGraph;
    private final String host = "localhost";
    private final String port = "10000";

    NetworkController(NetworkGraph networkGraph) {
        this.networkGraph = networkGraph;
    }

    void initAgents() {
        // Retrieve the singleton instance of the JADE Runtime
        Runtime jadeRuntime = Runtime.instance();

        // Create a container to host
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        // profile.setParameter(Profile.GUI, "true");
        ContainerController containerController = jadeRuntime.createMainContainer(profile);

        try {
            for (int i = 1; i <= this.networkGraph.getNodeNumber(); ++i) {

                // Packing a data for an agent into array required in reason by we able to send to an agent the ONLY array of objects.
                AgentData[] payloadToAgent = {
                        new AgentData(this.networkGraph.getNodeWeight()[i], this.networkGraph.getEdges()[i], this.networkGraph.getNodeNumber(), (i == this.networkGraph.getNodeNumber()))
                };
                // Create an agent
                jade.wrapper.AgentController agent = containerController.createNewAgent(Integer.toString(i), "ru.spbu.mas.NetworkAgent", payloadToAgent);

                // And start him
                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}