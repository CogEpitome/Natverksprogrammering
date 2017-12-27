/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.model;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Jonas
 */
@Entity
public class Game implements GameDTO, Serializable{
    
    @Id
    private String player;
    private char[] word;
    private char[] guessed;
    private int tries;
    private int score;
    
    public Game(){
    }
    
    public Game(String player, char[] word){
        this.player = player;
        setWord(word);
        this.score = 0;
    }
    
    public Game(GameDTO game){
        this.player = game.getPlayer();
        this.word = game.getWord();
        this.guessed = game.getGuessed();
        this.tries = game.getTries();
        this.score = game.getScore();
    }
    
    public void setWord(char[] newWord){
        this.word = newWord;
        this.guessed = new char[newWord.length];
        for(int i = 0; i < this.guessed.length; i++){
            this.guessed[i] = '-';
        }
        this.tries = newWord.length;
    }
    
    public String guess(String guessString){
        boolean correct = false;
        String message = "";
        char[] guess = guessString.toCharArray();
        
        if(guess.length > 1){
            correct = evaluate(guess);
        } else {
            correct = evaluate(guess[0]);
        }
        
        if(!correct){
            message = Messages.INCORRECT_MESSAGE;
            tries--;
        }
        
        if(tries <= 0){
            message = Messages.LOSE_MESSAGE;
            score--;
        }
        
        if(correct){
            if(Arrays.equals(guessed, word)){
                message = Messages.WIN_MESSAGE;
                score++;
            } else {
                message = Messages.CORRECT_MESSAGE;
            }
        }
        
        return message;
    }
    
    private boolean evaluate(char c){
        boolean found = false;
        for(int i = 0; i < word.length; i++){
            if(word[i] == c){
                guessed[i] = c;
                found = true;
            }
        }
        return found;
    }
    private boolean evaluate(char[] ca){
        if(Arrays.equals(ca, word)){
            guessed = word;
            return true;
        } else
        return false;
    }

    @Override
    public String getPlayer(){
        return player;
    }
    
    @Override
    public char[] getWord(){
        return word;
    }
    
    @Override
    public char[] getGuessed(){
        return guessed;
    }
    
    @Override
    public int getTries(){
     return tries;   
    }
    
    @Override
    public int getScore(){
        return score;
    }
    
    @Override
    public String getState(){
        String stateString = String.valueOf(guessed) + " | " + tries + " remaining | Score: " + score;
        return stateString;
    }
    
    @Override
    public boolean equals(Object object){
        if(!(object instanceof Game)){
            return false;
        }
        Game other = (Game) object;
        return this.player == other.player;
    }    
}
