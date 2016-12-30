package Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

/**
 * Created by mateu on 14.11.2016.
 */
public class ReadFile {

    BufferedImage img = null;
    int[] imgArray;
    int height;
    int width;

    static double[][] tab;


    public void imgReadFromFile(String path) {
        try {
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();

            imgArray = new int[height*width];
        } catch (IOException e) {
        }
    }

    public void imgToArray() {
        if(img != null){
            for(int i = 0; i < height; i++){
                for(int j=0; j< width; j++){
                    if(img.getRGB(i,j) == -1) imgArray[i+j*width] = 0;
                    else imgArray[i+j*width] = 1;
                }
            }
        }
    }

    public void showImgArray(){
        if(imgArray != null){
            for(int i = 0; i < width; i++){
                for(int j=0; j<height; j++){
                    System.out.print(imgArray[j+i*width] + " ");
                }
                System.out.println();
            }
        }
    }

    public static double[][] tabReadFromFile(String path, int from, int to, int rowSize){
        try(BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
            tab = new double[to-from][rowSize];

            String line;
            String[] tmp;

            for(int i = 0; i<from; i++) {
                line = br.readLine();
            }

            for(int i = 0 ; i<tab.length; i++){
                line = br.readLine();
                tmp = line.split(",", -1);
                for(int j = 0; j<rowSize; j++){
                    tab[i][j] = Integer.parseInt(tmp[j+1]);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tab;
    }
}
