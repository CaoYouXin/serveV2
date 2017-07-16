package rest;

public class Service1Impl implements Service1 {
    @Override
    public String getA() {
        throw new RuntimeException("get a fail.");
    }

    @Override
    public void before() {
        throw new RuntimeException("before fail.");
    }
}
