/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserver.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jonas Iacobi
 * 
 * This class is used to retrieve the lines of a text document for use in the hangman game.
 * Stores the lines in memory, so not to be used with very large text files.
 */
public class FileHandler {
    
    private static final Path PATH = Paths.get("resources/words.txt");
    private List<String> lines;
    
    //Constructor. The class reads in the lines of the specified file once on creation.
    protected FileHandler()
    {
        try{readWords();}
        catch(IOException ioe){throw new RuntimeException("Could read words from file");}
    }
        
    //Returns a random word stored in memory
    protected char[] getWord()
    {
        Random rand = new Random();
        return lines.get(rand.nextInt(lines.size()-1)).toCharArray();
    }
    
    //Reads all lines from the specified Path into memory
    //Requires the List<String> lines
    private void readWords() throws IOException 
    {
        try
        {
            lines = Files.readAllLines(PATH);
        } 
        catch(IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }
        
    
}
