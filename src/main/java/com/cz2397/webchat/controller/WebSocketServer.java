package com.cz2397.webchat.controller;

import cn.hutool.core.util.StrUtil;
import com.cz2397.webchat.entity.ChatLog;
import com.cz2397.webchat.service.LogServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cz2397.webchat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**@Author: Cailin
 * @Date: 2022.8.25
 * @Description: my WebSocket class. must include open/close/error/message.
 */

@Component
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
    // to count online number in this room
    private static AtomicInteger onlineCount = new AtomicInteger();

    // one session for one user
    private Session session;

    // map<room, user>
    private static Map<String, String> RoomForUser = new ConcurrentHashMap<String, String>();

    // map<room,user<set>>
    public static Map<String, CopyOnWriteArraySet<User>> UserForRoom = new ConcurrentHashMap<String, CopyOnWriteArraySet<User>>();

    // map<user, pwd>
    public static Map<String, String> PwdForRoom = new ConcurrentHashMap<String, String>();

    // get JSON
    private Gson gson = new Gson();

    // random num
    private Random random = new Random();

    // DB service impl
    private LogServiceImpl logServiceImpl;

    // Handler for each WebSocket session on
    // OnlineCount++, update map<UserForRoom>, and response JSON
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        addOnlineCount();
        Map<String, String> result = new HashMap<String, String>();
        result.put("sendUser", "Admin");
        result.put("id", session.getId());
        this.sendMessage(gson.toJson((result)));
    }

    // Handle when user's message received
    @OnMessage
    public void onMessage(String message, Session session) {


        // request map
        Map<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
        }.getType());

        // response map
        Map<String, String> result = new HashMap<>();
        User user = null;
        String shiels = map.containsKey("shiels") ? map.get("shiels").toString() : null;
        switch (map.get("type")) {

            // user already in room
            case "msg":

                user = getUser(session);

                result.put("type", "msg");
                result.put("msg", map.get("msg"));
                result.put("sendUser", user.getNickname());
                result.put("shake", map.get("shake"));
                break;

            // user firstly join
            case "init":

                String room = map.get("room");
                String nick = map.get("nick");
                String pwd = map.get("pwd");
                if (room != null && nick != null) {
                    user = new User(session.getId(), nick, this);

                    if (UserForRoom.get(room) == null) {
                        CopyOnWriteArraySet<User> roomUsers = new CopyOnWriteArraySet<>();
                        roomUsers.add(user);
                        UserForRoom.put(room, roomUsers);
                        if (StrUtil.isNotEmpty(pwd)) {
                            PwdForRoom.put(room, pwd);
                        }
                        RoomForUser.put(session.getId(), room);
                    } else {
                        UserForRoom.get(room).add(user);
                        RoomForUser.put(session.getId(), room);
                    }
                    result.put("type", "init");
                    result.put("msg", nick + "enter room");
                    result.put("sendUser", "Admin");
                }
                break;

            case "img":
                user = getUser(session);
                result.put("type", "img");
                result.put("msg", map.get("msg"));
                result.put("sendUser", user.getNickname());
                break;
            case "ping":
                return;
        }

        if (StrUtil.isEmpty(shiels)) {
            sendMessagesOther(getUsers(session), gson.toJson(result));
        } else {
            sendMessagesOther(getUsers(session), gson.toJson(result), shiels);
        }
    }

    //Onclose: count, update map
    @OnClose
    public void onClose() {
        subOnlineCount();

        CopyOnWriteArraySet<User> users = getUsers(session);
        if (users != null) {
            String nick = "Anonymous";
            for (User user : users) {
                if (user.getId().equals(session.getId())) {
                    nick = user.getNickname();
                }
            }

            Map<String, String> result = new HashMap<>();
            result.put("type", "init");
            result.put("msg", nick + "exit");
            result.put("sendUser", "Admin");
            sendMessagesOther(users, gson.toJson(result));

            User closeUser = getUser(session);
            users.remove(closeUser);

            if (users.size() == 0) {
                String room = RoomForUser.get(session.getId());
                UserForRoom.remove(room);
                PwdForRoom.remove(room);
            }
            RoomForUser.remove(session.getId());
        }
    }

    
    @OnError
    public void onError(Session session, Throwable error) {
        subOnlineCount();
        CopyOnWriteArraySet<User> users = getUsers(session);
        if (users != null) {
            String nick = "Anonymous";
            for (User user : users) {
                if (user.getId().equals(session.getId())) {
                    nick = user.getNickname();
                }
            }
            Map<String, String> result = new HashMap<>();
            result.put("type", "init");
            result.put("msg", nick + "exit");
            result.put("sendUser", "Admin");
            sendMessagesOther(users, gson.toJson(result));
            User closeUser = getUser(session);
            users.remove(closeUser);
            if (users.size() == 0) {
                String room = RoomForUser.get(session.getId());
                UserForRoom.remove(room);
                PwdForRoom.remove(room);
            }
            RoomForUser.remove(session.getId());
        }
        error.printStackTrace();
    }


    
    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.incrementAndGet();
    }


    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.decrementAndGet();
    }

    
    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

    
    private User getUser(Session session) {
        String room = RoomForUser.get(session.getId());
        CopyOnWriteArraySet<User> users = UserForRoom.get(room);
        for (User user : users) {
            if (session.getId().equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    
    private CopyOnWriteArraySet<User> getUsers(Session session) {
        String room = RoomForUser.get(session.getId());
        CopyOnWriteArraySet<User> users = UserForRoom.get(room);
        return users;
    }


    
    private void sendMessagesOther(CopyOnWriteArraySet<User> users, String message) {

        for (User item : users) {

            if (item.getWebSocket() != this) {
                try {
                    item.getWebSocket().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private void sendMessagesOther(CopyOnWriteArraySet<User> users, String message, String shiel) {


        List<String> shiels = Arrays.asList(shiel.split(","));

        for (User item : users) {
            if (item.getWebSocket() != this && !shiels.contains(item.getId())) {
                try {
                    item.getWebSocket().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private void sendMessagesAll(CopyOnWriteArraySet<User> users, String message) {

        for (User item : users) {
            try {
                item.getWebSocket().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void insertLog(ChatLog chatlog){
        logServiceImpl.logInsert(chatlog);
    }
}
