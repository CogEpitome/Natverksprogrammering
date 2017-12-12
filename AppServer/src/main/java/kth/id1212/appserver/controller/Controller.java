/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.controller;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import kth.id1212.appserver.integration.CurrencyDAO;
import kth.id1212.appserver.model.Currency;
import kth.id1212.appserver.model.CurrencyDTO;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller{
    @EJB CurrencyDAO currencyDB;
    
    public void initDatabase(){
        Currency currency1 = new Currency("Kronor","SEK",1);
        Currency currency2 = new Currency("US Dollar","USD",8.7f);
        Currency currency3 = new Currency("Euro","EUR",10);
        
        currencyDB.registerCurrency(currency1);
        currencyDB.registerCurrency(currency2);
        currencyDB.registerCurrency(currency3);
    }
    
    public Currency findCurrency(String name) throws Exception{
        return currencyDB.findCurrency(name);
    }
}
