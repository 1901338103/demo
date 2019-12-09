package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.UserService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserController {
    @RequestMapping(value = "/login.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str,
                       @RequestParam(value = "username",required = false) String username_str
    ){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str!=null){
                return  responseUser(Integer.parseInt(id_str));
            }else{
                return responseUserByUsername(username_str);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        }
    }

    //响应一个学位对象
    private String responseUser(int id)
            throws SQLException {
        //根据id查找学院
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);

        //响应message到前端
        return user_json;
    }

    //要求服务器响应paraType类的并对应相应Id的所有系
    private String responseUserByUsername(String username) throws SQLException {
        //根据用户名查找账号
        User user = UserService.getInstance().findByUsername(username);
        String user_json = JSON.toJSONString(user);
        //响应
       return user_json;
    }

    @RequestMapping(value = "/user.ctl",method = RequestMethod.PUT)
    public JSONObject put(@RequestParam(value = "id",required = false) String id_str,
                          @RequestParam(value = "password",required = false) String password_str,
                          HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToAdd = JSON.parseObject(user_json, User.class);
        //到数据库表修改User对象对应的记录
        try {
            boolean puted = UserService.getInstance().changePassword(Integer.parseInt(id_str),password_str);
            if (puted) {
                message.put("message", "修改成功");
            }else {
                message.put("message", "已被修改");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    @RequestMapping(value = "/user.ctl",method = RequestMethod.DELETE)
    private String Delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            boolean deleted = UserService.getInstance().delete(Integer.parseInt(id_str));
            if (deleted) {
                message.put("message", "删除成功");
            }else{
                message.put("message", "已被删除");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message.toString();
    }
}
