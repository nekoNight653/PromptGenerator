package PromptGeneratorPackage.Prompts;


import PromptGeneratorPackage.GUI.ImagePromptPnl;
import PromptGeneratorPackage.GUI.PromptPnl;
import PromptGeneratorPackage.GUI.TextPromptPnl;

import java.io.File;

//This handles all the conversion of file paths and mangers to types and vice versa
//There are a lot of if statements involved in this (I might figure out a better way later, but for now it's if statements)
public enum PromptType {
    //This is just a replacement for null (Last time null went into one of my prompts it just broke so hopefully this is better)
    //It shouldn't really get used unless an error has happened
    NONE,
    TEXT,
    IMAGE;

    //An array of every manager we have
    //Note the all manager isn't counted as a manager,
    // though it does implement the PromptManager interface for the purpose of this enum it doesn't exist
    public static final PromptManager[] MANAGERS = new PromptManager[]{new TextPrompts(), new ImagePrompts()};
    //An array of every Prompt panel we have
    //This is because the prompt panels are the managers of everything to do with outputting prompts
    public static final PromptPnl[] PANELS = new PromptPnl[]{ new TextPromptPnl(), new ImagePromptPnl()};

    public static PromptType getType(File file) {
        String path = file.getPath();

        if(path.contains(TextPrompts.TEXT_PROMPT_FOLDER.getPath())) return TEXT;
        if(path.contains(ImagePrompts.IMAGE_PROMPTS_FOLDER.getPath())) return IMAGE;

        return NONE;
    }

    public static PromptManager getManager(PromptType type) {
        return switch (type) {
            case TEXT -> MANAGERS[0];
            case IMAGE -> MANAGERS[1];
            default -> null;
        };
    }
    public static PromptManager getManager(File path){
        return getManager(getType(path));
    }

    public static PromptPnl getPanel(PromptType type) {
        return switch (type) {
            case TEXT -> PANELS[0];
            case IMAGE -> PANELS[1];
            default -> null;
        };
    }

}
