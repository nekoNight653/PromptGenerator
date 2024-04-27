package PromptGeneratorPackage.Prompts;

import java.awt.image.BufferedImage;
import java.io.File;

//This is just a tuple or pair or whatever for image prompts.
//It contains two Files one supposed to be an image file the other the directory it's stored in
public record ImagePrompt(BufferedImage bufferedImage, File directory) {
}
