package com.example;

import org.junit.Assert;
import org.junit.Test;

public class Test2 {

    @Test
    public void testMethod1() {
        System.out.println("We are running TestMethod1 from Test2 class");
        Assert.assertTrue(true);
    }

    @Test
    public void testMethod2() {
        System.out.println("We are running TestMethod2 from Test2 class");
        Assert.assertTrue(true);
    }

}
