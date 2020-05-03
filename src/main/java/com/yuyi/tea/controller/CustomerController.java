package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Customer;
import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.bean.EnterpriseCustomerApplication;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.service.CustomerService;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserFaceInfoService userFaceInfoService;

    @Autowired
    private OrderService orderService;

    /**
     * 获取客户类型
     * @return
     */
    @GetMapping("/admin/customerTypes")
    public List<CustomerType> getCustomerTypes(){
        List<CustomerType> customerTypes = customerService.getCustomerTypes();
        return customerTypes;
    }

    /**
     * 获取企业客户申请列表
     * @param isFetchAll
     * @return
     */
    @GetMapping("/admin/enterpriseCustomerApplications/{isFetchAll}")
    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications(@PathVariable boolean isFetchAll){
        List<EnterpriseCustomerApplication> enterpriseCustomerApplications = customerService.getEnterpriseCustomerApplications(isFetchAll);
        return enterpriseCustomerApplications;
    }

    /**
     * 企业客户申请开始审核
     * @param uid
     * @return
     */
    @GetMapping("/admin/startEnterpriseCustomerApplication/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String startEnterpriseCustomerApplication(@PathVariable int uid){
        customerService.startEnterpriseCustomerApplication(uid);
        return "success";
    }

    /**
     * 根据uid获取企业客户申请详细信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/enterpriseCustomerApplication/{uid}")
    public EnterpriseCustomerApplication getEnterpriseCustomerApplication(@PathVariable int uid){
        EnterpriseCustomerApplication enterpriseCustomerApplication = customerService.getEnterpriseCustomerApplication(uid);
        return enterpriseCustomerApplication;
    }

    /**
     * 通过企业客户申请
     * @param uid
     * @return
     */
    @GetMapping("/admin/approveEnterpriseCustomerApplication/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String approveEnterpriseCustomerApplication(@PathVariable int uid){
        customerService.approveEnterpriseCustomerApplication(uid);
        return "success";
    }

    /**
     * 拒绝企业客户申请
     * @param uid
     * @return
     */
    @GetMapping("/admin/rejectEnterpriseCustomerApplication/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String rejectEnterpriseCustomerApplication(@PathVariable int uid){
        customerService.rejectEnterpriseCustomerApplication(uid);
        return "success";
    }

    /**
     * 获取客户列表
     * @return
     */
    @GetMapping("/admin/customers")
    public List<Customer> getCustomers(){
        List<Customer> customers = customerService.getCustomers();
        return customers;
    }

    /**
     * 将客户升级为超级vip
     * @param uid
     * @return
     */
    @GetMapping("/admin/setSupervip/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public Customer setSuperVIP(@PathVariable int uid){
        Customer customer = customerService.setSuperVIP(uid);
        return customer;
    }

    /**
     * 根据user_face_info的uid获取客户信息
     * 若此face有对应的customer则返回
     * 否则返回null
     * @param uid
     * @return
     */
    @GetMapping("/mobile/customer/{uid}")
    public Customer fetchCustomerByFaceUid(@PathVariable int uid){
        FaceUserInfo faceUserInfo = userFaceInfoService.getFaceUserInfo(uid);
        if(faceUserInfo.getCustomer()!=null){
            Customer customer=faceUserInfo.getCustomer();
            customer.setPassword(null);
            //获取该用户未完成的订单
            List<Order> uncompleteOrders = orderService.getUncompleteOrders(customer.getUid());
            customer.setOrders(uncompleteOrders);
            return customer;
        }
        return null;
    }

}
