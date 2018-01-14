package pacman.game.internal;

import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.lang.Math;/*1225added*/

/*
 * This class is used to compute the shortest path for the ghosts: as these may not reverse, one cannot use 
 * a simple look-up table. Instead, we use the pre-computed shortest path distances as an admissable
 * heuristic. Although AStar needs to be run every time a path is to be found, it is very quick and does
 * not expand too many nodes beyond those on the optimal path.
 */
public class AStar_new {
    //private N[] graph;
    public N[] graph;

    public void createGraph(Node[] nodes) {
        graph = new N[nodes.length];

        //create graph
        for (int i = 0; i < nodes.length; i++) {
            graph[i] = new N(nodes[i].nodeIndex);
        }

        //add neighbours
        for (int i = 0; i < nodes.length; i++) {
            EnumMap<MOVE, Integer> neighbours = nodes[i].neighbourhood;
            
            MOVE[] moves = MOVE.values();//上右下左原

            for (int j = 0; j < moves.length; j++) {
                if (neighbours.containsKey(moves[j])) {//node#1 for example : j=1,2
                    graph[i].adj.add(new E(graph[neighbours.get(moves[j])], moves[j], 1));
                }
            }
        }
    }

    public synchronized int[] computePathsAStar(int s, int t, MOVE lastMoveMade, Game game,int ghpo,int ghdir) {
        N start = graph[s];
        N target = graph[t];
        //System.out.println("start.parent: "+start.parent.index);
        PriorityQueue<N> open = new PriorityQueue<N>();
        ArrayList<N> closed = new ArrayList<N>();

        start.g = 0;
        start.h = game.getShortestPathDistance(start.index, target.index);

        start.reached = lastMoveMade;
        /*start.parent*/
        start.parent = null;
        
        open.add(start);
        /*1225 find 5 point out from gh*/
        MOVE[] tm= {Constants.MOVE.UP,Constants.MOVE.RIGHT,Constants.MOVE.DOWN,Constants.MOVE.LEFT};
    	int now = 0;
    	int tmp = 0;
    	int tdir = -1;/*0103 用來記錄 鬼的方向的反向 此方向不加入修改加權範圍*/
    	int[][] r = new int[5][6];
    	/*0103 ghdir define*/
    	switch(ghdir){
    	case 0:
    		tdir = 2;
    		break;
    	case 1:
    		tdir = 3;
    		break;
    	case 2:
    		tdir = 0;
    		break;
    	case 3:
    		tdir = 1;
    		break;
    	}
    	/*0103 end*/
    	Map<Integer,Integer> mp = new HashMap<>();
    	Map<Integer, Map<Integer,Integer>> mp1 = new HashMap<>();
    	ArrayList<Integer> reach1 = new ArrayList<Integer>();
    	ArrayList<Integer> reach2 = new ArrayList<Integer>();
    	ArrayList<Integer> reach3 = new ArrayList<Integer>();
    	ArrayList<Integer> reach4 = new ArrayList<Integer>();
    	ArrayList<Integer> reach5 = new ArrayList<Integer>();
    	
    	reach1.add(ghpo);
    	for(int i = 0;i<4;i++){
    		if(i!=tdir&&game.getCurrentMaze().graph[ghpo].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[i])!=null){
    			/*0103 新增 i!=tdir 即不考慮鬼的反向的那些點*/
    			tmp = game.getCurrentMaze().graph[ghpo].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[i]);
    			reach1.add(tmp);
    			mp.put(now, game.getCurrentMaze().graph[ghpo].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[i]));
    			//mp = {0=258, 2=270}
    			mp1.put(ghpo, mp);
    			//mp1 = {264={0=258, 2=270}}
    			now++;
    		}
    	}

    	for(int i = 0;i < reach1.size();i++){
    		for(int j =0;j<4;j++){
    			if(game.getCurrentMaze().graph[reach1.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j])!=null){
    				tmp = game.getCurrentMaze().graph[reach1.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j]);
    				if(!reach1.contains(tmp)){
    					reach2.add(tmp);
    				}
    			}
    		}
    	}
    	for(int i = 0;i < reach2.size();i++){
    		for(int j =0;j<4;j++){
    			if(game.getCurrentMaze().graph[reach2.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j])!=null){
    				tmp = game.getCurrentMaze().graph[reach2.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j]);
    				if(!reach1.contains(tmp)&&!reach2.contains(tmp)){
    					reach3.add(tmp);
    				}
    			}
    		}
    	}
    	for(int i = 0;i < reach3.size();i++){
    		for(int j =0;j<4;j++){
    			if(game.getCurrentMaze().graph[reach3.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j])!=null){
    				tmp = game.getCurrentMaze().graph[reach3.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j]);
    				if(!reach1.contains(tmp)&&!reach2.contains(tmp)&&!reach3.contains(tmp)){
    					reach4.add(tmp);
    				}
    			}
    		}
    	}
    	for(int i = 0;i < reach4.size();i++){
    		for(int j =0;j<4;j++){
    			if(game.getCurrentMaze().graph[reach4.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j])!=null){
    				tmp = game.getCurrentMaze().graph[reach4.get(i)].allNeighbourhoods.get(Constants.MOVE.NEUTRAL).get(tm[j]);
    				if(!reach1.contains(tmp)&&!reach2.contains(tmp)&&!reach3.contains(tmp)&&!reach4.contains(tmp)){
    					reach5.add(tmp);
    				}
    			}
    		}
    	}
    	for(int i = 0;i < reach1.size();i++){
    		r[0][i] = reach1.get(i);
    	}
    	for(int i = 0;i < reach2.size();i++){
    		r[1][i] = reach2.get(i);
    	}
    	for(int i = 0;i < reach3.size();i++){
    		r[2][i] = reach3.get(i);
    	}
    	for(int i = 0;i < reach4.size();i++){
    		r[3][i] = reach4.get(i);
    	}
    	for(int i = 0;i < reach5.size();i++){
    		r[4][i] = reach5.get(i);
    	}
    	reach1.clear();
    	reach2.clear();
    	reach3.clear();
    	reach4.clear();
    	reach5.clear();
        /*end 1225*/

        while (!open.isEmpty()) {
        	//當開啟列表不為空
            N currentNode = open.poll();//取得當前格
            closed.add(currentNode);

            if (currentNode.isEqual(target)) {
            	//目標格已加入關閉列表
                break;
            }
            //System.out.println(currentNode.index);
            /*1225 added*/
            tmp = -1;
            /*1225*/
            for (E next : currentNode.adj) {
                if (next.move != currentNode.reached.opposite()) {
                	for(int i =0;i<5;i++){
                		for(int j =0;j<6;j++){
                			if(r[i][j]==next.node.index){
                				tmp = i;
                				break;
                			}
                		}
                		if(tmp!=-1) break;
                	}
                	// if tmp =  0,dis = 2^8 , if tmp = 1, dis = 2^7
                    double currentDistance = (tmp!=-1)?Math.pow(2, (8-tmp)):next.cost;

                    if (!open.contains(next.node) && !closed.contains(next.node)) {
                    	
                        next.node.g = currentDistance + currentNode.g;
                        next.node.h = game.getShortestPathDistance(next.node.index, target.index);
                        next.node.parent = currentNode;

                        next.node.reached = next.move;

                        open.add(next.node);
                    } else if (currentDistance + currentNode.g < next.node.g) {
                    	//在開啟列表或關閉列表中且符合上述判斷
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

        return extractPath(target);
    }

    public synchronized int[] computePathsAStar(int s, int t, Game game,int ghpo,int ghdir) {
        return computePathsAStar(s, t, MOVE.NEUTRAL, game,ghpo,ghdir);
    }

    private synchronized int[] extractPath(N target) {
        ArrayList<Integer> route = new ArrayList<Integer>();
        N current = target;
        route.add(current.index);
        //System.out.println("extractPath in ");
        //System.out.println("current: "+current.index);
        //System.out.println("current.parent: "+current.parent.index);
        while (current.parent != null) {
            route.add(current.parent.index);
            current = current.parent;
        }
        //System.out.println("extractPath after while ");
        //System.out.println("current now : "+current.index);
        Collections.reverse(route);

        int[] routeArray = new int[route.size()];

        for (int i = 0; i < routeArray.length; i++) {
            routeArray[i] = route.get(i);
        }
        //System.out.println("extractPath out ");
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

