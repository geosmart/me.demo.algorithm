package me.demo.rmi.server;

import me.demo.rmi.inf.MyRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MyRemoteImpl extends UnicastRemoteObject implements MyRemote {
    private static final long serialVersionUID = 1L;


    protected MyRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public String concat(String a, String b) throws RemoteException {
        return a + b;
    }

    @Override
    public String sayHello() throws RemoteException {
        return "hello JAVA RMI !";
    }

}
