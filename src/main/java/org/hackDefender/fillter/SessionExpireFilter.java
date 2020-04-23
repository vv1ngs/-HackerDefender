package org.hackDefender.fillter;

import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.pojo.User;
import org.hackDefender.util.CookieUtil;
import org.hackDefender.util.JacksonUtil;
import org.hackDefender.util.RedisPoolSharedUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author vvings
 * @version 2020/4/22 20:13
 */
public class SessionExpireFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisPoolSharedUtil.get(loginToken);
            User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
            if (user != null) {
                RedisPoolSharedUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
