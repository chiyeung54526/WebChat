package com.cz2397.webchat.service;

import com.cz2397.webchat.dao.LogMapper;
import com.cz2397.webchat.entity.ChatLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService{

    @Autowired
    private LogMapper logMapper;

    @Override
    public boolean logInsert(ChatLog chatLog){
        logMapper.insertlog(chatLog);
        return true;
    }
}
