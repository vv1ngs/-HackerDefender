package org.hackDefender.interceptor;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.pojo.User;
import org.hackDefender.util.CookieUtil;
import org.hackDefender.util.JacksonUtil;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/4/21 11:08
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequestLogin requestLogin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequestLogin.class);
        if (requestLogin == null) {//如果类上的注解为空
            requestLogin = handlerMethod.getMethod().getAnnotation(RequestLogin.class);
            if (requestLogin == null) {
                return true;
            }
        }
        if (Permission.REQUEST_NONE.equals(requestLogin.desc())) {
            return true;
        }
        //需要登录
        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisPoolSharedUtil.get(loginToken);
            user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        }
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        if (user == null) {
            PrintWriter out = response.getWriter();
            Map resultMap = Maps.newHashMap();
            resultMap.put("success", false);
            resultMap.put("msg", ResponseCode.NEED_LOGIN.getDesc());
            resultMap.put("status", ResponseCode.NEED_LOGIN.getCode());
            out.print(JacksonUtil.ObjToString(resultMap));
            out.flush();
            out.close();
            return false;
        }
        if (Permission.REQUEST_LOGIN.equals(requestLogin.desc())) {
            request.setAttribute("user", user);
            return true;
        } else if (Permission.REQUEST_ADMIN.equals(requestLogin.desc()) && (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            PrintWriter out = response.getWriter();
            Map resultMap = Maps.newHashMap();
            resultMap.put("success", false);
            resultMap.put("msg", ResponseCode.PERMISSION_DENIED.getDesc());
            resultMap.put("status", ResponseCode.PERMISSION_DENIED.getCode());
            out.print(JacksonUtil.ObjToString(resultMap));
            out.flush();
            out.close();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
