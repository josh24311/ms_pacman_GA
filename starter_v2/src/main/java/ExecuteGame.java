import examples.StarterGhostComm.Blinky;
import examples.StarterGhostComm.Inky;
import examples.StarterGhostComm.Pinky;
import examples.StarterGhostComm.Sue;
import entrants.pacman.josh24311.MyPacMan;
//import examples.StarterPacMan.*;


import pacman.Executor;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.game.Constants.*;


import java.util.EnumMap;
import java.io.*;

public class ExecuteGame {
	public double run(int[] D,int mode){
		//GetScore  gs = new GetScore();
		int testTrial  = 10;
		int ParameterCount = 13+10;/*0913*/
		double avgs = 0;
		
		try{
			FileWriter fw = new FileWriter("D:\\new.txt");
			for(int i = 0;i<ParameterCount;i++)
			{
				fw.write(String.valueOf(D[i]));
				fw.write("\r\n");
			}
			
			fw.close();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		/*0830 ambush_stat initial*/
		/*
        try{
			FileWriter fw2 = new FileWriter("D:\\ambush.txt");
			fw2.write(String.valueOf(false));
			fw2.write("\r\n");
			fw2.close();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}*/
        
        
        
        /*0830_end*/
	    Executor executor = new Executor(true, true);

	    EnumMap<GHOST, IndividualGhostController> controllers = new EnumMap<>(GHOST.class);

	    controllers.put(GHOST.INKY, new Inky());
	    controllers.put(GHOST.BLINKY, new Blinky());
	    controllers.put(GHOST.PINKY, new Pinky());
	    controllers.put(GHOST.SUE, new Sue());
	    
	    //executor.runGameTimed(new MyPacMan(), new MASController(controllers), true);
	    //executor.runGame(new MyPacMan(), new MASController(controllers), true, 10);
	    //executor.runGameTimedSpeedOptimised(new MyPacMan(), new MASController(controllers), false, true, "waituntil");
	    //executor.runExperiment(new MyPacMan(), new MASController(controllers), testTrial, "EX", 8000);
	    //System.out.println("D1 = "+D1+" D2 = "+D2);
	    if(mode==1) //Experiment Mode
	    {
	    	avgs = executor.runExperiment_new(new MyPacMan(), new MASController(controllers), testTrial, "EX", 8000);
	    }
	    else //Testing Parameters Mode
	    {
	    	avgs = executor.runGame(new MyPacMan(), new MASController(controllers), true, 20);
	    }
	    
	    
	    return avgs;
	}
}
