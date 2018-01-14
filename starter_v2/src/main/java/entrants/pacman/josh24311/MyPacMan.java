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
        int ParameterCount = 9;/*0913_13+10,1019_91204_9*/
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
        int ghdir = -1;/*0103 AStar_new 傳入鬼的方向*/

        int D[] = new int[ParameterCount];
        //0823 new for special rule
        double risk[] = new double[4];
        boolean ambush_stat = false ;
        int mapNow = 0;

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
        MOVE tmpmv;
        /*1103 statistics*/

        int r11=0,r12=0,r13=0,r14=0,r15=0,r16=0,r17=0,r18=0,r19=0,r110=0,r111=0;
        int r22=0,r23=0,r24=0,r25=0,r26=0,r27=0,r28=0,r29=0,r210=0,r211=0;
        int r33=0,r34=0,r35=0,r36=0,r37=0,r38=0,r39=0,r310=0,r311=0;
        int r44=0,r45=0,r46=0,r47=0,r48=0,r49=0,r410=0,r411=0;
        int r55=0,r56=0,r57=0,r58=0,r59=0,r510=0,r511=0;
        int r66=0,r67=0,r68=0,r69=0,r610=0,r611=0;
        int r77=0,r78=0,r79=0,r710=0,r711=0;
        int r88=0,r89=0,r810=0,r811=0;
        int r99=0,r910=0,r911=0;
        int r1010=0,r1011=0;
        int r1111=0;
        int rt1=0,rt2=0,rt3=0,rt4=0,rt5=0,rt6=0,rt7=0,rt8=0,rt9=0,rt10=0,rt11=0;
        int defaultnum = 0;
        boolean hastri = false;
        int totalRuleNum = 10;/*1124_10*/
        boolean[] rt = new boolean[totalRuleNum];
        int[] rtn = new int[totalRuleNum];
        int[][] r = new int[totalRuleNum][totalRuleNum];
        int[] AStarPath;/*1204*/
        //initialize
        for(int i = 0; i<totalRuleNum; i++)
        {
            rt[i] = false;
            rtn[i] = 0;
            for(int j=0; j<totalRuleNum; j++)
            {
                r[i][j] = 0;
            }
        }
        /*
        try
        {
            File myFile = new File("D:\\trigger.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            //121
            for(int i = 0; i<totalRuleNum; i++)
            {

                for(int j=0; j<totalRuleNum; j++)
                {
                    r[i][j] = Integer.valueOf(reader.readLine());
                }
            }
            //11
            for(int i = 0; i<totalRuleNum; i++)
            {
                rtn[i] = Integer.valueOf(reader.readLine());
            }
            //1
            defaultnum = Integer.valueOf(reader.readLine());
            reader.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
		*/

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
                if(pills[i]!=mkpa[now])  // 不相等將此pill加入 unmarked
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
                else //相等則將now這個pointer指向下個marked pill
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
            else  // now >= length,marked pills都比對完，剩下必定是unmarked
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
            // 對於所有的鬼
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
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
        if(minGhost!=null)  //若不可食鬼存在，取得其位置
        {
        	
            /*做risk 修正 */
            ghostLocation = game.getGhostCurrentNodeIndex(minGhost);
            ghostLocation_now = game.getGhostCurrentNodeIndex(minGhost);
            int disFromGh = game.getShortestPathDistance(current, ghostLocation_now);
            /*if(disFromGh<10)*/ //此鬼和小精靈距離小於10,10/1 take off
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
            /*0103 AStar_new 傳入鬼的方向*/
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
        if(minEdGhost!=null)  //若可食鬼存在，取得其位置
        {
            EdghostLocation = game.getGhostCurrentNodeIndex(minEdGhost);
        }
        //確認PP是否存在
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable(i);
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
            if(disTonearestP < 10)
            {
                int xdif = 0;//toNode - fromNode
                int ydif = 0;
                xdif = game.getNodeXCood(closestP) - game.getNodeXCood(current);//目標與pac之x差距
                ydif = game.getNodeYCood(closestP) - game.getNodeYCood(current);//目標與pac之y差距
                if(xdif>0) //right
                {
                    risk[1]-=10;
                }
                else if(xdif<0) //left
                {
                    risk[3]-=10;
                }

                if(ydif>0) //up
                {
                    risk[0]-=10;
                }
                else if(ydif<0) //down
                {
                    risk[2]-=10;
                }
            }
        }

        if (!targets.isEmpty())    //還有PP存在之狀況
        {
            //轉換
            int[] targetsArray = new int[targets.size()]; // convert from ArrayList to array
            for (int i = 0; i < targetsArray.length; i++)
            {
                targetsArray[i] = targets.get(i);
            }
            closestPp = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
            disTonearestPp = game.getShortestPathDistance(current, closestPp);
            //0823 risk
            /*disTonearestPp判定*/
            if(disTonearestPp < 10)
            {
                int xdif = 0;//toNode - fromNode
                int ydif = 0;
                xdif = game.getNodeXCood(closestPp) - game.getNodeXCood(current);
                ydif = game.getNodeYCood(closestPp) - game.getNodeYCood(current);
                if(xdif>0) //right
                {
                    risk[1]-=20;
                }
                else if(xdif<0) //left
                {
                    risk[3]-=20;
                }

                if(ydif>0) //up
                {
                    risk[0]-=20;
                }
                else if(ydif<0) //down
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
        /*1201_AStarPath*/
        
        
        
        /*statistics start*/

        /*r8*/
        if(!targets.isEmpty()&&minEdGhost!=null&&minGhost!=null&&minDistanceEdGh>=D[6]&&minDistanceGh>=D[0]&&minDistanceGh<=D[1]&&disTonearestP<=D[5])
        {
            rt[7] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[7]+=1;
            }
        }
        /*r2*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh>=D[0] && minDistanceGh <=D[1]&&disTonearestPp>=D[2]&&disTonearestPp<=D[2]+D[7])
        {
            rt[1] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[1]+=1;
            }
        }
        /*r4*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh>D[1])
        {
            try
            {
                File myFile = new File("D:\\ambush.txt");
                FileReader fileReader = new FileReader(myFile);
                BufferedReader reader = new BufferedReader(fileReader);
                ambush_stat = Boolean.valueOf(reader.readLine());
                reader.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            if(ambush_stat)
            {
                rt[3] = true;
                if(!hastri)
                {
                    hastri = true;
                    rtn[3]+=1;
                }
            }
        }
        /*r3*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh<D[0])
        {
            try
            {
                File myFile = new File("D:\\ambush.txt");
                FileReader fileReader = new FileReader(myFile);
                BufferedReader reader = new BufferedReader(fileReader);
                ambush_stat = Boolean.valueOf(reader.readLine());
                reader.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            if(ambush_stat)
            {
                rt[2] = true;
                if(!hastri)
                {
                    hastri = true;
                    rtn[2]+=1;
                }
            }
        }
        /*r10*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh<=D[1]&&minDistanceGh>=D[0])
        {
            rt[9] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[9]+=1;
            }
        }


        /*r6*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh>D[1] &&disTonearestPp>D[3]&&disTonearestP<=D[4])
        {
            rt[5] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[5]+=1;
            }
        }
        /*r7*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh>D[1] &&disTonearestPp<=D[3]&&disTonearestP<=D[5])
        {
            rt[6] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[6]+=1;
            }
        }
        /*r5*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh<=D[0] &&disTonearestPp<=D[2])
        {
            rt[4] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[4]+=1;
            }
        }
        /*r9*/
        if(targets.isEmpty()&&minEdGhost!=null&&minDistanceEdGh<D[6])
        {
            rt[8] = true;
            if(!hastri)
            {
                hastri = true;
                rtn[8]+=1;
            }
        }

        /*r1*/
        if(!targets.isEmpty()&&minEdGhost!=null && minDistanceEdGh <D[6])
        {
            rt[0] = true;//表示rule1 可以被trigger
            if(!hastri)
            {
                hastri = true;//flag 紀錄是否有rule已經被trigger
                rtn[0]+=1;//最後的紀錄，rule1 真正被trigger的次數+1
            }

        }



        if(!hastri)
        {
            /*no rule apply*/
            defaultnum+=1;
        }
        /*得到所有rules 可否 trigger 之情形*/
        /*
        ArrayList<Integer> cantrigger = new ArrayList<Integer>();
        for(int i =0; i<totalRuleNum; i++)
        {
            if(rt[i])
            {
                //若可以被trigger則存入
                cantrigger.add(i);
            }
        }
        if (!cantrigger.isEmpty())    //還有trigger 存在之狀況
        {
            //轉換
            int[] triggerArray = new int[cantrigger.size()]; // convert from ArrayList to array
            for (int i = 0; i < triggerArray.length; i++)
            {
                triggerArray[i] = cantrigger.get(i);//轉換至array
            }
            for(int i = 0; i<triggerArray.length; i++)
            {
                for(int j =i; j< triggerArray.length; j++)
                {
                    r[triggerArray[i]][triggerArray[j]] +=1;
                }
            }
        }
        try
        {
            FileWriter fw2 = new FileWriter("D:\\trigger.txt");
            for(int i = 0; i<totalRuleNum; i++)
            {
                for(int j = 0; j<totalRuleNum; j++)
                {
                    fw2.write(String.valueOf(r[i][j]));
                    fw2.write("\r\n");
                }
            }
            for(int i =0; i<totalRuleNum; i++)
            {
                fw2.write(String.valueOf(rtn[i]));
                fw2.write("\r\n");
            }
            fw2.write(String.valueOf(defaultnum));

            fw2.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
		*/
        
        
        
        
        
        
        /*decision start*/
        /*r8*/
        if(!targets.isEmpty()&&minEdGhost!=null&&minGhost!=null&&minDistanceEdGh>=D[6]&&minDistanceGh>=D[0]&&minDistanceGh<=D[0]+D[7]&&disTonearestP<=D[5])
        {
        	System.out.println("r8 in ");
        	AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,closestP,game,ghostLocation_now,ghdir);
        	//System.out.println("r8 out ");
        	return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
        }
        /*r2*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh>=D[0] && minDistanceGh <=D[0]+D[7]&&disTonearestPp>=D[2]&&disTonearestPp<=D[2]+D[8])
        {
        	System.out.println("r2 in ");
            ambush_stat = true;
            try
            {
                FileWriter fw2 = new FileWriter("D:\\ambush.txt");
                fw2.write(String.valueOf(ambush_stat));
                //fw2.write("\r\n");
                fw2.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            return game.getPacmanLastMoveMade().opposite();
        }
        /*r4*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh>D[1])
        {
        	System.out.print("r4 in :");
            try
            {
                File myFile = new File("D:\\ambush.txt");
                FileReader fileReader = new FileReader(myFile);
                BufferedReader reader = new BufferedReader(fileReader);
                ambush_stat = Boolean.valueOf(reader.readLine());
                reader.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            if(ambush_stat)
            {
            	System.out.println("r4 eat unmark ");
                ambush_stat = false;
                try
                {
                    FileWriter fw2 = new FileWriter("D:\\ambush.txt");
                    fw2.write(String.valueOf(ambush_stat));
                    //fw2.write("\r\n");
                    fw2.close();
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
                if(!unmarked.isEmpty())//若還有unmarked p
                {
                    int unmarkedArr[] = new int[unmarked.size()];
                    for(int i = 0; i<unmarkedArr.length; i++)
                    {
                        unmarkedArr[i] = unmarked.get(i);
                    }

                    int ttmp = game.getClosestNodeIndexFromNodeIndex(current, unmarkedArr, Constants.DM.PATH);
                    return game.getNextMoveTowardsTarget(current, ttmp, Constants.DM.PATH);
                }
                return game.getNextMoveTowardsTarget(current, 978, Constants.DM.PATH);
            }
        }
        /*r3*/
        if(!targets.isEmpty()&&minGhost!=null && minDistanceGh<D[0])
        {
        	System.out.print("r3 in :");
            try
            {
                File myFile = new File("D:\\ambush.txt");
                FileReader fileReader = new FileReader(myFile);
                BufferedReader reader = new BufferedReader(fileReader);
                ambush_stat = Boolean.valueOf(reader.readLine());
                reader.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            if(ambush_stat)
            {
            	System.out.println("r3 gh_too_n eat pp ");
                ambush_stat = false;
                try
                {
                    FileWriter fw2 = new FileWriter("D:\\ambush.txt");
                    fw2.write(String.valueOf(ambush_stat));
                    //fw2.write("\r\n");
                    fw2.close();
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }            
                return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
            }
        }
        /*r10*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh<=D[0]+D[7]&&minDistanceGh>=D[0])
        {
        	System.out.println("r10 in gh mid eat p");
            if(!unmarked.isEmpty())//若還有unmarked p
            {
                int unmarkedArr[] = new int[unmarked.size()];
                for(int i = 0; i<unmarkedArr.length; i++)
                {
                    unmarkedArr[i] = unmarked.get(i);
                }

                int ttmp = game.getClosestNodeIndexFromNodeIndex(current, unmarkedArr, Constants.DM.PATH);
                //System.out.println("r10 in ");
                AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,ttmp,game,ghostLocation_now,ghdir);
                //System.out.println("r10 out ");
                return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
            }
            else
            {
            	//System.out.println("r10-1 in ");
            	AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,closestP,game,ghostLocation_now,ghdir);
            	//System.out.println("r10-1 out ");
            	return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
            }
        }


        /*r6*/
        if(!targets.isEmpty()&&minEdGhost==null&&minGhost!=null&&minDistanceGh>D[1] &&disTonearestPp>D[3]&&disTonearestP<=D[4])
        {
        	System.out.println("r6 in gh_pp far eat p");
        	//System.out.println("r6 in current: "+current+" closestP:"+closestP+" ghostLocation_now: "+ghostLocation_now);
        	AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,closestP,game,ghostLocation_now,ghdir);
        	//System.out.println("r6 out ->path:");
        	for(int i =0;i<AStarPath.length;i++){
        		//System.out.print(AStarPath[i]+" ");
        	}
        	//System.out.println("");
        	return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
        }
        /*r7*/
        if(!targets.isEmpty()&&minEdGhost==null&&minGhost!=null&&minDistanceGh>D[1] &&disTonearestPp<=D[3]&&disTonearestP<=D[5])
        {
        	System.out.println("r7 in gh far eat p");
        	//System.out.println("r7 in current: "+current+" closestP:"+closestP+" ghostLocation_now: "+ghostLocation_now);
        	AStarPath = game.getCurrentMaze().astar_new.computePathsAStar(current,closestP,game,ghostLocation_now,ghdir);
        	//System.out.println("r7 out ->path:");
        	for(int i =0;i<AStarPath.length;i++){
        		//System.out.print(AStarPath[i]+" ");
        	}
        	//System.out.println("");
        	return game.getNextMoveTowardsTarget(current, AStarPath[1], Constants.DM.PATH);
        }
        /*r5*/
        if(!targets.isEmpty()&&minEdGhost==null&&minDistanceGh<=D[0] &&disTonearestPp<=D[2])
        {
        	System.out.println("r5 in gh n eat pp");
            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
        }
        /*r9*/
        if(targets.isEmpty()&&minEdGhost!=null&&minDistanceEdGh<D[6])
        {
        	System.out.println("r9 in eat edgh");
            return game.getNextMoveTowardsTarget(current, EdghostLocation,Constants.DM.PATH );
        }
        


        /*r1*/
        if(!targets.isEmpty()&&minEdGhost!=null && minDistanceEdGh <D[6])
        {
        	System.out.println("r1 in eat edgh");
            tmpmv = game.getNextMoveTowardsTarget(current, EdghostLocation,Constants.DM.PATH );
            return tmpmv;
        }


        /*default */
        //判斷四方位中risk最小者
        System.out.println("default ");
        
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

        
        
        /*
        System.out.println("");
        System.out.print("DF: np:");
        System.out.print(disTonearestP+" ");
        if(minGhost!=null) System.out.print("dGH"+minDistanceGh+" ");
        if(!targets.isEmpty()) System.out.print("dPP"+disTonearestPp+" ");
        if(minEdGhost!=null) System.out.print("dEDGH"+minDistanceEdGh+" ");*/

        return MOVE.NEUTRAL;
    }
}
