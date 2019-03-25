package base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Diese Klasse representiert einen generischen Graphen mit einer Liste aus Knoten und Kanten.
 *
 * @param <T> Die zugrundeliegende Datenstruktur, beispielsweise {@link game.map.Castle}
 */
public class Graph<T> {

    private List<Edge<T>> edges;
    private List<Node<T>> nodes;

    /**
     * Konstruktor für einen neuen, leeren Graphen
     */
    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new LinkedList<>();
    }

    /**
     * Einen neuen Knoten zum Graphen hinzufügen
     * @param value Der Wert des Knotens
     * @return Der erstellte Knoten
     */
    public Node<T> addNode(T value) {
        Node<T> node = new Node<>(value);
        this.nodes.add(node);
        return node;
    }

    /**
     * Eine neue Kante zwischen zwei Knoten hinzufügen. Sollte die Kante schon existieren, wird die vorhandene Kante zurückgegeben.
     * @param nodeA Der erste Knoten
     * @param nodeB Der zweite Knoten
     * @return Die erstellte oder bereits vorhandene Kante zwischen beiden gegebenen Knoten
     */
    public Edge<T> addEdge(Node<T> nodeA, Node<T> nodeB) {
        Edge<T> edge = getEdge(nodeA, nodeB);
        if(edge != null) {
            return edge;
        }

        edge = new Edge<>(nodeA, nodeB);
        this.edges.add(edge);
        return edge;
    }

    /**
     * Gibt die Liste aller Knoten zurück
     * @return die Liste aller Knoten
     */
    public List<Node<T>> getNodes() {
        return this.nodes;
    }

    /**
     * Gibt die Liste aller Kanten zurück
     * @return die Liste aller Kanten
     */
    public List<Edge<T>> getEdges() {
        return this.edges;
    }

    /**
     * @author Jan Braun
     * Diese Methode gibt alle Werte der Knoten in einer Liste mittels Streams zurück.
     * @see java.util.stream.Stream#map(Function)
     * @see java.util.stream.Stream#collect(Collector)
     * @return Eine Liste aller Knotenwerte
     */
    public List<T> getAllValues() {
    	return nodes.stream()
    				.map(n -> n.getValue())
    				.collect(Collectors.toList());
    }

    /**
     * @author Jan Braun
     * Diese Methode gibt alle Kanten eines Knotens als Liste mittels Streams zurück.
     * @param node Der Knoten für die dazugehörigen Kanten
     * @see java.util.stream.Stream#filter(Predicate)
     * @see java.util.stream.Stream#collect(Collector)
     * @return Die Liste aller zum Knoten zugehörigen Kanten
     */
    public List<Edge<T>> getEdges(Node<T> node) {
        return edges.stream()
        			.filter(e -> e.contains(node))
        			.collect(Collectors.toList());
    }
    
    /**
     * @author Jan Braun
     * @param node der Knoten dessen Nachbarn man kennenlernen möchte
     * @return alle Nachbar-Knoten auf Grund ihrer gemeinsamen Kanten
     */
    public List<Node<T>> getNodes(Node<T> node) {
    	return getEdges(node).stream()
    						 .map(e -> e.getOtherNode(node))
    						 .collect(Collectors.toList());
    }

    /**
     * @author Jan Braun
     * Diese Methode sucht eine Kante zwischen beiden angegebenen Knoten und gibt diese zurück
     * oder null, falls diese Kante nicht existiert
     * @param nodeA Der erste Knoten
     * @param nodeB Der zweite Knoten
     * @return Die Kante zwischen beiden Knoten oder null
     */
    public Edge<T> getEdge(Node<T> nodeA, Node<T> nodeB) {
    	return edges.stream()
    				.filter(e -> e.contains(nodeA) && e.getOtherNode(nodeA) == nodeB)
    				.findFirst().orElse(null);
    }

    /**
     * @author Jan Braun
     * Gibt den ersten Knoten mit dem angegebenen Wert zurück oder null, falls dieser nicht gefunden wurde
     * @param value Der zu suchende Wert
     * @return Ein Knoten mit dem angegebenen Wert oder null
     */
    public Node<T> getNode(T value) {
    	return nodes.stream()
    				.filter(n -> n.getValue().equals(value))
    				.findFirst().orElse(null);
    }
    
    /**
     * @author Jan Braun
     * Überprüft, ob alle Knoten in dem Graphen erreichbar sind.
     * @return true, wenn alle Knoten erreichbar sind
     */
    public boolean allNodesConnected() {
    	return oneNodeConnected(nodes.get(0), new ArrayList<Node<T>>());
    }
    
    /**
     * @author Jan Braun
     * @param startNode der Knoten mit dem die Erreichbarkeit getestet wird
     * @param connectedNodes Liste an Knoten über die bereits erreichbar waren
     * @return true, wenn alle Knoten von diesem Knoten erreichbar sind
     */
    private boolean oneNodeConnected(Node<T> startNode, List<Node<T>> connectedNodes) {
    	connectedNodes.add(startNode);
    	for(Node<T> n : getNodes(startNode)) {
    		if(connectedNodes.contains(n))
    			continue;
    		oneNodeConnected(n, connectedNodes);
    	} return connectedNodes.containsAll(nodes);
    }
}
