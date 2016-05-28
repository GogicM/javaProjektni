/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.scene.image.Image;

/**
 *
 * @author Milan
 */
public class KnjigaInfo {
    private String imagePath;
    private String bookInfo;
    
    public KnjigaInfo(String imagePath, String bookInfo) {
        this.imagePath = imagePath;
        this.bookInfo = bookInfo;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setBookInfo(String bookInfo) {
        this.bookInfo = bookInfo;
    }
    public String getBookInfo() {
        return bookInfo;
    }
}
