package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.Prompts.ImagePrompts;
import PromptGeneratorPackage.Prompts.Prompt;
import PromptGeneratorPackage.Prompts.PromptManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImagePromptPnl extends PromptPnl {
    private final static ImagePrompts prompts = new ImagePrompts();
    /*
    * Genre related buttons
    * **
    * displayImage a test button for displaying images
    * addGenreButton a button for creating genre directories
    * deleteGenreButton a button for deleting genre directories
    * getGenresButton a button for outputting all genres
    */
    private final JButton addGenreButton, deleteGenreButton, getGenresButton;
    /*
    * Genre related textFields
    * **
    * genreToAdd the input which genre to create
    * genreToDelete the input for which genre to delete
    */
    private final JTextField genreToAdd, genreToDelete;
    /*
    * Prompt related buttons
    * **
    * addPromptButton adds a prompt (copies a prompt into a genre file)
    * deletePromptButton a prompt from a genre file
    * getRandPrompts gets x random prompts from random genres
    * getAllPromptsButton gets all prompts from all genres
    *
    * getPararmmedRandPrmpts Opens a temporary tab for the user to choose how many prompts they want from each genre.
    * Then randomly gets prompts x prompts from each genre where x is the user declared amount for that genre
    *
    */
    private final JButton addPromptButton, deletePromptButton, getRandPromptsButton, getAllPromptsButton, getPararmedRandPrmptsBttn;
    /*
    * Prompt related textFields
    * **
    * promptToAdd the prompt to add. pairs with addPromptButton
    * promptToDelete the prompt to delete. pairs with deletePromptButton
    * getRandPromptNum the number of truly random prompts to get. pairs with getRandPrompts
    */
    private final JTextField promptToAdd, promptToDelete, getRandPromptNum;
    //The combo boxes for choosing which genre a prompt is going in going out of
    private final JComboBox<String> genreToAddTo, genreToDeleteFrom;

    //The misc buttons
    private final JButton clearInputBttn;

    public ImagePromptPnl() {

        int y = 0;

        GBC.fill = GridBagConstraints.BOTH;


        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the controlPnl)
        //I use constants in the class PromptGeneratorPackage.GUI for the size, so I can set the size from the GUI class (If I ever decide to resize them)
        this.setPreferredSize(new Dimension(GUI.PRMPT_PNLS_WIDTH, GUI.PRMPT_PNLS_HEIGHT));
        this.setLayout(new GridBagLayout());

        //Genre creation inputs
        addGenreButton = new JButton(GUI.ADD_GENRE_BUTTON_NAME);
        genreToAdd = new JTextField();
        bttnTxtFldCombo(addGenreButton, genreToAdd, this::createGenre, y);

        //Genre deletion inputs
        deleteGenreButton = new JButton(GUI.DELETE_GENRE_BUTTON_NAME);
        genreToDelete = new JTextField();
        bttnTxtFldCombo(deleteGenreButton, genreToDelete, this::deleteGenre, ++y);

        //Prompt adding inputs
        addPromptButton = new JButton(GUI.ADD_BUTTON_NAME);
        promptToAdd = new JTextField();
        bttnTxtFldCombo(addPromptButton, promptToAdd, this::addPrompt, ++y);

        genreToAddTo = new JComboBox<>(prompts.getGenreNames());
        createComboBox(genreToAddTo, 3, y);

        //Prompt deleting inputs
        deletePromptButton = new JButton(GUI.DELETE_BUTTON_NAME);
        promptToDelete = new JTextField();
        bttnTxtFldCombo(deletePromptButton, promptToDelete, this::deletePrompt, ++y);

        genreToDeleteFrom = new JComboBox<>(prompts.getGenreNames());
        createComboBox(genreToDeleteFrom, 3, y);

        //Truly random prompt acquirers inputs
        getRandPromptsButton = new JButton(GUI.GET_BUTTON_NAME);
        getRandPromptNum = new JTextField();
        bttnTxtFldCombo(getRandPromptsButton, getRandPromptNum, this::getRandPrompts, ++y);

        //Parameterized random prompt acquirer
        getPararmedRandPrmptsBttn = new JButton(GUI.PARAMED_RAND_PRMPT_BTTN_NAME);
        createButton(getPararmedRandPrmptsBttn, this::getParameterizedRandPrompts, 0, ++y);

        //clear anything in the textFields
        clearInputBttn = new JButton(GUI.CLEAR_INPUT_BUTTON_NAME);
        createButton(clearInputBttn, this::clearInput, 1, y);

        //Get all prompts button
        getAllPromptsButton = new JButton(GUI.GET_ALL_BUTTON_NAME);
        createButton(getAllPromptsButton, this::outputAllPrompts, 0, ++y);

        //Get all genres button
        getGenresButton = new JButton(GUI.GET_GENRES_BUTTON_NAME);
        createButton(getGenresButton, this::outputAllGenres, 1, y);


    }



    //Some info for our resident abstract class
    @Override
    protected String promptTypeName() {
        return "Image";
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
        try {
            return Integer.parseInt(getRandPromptNum.getText());

        } catch (NumberFormatException formatException) {

            //In case they put a non integer
            gui.outputln("Tried to get prompts without giving a proper integer. Or you went over the int size limit of 2,147,483,647", GUI.STYLE_RED);
            formatException.printStackTrace();
            return 0;
        }
    }

    @Override
    protected JTextField[] textFields() {
        return new JTextField[]{genreToAdd, genreToDelete, promptToAdd, promptToDelete, getRandPromptNum};
    }

    //Now some methods for us to define

    //Triggers whenever a genre is created or deleted
    @Override
    protected void genreExistenceUpdate() {

        genreToAddTo.setModel(new DefaultComboBoxModel<>(prompts.getGenreNames()));
        genreToDeleteFrom.setModel(new DefaultComboBoxModel<>(prompts.getGenreNames()));

    }

    //Just adds a prompt to a genre and has 5 different possible outputs depending on the result (4 are error messages and the other one is success)
    //It just takes the file path you enter and gives it with genre to the ImagePrompts.addPrompt method
    @Override
    protected void addPrompt() {
        File genre = prompts.getGenreFile(genreToAddTo.getSelectedItem().toString());

        int result = prompts.addPrompt(promptToAdd.getText(), genre);
        switch(result) {
            case 1:
                gui.outputln("New image prompt added to \"" + genreToAddTo.getSelectedItem() + "\".\n " +
                        "Prompt: " + promptToAdd.getText(), null);
                break;
            case 0:
                gui.outputln("\nCouldn't find file \"" + promptToAdd.getText() + "\"" +
                        "\nNote add image prompt takes a full file path not just a name\n", GUI.STYLE_RED);
                break;
             case -1:
                gui.outputln("Genre \"" + genreToAddTo.getSelectedItem() + "\" somehow doesn't exist?", GUI.STYLE_RED);
                 break;
             case -2:
                gui.outputln( "An IOException occurred and your prompt hasn't been added", GUI.STYLE_RED);
                 break;
             case -3:
                gui.outputln( "A prompt with that name already exists", GUI.STYLE_RED);
                 break;

        }

    }

    @Override
    protected void deletePrompt() {

        //We get the first entry of this, since it will only ever return an array of 1
        //It's set up as an array list because that's what text prompts wanted. I wonder if there's a better way to do them both though
        String result = prompts.deletePrompt(promptToDelete.getText(), prompts.getGenreFile(genreToDeleteFrom.getSelectedItem().toString())).get(0);
        Style style = GUI.STYLE_RED;

        //If it contains the string const for when a prompt is successfully deleted we set the style to orange
        if (result.contains(ImagePrompts.successFullyDeleted)) style = GUI.STYLE_DARK_ORANGE;
        gui.outputln(result, style);
    }

    private Thread outputImages = null;
    //This is so that various classes using this can wait until after the thread finishes
    public Thread getThread() {return outputImages;}
    //Just calls a thread because images can take a while
    @Override
    protected void outputPrompts(ArrayList<Prompt> promptList) {

        outputImages = new Thread(() -> outputPromptLoop(promptList));
        outputImages.start();

    }

    //For each prompt on the list it creates a buffered image out of that and passes it to the gui.outputImg method
    //Then it outputs what the prompt name is and which genre it's from

    //If it can't convert a prompt to a buffered image it outputs "Prompt: "prompt" not able to be converted to image?"
    private void outputPromptLoop(ArrayList<Prompt> promptList) {
        String currentPrompt = "";

        try {

            for(Prompt prompt : promptList) {

                currentPrompt = prompt.prompt();
                File imagePath = new File(prompt.prompt());

                BufferedImage image = ImageIO.read(imagePath);

                //It seems to only cause an IOException if it's not an image file at all
                //Like with an avif file it says it can do it, but it just returns null
                //So we check for that here
                if (image == null) {
                    String name = imagePath.getName();
                    int index = name.lastIndexOf('.');
                    String extension = name.substring(index);

                    gui.outputln("Improper image format for image: \n\"" + imagePath
                            + "\"\n can't support \"" + extension + "\" files", GUI.STYLE_RED);
                    continue;
                }

                String promptName = currentPrompt.substring((currentPrompt.lastIndexOf(File.separatorChar) + 1));

                try {

                    gui.outputImg(image);
                    gui.outputln("Image prompt \"" + promptName + "\" from genre \"" + prompt.genre().getName() + "\" displaying:", null);

                } catch (BadLocationException e) {
                    gui.outputln("Image prompt \"" + promptName + "\" not found", GUI.STYLE_RED);
                }
            }
            gui.outputln("\nImage prompts got:\n ", null);

        } catch (IOException e) {

            gui.outputln("Prompt: \"" + currentPrompt + "\" not able to be converted to image?", GUI.STYLE_RED);

        }

    }
    @Override
    protected void outputAllPrompts() {
        outputPrompts(prompts.getPrompts(prompts.getGenres()));
    }
}
