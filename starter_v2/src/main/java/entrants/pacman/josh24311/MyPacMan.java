package entrants.pacman.josh24311;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

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
        int ngh2npp = 0;
        int ghostLocation_now = 2;
        int ghostLocation = 0;
        int EdghostLocation = 0;
        int ghlairtimemux = 1;
        int ParameterCount = 9;/*0913_13+10,1019_9*/

        int D[] = new int[ParameterCount];
        //0823 new for special rule
        double risk[] = new double[4];
        double riskP  = 0;
        double riskPp  = 0;
        double riskGh  = 0;

        boolean ambush_stat = false ;
        boolean ambush_tmp = false;
        int mapNow = 0;

        /*0817 Record ALL Ghost location*/
        ArrayList<Integer> gh = new ArrayList<Integer>();
        int[] ghArray = new int[4];

        int[] avoidGhostFindP = new int[100]; //record next index that A* return
        /*0817 Record ALL Ghost location*/
        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        /*0831 safe points*/
        int[] sfpa = {153,165,225,237,940,1211,1020,1271};
        int[] sfpb = {151,430,200,449,942,1237,1004,1297};
        int[] sfpc = {166,281,191,286,895,1166,946,1233};
        int[] sfpd = {12,352,88,403,958,1230,1020,1295};
        int[] sfpa1 = {153,165};
        int[] sfpa2 = {225,237};
        int[] sfpa3 = {940,1211};
        int[] sfpa4 = {1020,1271};
        int[] sfpb1 = {151,430};
        int[] sfpb2 = {200,449};
        int[] sfpb3 = {942,1237};
        int[] sfpb4 = {1004,1297};
        int[] sfpc1 = {166,281};
        int[] sfpc2 = {191,286};
        int[] sfpc3 = {895,1166};
        int[] sfpc4 = {946,1233};
        int[] sfpd1 = {12,352};
        int[] sfpd2 = {88,403};
        int[] sfpd3 = {958,1230};
        int[] sfpd4 = {1020,1295};
        /*1019 marked pills*/
        int[] mkpa = {0,4,8,12,16,20,58,62,66,70,74,78,98,101,121,122,125,126,145,149,241,245,928,932,1024,1028,1047,1052,1071,1076,1095,1124,1167,1172,1191,1195,1199,1203,1207,1275,1279,1283,1287,1291};
        int[] mkpb = {135,139,143,147,151,155,159,192,196,200,204,208,212,216,239,244,263,268,287,332,357,375,394,399,418,422,426,453,457,461,1035,1040,1059,1064,1087,1091,1142,1146,1169,1174,1193,1198,1217,1221,1225,1229,1233,1301,1305,1309,1313,1317};
        int[] mkpc = {0,4,8,12,16,20,24,28,32,46,50,54,58,62,66,70,74,78,97,98,101,102,122,125,145,212,237,244,269,273,277,290,294,298,973,978,997,1002,1025,1029,1091,1095,1118,1123,1142,1147};
        int[] mkpd = {0,4,8,92,96,100,119,124,167,185,210,255,280,287,312,319,344,348,407,411,950,954,1024,1028,1047,1052,1071,1076,1095,1151,1194,1199,1218,1222,1226,1299,1303,1307};
        int closehnode = 0;
        int now = 0;
        /*10/06 vote version1*/
        int vote[] = new int[4];
        MOVE tmpmv ;
        for(int i = 0; i<4; i++)
        {
            vote[i] = 0;
        }
        
        
        
        
        // 產生隨機Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);
        //開檔讀pamameter
        try
        {
            File myFile = new File("D:\\new.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);

            for(int i = 0; i<ParameterCount; i++)
            {
                D[i] = Integer.valueOf(reader.readLine());
            }

            reader.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //顯示某點的所有可走鄰居
        //game.showNeighbour(1167);
        //判斷現在為哪一個map
        if(game.getCurrentMaze().name.equalsIgnoreCase("a"))
        {
            mapNow = 0;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("b"))
        {
            mapNow = 1;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("c"))
        {
            mapNow = 2;
        }
        else
        {
            mapNow = 3;
        }
        /*0830 closehnode find*/

        /*0830_end*/

        //四方位風險值初始化
        for(int i = 0; i<4; i++)
        {
            risk[i] = 100;
        }


        /*1019 unmarked pills set*/
        ArrayList<Integer> unmarked = new ArrayList<Integer>();
        for (int i = 0; i < pills.length ; i++)
        {
        	if(mapNow==0 && now <mkpa.length)
        	{
        		if(pills[i]!=mkpa[now])
    			{
        			Boolean pillStillAvailable = game.isPillStillAvailable(i);
                    if (pillStillAvailable != null)
                    {
                        if (pillStillAvailable)
                        {
                        	unmarked.add(pills[i]);
                        }
                    }
    			}
    			else
    			{
    				now++;
    			}
        	}
        	else if(mapNow==1 && now <mkpb.length)
        	{
        		if(pills[i]!=mkpb[now])
    			{
        			Boolean pillStillAvailable = game.isPillStillAvailable(i);
                    if (pillStillAvailable != null)
                    {
                        if (pillStillAvailable)
                        {
                        	unmarked.add(pills[i]);
                        }
                    }
    			}
    			else
    			{
    				now++;
    			}
        	}
        	else if(mapNow==2 && now <mkpc.length)
        	{
        		if(pills[i]!=mkpc[now])
    			{
        			Boolean pillStillAvailable = game.isPillStillAvailable(i);
                    if (pillStillAvailable != null)
                    {
                        if (pillStillAvailable)
                        {
                        	unmarked.add(pills[i]);
                        }
                    }
    			}
    			else
    			{
    				now++;
    			}
        	}
        	else if(mapNow==3 && now <mkpd.length)
        	{
        		if(pills[i]!=mkpd[now])
    			{
        			Boolean pillStillAvailable = game.isPillStillAvailable(i);
                    if (pillStillAvailable != null)
                    {
                        if (pillStillAvailable)
                        {
                        	unmarked.add(pills[i]);
                        }
                    }
    			}
    			else
    			{
    				now++;
    			}
        	}
        	else
        	{
        		Boolean pillStillAvailable = game.isPillStillAvailable(i);
                if (pillStillAvailable != null)
                {
                    if (pillStillAvailable)
                    {
                    	unmarked.add(pills[i]);
                    }
                }
        	}	
        }
        /*上面得到全部的unmark且已確認該點是否存在*/
        
        /*
        if(!unmarked.isEmpty())
        {
        	int unmarkedArr[] = new int[unmarked.size()];
    		for(int i = 0;i<unmarkedArr.length;i++)
    		{
    			unmarkedArr[i] = unmarked.get(i);
    		}
        }*/
        
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
                ghostLocation = game.getGhostCurrentNodeIndex(ghost);
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
                EdghostLocation = game.getGhostCurrentNodeIndex(ghost);
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
        if(minEdGhost!=null)
        {
            EdghostLocation = game.getGhostCurrentNodeIndex(minEdGhost);
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
            /*0831 ambush check which pp*/
            switch(mapNow)
            {
            case 0:
                switch(closestPp)
                {
                case 97:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpa1, Constants.DM.PATH);
                    break;
                case 102:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpa2, Constants.DM.PATH);
                    break;
                case 1143:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpa3, Constants.DM.PATH);
                    break;
                case 1148:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpa4, Constants.DM.PATH);
                    break;
                }

                break;
            case 1:
                switch(closestPp)
                {
                case 131:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpb1, Constants.DM.PATH);
                    break;
                case 220:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpb2, Constants.DM.PATH);
                    break;
                case 1083:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpb3, Constants.DM.PATH);
                    break;
                case 1150:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpb4, Constants.DM.PATH);
                    break;
                }
                break;
            case 2:
                switch(closestPp)
                {
                case 121:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpc1, Constants.DM.PATH);
                    break;
                case 126:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpc2, Constants.DM.PATH);
                    break;
                case 1021:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpc3, Constants.DM.PATH);
                    break;
                case 1099:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpc4, Constants.DM.PATH);
                    break;
                }
                break;
            case 3:
                switch(closestPp)
                {
                case 143:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpd1, Constants.DM.PATH);
                    break;
                case 148:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpd2, Constants.DM.PATH);
                    break;
                case 1170:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpd3, Constants.DM.PATH);
                    break;
                case 1175:
                    closehnode = game.getClosestNodeIndexFromNodeIndex(current, sfpd4, Constants.DM.PATH);
                    break;
                }

            }

            /*0831_end*/
        }
        /*判斷式開始*/
        /*0918幾個規則先給1*/
        
        
        if(!targets.isEmpty()&& minGhost!=null)
        {
        	ghostLocation = game.getGhostCurrentNodeIndex(minGhost);
        	ngh2npp = game.getShortestPathDistance(ghostLocation, closestPp);
        	/*r1*/
        	if(disTonearestPp<=D[1]&&minDistanceGh>=D[2]&&minDistanceGh<=D[0]&&ngh2npp>=D[1]+1)
        	{
        		ambush_stat = true;
        		try{
        			FileWriter fw2 = new FileWriter("D:\\ambush.txt");
        			fw2.write(String.valueOf(ambush_stat));
        			//fw2.write("\r\n");
        			fw2.close();
        			
        		}catch(IOException ex){
        			ex.printStackTrace();
        		}
        		//stop 如何實作?
        		System.out.println("r1 ambush");
        		return game.getPacmanLastMoveMade().opposite(); 
        	}
        	
        	
        	if(minEdGhost==null)
        	{
        		/*r2*/
        		if(minDistanceGh<=D[2]||ngh2npp<=D[3])
        		{
        			try{
                    	File myFile = new File("D:\\ambush.txt");
                        FileReader fileReader = new FileReader(myFile);
                        BufferedReader reader = new BufferedReader(fileReader);
                        ambush_stat = Boolean.valueOf(reader.readLine());
                        reader.close();
                	}catch(IOException ex){
                		ex.printStackTrace();
                	}
        			if(ambush_stat)
        			{
        				System.out.println("r2 ambush and go eat pp");
        				return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
        			}
        		}
        		/*r3*/
        		if(minDistanceGh<=D[0]&&disTonearestPp<=D[4])
        		{
        			System.out.println("r3 safe eat pp");
        			return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
        		}
        		/*r4*/
        		if(minDistanceGh<=D[0])
        		{
        			System.out.println("r4 safe eat pp");
        			return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
        		}
        		
        		
        	}
        	else // 至少一隻可食鬼
        	{
        		/*r5*/
        		if(minDistanceGh<=D[0]&&minDistanceEdGh<=D[5])
        		{
        			System.out.println("r5 safe eat edgh");
        			return game.getNextMoveTowardsTarget(current, EdghostLocation, Constants.DM.PATH);
        		}
        	}
        	
        	
        }
        
        /*r6*/
        if(!targets_p.isEmpty()&&minDistanceGh<=D[0])
        {
        	System.out.println("r6 eat p");
        	return game.getNextMoveTowardsTarget(current,closestP, Constants.DM.PATH);
        }
        /*r7*/
        if(minEdGhost!=null&&minDistanceGh>D[0]&&minDistanceEdGh<=D[6])
        {
        	System.out.println("r7 has edgh safe eat edgh");
        	return game.getNextMoveTowardsTarget(current, EdghostLocation, Constants.DM.PATH);
        }
        /*r8*/
        if(!targets_p.isEmpty() && !targets.isEmpty()&&minGhost!=null&& minDistanceGh > D[0] && (unmarked.isEmpty()||disTonearestPp>=D[7]&&disTonearestP<=D[8]))
        {
        	System.out.println("r8 eat p");
        	return game.getNextMoveTowardsTarget(current,closestP, Constants.DM.PATH);
        }
        /*r9*/
        if(!unmarked.isEmpty()&&minGhost!=null && minDistanceGh>D[0])
        {
        	int unmarkedArr[] = new int[unmarked.size()];
    		for(int i = 0;i<unmarkedArr.length;i++)
    		{
    			unmarkedArr[i] = unmarked.get(i);
    		}
    		closestP = game.getClosestNodeIndexFromNodeIndex(current, unmarkedArr, Constants.DM.PATH);
            //disTonearestP = game.getShortestPathDistance(current, closestP);
    		System.out.println("r9 eat unmarked p");
    		return game.getNextMoveTowardsTarget(current,closestP, Constants.DM.PATH);
        }
        
        
        
        
        
        return MOVE.NEUTRAL;
        //return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);



        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP

    }
}
