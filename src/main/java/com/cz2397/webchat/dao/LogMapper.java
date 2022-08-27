package com.cz2397.webchat.dao;

import com.cz2397.webchat.entity.ChatLog;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {
    public void insertlog(ChatLog chatLog);
}
