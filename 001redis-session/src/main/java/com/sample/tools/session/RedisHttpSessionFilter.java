package com.sample.tools.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.sample.tools.comm.util.RedisUtil;

/**
 * 过滤器配置好后使用redis替代HttpSession
 * @author swq
 *
 */
public class RedisHttpSessionFilter implements Filter {

    public static final String TOKEN_HEADER_NAME = "x-auth-token";
    
    private RedisHttpSessionRepository repository;

    public RedisHttpSessionFilter(){
        repository = RedisHttpSessionRepository.getInstance();
    }

	
	public FilterConfig config;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         RedisSessionRequestWrapper requestWrapper = new RedisSessionRequestWrapper((HttpServletRequest) request,(HttpServletResponse)response);
         RedisSessionResponseWrapper responseWrapper = new RedisSessionResponseWrapper((HttpServletResponse) response, requestWrapper);

         chain.doFilter(requestWrapper, responseWrapper);
	}
	@Override
	public void destroy() {
		this.config=null;
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config=filterConfig;
	}
	
    private final class RedisSessionRequestWrapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;
    	private HttpServletResponse response;

        private String token;
        /**
         * 重新包装request
         */
        public RedisSessionRequestWrapper(HttpServletRequest request,HttpServletResponse response) {
            super(request);
            this.request = request;
            this.response = response;
            this.token = request.getHeader(TOKEN_HEADER_NAME);
        }

        @Override
        public HttpSession getSession(boolean create) {
        	HttpSession session = null;
        	if (token == null) {
            	String tmpToken = CookieUtil.getCookieValue(request, "pcxSessionId");
            	if(RedisUtil.exists("session:" + tmpToken)) {//cookie中的有效则直接使用
            		token = tmpToken;
            	}
            }
            
            if (token != null && !"".equals(token)) {
                return repository.getSession(token, request.getServletContext());
            } else if (create){
                session = repository.newSession(request.getServletContext());
                token = session.getId();
                CookieUtil.setCookie(request, response, "pcxSessionId", token, -1);
                return session;
            } else {
                return null;
            }
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return token;
        }
    }

    private final class RedisSessionResponseWrapper extends HttpServletResponseWrapper {
        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response
         * @throws IllegalArgumentException if the response is null
         */
        public RedisSessionResponseWrapper(HttpServletResponse response, RedisSessionRequestWrapper request) {
            super(response);
            //if session associate with token is not existed, create one for the response
            response.setHeader(TOKEN_HEADER_NAME, request.getSession(true).getId());
        }
    }
}