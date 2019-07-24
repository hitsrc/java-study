package com.sample.tools.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.sample.tools.comm.util.RedisUtil;

/**
 * RedisHttpSession 替代 HttpSession
 * @author swq
 *
 */
public class RedisHttpSession implements HttpSession{

    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;

    public static final String SESSION_PREFIX = "session:";
    private static final String SESSION_ATTR = "sessionAttr:";
    private static final String CREATION_TIME = "creationTime";
    private static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    private static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";

    private String key;
    private String id;
    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveInterval;

    private ServletContext servletContext;

    private RedisHttpSession(){}


    private RedisHttpSession(ServletContext servletContext) {
        this.servletContext = servletContext;
        id = UUID.randomUUID().toString();
        key = SESSION_PREFIX + id;
        creationTime = System.currentTimeMillis();
        lastAccessedTime = creationTime;
        maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
        //save to redis
        saveSession();
    }

    public static RedisHttpSession createNew(ServletContext servletContext){
        return new RedisHttpSession(servletContext);
    }

    public static RedisHttpSession createWithExistSession(String token, ServletContext servletContext){
        RedisHttpSession redisHttpSession= new RedisHttpSession();
        redisHttpSession.setId(token);
        redisHttpSession.setKey(SESSION_PREFIX + token);
        redisHttpSession.setServletContext(servletContext);

        return redisHttpSession;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public long getCreationTime() {
    	return (long)RedisUtil.get(key,CREATION_TIME);
        //return (long) redisConnection.hget(key, CREATION_TIME);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return (long) RedisUtil.get(key, LAST_ACCESSED_TIME);
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        RedisUtil.hset(key, MAX_INACTIVE_INTERVAL, interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return (int) RedisUtil.get(key, MAX_INACTIVE_INTERVAL);
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return RedisUtil.get(key, SESSION_ATTR + name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(getAttributeKeys());
    }

    private Set<String> getAttributeKeys(){
        Set<String> keys = RedisUtil.keys(key);
        Set<String> attrNames = new HashSet<>();
        for (String key : keys){
            if (key.startsWith(SESSION_ATTR)){
                attrNames.add(key.substring(SESSION_ATTR.length()));
            }
        }
        return attrNames;
    }


    @Override
    public String[] getValueNames() {
        return getAttributeKeys().toArray(new String[0]);
    }

    @Override
    public void setAttribute(String name, Object value) {
        RedisUtil.hset(key, SESSION_ATTR + name, (Serializable) value);
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        RedisUtil.hdel(key, name);
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
    	RedisUtil.del(key);
    }

    @Override
    public boolean isNew() {
        return false;
    }

    private void saveSession(){
        RedisUtil.hset(key, LAST_ACCESSED_TIME, lastAccessedTime);
        RedisUtil.hset(key, CREATION_TIME, creationTime);
        RedisUtil.hset(key, MAX_INACTIVE_INTERVAL, maxInactiveInterval);
        refresh();
    }

    /**
     * update expire time
     */
    public void refresh(){
        RedisUtil.expire(key, getMaxInactiveInterval());
    }


    public void setLastAccessedTime(long lastAccessedTime) {
        RedisUtil.hset(key, LAST_ACCESSED_TIME, lastAccessedTime);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isInvalidated() {
        return !RedisUtil.exists(key);
    }

}
