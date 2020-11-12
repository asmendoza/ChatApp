/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arisgail.chatapp.chatserver;

import java.util.Objects;

/**
 *
 * @author arisgailmendoza
 */
public class Message {
    private String sender;
    private String recipient;
    private String textBody;

    @Override
    public String toString() {
        return "Message{" + "sender=" + sender + ", recipient=" + recipient + ", textBody=" + textBody + '}';
    }

    public Message(String sender, String recipient, String textBody) {
        this.sender = sender;
        this.recipient = recipient;
        this.textBody = textBody;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.sender);
        hash = 11 * hash + Objects.hashCode(this.recipient);
        hash = 11 * hash + Objects.hashCode(this.textBody);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.recipient, other.recipient)) {
            return false;
        }
        if (!Objects.equals(this.textBody, other.textBody)) {
            return false;
        }
        return true;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }
}
