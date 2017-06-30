package service.impl;

import service.ITestService;

public class TestService1 implements ITestService {
    @Override
    public void print() {
        System.out.println("Test 1");
    }

    @Override
    public String toString() {
        return "Test 1";
    }
}
