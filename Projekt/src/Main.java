import Data.ReadFile;
import Neuron.KohonenNeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * Created by mateu on 14.11.2016.
 */
public class Main{


    public static void main(String[] args) {
//        ReadFile file = new ReadFile();
//        file.imgReadFromFile("resources/fonts/Calibri/A.png");
//        file.imgToArray();
//        file.showImgArray();
//
//        System.out.println();
//
//        file.imgReadFromFile("resources/fonts/TimesNR/A.png");
//        file.imgToArray();
//        file.showImgArray();

        double[][] inputs;
        double[][] tests;

        inputs = ReadFile.tabReadFromFile("resources/data/letter-recognition.data.txt", 0, 700, 16);
        tests = ReadFile.tabReadFromFile("resources/data/letter-recognition.data.txt", 0, 300 , 16);


        int Nneurons = 26;
        int Nweights = 16;


        KohonenNeuralNetwork hokonen = new KohonenNeuralNetwork(Nneurons, Nweights);

        hokonen.setInputs(inputs);
        hokonen.setTest(tests);

        hokonen.initNetwork();
        hokonen.initWeights();

        int epoch = 4;
        for(int i=0; i<epoch; i++) {
            hokonen.setWinners();
        }

        hokonen.train();

        hokonen.showGroups1();



    }
}
