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
        int ghostLocation_now = 2;
        int EdghostLocation = 0;
        int ghlairtimemux = 1;
        int ParameterCount = 13+10;/*0913*/

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
        int[] sfpa = {0,145,78,245,1071,1191,1076,1291};
        int[] sfpb = {139,263,212,268,1091,1193,1142,1198};
        int[] sfpc = {0,237,78,244,1029,1142,1091,1147};
        int[] sfpd = {0,210,100,255,1071,1218,1076,1307};
        int[] sfpa1 = {0,145};
        int[] sfpa2 = {78,245};
        int[] sfpa3 = {1071,1191};
        int[] sfpa4 = {1076,1291};
        int[] sfpb1 = {139,263};
        int[] sfpb2 = {212,268};
        int[] sfpb3 = {1091,1193};
        int[] sfpb4 = {1142,1198};
        int[] sfpc1 = {0,237};
        int[] sfpc2 = {78,244};
        int[] sfpc3 = {1021,1142};
        int[] sfpc4 = {1099,1147};
        int[] sfpd1 = {0,210};
        int[] sfpd2 = {100,255};
        int[] sfpd3 = {1071,1218};
        int[] sfpd4 = {1076,1307};
        int closehnode = 0;
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
        
        /*r9*/
        
        if(minEdGhost!=null&&minGhost!=null&&D[21]==1) //有兩種狀態的鬼
        {
            if(minDistanceEdGh>D[12]&&minDistanceGh>D[1]&&minDistanceGh<=D[2]&&disTonearestP<=D[9])
            {
                //System.out.println("two kinds of ghost ,edg not close, eat p first");
                return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            }
        }
        

        /*r2 go to ambush point*/
        
        if(D[14]==1)
        {
            if(!targets.isEmpty())
            {
                if(minGhost!=null)
                {
                    if(minDistanceGh>=D[1] && minDistanceGh <=D[2] && disTonearestPp>=D[5] && disTonearestPp <=D[6])
                    {
                        //System.out.println("go to ambush point");
                        return game.getNextMoveTowardsTarget(current, closehnode, Constants.DM.PATH);
                    }
                }
            }
        }
		
        /*判斷是否到埋伏點*/
        
        if(!targets.isEmpty())
        {
            if(minGhost!=null)
            {
                if(current==closehnode) //到達埋伏點
                {
                    ambush_stat = true;
                    //System.out.println("right here ambush point");
                }
            }
        }

        //若已無PP ,或所在地不是埋伏點 ，則必不會有埋伏狀態
        if(targets.isEmpty()||current!=closehnode)
        {
            ambush_stat = false;
        }
        
        /*r3*/
        
        if(ambush_stat && D[15]==1) //在埋伏狀態
        {
            //System.out.println("ambush now");
            if(minDistanceGh<=D[0]) 
            {
                //System.out.println("ghost too close eat pp");
                ambush_stat = false;
                //回傳往最近PP走
                return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
            }
            //not r3 nor r4 return same place
            //System.out.println("remain ambush point in D[15]");
            //return game.getNextMoveTowardsTarget(current, closehnode, Constants.DM.PATH);
        }
        
        /*r4*/
        
        if(ambush_stat && D[16]==1) //在埋伏狀態
        {
            if(minDistanceGh>=D[2]) 
            {
                //System.out.println("ghost go away , go opp");
                ambush_stat = false;
                //回傳往前一步的相反
                return game.getPacmanLastMoveMade().opposite();
                //或者要他直接走到 978
            }
            //not r3 nor r4 return same place
            //System.out.println("remain ambush point in D[16]");
            //return game.getNextMoveTowardsTarget(current, closehnode, Constants.DM.PATH);
        }
        /*r1  eat edghost */
        if(D[13]==1)
        {
            if(minEdGhost!=null)
            {
                //int EdghostLocation = game.getGhostCurrentNodeIndex(minEdGhost);
                if(minDistanceEdGh < D[12])
                {
                    //System.out.println("eat Edghost");
                    return game.getNextMoveTowardsTarget(current, EdghostLocation,Constants.DM.PATH );
                }
            }
        }
		
        /*r5*/
        if(!targets.isEmpty()&&minGhost!=null && D[17]==1) //還有PP且至少一隻不可食鬼
        {
            if(minEdGhost==null)
            {
                if(minDistanceGh<=D[0] && disTonearestPp<D[4])
                {
                    //System.out.println("in case gh go tunnel ,eat pp");
                    return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                }
            }
        }
        /*r6*/
        if(!targets.isEmpty()&&minGhost!=null && D[18]==1) //還有PP且至少一隻不可食鬼
        {
            if(minDistanceGh>=D[3] && disTonearestPp>=D[7] && disTonearestP<=D[8])
            {
                //System.out.println("ghost and pp are far,eat p first");
                return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            }
        }


        /*r7*/
        /*Q: how to do avoid ghost?*/
        if(targets.isEmpty()&&D[19]==1) //no more pp
        {
            if(minDistanceGh<=D[0]&&disTonearestP<=D[11])
            {
                //System.out.println("no pp,eat close p first");
                return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            }
        }
        /*r8*/
        
        if(minDistanceGh>=D[3]&&disTonearestPp<=D[7]&&disTonearestP<=D[10]&&D[20]==1)
        {
            //System.out.println("in case eat pp too early,eat p first");
            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        }
        
        
        
        /*r10*/
        
        //System.out.println("Not in rule");
        //implement special rule
        if(minGhost!=null && D[22]==1)//有一般鬼
        {
            ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
            int disFromGh = game.getShortestPathDistance(current, ghostLocation_now);
            /*if(disFromGh<10)*/ //此鬼和小精靈距離小於10,10/1 take off
            if(true)
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
            double minnow = 0;
            for(int i  = 0; i<risk.length; i++)
            {
                minnow = risk[i];
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

    }
}
