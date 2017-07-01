package service;

import rest.Service;

public class TestService implements ITestService {
    @Override
    public String name() {
        return "service.TestService";
    }

    @Override
    public String test() {
        return "test x3";
    }
}
