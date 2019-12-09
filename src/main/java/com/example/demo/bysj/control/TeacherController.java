package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.Teacher;
import com.example.demo.bysj.service.TeacherService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class TeacherController {
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseTeachers();
            } else {
                return responseTeacher(Integer.parseInt(id_str));
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            return message.toString();
        }
    }



    //响应一个学院对象
    private String responseTeacher(int id)
            throws SQLException {
        //根据id查找学院
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);

        //响应teacher_json到前端
        return teacher_json;
    }

    //响应所有学院对象
    private String responseTeachers()
            throws SQLException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);

        //响应teachers_json到前端
        return teachers_json;
    }


    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //在数据库表中增加Teacher对象
        try {
            boolean added = TeacherService.getInstance().add(teacherToAdd);
            if (added) {
                message.put("message", "增加成功");
            }else {
                message.put("message", "已被增加");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.DELETE)
    private String Delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            boolean deleted = TeacherService.getInstance().delete(Integer.parseInt(id_str));
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

    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //到数据库表修改Teacher对象对应的记录
        try {
            boolean puted = TeacherService.getInstance().update(teacherToAdd);
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
}
