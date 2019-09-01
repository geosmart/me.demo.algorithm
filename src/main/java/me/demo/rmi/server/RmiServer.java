package me.demo.rmi.server;

import me.demo.rmi.inf.MyRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {

    public static void main(String[] args) {
        int port = 1099;
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("start rmiregistry ");

            MyRemote remote = new MyRemoteImpl();
            System.out.println(String.format("register service[%s],class=[%s]", MyRemote.class.getSimpleName(), MyRemoteImpl.class.getTypeName()));
            registry.rebind(MyRemote.class.getSimpleName(), remote);
            System.out.println("rim server is running...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
