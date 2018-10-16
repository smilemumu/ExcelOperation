package com.szc.excel.schedule;


import com.szc.excel.util.DBBackup;
import com.szc.excel.util.OsUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BackupSchedule {
    /**
     * 每日凌晨一点执行数据库备份
     * 测试cron每分钟执行一次：0 0/1 * * * ?
     * 上线cron每日凌晨一点执行：0 0 0 * * ?
     *
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void backup(){
        try{
            System.out.println("数据库备份开始！");
            String windowsBackPath = "d:/data/";
            String linuxBackPath = "/root/d/";
            File pathFile = new File(windowsBackPath);
            if(!pathFile.exists()){
                pathFile.mkdir();
            }
            String backName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
            if(OsUtil.isOSLinux())
                 DBBackup.dbBackUp("root","123456","archives",linuxBackPath,backName);
            else
                 DBBackup.dbBackUp("root","123456","archives",windowsBackPath,backName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
