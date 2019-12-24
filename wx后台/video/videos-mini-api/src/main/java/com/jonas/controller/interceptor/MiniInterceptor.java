package com.jonas.controller.interceptor;

import com.jonas.common.utils.JsonUtils;
import com.jonas.common.utils.RedisOperator;
import com.jonas.utils.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author cmj
 * @date 2019-03-25 13:05
 */
public class MiniInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    /**
     * session前缀格式
     */
    public final String USER_REDIS_SESSION="user-redis-session";

    /**
     * 拦截请求，在controller调用之前（拦截器）
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader("userId");
        String userToken = httpServletRequest.getHeader("userToken");

        if (StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            String uniqueToken = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            if (StringUtils.isEmpty(uniqueToken)&&StringUtils.isBlank(uniqueToken)){
                //如果获取到的token为空或者为空字符串，则用户的token过期，需要重新登录
                returnErrorResponse(httpServletResponse,new JSONResult().errorTokenMsg("用户信息过期"));
                return false;
            }else {
//                if (!uniqueToken.equals(userToken)) {
//                    // 如果两个token不一致则说明在不同设备登录
//                    System.out.println(uniqueToken);
//                    System.out.println(userToken);
//                    redisOperator.del(USER_REDIS_SESSION + ":" + userId);
//                    returnErrorResponse(httpServletResponse, new JSONResult().errorTokenMsg("您被强制下线"));
//                    return false;
//                }else {
                    return true;
//                }

            }
        }else {
            returnErrorResponse(httpServletResponse,new JSONResult().errorTokenMsg("请登录"));
            return false;
        }

    }

    /**
     * 在调用controller之后，渲染视图层之前调用
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求结束，视图层渲染完毕之后调用
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse httpServletResponse, JSONResult jsonResult) throws IOException {
        OutputStream outputStream = null;
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("text/json");
        outputStream = httpServletResponse.getOutputStream();
        outputStream.write(JsonUtils.objectToJson(jsonResult).getBytes());
        outputStream.flush();

        if (outputStream!=null){
            outputStream.close();
        }
    }
}
