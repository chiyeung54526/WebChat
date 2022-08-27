package com.cz2397.webchat.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cz2397.webchat.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description: Controller
 *  API:
 *  /ws: WebSocket Controller
 *  /online: get online user in this room
 *  /check: get list of all existed room
 *  /fileupload: upload imgs to serve
 */

@RestController
@RequestMapping("/ws")
public class SocketController {

    private String imgPath = new ApplicationHome(getClass()).getSource().getParentFile().toString()+"/img/";


    public static Map<Long,String> img = new HashMap();

    // response a list of users
    @RequestMapping("/online")
    public Map<String,Object> online(String room){
        Map<String,Object> result = new HashMap<>();
        CopyOnWriteArraySet<User> rooms = WebSocketServer.UserForRoom.get(room);

        List<Map<String,String>> users = new ArrayList<>();
        if (rooms != null){
            rooms.forEach(user -> {
                Map<String,String> map = new HashMap<>();
                map.put("nick",user.getNickname());
                map.put("id",user.getId());
                users.add(map);
            });
            result.put("onlineNum",rooms.size());
            result.put("onlineUsera",users);
        }else {
            result.put("onlineNum",0);
            result.put("onlineUsera",null);
        }
        return result;
    }

    // check name duplicate/room password for login page
    @RequestMapping("/check")
    public Map<String,Object> judgeNick(String room, String nick, String pwd){
        Map<String,Object> result = new HashMap<>();
        result.put("code",0);
        CopyOnWriteArraySet<User> rooms = WebSocketServer.UserForRoom.get(room);
        if (rooms != null){
            rooms.forEach(user -> {
                if (user.getNickname().equals(nick)){
                    result.put("code",1);
                    result.put("msg","Username exists");
                }
            });
            if ((Integer)result.get("code") != 0){
                return result;
            }
            String password = WebSocketServer.PwdForRoom.get(room);
            if (StrUtil.isNotEmpty(password) && !(pwd.equals(password))){
                result.put("code",2);
                result.put("msg","Wrong paaword");
                return result;
            }else {
                result.put("code",3);
                result.put("msg","No password");
                return result;
            }
        }
        return result;
    }


    // file transfer
    @RequestMapping("/fileUpload")
    public Map<String,Object> fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        Map<String,Object> result = new HashMap<>();

        String root = request.getRequestURL().toString().replace(request.getRequestURI(),"");
        if(file.isEmpty()){
            return null;
        }

        String fileName = file.getOriginalFilename();
        String imgName = RandomUtil.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        File dest = new File(imgPath + imgName);
        img.put(System.currentTimeMillis(),imgPath + imgName);

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
        try {

            file.transferTo(dest);

            result.put("url",root +"/img/" + imgName);
            return result;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get the list of all rooms
    @RequestMapping("/allRoom")
    public Map<String,Object> allRoom(){
        Map<String,Object> result = new HashMap<>();
        Map<String,CopyOnWriteArraySet<User>> userForRoom = WebSocketServer.UserForRoom;
        List<String> rooms = new ArrayList<>();
        for (String key : userForRoom.keySet()) {
            rooms.add(key);
        }
        result.put("rooms",rooms);
        return result;
    }

}
