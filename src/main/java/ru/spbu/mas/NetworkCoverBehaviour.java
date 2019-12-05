package ru.spbu.mas;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.wrapper.StaleProxyException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class NetworkCoverBehaviour extends TickerBehaviour {
    private int tick;
    private State state = State.SEND;
    private NetworkAgent agent;

    NetworkCoverBehaviour(NetworkAgent agent) {
        super(agent, TimeUnit.SECONDS.toMillis(1));
        this.setFixedPeriod(true);
        this.agent = agent;
        this.tick = 0;
    }

    @Override
    public void onTick() {
        tick++;
        switch (state) {
            case SEND:
                Send();
                this.state = State.RECEIVE;
                break;
            case RECEIVE:
                Receive();
                this.state = State.SEND;
                break;
            default:
                block();
        }
    }

    private void Send() {
        double random;
        boolean missed = false;
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        for (Integer neighbor : agent.AgentData.neighbors) {
            switch (neighbor) {
                // in case when the neighbor node are 2 and 3
                case 2:
                    random = Math.random();
                    if (random > agent.AgentData.networkConfiguration.probability) {
                        missed = true;
                    }
                    break;
                case 3:
                    random = Math.random();
                    if (random < agent.AgentData.networkConfiguration.probability) {
                        try {
                            TimeUnit.MILLISECONDS.sleep((int) (Math.random() * agent.AgentData.networkConfiguration.maxDelay));
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted exception");
                        }
                    }
                    break;
            }
            if (missed) {
                missed = false;
                continue;
            }

            msg.addReceiver(new AID(neighbor.toString(), AID.ISLOCALNAME));
        }
        double valueWithNoise = agent.AgentData.value + (Math.random() * 0.2 - 0.1);
        msg.setContent(String.valueOf(valueWithNoise));
        agent.send(msg);

        System.out.println(tick + ") " + "Agent:" + agent.getAID().getLocalName() + " has sent everybody his value ");
    }

    private void Receive() {
        double result = 0;
        HashSet<String> used = new HashSet<>();
        double agentValue = agent.AgentData.value;
        while ((agent.receive()) != null) {
            ACLMessage msg = agent.receive();
            if (msg != null) {
                if (used.isEmpty() || !used.contains(msg.getSender().getLocalName())) {
                    double receivedValue = Double.parseDouble(msg.getContent());
                    System.out.println(tick + ") " + "Agent:" + agent.getAID().getLocalName() + " Received " + receivedValue);

                    result += receivedValue - agentValue;
                    used.add(msg.getSender().getLocalName());
                }
                else{
                    if (used.size() == agent.AgentData.neighbors.length){
                        break;
                    }
                }
            }
        }
        agent.AgentData.value = agentValue + agent.AgentData.networkConfiguration.alpha * result;

        if (tick >= this.agent.networkConfiguration.maxNumberTicks) {
            Kill();
        }
    }

    private void Kill() {
        String currentAgentName = agent.getAID().getLocalName();

        DecimalFormat formatter = new DecimalFormat("#.######");
        System.out.println("================= REPORT =================");
        System.out.println("Current tick: " + tick);
        System.out.println("Current agent: " + currentAgentName);
        System.out.println("Calculated average: " + formatter.format(agent.AgentData.value));

        jade.wrapper.AgentContainer container = agent.getContainerController();

        agent.doDelete();

        new Thread(() -> {
            try {
                container.kill();
            }
            catch (StaleProxyException ignored) {
            }
        }).start();
    }
}
