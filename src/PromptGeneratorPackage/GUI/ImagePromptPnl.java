package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePromptPnl extends JPanel {

    private final static GUI gui = PromptGenerator.gui;



    //A test button for displaying images
    private final JButton displayImage;

    public ImagePromptPnl() {

        int y = 0;
        GridBagConstraints gbc = new GridBagConstraints();

        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the controlPnl)
        //I use constants in the class PromptGeneratorPackage.GUI for the size, so I can set the size from the GUI class (If I ever decide to resize them)
        this.setPreferredSize(new Dimension(GUI.INPUT_WIDTH, GUI.PRMPT_PNL_HEIGHT));
        this.setLayout(new GridBagLayout());

        displayImage = new JButton(GUI.DISPLAY_IMAGE_BUTTON_NAME);
        displayImage.addActionListener(e -> displayImage());
        displayImage.setFont(GUI.inputFont);
        gbc.gridx = 0;
        gbc.gridy = y;
        this.add(displayImage, gbc);
    }



    //A test method for displaying images
    public void displayImage() {

        try {
            File imagePath = new File("C:\\Users\\catwi\\OneDrive\\Desktop\\Backgrounds\\Toyosatomimi.png");
            BufferedImage image = ImageIO.read(imagePath);
            gui.outputImg(image);
        } catch (IOException e) {
            System.out.println("Failed to read image");
            e.printStackTrace();
        }




    }
}
