/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.project.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import kth.id1212.project.controller.Controller;
import kth.id1212.project.model.GameDTO;

/**
 *
 * @author Jonas
 */
@Named("viewManager")
@ConversationScoped
public class ViewManager implements Serializable{
    @EJB
    private Controller controller;
    private GameDTO game;
    private String gameStateMessage, guessMessage, player, guess;
    private Exception conversionError;
    @Inject
    private Conversation conversation;
    
    private void startConversation(){
        if(conversation.isTransient()){
            conversation.begin();
        }
    }
    
    private void stopConversation(){
        if(conversation.isTransient()){
            conversation.end();
        }
    }
    
    private void handleException(Exception e){
        stopConversation();
        e.printStackTrace(System.err);
        conversionError = e;
    }
    
    public void setGame(){
        return;
    }
    public GameDTO getGame(){
        return game;
    }
    
    public void setGameStateMessage(String msg){
        return;
    }
    public String getGameStateMessage(){
        return gameStateMessage;
    }
    
    public void setGuessMessage(String msg){
        return;
    }
    public String getGuessMessage(){
        return guessMessage;
    }
    
    public void setGuess(String guess){
        this.guess = guess;
    }
    public String getGuess(){
        return guess;
    }
    
    public void setPlayer(String player){
        this.player = player;
    }
    public String getPlayer(){
        return player;
    }
    
    public boolean getSuccess(){
        return conversionError == null;
    }
    
    public Exception getException(){
        return conversionError;
    }
    
    public void startGame(){
        try{
            startConversation();
            conversionError = null;
            game = controller.startGame(player);
            refreshGame(game.getPlayer());
        } catch(Exception e){
            handleException(e);
        }
    }
    
    public void guess(){
        try{
            conversionError = null;
            guessMessage = controller.guess(game.getPlayer(), guess);
            refreshGame(game.getPlayer());
        } catch(Exception e){
            handleException(e);
        }
    }
    
    private void refreshGame(String name){
        GameDTO foundGame = controller.findGame(name);
        this.gameStateMessage = foundGame.getState();
    }
}
