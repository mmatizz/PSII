package Data;

import Data.SampleData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

/**
 * Created by mateu on 28.12.2016.
 */
public class ImageEntry extends JPanel{

    protected Image entryImage;

    protected Graphics entryGraphics;

    protected int lastX = -1;

    protected int lastY = -1;

    protected SampleData sampleData;

    protected int downSampleLeft;

    protected int downSampleRight;

    protected int downSampleTop;

    protected int downSampleBottom;

    protected double ratioX;

    protected double ratioY;

    protected int pixelMap[];

    public ImageEntry(){
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|
                AWTEvent.MOUSE_EVENT_MASK|
                AWTEvent.COMPONENT_EVENT_MASK);
    }

    protected void initImage(){
        entryImage = createImage(getWidth(),getHeight());
        entryGraphics = entryImage.getGraphics();
        entryGraphics.setColor(Color.white);
        entryGraphics.fillRect(0,0,getWidth(),getHeight());
    }

    public void paint(Graphics g){
        if ( entryImage==null )
            initImage();
        g.drawImage(entryImage,0,0,this);
        g.setColor(Color.black);
        g.drawRect(0,0,getWidth(),getHeight());

    }

    protected void processMouseEvent(MouseEvent e){
        if ( e.getID()!=MouseEvent.MOUSE_PRESSED )
            return;
        lastX = e.getX();
        lastY = e.getY();
    }

    protected void processMouseMotionEvent(MouseEvent e){
        if ( e.getID()!=MouseEvent.MOUSE_DRAGGED )
            return;

        entryGraphics.setColor(Color.black);
        entryGraphics.drawLine(lastX,lastY,e.getX(),e.getY());
        getGraphics().drawImage(entryImage,0,0,this);
        lastX = e.getX();
        lastY = e.getY();
    }

    public void setSampleData(SampleData s)
    {
        sampleData = s;
    }

    public SampleData getSampleData()
    {
        return sampleData;
    }

    protected boolean hLine(int y){
        int w = entryImage.getWidth(this);
        for ( int i=0;i<w;i++ ) {
            if ( pixelMap[(y*w)+i] !=-1 )
                return false;
        }
        return true;
    }

    protected boolean vLine(int x)
    {
        int w = entryImage.getWidth(this);
        int h = entryImage.getHeight(this);
        for ( int i=0;i<h;i++ ) {
            if ( pixelMap[(i*w)+x] !=-1 )
                return false;
        }
        return true;
    }

    protected void findBounds(int w,int h)
    {
        for ( int y=0;y<h;y++ ) {
            if ( !hLine(y) ) {
                downSampleTop=y;
                break;
            }
        }

        for ( int y=h-1;y>=0;y-- ) {
            if ( !hLine(y) ) {
                downSampleBottom=y;
                break;
            }
        }

        for ( int x=0;x<w;x++ ) {
            if ( !vLine(x) ) {
                downSampleLeft = x;
                break;
            }
        }
        for ( int x=w-1;x>=0;x-- ) {
            if ( !vLine(x) ) {
                downSampleRight = x;
                break;
            }
        }
    }

    protected boolean downSampleQuadrant(int x,int y){
        int w = entryImage.getWidth(this);
        int startX = (int)(downSampleLeft+(x*ratioX));
        int startY = (int)(downSampleTop+(y*ratioY));
        int endX = (int)(startX + ratioX);
        int endY = (int)(startY + ratioY);

        for ( int yy=startY;yy<=endY;yy++ ) {
            for ( int xx=startX;xx<=endX;xx++ ) {
                int loc = xx+(yy*w);

                if ( pixelMap[ loc  ]!= -1 )
                    return true;
            }
        }
        return false;
    }


    public void downSample()
    {
        int w = entryImage.getWidth(this);
        int h = entryImage.getHeight(this);

        PixelGrabber grabber = new PixelGrabber(
                entryImage,
                0,
                0,
                w,
                h,
                true);
        try {

            grabber.grabPixels();
            pixelMap = (int[])grabber.getPixels();
            findBounds(w,h);

            ratioX = (double)(downSampleRight-
                    downSampleLeft)/(double)sampleData.getWidth();
            ratioY = (double)(downSampleBottom-
                    downSampleTop)/(double)sampleData.getHeight();

            for ( int y=0;y<sampleData.getHeight();y++ ) {
                for ( int x=0;x<sampleData.getWidth();x++ ) {
                    if ( downSampleQuadrant(x,y) )
                        sampleData.setData(x,y,true);
                    else
                        sampleData.setData(x,y,false);
                }
            }
            repaint();
        } catch ( InterruptedException e ) {
        }
    }

    public void clear()
    {
        this.entryGraphics.setColor(Color.white);
        this.entryGraphics.fillRect(0,0,getWidth(),getHeight());
        this.downSampleBottom =
                this.downSampleTop =
                        this.downSampleLeft =
                                this.downSampleRight = 0;
        repaint();
    }
}
