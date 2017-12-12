/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Jonas
 */
@Entity
public class Currency implements CurrencyDTO, Serializable{
    @Id 
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name="ABBR")
    private String abbr;
    @Column(name="RATE")
    private double rate;

    public Currency(){
    }
    
    public Currency(String name, String abbr, float rate){
        this.name = name;
        this.abbr = abbr;
        this.rate = rate;
    }
    
    @Override
    public String getName(){
        return name;
    }
    @Override
    public String getAbbr(){
        return abbr;
    }
    @Override
    public double getRate(){
        return rate;
    }
}
