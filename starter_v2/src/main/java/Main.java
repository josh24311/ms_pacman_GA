import java.io.*;
import java.util.Random;



/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {

    public static void main(String[] args) throws IOException{
    	final  int ELITISM_K = 5;
        final  int POP_SIZE = 40 + ELITISM_K;  // population size
        final  int MAX_ITER = 20;             // max number of iterations
        final  double MUTATION_RATE = 0.05;     // probability of mutation
        final  double CROSSOVER_RATE = 0.7;     // probability of crossover
        
        int ParameterCount = 13;
        int GeneLength = 7;
        int SIZE = ParameterCount*GeneLength;
        int gnow = 0;
        int bit = 0;
        int sum = 0;
        int[] D = new int[ParameterCount];
        Random m_rand = new Random();
    	// 宣告 node 類別物件
    	/*
    	Test t = new Test();
    	t.setVar();
    	
    	try{
    		FileWriter fw = new FileWriter("D:\\new.txt");
    		fw.write(String.valueOf(t.D1));
    		fw.write("\r\n");
    		fw.write(String.valueOf(t.D2));
    		fw.close();
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}*/
    	/*
    	double avgs = 0;
    	ExecuteGame play = new ExecuteGame();
    	avgs = play.run();*/
    	
    	//System.out.println("Average Score = "+avgs);
    	Population pop = new Population();
        Individual[] newPop = new Individual[POP_SIZE];
        Individual[] indiv = new Individual[2];

        // current population
        //System.out.print("Total Fitness = " + pop.totalFitness);
        //System.out.println(" ; Best Fitness = " + pop.findBestIndividual().getFitnessValue());

        // main loop
        int count;
        for (int iter = 0; iter < MAX_ITER; iter++) {
        	System.out.println("=====================Iteration:"+iter);
            count = 0;

            // Elitism
            for (int i=0; i<ELITISM_K; ++i) {
                newPop[count] = pop.findBestIndividual();
                count++;
            }

            // build new Population
            while (count < POP_SIZE) {
				//5<15
                // Selection
                indiv[0] = pop.rouletteWheelSelection();
                indiv[1] = pop.rouletteWheelSelection();

                // Crossover
                if ( m_rand.nextDouble() < CROSSOVER_RATE ) {
                    //indiv = crossover(indiv[0], indiv[1]);
                    indiv = Population.crossover(indiv[0], indiv[1]);
                }

                // Mutation
                if ( m_rand.nextDouble() < MUTATION_RATE ) {
                    indiv[0].mutate();
                }
                if ( m_rand.nextDouble() < MUTATION_RATE ) {
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
        
        for(int i = 0; i<ParameterCount; i++){
			for(int j = 0; j<GeneLength; j++){
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
