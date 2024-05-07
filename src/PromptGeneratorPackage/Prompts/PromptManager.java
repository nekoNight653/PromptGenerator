package PromptGeneratorPackage.Prompts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//A basic outline for classes that manage prompts
//Maybe overboard?
public interface PromptManager {

    //Gets all genres
    ArrayList<File> getGenres();
    //This gets the genre file from the name of the genre file
    File getGenreFile(String name);
    //Creates a new genre
    int createGenre(String name);

    //deletes a genre
    boolean deleteGenre(String genreName);
    //Gets all prompts
    ArrayList<Prompt> getPrompts(File genre);
    //Gets x random prompts from a file
    //The file is determined by the File in the hashmap and the amount from that file is determined from the integer


    //Creates a prompt
    /*
     * It returns a 1 if it went successfully
     * A 0 if it had incorrect input
     * A -1 if the requested genre file doesn't exist
     * And a -2 if an unexpected error occurred
     */
    int addPrompt(String prompt, File genre);

    //Deletes a prompt
    ArrayList<String> deletePrompt(String unwantedPrompt, File genre);

    /*
     * All this does is return the names of the genre files
     * This is used for the combo boxes in imagePromptPnl
     */
    default String[] getGenreNames() {
        ArrayList<File> genres = getGenres();
        String[] names = new String[genres.size()];

        for (int i = genres.size(); i > 0; i--) {
            names[i - 1] = genres.get(i - 1).getName();
        }
        return names;
    }



    //This method reads each genre file in the past in array
    //it adds all prompts to the hash map with their respective file
    //TextPrompts are contained within a single line and are not blankSpace and don't contain any *
    default ArrayList<Prompt> getPrompts(ArrayList<File> genres) {
        //The first is a prompt and the second is the file it's contained in
        ArrayList<Prompt> promptList = new ArrayList<Prompt>();

        for(File genre : genres) {
            promptList.addAll(getPrompts(genre));
        }
        return promptList;
    }


    /*This gets a select amount of prompts randomly from each file
     * Which files it gets prompts from is decided by the key set of the hashmap "specifications"
     * It then gets x prompts from that file where x is the Integer attached to that key
     *
     * Puts the prompt(promptNotFound, genre) if it runs out of prompts in a genre
     */
    String promptNotFound = "No more prompts";
    default ArrayList<Prompt> getXRandomPrompts(HashMap<File, Integer> specifications) {

        ArrayList<Prompt> randomPrompts = new ArrayList<Prompt>();
        ArrayList<File> genres = new ArrayList<>(specifications.keySet());

        Random random = new Random();


        for(File genre : genres) {

            ArrayList<Prompt> prompts = getPrompts(genre);


            int i = 0;
            if(!prompts.isEmpty()) {
                while (i < specifications.get(genre)) {
                    int index = random.nextInt(prompts.size());


                    randomPrompts.add(prompts.get(index));
                    prompts.remove(index);
                    i++;

                    //This just makes sure it doesn't try and get nonexistent prompts
                    if (prompts.isEmpty() && i < specifications.get(genre)) {

                        randomPrompts.add(new Prompt(promptNotFound, genre));
                        break;
                    }

                }
            } else  {
                randomPrompts.add(new Prompt(promptNotFound, genre));
            }

        }
        return randomPrompts;
    }
}
