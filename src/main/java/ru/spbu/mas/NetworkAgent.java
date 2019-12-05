package ru.spbu.mas;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class NetworkAgent extends Agent {
    AgentData AgentData;
    NetworkConfiguration networkConfiguration;

    @Override
    protected void setup() {

        // Attach the behaviour to the agent
        addBehaviour(new NetworkCoverBehaviour(this));

        // Getting neighbours list
        this.AgentData = ((AgentData[])getArguments())[0];
        this.networkConfiguration = this.AgentData.networkConfiguration;
    }
}