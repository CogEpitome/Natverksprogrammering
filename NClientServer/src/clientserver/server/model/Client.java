package clientserver.server.model;

import clientserver.server.filehandler.FileHandler;

/**
 * The class representing a client in the server. Each Client object has information on one player's session.
 * @author Jonas
 */
public final class Client {
    
    public Session session;
    public FileHandler fileHandler;
    
    //Session keeps track of a game session's variables
    public class Session
    {   
        public char[] word;
        public char[] guessed;
        public int tries;
        public int score;
    }
    
    //Constructor
    public Client(){
        this.session = new Session();
        this.fileHandler = new FileHandler();
        this.newWord();
    }
    
    //Updates the session with a new word from the FileHandler class
    public void newWord()
    {
        char[] word = this.fileHandler.getWord();
        setWord(word, this.session);
        this.session.tries = this.session.word.length;
    }
    
    //Sets the session's word and resets the session's guessed array
    private void setWord(char[] word, Session session)
    {
        session.word = word;
        for(char w : word) {w = Character.toLowerCase(w);}
        session.guessed = new char[word.length];
        for(int i = 0; i < session.guessed.length; i++) { session.guessed[i] = '_'; }
    }
    
}
