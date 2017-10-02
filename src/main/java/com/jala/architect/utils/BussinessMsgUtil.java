
package com.jala.architect.utils;


import com.jala.architect.constant.BussinessCode;
import com.jala.model.bo.BussinessMsg;

/**
 * 后台管理系统返回码信息帮助类
 *
 * @author ww
 * @date 2017/7/11
 */

public class BussinessMsgUtil {


    /**
     * 返回消息代码code 和 message
     *
     * @param bussinessCode 返回码
     * @return
     */
    public static BussinessMsg returnCodeMessage(BussinessCode bussinessCode) {
        return returnCodeMessage(bussinessCode, null);
    }

    /**
     * 返回消息代码和数据
     *
     * @param bussinessCode 返回码
     * @param returnData    返回数据
     * @return
     */
    public static BussinessMsg returnCodeMessage(BussinessCode bussinessCode, Object returnData) {
        BussinessMsg bussinessMsg = new BussinessMsg();
        bussinessMsg.setReturnCode(bussinessCode.getCode());
        bussinessMsg.setReturnMessage(bussinessCode.getMsg());
        bussinessMsg.setReturnData(returnData);
        return bussinessMsg;
    }
}
