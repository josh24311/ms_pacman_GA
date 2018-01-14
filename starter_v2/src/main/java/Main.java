import java.io.*;
import java.util.Random;
import java.util.Scanner;



/**
 * Created by pwillic on 06/05/2016.
 */
public class Main
{

    public static void main(String[] args) throws IOException
    {
        final  int ELITISM_K = 5;
        final  int POP_SIZE = 40 + ELITISM_K;  // population size
        final  int MAX_ITER = 100;             // max number of iterations
        final  double MUTATION_RATE = 0.05;     // probability of mutation
        final  double CROSSOVER_RATE = 0.95;     // probability of crossover

        int ParameterCount = 9;/*0913_13+10,1019_9,1204_9*/
        int GeneLength = 7;/*1017*/
        //int SIZE = ParameterCount*GeneLength;
        int gnow = 0;
        int bit = 0;
        int sum = 0;
        int count = 0;
        //0817
        int mode = 0;
        double score = 0;


        int[] D = new int[ParameterCount];
        Random m_rand = new Random();
        // 宣告 node 類別物件

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Mode(1: For run experiment, 2: For parameters testing, 3: For fast get score ): ");
        mode = scanner.nextInt();
        if(mode==2)//testing mode
        {
            D[0] = 50;
            D[1] = 26;
            D[2] = 113;
            D[3] = 27;
            D[4] = 0;
            D[5] = 58;
            D[6] = 83;
            D[7] = 37;
            D[8] = 75;
            /*
            D[8] = 35;
            D[9] = 127;
            D[10] = 32;
            D[11] = 74;
            D[12] = 104;
            
            /*
            D[13] = 1;
            D[14] = 1;
            D[15] = 1;
            D[16] = 0;
            D[17] = 1;
            D[18] = 1;
            D[19] = 1;
            D[20] = 0;
            D[21] = 1;
            D[22] = 1;
            */
            ExecuteGame play = new ExecuteGame();
            score = play.run(D,2);
            System.out.println("Testing Score: "+score);
        }
        else if(mode==1) //experiment mode
        {
            Population pop = new Population();
            Individual[] newPop = new Individual[POP_SIZE];
            Individual[] indiv = new Individual[2];

            // GA main loop

            for (int iter = 0; iter < MAX_ITER; iter++)
            {
                System.out.println("=====================Iteration:"+iter);
                count = 0;

                // Elitism
                for (int i=0; i<ELITISM_K; ++i)
                {
                    newPop[count] = pop.findBestIndividual();
                    count++;
                }

                // build new Population
                while (count < POP_SIZE)
                {
                    //5<15
                    // Selection
                    indiv[0] = pop.rouletteWheelSelection();
                    indiv[1] = pop.rouletteWheelSelection();

                    // Crossover
                    if ( m_rand.nextDouble() < CROSSOVER_RATE )
                    {
                        //indiv = crossover(indiv[0], indiv[1]);
                        indiv = Population.crossover(indiv[0], indiv[1]);
                    }

                    // Mutation
                    if ( m_rand.nextDouble() < MUTATION_RATE )
                    {
                        indiv[0].mutate();
                    }
                    if ( m_rand.nextDouble() < MUTATION_RATE )
                    {
                        indiv[1].mutate();
                    }

                    // add to new population
                    newPop[count] = indiv[0];
                    newPop[count+1] = indiv[1];
                    count += 2;
                }
                pop.setPopulation(newPop);

                // reevaluate current population
                pop.evaluate();
                //System.out.print("Total Fitness = " + pop.totalFitness);
                //System.out.println(" ; Best Fitness = " +pop.findBestIndividual().getFitnessValue());
            }

            // best indiv
            Individual bestIndiv = pop.findBestIndividual();
            System.out.println("Best Score of Last GEN: "+bestIndiv.getFitnessValue());
            
            //<ParameterCount-10
            for(int i = 0; i<ParameterCount; i++)
            {
                for(int j = 0; j<GeneLength; j++)
                {
                    sum += bestIndiv.getGene(gnow) <<bit;
                    bit++;
                    gnow++;
                }
                bit = 0;
                D[i] = sum;//得到一個長度
                System.out.println("D["+i+"]= "+D[i]);
                sum = 0;
            }
            
            try{
    			FileWriter fw2 = new FileWriter("D:\\lastGen.txt");
    			for(int k = 0;k<POP_SIZE;k++)
    			{
    				bestIndiv = pop.getIndividual(k);
    				fw2.write("\r\n");
    				fw2.write("Individual "+k+":");
    				fw2.write("\r\n");
    				for(int i = 0; i<ParameterCount; i++)
    				{
    					for(int j = 0; j<GeneLength; j++)
    					{
    						sum += bestIndiv.getGene(gnow) <<bit;
    						bit++;
    						gnow++;
    					}
    					bit = 0;
    					D[i] = sum;//得到一個長度
    					//System.out.println("D["+i+"]= "+D[i]);
    					fw2.write(" D["+i+"]= "+D[i]+"_");
    					sum = 0;
    				}
    			}
    			
    			//fw2.write(String.valueOf(ambush_stat));
    			//fw2.write("\r\n");
    			fw2.close();
    			
    		}catch(IOException ex){
    			ex.printStackTrace();
    		}
            //列出gene[91]~gene[100]
            /*
            for(int i = 13;i<ParameterCount;i++)
            {
            	D[i] = bestIndiv.getGene(gnow);
            	gnow++;
            	System.out.println("D["+i+"]= "+D[i]);
            }*/
        }
        else
        { //mode ==3
        	 D[0] = 40;
             D[1] = 94;
             D[2] = 112;
             D[3] = 43;
             D[4] = 49;
             D[5] = 110;
             D[6] = 117;
             D[7] = 11;
             /*
             D[8] = 35;
             D[9] = 127;
             D[10] = 32;
             D[11] = 74;
             D[12] = 104;
             
             /*
             D[13] = 1;
             D[14] = 1;
             D[15] = 1;
             D[16] = 0;
             D[17] = 1;
             D[18] = 1;
             D[19] = 1;
             D[20] = 0;
             D[21] = 1;
             D[22] = 1;
             */
             ExecuteGame play = new ExecuteGame();
             score = play.run(D,3);
             System.out.println("Testing Score: "+score);
        }
        
    }
}
