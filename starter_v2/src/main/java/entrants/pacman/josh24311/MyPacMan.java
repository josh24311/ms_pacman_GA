package entrants.pacman.josh24311;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

//import entrants.pacman.josh24311.Test;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController
{
    private MOVE myMove = MOVE.NEUTRAL;
    
    public MOVE getMove(Game game, long timeDue)
    {
        // Place your game logic here to play the game as Ms Pac-Man
        
    	/*   *************兩功能版本*/
    	/*
        int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();
        int minDistance = Integer.MAX_VALUE;
        int minDistanceGh = Integer.MAX_VALUE;
        int minDistanceEdGh = Integer.MAX_VALUE;
        int closestPp;
        int closestP = 0;
        int mapNow = 0;

        int disTonearestPp = 0;
        int disTonearestP = 0;
        int disMinGhAndCloPp = 0;
        int nghWithNp = 0;
        int ghostLocation_now = 2;
        int edghostLocation_now = 2;
        int ghlairtimemux = 1;
        

        int D1 = 0;
        int D2 = 0;
        
        boolean ambush_stat = false ;

        //test.java
        try{
        	File myFile = new File("D:\\new.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            
            D1 = Integer.valueOf(reader.readLine());
            D2 = Integer.valueOf(reader.readLine());
            reader.close();
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}
        
        
        //System.out.println("D1 = "+D1+" D2 = "+D2);
        
        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        // 產生隨機Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);

        //顯示某點的所有可走鄰居
        //game.showNeighbour(1167);

        //不可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If can't see these will be -1 so all fine there
            // 對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) == 0 && game.getGhostLairTime_new(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
                int ghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromGh = game.getShortestPathDistance(current, ghostLocation);
                if (disFromGh < minDistanceGh)
                {
                    minDistanceGh = disFromGh;
                    minGhost = ghost;
                }
            }
            else if(game.getGhostLairTime_new(ghost)!=0)
            {
            	ghlairtimemux = ghlairtimemux * game.getGhostLairTime_new(ghost);
            	//若for 跑完 此值>0表全在籠子內
            }
        }
        //可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If it is > 0 then it is visible so no more PO checks
            //對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) > 0)
            {
                //如果這支鬼處於可食狀態
                int EdghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromEdGh = game.getShortestPathDistance(current, EdghostLocation);
                //取得此鬼跟小精靈最近距離
                if (disFromEdGh < minDistanceEdGh)
                {
                    //做完FOR迴圈之後會找到距離最短的可食鬼
                    minDistanceEdGh = disFromEdGh;
                    minEdGhost = ghost;
                }
            }
        }

        //確認PP是否存在
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)   // check with power pills
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable_new(i);
            // 這裡應為誤植，應改為game.isPowerPillStillAvailable(i);
            if (PowerpillStillAvailable != null)
            {
                if (PowerpillStillAvailable)
                {
                    targets.add(powerPills[i]);
                    // 如果這個大力丸存在，則存入target
                }
            }
        }
        //確認P是否存在
        ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)   // check with pills are available
        {
            Boolean pillStillAvailable = game.isPillStillAvailable_new(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // 如果這個藥丸存在，則存入target_p
                }
            }
        }
        if(!targets_p.isEmpty())  //還有p
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
        }
        if (!targets.isEmpty())   //還有PP存在之狀況
        {
            //轉換
            int[] targetsArray = new int[targets.size()]; // convert from
            // ArrayList to
            // array

            for (int i = 0; i < targetsArray.length; i++)
            {
                targetsArray[i] = targets.get(i);
            }
            closestPp = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
            disTonearestPp = game.getShortestPathDistance(current, closestPp);  
        }
        
        //=========================================判斷式開始
        if(minEdGhost!=null) //有可食鬼
        {
        	edghostLocation_now = game.getGhostCurrentNodeIndex_new(minEdGhost); //取得最近可食鬼的位置
        	if(minGhost!=null) //有一般鬼在籠子外
        	{
        		ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost); //取得最近鬼的位置
        		if(minDistanceGh<D2) //一般鬼太近
        		{
        			//System.out.println("Ghost too close MOVE away");
        			//遠離一般鬼
        			return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
        		}
        		else   //dis_gh >=D2，一般鬼在一定距離外
        		{
        			if(minDistanceEdGh<D1) // 和可食鬼距離近
        			{
        				//System.out.println("GO HUNTING");
        				//吃可食鬼
        				return game.getNextMoveTowardsTarget(current, edghostLocation_now, Constants.DM.PATH);
        			}
        			else //和可食鬼距離遠
        			{
        				//System.out.println("That edgh too far GO EAT N_P");
        				return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        			}
        		}
        	}
        	else //全部都是可食鬼
        	{
        		if(minDistanceEdGh<D1) // 和可食鬼距離近
    			{
        			//System.out.println("GO HUNTING(all edible)");
    				//吃可食鬼
    				return game.getNextMoveTowardsTarget(current, edghostLocation_now, Constants.DM.PATH);
    			}
    			else //和可食鬼距離遠
    			{
    				//System.out.println("That edgh too far GO EAT N_P");
    				//吃最近小藥丸
    				return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
    			}
        	}
        }
        else //無可食鬼
        {
        	if(ghlairtimemux > 1) // 全部一般鬼都在籠子內
        	{
        		//System.out.println("ALL Ghost IN CAGE,GO RANDOM");
        		switch (randdir)//隨機挑一個方向
                {
                case 0:
                    myMove = MOVE.UP;
                    break;
                case 1:
                    myMove = MOVE.RIGHT;
                    break;
                case 2:
                    myMove = MOVE.DOWN;
                    break;
                case 3:
                    myMove = MOVE.LEFT;
                    break;
                case 4:
                    myMove = MOVE.NEUTRAL;
                    break;
                }
        		//回傳此方向
        		return myMove;
        	}
        	else //有一般鬼跑出籠子了
        	{
        		if(minDistanceGh<D2) //一般鬼太近
        		{
        			//System.out.println("Ghost too close MOVE away");
        			//遠離一般鬼
        			return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
        		}
        		else//一般鬼在一定距離外
        		{
        			//System.out.println("Ghost far enough EAT N_P");
        			//吃最近小藥丸
        			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        		}
        	}
        }

        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP
        //兩功能版本*******************************************************
        */
    	
    	/*10功能版本*******************************************************/
    	
    	int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();
        int minDistance = Integer.MAX_VALUE;
        int minDistanceGh = Integer.MAX_VALUE;
        int minDistanceEdGh = Integer.MAX_VALUE;
        int closestPp;
        int closestP = 0;

        int disTonearestPp = 0;
        int disTonearestP = 0;
        int disMinGhAndCloPp = 0;
        int nghWithNp = 0;
        int ghostLocation_now = 2;
        int ghlairtimemux = 1;
        int ParameterCount = 13;
        /*
        int D1 = 12;
        int D2 = 16;
        int D3 = 50;
        int D4 = 32;
        int D5 = 8;
        int D6 = 0;
        int D7 = 50;
        int D8 = 30;
        int D9 = 6;
        int D10 = 10;
        int D11 = 17;
        int D12 = 28;
        int D13 = 100;
        */
        int D[] = new int[ParameterCount];
        
        int mapNow = 0;
        boolean ambush_stat = false ;

        /* D13 經過Randomd13.java產生的隨機Int */
        /*
         * Randomd13 dis = new Randomd13(); int D13 = dis.rand13;
         * System.out.println("D13_now : "+D13);
         */

        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        // 產生隨機Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);
        //開檔讀pamameter
        try{
        	File myFile = new File("D:\\new.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            for(int i = 0;i<ParameterCount;i++){
            	D[i] = Integer.valueOf(reader.readLine());
            }
            
            reader.close();
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}

        //顯示某點的所有可走鄰居
        //game.showNeighbour(1167);
        //判斷現在為哪一個map
        if(game.getCurrentMaze().name.equalsIgnoreCase("a")){
        	mapNow = 0;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("b")){
        	mapNow = 1;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("c")){
        	mapNow = 2;
        }
        else{
        	mapNow = 3;
        }
        
        //不可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If can't see these will be -1 so all fine there
            // 對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) == 0 && game.getGhostLairTime_new(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
                int ghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
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

        //可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If it is > 0 then it is visible so no more PO checks
            //對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) > 0)
            {
                //如果這支鬼處於可食狀態
                int EdghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromEdGh = game.getShortestPathDistance(current, EdghostLocation);
                //取得此鬼跟小精靈最近距離
                if (disFromEdGh < minDistanceEdGh)
                {
                    //做完FOR迴圈之後會找到距離最短的可食鬼
                    minDistanceEdGh = disFromEdGh;
                    minEdGhost = ghost;
                }
            }
        }

        //確認PP是否存在
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)   // check with power pills
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable_new(i);
            // 這裡應為誤植，應改為game.isPowerPillStillAvailable(i);
            if (PowerpillStillAvailable != null)
            {
                if (PowerpillStillAvailable)
                {
                    targets.add(powerPills[i]);
                    // 如果這個大力丸存在，則存入target
                }
            }
        }
        //確認P是否存在
        ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)   // check with pills are available
        {
            Boolean pillStillAvailable = game.isPillStillAvailable_new(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // 如果這個藥丸存在，則存入target_p
                }
            }
        }
        if(!targets_p.isEmpty())  //還有p
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
        }
        if (!targets.isEmpty())   //還有PP存在之狀況
        {
            //轉換
            int[] targetsArray = new int[targets.size()]; // convert from
            // ArrayList to
            // array

            for (int i = 0; i < targetsArray.length; i++)
            {
                targetsArray[i] = targets.get(i);
            }

            closestPp = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
            disTonearestPp = game.getShortestPathDistance(current, closestPp);
           // game.getCurrentMaze().shortestPathDistances
            //System.out.println(game.getNextMoveTowardsTarget(948, 12, Constants.DM.PATH));
            
            //判斷式開始=====================================================================
            // 有可食鬼存在
            if(minEdGhost!=null)
            {
            	if(minDistanceEdGh<D[12])
            	{
            		//System.out.println("Situation 1 : Go hunting");
            		return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
            	}
            	else if(minDistanceEdGh>D[12]&&minDistanceGh>D[1] &&minDistanceGh<=D[2]&&disTonearestP<=D[6])
            	{
            		//System.out.println("Situation 9 : Avoid danger eat n_p first");
            		return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            	}
            	
            }
            else //可食鬼不存在
            {
                if(minDistanceGh>=D[1] && minDistanceGh <=D[2] && disTonearestPp >=D[5] && disTonearestPp <=D[6])
                {
                	if(mapNow ==0)
                	{
                		//System.out.print("a");
                		if(current==1149||current==1095||current==1125||current==1155||current==1142||current==1136||current==1154||current==1160||current==91||current==85||current==103||current==109||current==96||current==90||current==108||current==114)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                	}
                	else if(mapNow ==1)
                	{
                		if(current==132||current==133||current==221||current==227||current==219||current==218||current==226||current==232||current==1084||current==1085||current==1151||current==1157||current==1149||current==1148||current==1156||current==1162)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                	}
                	else if(mapNow ==2)
                	{
                		if(current==115||current==109||current==127||current==133||current==114||current==120||current==132||current==138||current==1022||current==1023||current==1100||current==1106||current==1098||current==1097||current==1105||current==1111)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                	}
                	else if(mapNow ==3)
                	{
                		if(current==137||current==131||current==149||current==155||current==142||current==136||current==154||current==160||current==1164||current==1158||current==1176||current==1182||current==1169||current==1163||current==1181||current==1187)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                	}
                    //預備埋伏區間，察覺有危險
                	//System.out.println("Coming Ghost: " + minGhost);
                    
                    if(ambush_stat) //在ambush狀態下
                    {
                        if(minDistanceGh<=D[0])
                        {
                            //鬼太接近
                            ambush_stat = false;
                            //System.out.println("Situation 3 : Too Close Eat PP");
                            //eat pp
                            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                        }
                        else if(minDistanceGh >=D[1])
                        {
                        	//這裡要修正
                            //鬼漸漸遠離
                            ambush_stat = false;
                            //System.out.println("Situation 4 : Ghost GO Away Eat nearest p");
                            //eat n_p
                            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                        }
                        else
                        {
                        	//撞牆實作
                        	if(mapNow ==0) //MAZE_A
                        	{
                        		switch(current){
                        		case 85:
                        			return MOVE.UP;
                        		case 91:
                        			return MOVE.UP;
                        		case 103:
                        			return MOVE.DOWN;
                        		case 109:
                        			return MOVE.DOWN;
                        		case 96:
                        			return MOVE.UP;
                        		case 90:
                        			return MOVE.UP;
                        		case 108:
                        			return MOVE.DOWN;
                        		case 114:
                        			return MOVE.DOWN;
                        		case 1095:
                        			return MOVE.UP;
                        		case 1125:
                        			return MOVE.UP;
                        		case 1149:
                        			return MOVE.DOWN;
                        		case 1155:
                        			return MOVE.DOWN;
                        		case 1142:
                        			return MOVE.UP;
                        		case 1136:
                        			return MOVE.UP;
                        		case 1154:
                        			return MOVE.DOWN;
                        		case 1160:
                        			return MOVE.DOWN;
                        		}
                        	}
                        	else if(mapNow ==1)
                        	{
                        		switch(current){
                        		case 132:
                        			return MOVE.RIGHT;
                        		case 133:
                        			return MOVE.RIGHT;
                        		case 221:
                        			return MOVE.DOWN;
                        		case 227:
                        			return MOVE.DOWN;
                        		case 218:
                        			return MOVE.LEFT;
                        		case 219:
                        			return MOVE.LEFT;
                        		case 226:
                        			return MOVE.DOWN;
                        		case 232:
                        			return MOVE.DOWN;
                        		case 1084:
                        			return MOVE.RIGHT;
                        		case 1085:
                        			return MOVE.RIGHT;
                        		case 1151:
                        			return MOVE.DOWN;
                        		case 1157:
                        			return MOVE.DOWN;
                        		case 1148:
                        			return MOVE.LEFT;
                        		case 1149:
                        			return MOVE.LEFT;
                        		case 1156:
                        			return MOVE.DOWN;
                        		case 1162:
                        			return MOVE.DOWN;
                        		}
                        	}
                        	else if(mapNow ==2)
                        	{
                        		switch(current){
                        		case 109:
                        			return MOVE.UP;
                        		case 115:
                        			return MOVE.UP;
                        		case 127:
                        			return MOVE.DOWN;
                        		case 133:
                        			return MOVE.DOWN;
                        		case 114:
                        			return MOVE.UP;
                        		case 120:
                        			return MOVE.UP;
                        		case 132:
                        			return MOVE.DOWN;
                        		case 138:
                        			return MOVE.DOWN;
                        		case 1022:
                        			return MOVE.RIGHT;
                        		case 1023:
                        			return MOVE.RIGHT;
                        		case 1100:
                        			return MOVE.DOWN;
                        		case 1106:
                        			return MOVE.DOWN;
                        		case 1097:
                        			return MOVE.LEFT;
                        		case 1098:
                        			return MOVE.LEFT;
                        		case 1105:
                        			return MOVE.DOWN;
                        		case 1111:
                        			return MOVE.DOWN;
                        		}
                        	}
                        	else
                        	{
                        		switch(current){
                        		case 131:
                        			return MOVE.UP;
                        		case 137:
                        			return MOVE.UP;
                        		case 149:
                        			return MOVE.DOWN;
                        		case 155:
                        			return MOVE.DOWN;
                        		case 136:
                        			return MOVE.UP;
                        		case 142:
                        			return MOVE.UP;
                        		case 154:
                        			return MOVE.DOWN;
                        		case 160:
                        			return MOVE.DOWN;
                        		case 1158:
                        			return MOVE.UP;
                        		case 1164:
                        			return MOVE.UP;
                        		case 1176:
                        			return MOVE.DOWN;
                        		case 1182:
                        			return MOVE.DOWN;
                        		case 1163:
                        			return MOVE.UP;
                        		case 1169:
                        			return MOVE.UP;
                        		case 1181:
                        			return MOVE.DOWN;
                        		case 1187:
                        			return MOVE.DOWN;
                        		}
                        	}
                        	//System.out.println("Ambush~~~");
                        }
                    }
                    else //非ambush狀態但察覺有危險
                    {
                        //System.out.println("Situation 2 : In Danger Find Somewhere To Hide");
                        switch(closestPp)
                        {
                        case 1143:
                            return game.getNextMoveTowardsTarget(current, 1125, Constants.DM.PATH);
                        case 1148:
                            return game.getNextMoveTowardsTarget(current, 1142, Constants.DM.PATH);
                        case 97:
                            return game.getNextMoveTowardsTarget(current, 103, Constants.DM.PATH);
                        case 102:
                            return game.getNextMoveTowardsTarget(current, 108, Constants.DM.PATH);
                        case 131:
                            return game.getNextMoveTowardsTarget(current, 132, Constants.DM.PATH);
                        case 220:
                            return game.getNextMoveTowardsTarget(current, 219, Constants.DM.PATH);
                        case 1083:
                            return game.getNextMoveTowardsTarget(current, 1084, Constants.DM.PATH);
                        case 1150:
                            return game.getNextMoveTowardsTarget(current, 1149, Constants.DM.PATH);
                        case 121:
                            return game.getNextMoveTowardsTarget(current, 127, Constants.DM.PATH);
                        case 126:
                            return game.getNextMoveTowardsTarget(current, 132, Constants.DM.PATH);
                        case 1021:
                            return game.getNextMoveTowardsTarget(current, 1022, Constants.DM.PATH);
                        case 1099:
                            return game.getNextMoveTowardsTarget(current, 1098, Constants.DM.PATH);
                        case 143:
                            return game.getNextMoveTowardsTarget(current, 137, Constants.DM.PATH);
                        case 148:
                            return game.getNextMoveTowardsTarget(current, 142, Constants.DM.PATH);
                        case 1170:
                            return game.getNextMoveTowardsTarget(current, 1176, Constants.DM.PATH);
                        case 1175:
                            return game.getNextMoveTowardsTarget(current, 1181, Constants.DM.PATH);
                        }
                    }
                }
                else if(minDistanceGh<=D[0] && disTonearestPp <D[4])
                {
                	//System.out.println("Situation 5 : Avoid tunnel Eat PP now");
                    //eat pp
                    return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                }
                else if(minDistanceGh>=D[3] &&disTonearestPp >=D[7] &&disTonearestP<=D[8])
                {
                	//System.out.println("Situation 6 : Gh and pp too far Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
                else if(minDistanceGh>=D[3]&&disTonearestPp <=D[7] && disTonearestP<=D[10])
                {
                	//System.out.println("Situation 8: gh too far AVOID eat pp too early Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
            }
        }
        else // no pp exist
        {
        	if(minEdGhost!=null) //有可食鬼
        	{
        		if(minDistanceEdGh<D[12])
        		{
        			//System.out.println("Eat Nearest Edgh");
        			return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
        		}
        		else if(minDistanceEdGh>D[12] && minDistanceGh>D[1] &&minDistanceGh<=D[2] && disTonearestP<=D[6])
        		{
        			//System.out.println("Edgh too far Eat p first");
        			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        		}
        	}
        	else//沒有可食鬼
        	{
        		//10
        		//問題: 沒有PP沒有可食鬼 ，但自己又死了一次，此時就沒有 minGhost 的值了*******************************
        		//試解: 鬼在籠子裡的這段時間先隨機走
        		if(minGhost!=null)//鬼不在籠子裡
        		{
        			ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost);
            		nghWithNp = game.getShortestPathDistance(ghostLocation_now, closestP);
            		if(minDistanceGh<=D[0] && nghWithNp<=D[2])
            		{
            			//System.out.println("Situation 10 : Eat  remain p");
            			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            		}
            		else // 躲避鬼走法
            		{
            			//這裡要實作鬼夾擊之走法
            			//System.out.println("Avoiding too close ghost");
            			return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
            		}
        		}
        		else //鬼在籠子裡，此時沒有PP，沒有可食鬼
        		{
        			switch (randdir)
        	        {
        	        case 0:
        	            myMove = MOVE.UP;
        	            break;
        	        case 1:
        	            myMove = MOVE.RIGHT;
        	            break;
        	        case 2:
        	            myMove = MOVE.DOWN;
        	            break;
        	        case 3:
        	            myMove = MOVE.LEFT;
        	            break;
        	        case 4:
        	            myMove = MOVE.NEUTRAL;
        	            break;
        	        }
        			return myMove;
        		}
        		
        	}
        }


        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP
        
        //System.out.println("=================go random=================");
        switch (randdir)
        {
        case 0:
            myMove = MOVE.UP;
            break;
        case 1:
            myMove = MOVE.RIGHT;
            break;
        case 2:
            myMove = MOVE.DOWN;
            break;
        case 3:
            myMove = MOVE.LEFT;
            break;
        case 4:
            myMove = MOVE.NEUTRAL;
            break;
        }
		/*
						Problem : 
		1.鬼夾擊之走法 -L313
		  Situation 7 實作，不能只遠離鬼，還要去吃小藥丸，
		2.沒有PP沒有可食鬼，此時找不到minGhost，需再加一個判斷 -L303
		3.D2 D6 值的挑選 L190
		
		
		*/
        // System.out.println("MOVE : "+);
        //System.out.println("RAND_MOVE : " + myMove);
        //return myMove;
        //System.out.println("Follow nearest pill");
        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        //return MOVE.LEFT;
    	
    }
}
