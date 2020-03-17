package com.yuyi.tea.exception;

import com.yuyi.tea.common.CommConstants;

public class ShopNotExistException extends UserException {

    public ShopNotExistException() {
        super(CommConstants.UserException.SHOP_INFO_NOT_EXIST.error, CommConstants.UserException.SHOP_INFO_NOT_EXIST.msg);
//        super("lalala","lalalsl");
    }
}
