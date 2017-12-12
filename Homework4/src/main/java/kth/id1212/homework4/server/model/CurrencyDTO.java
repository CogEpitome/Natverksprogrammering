/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.homework4.server.model;

import java.io.Serializable;

/**
 *
 * @author Jonas
 */
public class CurrencyDTO implements Serializable{
    private final String name;
    private final String abbr;
    private final float rate;
    
    public CurrencyDTO(String name, String abbr, float rate){
        this.name = name;
        this.abbr = abbr;
        this.rate = rate;
    }
    
    public String getName(){
        return name;
    }
    
    public String getAbbr(){
        return abbr;
    }
    
    public float getRate(){
        return rate;
    }
}
