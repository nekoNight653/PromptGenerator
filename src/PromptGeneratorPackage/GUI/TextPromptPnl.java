package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.Prompt;
import PromptGeneratorPackage.Prompts.TextPrompts;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class TextPromptPnl extends JPanel {


    private static final GUI gui = PromptGenerator.gui;

    private static final TextPrompts prompts = new TextPrompts();


    //Genre related buttons
    /*
     * addGenreButton is the button for adding genres
     * deleteGenreButton is the button for deleting genres
     * getGenresButton is the button for getting all genres
     */
    private final JButton addGenreButton, deleteGenreButton, getGenresButton;

    //Prompt related buttons
    /*
     * addButton is the button for adding prompts
     * deleteButton is the button for deleting prompts
     * getButton is the button for getting prompts X random prompts where X is inputted by the user
     * getAllButton is the button for getting all prompts
     * paramedRandPrmptBttn or parameterizedRandomPromptButton (way too long a name) gets random prompts,
     *   but you can specify how many from each genre
     *
     */
    private final JButton addButton, deleteButton, getButton, getAllButton, paramedRandPrmptBttn;

    //Misc buttons
    /*
     * clearOutputButton clears all text in the output textPane
     * clearInputButton clears all user input in the JTextFields
     * unknownButton who knows?
     */
    private final JButton clearOutputButton, clearInputButton, unknownButton;

    /* The text fields
     * genreToAdd is the input for which genre to add
     * genreToDelete is the input for which genre to delete
     * promptToAdd is the input for adding a prompt
     * getPromptNum is the number of how many random prompts you want to get
     * promptToDelete is the specifier for which prompt you want to delete
     */
    private final JTextField genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete;

    //The JComboBox for choosing which genre
    private final JComboBox genreToAddTo, genreToDeleteFrom;

    //We have this as a constant because it gets weird when we bounce it around between methods
    private static final GridBagConstraints GBC = new GridBagConstraints();

    /*
    * Just creates a button with an action listener and the font inputFont
    * At the specified location
    * Takes the button it's making, the runnable it runs on click, and the position of the button on the grid
    */
    private void createButton(JButton button, Runnable runnable, int x, int y) {
        button.addActionListener(e -> runnable.run());
        button.setFont(GUI.inputFont);
        GBC.gridx = x;
        GBC.gridy = y;
        this.add(button, GBC);
    }


    //I just realized since both this and imagePromptsPnl will have these 3 methods below I could I guess make a parent class with these two in it
    //But I think that's probably just more confusing.. Maybe if I start making tons of these

    /*
     * Just creates a textField with an action listener and the font inputFont
     * At the specified location
     * Takes the textField it's making, the runnable it runs on enter, and the position of the textField on the grid
     */
    private void createTextField(JTextField textField, Runnable runnable, int x, int y) {
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
    private void bttnTxtFldCombo(JButton button, JTextField textField, Runnable runnable, int y) {
        createButton(button, runnable, 0, y);
        createTextField(textField, runnable, 1, y);
    }

    public TextPromptPnl() {

        int y = 0;

        
        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the inputPnl)
        this.setPreferredSize(new Dimension(GUI.INPUT_WIDTH, GUI.PRMPT_PNL_HEIGHT));
        this.setLayout(new GridBagLayout());


        GBC.fill = GridBagConstraints.BOTH;

        addGenreButton = new JButton(GUI.ADD_GENRE_BUTTON_NAME);
        genreToAdd = new JTextField();

        bttnTxtFldCombo(addGenreButton, genreToAdd, this::addGenre, y);

        deleteGenreButton = new JButton(GUI.DELETE_GENRE_BUTTON_NAME);
        genreToDelete = new JTextField();

        bttnTxtFldCombo(deleteGenreButton, genreToDelete, this::deleteTxtGenre, ++y);

        addButton = new JButton(GUI.ADD_BUTTON_NAME);
        promptToAdd = new JTextField();

        bttnTxtFldCombo(addButton, promptToAdd, this::addPrompt, ++y);


        genreToAddTo = new JComboBox<>(prompts.getGenreNames().toArray());
        genreToAddTo.setFont(GUI.inputFont);
        GBC.gridx = 3;
        GBC.gridy = y;
        this.add(genreToAddTo, GBC);

        deleteButton = new JButton(GUI.DELETE_BUTTON_NAME);
        promptToDelete = new JTextField();

        bttnTxtFldCombo(deleteButton, promptToDelete, this::deletePrompt, ++y);

        genreToDeleteFrom = new JComboBox<>(prompts.getGenreNames().toArray());
        genreToDeleteFrom.setFont(GUI.inputFont);
        GBC.gridx = 3;
        GBC.gridy = y;
        this.add(genreToDeleteFrom, GBC);


        getButton = new JButton(GUI.GET_BUTTON_NAME);
        getPromptNum = new JTextField();

        bttnTxtFldCombo(getButton, getPromptNum, this::getRandPrompts, ++y);

        paramedRandPrmptBttn = new JButton(GUI.PARAMED_RAND_PRMPT_BTTN_NAME);
        createButton(paramedRandPrmptBttn, this::getParameterizedRandPrompts, 0, ++y);

        unknownButton = new JButton(GUI.UNKOWN_BUTTON_NAME);
        createButton(unknownButton, this::unknowable, 1, y);

        getAllButton = new JButton(GUI.GET_ALL_BUTTON_NAME);
        createButton(getAllButton, this::outputAllPromptsSpaced, 0, ++y);

        clearInputButton = new JButton(GUI.CLEAR_INPUT_BUTTON_NAME);
        createButton(clearInputButton, this::clearInput, 1, y);

        getGenresButton = new JButton(GUI.GET_GENRES_BUTTON_NAME);
        createButton(getGenresButton, this::outputAllGenres, 0, ++y);

        clearOutputButton = new JButton(GUI.CLEAR_OUTPUT_BUTTON_NAME);
        createButton(clearOutputButton, gui::clearOuput, 1, y);


    }


    /*
     * This is just a method to call whenever we change what genres there are
     * It just refreshes the combo boxes that use Genres, so they display the correct information
     */
    private void updateGenreCmboBxes() {

        genreToAddTo.setModel(new DefaultComboBoxModel(prompts.getGenreNames().toArray()));
        genreToDeleteFrom.setModel(new DefaultComboBoxModel(prompts.getGenreNames().toArray()));

    }

    //Genre related button functions

    //Creates a genre.txt file (Uses the input in the text field genreToAdd)
    //unless the name is blank, or the file already exists, or there was an unforeseen error
    //In which case it tells you what the problem was
    private void addGenre() {

        String genreName = genreToAdd.getText();

        if(genreName.isBlank()) {
            gui.outputln("Genre name blank", GUI.STYLE_RED);
            return;
        }

        //I add this because spaces in file names can be weird, and they freak me out.. maybe I shouldn't but
        genreName = genreName.replace(' ', '_');

        int addGenreReturn = prompts.createGenre(genreName);
        if(addGenreReturn == 1) {
            gui.outputln("Genre \"" + genreName + "\" created", GUI.STYLE_DARK_ORANGE);
        } else if (addGenreReturn == 0) {
            gui.outputln("Genre \"" + genreName + "\" already exists", GUI.STYLE_RED);
        } else {
            gui.outputln("Problem adding genre \"" + genreName + "\"", GUI.STYLE_RED);
        }

        updateGenreCmboBxes();
    }

    //Deletes a genre (Uses the input in the textField genreToAdd)
    //Or if a problem occurred it will say "couldn't delete genre file "name of requested genre to delete" "
    private void deleteTxtGenre() {
        //I add this because spaces in file names can be weird, and they freak me out.. maybe I shouldn't dictate user genre-names but...
        String genreName = genreToDelete.getText().replace(' ', '_');

        if(prompts.deleteGenre(genreName)) gui.outputln("Genre \"" + genreName + "\" deleted", GUI.STYLE_DARK_ORANGE);

        else gui.outputln("Failed to delete genre file \"" + genreName + "\"", GUI.STYLE_RED);

        updateGenreCmboBxes();
    }

    //Outputs all genre file names
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


    //Prompt related buttons

    //Writes a prompt to the prompt file
    //Then outputs dark orange text telling the user what prompt they wrote
    private void addPrompt() {
        String genreName = (String) genreToAddTo.getSelectedItem();
        File genre = prompts.getGenreFile(genreName);

        int writePromptReturn = prompts.addPrompt(promptToAdd.getText(), genre);
        if(writePromptReturn == 0) {
            gui.outputln("Prompt either empty or contained a *", GUI.STYLE_RED);
            return;
        } else if (writePromptReturn == -1) {
            //Since you choose the genre from a combo box I don't know how this result would be possible but...
            gui.outputln("Genre " + genreName + " doesn't exist?", GUI.STYLE_RED);
        } else if (writePromptReturn == -2) {
            gui.outputln("An unexpected IOException occurred, prompt not wrote.", GUI.STYLE_RED);
            return;
        }
        gui.outputln("Added to \"" + genreName + "\" prompt \"" + promptToAdd.getText() + "\"", GUI.STYLE_DARK_ORANGE);
    }

    private void deletePrompt() {
        String genreName = (String) genreToDeleteFrom.getSelectedItem();
        ArrayList<String> deletedPrompts = prompts.deletePrompt(promptToDelete.getText(), prompts.getGenreFile(genreName));
        if (deletedPrompts.isEmpty()) {
            gui.outputln("Prompt \"" + promptToDelete.getText() + "\" not found in genre \"" + genreName + "\"", GUI.STYLE_RED);
            return;
        }
        gui.outputln("Deleted from genre \"" + genreName + "\" prompt(s): " + deletedPrompts, GUI.STYLE_DARK_ORANGE);
    }

    //It goes through each genre and outputs all prompts 1 per line with a number for which one it is in that genre
    //This method is used in two places, so I don't print a new line before and after it, but I heavily suggest you do that in most situations
    private void outputAllPrompts() {

        for(File genre : prompts.getGenres()) {
            //We add 1 so that the final number it outputs is 1 and not 0
            int promptNum = prompts.getPrompts(genre).size() + 1;

            ArrayList<Prompt> promptList = prompts.getPrompts(genre);
            ArrayList<String> stringPrompts = new ArrayList<>();

            //We do this, so we can actually sort the list alphabetically..
            for(Prompt prompt : promptList) {
                stringPrompts.add(prompt.prompt());
            }
            stringPrompts.sort(Comparator.reverseOrder());

            //This is so that it prints 1 per line which makes it far more readable
            for (String prompt : stringPrompts) {
                promptNum--;
                gui.outputln("   " + promptNum + ": " + prompt, null);
            }

            gui.outputln("\nGenre " + genre.getName().replace(".txt", "") + ":", null);
        }
    }
    //The reason I have this is the outputAllPrompts method is used in two places, so I can't do this inside it.
    //And I need to make these lines a runnable. Which maybe I could do a lambda with all three lines, but I don't like it
    private void outputAllPromptsSpaced() {
        gui.outputln("", null);
        outputAllPrompts();
        gui.outputln("", null);
    }


    //Contains all the code for how the rand-prompt selectors output their prompts
    private void outputPrompts(ArrayList<Prompt> promptList) {

        gui.outputln("\n", null);
        //This is so that we can track if we've changed genre file or not that way we can separate prompts by genre
        File previousGenre = promptList.get(0).genre();

        for(Prompt prompt : promptList) {
            //This is where we output the previous genre and then set the new genre so that you know which genre the prompts are from
            //We output on change because it always outputs at the top, so we have to do it in "reverse"
            if(prompt.genre() != previousGenre){

                gui.outputln("\n" + previousGenre.getName().replace(".txt", "") + ":", null);
                previousGenre = prompt.genre();

            }
            //The padding is, so it looks like a tab but an actual tab was too big
            gui.outputln("   " + prompt.prompt(), null);
        }

        //Since it's on change we usually ouput the genre we also have to do it at the end, so it's not missing one
        gui.outputln("\n" + previousGenre.getName().replace(".txt", "") + ":", null);

        gui.outputln("\nChosen prompts:", null);
    }

    //Opens a JOptionsPane so the user can specify how many prompts they want from each genre
    //Then just calls GetXPromptsJP with the hashMap<File, Integer> specifications
    //Then outputs the gotten prompts
    private void getParameterizedRandPrompts() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        ArrayList<File> genres = prompts.getGenres();
        ArrayList<JComponent> inputs = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel number = new JLabel("Number");
        JLabel genreLabel = new JLabel("Genre");


        int y = 0;

        gbc.gridx = 2;
        gbc.gridy = y;
        panel.add(number, gbc);

        gbc.gridx = 0;
        panel.add(genreLabel, gbc);

        String colonWithSep = ":      ";

        for(File genre : genres){

            //The end has 6 spaces because I wanted there to be a separation...
            //It's hacky but it works
            inputs.add(new JLabel(genre.getName().replace(".txt", "") + colonWithSep));
            inputs.get(inputs.size() - 1).setFont(GUI.outputFont);
            gbc.gridx = 0;
            gbc.gridy = ++y;
            panel.add(inputs.get(inputs.size() - 1 ), gbc);

            inputs.add(new JTextField());
            inputs.get(inputs.size() - 1).setFont(GUI.outputFont);
            inputs.get(inputs.size() - 1).setPreferredSize(new Dimension((int) (screenSize.width * 0.04), (int) (screenSize.height * 0.05)));
            gbc.gridx = 2;
            panel.add(inputs.get(inputs.size() - 1), gbc);

        }

        //It's probably rare but if it gets too big this makes it scrollable
        JScrollPane scrollPane = new JScrollPane(panel);

        //You have to set the width so the scroll bar actually appears and the height so the confirmation options don't go off-screen
        //I tried with min sizes and max sizes but those didn't work at all
        scrollPane.setPreferredSize(new Dimension((int) (screenSize.width * 0.21), (int) (screenSize.height * 0.3)));

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Random prompt specification", JOptionPane.OK_CANCEL_OPTION);

        HashMap<File, Integer> specifications = new HashMap<File, Integer>();


        if(result == JOptionPane.OK_OPTION) {
            File genre = TextPrompts.TEXT_PROMPT_FOLDER;

            //This is what gets the information from the JOptionPane
            for(JComponent comp : inputs) {

                if(comp instanceof JTextField) {
                    int num;
                    //So that it doesn't break if they accidentally put in 1 wrong input
                    try {
                        //I do this because I once accidentally put a space, and they're hard to see.
                        String input = ((JTextField) comp).getText().replace(" ", "");
                        num = Integer.parseInt(input);

                    } catch (NumberFormatException e) {
                        //If the input is invalid we set it to 0, so we get no prompts from said file
                        num = 0;
                    }

                    specifications.put(genre, num);
                } else {
                    genre = prompts.getGenreFile(((JLabel) comp).getText().replace(colonWithSep, ""));
                }
            }



            ArrayList<Prompt> randPrompts = prompts.getXPromptsJp(specifications);
            outputPrompts(randPrompts);

        }
    }

    //Outputs x random prompts
    //x is just the input in the textField getPromptsNum
    private void getRandPrompts() {
        int num;
        final Random random = new Random();
        //We make sure it's actually a number
        try {

            num = Integer.parseInt(getPromptNum.getText());

        } catch (NumberFormatException formatException) {

            //In case they put a non integer
            gui.outputln("Tried to get prompts without giving a proper integer. Or you went over the int size limit of 2,147,483,647", GUI.STYLE_RED);
            formatException.printStackTrace();
            return;
        }


        //If it's greater than 0 we get prompts
        if(num > 0) {

            ArrayList<Prompt> allPrompts = prompts.getPrompts(prompts.getGenres());
            if(num >= allPrompts.size()) {
                gui.outputln("", null);
                outputAllPrompts();
                gui.outputln(
                        "\nNumber of prompts requested(" + num + ") greater than or equal to number of prompts available(" + allPrompts.size() + ").\n" +
                                "Outputting all prompts instead.", GUI.STYLE_RED
                );

                return;
            }


            final HashMap<File, Integer> specifications = new HashMap<>();
            final ArrayList<File> genres = prompts.getGenres();


            //We do this because in the while loop below we test for how many prompts are in a genre,
            // but we had to do that by looping through a genre file each time the while loop ran,
            // so I decided to loop through them all first.

            //Though I guess if they have 50 genre files and request two prompts this is inefficient... maybe I shouldn't do this?
            //Maybe I test for the difference.. IDK it's probably not that big a deal
            ArrayList<Integer> genreSizes = new ArrayList<Integer>();
            for(File genre : genres) {
                genreSizes.add(prompts.getPrompts(genre).size());
            }

            //Here is where we actually get the random prompts
            while(num > 0) {

                int nextGenre = random.nextInt(genres.size());
                File genre = genres.get(nextGenre);


                if(specifications.containsKey(genre)) {
                    specifications.put(genre, (specifications.get(genre) + 1));
                } else {
                    specifications.put(genre, 1);
                }

                //This just makes sure we don't go over the genres amount of available prompts
                //So that we actually get the number of prompts requested.
                //This doesn't break because we test if the prompts requested are >= to the number of prompts available above
                if(specifications.get(genre) >= genreSizes.get(nextGenre)) {
                    genres.remove(nextGenre);
                    genreSizes.remove(nextGenre);
                }

                num--;
            }

            ArrayList<Prompt> randomPromptList = prompts.getXPromptsJp(specifications);
            outputPrompts(randomPromptList);

            //Otherwise we tell the user they input a number too low
        } else {
            gui.outputln("Tried to get " + num + " prompts. Number must be 1 or more to get prompts", GUI.STYLE_RED);
        }
    }

    //Clears the input in all JTextFields
    private void clearInput() {
        //I would put this outside so that it could be used in two places, but it gave me a null field when I did that
        final JTextField[] textFields = new JTextField[]{genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete};

        for (JTextField field : textFields) {
            field.setText("");
        }
    }


    //IDK I had a gap in my GUI, so I just decided to make this
    private void unknowable() {
        Random random = new Random();
        //I would put this outside so that it could be used in two places, but it gave me a null field when I did that
        final JTextField[] textFields = new JTextField[]{genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete};
        for(JTextField field : textFields) {
            field.setText("????");
        }
        int i = random.nextInt(100, 200);
        while (i > 0) {
            gui.outputln("?????", GUI.STYLE_RED);
            i--;
        }
    }
}
