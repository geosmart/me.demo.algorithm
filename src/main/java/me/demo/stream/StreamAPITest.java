package me.demo.stream;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPITest {

    @Test
    public void test_swap() {
        int a = 10;
        int b = 20;
        a = a ^ b;
        b = b ^ a;
        a = a ^ b;
        System.out.println(a + "\t" + b);
    }

    @Test
    public void test_api() {
        String[] words = new String[]{"hello", "world"};
        List<String> collect = Arrays.asList(words).stream()
                .map(e -> e.split(""))
                .flatMap(e -> Arrays.stream(e))
                .collect(Collectors.toList());
        System.out.println(collect);
    }

}
