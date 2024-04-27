package PromptGeneratorPackage;

import PromptGeneratorPackage.GUI.GUI;

public class PromptGenerator {

    //We make this a constant because otherwise any other class that tries to use PromptGeneratorPackage.GUI makes an entire new object where everything is null
    public static GUI gui = new GUI();

    //I really don't use my main class part of me thinks I should move the main method to the PromptGeneratorPackage.GUI.PromptGeneratorPackage.GUI class
    public static void main(String[] args) {

        gui.gui();

    }

}