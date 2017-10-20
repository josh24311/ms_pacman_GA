package examples.StarterPacManOneJunction;

import pacman.controllers.MASController;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.info.GameInfo;
import pacman.game.internal.Ghost;

import java.util.*;

/**
 * Created by piers on 23/02/17.
 */
public class MyPacMan extends PacmanController {

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
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
    	/*
    	int current = game.getPacmanCurrentNodeIndex();
    	//System.out.println()
    	
    	int[] nnp = game.getNeighbouringNodes(165);
    	System.out.println(nnp.length);
    	return game.getNextMoveTowardsTarget(current, 978, Constants.DM.PATH);
        //return bestMove;*/
    	int[] pills = game.getPillIndices();
    	int current = game.getPacmanCurrentNodeIndex();
    	//int[] unmarked = pills - {0};
    	//System.out.println(pills[219]);
    	//return MOVE.NEUTRAL;
    	//return game.getNextMoveTowardsTarget(current, current, Constants.DM.PATH);
    	
    	int[] a = {2,5,7,10,12,17};
    	int[] b = {5,12};
    	int tr = 1;
    	int tp = 3;
    	int now  = 0;
    	ArrayList<Integer> unmarked = new ArrayList<Integer>();
    	for(int i = 0;i<a.length;i++)
    	{
    		if(now < b.length)
    		{
    			if(a[i]!=b[now])
    			{
    				if(a[i]!=7)
    				{
    					unmarked.add(a[i]);
    				}
    				
    			}
    			else
    			{
    				now++;
    			}
    		}
    		else
    		{
    			if(a[i]!=17)
    			{
    				unmarked.add(a[i]);
    			}
    				
    			
    		}
    	}
    	
    	if(!unmarked.isEmpty())
    	{
    		int arrs[] = new int[unmarked.size()];
    		for(int i = 0;i<arrs.length;i++)
    		{
    			arrs[i] = unmarked.get(i);
    		}
    		for(int i = 0; i< arrs.length;i++)
        	{
    			System.out.println(arrs[i]);
        	}
    	}
    	
    	
    	return game.getPacmanLastMoveMade().opposite(); 
    }

}