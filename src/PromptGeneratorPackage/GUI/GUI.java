package PromptGeneratorPackage.GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //This just gets the percents of the screen width that the input and output panels use, since they will together use all of the screen.
    //I have it like this, so I can easily change it later
    private static final float INPUT_WIDTH_PERCENT = 0.6F;
    private static final float OUTPUT_WIDTH_PERCENT = 1 - INPUT_WIDTH_PERCENT;

    //This is the width of the inputPanel
    public static final int INPUT_WIDTH = (int) (screenSize.width * INPUT_WIDTH_PERCENT);

    //This is the height of both of the prompt panels (TextPromptPnl and ImagePromptPnl)
    public static final int PRMPT_PNL_HEIGHT = (int) (screenSize.height * 0.3);

    //This is the size of the output panel (Not sure if I really need this as a constant)
    public static final int OUTPUT_WIDTH = (int) (screenSize.width * OUTPUT_WIDTH_PERCENT);

    //The fonts for the input panel and output panel
    public static final Font inputFont = new Font("serif", Font.PLAIN, 30);
    public static final Font outputFont = new Font("serif", Font.PLAIN, 25);

    /*
     * Dark orange is used for text that is important such as writing and deleting prompts
     * Since it's important to know if you accidentally wrote or deleted a prompt
     * Red is used for errors such as bad input like putting a letter into the getPromptNum text field which wants a number
     * And for all other cases you use null for the normal black text
     */
    public static Style STYLE_RED, STYLE_DARK_ORANGE;

    //These are just the names of all my buttons
    //I don't know if I should use them anymore (I use to use them to check  which button was being clicked, but I use lambdas now)

    //TextPromptPnl button names
    public static final String ADD_GENRE_BUTTON_NAME = "Create genre";
    public static final String DELETE_GENRE_BUTTON_NAME = "Delete genre";
    public static final String ADD_BUTTON_NAME = "Add prompt";
    public static final String GET_BUTTON_NAME = "Truly random prompts";
    public static final String DELETE_BUTTON_NAME = "Delete prompt";
    public static final String GET_ALL_BUTTON_NAME = "Get all prompts";
    public static final String PARAMED_RAND_PRMPT_BTTN_NAME = "Get random prompts";
    public static final String CLEAR_INPUT_BUTTON_NAME = "Clear input";
    public static final String GET_GENRES_BUTTON_NAME = "Get all genres";
    public static final String CLEAR_OUTPUT_BUTTON_NAME = "Clear output";
    public static final String UNKOWN_BUTTON_NAME = "????";

    //ImagePromptsPnl button names

    public static final String DISPLAY_IMAGE_BUTTON_NAME = "Display image";

    private final JFrame frame = new JFrame();

    /*
    * textPromptsPnl contains all the input methods for dealing with text prompts and a little bit extra
    * imagePromptsPnl contains all the input methods for dealing with image prompts
    * inputPnl contains the textPromptsPnl and imagePromptsPnl
    */
    private JPanel inputPnl, outputPanel;
    //The labels just identify where the text panel and image panel start
    private JLabel textPrmptLbl, imagePrmptLbl;
    //For what ever I want to display, such as prompt added or which prompts were got
    private StyledDocument outputStyled;
    private JScrollPane outputScrollable;
    private JTextPane output;

    public void gui() {

        TextPromptPnl textPromptsPnl = new TextPromptPnl();
        ImagePromptPnl imagePromptsPnl = new ImagePromptPnl();

        /*
        * Up here we declare and modify the panels that hold all the most the components
        * Down near the bottom of the gui method you'll find the panel inputPanel.
        * It just contains label("Text prompts"), textPromptsPnl, label("Image prompts")  imagePromptsPnl
        * You'll also find the frame down there
        */
        outputPanel = new JPanel();
        outputPanel.setBorder(BorderFactory.createEmptyBorder());
        outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, screenSize.height));
        outputPanel.setLayout(new GridLayout());


        /*
         ******
         * Here is where we add and create the JTextPane for the outputPanel
         ******
         */

        output = new JTextPane();
        output.setFont(outputFont);

        outputScrollable = new JScrollPane(output);
        outputStyled = output.getStyledDocument();

        STYLE_RED = outputStyled.addStyle("RedStyle", null);
        StyleConstants.setForeground(STYLE_RED, Color.RED);

        //I made my own color here because orange was a bit too bright, so I lowered the numbers by a hundred-ish
        STYLE_DARK_ORANGE = outputStyled.addStyle("DarkOrangeStyle", null);
        StyleConstants.setForeground(STYLE_DARK_ORANGE, new Color(150, 100, 0));

        output.setEditable(false);
        output.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.gray));
        outputPanel.add(outputScrollable);

        inputPnl = new JPanel();
        inputPnl.setFont(inputFont);
        inputPnl.setBorder(BorderFactory.createEmptyBorder());
        inputPnl.setPreferredSize(new Dimension(INPUT_WIDTH, screenSize.height));
        inputPnl.setLayout(new BoxLayout(inputPnl, BoxLayout.Y_AXIS));

        textPrmptLbl = new JLabel("Text prompts");
        textPrmptLbl.setFont(inputFont);
        inputPnl.add(textPrmptLbl);

        inputPnl.add(textPromptsPnl);

        imagePrmptLbl = new JLabel("Image prompts");
        imagePrmptLbl.setFont(inputFont);
        inputPnl.add(imagePrmptLbl);

        inputPnl.add(imagePromptsPnl);


        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("PromptGeneratorPackage.PromptsFldr.Prompt generator");
        frame.pack();
        frame.setVisible(true);

        frame.add(outputPanel, BorderLayout.EAST);
        frame.add(inputPnl, BorderLayout.CENTER);
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
    public void outputln(String text, Style style) {
        try {
            outputStyled.insertString(0, text + "\n", style);
            //This because otherwise it teleports you to the bottom everytime
            output.setCaretPosition(0);

        } catch (BadLocationException e) {
            System.out.println("Some problem adding text to styled document");
            e.printStackTrace();
        }
    }


    /*
    * This method takes a buffered image and displays it in the output
    * It resizes the image if it's too big and keeps the dimensions intact
    * Then converts it to an iconStyle? (I don't know why it's done like this but everyone seems to do it)
    *
    * Note after that when the gui gets a scrollbar it gets shorter by like 15px (I didn't notice it with just text,
    *  but since the images don't retroactively scale with the gui it was very obvious)
    */
    public void outputImg(BufferedImage originalImage) {

        //We multiply by 0.95 because it will try and increase the output panel size slowly over time if we don't
        int maxWidth = (int) (output.getWidth() * 0.95);
        int maxHeight = (int) (output.getHeight() * 0.95);

        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();


        try {
            // if it's smaller than the max width and height we don't need to do anything!
            if (imageWidth < maxWidth && imageHeight < maxHeight) {

                SimpleAttributeSet iconStyle = new SimpleAttributeSet();
                StyleConstants.setIcon(iconStyle, new ImageIcon(originalImage));

//                outputln("Width: " + imageWidth +" Height: " + imageHeight + " Output width: " + output.getWidth() + " Output height: " + output.getHeight(), styleRed);

                outputStyled.insertString(0, "Image " + "\n", iconStyle);

            // Otherwise we need to scale it appropriately
            } else {
                //Gets the ratio of the width to the height
                //Example 1000 width 10 height would return 100 because the ratio is 100-Width to 1-Height
                //Example two 10 width 1000 height would return 0.01 because the ratio is 1-Width to 100-Height
                //
                //You have to cast to float, or it will round it to an int since two it's just two ints in there
                float dimensionsRatio = (float) imageWidth /imageHeight;

                int newWidth;
                int newHeight;

                if(maxWidth < imageWidth) {

                    int offset = imageWidth - maxWidth;
                    newWidth = maxWidth;
                    newHeight = (int) (imageHeight - offset / dimensionsRatio);
                } else {
                    int offset = imageHeight - maxHeight;
                    newHeight = maxHeight;
                    newWidth = (int) (imageWidth - offset * dimensionsRatio);
                }

                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                graphics2D.dispose();

                SimpleAttributeSet iconStyle = new SimpleAttributeSet();
                StyleConstants.setIcon(iconStyle, new ImageIcon(resizedImage));

//                outputln("Width: " + newWidth +" Height: " + newHeight + " Output width: " + output.getWidth() + " Output height: " + output.getHeight(), styleRed);

                outputStyled.insertString(0, "Image " + "\n", iconStyle);
            }
            //This because otherwise it teleports you to the bottom everytime
            output.setCaretPosition(0);

        } catch (BadLocationException e) {
            System.out.println("Couldn't output image");
            e.printStackTrace();
        }
    }

    //Clears the output textPane
    public void clearOuput() {
        output.setText("");
    }
}