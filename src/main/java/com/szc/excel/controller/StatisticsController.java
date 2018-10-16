package com.szc.excel.controller;

import com.szc.excel.domain.Archives2;
import com.szc.excel.domain.Statistics;
import com.szc.excel.domain.TypeStatistics;
import com.szc.excel.mapper.Archives2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 统计信息的操作
 */
@RestController
@RequestMapping("/Statistics")
public class StatisticsController {

    @Value("${page.constant}")
    private Integer constant;
    @Autowired
    private Archives2Mapper archives2Mapper;

    //跑马灯提示
    /**
     * 查询库中secretDate不为""的字段,且过了年限的组成跑马灯字符串.
     */

    @RequestMapping(value = "/slide")
    public HashMap<String,Object> slide() {
        HashMap<String, Object> result = new HashMap<>();
        try{
            //select * from archives_2 where TO_DAYS(DATE_ADD(date,INTERVAL CONCAT(secret_date) YEAR)) -TO_DAYS(now())<=0;
            List<Archives2> archives2s = archives2Mapper.selectExpiredData();
            StringBuffer expiredDescription  = new StringBuffer();
            int count=0;

            if(archives2s.size()>0){
                for(Archives2 archives2:archives2s){
                    count++;
                    expiredDescription.append("《"+archives2.getTitle()+"》、");
                }
                expiredDescription.append("等"+count+"份文件已到销毁期，请及时处理！");
            }else{
                expiredDescription.append("暂无文件到销毁期");
            }
            result.put("data",expiredDescription.toString());
            result.put("status","true");
            result.put("msg","获取数据正常！");
        }catch (Exception e){
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }

    //总份数以及各类型份数的统计
    /**
     * 查询总份数,总卷数  以及group个类型文件 的总卷数总份数
     */

    @RequestMapping(value = "/statistics")
    public HashMap<String,Object> statistics(@RequestParam(value = "start",required = false) String start,
                                             @RequestParam(value = "end",required = false) String end) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String,Object> sel = new HashMap<>();
        try{
            if(start!=null&&!"".equals(start)){
                sel.put("start",start);
            }
            if(end!=null&&!"".equals(end)){
                sel.put("end",end);
            }
            Statistics statistics;
            statistics=  archives2Mapper.selectStatistics(sel);
            List<TypeStatistics> typeStatistics =  archives2Mapper.selectTypeStatistics(sel);
            for(int i=0;i<typeStatistics.size();i++ ){
                if(typeStatistics.get(i).getFileType()!=""){

                }
            }
            statistics.setTypeStatistics(typeStatistics);
            Integer rollCount = 0;
            for(TypeStatistics t:typeStatistics){
                rollCount +=t.getRollCount();
            }
            statistics.setRollCount(rollCount);

            result.put("data",statistics);
            result.put("status","true");
            result.put("msg","获取统计数据正常！");

        }catch (Exception e){
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }
}
