import java.io.*;
import java.nio.file.Files;
import java.util.*;

//Just a class handling all things to do with the prompts folder
public class Prompts {
    public static final File PROMPTS_FOLDER = new File(System.getProperty("user.dir"), "Prompts");


    //Returns an array list of all the files in the prompt folder
    public ArrayList<File> getGenres() {
        ArrayList<File> genres = new ArrayList<File>();
        if(!PROMPTS_FOLDER.exists()) {
            PROMPTS_FOLDER.mkdir();
            return genres;
        }

        Collections.addAll(genres, PROMPTS_FOLDER.listFiles());
        return genres;
    }

    //All this does is loops through all the files in Prompts and gets the name - the .txt
    //Then it adds them to the arrayList of names
    public ArrayList<String> getGenreNames() {
        ArrayList<String> fileNames = new ArrayList<>();

        if(!PROMPTS_FOLDER.exists()) {
            PROMPTS_FOLDER.mkdir();
            return fileNames;
        }


        for (File genre : getGenres()) {
            String name = genre.getName().replace(".txt", "");
            fileNames.add(name);
        }

        return fileNames;
    }

    //This gets a file from a genre name
    //Returns null if none are found
    //It's tiny and I almost just put it in the GUI class since that's what needed it
    //... but I figured it made sense here
    public File getGenreFile(String name) {
        return new File(PROMPTS_FOLDER, name + ".txt");
    }

    /*
    * returns 1 if everything went well
    * 0 if the file already exists
    * -1 if an unexpected error occurred
    *
    * This just creates a new txt file with the string passed in as the name
     */
    public int createNewGenre(String name) {
        if(!PROMPTS_FOLDER.exists()) PROMPTS_FOLDER.mkdir();

        File newGenre = new File (PROMPTS_FOLDER, name + ".txt");



        try {
            if(newGenre.createNewFile()) {
                System.out.println("Created new genre " + name);

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

            } else {
                System.out.println("Genre " + name + " already exists");
                return 0;
            }

        } catch (IOException e) {
            System.out.println("Problem creating new genre " + name);
            e.printStackTrace();
            return -1;
        }

    }

    //Just deletes a genre file
    public boolean deleteGenre(String genreName) {
        if(!PROMPTS_FOLDER.exists()) {
            PROMPTS_FOLDER.mkdir();
            return false;
        }
        File genre = new File(PROMPTS_FOLDER, genreName + ".txt");
        return genre.delete();
    }



    /*
    * This method returns an array list of prompts
    * It does this by reading through the whole file and returning each line that's a valid prompt
    * Valid prompts are any line that's not blank and doesn't contain a *
     */
    public ArrayList<Prompt> getPrompts(File genre){
        ArrayList<Prompt> promptList = new ArrayList<Prompt>();

        if(!PROMPTS_FOLDER.exists()) {
            PROMPTS_FOLDER.mkdir();
            return promptList;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(genre));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (!line.contains("*") && !line.isBlank()) {
                        promptList.add(new Prompt(line, genre));
                    }
                }

            } catch (IOException e) {
                System.out.println("Problem reading file: " + genre);
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + genre + " not found");
            e.printStackTrace();
        }

        return promptList;
    }


    //This method reads each genre file in the past in array
    //it adds all prompts to the hash map with their respective file
    //Prompts are contained within a single line and are not blankSpace and don't contain any *
    public ArrayList<Prompt> getPrompts(ArrayList<File> genres) {
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
    * If it runs out of prompts it adds to randomPrompts Prompt("Out of prompts", genre);
    */
    public ArrayList<Prompt> getXRandomPrompts(HashMap<File, Integer> specifications) {
        ArrayList<Prompt> randomPrompts = new ArrayList<Prompt>();
        ArrayList<File> genres = new ArrayList<>(specifications.keySet());


        ArrayList<Prompt> allPrompts = getPrompts(genres);
        Random random = new Random();


        int summedNum = 0;
        for (int i : specifications.values()) summedNum = summedNum + i;



        int promptNum = allPrompts.size();
        if(summedNum > promptNum) {
            System.out.println("Number of prompts(" + promptNum + ") not sufficient for number of prompts wanted(" + summedNum + "). Returning all prompts instead");
            return allPrompts;
        }


        for(File genre : genres) {

            ArrayList<Prompt> prompts = getPrompts(genre);

            if(!prompts.isEmpty()) {
                int i = 0;
                while (i < specifications.get(genre) ) {
                    int index = random.nextInt(prompts.size());


                    randomPrompts.add(prompts.get(index));
                    prompts.remove(index);
                    i++;

                    //This just makes sure it doesn't try and get nonexistent prompts
                    if(prompts.isEmpty() && i < specifications.get(genre)) {

                        randomPrompts.add(new Prompt("No more prompts in genre \""
                                + genre.getName().replace(".txt", "") + "\"", genre));

                        break;
                    }

                }
            } else {
                randomPrompts.add(new Prompt("No prompts to be found", genre));
            }
        }
        return randomPrompts;
    }


    /*
    * Writes a prompt to a genre file
    * Prompts must be only 1 line and not blank
    * Prompts also can't have a *, while it will write those they will never be found by any methods for getting prompts
    * It also creates the prompt folder if it doesn't exist
    * It returns a 1 if it went successfully
    * A 0 if it had incorrect input
    * A -1 if the requested genre file doesn't exist
    * And a -2 if an unexpected error occurred
     */
    public int writePrompt(String prompt, File genre) {
        //Creates the file Prompts if it doesn't exist
        if(!PROMPTS_FOLDER.exists()) PROMPTS_FOLDER.mkdir();
        if (prompt.contains("*") || prompt.isBlank()) return 0;

        if(!genre.exists()) return -1;

        try {
            FileWriter promptWriter = new FileWriter(genre, true);

            promptWriter.write("\n" + prompt);
            promptWriter.close();
            System.out.println("Prompt: <" + prompt + "> wrote");

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
    public ArrayList<String> deletePrompt(String unwantedPrompt, File genre) {
        ArrayList<String> deletedPrompts = new ArrayList<String>();

        if(!PROMPTS_FOLDER.exists()){
            PROMPTS_FOLDER.mkdir();
            return deletedPrompts;
        }
        if(!genre.exists()){
            return deletedPrompts;
        }

        try {

            List<String> lines = Files.readAllLines(genre.toPath());

            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(genre));

            //Since it's a fencepost problem as far as \n go
            if(!lines.get(0).equals(unwantedPrompt)) {
                buffWriter.write(lines.get(0));
            } else {
                deletedPrompts.add(lines.get(0));
            }
            lines.remove(0);

            //Looping through the file to rewrite it without the specified string
            for(String line : lines) {
                if(!line.equals(unwantedPrompt)) {

                    buffWriter.write("\n" + line);

                } else {
                    deletedPrompts.add(line);
                }
            }
            buffWriter.close();

        } catch (IOException e) {
            System.out.println("Problem deleting prompt");
            e.printStackTrace();
        }
        System.out.println("these " + deletedPrompts + " prompts have been deleted");
        return deletedPrompts;
    }



    /*
    * I have this next method because I wanted a low chance for it to give an extra prompt of "Surprise extra prompt! 日本語で書きます"
    * This extra prompt is to write it in japanese. I only did this because I know who is going to be using this
    * Since it's an extra prompt you will still get all your original prompts
    * That means you can just choose to ignore the japanese prompt if you so desire
    * the HashMap specifications is just how many prompts from which files.
    * Each file has a number attached to it which is the number to get from that file
    */
    public ArrayList<Prompt> getXPromptsJp(HashMap<File, Integer> specifications) {
        ArrayList<Prompt> randPrompts = getXRandomPrompts(specifications);
        Random random = new Random();

        if(random.nextInt(30) == 0) {
            randPrompts.add(new Prompt("Surprise extra prompt! 日本語で書きます", PROMPTS_FOLDER));
        }
        return randPrompts;
    }

}
