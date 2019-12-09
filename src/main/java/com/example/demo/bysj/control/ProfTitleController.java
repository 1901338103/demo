package com.example.demo.bysj.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.ProfTitle;
import com.example.demo.bysj.service.ProfTitleService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class ProfTitleController {
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseProfTitles();
            } else {
                return responseProfTitle(Integer.parseInt(id_str));
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
    private String responseProfTitle(int id)
            throws SQLException {
        //根据id查找学院
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);

        //响应profTitle_json到前端
        return profTitle_json;
    }

    //响应所有学院对象
    private String responseProfTitles()
            throws SQLException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);

        //响应profTitles_json到前端
        return profTitles_json;
    }

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //在数据库表中增加ProfTitle对象
        try {
            boolean added = ProfTitleService.getInstance().add(profTitleToAdd);
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

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.DELETE)
    private String Delete(@RequestParam(value = "id",required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            boolean deleted = ProfTitleService.getInstance().delete(Integer.parseInt(id_str));
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

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request)throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //到数据库表修改ProfTitle对象对应的记录
        try {
            boolean puted = ProfTitleService.getInstance().update(profTitleToAdd);
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


