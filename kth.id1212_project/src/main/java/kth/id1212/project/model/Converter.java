/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.model;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Jonas
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class Converter {
    public float convert(float amount, float rateFrom, float rateTo){
        return amount*(rateFrom/rateTo);
    }
}
