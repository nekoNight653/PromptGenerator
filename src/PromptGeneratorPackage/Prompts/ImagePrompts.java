package PromptGeneratorPackage.Prompts;

import PromptGeneratorPackage.PromptGenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

public class ImagePrompts {

    public static final File ImagePromptFolder = new File(PromptGenerator.PROMPTS_FOLDER, "Image_prompts");


    /*
    * Takes a string that determines the name of the genre file (Tiny)
    *
    * returns false if it failed
    * true if it succeeded
    */
    public boolean createGenre(String name) {
        //We don't check for if PROMPTS_FOLDER exists since it should make it if it doesn't
        return (new File(ImagePromptFolder, name)).mkdir();
    }


    //Just takes a name of the file to be deleted
    //That's because that's the only way I'll need it
    public boolean deleteGenre(String genre) {
        if(!ImagePromptFolder.exists()) return ImagePromptFolder.mkdir();

        File genreToDelete = new File(ImagePromptFolder, genre + ".txt");
        return genreToDelete.delete();
    }


    public ArrayList<File> getGenres() {
        ArrayList<File> genres = new ArrayList<File>();
        if(!ImagePromptFolder.exists()) {
            ImagePromptFolder.mkdir();
            return genres;
        }
        Collections.addAll(genres, ImagePromptFolder.listFiles());
        return genres;
    }


    /*
    * All this does is return the names of the genre files
    * This is used for the combo boxes in imagePromptPnl
    */
    public ArrayList<String> getGenreNames() {
        ArrayList<String> genreNames = new ArrayList<String>();

        for (File genre: getGenres()) {
            genreNames.add(genre.getName());
        }
        return genreNames;
    }


//    public ArrayList<ImagePrompt> getPrompts(File genre) {
//        ArrayList<ImagePrompt> prompts = new ArrayList<ImagePrompt>();
//
//        if (!genre.exists()) {
//            return prompts;
//        }
//
//        for(File prompt : genre.listFiles()) {
//            try {
//                prompts.add(new ImagePrompt(ImageIO.read(prompt), genre));
//            } catch (IOException e) {
//                System.out.println("Couldn't convert file to image");
//                e.printStackTrace();
//            }
//
//        }
//
//        return prompts;
//    }
}
