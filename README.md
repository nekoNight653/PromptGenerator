"#PromptGeneratorPackage.Prompts.Prompt generator"

This is version 1.1 of the prompt generator

This version allows you to separate prompts by genre.

You have complete control over what the genres are.

You can get random prompts either completely randomly (random genres too), 

or you can specify which genres and how many from said genre you want!

This is the branch for the upcoming version 1.2

Current version 1.2.0 (Actually I just realized this doesn't even count as an early beta so why am I versioning it?)

Changes:

    Reverted genreToDelete to a JTextField as I found it too easy to accidentally delete a genre with potentially hundreds of prompts in it

    Made prompt tuple a record rather than a class

    Created an image prompt tuple record thing

    Made class ImagePrompts that will be for handling the interaction with image prompts

    Organized my file layout!

    Seperated my TextPromptPnl and ImagePromptPnl into two different classes since they are large (Well ImagePromptPnl will be probably)

Planned changes:

    Capability to store and get random images

    So that it's a little better than a random number generator I'm also going to make it genre seperated for the images
