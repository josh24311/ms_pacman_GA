package examples.StarterPacMan;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Random;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
    private static final int MIN_DISTANCE = 20;
    private Random random = new Random();

    @Override
    public MOVE getMove(Game game, long timeDue) {

        // Should always be possible as we are PacMan
    	//取得目前小精靈所在index
        int current = game.getPacmanCurrentNodeIndex();

        // Strategy 1: Adjusted for PO
        //躲避第一個找到且距離小於某值的鬼
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If can't see these will be -1 so all fine there
        	//對於所有的鬼
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
            	//如果這支鬼不可食，且不在籠子裡
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                //取得鬼的index
                if (ghostLocation != -1) {
                    if (game.getShortestPathDistance(current, ghostLocation) < MIN_DISTANCE) {
                    	//如果小精靈跟此鬼的最短距離小於MIN_DISTANCE
//                        System.out.println("Evading Ghost");
                        return game.getNextMoveAwayFromTarget(current, ghostLocation, Constants.DM.PATH);
                        //回傳遠離此鬼走法，傳完FOR迴圈就結束了
                        
                    }
                }
            }
        }

        /// Strategy 2: Find nearest edible ghost and go after them
        int minDistance = Integer.MAX_VALUE;
        Constants.GHOST minGhost = null;
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If it is > 0 then it is visible so no more PO checks
        	//對於所有的鬼
            if (game.getGhostEdibleTime(ghost) > 0) {
            	//如果這支鬼處於可食狀態
                int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
                //取得此鬼跟小精靈最近距離
                if (distance < minDistance) {
                	//做完FOR迴圈之後會找到距離最短的可食鬼
                    minDistance = distance;
                    minGhost = ghost;
                }
            }
        }

        if (minGhost != null) {
        	//如果有這樣的可食鬼存在
//            System.out.println("Hunting Ghost");
            return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);
            //回傳往此可食鬼的前進方向
        }

        // Strategy 3: Go after the pills and power pills that we can see
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();
        
        for (int i = 0; i < pills.length; i++) {
            //check which pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable != null) {
                if (pillStillAvailable) {
                    targets.add(pills[i]);
                    //如果這個小藥丸存在，則存入target
                }
            }
        }

        for (int i = 0; i < powerPills.length; i++) {            //check with power pills are available
            Boolean pillStillAvailable = game.isPowerPillStillAvailable(i);
            //這裡應為誤植，應改為game.isPowerPillStillAvailable(i);
            if (pillStillAvailable != null) {
                if (pillStillAvailable) {
                    targets.add(powerPills[i]);
                    //如果這個大力丸存在，則存入target
                }
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            //return the next direction once the closest target has been identified
//            System.out.println("Hunting pill");
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH), Constants.DM.PATH);
            //回傳小精靈往targetArray第一個物件的移動方向
        }


        // Strategy 4: New PO strategy as now S3 can fail if nothing you can see
        // Going to pick a random action here
        MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
        if (moves.length > 0) {
            return moves[random.nextInt(moves.length)];
        }

//        System.out.println("Just turn around");
        // Must be possible to turn around
        return game.getPacmanLastMoveMade().opposite();
    }
}