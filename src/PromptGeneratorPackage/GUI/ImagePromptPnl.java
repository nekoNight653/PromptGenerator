package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.ImagePrompts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ImagePromptPnl extends PromptPnl {
    private final static ImagePrompts prompts = new ImagePrompts();

    //A test button for displaying images
    private final JButton displayImage, addGenreButton, deleteGenreButton, getGenresButton;


    private final JTextField genreToAdd, genreToDelete;

    public ImagePromptPnl() {

        int y = 0;

        GBC.fill = GridBagConstraints.BOTH;


        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the controlPnl)
        //I use constants in the class PromptGeneratorPackage.GUI for the size, so I can set the size from the GUI class (If I ever decide to resize them)
        this.setPreferredSize(new Dimension(GUI.INPUT_WIDTH, GUI.PRMPT_PNL_HEIGHT));
        this.setLayout(new GridBagLayout());

        addGenreButton = new JButton(GUI.ADD_GENRE_BUTTON_NAME);
        genreToAdd = new JTextField();
        bttnTxtFldCombo(addGenreButton, genreToAdd, this::createGenre, y);

        deleteGenreButton = new JButton(GUI.DELETE_GENRE_BUTTON_NAME);
        genreToDelete = new JTextField();
        bttnTxtFldCombo(deleteGenreButton, genreToDelete, this::deleteGenre, ++y);


        getGenresButton = new JButton(GUI.GET_GENRES_BUTTON_NAME);
        createButton(getGenresButton, this::outputAllGenres, 0, ++y);
        displayImage = new JButton(GUI.DISPLAY_IMAGE_BUTTON_NAME);
        createButton(displayImage, this::displayImage, 1, y);
    }



    //A test method for displaying images
    private void displayImage() {

        try {
            File imagePath = new File("C:\\Users\\catwi\\OneDrive\\Desktop\\Backgrounds\\Ran.png");
            BufferedImage image = ImageIO.read(imagePath);
            gui.outputImg(image);
        } catch (IOException e) {
            System.out.println("Failed to read image");
            e.printStackTrace();
        }

    }

    private void createGenre() {
        String genreName = genreToAdd.getText();
        int result = prompts.createGenre(genreName);
        if(result == 1) {
            gui.outputln("genre \"" + genreName + "\" created.", GUI.STYLE_DARK_ORANGE);
        } else if(result == 0) {
            gui.outputln("Genre \"" + genreName + "\" alread exists.", GUI.STYLE_RED);
        }  else {
            gui.outputln("Some security exception creating genre \"" + genreName + "\"", GUI.STYLE_RED);
        }
    }

    private void deleteGenre() {
        String genreName = genreToDelete.getText();
        if(prompts.deleteGenre(genreName)) gui.outputln("Genre \"" + genreName + "\" and all it's contents deleted", GUI.STYLE_DARK_ORANGE);
        else gui.outputln("Failed to delete genre \"" + genreName + "\"", GUI.STYLE_RED);
    }

    private void outputAllGenres() {
        ArrayList<String> allNames = prompts.getGenreNames();
        allNames.sort(Comparator.reverseOrder());

        //This makes it so that there's a number counting down how many genres there are.
        int genreNum = allNames.size() + 1;

        //The gui.addOutputText("", null); just prints a blank line because a new line is built in to the method
        gui.outputln("", null);
        for(String name : allNames) {
            gui.outputln( (--genreNum) + ": " + name, null);
        }
        gui.outputln("", null);
    }

}
