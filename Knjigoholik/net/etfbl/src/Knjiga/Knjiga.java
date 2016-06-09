    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Knjiga;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 *
 * @author Milan
 */
public class Knjiga implements Serializable {
    
   // private StringProperty bookName;
    //ubacujem boolean zbog pravljenja checkbox a
    private boolean checked;
    private boolean isItAvailable;
    private String bookName;
    private String isbn;
    private String authorName;
    private int numberOfPages;
    private String summary;
    private String publisher;
    private String mainPage;
    private double cost;
    private int quantity;
    private String genre;

    public Knjiga(boolean checked, boolean isItAvailable, String bookName, String isbn, String authorName, int numberOfPages,
                  String summary, String publisher, String mainPage, double cost,
                  int quantity, String genre) {
     //   this.bookName = new SimpleStringProperty(bookName);
        this.isItAvailable = isItAvailable;
        this.checked = checked;
        this.bookName = bookName;
        this.isbn = isbn;
        this.authorName = authorName;
        this.numberOfPages = numberOfPages;
        this.summary = summary;
        this.publisher = publisher;
        this.mainPage = mainPage;
        this.cost = cost;
        this.quantity = quantity;
        this.genre = genre;
    }
//    public String getBookName() {
//        return bookName.get();
//    }
//
//    public void setBookName(String bookName) {
//        this.bookName.set(bookName);
//    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getSummary() {
        return summary;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getPublisher() {
        return publisher;
    }


    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    public String getMainPage() {
        return mainPage;
    }


    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }   
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public boolean getChecked() {
        return checked;
    }
    
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public boolean getIsItAvailable() {
        return isItAvailable;
    }
    
    public void setIsItAvailable(boolean isItAvailable) {
        this.isItAvailable = isItAvailable;
    }
    
    public String toString() {
        return "AUTOR: " + getAuthorName() + "\nIZDAVAC:" + getPublisher() +
                "\nNAZIV:" + getBookName() + "\nCIJENA: " + getCost();
    }
    //pomocna funkcija koja vrsi dodavanje teksta iz fajla u string
    // koristimo je u slucaju opisa kog citamo iz tekstualnog fajla
    public static String fromFileToString(File file)
    throws IOException {
      int len;
      char[] chr = new char[4096];
      final StringBuffer buffer = new StringBuffer();
      final FileReader reader = new FileReader(file);
      try {
          while ((len = reader.read(chr)) > 0) {
              buffer.append(chr, 0, len);
          }
      } finally {
          reader.close();
      }
      return buffer.toString();
    } 
}
