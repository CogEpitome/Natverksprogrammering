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
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import kth.id1212.project.model.Currency;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class CurrencyDAO {
    @PersistenceContext(unitName = "projectPU")
    private EntityManager em;
    
    public Currency findCurrency(String abbr){
        Currency currency = em.find(Currency.class, abbr);
        if(currency == null){
            throw new EntityNotFoundException("No currency abbreviated to "+abbr+" found in database");
        }
        return currency;
    }
    
    public void persistCurrency(Currency curr){
        em.persist(curr);
    }
}
