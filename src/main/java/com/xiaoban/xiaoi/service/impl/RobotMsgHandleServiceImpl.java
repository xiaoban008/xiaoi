package com.xiaoban.xiaoi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xiaoban.xiaoi.model.TentcentChatResult;
import com.xiaoban.xiaoi.service.RobotMsgHandleService;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaoban
 * @version 1.0.0
 * @create 2021/04/13 - 14:05
 */
@Service
public class RobotMsgHandleServiceImpl implements RobotMsgHandleService {
    /**
     * 处理
     *
     * @param apiResponse api的响应
     * @return {@link String}
     */
    @Override
    public String handle(String apiResponse) {
        String name = "张三";
        String name1 = "李四";
        Date birthday = new Date();
        apiResponse = handleBirthday(apiResponse,birthday);
        return apiResponse.replace(name,name1);
    }

    @Override
    public <T> T handle(TentcentChatResult<T> result,Class<T> tClass) {
        if (result == null || result.getData()==null) {
            return null;
        }
        T data = result.getData();
        String json = JSON.toJSONString(data);
        json = handle(json);
        data = JSON.parseObject(json,tClass);
        return data;
    }

    /**
     * 生日处理
     *
     * @param msg      味精
     * @param birthday 生日
     * @return {@link String}
     */
    public static String handleBirthday(String msg, Date birthday) {
        // 清除掉所有特殊字符(除了~之外)
        String regEx="[0-9]{4}年[0-9]*月";
        Pattern pattern   =   Pattern.compile(regEx);
        Matcher matcher   =   pattern.matcher(msg);
       if (matcher.find()){
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
           String format = sdf.format(birthday);
           msg = matcher.replaceAll(format).trim();
       }
        regEx = "[0-9]*(岁)";
        pattern   =   Pattern.compile(regEx);
        matcher   =   pattern.matcher(msg);
        if (!matcher.find()){
            return msg;
        }
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar calendarOld =  Calendar.getInstance();
        calendarOld.setTime(birthday);
        int year = calendar.get(Calendar.YEAR)-calendarOld.get(Calendar.YEAR);
        String ageStr = "";
        if (year>0){
            ageStr += year+"岁";
        }
        int month = calendar.get(Calendar.MONTH)-calendarOld.get(Calendar.MONTH);
        System.out.println(month);
        if (year>0){
            ageStr += month+"个月";
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH)-calendarOld.get(Calendar.DAY_OF_MONTH);
        if(day>0){
            ageStr += day+"天";
        }else if ("".equals(ageStr)){
            ageStr = ""+(calendar.get(Calendar.HOUR_OF_DAY)-calendarOld.get(Calendar.HOUR_OF_DAY));
            ageStr +="小时";
            ageStr +=""+(calendar.get(Calendar.MINUTE)-calendarOld.get(Calendar.MINUTE));
            ageStr +="分钟";
        }
        return   matcher.replaceAll(ageStr).trim();
    }

    public static void main(String[] args) {
        RobotMsgHandleService robotMsgHandleService = new RobotMsgHandleServiceImpl();
        String cot = "在2021年4月，天降祥瑞，一位伟大的帅哥诞生了，现在21岁。";
        System.out.println(robotMsgHandleService.handle(cot));
    }

}
