package me.demo.rmi.inf;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyRemote extends Remote {
    String concat(String a, String b) throws RemoteException;

    String sayHello() throws RemoteException;
}
