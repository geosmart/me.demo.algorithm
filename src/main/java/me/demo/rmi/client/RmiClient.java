package me.demo.rmi.client;

import me.demo.rmi.inf.MyRemote;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Registry registry = LocateRegistry.getRegistry("127.0.01", 1099);
        MyRemote remote = (MyRemote) registry.lookup(MyRemote.class.getSimpleName());
        System.out.println(remote.sayHello());
        System.out.println(remote.concat("hello.", "world"));
    }
}
