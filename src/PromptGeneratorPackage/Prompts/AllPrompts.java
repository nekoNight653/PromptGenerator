package PromptGeneratorPackage.Prompts;

import java.io.File;
import java.util.ArrayList;

public class AllPrompts implements PromptManager {

    //Rather than returning the typical genre this will return all the prompt type files
    @Override
    public ArrayList<File> getGenres() {
        ArrayList<File> genres = new ArrayList<>();
        for(PromptManager manager : PromptType.MANAGERS) {
            genres.addAll(manager.getGenres());
        }
        return genres;
    }
    //This will get all prompts from a genre
    @Override
    public ArrayList<Prompt> getPrompts(File genre) {

        PromptManager manager = PromptType.getManager(genre);

        if(manager == null) return new ArrayList<Prompt>();

        return manager.getPrompts(genre);
    }
    //This will get the file from the name of the genre file
    @Override
    public String promptNotFound(File path) {

        PromptManager manager = PromptType.getManager(path);

        if(manager == null) return "Unknown type";

        return manager.promptNotFound(path);
    }


    //Down here we have the methods this doesn't use
    //We just return their fail value or the closest thing they have to it

    @Override
    public File getGenreFile(String name) {
        return null;
    }
    @Override
    public boolean deleteGenre(String genreName) {
        return false;
    }

    @Override
    public int createGenre(String name) {
        return -1;
    }

    @Override
    public int addPrompt(String prompt, File genre) {
        return -2;
    }

    @Override
    public ArrayList<String> deletePrompt(String unwantedPrompt, File genre) {
        return new ArrayList<>();
    }
}
