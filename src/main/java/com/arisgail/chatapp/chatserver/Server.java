/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arisgail.chatapp.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arisgailmendoza
 */
public class Server extends Thread{

    private final int serverPort;
    
    private ArrayList<ClientCxn> clientList = new ArrayList<>();
    
    public Server (int port)
    {
        this.serverPort = port;
    }
    
    public List<ClientCxn> getClientList()
    {
        return clientList;
    }
    
    @Override
    public void run()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true)
            {
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                ClientCxn worker = new ClientCxn(this, clientSocket);
                clientList.add(worker);
                worker.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
