package tests.student;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import base.Graph;
import base.Node;

/**
 * @author Jan Braun
 * tests whether the base.Graph#allNodesConnected() method is working
 */
public class GraphConnectionTest {
	
	Graph<String> testGraph;
	List<Node<String>> nodeArray = new ArrayList<Node<String>>();
	
	@BeforeEach
	void initTests() {
		testGraph = new Graph<String>();
		nodeArray.add(testGraph.addNode("123456789"));
		nodeArray.add(testGraph.addNode("qwertz"));
		nodeArray.add(testGraph.addNode("flort"));
		nodeArray.add(testGraph.addNode("FoP"));
		nodeArray.add(testGraph.addNode("065"));
	}
	
	@Test
	void korrekterGraph_Test01() {
		testGraph.addEdge(nodeArray.get(0), nodeArray.get(4));
		testGraph.addEdge(nodeArray.get(1), nodeArray.get(3));
		testGraph.addEdge(nodeArray.get(2), nodeArray.get(0));
		testGraph.addEdge(nodeArray.get(3), nodeArray.get(0));
		testGraph.addEdge(nodeArray.get(2), nodeArray.get(4));
		
		Assertions.assertTrue(testGraph.allNodesConnected());
	}
	
	@Test
	void inkorrekterGraph_Test01() {
		testGraph.addEdge(nodeArray.get(0), nodeArray.get(0));
		testGraph.addEdge(nodeArray.get(0), nodeArray.get(1));
		testGraph.addEdge(nodeArray.get(1), nodeArray.get(0));
		testGraph.addEdge(nodeArray.get(1), nodeArray.get(1));
		
		Assertions.assertFalse(testGraph.allNodesConnected());
	}
	
	@Test
	void inkorrekterGraph_Test02() {
		testGraph.addEdge(nodeArray.get(0), nodeArray.get(1));
		testGraph.addEdge(nodeArray.get(1), nodeArray.get(2));
		testGraph.addEdge(nodeArray.get(2), nodeArray.get(4));
		testGraph.addEdge(nodeArray.get(4), nodeArray.get(0));
		
		Assertions.assertFalse(testGraph.allNodesConnected());
	}

}
