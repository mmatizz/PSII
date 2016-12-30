/**
 * Created by mateu on 29.12.2016.
 */
public class CharacterSet {
    protected int inputCount;
    protected int outputCount;
    protected double input[][];
    protected double output[][];
    protected int trainingSetCount;

    CharacterSet( int inputCount , int outputCount )
    {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        trainingSetCount = 0;
    }


    public void setTrainingSetCount(int trainingSetCount)
    {
        this.trainingSetCount = trainingSetCount;
        input = new double[trainingSetCount][inputCount];
        output = new double[trainingSetCount][outputCount];

    }

    public int getTrainingSetCount()
    {
        return trainingSetCount;
    }

    void setInput(int set,int index,double value)
            throws RuntimeException
    {
        input[set][index] = value;
    }

    public double[] getInputSet(int i) {
        return input[i];
    }
}
