package com.cz2397.webchat.entity;

import com.cz2397.webchat.controller.WebSocketServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    public String id;

    public String nickname;

    public WebSocketServer webSocket;
}
