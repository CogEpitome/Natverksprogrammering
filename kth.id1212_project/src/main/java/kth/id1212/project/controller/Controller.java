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
import kth.id1212.project.integration.CurrencyDAO;
import kth.id1212.project.model.Converter;
import kth.id1212.project.model.Currency;
import kth.id1212.project.model.CurrencyDTO;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB CurrencyDAO currencyDB;
    @EJB Converter converter;
    
    public CurrencyDTO createCurrency(String abbr, String name, float rate){
        Currency currency = new Currency(abbr, name, rate);
        currencyDB.persistCurrency(currency);
        return currency;
    }
    
    public CurrencyDTO findCurrency(String abbr){
        return currencyDB.findCurrency(abbr);
    }
    
    public float convert(CurrencyDTO currencyFrom, CurrencyDTO currencyTo, float amount){
        float value = converter.convert(amount, currencyFrom.getRate(), currencyTo.getRate());
        return value;
    }
}
