package com.bank.DAO;

import com.bank.BankHelper;
import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class BankHelper_Test{
    @Test
    public void format_money_given_1000_result_should_be_10_00f(){
        //gdy liczba jest większa niż 0
        //given
        BigDecimal bigDecimal1=new BigDecimal(1000);
        BigDecimal bigDecimal2=new BigDecimal("10.00");

        //when
        BigDecimal result=BankHelper.formatMoney(bigDecimal1);

        //then
        Assert.assertEquals(result, bigDecimal2);
    }

    @Test
    public void format_money_when_given_number_is_negative(){
        //given
        BigDecimal bigDecimal1=new BigDecimal(-112);
        BigDecimal bigDecimal2=new BigDecimal("-1.12");

        //when
        BigDecimal result=BankHelper.formatMoney(bigDecimal1);

        //then
        Assert.assertEquals(result, bigDecimal2);
    }

}
