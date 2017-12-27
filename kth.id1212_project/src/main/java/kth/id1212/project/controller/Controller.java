/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import kth.id1212.project.integration.FileHandler;
import kth.id1212.project.integration.GameDAO;
import kth.id1212.project.model.Game;
import kth.id1212.project.model.GameDTO;
import kth.id1212.project.model.Messages;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB GameDAO projectDB;
    @EJB FileHandler fileHandler;
    
    public GameDTO startGame(String player){
        GameDTO foundGame = findGame(player);
        if(foundGame == null){
            return createGame(player);
        }
        return foundGame;
    }
    
    public GameDTO findGame(String player){
        return projectDB.findGame(player);
    }
    
    public GameDTO createGame(String player){
        char[] word = fileHandler.getWord();
        Game game = new Game(player, word);
        projectDB.persistGame(game);
        return game;
    }
    
    public String guess(String player, String guess){
        Game game = projectDB.findGame(player);
        String message = game.guess(guess);
        if(message.equals(Messages.WIN_MESSAGE) || message.equals((Messages.LOSE_MESSAGE))){
            char[] word = fileHandler.getWord();
            game.setWord(word);
        }
        return message;
    }
}
