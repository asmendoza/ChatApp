/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arisgail.chatapp.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author arisgailmendoza
 */
public class ClientCxn extends Thread{
    
    private final Socket clientSocket;
    private String login = null;
    private User user = null;
    private final Server server;
    private OutputStream outputStream;
    private InputStream inputStream; 
    
    public ClientCxn(Server server, Socket clientSocket)
    {
        this.server = server;
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run()
    {
        try {
            handleClientSocket();
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ClientCxn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void handleClientSocket() throws InterruptedException, IOException 
    {
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while( (line = reader.readLine()) != null)
        {
            String[] tokens = StringUtils.split(line);
            if(tokens != null && tokens.length > 0)
            {
                String cmd = tokens[0];
                if("quit".equalsIgnoreCase(cmd))
                {
                    if(this.user != null)
                        handleLogOff(outputStream);
                    this.server.getClientList().remove(this);
                    break;
                }
                else if ("login".equalsIgnoreCase(cmd))
                {
                    handleLogin(outputStream, tokens);
                }
                else if ("logoff".equalsIgnoreCase(cmd))
                {
                    if(this.user != null)
                        handleLogOff(outputStream);
                }
                else if ("msg".equalsIgnoreCase(cmd))
                {
                    if(this.user != null)
                    {
                        String[] tokenMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokenMsg);
                    }
                }
                else if ("join".equalsIgnoreCase(cmd))
                {
                    if(this.user != null)
                    {
                        handleJoin(tokens);
                    }
                }
                else if ("leave".equalsIgnoreCase(cmd))
                {
                    if(this.user != null)
                    {
                        handleLeave(tokens);
                    }
                }
                else
                {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        
        clientSocket.close();
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException 
    {
        if(tokens.length == 3)
        {
            this.login = tokens[1];
            String password = tokens[2];
            
            if (DBCxn.userExists(login, password))
            {
                this.user = DBCxn.getUser(login, password);
                this.user.setOnline(true);
                this.user = DBCxn.updateUser(this.user);
                String msg = "Welcome " + this.user.getDisplayName() + "!\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("User logged in successfully: " + user.getDisplayName());
                
                
                List<ClientCxn> clientList = server.getClientList();
                // print all online users for current user
                for(ClientCxn client : clientList)
                {
                    if(client.getUser() != null && !this.user.equals(client.getUser()))
                    {
                        String onlineMsg = "online " + client.getUser().getDisplayName() + "\n";
                        send(onlineMsg);
                    }
                }
                
                // notify other users of current user's status.
                for(ClientCxn client : clientList)
                {
                    if(client.getUser() != null && !this.user.equals(client.getUser()))
                    {
                        String onlineMsg = "online " + this.user.getDisplayName() + "\n";
                        client.send(onlineMsg);
                    }
                }
            }
            else
            {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.out.println("Unknown user account or wrong password: " + login);
            }
        }
        
    }
    
    private void handleLogOff(OutputStream outputStream) throws IOException
    {
        this.user.setOnline(false);
        this.user = DBCxn.updateUser(this.user);
        String msg = "logged off\n";
        outputStream.write(msg.getBytes());
        System.out.println("User logged off successfully: " + user.getDisplayName());
        
        // notify other users of current user's status.
        for(ClientCxn client : this.server.getClientList())
        {
            if(client.getUser() != null && !this.user.equals(client.getUser()))
            {
                String onlineMsg = this.user.getDisplayName() + " is offline\n";
                client.send(onlineMsg);
            }
        }
        
        this.login = null;
        this.user = null;
    }

    public User getUser()
    {
        return this.user;
    }
    
    public void send(String msg) throws IOException {
        if(this.login != null || this.user != null)
            outputStream.write(msg.getBytes());
    }

    // format: msg login textBody
    // format: msg #topic textBody
    private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];
        
        boolean isTopic = sendTo.charAt(0) == '#';
        
        for(ClientCxn client : this.server.getClientList())
        {
            if(isTopic && this.user.isMemberOfTopic(sendTo))
            {
                if(client.getUser().isMemberOfTopic(sendTo))
                {
                    String outMsg = this.user.getDisplayName() 
                            + "(" + sendTo + "): " + body + "\n";
                    client.send(outMsg);
                }
            }
            else
            {
                if(sendTo.toUpperCase().equals(
                        client.getUser().getDisplayName().toUpperCase()))
                {
                    String outMsg = this.user.getDisplayName() 
                            + ": " + body + "\n";
                    client.send(outMsg);
                    break;
                }
            }
        }
    }

    private void handleJoin(String[] tokens) 
    {
        if (tokens.length > 1)
        {
            String topic = tokens[1];
            this.user.getTopicSet().add(topic);
        }
    }

    private void handleLeave(String[] tokens) 
    {
        if (tokens.length > 1)
        {
            String topic = tokens[1];
            this.user.getTopicSet().remove(topic);
        }
    }
}
