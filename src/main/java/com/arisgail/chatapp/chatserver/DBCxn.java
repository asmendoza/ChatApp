/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arisgail.chatapp.chatserver;

/**
 *
 * @author arisgailmendoza
 */
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DBCxn {
    //Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
    
    private static final ConnectionString conn
            = new ConnectionString(
            "mongodb://MCS-15:27017");
    private static final CodecRegistry pojoCodecRegistry = 
            fromProviders(PojoCodecProvider.builder()
                    .automatic(true).build());
    private static final CodecRegistry codecRegistry = 
            fromRegistries(MongoClientSettings.getDefaultCodecRegistry()
                    , pojoCodecRegistry);
    private static final MongoClientSettings settings = 
            MongoClientSettings.builder()
                .applyConnectionString(conn)
                .codecRegistry(codecRegistry)
                .retryWrites(true)
                .build();
    private static final MongoClient mClient = 
            MongoClients.create(settings);
    private static final MongoDatabase mDbase = 
            mClient.getDatabase("chatapp");
    private static final MongoCollection<User> users = 
            mDbase.getCollection("users", User.class);
    
    public static boolean userExists(String emailAdd, String password)
    {
        boolean retVal = false;
        
        User user = users.find(eq("emailAdd",emailAdd)).first();
        
        if(user != null)
            retVal = user.getPassword().equals(password);
        
        return retVal;
    }
    
    public static User getUser(String emailAdd, String password)
    {
        User retVal;
        
        retVal = users.find(eq("emailAdd", emailAdd)).first();
        
        if(retVal != null)
            if(retVal.getPassword().equals(password) == true)
                return retVal;
        
        return retVal;
    }
    
    public static User getUser(org.bson.types.ObjectId id)
    {
        User retVal;
        
        retVal = users.find(eq("_id", id)).first();
        
        if(retVal != null)
            return retVal;
        
        return retVal;
    }
    
    public static User updateUser(User user)
    {
        Document filterByUserId = new Document("_id", user.getId());
        FindOneAndReplaceOptions returnDocAfterReplace = 
                new FindOneAndReplaceOptions().returnDocument(
                        ReturnDocument.AFTER);
        return users.findOneAndReplace(filterByUserId, user,returnDocAfterReplace);
    }
}
