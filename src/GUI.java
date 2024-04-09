import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//Maybe make a way to delete specific prompts
//*This will first require a way to delete prompts then a way for the user to specify which prompt to delete
public class GUI implements ActionListener {

    private final Prompts prompts = new Prompts();

    private static final String ADD_BUTTON_NAME = "Add prompt";
    private static final String GET_BUTTON_NAME = "Get X random prompts";
    private static final String DELETE_BUTTON_NAME = "Delete prompt";
    private static final String GET_ALL_BUTTON_NAME = "Get all prompts";
    private static final String CLEAR_WINDOW_BUTTON_NAME = "Clear output";

    //The dimensions for all the controls such as buttons and text input areas
    //private Dimension controlsSize = new Dimension(20, 30);

    private JFrame frame = new JFrame();
    //One panel specifically for the out since it has to be bigger than I want the controls
    private JPanel controlPanel, outputPanel;
    //For what ever I want to display, such as prompt added or which prompts were got
    private StyledDocument outputStyled;
    private JScrollPane outputScrollable;
    private JTextPane output;
    //The button for adding prompts
    private JButton addButton;
    //The button for deleting prompts
    private JButton deleteButton;
    //The button for getting prompts X random prompts where X is inputted by the user
    private JButton getButton;
    //The button for getting all prompts
    private JButton getAllButton;
    //The button for clearing the output window
    private JButton clearWindowButton;
    //The input for adding a prompt
    private JTextField promptToAdd;
    //The number of how many random prompts you want to get
    private JTextField getPromptNum;
    //The specifier for which prompt you want to delete
    private JTextField promptToDelete;

    /*
    * Dark orange is used for text that is important such as writing and deleting prompts
    * Since it's important to know if you accidentally wrote or deleted a prompt
    * Red is used for errors such as bad input like putting a letter into the getPromptNum text field which wants a number
    * And for all other cases you use null for the normal black text
     */
    private Style styleRed, styleDarkOrange;
    private Font controlFont, outputFont;
    //Maps the name of the buttons to the function of the buttons
    private final HashMap<String, Runnable> buttonFuncts = new HashMap<>();
    public void gui() {
        GridBagConstraints gbc = new GridBagConstraints();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        controlFont = new Font("serif", Font.PLAIN, 30);
        outputFont = new Font("serif", Font.PLAIN, 25);

        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder());
        controlPanel.setPreferredSize(new Dimension((int) (screenSize.width*0.7), screenSize.height));
        controlPanel.setLayout(new GridBagLayout());


        outputPanel = new JPanel();
        outputPanel.setBorder(BorderFactory.createEmptyBorder());
        outputPanel.setPreferredSize(new Dimension((int) (screenSize.width*0.3), screenSize.height));
        outputPanel.setLayout(new GridLayout());

        gbc.fill = GridBagConstraints.BOTH;

        addButton = new JButton(ADD_BUTTON_NAME);
        addButton.addActionListener(this);
        addButton.setFont(controlFont);
        buttonFuncts.put(ADD_BUTTON_NAME, this::addPrompt);
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(addButton, gbc);

        promptToAdd = new JTextField();
        promptToAdd.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        controlPanel.add(promptToAdd, gbc);



        deleteButton = new JButton(DELETE_BUTTON_NAME);
        deleteButton.addActionListener(this);
        deleteButton.setFont(controlFont);
        buttonFuncts.put(DELETE_BUTTON_NAME, this::deletePrompt);
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(deleteButton, gbc);


        promptToDelete = new JTextField();
        promptToDelete.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        controlPanel.add(promptToDelete, gbc);


        getButton = new JButton(GET_BUTTON_NAME);
        getButton.addActionListener(this);
        getButton.setFont(controlFont);
        buttonFuncts.put(GET_BUTTON_NAME, this::outputXPrompts);
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(getButton, gbc);

        getPromptNum = new JTextField();
        getPromptNum.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        controlPanel.add(getPromptNum, gbc);


        getAllButton = new JButton(GET_ALL_BUTTON_NAME);
        getAllButton.addActionListener(this);
        getAllButton.setFont(controlFont);
        buttonFuncts.put(GET_ALL_BUTTON_NAME, this::outputAllPromptsSpaced);
        gbc.gridx = 0;
        gbc.gridy = 3;
        controlPanel.add(getAllButton, gbc);

        clearWindowButton = new JButton(CLEAR_WINDOW_BUTTON_NAME);
        clearWindowButton.addActionListener(this);
        clearWindowButton.setFont(controlFont);
        buttonFuncts.put(CLEAR_WINDOW_BUTTON_NAME, () -> output.setText(""));
        gbc.gridx = 1;
        gbc.gridy = 3;
        controlPanel.add(clearWindowButton, gbc);



        output = new JTextPane();
        output.setFont(outputFont);

        outputScrollable = new JScrollPane(output);
        outputStyled = output.getStyledDocument();

        styleRed = outputStyled.addStyle("RedStyle", null);
        StyleConstants.setForeground(styleRed, Color.RED);




        //I made my own color here because orange was a bit too bright, so I lowered the numbers by a hundred-ish
        styleDarkOrange = outputStyled.addStyle("DarkOrangeStyle", null);
        StyleConstants.setForeground(styleDarkOrange, new Color(150, 100, 0));

        output.setEditable(false);
        output.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.gray));
        outputPanel.add(outputScrollable);


        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Prompt generator");
        frame.pack();
        frame.setVisible(true);

        frame.add(outputPanel, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.CENTER);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        buttonFuncts.get(e.getActionCommand()).run();
    }

     /*
     * Adds the specified text to the output, with the specified style passed in
     * The styles I use are just different colors
     *
     * styleDarkOrange is used for text that is important such as writing and deleting prompts
     * Since it's important to know if you accidentally wrote or deleted a prompt
     * styleRed is used for errors such as bad input like putting a letter into the getPromptNum text field which wants a number
     * And for all other cases you use null for the normal black text
     *
     * Note: it makes spaces for you so just call it to make a just a newline character
     */
    private void addOutputText(String text, Style style) {
        try {
            outputStyled.insertString(0, text + "\n", style);
            output.setCaretPosition(0);

        } catch (BadLocationException e) {
            System.out.println("Some problem adding text to styled document");
            e.printStackTrace();
        }

    }


    //Writes a prompt to the prompt file
    //Then outputs dark orange text telling the user what prompt they wrote
    private void addPrompt() {
        int writePromptReturn = prompts.writePrompt(promptToAdd.getText());
        if(writePromptReturn == 0) {
            addOutputText("Prompt either empty or contained a *", styleRed);
            return;
        } else if (writePromptReturn == -1) {
            addOutputText("An unexpected IOException occurred, prompt not wrote.", styleRed);
            return;
        }
        addOutputText("Added prompt: " + promptToAdd.getText(), styleDarkOrange);
    }

    private void deletePrompt() {
        ArrayList<String> deletedPrompts = prompts.deletePrompt(promptToDelete.getText());
        if (deletedPrompts.isEmpty()) {
            addOutputText("Prompt \"" + promptToDelete.getText() + "\" not found", styleRed);
            return;
        }
        addOutputText("Deleted prompt(s): " + deletedPrompts, styleDarkOrange);
    }

    //Just outputs all prompts 1 per line with a number for which one it is
    //This method is used in two places so i don't print a new line before and after it, but I heavily suggest you do that in most situations
    private void outputAllPrompts() {
        ArrayList<String> promptList = prompts.getPrompts();
        promptList.sort(Collections.reverseOrder());
        int promptNum = promptList.size() + 1;

        //This is so that it prints 1 per line which makes it far more readable
        for(String aPrompt : promptList) {
            promptNum--;
            addOutputText(promptNum + ": " + aPrompt, null);
        }
    }
    //The reason I have this is the outputAllPrompts method is used in two places, so I can't do this inside it.
    //And I need to make these lines a runnable. Which maybe I could do a lambda with all three lines, but I don't like it
    private void outputAllPromptsSpaced() {
        addOutputText("", null);
        outputAllPrompts();
        addOutputText("", null);
    }

    //Outputs x random prompts
    //This happens to be the biggest button function purely because of how many different problems it can have
    private void outputXPrompts() {
        int num;
        //We make sure it's actually a number
        try {

            num = Integer.parseInt(getPromptNum.getText());

        } catch (NumberFormatException formatException) {

            //In case they put a non integer
            addOutputText("Tried to get prompts without giving a proper integer.", styleRed);
            formatException.printStackTrace();
            return;
        }
        //If it's greater than 0 we get prompts
        if(num > 0) {

            long promptListSize = prompts.getPrompts().size();
            //If the number is equal to or bigger than the size of all the prompts, we just print them all,
            //while telling the user it's all prompts
            if (num >= promptListSize){

                addOutputText("", null);
                outputAllPrompts();
                addOutputText(
                        "Number of prompts requested(" + num +  ") equal to or greater than number of prompts available(" + promptListSize
                                + "), all prompts returned:", null);

                addOutputText("", null);

                return;
            }

            ArrayList<String> randomPromptList = prompts.getXPromptsJp(num);
            randomPromptList.sort(Comparator.reverseOrder());

            addOutputText("", null);

            //Here's where we actually print out the random prompts
            for(String randPrompt : randomPromptList) {
                addOutputText(randPrompt, null);
            }
            addOutputText("", null);

            //Otherwise we tell the user they input a number too low
        } else {
            addOutputText("Tried to get " + num + " prompts. Number must be 1 or more to get prompts", styleRed);
        }
    }
}