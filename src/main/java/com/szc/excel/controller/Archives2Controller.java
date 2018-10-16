package com.szc.excel.controller;


import com.szc.excel.domain.Archives2;
import com.szc.excel.mapper.Archives2Mapper;
import com.szc.excel.util.DateUtil;
import com.szc.excel.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  表格的一些操作
 * @author shige
 * @version 1.0
 *
 */

@RestController
@RequestMapping("/Archives")
public class Archives2Controller {


    @Value("${page.constant}")
    private Integer constant;

    @Value("${permission.is.effect}")
    private Boolean effect;

    @Autowired
    private Archives2Mapper archives2Mapper;




    @RequestMapping("/search")
    public HashMap<String,Object> search(@RequestParam(value = "virtualId",required = false) String virtualId,
                                         @RequestParam(value = "id",required = false) String id,
                                         @RequestParam(value = "fileId",required = false) String fileId,
                                         @RequestParam(value = "responsePerson",required = false) String responsePerson,
                                         @RequestParam(value = "title",required = false) String title,
                                         @RequestParam(value = "level",required = false) String level,
                                         @RequestParam(value = "start",required = false) String start,
                                         @RequestParam(value = "end",required = false) String end,
                                         @RequestParam(value = "summary",required = false) String summary,
                                         @RequestParam(value = "note",required = false) String note,
                                         @RequestParam(value = "fileType",required = false) String fileType,
                                         @RequestParam(value = "secretDate",required = false) String secretDate,
                                         @RequestParam(value = "fileColor",required = false) String fileColor,
                                         @RequestParam(value = "concatField",required = false) String concatField,
                                         @RequestParam(value = "page",required = false) Integer page){
        HashMap<String, Object> result = new HashMap<>();
        try {
            HashMap<String, Object> sel = new HashMap<>();
            if(page == null) page = 1;
            if(virtualId!=null&&!"".equals(virtualId)){
                Pattern pattern = Pattern.compile("^[0-9]*$");
                Matcher matcher = pattern.matcher(virtualId);
                if(matcher.matches())
                sel.put("virtualId", virtualId);
            }

            if(id!=null&&!"".equals(id))
            sel.put("id", id+"%");
            if(fileId!=null&&!"".equals(fileId))
            sel.put("fileId", fileId+"%");
            System.out.println("responsePerson:"+responsePerson);
            if(responsePerson!=null&&!"".equals(responsePerson))
            sel.put("responsePerson","%"+responsePerson+"%");
            if(title!=null&&!"".equals(title))
            sel.put("title",title+"%");
            if(level!=null&&!"".equals(level))
            sel.put("level", level);
            if(start!=null&&!"".equals(start))
            sel.put("start", start);
            if(end!=null&&!"".equals(end))
            sel.put("end", end);
            if(summary!=null&&!"".equals(summary))
            sel.put("summary", summary);
            if(note!=null&&!"".equals(note))
            sel.put("note", note);

            if(fileType!=null&&!"".equals(fileType))
            sel.put("fileType",fileType);
            if(secretDate!=null&&!"".equals(secretDate))
            sel.put("secretDate",secretDate);
            if(fileColor!=null&&!"".equals(fileColor))
                sel.put("fileColor",fileColor);
            if(!(concatField==null||"".equals(concatField)))
            sel.put("concatField","%"+concatField+"%");
            sel.put("page", (page-1)*this.constant);
            sel.put("constant", this.constant);
            //获取结果
            List<Archives2> archives2 = archives2Mapper.selectByColumSelective(sel);
            List<Archives2> colorResult =   changeColor(archives2);
            int count = archives2Mapper.selectByColumSelectiveCount(sel);

            //分页
            int pageCount;
            if(count%this.constant==0){
                pageCount = count/this.constant;
            }else{
                pageCount = count/this.constant+1;
            }

            result.put("data", colorResult);
            result.put("count", count);
            result.put("page", page);
            result.put("pageCount", pageCount);
            result.put("status", "true");
            result.put("msg","搜索成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }

    }

    private List<Archives2> changeColor(List<Archives2> archives2) {
        List<Archives2> list = new ArrayList<>();
        for(Archives2 archives:archives2){
            if(null!=archives.getSecretDate()&&!"".equals(archives.getSecretDate())){
                if (!"".equals(archives.getSecretDate())) {
                    Pattern pattern = Pattern.compile("^[0-9]*$");
                    Matcher matcher = pattern.matcher(archives.getSecretDate());
                    if(matcher.matches()){
                        if (DateUtil.isExpired(DateUtil.dateAddYear(Integer.parseInt(archives.getSecretDate()), archives.getDate())))
                            archives.setFileColor("red");
                        else {
                            archives.setFileColor("black");
                        }
                    }
                } else {
                    archives.setFileColor("black");
                }
                if ("借阅".equals(archives.getNote())) archives.setFileColor("yellow");
                else if ("销毁".equals(archives.getNote())) archives.setFileColor("green");
             }else {
                archives.setFileColor("black");
            }
            list.add(archives);
        }
        return list;
    }


    /**
     *
     * @param id    档案号
     * @param fileId    //文号
     * @param responsePerson   //责任人
     * @param title    //题名
     * @param level    //密级  绝密,机密,普通
     * @param date    //文件日期
     * @param summary  //暂时不填入,新给的表格没有这个字段
     * @param note    //备注   借阅=yellow,销毁=green,其他
     * @param fileType  //文件类型 包含5种  县级文件,内部传真,机要文件,密码电报,县局文件
     * @param secretDate  //保密年限(1,2,3)年
     * @param pageCount   //文件页数
     * @return
     */
    @RequestMapping("/insert")
    public HashMap<String,Object> insert(
            @RequestParam(value = "id",required = false) String id,
            @RequestParam(value = "fileId",required = false) String fileId,
            @RequestParam(value = "rollId",required = false) String rollId,
            @RequestParam(value = "responsePerson",required = false) String responsePerson,
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "level",required = false) String level,
            @RequestParam(value = "date",required = false) String date,
            @RequestParam(value = "summary",required = false) String summary,
            @RequestParam(value = "note",required = false) String note,
            @RequestParam(value = "fileType",required = false) String fileType,
            @RequestParam(value = "secretDate",required = false) String secretDate,
            @RequestParam(value = "pageCount",required = false )Integer pageCount,
                                            HttpServletRequest request, HttpServletResponse response){
        HttpSession session = (HttpSession)request.getSession();
        HashMap<String, Object> result = new HashMap<>();
            try {
                if(effect&&!"insert".equals(session.getAttribute("insert"))){
                //TODO 权限
//                if(false){
                    result.put("status", "false");
                    result.put("msg","没有权限，请联系管理员!");
                }else {
                    String flag = "true";
                    /**     模拟的数据
                     * for(int  i =1500;i<2000;i++){
                     *                 Archives2 archives2 = new Archives2();
                     *                 archives2.setDate("2018-08-04 12:12:00");
                     *                 archives2.setFileColor("yellow");
                     *                 archives2.setFileId("F"+i);
                     *                 archives2.setFileType("县级文件");
                     *                 archives2.setFileType("内部传真");
                     *                 archives2.setFileType("机要文件");
                     *                 archives2.setFileType("密码电报");
                     *
                     *                 archives2.setLevel("一般");
                     *                 archives2.setTitle("title4");
                     *                 archives2.setNote("note4");
                     *                 archives2.setPageCount(5);
                     *                 archives2.setResponsePerson("责任人4");
                     *                 archives2.setSecretDate("");
                     *                 archives2.setSummary("summary4");
                     *                 archives2Mapper.insertSelective(archives2);
                     *             }
                     */


                    Archives2 archives2 = new Archives2();
                    archives2.setId(id);
                    archives2.setDate(date);
                    archives2.setFileId(fileId);
                    archives2.setRollId(rollId);
                    archives2.setFileType(fileType);
                    archives2.setLevel(level);
                    archives2.setTitle(title);
                    archives2.setNote(note);
                    archives2.setPageCount(pageCount);
                    archives2.setResponsePerson(responsePerson);
                    archives2.setSecretDate(secretDate);
                    archives2.setSummary(summary);

                   if(secretDate!=null&&!"".equals(secretDate)){
                       Pattern pattern = Pattern.compile("^[0-9]*$");
                       Matcher matcher = pattern.matcher(secretDate);
                       if(matcher.matches()){
                          if(Integer.parseInt(secretDate)==0){
                              archives2.setSecretDate("无保密年限");
                          }else if(Integer.parseInt(secretDate)==-1){
                               archives2.setSecretDate("长期");
                          }
                       }
                           int changes = archives2Mapper.insertSelective(archives2);
                           if (changes <= 0) {
                               flag = "false";
                           }
                           result.put("status", flag);
                           result.put("msg", "插入成功!");
//                       }
                   }else{
                       result.put("status", false+"");
                       result.put("msg", "保密年限不能为空!");
                   }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("status","false");
                result.put("msg","未知异常,请联系管理员!");
            }finally {
                return result;
            }

    }

    @RequestMapping("/update")
    public HashMap<String,Object> update(@RequestParam(value = "virtualId",required = true) Integer virtualId,
                                         @RequestParam(value = "id",required = false) String id,
                                         @RequestParam(value = "fileId",required = false) String fileId,
                                         @RequestParam(value = "rollId",required = false) String rollId,
                                         @RequestParam(value = "responsePerson",required = false) String responsePerson,
                                         @RequestParam(value = "title",required = false) String title,
                                         @RequestParam(value = "level",required = false) String level,
                                         @RequestParam(value = "date",required = false) String date,
                                         @RequestParam(value = "summary",required = false) String summary,
                                         @RequestParam(value = "note",required = false) String note,
                                         @RequestParam(value = "fileType",required = false) String fileType,
                                         @RequestParam(value = "secretDate",required = false) String secretDate,
                                         @RequestParam(value = "fileColor",required = false) String fileColor,
                                         @RequestParam(value = "pageCount",required = false) Integer pageCount,
                                         HttpServletRequest request, HttpServletResponse response){
        String flag = "true";
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = (HttpSession)request.getSession();
        try {
            if (effect&&!"update".equals(session.getAttribute("update"))) {
            //TODO 权限
//            if(false){
                result.put("status", "false");
                result.put("msg", "没有权限，请联系管理员!");
            }else{
                Archives2 archives2 = new Archives2();
                archives2.setId(id);
                archives2.setVirtualId(virtualId);
                archives2.setDate(date);
                archives2.setFileColor(fileColor);
                archives2.setFileId(fileId);
                archives2.setRollId(rollId);
                archives2.setFileType(fileType);
                archives2.setLevel(level);
                archives2.setTitle(title);
                archives2.setNote(note);
                archives2.setPageCount(pageCount);
                archives2.setResponsePerson(responsePerson);
                archives2.setSecretDate(secretDate);
                archives2.setSummary(summary);

                if("借阅".equals(note)) archives2.setFileColor("yellow");
                else if("销毁".equals(note)) archives2.setFileColor("green");

                if(secretDate!=null){
                    Pattern pattern = Pattern.compile("^[0-9]*$");
                    Matcher matcher = pattern.matcher(secretDate);
                    if(matcher.matches()){
                        if(Integer.parseInt(secretDate)>=0){
                            int changes = archives2Mapper.updateByPrimaryKeySelective(archives2);
                            if(changes<=0){
                                flag = "false";
                            }
                            result.put("status", flag);
                            result.put("msg",flag.equals("true")?"更新成功!":"更新失败!"    );
                        }else{
                            result.put("status", "false");
                            result.put("msg","更新失败,保密年限不能为负数!");
                        }
                    }else{
                        int changes = archives2Mapper.updateByPrimaryKeySelective(archives2);
                        if(changes<=0){
                            flag = "false";
                        }
                        result.put("status", flag);
                        result.put("msg",flag.equals("true")?"更新成功!":"更新失败!"    );
                    }
                }else{
                    int changes = archives2Mapper.updateByPrimaryKeySelective(archives2);
                    if(changes<=0){
                        flag = "false";
                    }
                    result.put("status", flag);
                    result.put("msg",flag.equals("true")?"更新成功!":"更新失败!"    );
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }
        return result;
    }

    @RequestMapping("/hardDelete")
    public HashMap<String,Object> hardDelete(
            @RequestParam(value = "virtualId",required = false) Integer[] virtualId,
            HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = (HttpSession)request.getSession();
        String flag = "true";
        try {
            if (effect&&!"delete".equals(session.getAttribute("delete"))) {
            //TODO 权限
//            if(false){
                result.put("status", "false");
                result.put("msg", "没有权限，请联系管理员!");
            }else{
               for(Integer vid:virtualId){
                   int change =  archives2Mapper.deleteByPrimaryKey(vid);
                   if(change<=0) flag ="false";
               }
                result.put("status", flag);
                result.put("msg",flag.equals("true")?"硬删除成功!":"硬删除失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }

    @RequestMapping("/softDelete")
    public HashMap<String,Object> softDelete(
            @RequestParam(value = "virtualId",required = true) Integer[] virtualId,
            HttpServletRequest request, HttpServletResponse response){
        HttpSession session = (HttpSession)request.getSession();
        HashMap<String, Object> result = new HashMap<>();
        String flag = "true";
        try {
            if (effect&&!"delete".equals(session.getAttribute("delete"))) {
            //TODO 权限
//            if(false){
                result.put("status", "false");
                result.put("msg", "没有权限，请联系管理员!");
            }else{
                for(Integer vid:virtualId){
                    Archives2 archives2 = new Archives2();
                    archives2.setVirtualId(vid);
                    archives2.setFileColor("green");
                    int change = archives2Mapper.updateByPrimaryKeySelective(archives2);
                    if(change<=0) flag ="false";
                }
                result.put("status", flag);
                result.put("msg",flag.equals("true")?"软删除成功(修改文件颜色)!":"软删除（修改文件颜色）失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }


    @RequestMapping("/importExcel")
    public HashMap<String,Object> importExcel(@RequestParam(value = "file",required = true) MultipartFile file,
                                              @RequestParam(value = "fileType",required = true) String fileType,
                                              HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = (HttpSession)request.getSession();
        int errorLine =0;
      try{
          if (effect&&!"insert".equals(session.getAttribute("insert"))) {
          //TODO 权限
//          if(false){
              result.put("status", "false");
              result.put("msg", "没有权限，请联系管理员!");
          }else{
              if(!file.isEmpty()){
                  try {
                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                      String fileName = file.getOriginalFilename();
                      InputStream is = file.getInputStream();//转化为流的形式
                      List<Archives2> archives2 = new ArrayList<Archives2>();
                      List<Row> list = ExcelUtil.getExcelRead(fileName,is, true);
                      //首先是读取行 也就是一行一行读，然后在取到列，遍历行里面的行，根据行得到列的值

                      for (Row row : list) {
                          errorLine++;
                          /****************得到每个元素的值start**********************/
//                    Cell cell_0 = row.getCell(0);//序号
                          Cell cell_0 = row.getCell(0);//档号
                          Cell cell_1 = row.getCell(1);//文号
                          Cell cell_2 = row.getCell(2);//卷号
                          Cell cell_3 = row.getCell(3);//责任者
                          Cell cell_4 = row.getCell(4);//题名
                          Cell cell_5 = row.getCell(5);//日期
                          Cell cell_6= row.getCell(6);//密级
                          Cell cell_7 = row.getCell(7);//页数
                          Cell cell_8 = row.getCell(8);//备注
                          Cell cell_9 = row.getCell(9);//保密年限
                          System.out.println(cell_5.getCellTypeEnum());
                          System.out.println(cell_9.getCellTypeEnum());
                          if(ExcelUtil.getValue(cell_0).equals("序号")){
                              continue;
                          }
                          //如果有空格行,则自动跳过空白行
                          if ((ExcelUtil.getValue(cell_0)==null||(ExcelUtil.getValue(cell_0).length() == 0) && (ExcelUtil.getValue(cell_1)==null||ExcelUtil.getValue(cell_1).length() == 0)))
                              continue;
                          /*****************得到每个元素的值end**********************/
                          /******************解析每个元素的值start*******************/
                          //得到列的值，也就是你需要解析的字段的值
//                    Integer virtualId = Integer.parseInt(ExcelUtil.getValue(cell_0));
                          String id = ExcelUtil.getValue(cell_0);
                          String  fileId = ExcelUtil.getValue(cell_1);
                          String  rollId = ExcelUtil.getValue(cell_2);
                          String  responsePerson = ExcelUtil.getValue(cell_3);
                          String  title =  ExcelUtil.getValue(cell_4);
                          String  date = "";
                          System.out.println(cell_5.getCellTypeEnum());
                          if(CellType.NUMERIC.equals(cell_5.getCellTypeEnum()))
                              date   = ExcelUtil.getValue(cell_5);
                          else if(CellType.STRING.equals(cell_5.getCellTypeEnum()))
                              date =  ExcelUtil.getValue(cell_5);
                          String  level = ExcelUtil.getValue(cell_6);
                          Integer  pageCount = Integer.parseInt(ExcelUtil.getValue(cell_7));
                          String  note = ExcelUtil.getValue(cell_8);
                          String  secretDate =ExcelUtil.getValue(cell_9);
                          /******************解析每个元素的值end*******************/
                          /****************将读取出来的数值进行包装start***********/
//	                    System.out.println(new Date(Long.parseLong((Double.parseDouble(date) - 25569) * 24*60*60)+""));
                          Archives2 archive2 = new Archives2();
//                    archive2.setVirtualId(virtualId);
                          archive2.setId(id);
                          archive2.setFileId(fileId);
                          archive2.setRollId(rollId);
                          archive2.setResponsePerson(responsePerson);
                          archive2.setTitle(title);

                          if(date.contains("-")){
                              archive2.setDate(date);
                          }else{
                              archive2.setDate(parseDate(date));
                          }

                          archive2.setLevel(level);
                          archive2.setPageCount(pageCount);
                          archive2.setNote(note);
                          archive2.setFileType(fileType);
                          archive2.setSecretDate(secretDate);
                          if(!"".equals(secretDate)){
                              if(DateUtil.isExpired(DateUtil.dateAddYear(Integer.parseInt(archive2.getSecretDate()),archive2.getDate())))
                                  archive2.setFileColor("red");
                              else{
                                  archive2.setFileColor("black");
                              }
                          }else{
                              archive2.setFileColor("black");
                          }

                          if("借阅".equals(note)) archive2.setFileColor("yellow");
                          else if("销毁".equals(note)) archive2.setFileColor("green");
                          archives2.add(archive2);




                          /**************将读取出来的数值进行包装end**************/
                      }
                      if(archives2.size()>0){
                          for(Archives2 item:archives2){
                              archives2Mapper.insert(item);
                          }
                      }
                      result.put("status", "true");
                      result.put("msg","导入成功!");
                  }catch (Exception e){
                      e.printStackTrace();
                      result.put("status", "false");
                      result.put("msg","导入失败!"+"请检查数据第"+errorLine+"行");
                  }
              }else{
                  result.put("status", "false");
                  result.put("msg","文件内容为空!");
              }
          }
      }catch (Exception e){
          e.printStackTrace();
          result.put("status","false");
          result.put("msg","未知异常,请联系管理员!");
      }finally {
          return  result;
      }
    }

    /**
     * 获取excel的方法
     * @param list
     * @param headers
     * @param fileName
     * @param response
     */
    public  void getExcel(List<Archives2> list,List<String> headers,String fileName, HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheetl");
        //设置列宽
        sheet.setDefaultColumnWidth((short) 18);
        //创建第一行的对象，第一行一般用于填充标题内容。从第二行开始一般是数据
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.size(); i++) {
            //创建单元格，每行多少数据就创建多少个单元格
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
            //给单元格设置内容
            cell.setCellValue(text);
        }

        //遍历集合，将每个集合元素对象的每个值填充到单元格中
        for(int i=0;i<list.size();i++){
            Archives2  archive2 =list.get(i);
            List<String> datas = new ArrayList<>();
            //从第二行开始填充数据
            row = sheet.createRow(i+1);
            datas.add(archive2.getVirtualId()+"");
            datas.add(archive2.getId());
            datas.add(archive2.getFileId());
            datas.add(archive2.getRollId());
            datas.add(archive2.getResponsePerson());
            datas.add(archive2.getTitle());
            datas.add(archive2.getDate());
            datas.add(archive2.getLevel());
            datas.add(archive2.getPageCount()+"");
            datas.add(archive2.getNote());
            datas.add(archive2.getSecretDate());
            for (int j=0;j<datas.size();j++) {
                String string=datas.get(j);
                HSSFCell cell = row.createCell(j);
                HSSFRichTextString richString = new HSSFRichTextString(string);
//		               HSSFFont font3 = workbook.createFont();
                //定义Excel数据颜色，这里设置为蓝色
//		               font3.setColor(HSSFColor.BLUE.index);
//		               richString.applyFont(font3);
                cell.setCellValue(richString);
            }
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(fileName.getBytes("gbk"), "iso8859-1") + ".xls");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将workbook中的内容写入输出流中
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //export

    @RequestMapping("/export")
    public void export(@RequestParam(value = "virtualId",required = false) Integer virtualId,
                                         @RequestParam(value = "id",required = false) String id,
                                         @RequestParam(value = "fileId",required = false) String fileId,
                                         @RequestParam(value = "responsePerson",required = false) String responsePerson,
                                         @RequestParam(value = "title",required = false) String title,
                                         @RequestParam(value = "level",required = false) String level,
                                         @RequestParam(value = "start",required = false) String start,
                                         @RequestParam(value = "end",required = false) String end,
                                         @RequestParam(value = "summary",required = false) String summary,
                                         @RequestParam(value = "note",required = false) String note,
                                         @RequestParam(value = "fileType",required = false) String fileType,
                                         @RequestParam(value = "secretDate",required = false) String secretDate,
                                         @RequestParam(value = "fileColor",required = false) String fileColor,
                                         @RequestParam(value = "concatField",required = false) String concatField,
                       HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> result = new HashMap<>();
        try {
            HashMap<String, Object> sel = new HashMap<>();
            if(virtualId!=null&&!"".equals(virtualId)){
                Pattern pattern = Pattern.compile("^[0-9]*$");
                Matcher matcher = pattern.matcher(virtualId.toString());
                if(matcher.matches())
                    sel.put("virtualId", virtualId);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(id!=null&&!"".equals(id))
                sel.put("id", id+"%");
            if(fileId!=null&&!"".equals(fileId))
                sel.put("fileId", fileId+"%");
            System.out.println("responsePerson:"+responsePerson);
            if(responsePerson!=null&&!"".equals(responsePerson))
                sel.put("responsePerson","%"+responsePerson+"%");
            if(title!=null&&!"".equals(title))
                sel.put("title",title+"%");
            if(level!=null&&!"".equals(level))
                sel.put("level", level);
            if(start!=null&&!"".equals(start))
                sel.put("start", start);
            if(end!=null&&!"".equals(end))
                sel.put("end", end);
            if(summary!=null&&!"".equals(summary))
                sel.put("summary", summary);
            if(note!=null&&!"".equals(note))
                sel.put("note", note);

            if(fileType!=null&&!"".equals(fileType))
                sel.put("fileType",fileType);
            if(secretDate!=null&&!"".equals(secretDate))
                sel.put("secretDate",secretDate);
            if(fileColor!=null&&!"".equals(fileColor))
                sel.put("fileColor",fileColor);
            if(!(concatField==null||"".equals(concatField)))
                sel.put("concatField","%"+concatField+"%");
                //获取结果
                List<Archives2> archives2 = archives2Mapper.selectByColumSelective(sel);
                List<String> headers = Arrays.asList(new String[]{"序号","档号","文号","卷号","责任者","题名","日期","密级","页数","备注","保密年限"});
                String fileName =  fileType+"_"+sdf.format(new Date())+"_导出文件";
                getExcel(archives2,headers,fileName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
    }

    public static String parseDate(String date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate= formatter.parse(date);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate); }
}
