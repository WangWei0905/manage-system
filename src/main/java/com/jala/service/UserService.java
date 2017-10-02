package com.jala.service;


import com.jala.architect.annotation.SystemServiceLog;
import com.jala.architect.constant.BusinessConstants;
import com.jala.architect.constant.BussinessCode;
import com.jala.architect.utils.BussinessMsgUtil;
import com.jala.dao.RoleMapper;
import com.jala.dao.UserMapper;
import com.jala.dao.UserRoleMapper;
import com.jala.model.bo.BussinessMsg;
import com.jala.model.bo.ExcelExport;
import com.jala.model.vo.Role;
import com.jala.model.vo.User;
import com.jala.model.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 用户信息服务类
 * @author ww
 * @date 2017/7/14
 */

@Service
public class UserService {

    private Log log = LogFactory.getLog(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ShiroService shiroService;

    /**
     * 根据用户Id查询用户信息
     * @param userLoginName 登陆用户名
     * @return
     */
    public User selectUserByloginName(String  userLoginName){
        return userMapper.selectUserByloginNameAndStatus(userLoginName, Long.valueOf(BusinessConstants.SYS_USER_STATUS_0.getCode()));
    }
    /**
     * 根据用户Id查询用户信息
     * @param userId 用户Id
     * @return
     */
    public User selectUserById(Integer userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 用户信息分页显示
     * @param user 用户实体
     * @return
     */
    public String selectUserResultPageList(User user){

        List<User> userList = userMapper.selectUserListByPage(user);
        if(null != userList && !userList.isEmpty() ){
            for (User u : userList) {
                User userRole = selectUserRolesByUserId(u.getUserId());
                u.setRoleNames(userRole.getRoleNames());
            }
        }
        Long count = userMapper.selectCountUser(user);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data", userList);

        return Json.toJson(map);
    }

    /**
     * 用户列表EXCEL导出
     * @param user 用户实体
     * @return
     */
    public ExcelExport excelExportUserList(User user){
        ExcelExport excelExport = new ExcelExport();
        List<User> userList = this.selectUsersList(user);
        excelExport.addColumnInfo("登陆账号","userLoginName");
        excelExport.addColumnInfo("用户姓名","userName");
        excelExport.addColumnInfo("用户状态","userStatus_Lable");
        excelExport.addColumnInfo("拥有角色","roleNames");
        excelExport.addColumnInfo("创建人","creator");
        excelExport.addColumnInfo("创建时间","createTime_Lable");
        excelExport.addColumnInfo("修改人","modifier");
        excelExport.addColumnInfo("修改时间","updateTime_Lable");

        excelExport.setDataList(userList);
        return excelExport;
    }


    /**
     * 用户列表信息List
     * @param user 用户实体
     * @return
     */
    public List<User> selectUsersList(User user){

        List<User> userList = userMapper.selectUserList(user);
        if (null != userList && !userList.isEmpty()){
            for (User u : userList) {
                User userRole = selectUserRolesByUserId(u.getUserId());
                u.setRoleNames(userRole.getRoleNames());
            }
        }
        return userList;
    }


    /**
     * 用户状态失效
     * @param userId	用户Id
     * @param loginName 当前登录用户名
     * @return
     * @throws Exception
     */
    @Transactional
    public BussinessMsg updateUserStatus(Integer userId, String loginName) throws Exception{
        log.info("用户失效开始，当前用户Id:"+userId);
        long start = System.currentTimeMillis();
        try {

            //解除用户与角色绑定关系
            List<UserRole> userRoles = userRoleMapper.selectUserRolesListByUserId(userId);
            if (null != userRoles && !userRoles.isEmpty()) {
                for (UserRole userRole : userRoles) {
                    userRoleMapper.deleteByPrimaryKey(userRole.getUserRoleId());
                }
            }
            //更改用户状态为1-失效
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userStatus", BusinessConstants.SYS_USER_STATUS_1.getCode());
            params.put("modifier", loginName);
            params.put("updateTime", new Date());
            params.put("userId", userId);
            userMapper.updateUserByStatus(params);

            // 清空用户权限缓存信息
            shiroService.clearAllCacheAuth();

        } catch (Exception e) {
            log.error("失效用户方法内部错误",e);
            throw e;
        }finally {
            log.info("用户失效结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
        }
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }

    /**
     * 批量用户状态失效
     * @param userIds	用户Id
     * @param loginName 当前登录用户名
     * @return
     * @throws Exception
     */
    public BussinessMsg updateUserBatchStatus(Integer[] userIds,String loginName) throws Exception{
        log.info("批量失效用户开始，当前用户Id:"+Arrays.toString(userIds));
        long start = System.currentTimeMillis();
        try {
            if(null != userIds && userIds.length > 0){
                for (Integer userId : userIds) {
                    //解除用户与角色绑定关系
                    List<UserRole> userRoles = userRoleMapper.selectUserRolesListByUserId(userId);
                    if (null != userRoles && !userRoles.isEmpty()) {
                        for (UserRole userRole : userRoles) {
                            userRoleMapper.deleteByPrimaryKey(userRole.getUserRoleId());
                        }
                    }
                    //更改用户状态为1-失效
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("userStatus", BusinessConstants.SYS_USER_STATUS_1.getCode());
                    params.put("modifier", loginName);
                    params.put("updateTime", new Date());
                    params.put("userId", userId);
                    userMapper.updateUserByStatus(params);
                }

                // 清空用户权限缓存信息
                shiroService.clearAllCacheAuth();
            }

        } catch (Exception e) {
            log.error("失效用户方法内部错误",e);
            throw e;
        }finally {
            log.info("批量失效用户结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
        }
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }

    /**
     * 保存用户信息
     * @param user 用户对象
     * @param loginName 当前登录用户
     * @return
     * @throws Exception
     */
    @Transactional
    @SystemServiceLog(description="保存用户信息Service")
    public BussinessMsg saveOrUpdateUser(User user, String loginName) throws Exception{
        log.info("保存用户信息开始");
        long start = System.currentTimeMillis();
        try {
            //验证用户账号唯一性
            Long checkUserLoginName = userMapper.selectUserLoginNameCheck(user.getUserLoginName(),user.getUserId());
            if(checkUserLoginName.intValue() > 0){
                return BussinessMsgUtil.returnCodeMessage(BussinessCode.USER_LOGIN_NAME_EXIST);
            }
            //保存用户信息
            if (null == user.getUserId()) {
                user.setUserPassword("123456");
                user.setCreator(loginName);
                user.setCreateTime(new Date());
                userMapper.insertSelective(user);
            } else {
                //更新用户信息
                user.setModifier(loginName);
                user.setUpdateTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            log.error("保存用户信息方法内部错误",e);
            throw e;
        }finally {
            log.info("保存用户信息结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
        }
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }


    /**
     * 根据用户Id查询用户角色信息
     * @param userId 用户id
     * @return
     */
    public User selectUserRolesByUserId(Integer userId){
        if(userId != null){
            User user = userMapper.selectByPrimaryKey(userId);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            //用户所对应的角色信息
            List<Role> roles = roleMapper.selectUserRolesByUserId(userId);
            if(null != roles && !roles.isEmpty()){
                for (Role role : roles) {
                    sb.append(role.getRoleId()).append(",");
                    sb2.append(role.getRoleName()).append(",");
                }
                if(sb.length()>0){
                    sb.deleteCharAt(sb.length()-1);
                    user.setRoleIds(sb.toString());
                }
                if(sb2.length()>0){
                    sb2.deleteCharAt(sb2.length()-1);
                    user.setRoleNames(sb2.toString());
                }

            }
            return user;

        }
        return null;
    }


    /**
     * 保存用户分配角色信息
     * @param userId 用户id
     * @param roleIds 分配的角色信息
     * @param longinName 当前登录用户名称
     * @return
     * @throws Exception
     */
    @Transactional
    public BussinessMsg saveUserRole(Integer userId,String roleIds,String longinName) throws Exception{
        log.info("保存用户分配角色信息开始");
        long start = System.currentTimeMillis();
        try {
            List<UserRole> userRoles = userRoleMapper.selectUserRolesListByUserId(userId);
            //如果角色Id不为空插入用户角色信息，否则删除用户下所有分配的角色
            if (StringUtils.isNotEmpty(roleIds)) {
                if (null != userRoles && !userRoles.isEmpty()) {
                    for (UserRole userRole : userRoles) {
                        userRoleMapper.deleteByPrimaryKey(userRole.getUserRoleId());
                    }
                }

                for (String roleId : roleIds.split(",")) {
                    UserRole ur = new UserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(Integer.valueOf(roleId));
                    ur.setCreateTime(new Date());
                    ur.setCreator(longinName);
                    userRoleMapper.insertSelective(ur);
                }

            } else {
                if (null != userRoles && !userRoles.isEmpty()) {
                    for (UserRole userRole : userRoles) {
                        userRoleMapper.deleteByPrimaryKey(userRole.getUserRoleId());
                    }
                }
            }

            // 清空用户权限缓存信息
            shiroService.clearAllCacheAuth();

        } catch (Exception e) {
            log.error("用户分配角色信息方法内部错误",e);
            throw e;
        }finally {
            log.info("保存用户分配角色信息结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
        }
        return BussinessMsgUtil.returnCodeMessage(BussinessCode.GLOBAL_SUCCESS);
    }




}
