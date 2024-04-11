import java.io.File;

//This is just basically a tuple or pair or whatever for prompts
public class Prompt {

    private String thePrompt;
    private File originFile;
    Prompt(String thePrompt, File originFile) {
        this.thePrompt = thePrompt;
        this.originFile = originFile;
    }

    public String getPrompt() {
        return thePrompt;
    }
    public File getOriginFile() {
        return originFile;
    }
}
