package me.demo.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPITest {

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
