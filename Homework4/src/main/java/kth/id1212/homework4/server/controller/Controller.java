/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.homework4.server.controller;

import java.io.Serializable;
import kth.id1212.homework4.server.model.CurrencyDTO;

/**
 *
 * @author Jonas
 */
public class Controller implements Serializable{
    
    public CurrencyDTO findCurrencyFrom(String currencyFromId){
        return new CurrencyDTO("Kronor", "SEK", 1.0f);
    }
    
    public CurrencyDTO findCurrencyTo(String currencyToId){
        return new CurrencyDTO("United States Dollar", "USD", 10.0f);
    }
}
