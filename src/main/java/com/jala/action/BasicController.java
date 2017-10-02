package com.jala.action;

import com.jala.model.vo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Controller 父类
 * @author ww
 * @date 2017/7/17
 *
 */
@Controller
@Scope("prototype")
public class BasicController {

    protected Log log = LogFactory.getLog(getClass());


    /**
     * 登录用户名
     */
    public String getCurrentLoginName() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user.getUserLoginName();
    }

    /**
     * 登陆用户id
     * @return
     */
    public Integer getCurrentLoginId(){
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user.getUserId();
    }

    /**
     * 登录用户对象
     */
    public User getCurrentUser() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user;
    }

}
