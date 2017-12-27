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
import kth.id1212.project.integration.GameDAO;
import kth.id1212.project.model.Game;
import kth.id1212.project.model.GameDTO;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB GameDAO projectDB;
    
    public GameDTO createGame(){
        Game game = new Game();
        projectDB.persistGame(game);
        return game;
    }
    
    public GameDTO findGame(long id){
        return projectDB.findGame(id);
    }
}
