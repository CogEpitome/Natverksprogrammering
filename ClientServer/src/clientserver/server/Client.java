/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserver.server;

import java.net.Socket;

/**
 *
 * @author Jonas
 */
public class Client {
    
    protected Session session;
    protected Socket socket;
    protected FileHandler fileHandler;
    
    //Session keeps track of a game session's variables
    protected class Session
    {   
        protected char[] word;
        protected char[] guessed;
        protected int tries;
        protected int score;
    }
    
    public Client(){
        this.session = new Session();
        this.fileHandler = new FileHandler();
        this.newWord();
    }
    
    //Updates the session with a new word from the FileHandler class
    protected void newWord()
    {
        char[] word = this.fileHandler.getWord();
        setWord(word, this.session);
        this.session.tries = this.session.word.length;
    }
    
    //Sets the session's word and resets the session's guessed array
    private void setWord(char[] word, Session session)
    {
        session.word = word;
        session.guessed = new char[word.length];
        for(int i = 0; i < session.guessed.length; i++) { session.guessed[i] = '_'; }
    }
    
}
