package com.bank.DAO;

import org.junit.Assert;
import org.junit.Test;

public class ClientDAO_DB_Test {
    @Test
    public void check_the_account_number_that_exist(){
        String number="PL4900012990"; //number that exist in table
        ClientDAO_DB clientDAO_db=new ClientDAO_DB();
        Assert.assertTrue(clientDAO_db.checkAccountNumber(number));
    }

    @Test
    public void check_the_account_number_that_not_exist(){
        String number="PL4900012999"; //number that does not exist in table
        ClientDAO_DB clientDAO_db=new ClientDAO_DB();
        Assert.assertFalse(clientDAO_db.checkAccountNumber(number));
    }

    @Test
    public void check_user_name_that_exist(){
        String nick="Adam55"; //this nick exist in table
        ClientDAO_DB clientDAO_db = new ClientDAO_DB();
        Assert.assertTrue(clientDAO_db.checkUserName(nick));
    }

    @Test
    public void check_user_name_that_not_exist(){
        String nick="Adam56";
        ClientDAO_DB clientDAO_db = new ClientDAO_DB();
        Assert.assertFalse(clientDAO_db.checkUserName(nick));
    }

    @Test
    public void check_email_that_exist(){
        String email="kowal@gmail.com";
        ClientDAO_DB clientDAO_db=new ClientDAO_DB();
        Assert.assertTrue(clientDAO_db.checkEmail(email));
    }

    @Test
    public void check_email_that_not_exist(){
        String email="kowalski@gmail.com";
        ClientDAO_DB clientDAO_db=new ClientDAO_DB();
        Assert.assertFalse(clientDAO_db.checkEmail(email));
    }
}
