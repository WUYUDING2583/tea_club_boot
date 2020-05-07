package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.Result;
import com.yuyi.tea.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {

    @Autowired
    private WebSocketPayServer ws;

    @GetMapping("/mobile/simulatePay/{customerId}")
    public void simulatePay(@PathVariable int customerId){
        try {
            Result result=new Result("支付成功");
            ws.sendInfo(new Gson().toJson(result), customerId + "");
            //添加充值记录
            //改变账户余额
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }
}
