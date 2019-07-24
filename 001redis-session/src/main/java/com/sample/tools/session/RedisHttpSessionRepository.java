package com.sample.tools.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * Session操作类 
 * 获取RedisHttpSession
 * 新session则同时保存到redis
 * @author swq
 *
 */
public class RedisHttpSessionRepository {

    private final static RedisHttpSessionRepository instance = new RedisHttpSessionRepository();

    public static RedisHttpSessionRepository getInstance(){
        return instance;
    }

    public HttpSession newSession(ServletContext servletContext) {
        RedisHttpSession redisHttpSession = RedisHttpSession.createNew(servletContext);
        return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
    }

    /**
     * get session according to token
     * @param token
     * @param servletContext
     * @return session associate to token or null if the token is invalid
     */
    public HttpSession getSession(String token, ServletContext servletContext){
        RedisHttpSession redisHttpSession = RedisHttpSession.createWithExistSession(token, servletContext);
        return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
    }
}
