import Data.ImageEntry;
import Data.SampleData;
import oracle.jrockit.jfr.JFR;

import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by mateu on 29.12.2016.
 */
public class MainFrame extends JFrame implements Runnable {

    static final int DOWNSAMPLE_WIDTH = 5;

    static final int DOWNSAMPLE_HEIGHT = 7;

    ImageEntry entry;

    protected SampleData sampleData;

    DefaultListModel letterListModel = new DefaultListModel();

    KohonenNeuralNetwork net;

    Thread trainThread = null;

    MainFrame()
    {
        getContentPane().setLayout(null);
        entry = new ImageEntry();
        entry.reshape(12,25,200,128);
        getContentPane().add(entry);

        sampleData = new SampleData(DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT);
        entry.setSampleData(sampleData);

        //INIT_CONTROLS
        setTitle("Rozpoznawanie liter");
        getContentPane().setLayout(null);
        setSize(400,400);
        setVisible(false);

        JLabel1.setText("Lista liter");
        getContentPane().add(JLabel1);
        JLabel1.setBounds(30,175,100,12);
        JLabel2.setBounds(12,264,72,24);

        add.setText("Dodaj");
        add.setActionCommand("Dodaj");
        getContentPane().add(add);
        add.setBounds(250,30,100,30);

        clear.setText("Wyczyść");
        clear.setActionCommand("Wyczyść");
        getContentPane().add(clear);
        clear.setBounds(250,75,100,30);

        recognize.setText("Rozpoznaj");
        recognize.setActionCommand("Rozpoznaj");
        getContentPane().add(recognize);
        recognize.setBounds(250,120,100,30);

        JScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane1.setOpaque(true);
        getContentPane().add(JScrollPane1);
        JScrollPane1.setBounds(12,190,144,132);
        JScrollPane1.getViewport().add(letters);
        letters.setBounds(200,200,126,129);

        del.setText("Usuń");
        del.setActionCommand("Usuń");
        getContentPane().add(del);
        del.setBounds(200,220,100,30);
        train.setText("Trenuj");
        train.setActionCommand("Trenuj");
        getContentPane().add(train);
        train.setBounds(200,260,100,30);



        //LISTENERS
        Action lSymAction = new Action();
        clear.addActionListener(lSymAction);
        add.addActionListener(lSymAction);
        del.addActionListener(lSymAction);
        ListSelection lSymListSelection = new ListSelection();
        letters.addListSelectionListener(lSymListSelection);
        train.addActionListener(lSymAction);
        recognize.addActionListener(lSymAction);
        letters.setModel(letterListModel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    JLabel JLabel1 = new JLabel();
    JLabel JLabel2 = new JLabel();
    JButton add = new JButton();
    JButton clear = new JButton();
    JButton recognize = new JButton();
    JScrollPane JScrollPane1 = new JScrollPane();
    JList letters = new JList();
    JButton del = new JButton();
    JButton train = new JButton();
    JLabel JLabel3 = new JLabel();

    //}}
    //{{DECLARE_MENUS
    //}}

    class Action implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == clear)
                clear_actionPerformed(event);
            else if (object == add)
                add_actionPerformed(event);
            else if (object == del)
                del_actionPerformed(event);
            else if (object == train)
                train_actionPerformed(event);
            else if (object == recognize)
                recognize_actionPerformed(event);
        }
    }


    void clear_actionPerformed(java.awt.event.ActionEvent event)
    {
        entry.clear();
        sampleData.clear();
    }

    void add_actionPerformed(java.awt.event.ActionEvent event)
    {
        int i;

        String letter = JOptionPane.showInputDialog(
                "Dodaj literę");
        if ( letter==null )
            return;

        if ( letter.length()>1 ) {
            JOptionPane.showMessageDialog(this,
                    "Litera musi być pojedyńczym znakiem","Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        entry.downSample();
        SampleData sD = (SampleData) entry.getSampleData().clone();
        sD.setLetter(letter.charAt(0));

        for ( i=0;i<letterListModel.size();i++ ) {
            Comparable str = (Comparable)letterListModel.getElementAt(i);
            if ( str.equals(letter) ) {
                JOptionPane.showMessageDialog(this,
                        "Podana litera już istnieje","Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ( str.compareTo(sD)>0 ) {
                letterListModel.add(i,sD);
                return;
            }
        }
        letterListModel.add(letterListModel.size(),sD);
        letters.setSelectedIndex(i);
        entry.clear();

    }

    /**
     * Called when the del button is pressed.
     *
     * @param event The event.
     */
    void del_actionPerformed(java.awt.event.ActionEvent event)
    {
        int i = letters.getSelectedIndex();

        if ( i==-1 ) {
            JOptionPane.showMessageDialog(this,
                    "Zaznacz literę do usunięcia.","Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        letterListModel.remove(i);
    }

    void train_actionPerformed(ActionEvent event)
    {
        if ( trainThread==null ) {
            train.setText("Stop Training");
            train.repaint();
            trainThread = new Thread(this);
            trainThread.start();
        } else {
            net.halt=true;
        }
    }

    /**
     * Called when the recognize button is pressed.
     *
     * @param event The event.
     */
    void recognize_actionPerformed(ActionEvent event)
    {
        if ( net==null ) {
            JOptionPane.showMessageDialog(this,
                    "Najpierw wytrenuj","Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        entry.downSample();

        double input[] = new double[5*7];
        int idx=0;

        for ( int y=0;y<sampleData.getHeight();y++ ) {
            for ( int x=0;x<sampleData.getWidth();x++ ) {
                input[idx++] = entry.getSampleData().getData(x,y)?.5:-.5;
            }
        }

        double norm[] = new double[1];

        int best = net.winner ( input , norm) ;
        char map[] = mapNeurons();
        JOptionPane.showMessageDialog(this,"  " + map[best] + "   (Odpalił Neuron #"+ best + ")","Litera to",JOptionPane.PLAIN_MESSAGE);
        clear_actionPerformed(null);

    }

    class ListSelection implements javax.swing.event.ListSelectionListener {
        public void valueChanged(javax.swing.event.ListSelectionEvent event)
        {
            Object object = event.getSource();
            if ( object == letters )
                letters_valueChanged(event);
        }
    }


    void letters_valueChanged(javax.swing.event.ListSelectionEvent event)
    {
        if ( letters.getSelectedIndex()==-1 )
            return;
        SampleData selected = (SampleData)letterListModel.getElementAt(letters.getSelectedIndex());
//                sample.setData((SampleData)selected.clone());
        sampleData = selected;
        entry.clear();

    }

    char []mapNeurons()
    {
        char map[] = new char[letterListModel.size()];
        double normfac[] = new double[1];

        for ( int i=0;i<map.length;i++ )
            map[i]='?';
        for ( int i=0;i<letterListModel.size();i++ ) {
            double input[] = new double[5*7];
            int idx=0;

            SampleData ds = (SampleData)letterListModel.getElementAt(i);
            for ( int y=0;y<ds.getHeight();y++ ) {
                for ( int x=0;x<ds.getWidth();x++ ) {
                    input[idx++] = ds.getData(x,y)?.5:-.5;
                }
            }

            int best = net.winner ( input , normfac) ;
            map[best] = ds.getLetter();
        }
            return map;
    }

    public void run()
    {
        try {
            int inputNeuron = 35;

            int outputNeuron = letterListModel.size();

            CharacterSet set = new CharacterSet(inputNeuron,outputNeuron);
            set.setTrainingSetCount(letterListModel.size());

            for ( int t=0;t<letterListModel.size();t++ ) {
                int idx=0;
                SampleData ds = (SampleData)letterListModel.getElementAt(t);
                for ( int y=0;y<ds.getHeight();y++ ) {
                    for ( int x=0;x<ds.getWidth();x++ ) {
                        set.setInput(t,idx++,ds.getData(x,y)?.5:-.5);
                    }
                }
            }

            net = new KohonenNeuralNetwork(inputNeuron,outputNeuron,this);
            net.setCharacterSet(set);
            net.learn();
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog(this,"Error: " + e,
                    "Trening",
                    JOptionPane.ERROR_MESSAGE);
        }

        if ( net.halt ) {
            trainThread = null;
            train.setText("Trenuj");
            JOptionPane.showMessageDialog(this,
                    "Trening zakończony.","Trening",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public static void main(String args[])
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                (new MainFrame()).show();
            }
        });

    }
}
