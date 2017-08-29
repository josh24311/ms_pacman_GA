package entrants.pacman.josh24311;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;



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
        
    	
    	int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();
        //int minDistance = Integer.MAX_VALUE;
        int minDistanceGh = Integer.MAX_VALUE;
        int minDistanceEdGh = Integer.MAX_VALUE;
        int closestPp = 0;
        int closestP = 0;

        int disTonearestPp = 0;
        int disTonearestP = 0;
        //int disMinGhAndCloPp = 0;
        int nghWithNp = 0;
        int ghostLocation_now = 2;
        int ghlairtimemux = 1;
        int ParameterCount = 13;
        
        int D[] = new int[ParameterCount];
        //0823 new for special rule
        double risk[] = new double[4];
        double riskP  = 0;
        double riskPp  = 0;
        double riskGh  = 0;
        
        boolean ambush_stat = false ;
        int mapNow = 0;
       
        /*0817 Record ALL Ghost location*/
        ArrayList<Integer> gh = new ArrayList<Integer>();
        int[] ghArray = new int[4];
        
        int[] avoidGhostFindP = new int[100]; //record next index that A* return 
        /*0817 Record ALL Ghost location*/
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
        
        //四方位風險值初始化
        for(int i = 0;i<4;i++)
        {
        	risk[i] = 100;
        }
        
        
        
        //不可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
        	//0817
            //int ghostLocationEach =  game.getGhostCurrentNodeIndex_new(ghost);
        	
            //0817
            // If can't see these will be -1 so all fine there
            // 對於所有的鬼
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                //gh.add(ghostLocation);
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
            if (game.getGhostEdibleTime(ghost) > 0)
            {
                //如果這支鬼處於可食狀態
                int EdghostLocation = game.getGhostCurrentNodeIndex(ghost);
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
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable(i);
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
            //0823 risk
            if(disTonearestP < 10)
            {
            	//int riskx = 0;
            	//int risky = 0;
            	int xdif = 0;//toNode - fromNode
            	int ydif = 0;
            	xdif = game.getNodeXCood(closestP) - game.getNodeXCood(current);
            	ydif = game.getNodeYCood(closestP) - game.getNodeYCood(current);
            	if(xdif>0)//right
            	{
            		risk[1]-=10;
            	}
            	else if(xdif<0)//left
            	{
            		risk[3]-=10;
            	}
            	
            	if(ydif>0)//up
            	{
            		risk[0]-=10;
            	}
            	else if(ydif<0)//down
            	{
            		risk[2]-=10;
            	}
            	
            }
            
        }
        /*0817 Record ALL Ghost location*/
        /*
        if(!gh.isEmpty())
        {
        	for(int i = 0; i < gh.size(); i++)
        	{
        		ghArray[i] = gh.get(i);
        	}
        }*/
        /*0817 Record ALL Ghost location*/
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
			//0823 risk
            if(disTonearestPp < 10)
            {
            	//int riskx = 0;
            	//int risky = 0;
            	int xdif = 0;//toNode - fromNode
            	int ydif = 0;
            	xdif = game.getNodeXCood(closestPp) - game.getNodeXCood(current);
            	ydif = game.getNodeYCood(closestPp) - game.getNodeYCood(current);
            	if(xdif>0)//right
            	{
            		risk[1]-=20;
            	}
            	else if(xdif<0)//left
            	{
            		risk[3]-=20;
            	}
            	
            	if(ydif>0)//up
            	{
            		risk[0]-=20;
            	}
            	else if(ydif<0)//down
            	{
            		risk[2]-=20;
            	}
            	
            }
            //判斷式開始=====================================================================
            // 有可食鬼存在
            if(minEdGhost!=null)
            {
            	//System.out.println("dis(edg): "+minDistanceEdGh);
                if(minDistanceEdGh<D[12])
                {
                    //System.out.println("Situation 1 : Go hunting ghost:"+minEdGhost);
                    myMove = game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
                    //System.out.println("myMove: "+myMove);
                    return myMove;
                }
                if(minGhost!=null)
                {
                    if(minDistanceEdGh>D[12]&&minDistanceGh>D[1] &&minDistanceGh<=D[2]&&disTonearestP<=D[9])
                    {
                        //System.out.println("Situation 9 : Avoid danger eat n_p first");
                        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                    }
                }
            }
            else //可食鬼不存在
            {
                if( minDistanceGh <=D[2] && minDistanceGh>=D[1] && disTonearestPp >=D[5] && disTonearestPp <=D[6])
                {
                	//minDistanceGh>=D[1] && disTonearestPp >=D[5]
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

                    if(ambush_stat) //在hide狀態下
                    {
                        if(minDistanceGh<=D[0])
                        {
                            //r3 hide狀態且鬼追來
                            //鬼太接近
                            ambush_stat = false;
                            //System.out.println("Situation 3 : Too Close Eat PP");
                            //eat pp
                            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                        }
						
                        if(minDistanceGh >=D[1])
                        {
                            //r4 hide狀態且鬼遠離
                            //這裡要修正
                            //鬼漸漸遠離
                            ambush_stat = false;
                            //System.out.println("Situation 4 : Ghost GO Away Eat nearest p");
                            //eat n_p
                            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                        }

                        //撞牆實作
                        if(mapNow ==0) //MAZE_A
                        {
                            switch(current)
                            {
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
                            switch(current)
                            {
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
                            switch(current)
                            {
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
                        else if(mapNow ==3)
                        {
                            switch(current)
                            {
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
                
				if(minDistanceGh<=D[0] && disTonearestPp <D[4])
                {
                    //System.out.println("Situation 5 : Avoid tunnel Eat PP now");
                    //eat pp
                    return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                }
                
				if(minDistanceGh>=D[3] &&disTonearestPp >=D[7] &&disTonearestP<=D[8])
                {
                    //System.out.println("Situation 6 : Gh and pp too far Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
                
				if(minDistanceGh>=D[3]&&disTonearestPp <=D[7] && disTonearestP<=D[10])
                {
                    //System.out.println("Situation 8: gh too far AVOID eat pp too early Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
            }
		}
		else //沒有PP
		{
			if(minEdGhost!=null) //有可食鬼
            {
                if(minDistanceEdGh<D[12])
                {
                    //System.out.println("No PP Eat Nearest Edgh");
                    return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
                }
				if(minGhost!=null)
				{
					ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
					if(minDistanceGh<=D[0]&&disTonearestP<=D[11])
					{
						//r7 Then avoid ghost and eat nearest pill
						//先做 avoid 
						//System.out.println("r7 avoiding ghost");
						
						/*0817 AStar_new*/
						//avoidGhostFindP = game.getCurrentMaze().astar.computePathsAStar(current, closestP, game);
						//avoidGhostFindP = game.getCurrentMaze().astar_new.computePathsAStar(current, closestP, game, ghArray);
						//return game.getNextMoveTowardsTarget(current, avoidGhostFindP[1], Constants.DM.PATH);
						
						return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
					}
					if(minDistanceEdGh>D[12] && minDistanceGh>D[1] &&minDistanceGh<=D[2] && disTonearestP<=D[9])
					{
						// r9 有兩種狀態鬼 距離適中 先吃P
						//System.out.println("Two Type GH ,Edgh too far Eat p first");
						return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
					}
				}
            }
            else//沒有可食鬼
            {
                //10
                //問題: 沒有PP沒有可食鬼 ，但自己又死了一次，此時就沒有 minGhost 的值了*******************************
                //試解: 鬼在籠子裡的這段時間先隨機走
                if(minGhost!=null)//鬼不在籠子裡
                {
                    ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
                    nghWithNp = game.getShortestPathDistance(ghostLocation_now, closestP);
                    if(minDistanceGh<=D[0] && nghWithNp<=D[2])
                    {
                        //System.out.println("Situation 10 : No PP No Edg ,Eat  remain p");
                        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                    }
                    else // 躲避鬼走法
                    {
                        //這裡要實作鬼夾擊之走法
                        //System.out.println("Situation 10 : No PP No Edg,Avoiding too close ghost");
                        return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
                    }
                }
                else //鬼在籠子裡，此時沒有PP，沒有可食鬼
                {
                	//System.out.println("Ghost In cage,random first");
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
      

		//System.out.println("Not in rule");
        //implement special rule
        if(minGhost!=null)//有一般鬼
        {
        	
        	ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
        	int disFromGh = game.getShortestPathDistance(current, ghostLocation_now);
        	if(disFromGh<10) //此鬼和小精靈距離小於10
        	{
        		int xdif = 0;
            	int ydif = 0;
            	xdif = game.getNodeXCood(ghostLocation_now)-game.getNodeXCood(current);
            	ydif = game.getNodeYCood(ghostLocation_now)-game.getNodeYCood(current);
            	if(xdif>0)
            	{
            		if(disFromGh<5) risk[1]*=1.5;
            		else risk[1]*=1.3;
            	}
            	else if(xdif<0)
            	{
            		if(disFromGh<5) risk[3]*=1.5;
            		else risk[3]*=1.3;
            	}
            	
            	if(ydif>0)
            	{
            		if(disFromGh<5) risk[0]*=1.5;
            		else risk[0]*=1.3;
            	}
            	else if(ydif<0)
            	{
            		if(disFromGh<5) risk[2]*=1.5;
            		else risk[2]*=1.3;
            	}
        	}
        	//判斷四方位中risk最小者
        	int minindex = 0;
        	for(int i  = 0;i<risk.length;i++)
        	{
        		double minnow = risk[i];
        		if(minnow<risk[minindex])
        		{
        			minindex = i;
        		}
        	}
        	//System.out.println("follow the MIN risk route");
        	switch(minindex)
        	{
        	
        	case 0:
        		return MOVE.UP;
        	case 1:
        		return MOVE.RIGHT;
        	case 2:
        		return MOVE.DOWN;
        	case 3:
        		return MOVE.LEFT;
        	}
        	
        }
        
        //System.out.println("No ghost now ,not in any rules");
		return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        


        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP
        
        //System.out.println("=================go random=================");
        /*
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
        }*/
		
    	
    }
}
