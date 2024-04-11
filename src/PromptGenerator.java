
public class PromptGenerator {

    //I really don't use my main class part of me thinks I should move the main method to the GUI class
    public static void main(String[] args) {

        GUI gui = new GUI();
        gui.gui();

    }

    /*
    Create genre,	“genre to add”
    Delete genre, “genre to delete”
    Add prompt, “prompt to add”, A radio box for genre
    Delete prompt, “prompt to delete”, A radio box for genre

    This one is a bit weird. How should I implement it with multiple genres to account for?
    1.Get X random prompts, “Number of prompts to get”, “A checklist for which genres.”
    Getting x prompts from each genre

    2.Get X random prompts, “Number of prompts to get”, “A checklist for which genres.”
    Getting x prompts in total. But how would I decide how many from each genre?

    Random chance? Imagine you get like 5 themes and nothing else?
    I guess you could just reroll lol

    Even distribution? What happens if you don’t have enough prompts requested for the lists? Does it error out? Just not get a prompt from each list? Get more prompts?

    3.Get X random prompts, A checklist with a number next to each one for how many from each?
    This a bit hard sounding not sure how I’d do it
    Though honestly probably the best



    Get all prompts, A checklist for which genres you want
    Then output for each prompt like this?
    “Genre: Genre prompt num: Prompt”,

    For example

    This is better
    “Genre”
    “Prompt num: Prompt”

    For example

    Themes:
    1: Fantasy
    2: Sci-fi
    3: Romance

    Characters:
    1: Sonic
    2: Mario
    3: Link

    Finally on the last line
    Get all genres,		Clear output
     */



}