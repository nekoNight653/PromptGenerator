package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.Prompt;
import PromptGeneratorPackage.Prompts.PromptManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
        button.setFont(GUI.INPUT_FONT);
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
        textField.setFont(GUI.INPUT_FONT);
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

    /*
    * Finally a helper method for creating combo boxes
    * I only have two per promptPanel, but hey why not?
    *
    *
    */
    protected  void createComboBox(JComboBox comboBox, int x, int y) {
        comboBox.setFont(GUI.INPUT_FONT);
        GBC.gridx = x;
        GBC.gridy = y;
        this.add(comboBox, GBC);
    }




    //Here we have some abstract methods for getting information, so we can implement several methods in here
    //Part of me wonders if I should do this because without these I could just make this into an interface (Since it would do less)

    //This is just for output currently. It does start the line so capitalization is recommended
    abstract protected String promptTypeName();
    //This is going to be whatever prompt manager class is in use like for image prompts the class ImagePrompts
    abstract protected PromptManager prompts();
    //A string which is what ever genre your user currently plans to add
    abstract protected String genreToAdd();
    //A string for whichever prompt your user currently plans to delete
    abstract protected String genreToDelete();
    //An int for how many truly random prompts you want to get
    abstract protected int getPromptNum();
    //All the textFields in the panel
    abstract protected JTextField[] textFields();

    //Down here we have methods

    //This just gets called on genre creation or deletion
    //I thought about making it an event then I realized that would take an entire class and I don't need it anywhere else anyway
    abstract protected void genreExistenceUpdate();

    protected void createGenre() {
        String genreName = genreToAdd();
        int result = prompts().createGenre(genreName);
        if(result == 1) {
            gui.outputln(promptTypeName() + " genre \"" + genreName + "\" created.", GUI.STYLE_DARK_ORANGE);
            genreExistenceUpdate();
        } else if(result == 0) {
            gui.outputln(promptTypeName() + " genre \"" + genreName + "\" already exists.", GUI.STYLE_RED);
        }  else {
            gui.outputln("Some security exception creating " + promptTypeName().toLowerCase() + " genre \"" + genreName + "\"", GUI.STYLE_RED);
        }

    }

    protected void deleteGenre() {
        String genreName = genreToDelete();
        if(prompts().deleteGenre(genreName)){
            gui.outputln(promptTypeName() + " genre \"" + genreName + "\" and all it's contents deleted", GUI.STYLE_DARK_ORANGE);
            genreExistenceUpdate();
        }
        else gui.outputln("Failed to delete " + promptTypeName() + " genre \"" + genreName + "\"", GUI.STYLE_RED);
    }

    protected void outputAllGenres() {
        ArrayList<String> allNames = new ArrayList<>(List.of(prompts().getGenreNames()));
        allNames.sort(Comparator.reverseOrder());

        //This makes it so that there's a number counting down how many genres there are.
        int genreNum = allNames.size() + 1;

        //The gui.addOutputText("", null); just prints a blank line because a new line is built in to the method
        gui.outputln("", null);
        for(String name : allNames) {
            //We have the spaces, so it's an indented list.
            gui.outputln( "   " + --genreNum + ": " + name, null);
        }
        gui.outputln(promptTypeName() + " genres: ", null);
        gui.outputln("", null);
    }
    abstract protected void addPrompt();
    abstract  protected void deletePrompt();

    //Genre by genre it will go through and output each prompt in that genre.
    //It will also tell you each time it goes to a new genre
    abstract protected void outputAllPrompts();

    //It will go through the prompt list provided to it and output each prompt on that list.
    //It also divides by genre
    abstract protected void outputPrompts(ArrayList<Prompt> promptList);
    //It generates a window for the user to choose how many prompts and from which genres they want, and then it gets random prompts from those genres
    protected void getParameterizedRandPrompts() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        ArrayList<File> genres = prompts().getGenres();
        ArrayList<JComponent> inputs = new ArrayList<>();

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel number = new JLabel("Number");
        JLabel genreLabel = new JLabel("Genre");


        int y = 0;

        gbc.gridx = 2;
        gbc.gridy = y;
        pane.add(number, gbc);

        gbc.gridx = 0;
        pane.add(genreLabel, gbc);



        //This creates a pane for the options pane to use
        for(File genre : genres){

            //The end has 6 spaces because I wanted there to be a separation...
            //It's hacky but it works
            inputs.add(new JLabel(genre.getName().replace(".txt", "") + ":      "));
            inputs.get(inputs.size() - 1).setFont(GUI.OUTPUT_FONT);
            gbc.gridx = 0;
            gbc.gridy = ++y;
            pane.add(inputs.get(inputs.size() - 1 ), gbc);

            inputs.add(new JTextField());
            inputs.get(inputs.size() - 1).setFont(GUI.OUTPUT_FONT);
            inputs.get(inputs.size() - 1).setPreferredSize(new Dimension((int) (screenSize.width * 0.04), (int) (screenSize.height * 0.05)));
            gbc.gridx = 2;
            pane.add(inputs.get(inputs.size() - 1), gbc);

        }

        //If it gets too big this makes it scrollable
        JScrollPane scrollPane = new JScrollPane(pane);

        //You have to set the width so the scroll bar actually appears and the height so the confirmation options don't go off-screen
        //I tried with min sizes and max sizes but those didn't work at all
        scrollPane.setPreferredSize(new Dimension((int) (screenSize.width * 0.21), (int) (screenSize.height * 0.3)));

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Random prompt specification", JOptionPane.OK_CANCEL_OPTION);


        HashMap<File, Integer> specifications = new HashMap<File, Integer>();


        if(result == JOptionPane.OK_OPTION) {
            //The reason this is assigned to the main prompt is because it doesn't matter we just need it to not complain that it could be unassigned
            //It will always go Label then textField so this will be reassigned before it even does  anything
            File genre = PromptGenerator.PROMPTS_FOLDER;

            //Because order is guaranteed we just use the genre list to get which genre it is
            int nextGenreIndex = 0;
            //This is what gets the information from the JOptionPane
            //
            //Since it always goes label then textField we can do this
            for(JComponent comp : inputs) {

                if(comp instanceof JTextField) {

                    int num = getNum((JTextField) comp);
                    specifications.put(genre, num);

                } else {
                    genre = genres.get(nextGenreIndex);
                    nextGenreIndex++;
                }
            }



            ArrayList<Prompt> randPrompts = prompts().getXRandomPrompts(specifications);
            outputPrompts(randPrompts);

        }
    }

    private static int getNum(JTextField comp) {
        int num;
        //So that it doesn't break if they accidentally put in 1 wrong input
        try {
            //I do this because I once accidentally put a space, and they're hard to see.
            String input = comp.getText().replace(" ", "");
            num = Integer.parseInt(input);

        } catch (NumberFormatException e) {
            //If the input is invalid we set it to 0, so we get no prompts from said file
            num = 0;
        }

        return num;
    }



    //Outputs x random prompts
    //x is just the input in the textField getPromptsNum
    protected void getRandPrompts() {
        int num = getPromptNum();
        final Random random = new Random();

        //If the number is less than or equal to 0 we bail
        if(num <= 0) {
            gui.outputln("Tried to get " + num + " prompts. Number must be 1 or more to get prompts", GUI.STYLE_RED);
            return;
        }

        //If it's greater than or equal to the number fo prompts we have we just return them all.
        //Rather than going through the process of getting random ones
        ArrayList<Prompt> allPrompts = prompts().getPrompts(prompts().getGenres());
        if(num >= allPrompts.size()) {

            gui.outputln("", null);
            outputAllPrompts();

            gui.outputln(
                    "\nNumber of prompts requested(" + num + ") greater than or equal to number of prompts available("
                            + allPrompts.size() + ").\nOutputting all prompts instead: ", GUI.STYLE_RED
            );

            return;
        }


        final HashMap<File, Integer> specifications = new HashMap<>();

        HashMap<File, Integer> genreToSize = new HashMap<>();
        //We do this a because it can stop tons of looping (before it could loop the same genre to get the size several times before it was done)
        //And also so that if any genres don't have any prompts we can remove them from the process before it starts
        for(File genre : prompts().getGenres()) {

            int size = prompts().getPrompts(genre).size();
            if(size == 0 ) {
                continue;
            }

            genreToSize.put(genre, size);
        }

        //This is because keySet doesn't seem to have an index that I can find
        ArrayList<File> genres = new ArrayList<>(genreToSize.keySet());

        //Here is where we get the hashmaps specifications filled out
        //Note with how we currently do it chooses from all genres evenly we do not weight the genres based on prompt number
        while(num > 0) {

            int nextGenre = random.nextInt(genres.size());
            File genre = genres.get(nextGenre);


            if(specifications.containsKey(genre)) {
                specifications.put(genre, (specifications.get(genre) + 1));
            } else {
                specifications.put(genre, 1);
            }

            //This just makes sure we don't go over the genres amount of available prompts in a certain genre
            //So that we actually get the number of prompts requested. (It would go over the genre limit before and return too few prompts)
            if(specifications.get(genre) >= genreToSize.get(genre)) {
                genres.remove(nextGenre);
                genreToSize.remove(genre);
            }

            num--;
        }

        ArrayList<Prompt> randomPromptList = prompts().getXRandomPrompts(specifications);
        outputPrompts(randomPromptList);
    }

    protected void clearInput() {
        //I would put this outside so that it could be used in two places, but it gave me a null field when I did that
        final JTextField[] textFields = textFields();

        for (JTextField field : textFields) {
            field.setText("");
        }
    }
}
