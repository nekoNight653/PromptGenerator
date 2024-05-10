package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.Prompt;
import PromptGeneratorPackage.Prompts.PromptManager;
import PromptGeneratorPackage.Prompts.TextPrompts;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class TextPromptPnl extends PromptPnl {

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
    private final JButton clearInputButton;

    /* The text fields
     * genreToAdd is the input for which genre to add
     * genreToDelete is the input for which genre to delete
     * promptToAdd is the input for adding a prompt
     * getPromptNum is the number of how many random prompts you want to get
     * promptToDelete is the specifier for which prompt you want to delete
     */
    private final JTextField genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete;

    //The JComboBox for choosing which genre
    private final JComboBox<String> genreToAddTo, genreToDeleteFrom;


    public TextPromptPnl() {

        int y = 0;

        
        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the inputPnl)
        this.setPreferredSize(new Dimension(GUI.PRMPT_PNLS_WIDTH, GUI.PRMPT_PNLS_HEIGHT));
        this.setLayout(new GridBagLayout());


        GBC.fill = GridBagConstraints.BOTH;

        addGenreButton = new JButton(GUI.ADD_GENRE_BUTTON_NAME);
        genreToAdd = new JTextField();

        bttnTxtFldCombo(addGenreButton, genreToAdd, this::createGenre, y);

        deleteGenreButton = new JButton(GUI.DELETE_GENRE_BUTTON_NAME);
        genreToDelete = new JTextField();

        bttnTxtFldCombo(deleteGenreButton, genreToDelete, this::deleteGenre, ++y);

        addButton = new JButton(GUI.ADD_BUTTON_NAME);
        promptToAdd = new JTextField();

        bttnTxtFldCombo(addButton, promptToAdd, this::addPrompt, ++y);


        genreToAddTo = new JComboBox<String>(prompts.getGenreNames());
        createComboBox(genreToAddTo, 3, y);

        deleteButton = new JButton(GUI.DELETE_BUTTON_NAME);
        promptToDelete = new JTextField();

        bttnTxtFldCombo(deleteButton, promptToDelete, this::deletePrompt, ++y);

        genreToDeleteFrom = new JComboBox<String>(prompts.getGenreNames());
        createComboBox(genreToDeleteFrom, 3, y);


        getButton = new JButton(GUI.GET_BUTTON_NAME);
        getPromptNum = new JTextField();

        bttnTxtFldCombo(getButton, getPromptNum, this::getRandPrompts, ++y);

        paramedRandPrmptBttn = new JButton(GUI.PARAMED_RAND_PRMPT_BTTN_NAME);
        createButton(paramedRandPrmptBttn, this::getParameterizedRandPrompts, 0, ++y);

        clearInputButton = new JButton(GUI.CLEAR_INPUT_BUTTON_NAME);
        createButton(clearInputButton, this::clearInput, 1, y);

        getAllButton = new JButton(GUI.GET_ALL_BUTTON_NAME);
        createButton(getAllButton, this::outputAllPromptsSpaced, 0, ++y);

        getGenresButton = new JButton(GUI.GET_GENRES_BUTTON_NAME);
        createButton(getGenresButton, this::outputAllGenres, 1, y);

    }
    //Handing over some information to our resident abstract class
    @Override
    protected String promptTypeName() {
        return "Text";
    }

    @Override
    protected PromptManager prompts() {
        return prompts;
    }

    @Override
    protected String genreToAdd() {
        return genreToAdd.getText();
    }

    @Override
    protected String genreToDelete() {
        return genreToDelete.getText();
    }

    @Override
    protected int getPromptNum() {
        int num;
        try {
            num = Integer.parseInt(getPromptNum.getText());
        } catch (NumberFormatException e) {
            num = 0;
            e.printStackTrace();
        }
        return num;
    }

    @Override
    protected JTextField[] textFields() {
        return new JTextField[]{genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete};
    }

    @Override
    protected void genreExistenceUpdate() {

        genreToAddTo.setModel(new DefaultComboBoxModel<>(prompts.getGenreNames()));
        genreToDeleteFrom.setModel(new DefaultComboBoxModel<>(prompts.getGenreNames()));

    }
    //Button functions

    //Writes a prompt to the prompt file
    //Then outputs dark orange text telling the user what prompt they wrote
    @Override
    protected void addPrompt() {
        String genreName = (String) genreToAddTo.getSelectedItem();
        File genre = prompts.getGenreFile(genreName);

        int result = prompts.addPrompt(promptToAdd.getText(), genre);
        if(result == 0) {
            gui.outputln("Prompt either empty or contained a *", GUI.STYLE_RED);
            return;
        } else if (result == -1) {
            //Since you choose the genre from a combo box I don't know how this result would be possible but...
            gui.outputln("Genre " + genreName + " doesn't exist?", GUI.STYLE_RED);
        } else if (result == -2) {
            gui.outputln("An unexpected IOException occurred, prompt not wrote.", GUI.STYLE_RED);
            return;
        }
        gui.outputln("Added to \"" + genreName + "\" prompt \"" + promptToAdd.getText() + "\"", GUI.STYLE_DARK_ORANGE);
    }
    //Calls the delete prompt method in TextPrompts passing in the prompt to delete and the genre
    @Override
    protected void deletePrompt() {
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
    @Override
    protected void outputAllPrompts() {

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
        gui.outputln("Text prompts gotten:", null);
    }
    //The reason I have this is the outputAllPrompts method is used in two places, so I can't do this inside it.
    //And I need to make these lines a runnable. Which maybe I could do a lambda with all three lines, but I don't like it
    private void outputAllPromptsSpaced() {
        gui.outputln("", null);
        outputAllPrompts();
        gui.outputln("", null);
    }


    //Contains all the code for how the rand-prompt selectors output their prompts
    @Override
    protected void outputPrompts(ArrayList<Prompt> promptList) {

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

        gui.outputln("\nText prompts:", null);
    }

}
