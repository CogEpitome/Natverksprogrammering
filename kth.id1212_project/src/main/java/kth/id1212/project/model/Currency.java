/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Jonas
 */

@Entity
public class Currency implements CurrencyDTO, Serializable{
    @Id
    private String abbr;
    private String name;
    private float rate;
    
    public Currency(){
        
    }
    
    public Currency(String abbr, String name, float rate){
        this.abbr = abbr;
        this.name = name;
        this.rate = rate;
    }

    @Override
    public String getAbbr(){
        return abbr;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public float getRate(){
        return rate;
    }
    
    @Override
    public boolean equals(Object object){
        if(!(object instanceof Currency)){
            return false;
        }
        Currency other = (Currency) object;
        return this.abbr == other.abbr;
    }
    
    @Override
    public String toString(){
        return "model.Currency[name="+name+"]";
    }
    
}
