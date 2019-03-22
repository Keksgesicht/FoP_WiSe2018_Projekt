package base;

import java.util.*;

/**
 * Abstrakte generische Klasse um Wege zwischen Knoten in einem Graph zu finden.
 * Eine implementierende Klasse ist beispielsweise {@link game.map.PathFinding}
 * @param <T> Die Datenstruktur des Graphen
 */
public abstract class GraphAlgorithm<T> {

    /**
     * Innere Klasse um {@link Node} zu erweitern, aber nicht zu verändern
     * Sie weist jedem Knoten einen Wert und einen Vorgängerknoten zu.
     * @param <T>
     */
    private static class AlgorithmNode<T> {

        private Node<T> node;
        private double value;
        private AlgorithmNode<T> previous;

        AlgorithmNode(Node<T> parentNode, AlgorithmNode<T> previousNode, double value) {
            this.node = parentNode;
            this.previous = previousNode;
            this.value = value;
        }
    }

    private Graph<T> graph;

    // Diese Liste enthält alle Knoten, die noch nicht abgearbeitet wurden
    private List<Node<T>> availableNodes;

    // Diese Map enthält alle Zuordnungen
    private Map<Node<T>, AlgorithmNode<T>> algorithmNodes;

    /**
     * Erzeugt ein neues GraphAlgorithm-Objekt mit dem dazugehörigen Graphen und dem Startknoten.
     * @param graph der zu betrachtende Graph
     * @param sourceNode der Startknoten
     */
    public GraphAlgorithm(Graph<T> graph, Node<T> sourceNode) {
        this.graph = graph;
        this.availableNodes = new LinkedList<>(graph.getNodes());
        this.algorithmNodes = new HashMap<>();

        for(Node<T> node : graph.getNodes())
            this.algorithmNodes.put(node, new AlgorithmNode<>(node, null, -1));

        this.algorithmNodes.get(sourceNode).value = 0;
    }

    /**
     * Diese Methode gibt einen Knoten mit dem kleinsten Wert, der noch nicht abgearbeitet wurde, zurück und entfernt ihn aus der Liste {@link #availableNodes}.
     * Sollte kein Knoten gefunden werden, wird null zurückgegeben.
     * Verbindliche Anforderung: Verwenden Sie beim Durchlaufen der Liste Iteratoren
     * @return Der nächste abzuarbeitende Knoten oder null
     */
    private AlgorithmNode<T> getSmallestNode() {
    	Iterator<Node<T>> iter = availableNodes.iterator();										//make new iterator
    	AlgorithmNode<T> smallest = null;														//initialize smallest with fallback return value
    	while(iter.hasNext()) {																	//as long as not iterated through all elems
    		AlgorithmNode<T> current = algorithmNodes.get(iter.next());							//set current elem to check
    		if (current.value >= 0 && (smallest == null || current.value < smallest.value))		//if the elem is not negative and current is smaller then every before
    			smallest = current;																//then set new smallest
    	}
    	if(smallest != null) availableNodes.remove(smallest.node);
        return smallest;																		//return the smallest elem
    }

    /**
     * Diese Methode startet den Algorithmus. Dieser funktioniert wie folgt:
     * 1. Suche den Knoten mit dem geringsten Wert (siehe {@link #getSmallestNode()})
     * 2. Für jede angrenzende Kante:
     * 2a. Überprüfe ob die Kante passierbar ist ({@link #isPassable(Edge)})
     * 2b. Berechne den Wert des Knotens, in dem du den aktuellen Wert des Knotens und den der Kante addierst
     * 2c. Ist der alte Wert nicht gesetzt (-1) oder ist der neue Wert kleiner, setze den neuen Wert und den Vorgängerknoten
     * 3. Wiederhole solange, bis alle Knoten abgearbeitet wurden

     * Nützliche Methoden:
     * @see #getSmallestNode()
     * @see #isPassable(Edge)
     * @see Graph#getEdges(Node)
     * @see Edge#getOtherNode(Node)
     */
    public void run() {
    	AlgorithmNode<T> v = getSmallestNode();											//initialize first smallest elem
    	while (v != null) {																//as long as there are more elems to handle
	    	List<Edge<T>> list = graph.getEdges(v.node);								//make list of all edges
	    	Iterator<Edge<T>> iter = list.iterator();									//make iterator for those edges
	    	while (iter.hasNext()) {													//iterate through edges
	    		Edge<T> e = iter.next(); 												//save edge
	    		if (isPassable(e) && isPassable(v.node)) {								//check edge whether it's passable
	    			double a = v.value + getValue(e);									//calc as task stated
	    			AlgorithmNode<T> n = algorithmNodes.get(e.getOtherNode(v.node));	//set n as stated
	    			double cmp = n.value;												//n's value to compare to
	    			if (cmp == -1 || a < cmp) {											//compare to n's value
	    				n.value = a;													//set both values as stated by the task
	    				n.previous = v;
	    			}
	    		}
	    	}
	    	v = getSmallestNode();														//set new elem to work with
    	}
    }

    /**
     * Diese Methode gibt eine Liste von Kanten zurück, die einen Pfad zu dem angegebenen Zielknoten representiert.
     * Dabei werden zuerst beginnend mit dem Zielknoten alle Kanten mithilfe des Vorgängerattributs {@link AlgorithmNode#previous} zu der Liste hinzugefügt.
     * Zum Schluss muss die Liste nur noch umgedreht werden. Sollte kein Pfad existieren, geben Sie null zurück.
     * @param destination Der Zielknoten des Pfads
     * @return eine Liste von Kanten oder null
     */
    public List<Edge<T>> getPath(Node<T> destination) {
    	List<Edge<T>> path = new ArrayList<Edge<T>>();					//initialize return list
    	Node<T> current = destination;									//set iterator
    	AlgorithmNode<T> currentAlgo = algorithmNodes.get(current);		//set iterator as other struct
    	AlgorithmNode<T> previousAlgo = currentAlgo.previous;			//set iterators previous
    	if (currentAlgo.previous == null) return null;
    	Node<T> previous = previousAlgo.node;							//set iterators previous as other struct
    	while (true) {								//iterate through previous elems
    		Edge<T> e = graph.getEdge(current, previous);				//make edge between current and prev
    		path.add(e);												//add path to return list
    		current = previous;											//set iterator and other new
        	currentAlgo = algorithmNodes.get(current);
        	if (currentAlgo.previous == null) break;
        	previousAlgo = currentAlgo.previous;
        	previous = previousAlgo.node;
    	}
    	Collections.reverse(path);										//reverse list as stated by task
        return path;													//return list
    }

    /**
     * Gibt den betrachteten Graphen zurück
     * @return der zu betrachtende Graph
     */
    protected Graph<T> getGraph() {
        return this.graph;
    }

    /**
     * Gibt den Wert einer Kante zurück.
     * Diese Methode ist abstrakt und wird in den implementierenden Klassen definiert um eigene Kriterien für Werte zu ermöglichen.
     * @param edge Eine Kante
     * @return Ein Wert, der der Kante zugewiesen wird
     */
    protected abstract double getValue(Edge<T> edge);

    /**
     * Gibt an, ob eine Kante passierbar ist.
     * @param edge Eine Kante
     * @return true, wenn die Kante passierbar ist.
     */
    protected abstract boolean isPassable(Edge<T> edge);

    /**
     * Gibt an, ob eine Knoten passierbar ist.
     * @param node Eine Knoten
     * @return true, wenn der Knoten passierbar ist.
    */
    protected abstract boolean isPassable(Node<T> node);
}
