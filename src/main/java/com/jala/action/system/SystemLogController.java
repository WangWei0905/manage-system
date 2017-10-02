package com.jala.action.system;

import com.jala.action.BasicController;
import com.jala.architect.utils.CommonHelper;
import com.jala.architect.utils.DateUtil;
import com.jala.model.bo.ExcelExport;
import com.jala.model.vo.SystemLog;
import com.jala.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


/**
 * 日志管理Controller
 *
 * @author ww
 * @date 2017/9/8
 */
@Controller
@RequestMapping("syslog")
public class SystemLogController extends BasicController {


    @Autowired
    private SystemLogService systemLogService;


    /**
     *跳转到日志管理页面
     * @return
     */
    @RequestMapping("/sys_log_list.do")
    public String toSysLogPage(Model model) {
        String currentDate = DateUtil.Date2Stirng(new Date());
        model.addAttribute("currentDate",currentDate);
        return "system/sys_log_list";
    }
    /**
     * 日志信息列表List
     * @param systemLog 日志实体
     * @return
     */
    @RequestMapping("/ajax_sys_log_list.do")
    @ResponseBody
    public String ajaxSysLogList(SystemLog systemLog){
        return systemLogService.selectSystemLogResultPageList(systemLog);
    }

    /**
     * 业务日志导出
     * @param systemLog 日志实体
     * @return
     */
    @RequestMapping("/excel_sys_log_export.do")
    public ModelAndView excelSysLogExport(SystemLog systemLog){
        ExcelExport excelExport = systemLogService.excelExportSystemLogList(systemLog);
        return CommonHelper.getExcelModelAndView(excelExport);
    }

    /**
     * 异常日志导出
     * @param systemLog 日志实体
     * @return
     */
    @RequestMapping("/excel_sys_exception_log_export.do")
    public ModelAndView excelSysExceptionLogExport(SystemLog systemLog){
        ExcelExport excelExport = systemLogService.excelExportSysExceptionLogList(systemLog);
        return CommonHelper.getExcelModelAndView(excelExport);
    }



}
