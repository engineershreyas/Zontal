package com.shreyas.zontal.zontal.utils;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by shreyashirday on 1/2/16.
 */
public class ChatFactory {



    public static ParseObject createChat(ParseUser userA, ParseUser userB){

        ParseObject chat = new ParseObject("Chat");

        chat.put("userAid",userA.getObjectId());
        chat.put("userBid",userB.getObjectId());
        chat.put("userA",userA.getUsername());
        chat.put("userB",userB.getUsername());

        chat.put("latestMessage","");


        String chatID = generateChatID(userA,userB);

        chat.put("chatID",chatID);

        return chat;



    }

    private static String generateChatID(ParseUser userA, ParseUser userB){

        String userAId = userA.getObjectId();
        String userBId = userB.getObjectId();

        String chatID = userAId.compareTo(userBId) > 0 ? userAId + userBId : userBId + userAId;

        return  chatID;

    }

    public static boolean isUserA(ParseObject chat,String userID){
        String userA = chat.getString("userAid");


        if(userID.equals(userA)){
            return true;
        }

        return false;

    }


    public static ParseObject createMessage(String msg, ParseUser userA, ParseUser userB){

        ParseObject message = new ParseObject("Message");

        String chatID = generateChatID(userA,userB);

        message.put("chatID",chatID);

        message.put("text",msg);

        message.put("read",false);




        return message;



    }




}
