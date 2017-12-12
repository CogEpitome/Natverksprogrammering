/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.inject.Inject;
import kth.id1212.appserver.controller.Controller;
import kth.id1212.appserver.model.CurrencyDTO;
import kth.id1212.appserver.controller.Controller;
import kth.id1212.appserver.model.Currency;

/**
 *
 * @author Jonas
 */
@Named("viewManager")
@ConversationScoped
public class ViewManager implements Serializable {
    
    @EJB
    private final Controller controller = new Controller();
    private CurrencyDTO currencyFrom;
    private CurrencyDTO currencyTo;
    private String currencyFromId, currencyToId;
    private Exception conversionError;
    private String searchedCurrencyFrom, searchedCurrencyTo;
    @Inject
    private Conversation conversation;
    
    public void initDatabase(){
        startConversation();
        controller.initDatabase();
    }
    
    private void startConversation(){
        if(conversation.isTransient()){
            conversation.begin();
        }
    }
    
    private void stopConversation(){
        if(!conversation.isTransient()){
            conversation.end();
        }
    }

    private void handleException(Exception e){
        stopConversation();
        e.printStackTrace(System.err);
        conversionError = e;
    }
    
    public boolean getSuccess(){
        return conversionError == null;
    }
    
    public Exception getException(){
        return conversionError;
    }
    
    public void findCurrency(){
        try{
            startConversation();
            conversionError = null;
            currencyFrom = controller.findCurrency(currencyFromId);
            currencyTo = controller.findCurrency(currencyToId);
        } catch (Exception e){
            handleException(e);
        }
    }
    
    public void setSearchedCurrencyFrom(String searchedCurrency){
        this.searchedCurrencyFrom = searchedCurrency;
    }
    public String getSearchedCurrencyFrom(){
        return null;
    }
    public void setSearchedCurrencyTo(String searchedCurrency){
        this.searchedCurrencyTo = searchedCurrency;
    }
    public String getSearchedCurrencyTo(){
        return null;
    }
    
    public void setCurrencyFromId(String currencyFromId){
        this.currencyFromId = currencyFromId;
    }
    //For JSF compaitibility
    public Integer getCurrencyFromId(){
        return null;
    }
    
    public void setCurrencyToId(String currencyToId){
        this.currencyToId = currencyToId;
    }
    //For JSF compaitibility
    public Integer getCurrencyToId(){
        return null;
    }

    public CurrencyDTO getCurrencyFrom(){
        return currencyFrom;
    }

    public CurrencyDTO getCurrencyTo(){
        return currencyTo;
    }
    
    public void convert(){
        
    }
}
