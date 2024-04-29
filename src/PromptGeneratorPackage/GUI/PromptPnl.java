package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.PromptManager;

import javax.swing.*;
import java.awt.*;

public abstract class PromptPnl extends JPanel {

    protected final static GUI gui = PromptGenerator.gui;


    protected static final GridBagConstraints GBC = new GridBagConstraints();

    //Here we have small methods to streamline button and text panel creation
    /*
     * Just creates a button with an action listener and the font inputFont
     * At the specified location
     * Takes the button it's making, the runnable it runs on click, and the position of the button on the grid
     */
    protected void createButton(JButton button, Runnable runnable, int x, int y) {
        button.addActionListener(e -> runnable.run());
        button.setFont(GUI.inputFont);
        GBC.gridx = x;
        GBC.gridy = y;
        this.add(button, GBC);
    }

    /*
     * Just creates a textField with an action listener and the font inputFont
     * At the specified location
     * Takes the textField it's making, the runnable it runs on enter, and the position of the textField on the grid
     */
    protected void createTextField(JTextField textField, Runnable runnable, int x, int y) {
        textField.addActionListener(e -> runnable.run());
        textField.setFont(GUI.inputFont);
        GBC.gridx = x;
        GBC.gridy = y;
        this.add(textField, GBC);
    }

    //This is because I have a combination of a button and textField
    //I have this because I have a fair amount of these.
    //It will always create the button at 0x on the grid and the textField at 1x on the grid
    //And both will have the same actionListener result
    protected void bttnTxtFldCombo(JButton button, JTextField textField, Runnable runnable, int y) {
        createButton(button, runnable, 0, y);
        createTextField(textField, runnable, 1, y);
    }



    //Down here we have methods that are for interacting with prompts



}
