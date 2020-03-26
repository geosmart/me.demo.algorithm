package me.demo.rpc.netty.client.demo;

import java.util.List;

interface TestBeanRemote {
    void delete(long id);

    long add(TestData o);

    void edit(TestData o);

    List<?> query(String key);
}
