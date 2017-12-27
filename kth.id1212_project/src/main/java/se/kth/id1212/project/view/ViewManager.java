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
import kth.id1212.project.model.CurrencyDTO;
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
    private String gameStateMessage;
    private long id;
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
    
    public GameDTO getGame(){
        return game;
    }
    
    public String getGameStateMessage(){
        return gameStateMessage;
    }
    
    public void getId(){
        return;
    }
    
    public boolean getSuccess(){
        return conversionError == null;
    }
    
    public Exception getException(){
        return conversionError;
    }
    

    
    public void findGame(){
        try{
            startConversation();
            conversionError = null;
            game = controller.findGame(id);
        } catch (Exception e){
            handleException(e);
        }
    }
    
    public void createGame(){
        try{
            startConversation();
            conversionError = null;
            GameDTO game = controller.createGame();
        } catch(Exception e){
            handleException(e);
        }
    }
}
