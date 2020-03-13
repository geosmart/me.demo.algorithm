package me.demo.rpc.java.demo.server;

public class CalculatorServiceImpl implements CalculatorService {

  @Override
  public int add(int a, int b) {
    return a + b;
  }
}
