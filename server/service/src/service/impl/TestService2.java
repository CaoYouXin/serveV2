package service.impl;

import service.ITestService;

public class TestService2 implements ITestService {
    @Override
    public void print() {
        System.out.println("Test 2");
    }
}
