package net.runelite.client.plugins.microbot.shortestpath.pathfinder;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.shortestpath.Transport;

public class TransportNode extends Node implements Comparable<TransportNode> {
    @Getter
    private final Transport transport;

    public TransportNode(Transport transport, Node previous, int travelTime) {
        super(transport.getDestination(), previous, cost(previous, travelTime));
        this.transport = transport;
    }

    private static int cost(Node previous, int travelTime) {
        return (previous != null ? previous.cost : 0) + travelTime;
    }

    @Override
    public int compareTo(TransportNode other) {
        return Integer.compare(cost, other.cost);
    }
}
