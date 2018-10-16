package com.szc.excel.controller;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.szc.excel.domain.Archives;
import com.szc.excel.mapper.ArchivesMapper;
import com.szc.excel.util.DateUtil;
import com.szc.excel.util.ExcelUtil;



/**
 * 
 * @author shige
 * @version 1.0
 */
@RestController
@RequestMapping("/Excel")
public class ExcelController {

	
	@Value("${page.constant}")
	private Integer constant;
	@Autowired
	private ArchivesMapper archivesMapper;

	/**
	 * 导入excel中的数据，选择文件上传导入
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importExcel")
	public HashMap<String,Object> importExcel(@RequestParam(value = "file",required = true) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> result = new HashMap<>();
		
		// 将request转换成Spring的request
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		// 获取上传文件
		MultipartFile excel = multipartHttpServletRequest.getFile("file");
		if(!excel.isEmpty()){
			 try {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                String fileName = excel.getOriginalFilename();
	                InputStream is = excel.getInputStream();//转化为流的形式
	                List<Archives> archives = new ArrayList<Archives>();
	                List<Row> list = ExcelUtil.getExcelRead(fileName,is, true);
	                //首先是读取行 也就是一行一行读，然后在取到列，遍历行里面的行，根据行得到列的值
	                for (Row row : list) {
	                    /****************得到每个元素的值start**********************/
	                    Cell cell_0 = row.getCell(0);
	                    Cell cell_1 = row.getCell(1);
	                    Cell cell_2 = row.getCell(2);
	                    Cell cell_3 = row.getCell(3);
	                    Cell cell_4 = row.getCell(4);
	                    Cell cell_5 = row.getCell(5);
	                    if(ExcelUtil.getValue(cell_0).equals("序号")){
	                    	continue;
	                    }
						if (ExcelUtil.getValue(cell_0).length() == 0 && ExcelUtil.getValue(cell_1).length() == 0)
							continue;
	                    /*****************得到每个元素的值end**********************/
	                    /******************解析每个元素的值start*******************/
	                    //得到列的值，也就是你需要解析的字段的值
	                    String id = ExcelUtil.getValue(cell_0);
	                    String  fileId = ExcelUtil.getValue(cell_1);
	                    String  level = ExcelUtil.getValue(cell_2);
	                    String  date = sdf.format(cell_3.getDateCellValue());
	                    System.out.println(date);
	                    String  summary = ExcelUtil.getValue(cell_4);
	                    String  note = ExcelUtil.getValue(cell_5);
	                    /******************解析每个元素的值end*******************/
	                    /****************将读取出来的数值进行包装start***********/
//	                    System.out.println(new Date(Long.parseLong((Double.parseDouble(date) - 25569) * 24*60*60)+""));
	                    Archives archive = new Archives();
	                    archive.setId(id);
	                    archive.setFileId(fileId);
	                    archive.setLevel(level);
	                    
	                    archive.setDate(date);
	                    archive.setSummary(summary);
	                    archive.setNote(note);
	                    System.out.println(archive);
	                    archives.add(archive);
	                    /**************将读取出来的数值进行包装end**************/
	                }
	                if(archives.size()>0){
	                	for(Archives item:archives){
	                		archivesMapper.insert(item);
	                	}
	                }
	                result.put("status", "true");
	                result.put("msg","导入成功!");
	            }catch (Exception e){
	                e.printStackTrace();
	                result.put("status", "false");
	                result.put("msg","导入失败!");
	            }
	        }else{
	        	result.put("status", "false");
                result.put("msg","文件内容为空!");
	        }
	        return  result;
		}
	/**
	 * 导出excel中的数据
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportExcel")
	public HashMap<String,Object> exportExcel(@RequestParam(value = "file",required = true) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> res = new HashMap<>();
		return null;
	}
	
	/**
	 * 搜索接口
	 * @param fileId
	 * @param level
	 * @param start
	 * @param end
	 * @param abstarct
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/search")
	public HashMap<String,Object> search(@RequestParam(value = "virtualId",required = false) Integer virtualId,
			@RequestParam(value = "id",required = false) String id,
			@RequestParam(value = "fileId",required = false) String fileId,
			@RequestParam(value = "level",required = false) String level,
			@RequestParam(value = "start",required = false) String start,
			@RequestParam(value = "end",required = false) String end,
			@RequestParam(value = "summary",required = false) String summary,
			@RequestParam(value = "note",required = false) String note,
			@RequestParam(value = "page",required = false) Integer page,
			@RequestParam(value = "constant",required = false) Integer constant,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> result = new HashMap<>();;
		try {
			
			HashMap<String, Object> sel = new HashMap<>();
			if(page == null) page = 1; 
			sel.put("virtualId", virtualId);
			sel.put("id", id);
			sel.put("fileId", fileId);
			sel.put("level", level);
			sel.put("start", start);
			sel.put("end", end);
			sel.put("summary", summary);
			sel.put("note", note);
			sel.put("page", (page-1)*this.constant);
			sel.put("constant", this.constant);
			//获取结果
			List<Archives> archives = archivesMapper.selectByColumSelective(sel);
			int count = archivesMapper.selectByColumSelectiveCount(sel);
			
			//分页
			int pageCount = 0;
				if(count%this.constant==0){
					pageCount = count/this.constant;
				}else{
					pageCount = count/this.constant+1;
			}
			
			result.put("result", archives);
			result.put("count", count);
			result.put("page", page);
			result.put("pageCount", pageCount);
			result.put("status", "true");
            result.put("msg","搜索成功!");
		} catch (Exception e) {
			e.printStackTrace();
			 result.put("status", "false");
             result.put("msg","搜索失败!");
		}
		return result;
	}
	
	/**
	 * 修改接口
	 * @param id
	 * @param fileId
	 * @param level
	 * @param start
	 * @param end
	 * @param abstarct
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/modify")
	public HashMap<String,Object> modify(@RequestParam(value = "virtualId",required = true) Integer virtualId,
			@RequestParam(value = "id",required = false) String id,
			@RequestParam(value = "fileId",required = false) String fileId,
			@RequestParam(value = "level",required = false) String level,
			@RequestParam(value = "date",required = false) String date,
			@RequestParam(value = "summary",required = false) String summary,
			@RequestParam(value = "note",required = false) String note,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> result = new HashMap<>();
		
		try {
			  Archives archive = new Archives();
			  archive.setVirtualId(virtualId);
	          archive.setId(id);
	          archive.setFileId(fileId);
	          archive.setLevel(level);
	          archive.setDate(date);
	          archive.setSummary(summary);
	          archive.setNote(note);
	          archivesMapper.updateByPrimaryKeySelective(archive);
			
//			for(int i=0;i<1000;i++){
//				  Archives archive = new Archives();
//		          archive.setId(i+"");
//		          archive.setFileId("f"+i);
//		          archive.setLevel(i%3+"");
//		          archive.setDate(DateUtil.getCurrentTime_yyMMddhhmmss());
//		          archive.setSummary("这是第"+i+"个摘要**************");
//		          archive.setNote("这是第"+i+"个便签############");
//		          archivesMapper.insertSelective(archive);
//			}
			
			 result.put("status", "true");
             result.put("msg","修改成功!");
		} catch (Exception e) {
			 result.put("status", "false");
             result.put("msg","修改失败!");
		}
		return result;
	}
	
	/**
	 * 新增接口
	 * @param id
	 * @param fileId
	 * @param level
	 * @param start
	 * @param end
	 * @param abstarct
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/insert")
	public HashMap<String,Object> insert(
			@RequestParam(value = "id",required = false) String id,
			@RequestParam(value = "fileId",required = false) String fileId,
			@RequestParam(value = "level",required = false) String level,
			@RequestParam(value = "date",required = false) String date,
			@RequestParam(value = "summary",required = false) String summary,
			@RequestParam(value = "note",required = false) String note,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> result = new HashMap<>();
		
		try {
			  Archives archive = new Archives();
	          archive.setId(id);
	          archive.setFileId(fileId);
	          archive.setLevel(level);
	          archive.setDate(date);
	          archive.setSummary(summary);
	          archive.setNote(note);
	          archivesMapper.insertSelective(archive);
			
			
			 result.put("status", "true");
             result.put("msg","新增成功!");
		} catch (Exception e) {
			 result.put("status", "false");
             result.put("msg","新增失败!");
		}
		return result;
	}
	/**
	 * 删除接口
	 * @param id
	 * @param fileId
	 * @param level
	 * @param start
	 * @param end
	 * @param abstarct
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	public HashMap<String,Object> delete(
			@RequestParam(value = "virtualId",required = false) Integer virtualId,
			HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> result = new HashMap<>();
		
		try {
			System.out.println(virtualId);
	          archivesMapper.deleteByPrimaryKey(virtualId);
			 result.put("status", "true");
             result.put("msg","删除成功!");
		} catch (Exception e) {
			 result.put("status", "false");
             result.put("msg","删除失败!");
		}
		return result;
	}
	
}
