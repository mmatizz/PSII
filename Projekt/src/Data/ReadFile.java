package Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by mateu on 14.11.2016.
 */
public class ReadFile {

    BufferedImage img = null;
    int[][] imgArray;
    int height;
    int width;

    public ReadFile(String path) {
        try {
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();

            imgArray = new int[height][width];
        } catch (IOException e) {
        }
    }

    public void imgToArray() {
        if(img != null){
            for(int i = 0; i < width; i++){
                for(int j=0; j<height; j++){
                    if(img.getRGB(i,j) == -1) imgArray[i][j] = 0;
                    else imgArray[i][j] = 1;
                }
            }
        }
    }

    public void showImgArray(){
        if(imgArray != null){
            for(int i = 0; i < height; i++){
                for(int j=0; j<width; j++){
                    System.out.print(imgArray[j][i] + " ");
                }
                System.out.println();
            }
        }
    }

    public void changePath(String path){
        try {
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();

            imgArray = new int[height][width];
        } catch (IOException e) {
        }
    }
}
