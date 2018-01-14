package examples.StarterPacManOneJunction;

import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;
import pacman.game.internal.SearchLink;/*1223added*/

import java.util.*;
import java.io.*;
import java.lang.Boolean;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

/**
 * Created by piers on 23/02/17.
 */
public class MyPacMan extends PacmanController
{

    @Override
    public Constants.MOVE getMove(Game game, long timeDue)
    {
        /*
        Game coGame;
        GameInfo info = game.getPopulatedGameInfo();
        info.fixGhosts((ghost) -> new Ghost(
                ghost,
                game.getCurrentMaze().lairNodeIndex,
                -1,
                -1,
                Constants.MOVE.NEUTRAL
        ));
        coGame = game.getGameFromInfo(info);


        // Make some ghosts
        MASController ghosts = new POCommGhosts(50);

        // Get the best one Junction lookahead move
        Constants.MOVE bestMove = null;
        int bestScore = -Integer.MAX_VALUE;
        for (Constants.MOVE move : Constants.MOVE.values()) {
            Game forwardCopy = coGame.copy();
            // Have to forward once before the loop - so that we aren't on a junction
            forwardCopy.advanceGame(move, ghosts.getMove(forwardCopy.copy(), 40));
            while(!forwardCopy.isJunction(forwardCopy.getPacmanCurrentNodeIndex())){
                forwardCopy.advanceGame(move, ghosts.getMove(forwardCopy.copy(), 40));
            }
            int score = forwardCopy.getScore();
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
            }
        }
        
    	
    	
    	
        
        */
    	int closestP = 0,disTonearestP = 0,ghostLocation_now=0;
    	int[] pills = game.getPillIndices();
    	int ghostLocation=0,minDistanceGh=Integer.MAX_VALUE,current=0,ghlairtimemux=1;
    	int ghdir = -1;/*0103 AStar_new 傳入鬼的方向*/
    	int[] AStarPath;
    	Constants.GHOST minGhost = null;
    	current = game.getPacmanCurrentNodeIndex();
    	for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // 對於所有的鬼
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
            	//System.out.println("there is a ghost");
                ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                int disFromGh = game.getShortestPathDistance(current, ghostLocation);
                if (disFromGh < minDistanceGh)
                {
                    minDistanceGh = disFromGh;
                    minGhost = ghost;
                }
            }
            else if(game.getGhostLairTime(ghost)!=0)
            {
                ghlairtimemux = ghlairtimemux * game.getGhostLairTime(ghost);
                //若for 跑完 此值>0表全在籠子內
            }
        }
    	ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)
        {
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // 如果這個藥丸存在，則存入target_p
                }
            }
        }
        if(!targets_p.isEmpty())    //還有p
        {
            //System.out.println("還有p");
            //轉換
            int[] targetsArray_p = new int[targets_p.size()];
            for (int i = 0; i < targetsArray_p.length; i++)
            {
                targetsArray_p[i] = targets_p.get(i);
            }
            closestP = game.getClosestNodeIndexFromNodeIndex(current, targetsArray_p, Constants.DM.PATH);
            disTonearestP = game.getShortestPathDistance(current, closestP);
            //0823 risk
            
        }
    	if(minGhost!=null)
    	{
    		ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
    		MOVE tdir = game.getGhostLastMoveMade_new(minGhost);
            switch(tdir){
            case UP :
            	ghdir = 0;
            	break;
            case RIGHT:
            	ghdir = 1;
            	break;
            case DOWN:
            	ghdir = 2;
            	break;
            case LEFT:
            	ghdir = 3;
            	break;
            }
            AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,closestP,game,ghostLocation_now,ghdir);
            return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
    	}
    	else{
    		//System.out.println("not in");
    		return game.getNextMoveAwayFromTarget(current, ghostLocation, Constants.DM.PATH);
    	}
    
    	
    	
    	
    	
        //return MOVE.NEUTRAL;
    }

}
