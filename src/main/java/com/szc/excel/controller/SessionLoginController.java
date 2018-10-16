package com.szc.excel.controller;

import com.szc.excel.domain.Permission;
import com.szc.excel.domain.User;
import com.szc.excel.domain.UserAndPermission;
import com.szc.excel.mapper.PermissionMapper;
import com.szc.excel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/Session")
public class SessionLoginController {

    @Value("${permission.is.effect}")
    private Boolean effect;

    @Value("${page.constant}")
    private Integer constant;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    //logout
    //register
    //admin修改用户权限

    @RequestMapping(value = "/login")
    public HashMap<String,Object> login(@RequestParam(required = true) String username, @RequestParam(required = true) String password,
                      HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        try{
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            User user2 = userMapper.selectByUser(user);
            System.out.println(user2);
            if(user2!=null){

                //登陆成功
                HttpSession httpsession = request.getSession();
                //设置登陆
                httpsession.setAttribute("login", "login");

                if("true".equals(user2.getIsSuper())){
                    result.put("super","super");
                    httpsession.setAttribute("super", "super");
                }

                List<Permission> permissions= permissionMapper.selectPermissionByUser(user2);
                //授予权限
                for(Permission p:permissions){
                    httpsession.setAttribute(p.getPermissions(),p.getPermissions());
                }
                result.put("status","true");

                result.put("msg","登录成功!");
            }else{
                //登陆失败
                result.put("status","false");
                result.put("msg","登录失败!用户名或密码错误！");
            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }finally {
            return result;
        }
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public HashMap<String,Object> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession ses = request.getSession();
        ses.invalidate();
        HashMap<String, Object> result = new HashMap<>();
        result.put("status","true");
        result.put("msg","退出成功!");
        return result;
    }

    @RequestMapping(value = "/registor")
    public  HashMap<String,Object> registor(@RequestParam(required = true) String username, @RequestParam(required = true) String password,
//                         @RequestParam(required = true) String[] permissions,
                         HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = (HttpSession)request.getSession();
       try{
//           if (!"super".equals(session.getAttribute("super"))) {
           //TODO 权限
           if(false){
               result.put("status", "false");
               result.put("msg", "没有权限，请联系管理员!");
          }else{
              User user = new User();
              user.setUsername(username);
              user.setPassword(password);
              User user2 =  userMapper.selectByUser(user);
              if(user2==null){
                  //如果没有查询到用户
                  user2 = new User();
                  user2.setUsername(username);
                  user2.setPassword(password);
                  userMapper.insertSelective(user2);
                  /**
                   * 下面注释掉的代码 可以在用户注册的时候注册权限，考虑到实际情况，暂时先去掉
                   */
//               User user3 =  userMapper.selectByUser(user2);
//               for(String s:permissions){
//                   if(!"".equals(s)){
//                       Permission p = new Permission();
//                       p.setPermissions(s);
//                       p.setUserId(user3.getId());
//                       permissionMapper.insertSelective(p);
//                   }
//               }
                  result.put("status","true");
                  result.put("msg","注册用户成功!");
              }else{
                  //重复
                  result.put("status","false");
                  result.put("msg","用户名已被占用,注册用户失败!");
              }
          }
       }catch (Exception e){
           e.printStackTrace();
           result.put("status","false");
           result.put("msg","未知异常,请联系管理员!");
       }
        return result;
    }


    @RequestMapping(value = "/modifyUser")
    public  HashMap<String,Object> modifyUser(@RequestParam(required = true) Integer id,
            @RequestParam(required = true) String username, @RequestParam(required = true) String password,
                                            @RequestParam(required = false) String[] permissions,
                                            HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = (HttpSession)request.getSession();
       try{
           if (effect&&!"super".equals(session.getAttribute("super"))) {
           //TODO 权限
//           if(false){
               result.put("status", "false");
               result.put("msg", "没有权限，请联系管理员!");
           }else{
               User u = userMapper.selectByPrimaryKey(id);
               if(u!=null){
                   if("true".equals(u.getIsSuper())){
                       result.put("status", "false");
                       result.put("msg", "管理员不能删除自己的权限!");
                   }else{
                       User user  = new User();
                       user.setUsername(username);
                       user.setPassword(password);
                       user.setId(id);
                       userMapper.updateByPrimaryKeySelective(user);
                       permissionMapper.deletePermissionByUser(user);
                       //更新权限
                       if(permissions!=null){
                           if(permissions.length>0)
                               for(String s:permissions){
                                   if(!"".equals(s)){
                                       Permission p = new Permission();
                                       p.setPermissions(s);
                                       p.setUserId(user.getId());
                                       permissionMapper.insert(p);
                                   }
                               }
                           result.put("status","true");
                           result.put("msg","修改信息成功!");
                       }else{
                           result.put("status","true");
                           result.put("msg","修改信息成功!");
                       }
                   }
              }
           }
       }catch (Exception e){
           e.printStackTrace();
           result.put("status","false");
           result.put("msg","未知异常,请联系管理员!");
       }
        return result;
    }

    @RequestMapping(value = "/search")
    public  HashMap<String,Object> search(@RequestParam(required = false) String id) {
        HashMap<String, Object> result = new HashMap<>();
        try{
            System.out.println(Objects.isNull(id));
            if(!Objects.isNull(id)){
                String ids ="%"+id+"%";
                HashMap<String,String> map = new HashMap<>();
                map.put("ids",ids);
               List<User> user2s =  userMapper.selectByLike(map);
                if(user2s.size()>0){
                    List<UserAndPermission> list = new ArrayList<>();
                  for(User u:user2s){
                      List<Permission> permissions = permissionMapper.selectPermissionByUser(u);
                      List<String> p = new ArrayList<>();
                      for(Permission permission:permissions){
                          p.add(permission.getPermissions());
                      }
                      UserAndPermission up = new UserAndPermission();
                      up.setUser(u);
                      up.setPermissions(p);
                      list.add(up);
                      result.put("data",list);
                      result.put("status","true");
                      result.put("msg","查询用户信息成功！");
                  }
                }else{
                    //不存在用户
                    result.put("status","false");
                    result.put("msg","不存在用户！");
                }
            }else{
                //查询所有用户
                List<User> users = userMapper.selectAllUser();
                List<UserAndPermission> ups = new ArrayList<>();
                UserAndPermission up;
                List<String> p;
                for(User u:users){
                    List<Permission> permissions = permissionMapper.selectPermissionByUser(u);
                    p = new ArrayList<>();
                    for(Permission permission:permissions){
                        p.add(permission.getPermissions());
                    }

                    up = new UserAndPermission();
                    up.setUser(u);
                    up.setPermissions(p);
                    ups.add(up);
                }
                result.put("data",ups);
                result.put("status","true");
                result.put("msg","查询用户信息成功！");

            }

        }catch (Exception e){
            e.printStackTrace();
            result.put("status","false");
            result.put("msg","未知异常,请联系管理员!");
        }
        return result;
    }
}
