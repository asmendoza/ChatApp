/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arisgail.chatapp.chatserver;

import java.util.HashSet;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.bson.Document;
/**
 *
 * @author arisgailmendoza
 */
public class User {
    private ObjectId id;
    private String emailAdd;
    private String password;
    private String displayName;
    private Object photo;
    private boolean online;
    private HashSet<String> topicSet = new HashSet<>();

    public boolean isMemberOfTopic(String topic)
    {
        return topicSet.contains(topic);
    }
    
    public HashSet<String> getTopicSet() {
        return topicSet;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", emailAdd=" + emailAdd + ", password=" + password + ", displayName=" + displayName + ", photo=" + photo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.emailAdd);
        hash = 53 * hash + Objects.hashCode(this.password);
        hash = 53 * hash + Objects.hashCode(this.displayName);
        hash = 53 * hash + Objects.hashCode(this.photo);
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
        final User other = (User) obj;
        if (!Objects.equals(this.emailAdd, other.emailAdd)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.displayName, other.displayName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.photo, other.photo)) {
            return false;
        }
        return true;
    }
}
