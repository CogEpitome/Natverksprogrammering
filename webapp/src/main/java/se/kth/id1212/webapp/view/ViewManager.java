/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.webapp.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import kth.id1212.webapp.controller.Controller;
import kth.id1212.webapp.model.Currency;
import kth.id1212.webapp.model.CurrencyDTO;

/**
 *
 * @author Jonas
 */
@Named("viewManager")
@ConversationScoped
public class ViewManager implements Serializable{
    @EJB
    private Controller controller;
    private CurrencyDTO currencyFrom;
    private CurrencyDTO currencyTo;
    private String currencyFromAbbr;
    private String currencyToAbbr;
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
    
    public CurrencyDTO getCurrencyFrom(){
        return currencyFrom;
    }
    public CurrencyDTO getCurrencyTo(){
        return currencyTo;
    }
    
    public boolean getSuccess(){
        return conversionError == null;
    }
    
    public Exception getException(){
        return conversionError;
    }
    
    public void setCurrencyFromAbbr(String currencyFromAbbr){
        this.currencyFromAbbr = currencyFromAbbr;
    }
    public String getCurrencyFromAbbr(){
        return null;
    }
    public void setCurrencyToAbbr(String currencyToAbbr){
        this.currencyToAbbr = currencyToAbbr;
    }
    public String getCurrencyToAbbr(){
        return null;
    }
    
    private void readCurrencyFrom(){
        currencyFromAbbr = currencyFrom.getAbbr();
        findCurrencyFrom();
    }
    private void readCurrencyTo(){
        currencyToAbbr = currencyTo.getAbbr();
        findCurrencyTo();
    }
    
    public void findCurrencyFrom(){
        try{
            startConversation();
            conversionError = null;
            currencyFrom = controller.findCurrency(currencyFromAbbr);
        } catch (Exception e){
            handleException(e);
        }
    }
    public void findCurrencyTo(){
        try{
            startConversation();
            conversionError = null;
            currencyTo = controller.findCurrency(currencyToAbbr);
        } catch (Exception e){
            handleException(e);
        }
    }
    
    public void initCurrencies(){
        try{
            startConversation();
            conversionError = null;
            CurrencyDTO currency1 = controller.createCurrency("SEK", "Kronor", 1);
            CurrencyDTO currency2 = controller.createCurrency("EUR", "Euro", 10);
            CurrencyDTO currency3 = controller.createCurrency("USD", "Dollar", 8.7f);
            CurrencyDTO currency4 = controller.createCurrency("GBP", "Pound", 9);
            CurrencyDTO currency5 = controller.createCurrency("INR", "Rupee", 0.1f);
        } catch(Exception e){
            handleException(e);
        }
    }
}
