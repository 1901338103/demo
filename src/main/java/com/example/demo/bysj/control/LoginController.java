package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(
            @RequestParam(value = "username",required = false) String username,
            @RequestParam(value = "password",required = false) String password,
            HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改User对象对应的记录
        try {
            User loggedUser = UserService.getInstance().login(username,password);
            if (loggedUser!=null) {
                message.put("message", "登录成功");
                HttpSession session = request.getSession();
                //十分钟没有操作，使session失效
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
                response.getWriter().println(message);
                return;

            } else {
                message.put("message","用户名或密码错误");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            e.printStackTrace();
        } catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        //响应到message前端
        response.getWriter().println(message);
    }
}
