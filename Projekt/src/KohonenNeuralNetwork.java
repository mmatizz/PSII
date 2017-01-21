import Neuron.Neuron;

import java.util.ArrayList;
import java.util.List;

import static Neuron.Neuron.vectorLength;

/**
 * Created by mateu on 08.12.2016.
 */
public class KohonenNeuralNetwork {

    List<Neuron> neurons;
    int inputNeurons;
    int outputNeurons;
    List<Integer> groups;
    public boolean halt;

    public MainFrame mainFrame;

    public CharacterSet characterSet;

    public double totalError;

    public double learnRate;

    public double quitError;

    public double[][] outputWeights;

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

    public KohonenNeuralNetwork(int inputNeurons, int outputNweights, MainFrame mainFrame) {
        this.inputNeurons = inputNeurons;
        this.outputNeurons= outputNweights;
        this.mainFrame = mainFrame;

        neurons = new ArrayList<Neuron>();
        groups = new ArrayList<>();
        groups1 = new ArrayList<>();
        outputWeights = new double[outputNeurons][35];


    }

    public double[][] getTest() {
        return test;
    }

    public void setTest(double[][] test) {
        this.test = test;
    }

    public CharacterSet getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(CharacterSet characterSet) {
        this.characterSet = characterSet;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public void setInputs(double[][] inputs) {
        this.inputs = inputs;
    }

    public void initNetwork(){
        for(int i=0; i<outputNeurons; i++) {
            neurons.add(new Neuron(35));
        }

        totalError = 1.0;
        learnRate = 0.1;
        quitError = 0.1;
        halt = false;
    }

    public void initWeights(){
        if(!neurons.isEmpty()){
            for(Neuron n: neurons)
                n.randWeigths();
        }
    }

    public void normalizeInputs(){
        if(inputs.length <= 0)
            return ;

        double length = 0;

        for(int i = 0; i<inputs.length; i++){

            length = 0;

            for(int j=0; j<inputs[i].length; j++){
                length += inputs[i][j]*inputs[i][j];

            }

            length = Math.sqrt(length);

            if(length != 0){
                for(int j = 0; j<inputs[i].length; j++){
                    inputs[i][j] = inputs[i][j]/length;
                }
            }
        }
    }

    void normalizeInput(
            final double input[] ,
            double norm[]
    )
    {
        double length, d ;

        length = vectorLength ( input ) ;
// just in case it gets too small
        if ( length < 1.E-30 )
            length = 1.E-30 ;


        norm[0] = 1.0 / Math.sqrt ( length ) ;

    }


    public void setWinners(){
        if(inputs.length <= 0)
            return;

        normalizeInputs();

        for(int j = 0; j < inputs.length; j++) {

            double minDistance = neurons.get(0).EuclideanDistance(inputs[j]);
            int neuronId = 0;
            double dist = 0;

            for (int i = 0; i < neurons.size(); i++) {
                dist = neurons.get(i).EuclideanDistance(inputs[j]);
                if (dist > minDistance) {
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
                if (dist > minDistance){
                    neuronId = j;
                    minDistance = dist;
                }
            }
            addToGroup(neuronId,i);
        }
    }

    public void learn(){
        int i, key, tset,iter,n_retry,nwts;
        int won[],winners ;
        double correc[][], rate, best_err, dptr[];
        double bigerr[] = new double[1];
        double bigcorr[] = new double[1];

        initNetwork();
        initWeights();

        won = new int[outputNeurons];
        correc = new double[outputNeurons][35];

        for(iter=0; ; iter++){
            calcError(won, bigerr, correc);

            if ( bigerr[0] < quitError )
                break;



            winners=0;

            for ( i=0;i<won.length;i++ )
                if ( won[i]!=0 )
                    winners++;

            if ( (winners < outputNeurons)  &&
                    (winners < characterSet.getTrainingSetCount()) ) {
                forceWin ( won );
                continue;
            }
            adjustWeights (won , bigcorr, correc ) ;
        }

        for(i=0; i<outputNeurons; i++){
            neurons.get(i).normalizeWeights();
        }
        halt = true;
    }

    private void forceWin(int[] won) {
        int best, which=0;
        double tmpInputSet[], tmpWeights[], maxOutput, tmpOutput, minOutput;
        double norm[] = new double[1];

        minOutput = 1.E30;
        for(int charSet=0; charSet<characterSet.getTrainingSetCount(); charSet++){
            tmpInputSet = characterSet.getInputSet(charSet);
            best = winner(tmpInputSet, norm);
            tmpOutput = neurons.get(best).calcOutput(tmpInputSet, norm);
            if(tmpOutput < minOutput){
                minOutput = tmpOutput;
                which = charSet;
            }
        }

        tmpInputSet = characterSet.getInputSet(which);
        winner(tmpInputSet, norm);

        maxOutput = -1.E30;

        for(int i=outputNeurons-1; i>0; i--){
            if(won[i] != 0)
                continue;

            tmpOutput = neurons.get(i).getOutput();
            if(tmpOutput > maxOutput){
                maxOutput = tmpOutput;
                which = i;
            }
        }
        tmpWeights = neurons.get(which).getWeights();
        System.arraycopy(tmpInputSet, 0, tmpWeights, 0, tmpInputSet.length);
        neurons.get(which).normalizeWeights();
    }

    void adjustWeights (int won[] ,double bigcorr[],double correc[][])
    {
        double corr, tmpWeight[], length, correcTmp[];
        bigcorr[0] = 0.0 ;

        for(int i=0; i<outputNeurons; i++){
            if ( won[i]==0 )
                continue ;

            tmpWeight = neurons.get(i).getWeights();
            correcTmp = correc[i];

            length = 0.0;

            for(int j=0; j<inputNeurons; j++){
                corr = learnRate * correcTmp[j];
                tmpWeight[j] += corr;
                length += corr*corr;
            }

            neurons.get(i).setWeights(tmpWeight);

            if(length > bigcorr[0])
                bigcorr[0] = length;

            bigcorr[0] = Math.sqrt(bigcorr[0])/learnRate;
        }
    }

    public void calcError(int won[], double bigger[], double correc[][]){
        double tmpWeight[], norm[] = new double[1];
        double inputSetTmp[];
        double correcTmp[];
        double length, diff;
        int win;
        int charSet;

        for ( int y=0;y<correc.length;y++ ) {
            for ( int x=0;x<correc[0].length;x++ ) {
                correc[y][x]=0;
            }
        }
        for ( int i=0;i<won.length;i++ )
            won[i]=0;

        bigger[0] = -1.E30 ;

        for(charSet = 0; charSet<characterSet.getTrainingSetCount(); charSet++){
            inputSetTmp = characterSet.getInputSet(charSet);
            win = winner(inputSetTmp, norm);
            won[win]++;

            tmpWeight = neurons.get(win).getWeights();
            correcTmp = correc[win];
            length = 0.0;

            for(int i=0; i<inputNeurons; i++){
                diff = inputSetTmp[i] * norm[0] - tmpWeight[i];
                length += diff*diff;
                correcTmp[i] += diff;

                if(length > bigger[0])
                    bigger[0] = length;
            }
        }

        bigger[0] = Math.sqrt(bigger[0]);
    }

    public int winner(double input[], double norm[]){
        int i=0, win=0;
        double maxOutput, tmpOutput = 0;

        normalizeInput(input, norm);

        maxOutput = -1.E30;

        for (Neuron n: neurons) {
            tmpOutput = n.calcOutput(input, norm);
            if(tmpOutput > maxOutput){
                maxOutput = tmpOutput;
                win = i;
            }
            i++;
        }

        return win;
    }

    public void setGroupsIds(int id) {
        if(!groups.contains(id)){
            groups.add(id);
        }
    }

    public void setOutputWeights(){
        for(int i=0; i<outputNeurons; i++){
            outputWeights[i] = neurons.get(i).getWeights();
        }
    }

    public void setOutputWeightsToNeurons(){
        for(int i=0; i<outputNeurons; i++){
            neurons.get(i).setWeights(outputWeights[i]);
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
