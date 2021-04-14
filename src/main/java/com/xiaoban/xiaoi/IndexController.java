package com.xiaoban.xiaoi;

import com.xiaoban.xiaoi.model.SmallTalkDTO;
import com.xiaoban.xiaoi.model.TentcentChatConfig;
import com.xiaoban.xiaoi.model.TentcentChatResult;
import com.xiaoban.xiaoi.service.RobotMsgHandleService;
import com.xiaoban.xiaoi.utils.XiaoIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoban
 * @version 1.0.0
 * @create 2021/04/13 - 10:52
 */
@Controller
public class IndexController {
    private final RobotMsgHandleService robotMsgHandleService;

    public IndexController(RobotMsgHandleService robotMsgHandleService) {
        this.robotMsgHandleService = robotMsgHandleService;
    }

    @GetMapping("")
    public String index() {
        return "mobile";
    }
    @ResponseBody
    @PostMapping("/ask")
    public Map ask(@RequestBody HashMap<String,String> params) {
        String appId = "2160281757";
        String appKey = "HUj9gsE5IEkYEarc";
        TentcentChatConfig ttConfig = new TentcentChatConfig();
        ttConfig.setAppId(appId);
        ttConfig.setAppKey(appKey);
        String question = params.get("question");
        try {
            TentcentChatResult<SmallTalkDTO> result = XiaoIUtil.smallTalk(question, ttConfig);
            if (result != null) {
                Map<String,Object> objectMap = new HashMap<>(2);
                objectMap.put("data",robotMsgHandleService.handle(result,SmallTalkDTO.class));
                return objectMap;
            }
            System.out.println("空响应");
        }catch (Exception e){
            System.out.println("出异常了");
            e.printStackTrace();
        }
        params.put("data","");
        return new HashMap<>();

    }
}
