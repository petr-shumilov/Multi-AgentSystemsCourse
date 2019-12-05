package ru.spbu.mas;

public class NetworkConfiguration {

    double alpha;
    double probability;
    int maxNumberTicks;
    int maxDelay;

    NetworkConfiguration(double alpha, double probability, int maxNumberTicks, int maxDelay) {
        this.alpha = alpha;
        this.probability = probability;
        this.maxDelay = maxDelay;
        this.maxNumberTicks = maxNumberTicks;
    }
}
