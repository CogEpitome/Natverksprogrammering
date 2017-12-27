/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.model;

import java.io.Serializable;
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
        this.word = word;
        this.guessed = new char[word.length];
        this.tries = word.length;
        this.score = 0;
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
