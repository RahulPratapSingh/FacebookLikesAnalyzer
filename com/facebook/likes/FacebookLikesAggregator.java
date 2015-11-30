package com.facebook.likes;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import com.restfb.*;
import com.restfb.types.Likes;
import com.restfb.types.User;

public class FacebookLikesAggregator {
    // have to generate again at https://developers.facebook.com/tools/explorer/ while demo
    private static final String ACCESS_TOKEN = "CAACEdEose0cBAEbr9jFYxZBnILndRcLZBZCyNRRsbc4ltCyHEZB9beehdyvYxVGeUK2l3PvOUQUMVgzoHldbhieLYE6ttxXzJ0d5zBNSnlxLcJPyT3pZCXYmY0JrHAzeKsxOVC2EWx9EPAlaOw6V8SsgBKC8wZAsXMve0jogkiAXZBBt8lD2HrfrGnERY4gsBZBWv3IFPoEdKTPPVXHRYaoj";
    private static final String FIELD_SEPARATOR = ",";
    private static final String FILE_NAME = "facebook-file.tsv";

    public static void main(String[] args) {
        FileWriter writer = null;
        PrintWriter printWriter = null;
        try {
            File file = new File(FILE_NAME);
            if(file.exists()) file.delete();
            file.createNewFile();
            writer = new FileWriter(file);
            printWriter = new PrintWriter(writer);
            // create connection with facebook
            FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.VERSION_2_5);
            // fetch all my friends (post 2.0 version fb is not returning all friends)
            Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
            Iterator<List<User>> iter = myFriends.iterator();
            int usersCount = 0;
            while (iter.hasNext()) {
                List<User> users = iter.next();
                for (User usr : users) {
                    int likes = getUsersLikes(facebookClient, usr.getId());
                    printDataToTabDelimitedFile(printWriter, usr.getId(), usr.getName(), likes);
                    usersCount++;
                }
            }
            System.out.println("Total users aggregated : " + usersCount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                printWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public static int getUsersLikes(FacebookClient facebookClient, String userId) {
        Connection<Likes> userLikes = facebookClient.fetchConnection(userId + "/likes", Likes.class);
        Iterator<List<Likes>> iter = userLikes.iterator();
        int likesCount = 0;
        while (iter.hasNext()) {
            List<Likes> likes = iter.next();
            likesCount = likesCount + likes.size();
        }
        return likesCount;
    }

    public static void printDataToTabDelimitedFile(PrintWriter writer, String userId, String userName, int likes) {
        StringBuilder userActivity = new StringBuilder();
        userActivity.append(userId).append(FIELD_SEPARATOR);
        userActivity.append(userName).append(FIELD_SEPARATOR);
        userActivity.append(likes);
        writer.print(userActivity);
        writer.println();
    }
}