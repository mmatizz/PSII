package Neuron;


import java.io.Serializable;
import java.util.Random;

/**
 * Created by mateu on 14.11.2016.
 */
public class Neuron implements Serializable{

    double[] weights;
    double output;
    double learningRate;

    public Neuron(int Nweights) {
        output = 0;
        weights = new double[Nweights];
        learningRate = 0.1;
    }

    public void randWeigths(){
        Random rand = new Random();
        for(int i=0; i<weights.length; i++){
            weights[i] = rand.nextDouble();
        }
    }

    public double getOutput(double[] tab, double norm[]){
        if(tab == null || tab.length != weights.length)
            return 0;

        output = 0;

        for(int i=0; i<weights.length; i++){
            output += weights[i]*tab[i];
        }

        output *= norm[0];

        return output;
    }

    public void normalizeWeights(){
        double length = lengthWeights();
        if(length != 0){
            for(int i = 0; i<weights.length; i++){
                weights[i] = weights[i]/length;
            }
        }
    }

    public static double vectorLength( double v[] )
    {
        double rtn = 0.0 ;
        for ( int i=0;i<v.length;i++ )
            rtn += v[i] * v[i];
        return rtn;
    }



    public void correctWeights(double[] inputs){
        if(inputs.length != weights.length)
            return;

        for(int i = 0; i<weights.length; i++){
            weights[i] = learningRate * (inputs[i] - weights[i]);
        }
    }

    double lengthWeights(){
        if(weights.length <= 0)
            return 0;

        double length = 0;
        for(int i = 0; i<weights.length; i++){
            length += weights[i]*weights[i];
        }

        return Math.sqrt(length);
    }

    double calcInputWeight(double[] inputs){
        if(inputs.length != weights.length)
            return -1;

        double val = 0;
        for(int i = 0; i< weights.length; i++){
            val += weights[i]*inputs[i];
        }

        return val;
    }

    public double EuclideanDistance(double[] inputs){
        if(inputs.length != weights.length)
            return -1;

        double distance = 0;
        for(int i = 0; i< weights.length; i++){
            distance += (inputs[i] - weights[i])*(inputs[i] - weights[i]);
        }

        return Math.sqrt(distance);
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }
}
