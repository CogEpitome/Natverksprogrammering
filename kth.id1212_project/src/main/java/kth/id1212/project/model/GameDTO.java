/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.project.model;

/**
 *
 * @author Jonas
 */
public interface GameDTO {
    public String getPlayer();
    public char[] getWord();
    public char[] getGuessed();
    public int getTries();
    public int getScore();
    public String getState();
}
