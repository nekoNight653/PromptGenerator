package PromptGeneratorPackage.Prompts;

import java.io.File;

//This is just basically a tuple or pair or whatever for prompts
public record Prompt(String prompt, File genre, PromptType type) {

}
