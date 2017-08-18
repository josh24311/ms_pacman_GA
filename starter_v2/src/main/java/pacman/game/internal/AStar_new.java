package pacman.game.internal;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.PriorityQueue;

/*
 * This class is used to compute the shortest path for the ghosts: as these may not reverse, one cannot use 
 * a simple look-up table. Instead, we use the pre-computed shortest path distances as an admissable
 * heuristic. Although AStar needs to be run every time a path is to be found, it is very quick and does
 * not expand too many nodes beyond those on the optimal path.
 */
public class AStar_new {
    private N[] graph;

    public void createGraph(Node[] nodes) {
        graph = new N[nodes.length];

        //create graph
        for (int i = 0; i < nodes.length; i++) {
            graph[i] = new N(nodes[i].nodeIndex);
        }

        //add neighbours
        for (int i = 0; i < nodes.length; i++) {
            EnumMap<MOVE, Integer> neighbours = nodes[i].neighbourhood;
            MOVE[] moves = MOVE.values();

            for (int j = 0; j < moves.length; j++) {
                if (neighbours.containsKey(moves[j])) {
                    graph[i].adj.add(new E(graph[neighbours.get(moves[j])], moves[j], 1));
                }
            }
        }
    }

    public synchronized int[] computePathsAStar(int s, int t, MOVE lastMoveMade, Game game,int g[]) {
        N start = graph[s];
        N target = graph[t];
        //new 
        int gnum = 0;//compute how many items in g[] 
        for(int i = 0;i<g.length;i++)
        {
        	if(g[i]==0)
        	{
        		gnum = i;
        	}
        	else
        	{
        		gnum = i+1;
        	}
        }
        System.out.println(gnum);
        N[] w = new N[gnum];
        boolean isGhost = false;
        for(int i = 0;i < gnum ; i++)
        {	
        		w[i] = graph[g[i]];
        }
        for(int i = 0;i< gnum; i++)
        {
        		w[i].h = 9999.0;
        }
        //System.out.println("here");
        //   new
        PriorityQueue<N> open = new PriorityQueue<N>();
        ArrayList<N> closed = new ArrayList<N>();
        
        
        
        start.g = 0;
        start.h = game.getShortestPathDistance(start.index, target.index);

        start.reached = lastMoveMade;

        open.add(start);

        while (!open.isEmpty()) {
            N currentNode = open.poll();
            closed.add(currentNode);

            if (currentNode.isEqual(target)) {
                break;
            }

            for (E next : currentNode.adj) {
                if (next.move != currentNode.reached.opposite()) {
                    double currentDistance = next.cost;

                    if (!open.contains(next.node) && !closed.contains(next.node)) {
                        next.node.g = currentDistance + currentNode.g;
                        //new if for
                        for(int i = 0;i<gnum; i++)
                        {
                        	if(next.node!=w[i])
                        	{
                        		continue;
                        	}
                        	else
                        	{
                        		isGhost = true;
                        		break;
                        	}
                        }
                        /*
                        if(!isGhost)
                        {
                        	next.node.h = game.getShortestPathDistance(next.node.index, target.index);
                        }
                        else
                        {
                        	next.node.h = 9999.0;
                        }*/
                        next.node.h = game.getShortestPathDistance(next.node.index, target.index);
                        // End  new if for 
                        next.node.parent = currentNode;

                        next.node.reached = next.move;

                        open.add(next.node);
                    } else if (currentDistance + currentNode.g < next.node.g) {
                        next.node.g = currentDistance + currentNode.g;
                        next.node.parent = currentNode;

                        next.node.reached = next.move;

                        if (open.contains(next.node)) {
                            open.remove(next.node);
                        }

                        if (closed.contains(next.node)) {
                            closed.remove(next.node);
                        }

                        open.add(next.node);
                    }
                }
            }
        }
        //System.out.println("left while open.isempty");
        return extractPath(target);
    }

    public synchronized int[] computePathsAStar(int s, int t, Game game,int g[]) {
        return computePathsAStar(s, t, MOVE.NEUTRAL, game,g);
    }

    private synchronized int[] extractPath(N target) {
        ArrayList<Integer> route = new ArrayList<Integer>();
        N current = target;
        route.add(current.index);

        while (current.parent != null) {
            route.add(current.parent.index);
            current = current.parent;
        }
        //System.out.println("left while current.parrent!=null");

        Collections.reverse(route);

        int[] routeArray = new int[route.size()];

        for (int i = 0; i < routeArray.length; i++) {
            routeArray[i] = route.get(i);
        }

        return routeArray;
    }

    public void resetGraph() {
        for (N node : graph) {
            node.g = 0;
            node.h = 0;
            node.parent = null;
            node.reached = null;
        }
    }
}

