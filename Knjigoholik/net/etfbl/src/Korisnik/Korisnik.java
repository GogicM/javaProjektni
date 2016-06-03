/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Korisnik;

/**
 *
 * @author Milan
 * @brief Klasa koja predstavlja korisnika, sa atributima imenom, korisnickim imenom, stanjem na racunu i valuti u kojoj placa
 */
public class Korisnik {
    
    private String name;
    private double accountBalance;
    private String currency;
    private String username;
    private String password;

    public Korisnik(String name, double accountBalance, String currency, String username , String password) {
        this.name = name;
        this.accountBalance = accountBalance;
        this.currency = currency;
        this.username = username;    
        this.password = password;
    }

    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getAccountBalance() {
        return accountBalance;
    }


    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

 
    public String getCurrency() {
        return currency;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public void setUserName(String username) {
        this.username = username;
    }
    
    public String getUserName() {
        return username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
}
