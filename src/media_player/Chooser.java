/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media_player;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Connor
 */
public class Chooser {

    String chosenFile;

    public Chooser() {

    }

    public String choose() {
        File root = new File("music");
        FileSystemView view = new SingleRootFileSystemView(root);

        //Initialize the file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSystemView(view);
        fileChooser.updateUI();
        fileChooser.setCurrentDirectory(root);
        fileChooser.setAcceptAllFileFilterUsed(false);
        //Initialize filter and apply filter to file chooser
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "mp3", "wav");
        fileChooser.setFileFilter(filter);

        //Open file dialog
        int foo = fileChooser.showOpenDialog(null);

        chosenFile = fileChooser.getSelectedFile().toString();
        
        return chosenFile;
    }
}
