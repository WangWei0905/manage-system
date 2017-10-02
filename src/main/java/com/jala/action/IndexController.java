package com.jala.action;

import com.jala.service.DataCleaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 主页Controller
 *
 * @author ww
 * @date 2017/7/6
 */
@Controller
@RequestMapping("main")
public class IndexController extends BasicController {

    @Autowired
    private DataCleaningService dataCleaningService;


    /**
     *跳转到主页
     * @return
     */
    @RequestMapping("/index.do")
    public String toIndexPage() {
        return "main/index";
    }

    /**
     * 跳转到欢迎页
     * @return
     */
    @RequestMapping("/home.do")
    public String toHomePage() {
        return "main/home";
    }

    /**
     * 跳转到权限不足页面
     * @return
     */
    @RequestMapping("/unauthorized.do")
    public String toUnauthorizedPage() {
        return "error/unauthorized";
    }

    /**
     * 网站访问量,图表展示
     * @return
     */
    @RequestMapping("/ajax_echarts_login_info.do")
    @ResponseBody
    public Map<String, Object> ajaxEchartsByLoginInfo() {
        return dataCleaningService.selectEchartsByLoginInfo();
    }


}
