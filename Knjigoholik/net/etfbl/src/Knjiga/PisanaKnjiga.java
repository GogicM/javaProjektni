/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Knjiga;

import javafx.scene.image.Image;

/**
 *
 * @author Milan
 */
public class PisanaKnjiga extends Knjiga {
    
    public PisanaKnjiga(boolean checked,boolean isItAvailable, String bookName, String isbn, String authorName, int numberOfPages,
            String summary, String publisher, String mainPage, double cost, 
            int quantity, String genre) {
           
        super(checked,isItAvailable, bookName, isbn, authorName, numberOfPages, summary, publisher, mainPage, cost, quantity, genre);
    }   
}
