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
        final  int MAX_ITER = 30;             // max number of iterations
        final  double MUTATION_RATE = 0.05;     // probability of mutation
        final  double CROSSOVER_RATE = 0.7;     // probability of crossover

        int ParameterCount = 13;
        int GeneLength = 7;
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
        System.out.print("Enter Mode(1: For run experiment, 2: For parameters testing): ");
        mode = scanner.nextInt();
        if(mode==2)//testing mode
        {
            D[0] = 42;//35
            D[1] = 127;//66
            D[2] = 25;//109
            D[3] = 23;//84
            D[4] = 89;//70
            D[5] = 43;//62
            D[6] = 57;//51
            D[7] = 65;//98
            D[8] = 107;//126
            D[9] = 20;//91
            D[10] = 27;//19
            D[11] = 7;//46
            D[12] = 74;//4
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

            for(int i = 0; i<ParameterCount; i++)
            {
                for(int j = 0; j<GeneLength; j++)
                {
                    sum += bestIndiv.getGene(gnow) <<bit;
                    bit++;
                    gnow++;
                }
                bit = 0;
                D[i] = sum;
                System.out.println("D["+i+"]= "+D[i]);
                sum = 0;
            }
        }
        
    }
}
