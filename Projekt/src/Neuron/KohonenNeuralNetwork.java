package Neuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateu on 08.12.2016.
 */
public class KohonenNeuralNetwork {

    List<Neuron> neurons;
    int Nneurons;
    int Nweights;
    List<Integer> groups;

    private List<List<Integer>> groups1;

    double[][] test1 = {
            {7, 11, 6, 6, 3, 5, 9, 4, 6, 4, 4, 10, 6, 10, 2, 8}
    };

    double[][] inputs1= {
            {2, 8, 3, 5, 1, 8, 13, 0, 6, 6, 10, 8, 0, 8, 0, 8},
            {5, 12, 3, 7, 2, 10, 5, 5, 4, 13, 3, 9, 2, 8, 4, 10},
            {4, 11, 6, 8, 6, 10, 6, 2, 6, 10, 3, 7, 3, 7, 3, 9},
            {7, 11, 6, 6, 3, 5, 9, 4, 6, 4, 4, 10, 6, 10, 2, 8},
            {2, 1, 3, 1, 1, 8, 6, 6, 6, 6, 5, 9, 1, 7, 5, 10},
            {4, 11, 5, 8, 3, 8, 8, 6, 9, 5, 6, 6, 0, 8, 9, 7}
    };

    double[][] inputs;
    double[][] test;

    public KohonenNeuralNetwork(int Nneurons, int Nweights) {
        this.Nneurons = Nneurons;
        this.Nweights = Nweights;

        neurons = new ArrayList<Neuron>();
        groups = new ArrayList<>();
        groups1 = new ArrayList<>();

    }

    public double[][] getTest() {
        return test;
    }

    public void setTest(double[][] test) {
        this.test = test;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public void setInputs(double[][] inputs) {
        this.inputs = inputs;
    }

    public void initNetwork(){
        for(int i=0; i<Nneurons; i++) {
            neurons.add(new Neuron(Nweights));
        }
    }

    public void initWeights(){
        if(!neurons.isEmpty()){
            for(Neuron n: neurons)
                n.randWeigths();
        }
    }


    public void setWinners(){
        if(inputs.length <= 0)
            return;

        for(int j = 0; j < inputs.length; j++) {

            double minDistance = neurons.get(0).EuclideanDistance(inputs[j]);
            int neuronId = 0;
            double dist = 0;

            for (int i = 0; i < neurons.size(); i++) {
                dist = neurons.get(i).EuclideanDistance(inputs[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    neuronId = i;
                }
            }

            neurons.get(neuronId).correctWeights(inputs[j]);
            neurons.get(neuronId).normalizeWeights();
            setGroupsIds(neuronId);
            setGroupsIds1(neuronId);
        }
    }


    public void train(){
        for(int i=0; i<test.length; i++){
            double minDistance = neurons.get(0).EuclideanDistance(test[i]);
            int neuronId = 0;
            double dist = 0;
            for (int j = 0; j < neurons.size(); j++) {
                dist = neurons.get(j).EuclideanDistance(test[i]);
                if (dist < minDistance){
                    neuronId = j;
                    minDistance = dist;
                }
            }

            addToGroup(neuronId,i);
        }
    }
    public void setGroupsIds(int id) {
        if(!groups.contains(id)){
            groups.add(id);
        }
    }




    public void setGroupsIds1(int id){
        boolean exists = false;
        for(List list: groups1){
            if(list.get(0).equals(id)){
                exists = true;
                break;
            }
        }

        if(!exists) {
            groups1.add(new ArrayList<Integer>());
            groups1.get(groups1.size()-1).add(id);
        }
   }

    public void addToGroup(int neuronId, int idTest){
        for(List list: groups1){
            if(list.get(0).equals(neuronId))
                list.add(idTest);

        }
    }

    public void showGroups1(){
        for(List list: groups1){
            System.out.print(list.get(0) + ": ");
            for(int i=1; i< list.size(); i++){
                System.out.print(list.get(i) + ", ");
            }
            System.out.println();
        }
    }

}
