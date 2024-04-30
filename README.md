Prompt generator

This is version 1.2 of the prompt generator

This version allows you to separate prompts by genre.

You have complete control over what the genres are.

You can get random prompts either completely randomly (random genres too), 

or you can specify which genres and how many from said genre you want!

This is the branch for the upcoming version 1.2

Current version 1.2.0 (It has now released!...
Kind of not like I've actually released more that it's in a state where I could release it might still be a few bugs though)

Changes:

    Made a fully working version of the ImagePromptsPnl you can now:
    
    Make genres, delete genres, get genres, add prompts, delete prompts, get prompts, get random prompts, paramaterized random prompts, clear input, and nothing else

    I increased parity or compatibility or whatever a bit of the TextPrompts and IamgePrompts class via the PromptManager interface

    I finished the PromptPnl abstract class that handels the prompt panels

    I made TextPromptPnl extend PromptPnl (signifgantly less code now)

    I made the Prompt record back into a class because I needed it to be able to be able to set prompts after they were already set-

     so that I could set any missing prompts to the path for the promptNotFound picture in the ImagePrompts class

Planned changes:

    I might even make a way to get random prompts from both of the prompt types

    I may also make sound prompts (Sounds interesting)
