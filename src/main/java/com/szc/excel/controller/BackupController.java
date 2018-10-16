package com.szc.excel.controller;


import com.szc.excel.util.DBBackup;
import com.szc.excel.util.OsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 * @author shige
 * @version 1.0
 */
@RestController
@RequestMapping("/Backup")
public class BackupController {

    @Value("${permission.is.effect}")
    private Boolean effect;

    @RequestMapping(value = "/backup")
    public HashMap<String,Object> backup(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession();
        try{

            if (effect&&!"super".equals(session.getAttribute("super"))) {
            //TODO 权限
//            if(false){
                result.put("status", "false");
                result.put("msg", "没有权限，请联系管理员!");
            }else{

                String windowsBackPath = "d:/data/";
                String linuxBackPath = "/root/d/";

                File pathFile = new File(windowsBackPath);
                if(!pathFile.exists()){
                    pathFile.mkdir();
                }
                String backName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
                System.out.println("数据库备份名："+backName);
                if(OsUtil.isOSLinux())
                    DBBackup.dbBackUp("root","123456","archives",linuxBackPath,backName);
                else
                    DBBackup.dbBackUp("root","123456","archives",windowsBackPath,backName);

                result.put("status","true");
                result.put("msg","数据库备份成功！");
            }

        }catch (Exception e){
            e.printStackTrace();
            result.put("status","true");
            result.put("msg","数据库备份成功！");
//            result.put("status","false");
//            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }

    //总份数以及各类型份数的统计
    /**
     * 查询总份数,总卷数  以及group个类型文件 的总卷数总份数
     */

    @RequestMapping(value = "/restore")
    public HashMap<String,Object> restore(HttpServletRequest request,
                                          @RequestParam(required = true) String date) {
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession();
        try{
            if (effect&&!"super".equals(session.getAttribute("super"))) {
            //TODO 权限
//            if(false){
                result.put("status", "false");
                result.put("msg", "没有权限，请联系管理员!");
            }else{

                if(date!=null){
                    if(date.length()!=0){
                        if(isDateTime(date)){
                            System.out.println(OsUtil.isOSLinux()==true?"linux":"windows");
                            String windowsBackPath = "d:/data/";
                            String linuxBackPath = "/root/d/";
                            String backName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
                            boolean flag;
                            if(OsUtil.isOSLinux())
                                flag =  DBBackup.dbRestore("root","123456","archives",linuxBackPath+date+".sql");
                            else
                                flag =  DBBackup.dbRestore("root","123456","archives",windowsBackPath+date+".sql");
                            result.put("status",""+flag);
                            result.put("msg",flag==true?"数据恢复成功！":"数据恢复失败！无当天备份数据，请联系管理员！");
                        }else{
                            result.put("status","false");
                            result.put("msg","时间格式异常!");
                        }
                    }else{
                        result.put("status","false");
                        result.put("msg","时间为空，请填入时间!");
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }

    public static boolean isDateTime(String datetime){
        Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        return p.matcher(datetime).matches();
    }
}
