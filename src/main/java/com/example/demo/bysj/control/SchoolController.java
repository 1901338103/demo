package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.School;
import com.example.demo.bysj.service.SchoolService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class SchoolController {
    @RequestMapping(value = "/school.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseSchools();
            } else {
               return responseSchool(Integer.parseInt(id_str));
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
    private String responseSchool(int id)
            throws SQLException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);

        //响应school_json到前端
        return school_json;
    }

    //响应所有学院对象
    private String responseSchools()
            throws SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);

        //响应schools_json到前端
        return schools_json;
    }

    @RequestMapping(value = "/school.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String school_json = JSONUtil.getJSON(request);
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //在数据库表中增加School对象
        try {
            boolean added = SchoolService.getInstance().add(schoolToAdd);
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

    @RequestMapping(value = "/school.ctl",method = RequestMethod.DELETE)
    private String Delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            boolean deleted = SchoolService.getInstance().delete(Integer.parseInt(id_str));
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

    @RequestMapping(value = "/school.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //到数据库表修改School对象对应的记录
        try {
            boolean puted = SchoolService.getInstance().update(schoolToAdd);
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
