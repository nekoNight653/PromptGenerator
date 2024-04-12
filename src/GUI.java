import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class GUI {

    private final Prompts prompts = new Prompts();

    private static final String ADD_GENRE_BUTTON_NAME = "Create genre";
    private static final String DELETE_GENRE_BUTTON_NAME = "Delete genre";
    private static final String ADD_BUTTON_NAME = "Add prompt";
    private static final String GET_BUTTON_NAME = "Get X random prompts";
    private static final String DELETE_BUTTON_NAME = "Delete prompt";
    private static final String GET_ALL_BUTTON_NAME = "Get all prompts";
    private static final String CLEAR_INPUT_BUTTON_NAME = "Clear input";
    private static final String GET_GENRES_BUTTON_NAME = "Get all genres";
    private static final String CLEAR_OUTPUT_BUTTON_NAME = "Clear output";

    //The dimensions for all the controls such as buttons and text input areas
    //private Dimension controlsSize = new Dimension(20, 30);

    private JFrame frame = new JFrame();
    //One panel specifically for the output since it has to be bigger than the controls
    private JPanel controlPanel, outputPanel;
    //For what ever I want to display, such as prompt added or which prompts were got
    private StyledDocument outputStyled;
    private JScrollPane outputScrollable;
    private JTextPane output;


    //Genre related buttons
    /*
    * addGenreButton is the button for adding genres
    * deleteGenreButton is the button for deleting genres
    * getGenresButton is the button for getting all genres
    */
    private JButton addGenreButton, deleteGenreButton, getGenresButton;

    //Prompt related buttons
    /*
    * addButton is the button for adding prompts
    * deleteButton is the button for deleting prompts
    * getButton is the button for getting prompts X random prompts where X is inputted by the user
    * getAllButton is the button for getting all prompts
    * clearWindowButton is the button for clearing the output window
    */
    private JButton addButton, deleteButton, getButton, getAllButton;

    //Misc buttons
    /*
    *
    *
    */
    private JButton clearOutputButton, clearInputButton;

    /* The text fields
    * genreToAdd is the input for which genre to add
    * genreToDelete is the input for which genre to delete
    * promptToAdd is the input for adding a prompt
    * getPromptNum is the number of how many random prompts you want to get
    * promptToDelete is the specifier for which prompt you want to delete
    */
    private JTextField genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete;


    /*
    * Dark orange is used for text that is important such as writing and deleting prompts
    * Since it's important to know if you accidentally wrote or deleted a prompt
    * Red is used for errors such as bad input like putting a letter into the getPromptNum text field which wants a number
    * And for all other cases you use null for the normal black text
     */
    private Style styleRed, styleDarkOrange;
    /*
    * control font is size 30 and the font for all the control panel buttons
    * outputFont font is size 25 and the font for the output panel
    * Other than that they're both Font.PLAIN
    */
    private Font controlFont, outputFont;

    /*
    * Next up is getting the checkboxes or radio boxes to work..
    * Or if I find a better multiple choice thing those to work
    */

    public void gui() {

        int y = 0;

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

        addGenreButton = new JButton(ADD_GENRE_BUTTON_NAME);
        addGenreButton.addActionListener(e -> addGenre());
        addGenreButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = y;
        controlPanel.add(addGenreButton, gbc);

        genreToAdd = new JTextField();
        genreToAdd.addActionListener(e -> addGenre());
        genreToAdd.setName("genreToAdd");
        genreToAdd.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(genreToAdd, gbc);

        deleteGenreButton = new JButton(DELETE_GENRE_BUTTON_NAME);
        deleteGenreButton.addActionListener(e -> deleteGenre());
        deleteGenreButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(deleteGenreButton, gbc);

        genreToDelete = new JTextField();
        genreToDelete.addActionListener(e -> deleteGenre());
        genreToDelete.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(genreToDelete, gbc);

        addButton = new JButton(ADD_BUTTON_NAME);
        addButton.addActionListener(e -> addPrompt());
        addButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(addButton, gbc);

        promptToAdd = new JTextField();
        promptToAdd.addActionListener(e -> addPrompt());
        promptToAdd.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(promptToAdd, gbc);



        deleteButton = new JButton(DELETE_BUTTON_NAME);
        deleteButton.addActionListener(e -> deletePrompt());
        deleteButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(deleteButton, gbc);


        promptToDelete = new JTextField();
        promptToDelete.addActionListener(e -> deletePrompt());
        promptToDelete.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(promptToDelete, gbc);


        getButton = new JButton(GET_BUTTON_NAME);
        getButton.addActionListener(e -> outputXPrompts());
        getButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(getButton, gbc);

        getPromptNum = new JTextField();
        getPromptNum.addActionListener(e -> outputXPrompts());
        getPromptNum.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(getPromptNum, gbc);


        getAllButton = new JButton(GET_ALL_BUTTON_NAME);
        getAllButton.addActionListener(e -> outputAllPromptsSpaced());
        getAllButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(getAllButton, gbc);

        clearInputButton = new JButton(CLEAR_INPUT_BUTTON_NAME);
        clearInputButton.addActionListener(e -> clearInput());
        clearInputButton.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(clearInputButton, gbc);

        getGenresButton = new JButton(GET_GENRES_BUTTON_NAME);
        getGenresButton.addActionListener(e -> outputAllGenres());
        getGenresButton.setFont(controlFont);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        controlPanel.add(getGenresButton, gbc);

        clearOutputButton = new JButton(CLEAR_OUTPUT_BUTTON_NAME);
        clearOutputButton.addActionListener(e -> output.setText(""));
        clearOutputButton.setFont(controlFont);
        gbc.gridx = 1;
        gbc.gridy = y;
        controlPanel.add(clearOutputButton, gbc);



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

    //Genre related button functions

    //Creates a genre.txt file (Uses the input in the text field genreToAdd)
    //unless the name is blank, or the file already exists, or there was an unforeseen error
    //In which case it tells you what the problem was
    private void addGenre() {
        String genreName = genreToAdd.getText().replace(' ', '_');

        if(genreName.isBlank()) {
            addOutputText("Genre name blank", styleRed);
            return;
        }

        int addGenreReturn = prompts.createNewGenre(genreName);
        if(addGenreReturn == 1) {
            addOutputText("Genre \"" + genreName + "\" created", styleDarkOrange);
        } else if (addGenreReturn == 0) {
            addOutputText("Genre \"" + genreName + "\" already exists", styleRed);
        } else {
            addOutputText("Problem adding genre \"" + genreName + "\"", styleRed);
        }
    }

    //Deletes a genre (Uses the input in the textField genreToAdd)
    //Or if a problem occurred it will say "couldn't delete genre file "name of requested genre to delete" "
    private void deleteGenre() {
        String genreName = genreToDelete.getText().replace(' ', '_');

        if(prompts.deleteGenre(genreName)) addOutputText("Genre \"" + genreName + "\" deleted", styleDarkOrange);

        else addOutputText("Failed to delete genre file \"" + genreName + "\"", styleRed);
    }

    //Outputs all genre file names
    private void outputAllGenres() {
        ArrayList<String> allNames = prompts.getGenreNames();
        allNames.sort(Comparator.reverseOrder());
        //This makes it so that there's a number counting how many genres there are.
        //I'm not sure it's required since there's probably not going to be too many of them but...
        int genreNum = allNames.size() + 1;

        //The addOutputText("", null); just prints a blank line because a new line is built in to the method
        addOutputText("", null);
        for(String name : allNames) {
            addOutputText( (--genreNum) + ": " + name, null);
        }
        addOutputText("", null);
    }


    //Prompt related buttons

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

    //It goes through each genre and outputs all prompts 1 per line with a number for which one it is in that genre
    //This method is used in two places, so I don't print a new line before and after it, but I heavily suggest you do that in most situations
    private void outputAllPrompts() {

        for(File genre : prompts.getGenres()) {
            int promptNum = prompts.getPrompts(genre).size() + 1;

            ArrayList<Prompt> promptList = prompts.getPrompts(genre);
            ArrayList<String> stringPrompts = new ArrayList<>();

            //We do this, so we can actually sort the list alphabetically..
            for(Prompt prompt : promptList) {
                stringPrompts.add(prompt.getPrompt());
            }
            stringPrompts.sort(Comparator.reverseOrder());

            //This is so that it prints 1 per line which makes it far more readable
            for (String prompt : stringPrompts) {
                promptNum--;
                addOutputText(promptNum + ": " + prompt, null);
            }

            addOutputText(genre.getName().replace(".txt", "") + ":", null);
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
//        int num;
//        //We make sure it's actually a number
//        try {
//
//            num = Integer.parseInt(getPromptNum.getText());
//
//        } catch (NumberFormatException formatException) {
//
//            //In case they put a non integer
//            addOutputText("Tried to get prompts without giving a proper integer.", styleRed);
//            formatException.printStackTrace();
//            return;
//        }
//        //If it's greater than 0 we get prompts
//        if(num > 0) {
//
//            long promptListSize = prompts.getPrompts().size();
//            //If the number is equal to or bigger than the size of all the prompts, we just print them all,
//            //while telling the user it's all prompts
//            if (num >= promptListSize){
//
//                addOutputText("", null);
//                outputAllPrompts();
//                addOutputText(
//                        "Number of prompts requested(" + num +  ") equal to or greater than number of prompts available(" + promptListSize
//                                + "), all prompts returned:", null);
//
//                addOutputText("", null);
//
//                return;
//            }
//
//            ArrayList<String> randomPromptList = prompts.getXPromptsJp(num);
//            randomPromptList.sort(Comparator.reverseOrder());
//
//            addOutputText("", null);
//
//            //Here's where we actually print out the random prompts
//            for(String randPrompt : randomPromptList) {
//                addOutputText(randPrompt, null);
//            }
//            addOutputText("", null);
//
//            //Otherwise we tell the user they input a number too low
//        } else {
//            addOutputText("Tried to get " + num + " prompts. Number must be 1 or more to get prompts", styleRed);
//        }
    }
    //Clears the input in all JTextFields. This was a suggestion by my "client"(family member)

    private void clearInput() {
        JTextField[] textFields = new JTextField[]{genreToAdd, genreToDelete, promptToAdd, getPromptNum, promptToDelete};

        for (JTextField field : textFields) {
            field.setText("");
        }
    }
}