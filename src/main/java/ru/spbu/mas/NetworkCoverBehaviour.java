package ru.spbu.mas;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class NetworkCoverBehaviour extends CyclicBehaviour {

    private NetworkAgent agent;

    NetworkCoverBehaviour(NetworkAgent agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action()
    {
        // Sync getting a message
        ACLMessage msg = this.agent.blockingReceive();

        if (msg != null) {
            /*
              We are using performatives strictly not according to documents and standards.
              At this case performatives useful for us exclusively as message type.
              "REQUEST" is using for broadcast searching in depth of graph
              "INFORM" is using for returning results of searching
              This covering based on DFS
             */
            switch (msg.getPerformative()) {
                case ACLMessage.REQUEST: {

                    if (!this.agent.Used) {
                        this.agent.Used = true;
                        this.agent.RequesterName = msg.getSender().getLocalName();

                        int willSendMessageCount = 0;
                        // Sending a request neighbors
                        ACLMessage requestMessage = new ACLMessage(ACLMessage.REQUEST);
                        System.out.println(this.agent.getLocalName() + " is sending to neighbors: ");
                        for (int i = 0; i < this.agent.AgentData.neighbors.length; ++i) {
                            if (!this.agent.AgentData.neighbors[i].toString().equals(this.agent.RequesterName)) {
                                requestMessage.addReceiver(new AID(this.agent.AgentData.neighbors[i].toString(), AID.ISLOCALNAME));
                                System.out.println(this.agent.getLocalName() + " -> " + this.agent.AgentData.neighbors[i]);
                                willSendMessageCount++;
                            }
                        }

                        if (willSendMessageCount > 0) {
                            this.agent.SentMessagesCount = willSendMessageCount;
                            this.agent.send(requestMessage);
                        }
                        else {
                            ACLMessage response = msg.createReply();
                            response.setPerformative(ACLMessage.INFORM);
                            response.setContent(this.agent.AgentData.value.toString());
                            this.agent.send(response);
                        }
                    }
                    else {
                        ACLMessage response = msg.createReply();
                        response.setPerformative(ACLMessage.INFORM);
                        response.setContent("0");
                        this.agent.send(response);

                        System.out.println("Already used (from: " + msg.getSender().getLocalName() + ") " + this.agent.getLocalName());
                    }
                    break;
                }
                case ACLMessage.INFORM: {

                    this.agent.ReceivedMessagesCount++;
                    this.agent.Accumulator += Integer.parseInt(msg.getContent());
                    System.out.println("Rcv " + msg.getSender().getLocalName() + " -> " + this.agent.getLocalName() + " (s: " + this.agent.SentMessagesCount + ", r: " + this.agent.ReceivedMessagesCount + ") value: " + msg.getContent());

                    if (this.agent.ReceivedMessagesCount == this.agent.SentMessagesCount) {

                        int sum = this.agent.Accumulator + this.agent.AgentData.value;

                        if (this.agent.AgentData.isInitialAgent) {
                            double mean = (double)sum / (double)this.agent.AgentData.numberOfNodes;
                            System.out.println("================= REPORT =================");
                            System.out.println("Sum of all nodes: " + sum);
                            System.out.println("Number of nodes: " + this.agent.AgentData.numberOfNodes);
                            System.out.println("Arithmetic mean of nodes: " + mean);
                        }
                        else {
                            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                            System.out.println("To the agent " + this.agent.getLocalName() + " returned all messages. requester: " + this.agent.RequesterName + "; result: " + Integer.toString(sum));
                            response.setContent(Integer.toString(sum));
                            response.addReceiver(new AID(this.agent.RequesterName, AID.ISLOCALNAME));
                            this.agent.send(response);
                        }
                    }
                    break;
                }
            }
        }
    }
}
