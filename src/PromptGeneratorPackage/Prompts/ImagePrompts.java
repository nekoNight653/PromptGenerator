package PromptGeneratorPackage.Prompts;

import PromptGeneratorPackage.PromptGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;


/*
 * Like the class textPrompts is for managing string based prompts this is for managing image based prompts.
 *
 * It really seems like I should be able to have some parent class for both of them which has some base features,
 * but after thinking for a little while I couldn't find much without going too far out of my way for it.
 *
 * Like perhaps, I could declare what methods you'd need (for a fully working prompt manager. Such as creating, getting and deleting genres and prompts)
 * and pass in generics, but it didn't seem worth it since the code has to work slightly differently for each of them anyways
 * and I won't be using those methods elsewhere in the class.
 *
 * They still seem so close
 */

public class ImagePrompts implements  PromptManager {

    public static final File IMAGE_PROMPTS_FOLDER = new File(PromptGenerator.PROMPTS_FOLDER, "Image_prompts");


    //returns false if any of the folders didn't exist
    //returns true if they all did
    public boolean genFolders() {

        try {
            if(!PromptGenerator.PROMPTS_FOLDER.exists()) {
                PromptGenerator.PROMPTS_FOLDER.mkdir();
                IMAGE_PROMPTS_FOLDER.mkdir();
                return false;
            }
            if(!IMAGE_PROMPTS_FOLDER.exists()) {
                IMAGE_PROMPTS_FOLDER.mkdir();
                return false;
            }
        } catch (SecurityException e) {
            System.out.println("Failed to generate folders");
            e.printStackTrace();
            return false;
        }


        return true;
    }

    @Override
    public ArrayList<File> getGenres() {
        ArrayList<File> genres = new ArrayList<>();

        if(!genFolders()) return genres;

        Collections.addAll(genres, IMAGE_PROMPTS_FOLDER.listFiles());
        return genres;
    }
    @Override
    public File getGenreFile(String name) {
        return new File(IMAGE_PROMPTS_FOLDER, name);
    }
    /*
    * Takes a string that determines the name of the genre file
    *
    * returns -1 if it failed for an unexpected reason
    * 0 if the genre folder already exists
    * 1 if it created the genre
    */
    @Override
    public int createGenre(String name) {
        //We don't check for if PROMPTS_FOLDER exists since it should make it if it doesn't
        genFolders();
        try {
            if((new File(IMAGE_PROMPTS_FOLDER, name)).mkdir()) {
                return 1;
            }
            return  0;
        } catch (SecurityException e) {
            e.printStackTrace();
            return  -1;
        }

    }


    //Just takes a name of the file to be deleted
    //Deletes everything within the file to do this of course
    @Override
    public boolean deleteGenre(String genre) {

        if(!genFolders()) return false;
        File genreToDelete = new File(IMAGE_PROMPTS_FOLDER, genre);

        ArrayList<File> filesToDelete = new ArrayList<>();
        Collections.addAll(filesToDelete, genreToDelete.listFiles());
        while (!filesToDelete.isEmpty()) {

            File nextFile = filesToDelete.get((filesToDelete.size()-1));

            //We go from the end that way it will not infinitely loop on directories
            if (nextFile.isDirectory()) {
                if(nextFile.listFiles().length == 0) {
                    nextFile.delete();
                    filesToDelete.remove(filesToDelete.get((filesToDelete.size()) -1));
                } else {
                    Collections.addAll(filesToDelete, nextFile.listFiles());
                }
            }
            else {
                filesToDelete.get(0).delete();
                filesToDelete.remove(0);
            }

        }
        return genreToDelete.delete();
    }

    @Override
    public ArrayList<Prompt> getPrompts(File genre) {
        ArrayList<Prompt> prompts = new ArrayList<Prompt>();



        if (!genFolders() || !genre.exists()) {
            return prompts;
        }

        for(File prompt : genre.listFiles()) {
            prompts.add(new Prompt(prompt.getPath(), genre, PromptType.IMAGE));
        }

        return prompts;
    }
    //This creates the prompt missing png if it doesn't exist
    //
    //If it fails to make the picture it returns. This shouldn't happen normally, but it can technically return null
    public File promptMissingPNG() {

        File stuff = new File(PromptGenerator.PROMPTS_FOLDER, "stuff");

        //We technically don't need one of the folders it generates for this, but we'll generate that folder very soon anyway I'm sure
        genFolders();
        if(!stuff.exists()) stuff.mkdir();

        File promptMissing = new File(stuff, "NoMorePrompts.png");

        if(promptMissing.exists()) return promptMissing;

        else {
            BufferedImage promptMissingPng = new BufferedImage(100, 13, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D graphics2D = promptMissingPng.createGraphics();
            graphics2D.drawString("Out of images!", 10F, 10F);
            graphics2D.dispose();

            try {
                ImageIO.write(promptMissingPng, "png", promptMissing);
            } catch (IOException e) {
                System.out.println("Failed to create missing prompt image");
                e.printStackTrace();
                return null;
            }

        }

        return  promptMissing;
    }
    @Override
    public String promptNotFound(File path) {
        return promptMissingPNG().getPath();
    }

    /*
     * Copies a picture into a genre folder
     * It also creates the prompt folder if it doesn't exist
     * It returns a 1 if it went successfully
     * A 0 if it had incorrect input (failed to find the input file)
     * A -1 if the requested genre file doesn't exist
     * A -2 if an unexpected error occurred
     * And a -3 if the file already exists
     *
     * If you're wondering why I didn't put -2 for file already exists and -3 for unexpected error.
     * It's because the other add prompt function for text prompts returns negative two for unexpected failure already so why break that?
     */
    @Override
    public int addPrompt(String prompt, File genre) {

        if(!genFolders() || !genre.exists()) return -1;

        File fileToCopy = new File(prompt);
        if(!fileToCopy.exists()) return  0;

        File copiedFile = new File(genre, fileToCopy.getName());
        if(copiedFile.exists()) return -3;

        try {
            Files.copy(fileToCopy.toPath(), copiedFile.toPath());
        } catch (IOException i) {
            System.out.println("Problem copying file");
            i.printStackTrace();
            return -2;
        }

        return 1;
    }

    //The only reason this is an arraylist result is that was required for the previous one so that's what it returns
    //And I think it might be fun to have some panel that randomly switched between text and image prompts in the future

    //This method returns a string (in an array) detailing what went wrong or right
    //
    //I would use a char for successFullyDeleted but .contains requires a char sequence so
    public static final String successFullyDeleted = "!";
    @Override
    public ArrayList<String> deletePrompt(String unwantedPrompt, File genre) {
        ArrayList<String> results = new ArrayList<>();

        if (!genFolders() || !genre.exists()) {
            results.add("Genre \"" + genre.getName() + "\" doesn't exist");
            return  results;
        }

        File promptToDelete = new File(genre, unwantedPrompt);
        if(promptToDelete.delete()) {
            results.add("Prompt \"" + unwantedPrompt + "\" deleted" + successFullyDeleted);
            return results;
        }
        results.add("\nFailed to delete prompt \"" + unwantedPrompt + "\"" +
                "\nDo note unlike add image prompt this method takes just the name(including the .png or .jpg etc) not the full file path\n");
        return results;
    }


}
