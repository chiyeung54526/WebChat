package com.cz2397.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
public class ChatLog{

    private String username;

    private Integer roomid;

    private Integer msgtype;

    private String message;
}

