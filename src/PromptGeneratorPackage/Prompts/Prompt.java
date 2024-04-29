package PromptGeneratorPackage.Prompts;

import java.io.File;

//This is just basically a tuple or pair or whatever for prompts
//Note for image prompts the string is just the file path
public class Prompt {
    private String prompt;
    private File genre;

    public Prompt(String prompt, File genre) {
        this.prompt = prompt;
        this.genre = genre;
    }
    public String prompt() {
        return this.prompt;
    }
    public File genre() {
        return this.genre;
    }
    //Allows you to set prompts. I didn't think I'd need this, but I found a singular case where I do
    public void setPrompt(String newPrompt) {
        prompt = newPrompt;
    }
    //I don't need this, but you know it makes just makes it nice and symmetrical
//    public void setGenre(File newGenre) {
//        genre = newGenre;
//    }
}
