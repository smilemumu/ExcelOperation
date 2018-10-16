package com.szc.excel.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: szc
 * 数据库备份与还原
 * @Date: 2018/4/28 19:56
 */
public class DBBackup {

    /**
     * 备份数据库db
     * @param root
     * @param pwd
     * @param dbName
     * @param backPath
     * @param backName
     */
    public static void dbBackUp(String root,String pwd,String dbName,String backPath,String backName) throws IOException {
        String pathSql = backPath+backName;
        File fileSql = new File(pathSql);
        //创建备份sql文件
        if (!fileSql.exists()){
            fileSql.createNewFile();
        }
        //mysqldump -hlocalhost -uroot -p123456 db > /home/back.sql
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" -h127.0.0.1");
        sb.append(" -u"+root);
        sb.append(" -p"+pwd);
        sb.append(" "+dbName+" >");
        sb.append(pathSql);
        System.out.println("cmd命令为："+sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("开始备份："+dbName);
        Process process = runtime.exec("cmd /c"+sb.toString());
        System.out.println("备份成功!");
    }

    /**
     * 恢复数据库
     * @param root
     * @param pwd
     * @param dbName
     * @param filePath
     * mysql -hlocalhost -uroot -p123456 db < /home/back.sql
     */
    public static boolean dbRestore(String root,String pwd,String dbName,String filePath){
        StringBuilder sb = new StringBuilder();
        sb.append("mysql");
        sb.append(" -h127.0.0.1");
        sb.append(" -u"+root);
        sb.append(" -p"+pwd);
        sb.append(" "+dbName+" <");
        //判断文件有没有没有就返回无备份数据，直接返回错误
        File f = new File(filePath);
        if(f.exists()){
            sb.append(filePath);
            System.out.println("cmd命令为："+sb.toString());
            Runtime runtime = Runtime.getRuntime();
            System.out.println("开始还原数据");
            try {
                Process process = runtime.exec("cmd /c"+sb.toString());
                InputStream is = process.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is,"utf8"));
                String line = null;
                while ((line=bf.readLine())!=null){
                    System.out.println(line);
                }
                is.close();
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("还原成功！");
            return true;
        }else{
            System.out.println("还原失败！");
            return false;
        }
    }



    public static void main(String[] args) throws Exception {
        String backName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
        System.out.println(backName);
        DBBackup.dbBackUp("root","123456","test","d:/",backName);
        dbRestore("root","123456","test","d://2018-08-30d.sql");
    }

}