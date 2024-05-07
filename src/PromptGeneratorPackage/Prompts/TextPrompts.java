package PromptGeneratorPackage.Prompts;

import PromptGeneratorPackage.PromptGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

//Just a class handling all things to do with the prompts folder
public class TextPrompts implements PromptManager {
    public static final File TEXT_PROMPT_FOLDER = new File(PromptGenerator.PROMPTS_FOLDER, "Text_prompts");


    //Returns an array list of all the files in the prompt folder
    @Override
    public ArrayList<File> getGenres() {
        ArrayList<File> genres = new ArrayList<File>();
        if(!TEXT_PROMPT_FOLDER.exists()) {
            TEXT_PROMPT_FOLDER.mkdir();
            return genres;
        }

        Collections.addAll(genres, TEXT_PROMPT_FOLDER.listFiles());
        return genres;
    }

    //Yes this basically identical to the default in the interface, but I really wanted to get rid of the .txt
    @Override
    public String[] getGenreNames() {
        ArrayList<File> genres = getGenres();
        String[] names = new String[genres.size()];

        for (int i = genres.size(); i > 0; i--) {
            names[i - 1] = genres.get(i - 1).getName().replace(".txt", "");
        }
        return names;
    }

    //This gets a file from a genre name
    @Override
    public File getGenreFile(String name) {
        return new File(TEXT_PROMPT_FOLDER, name + ".txt");
    }

    /*
    * returns 1 if everything went well
    * 0 if the file already exists
    * -1 if an unexpected error occurred
    *
    * This just creates a new txt file with the string passed in as the name
     */
    @Override
    public int createGenre(String name) {
        if(!PromptGenerator.PROMPTS_FOLDER.exists()) PromptGenerator.PROMPTS_FOLDER.mkdir();
        if(!TEXT_PROMPT_FOLDER.exists()) TEXT_PROMPT_FOLDER.mkdir();

        File newGenre = new File (TEXT_PROMPT_FOLDER, name + ".txt");


        //Here we return if it runs into any problem with creating it
        try {
            if(!newGenre.createNewFile()) {
                System.out.println("Genre " + name + " already exists");
                return 0;
            }

        } catch (IOException e) {
            System.out.println("Problem creating new genre " + name);
            e.printStackTrace();
            return -1;
        }

        System.out.println("File \"" + newGenre + "\" created");

        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(newGenre));
            fileWriter.write("""
                            *Welcome to a genre file! All lines that aren't a prompt must contain a * or be empty
                            *All prompts must be within 1 line and can't contain a *
                            *Prompts can contain a-z 0-9 _-&.,'[]{}()/?!+=~"\\
                            *I assume most other symbols work, but I haven't checked if they do. Use them at your own risk
                            *Any line that's not empty/blank and doesn't have a * will be considered a prompt
                            *This file must be in the same directory as the jar file, otherwise it will create a new prompts file
                             
                             """);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing starting line in a genre file?!");
            e.printStackTrace();
        }

        return 1;
    }

    //Just deletes a genre file
    //Takes a string without the .txt I could have made it take a file, but I don't need it like that
    @Override
    public boolean deleteGenre(String genreName) {
        if(!PromptGenerator.PROMPTS_FOLDER.exists()) {
            PromptGenerator.PROMPTS_FOLDER.mkdir();
            return false;
        }
        if(!TEXT_PROMPT_FOLDER.exists()) {
            TEXT_PROMPT_FOLDER.mkdir();
            return false;
        }
        File genre = new File(TEXT_PROMPT_FOLDER, genreName + ".txt");
        return genre.delete();
    }



    /*
    * This method returns an array list of prompts
    * It does this by reading through the whole file and returning each line that's a valid prompt
    * Valid prompts are any line that's not blank and doesn't contain a *
     */
    @Override
    public ArrayList<Prompt> getPrompts(File genre){
        ArrayList<Prompt> promptList = new ArrayList<Prompt>();

        if(!genre.exists()) {
            return promptList;
        }

        InputStreamReader isr;
        BufferedReader reader;
        try {
            //We use an InputStreamReader with StandardCharsets.UTF_8 so that reading Japanese (and potentially other special symbols)
            //actually works. Note this error didn't occur while I was in the IDE only outside of it after I built the program so be careful
            isr = new InputStreamReader(new FileInputStream(genre), StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);

        } catch (FileNotFoundException e) {
            System.out.println("File " + genre + " not found");
            e.printStackTrace();
            return promptList;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {

                if (!line.contains("*") && !line.isBlank()) {
                    promptList.add(new Prompt(line, genre));
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Problem reading file: " + genre);
            e.printStackTrace();
        }

        return promptList;
    }


    /*
    * Writes a prompt to a genre file
    * TextPrompts must be only 1 line and not blank
    * TextPrompts also can't have a *, while it will write those they will never be found by any methods for getting prompts
    * It also creates the prompt folder if it doesn't exist
    * It returns a 1 if it went successfully
    * A 0 if it had incorrect input
    * A -1 if the requested genre file doesn't exist
    * And a -2 if an unexpected error occurred
     */
    @Override
    public int addPrompt(String prompt, File genre) {
        //Creates the file TextPrompts if it doesn't exist

        if(!PromptGenerator.PROMPTS_FOLDER.exists()) PromptGenerator.PROMPTS_FOLDER.mkdir();
        if(!TEXT_PROMPT_FOLDER.exists()) TEXT_PROMPT_FOLDER.mkdir();

        if(!genre.exists()) return -1;

        if (prompt.contains("*") || prompt.isBlank()) return 0;

        try {
            //We use an OutputStreamWriter with StandardCharsets.UTF_8 so that writing Japanese (and potentially other special symbols)
            //actually works. Note this error didn't occur while I was in the IDE only outside of it after I built the program so be careful
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(genre, true), StandardCharsets.UTF_8);
            BufferedWriter promptWriter = new BufferedWriter(osw);

            promptWriter.write("\n" + prompt);
            promptWriter.close();
            System.out.println("Prompt: \"" + prompt + "\" wrote");

        } catch (IOException e) {
            System.out.println("Error writing prompt in file for some reason");
            e.printStackTrace();
            return -2;
        }
        return 1;
    }

    //Deletes a prompt by rewriting the entire file without the prompt
    //The only way to not have to rewrite the whole things was something about byte editing
    //And yes this can delete any line even if it's not a valid prompt
    @Override
    public ArrayList<String> deletePrompt(String unwantedPrompt, File genre) {
        ArrayList<String> deletedPrompts = new ArrayList<String>();

        if(!PromptGenerator.PROMPTS_FOLDER.exists()) {
            PromptGenerator.PROMPTS_FOLDER.mkdir();
            return deletedPrompts;
        }
        if(!TEXT_PROMPT_FOLDER.exists()){
            TEXT_PROMPT_FOLDER.mkdir();
            return deletedPrompts;
        }
        if(!genre.exists()){
            return deletedPrompts;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(genre.toPath());

        } catch (IOException e) {
            System.out.println("Problem reading file");
            e.printStackTrace();
            return deletedPrompts;
        }

        OutputStreamWriter osw;
        BufferedWriter buffWriter;
        try {
            //We use an OutputStreamWriter with StandardCharsets.UTF_8 so that writing Japanese (and potentially other special symbols)
            //actually works. Note this error didn't occur while I was in the IDE only outside of it after I built the program so be careful
            osw = new OutputStreamWriter(new FileOutputStream(genre), StandardCharsets.UTF_8);
            buffWriter = new BufferedWriter(osw);
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + genre + "\" not found");
            e.printStackTrace();
            return deletedPrompts;
        }

        //Here's where we actually rewrite the file without the prompt in it
        try {
            //Since it's a fencepost problem as far as \n go
            if (!lines.get(0).equals(unwantedPrompt)) {
                buffWriter.write(lines.get(0));
            } else {
                deletedPrompts.add(lines.get(0));
            }
            lines.remove(0);

            //Looping through the file to rewrite it without the specified string
            for (String line : lines) {
                if (!line.equals(unwantedPrompt)) {

                    buffWriter.write("\n" + line);

                } else {
                    deletedPrompts.add(line);
                }
            }
            buffWriter.close();
        } catch (IOException e) {
            System.out.println("Problem writing to genre \"" + genre + "\"");
            return deletedPrompts;
        }

        System.out.println("these " + deletedPrompts + " prompts have been deleted");
        return deletedPrompts;
    }


    /*
     * I override this next method because I wanted a low chance for it to give an extra prompt of "Surprise extra prompt! 日本語で書きます"
     * This extra prompt is to write it in japanese. I only did this because I know who is going to be using this
     * Since it's an extra prompt you will still get all your original prompts
     * That means you can just choose to ignore the japanese prompt if you so desire
     * the HashMap specifications is just how many prompts from which files.
     * Each file has a number attached to it which is the number to get from that file
     */
    @Override
    public ArrayList<Prompt> getXRandomPrompts(HashMap<File,  Integer> specifications) {
        ArrayList<Prompt> randPrompts = PromptManager.super.getXRandomPrompts(specifications);
        Random random = new Random();

        if(random.nextInt(30) == 0) {
            randPrompts.add(new Prompt("Surprise extra prompt! 日本語で書きます", TEXT_PROMPT_FOLDER));
        }
        return randPrompts;
    }



}
