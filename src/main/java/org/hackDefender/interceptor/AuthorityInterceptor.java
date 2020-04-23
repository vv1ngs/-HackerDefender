package org.hackDefender.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.ServerResponse;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/4/21 11:08
 */
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String method = handlerMethod.getMethod().getName();
        String classname = handlerMethod.getBean().getClass().getSimpleName();
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);

            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisPoolSharedUtil.get(loginToken);
            user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        }
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            if (user == null) {
                out.print(JacksonUtil.ObjToString(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
            } else {
                out.print(JacksonUtil.ObjToString(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
            }
            out.flush();
            out.close();
            return false;
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
