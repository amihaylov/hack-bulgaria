package dijkstra;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestDijkstra {

  private List<Vertex> nodes;
  private List<Edge> edges;
  private int distance = 0;

  @Test
  public void testExcute() {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    for (int i = 0; i < 11; i++) {
      Vertex location = new Vertex("Node_" + i, "Node_" + i);
      nodes.add(location);
    }

    addLane("Edge_0", 0, 1, 85);
    addLane("Edge_1", 0, 2, 217);
    addLane("Edge_2", 0, 4, 173);
    addLane("Edge_3", 2, 6, 186);
    addLane("Edge_4", 2, 7, 103);
    addLane("Edge_5", 3, 7, 183);
    addLane("Edge_6", 5, 8, 250);
    addLane("Edge_7", 8, 9, 84);
    addLane("Edge_8", 7, 9, 167);
    addLane("Edge_9", 4, 9, 502);
    addLane("Edge_10", 9, 10, 40);
    addLane("Edge_11", 1, 10, 600);

    // Lets check from location Loc_1 to Loc_10
    Graph graph = new Graph(nodes, edges);
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.execute(nodes.get(0));
    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(9));
    
    for (int i=0; i<path.size()-1;i++) {
    	String source = path.get(i).getId();
    	String dest = path.get(i+1).getId();
    	for (int j=0; j<edges.size();j++) {
    		if((edges.get(j).getSource().getId()==source)&&(edges.get(j).getDestination().getId()==dest)) {
    			distance+=edges.get(j).getWeight();
    		}
    	}
    }
    	
    assertNotNull(path);
    assertTrue(path.size() > 0);
    
    for (Vertex vertex : path) {
      System.out.println(vertex);
    }
    System.out.println("Distance = " + distance);
  }

  private void addLane(String laneId, int sourceLocNo, int destLocNo,
      int duration) {
    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    edges.add(lane);
  }
} 

