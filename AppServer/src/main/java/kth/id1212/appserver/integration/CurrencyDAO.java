/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.integration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kth.id1212.appserver.model.Currency;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class CurrencyDAO {
    @PersistenceContext(unitName = "CurrencyPU")
    private EntityManager em;
    
    public Currency findCurrency(String name) throws Exception{
        Currency currency = em.find(Currency.class, name);
        if(currency == null){
            throw new Exception("Currency entity not found");
        }
        return currency;
    } 
    
    public void registerCurrency(Currency currency){
        if(!em.contains(currency)){
            em.persist(currency);
            em.flush();
        }
    }
}
