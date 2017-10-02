package com.jala.architect.task;

import com.jala.service.DataCleaningService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 后台管理系统定时任务执行类
 * @author ww
 * @date 2017/9/14
 */
@Component
public class SystemScheduledTask {

    private Log log = LogFactory.getLog(SystemScheduledTask.class);

    @Autowired
    private DataCleaningService dataCleaningService;


    /**
     * 定时执行用户访问量，数据清洗，每天凌晨3点执行一次
     */
    //@Scheduled(cron = "0/10 * * * * ?") // 每10秒执行一次
    @Scheduled(cron = "0 0 3 * * ?")   //  每天23点执行
    public void executeDataCleanScheduler() {
        log.info(">>>>>>>>>>>>> 定时执行用户访问量数据清洗... ... ");
        try {
            dataCleaningService.insertDataCleanBatchByLogin();
        } catch (Exception e) {
            log.error("用户访问量数据清洗异常", e);
        }
    }
}
