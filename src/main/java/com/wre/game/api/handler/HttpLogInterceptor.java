package com.wre.game.api.handler;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.config.RequestExpireConfig;
import com.wre.game.api.data.RequestExpireInfo;
import com.wre.game.api.data.RequestExpireInfos;
import com.wre.game.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

public class HttpLogInterceptor extends HandlerInterceptorAdapter {
    static final Logger log = LoggerFactory.getLogger(HttpLogInterceptor.class);
    static final String STOP_WATCH_NAME = "HTTP_REQ_STOPWATCH";
    @Resource
    private SessionComponent sessionComponent;
    public HttpLogInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("\n\n\n--------------------------------------------------\n").append("Request Information\n")
                    .append("--------------------------------------------------\n")
                    .append("Request URL : " + request.getRequestURL().toString() + "\n")
                    .append("Method : " + request.getMethod() + "\n")
                    .append("HTTP Status : " + response.getStatus() + "\n")
                    .append("Session ID : " + request.getSession().getId() + "\n")
                    .append("\n\nHeaders : ★★★★★★★★★★★★★★★★★★★★★★\n");
            Enumeration headerNames = request.getHeaderNames();

            String paramName;
            while(headerNames.hasMoreElements()) {
                String headerName = (String)headerNames.nextElement();
                paramName = request.getHeader(headerName);
                buffer.append(headerName + " : " + paramName + "\n");
            }

            if (!this.isMultipart(request)) {
                buffer.append("\n\nParameters : ★★★★★★★★★★★★★★★★★★★★★★\n");
                Enumeration parameterNames = request.getParameterNames();

                while(parameterNames.hasMoreElements()) {
                    paramName = (String)parameterNames.nextElement();
                    String[] paramValues = request.getParameterValues(paramName);
                    buffer.append(paramName + " : " + JsonUtil.marshallingJson(paramValues) + "\n");
                }
            }

            buffer.append("--------------------------------------------------\n");
            log.debug(buffer.toString());
        }
        //没有token的放过
        String token = request.getHeader("X-WRE-TOKEN");
        if (token==null){
            return true;
        }
        //请求方式不是post的放过
        String method = request.getMethod();
        if (!method.equals("POST")){
            return true;
        }

        String requestURI = request.getRequestURI();
        RequestExpireInfo requestExpireInfo = RequestExpireConfig.getRequestExpireInfos().getRequestExpireInfoMap().get(requestURI);
        if (requestExpireInfo==null){
            return true;
        }
        boolean requestExprie = sessionComponent.getRequestExprie(requestExpireInfo, token);
        if (requestExprie){
            return true;
        }
        else {
            response.setStatus(500);
            PrintWriter writer = response.getWriter();
            writer.append("Too many requests, please try again later");
            writer.close();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private boolean isMultipart(HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
    }
}