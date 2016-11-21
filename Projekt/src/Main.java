import Data.ReadFile;

/**
 * Created by mateu on 14.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        ReadFile file = new ReadFile("resources/fonts/Calibri/A.png");
        file.imgToArray();
        file.showImgArray();

        System.out.println();

        file.changePath("resources/fonts/TimesNR/A.png");
        file.imgToArray();
        file.showImgArray();
    }
}
