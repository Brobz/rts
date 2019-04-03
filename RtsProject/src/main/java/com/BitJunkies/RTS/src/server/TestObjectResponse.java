/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BitJunkies.RTS.src.server;

/**
 *
 * @author rober
 */
public class TestObjectResponse {
 
    private TestObject test;
 
    public TestObjectResponse() {        
    }
 
    public TestObjectResponse(TestObject test) {
        this.test = test;
    }
 
    public TestObject getTest() {
        return test;
    }
 
    public void setTest(TestObject test) {
        this.test = test;
    }
}