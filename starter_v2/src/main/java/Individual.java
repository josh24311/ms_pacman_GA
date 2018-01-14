import java.util.Random;
public class Individual {
	public static final int SIZE = 63;/*0913_91+10,1204_63*/
	public static final int ParameterCount = 9;/*0913_13+10,1204_9*/
	public static final int DisParLength = 7;/*1017*/
    private int[] genes = new int[SIZE];
	//genes[0]~genes[100]
    private int fitnessValue;

    public Individual() {}

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public int getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int gene) {
        this.genes[index] = gene;
		//500百個基因，每個可能是0或1
    }

    public void randGenes() {
        Random rand = new Random();
        for(int i=0; i<SIZE; ++i) { //0~90
            this.setGene(i, rand.nextInt(2));
        }
    }

    public void mutate() {
        Random rand = new Random();
        int index = rand.nextInt(SIZE);
        this.setGene(index, 1-this.getGene(index));    // flip
    }

    public int evaluate() {
    	int bit=0;    //做2的升次運算
    	int sum = 0;  //累計一個parameter的值總和
    	int gnow = 0; //紀錄目前基因位置
    	int D[] = new int[ParameterCount];
		
    	//i<ParameterCount-10
		for(int i = 0; i<ParameterCount; i++){
			for(int j = 0; j<DisParLength; j++){
				sum += this.genes[gnow] <<bit;
				bit++;
				gnow++;
			}
			bit = 0;
			D[i] = sum;
			sum = 0;
		}
		/*
		for(int i = 13;i<ParameterCount;i++)
		{
			D[i] = this.genes[gnow];
			gnow++;
		}*/
    	
        int fitness = 0;
        double avgs = 0;
    	ExecuteGame play = new ExecuteGame();
    	avgs = play.run(D,1);
    	fitness = (int)avgs;
    	this.setFitnessValue(fitness);
        return fitness;
    }
}
