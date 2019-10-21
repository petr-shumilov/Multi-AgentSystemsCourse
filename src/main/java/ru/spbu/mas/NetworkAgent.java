package ru.spbu.mas;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class NetworkAgent extends Agent {
    boolean Used = false;
    AgentData AgentData;
    String RequesterName;
    int SentMessagesCount = 0;
    int ReceivedMessagesCount = 0;
    int Accumulator = 0;

    @Override
    protected void setup() {

        // Attach the behaviour to the agent
        addBehaviour(new NetworkCoverBehaviour(this));

        // Getting neighbours list
        this.AgentData = ((AgentData[])getArguments())[0];

        // Start covering from the initial agent
        if (this.AgentData.isInitialAgent) {
            System.out.println("Initial agent #" + getAID().getLocalName());

            this.Used = true;
            ACLMessage initMessage = new ACLMessage(ACLMessage.REQUEST);
            for (int i = 0; i < this.AgentData.neighbors.length; ++i) {
                initMessage.addReceiver(new AID(Integer.toString(this.AgentData.neighbors[i]), AID.ISLOCALNAME));
                this.SentMessagesCount++;
            }
            this.send(initMessage);
        }
        else {
            System.out.println("Default agent #" + getAID().getLocalName());
        }
    }
}