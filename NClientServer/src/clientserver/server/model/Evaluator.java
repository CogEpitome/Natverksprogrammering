package clientserver.server.model;

import java.util.Arrays;

/**
 *
 * @author Jonas Iacobi
 * 
 * Used to evaluate the user's guess and update the guessed array accordingly.
 */
public class Evaluator {
    
    //Constructor
    public Evaluator(){};
    
    //Compares the guess string to the word chosen by the server for the session
    public void evaluate(String guess, Client.Session session)
    {
        char[] guessArray = guess.toLowerCase().toCharArray();
        if(guessArray.length == 1)  evaluateGuess(guessArray[0], session);
        else                        evaluateGuess(guessArray, session);
    }
    
    //Determines if the guess matches the entire word
    private void evaluateGuess(char[] word, Client.Session session)
    {
        for(int i = 0; i < word.length; i++)
        {
            if(session.word[i] != word[i])
            {
                session.tries--;
                return;
            }
        }
        session.guessed = word;
    }
    
    //Determines if the guessed character exists in the word
    private void evaluateGuess(char c, Client.Session session)
    {
        boolean found = false;
        for(int i = 0; i < session.word.length; i++)
        {
            if(session.word[i] == c)
            {
                session.guessed[i] = c;
                found = true;
            }
        }
        
        if(!found){ session.tries--; }
    }
    
}
