package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.Department;
import com.example.demo.bysj.service.DepartmentService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class DepartmentController {
    @RequestMapping(value = "/department.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str
    ,@RequestParam(value = "paraType",required = false) String paraType
    ) {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str != null) {
                if (paraType == null) {
                    return responseDepartment(Integer.parseInt(id_str));
                } else if (paraType.equals("school")) {
                    return responseDepartmentBySchool(Integer.parseInt(id_str));
                }
            } else {
                return responseDepartments();
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
        return null;
    }


    //要求服务器响应paraType类的并对应相应Id的所有系
    private String responseDepartmentBySchool(int id) throws SQLException, IOException {
        Collection<Department>departments = DepartmentService.getInstance().findALLBySchool(id);
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应department_json到前端
        return departments_json;
    }
    //响应一个学院对象
    private String responseDepartment(int id)
            throws SQLException {
        //根据id查找学院
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);

        //响应department_json到前端
        return department_json;
    }

    //响应所有学院对象
    private String responseDepartments()
            throws SQLException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);

        //响应departments_json到前端
        return departments_json;
    }


    @RequestMapping(value = "/department.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String department_json = JSONUtil.getJSON(request);
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //在数据库表中增加Department对象
        try {
            boolean added = DepartmentService.getInstance().add(departmentToAdd);
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

    @RequestMapping(value = "/department.ctl",method = RequestMethod.DELETE)
    private String Delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            boolean deleted = DepartmentService.getInstance().delete(Integer.parseInt(id_str));
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

    @RequestMapping(value = "/department.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //到数据库表修改Department对象对应的记录
        try {
            boolean puted = DepartmentService.getInstance().update(departmentToAdd);
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

