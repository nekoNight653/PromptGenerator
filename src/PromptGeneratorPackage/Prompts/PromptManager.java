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
    /*
    * returns 1 if everything went well
    * 0 if the file already exists
    * -1 if an unexpected error occurred
    * */
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



    //This method loops through each genre passed in and calling the getPrompts method for each of them
    default ArrayList<Prompt> getPrompts(ArrayList<File> genres) {
        //The first is a prompt and the second is the file it's contained in
        ArrayList<Prompt> promptList = new ArrayList<Prompt>();

        for(File genre : genres) {
            promptList.addAll(getPrompts(genre));
        }
        return promptList;
    }


    /*
     * This gets a select amount of prompts randomly from each file
     * Which files it gets prompts from is decided by the key set of the hashmap "specifications"
     * It then gets x prompts from that file where x is the Integer attached to that key
     *
     * Puts the prompt(promptNotFound, genre) if it runs out of prompts in a genre
     */
    //This is just because each prompt manager will have a different return for not finding a prompt
    //We pass in the path just for the random prompts class
    String promptNotFound(File path);
    default ArrayList<Prompt> getXRandomPrompts(HashMap<File, Integer> specifications) {

        ArrayList<Prompt> randomPrompts = new ArrayList<Prompt>();
        ArrayList<File> genres = new ArrayList<>(specifications.keySet());

        Random random = new Random();


        for(File genre : genres) {

            ArrayList<Prompt> prompts = getPrompts(genre);

            //If it's empty we skip adding a prompt to tell the user we ran out of prompts in that genre
            if(prompts.isEmpty()) {
                randomPrompts.add(new Prompt(promptNotFound(genre), genre, PromptType.getType(genre)));
                continue;
            }

            //Get random prompts
            int i = 0;
            while (i < specifications.get(genre)) {
                int index = random.nextInt(prompts.size());


                randomPrompts.add(prompts.get(index));
                PromptType lastType = prompts.get(index).type();

                prompts.remove(index);
                i++;

                //This just makes sure it doesn't try and get nonexistent prompts
                if (prompts.isEmpty() && i < specifications.get(genre)) {

                    randomPrompts.add(new Prompt(promptNotFound(genre), genre, lastType));
                    break;
                }

            }

        }
        return randomPrompts;
    }
}
