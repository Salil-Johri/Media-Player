package media_player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*; //.JFrame, .JLabel, .JPanel, etc
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Ahmed, Connor, and Salil
 */
public class MediaPlayerGUI extends MouseAdapter implements ActionListener, ChangeListener {

    JFrame frame;
    JPanel mainCP, contentPane0, contentPane1, contentPane2, contentPane3, contentPane4;
    JButton playSong, nextSong, prevSong, shuffle, selectSong; //add functionality for reset/generateLife
    JSlider videoSpeed = new JSlider(0, 200, 100);
    AudioProgress duration = new AudioProgress();
    private int speed = 100;
    private static Timer time;
    private String dir = "";
    private int songQ = 0;
    private SongQueue songQueue = new SongQueue();
    private boolean shuffleOn = false;

    Chooser choose = new Chooser();
    Audio player = new Audio();
    Runner songThread = new Runner();

    /**
     * Constructor pre: none post: none
     */
    public MediaPlayerGUI() {
        /* Create and set up the frame */
        frame = new JFrame("Media Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Create the main content pane */
        mainCP = new JPanel();
        mainCP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainCP.setBackground(Color.gray);
        mainCP.setLayout(new BoxLayout(mainCP, BoxLayout.PAGE_AXIS));

        /* Create the supporting content panes */
        contentPane0 = new JPanel();
        contentPane0.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane1 = new JPanel();
        contentPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane2 = new JPanel();
        contentPane2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane2.setLayout(new FlowLayout());

        contentPane3 = new JPanel();
        contentPane3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane3.setLayout(new FlowLayout());

        contentPane4 = new JPanel();
        contentPane4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane4.setLayout(new FlowLayout());

        /*Slider to adjust song time*/
        contentPane0.add(duration);

        /* create and add the buttons for user interactions */
        prevSong = new JButton("Previous Song");
        prevSong.addActionListener(this);
        prevSong.setActionCommand("prevSong");
        prevSong.setPreferredSize(new Dimension(120, 20));
        prevSong.setBackground(Color.cyan);
        prevSong.setForeground(Color.blue);
        contentPane2.add(prevSong);

        playSong = new JButton("Play");
        playSong.addActionListener(this);
        playSong.setActionCommand("play");
        playSong.setPreferredSize(new Dimension(120, 20));
        playSong.setBackground(Color.cyan);
        playSong.setForeground(Color.blue);
        contentPane2.add(playSong);

        nextSong = new JButton("Next Song");
        nextSong.addActionListener(this);
        nextSong.setActionCommand("nextSong");
        nextSong.setPreferredSize(new Dimension(120, 20));
        nextSong.setBackground(Color.cyan);
        nextSong.setForeground(Color.blue);
        contentPane2.add(nextSong);

        shuffle = new JButton("Shuffle Song");
        shuffle.addActionListener(this);
        shuffle.setActionCommand("shuffleSong");
        shuffle.setPreferredSize(new Dimension(120, 20));
        shuffle.setBackground(Color.green);
        shuffle.setForeground(Color.black);
        contentPane3.add(shuffle);

        selectSong = new JButton("Select Song");
        selectSong.addActionListener(this);
        selectSong.setActionCommand("selectSong");
        selectSong.setPreferredSize(new Dimension(120, 20));
        selectSong.setBackground(Color.green);
        selectSong.setForeground(Color.black);
        contentPane3.add(selectSong);

        /* Create slider for changing speed of video */
        videoSpeed.setMajorTickSpacing(50);
        videoSpeed.setMinorTickSpacing(25);
        videoSpeed.setPaintTicks(true);
        videoSpeed.setPaintLabels(true);
        videoSpeed.setSnapToTicks(true);
        videoSpeed.addChangeListener(this);
        videoSpeed.setForeground(Color.black);
        contentPane4.add(videoSpeed);

        /* Set up timer for tracking the location in the song */
        time = new Timer(speed, new gameTimer());
        time.stop();

        /* Add content panes to the main content pane */
//        mainCP.add(contentPane0);
        mainCP.add(contentPane1);
        mainCP.add(contentPane2);
        mainCP.add(contentPane3);
        mainCP.add(contentPane4);

        /* Add content pane to frame */
        frame.setContentPane(mainCP);

        /* Size and then display the frame. */
//        frame.setSize(900, 900);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        MediaPlayerGUI project = new MediaPlayerGUI();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MediaPlayerGUI::runGUI);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
     public void actionPerformed(ActionEvent e) {
        int temp = 0;
        String eventName = e.getActionCommand();
        switch (eventName) {
            case "play":
                if(songQueue.getNumFiles() > 0) {
                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                    if (!dir.equals("")) {
                        songThread.start();
                    } else {
                        if(shuffleOn) {
                            do {
                                temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
//                                System.out.println(temp);
                            }while (temp == songQ);

                            songQ = temp;
                        }
                        player.setFilePath(songQueue.getNextSong(songQ));
                        if (songThread.isAlive()) {
                            songThread.stop();
                        }
                        songThread = new Runner();
                        songThread.setAudioPlayer(player);
                        songThread.start();

                        time.start();
                        playSong.setActionCommand("pause");
                        playSong.setText("Pause");
                    }
                }
                break;
            case "pause":
                time.stop();
                playSong.setActionCommand("resume");
                playSong.setText("Resume");
                songThread.suspend();
                break;
            case "resume":
                time.start();
                playSong.setActionCommand("pause");
                playSong.setText("Pause");
                songThread.resume();
                break;
            case "nextSong":
                //go forward 1 song from directory
                if(songQueue.getNumFiles() > 0) {
                    time.stop();
                    if(shuffleOn) {
                        do {
                            temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
                        }while (temp == songQ);

                        songQ = temp;
                    } else {
                        songQ += 1;
                    }

                    if(songQ >= songQueue.getNumFiles()) {
                        songQ = 0;
                    }
                    player.setFilePath(songQueue.getNextSong(songQ));
    //                System.out.println(player.getFile());
                    if (songThread.isAlive()) {
                        songThread.stop();
                        songThread = new Runner();
                    } else {
                        songThread = new Runner();
                    }
                    songThread.setAudioPlayer(player);
                    songThread.start();

                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                }
                break;
            case "prevSong":
                //go back 1 song from directory
                if(songQueue.getNumFiles() > 0) {
                    time.stop();
                    if(shuffleOn) {
                        do {
                        temp = (int)(Math.random() * (songQueue.getNumFiles())); //generate number between 1 and 6
                      }while (temp == songQ);
                        songQ = temp;
                    } else {
                        songQ -= 1;
                    }
                    if(songQ < 0) {
                        songQ = songQueue.getNumFiles() - 1;
                    }
                    player.setFilePath(songQueue.getNextSong(songQ));
                    if (songThread.isAlive()) {
                        songThread.stop();
                        songThread = new Runner();
                    } else {
                        songThread = new Runner();
                    }
                    songThread.setAudioPlayer(player);
                    songThread.start();

                    time.start();
                    playSong.setActionCommand("pause");
                    playSong.setText("Pause");
                }
                break;
            case "shuffleSong":
                //choose new song (shuffle/randomize it)
                if(songQueue.getNumFiles() > 1) {
                    time.stop();
                    if(shuffleOn) {
                        shuffleOn = false;
                        shuffle.setText("Shuffle = Off");
                    } else {
                        shuffleOn = true;
                        shuffle.setText("Shuffle = On");
                    }
                } else {
                    shuffle.setText("Shuffle = Off");
                    System.out.println("Not enough songs to shuffle.");
                }
                break;
            case "selectSong":
                time.stop();
                dir = choose.choose();
                player.setFilePath(dir);
                //set songQ position
                songQ = songQueue.getPos(player.getFile().toString());
                if (songThread.isAlive()) {
                    songThread.stop();
                    songThread = new Runner();
                } else {
                    songThread = new Runner();
                }
                songThread.setAudioPlayer(player);
                songThread.start();
                
                time.start();
                playSong.setActionCommand("pause");
                playSong.setText("Pause");
                break;
            default:
                break;
        }
    }

    /**
     * Slider listener that checks when the speed has changed and updates the
     * timer to the new rate of change (frames per second) in milliseconds pre:
     * none post: none
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        speed = videoSpeed.getValue();
        time.setDelay(speed);
    }

    class gameTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
