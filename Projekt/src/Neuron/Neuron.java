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

    void randWeigths(){
        Random rand = new Random();
        for(int i=0; i<weights.length; i++){
            weights[i] = rand.nextDouble();
        }
    }

    double getOutput(double[] tab){
        if(tab == null || tab.length != weights.length)
            return 0;

        output = 0;

        for(int i=0; i<weights.length; i++){
            output += weights[i]*tab[i];
        }

        return output;
    }

    void normalizeWeights(){
        double length = lengthWeights();
        if(length != 0){
            for(int i = 0; i<weights.length; i++){
                weights[i] = weights[i]/length;
            }
        }
    }

    void correctWeights(double[] inputs){
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

    double EuclideanDistance(double[] inputs){
        if(inputs.length != weights.length)
            return -1;

        double distance = 0;
        for(int i = 0; i< weights.length; i++){
            distance += (inputs[i] - weights[i])*(inputs[i] - weights[i]);
        }

        return Math.sqrt(distance);
    }
}
