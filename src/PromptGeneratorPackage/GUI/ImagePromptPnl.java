package PromptGeneratorPackage.GUI;

import javax.swing.*;
import java.awt.*;

public class ImagePromptPnl extends JPanel {

    public ImagePromptPnl() {
        this.setBorder(BorderFactory.createEmptyBorder());
        //This is only to set the preferred height (I just set the preferred width to the preferred width of the controlPnl)
        //I use constants in the class PromptGeneratorPackage.GUI for the size, so I can set the size from the PromptGeneratorPackage.GUI class (If I ever decide to resize them)
        this.setPreferredSize(new Dimension(GUI.INPUT_WIDTH, GUI.PRMPT_PNL_HEIGHT));
        this.setLayout(new GridBagLayout());
    }
}
