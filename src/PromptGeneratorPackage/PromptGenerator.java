package PromptGeneratorPackage;

import PromptGeneratorPackage.GUI.GUI;

import java.io.File;

public class PromptGenerator {

    //We make this a constant because otherwise any other class that tries to use GUI makes an entire new object where everything is null
    public static final GUI gui = new GUI();
    //The folder where everything is contained
    public static final File PROMPTS_FOLDER = new File(System.getProperty("user.dir"), "Prompts");

    public static void main(String[] args) {

        gui.gui();

    }

}