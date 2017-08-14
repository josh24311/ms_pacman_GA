import java.util.Random;
public class Individual {
	public static final int SIZE = 91;
	public static final int ParameterCount = 13;
    private int[] genes = new int[SIZE];
	//genes[0]~genes[13]
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
		int D1 = 0;
		int D2 = 0;
		int D3 = 0;
		int D4 = 0;
		int D5 = 0;
		int D6 = 0;
		int D7 = 0;
		int D8 = 0;
		int D9 = 0;
		int D10 = 0;
		int D11 = 0;
		int D12 = 0;
		int D13 = 0;
		for(int i = 0; i<ParameterCount; i++){
			for(int j = 0; j<SIZE/ParameterCount; j++){
				sum += this.genes[gnow] <<bit;
				bit++;
				gnow++;
			}
			bit = 0;
			D[i] = sum;
			sum = 0;
		}
		
    	
        int fitness = 0;
        double avgs = 0;
    	ExecuteGame play = new ExecuteGame();
    	avgs = play.run(D);
    	fitness = (int)avgs;
    	this.setFitnessValue(fitness);
        return fitness;
    }
}
