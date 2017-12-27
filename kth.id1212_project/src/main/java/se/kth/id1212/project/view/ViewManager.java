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
    private float currencyFromAmount;
    private float currencyToValue;
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
    
    public void setCurrencyFromAmount(float currencyFromAmount){
        this.currencyFromAmount = currencyFromAmount;
    }
    public float getCurrencyFromAmount(){
        return currencyFromAmount;
    }
    public void setCurrencyToValue(float currencyToValue){
        this.currencyToValue = currencyToValue;
    }
    public float getCurrencyToValue(){
        return currencyToValue;
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
    public void convert(){
        currencyFrom = controller.findCurrency(currencyFromAbbr);
        currencyTo = controller.findCurrency(currencyToAbbr);
        currencyToValue = controller.convert(currencyFrom, currencyTo, currencyFromAmount);
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
