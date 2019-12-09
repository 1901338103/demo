package com.example.demo.bysj.Filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 10",urlPatterns = "/*")
public class Filter10 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //轻质类型转换
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        //创建JSON对象message，以便向前端响应信息
        JSONObject message = new JSONObject();
        //获得path
        String path = request.getRequestURI();
        //访问权限验证
        HttpSession session=request.getSession();
        // Object currentUser=session.getAttribute("currentUser");
        if (!path.contains("/login")) {
            if (session == null || session.getAttribute("currentUser") == null) {
                message.put("message", "请登录或者重新登录");
                //响应到message前端
                response.getWriter().println(message);
                return;
            }
        }
        chain.doFilter(req, resp);//执行其他过滤器，如果过滤器已经执行完毕，则执行原请求
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

