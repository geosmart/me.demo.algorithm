package me.demo.stream;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StreamForLoopTest {
    static List<User> userList = null;
    static Consumer<User> consumer;

    @BeforeAll
    public static void setup() {
        userList = initList(10000000);
        consumer = user -> user.hashCode();
    }

    //11s
    @RepeatedTest(100)
    @Order(1)
    public void test_lambda() {
        testLambda(userList);
    }

    //15s
    @RepeatedTest(100)
    @Order(4)
    public void test_lambda_with_consumer() {
        testLambda2(userList, consumer);
    }

    //11s
    @RepeatedTest(100)
    @Order(3)
    public void test_for() {
        testFor(userList);
    }

    //11s
    @RepeatedTest(100)
    @Order(4)
    public void test_fori() {
        testFori(userList);
    }

    /**
     * 增强for测试
     *
     * @param userList
     */
    private static void testFor(List<User> userList) {
        for (User user : userList) {
            user.hashCode();
        }
    }

    /**
     * fori测试
     *
     * @param userList
     */
    private static void testFori(List<User> userList) {
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).hashCode();
        }
    }

    /**
     * lambda forEach测试2-提前定义好函数
     *
     * @param userList
     */
    private static void testLambda2(List<User> userList, Consumer func) {
        userList.forEach(func);
    }

    /**
     * lambda forEach测试
     *
     * @param userList
     */
    private static void testLambda(List<User> userList) {
        userList.forEach(user -> user.hashCode());
    }

    /**
     * 初始化测试集合
     *
     * @param size
     * @return
     */
    private static List<User> initList(int size) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            userList.add(new User("user" + i, String.valueOf(i)));
        }
        return userList;
    }

    public static class User {
        String id;
        String name;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
