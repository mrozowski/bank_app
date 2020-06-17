package com.bank.DAO;

import com.bank.Registration.RegistrationWindowPresenter;
import com.bank.Registration.RegistrationWindowPresenterFactory;

import javafx.scene.layout.Pane;
import org.junit.Assert;
import org.junit.Test;

public class RegistrationWindowPresenter_Test {

    @Test
     public void sprawdz_czy_ma_12_znakow(){
        RegistrationWindowPresenter presenter = new RegistrationWindowPresenterFactory(new Pane()).create();
        Assert.assertEquals(presenter.generateBankNumber().length(), 12);

}
}
