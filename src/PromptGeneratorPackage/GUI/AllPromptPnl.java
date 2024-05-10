package PromptGeneratorPackage.GUI;

import PromptGeneratorPackage.PromptGenerator;
import PromptGeneratorPackage.Prompts.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AllPromptPnl extends PromptPnl{
    private static final AllPrompts prompts = new AllPrompts();

    //Buttons
    private static JButton getRandPromptsButton, getPararmedRandPrmptsBttn, clearInputBttn, getAllPromptsButton, getGenresButton;
    //Text field
    private static JTextField getRandPromptNum;
    public AllPromptPnl() {


        int y = 0;

        GBC.fill = GridBagConstraints.BOTH;


        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the controlPnl)
        //I use constants in the class PromptGeneratorPackage.GUI for the size, so I can set the size from the GUI class (If I ever decide to resize them)
        this.setPreferredSize(new Dimension(GUI.PRMPT_PNLS_WIDTH, GUI.PRMPT_PNLS_HEIGHT));
        this.setLayout(new GridBagLayout());

        //Truly random prompt acquirers inputs
        getRandPromptsButton = new JButton(GUI.GET_BUTTON_NAME);
        getRandPromptNum = new JTextField();
        bttnTxtFldCombo(getRandPromptsButton, getRandPromptNum, this::getRandPrompts, y);

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
    @Override
    protected String promptTypeName() {
        return "All";
    }

    @Override
    protected PromptManager prompts() {
        return prompts;
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
        return new JTextField[]{getRandPromptNum};
    }
    @Override
    protected void outputAllPrompts() {
        outputPrompts(prompts.getPrompts(prompts.getGenres()));
    }

    //This outputs prompts based on which prompt type they are
    @Override
    protected void outputPrompts(ArrayList<Prompt> promptList) {

        ArrayList<ArrayList<Prompt>> typeSeparatedPrompts = new ArrayList<>();
        PromptType previoustype = null;

        for(Prompt prompt : promptList) {

            if(previoustype != prompt.type()) {
                typeSeparatedPrompts.add(new ArrayList<>());
                previoustype = prompt.type();
            }
            int index = typeSeparatedPrompts.size() - 1;

            typeSeparatedPrompts.get(index).add(prompt);
        }

        for(ArrayList<Prompt> prompts : typeSeparatedPrompts) {

            PromptPnl pnl = PromptType.getPanel(prompts.get(0).type());
            pnl.outputPrompts(prompts);

        }

    }





    ///////////////////////////////////////////////////////////////////
    //Unused methods
    @Override
    protected void addPrompt() {

    }

    @Override
    protected void deletePrompt() {

    }
    @Override
    protected void genreExistenceUpdate() {

    }
    @Override
    protected String genreToAdd() {
        return null;
    }

    @Override
    protected String genreToDelete() {
        return null;
    }
}
