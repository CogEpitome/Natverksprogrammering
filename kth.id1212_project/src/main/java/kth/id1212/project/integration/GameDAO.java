/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.integration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kth.id1212.project.model.Game;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class GameDAO {
    @PersistenceContext(unitName = "projectPU")
    private EntityManager em;
    
    public Game findGame(String player){
        Game game = em.find(Game.class, player);
        if(game == null){
            return null;
        }
        return game;
    }
    
    public void persistGame(Game game){
        em.persist(game);
    }
}
