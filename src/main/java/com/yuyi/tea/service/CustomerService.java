package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    public static final String REDIS_CUSTOMERS_NAME="customers";
    public static final String REDIS_CUSTOMER_TYPES_NAME=REDIS_CUSTOMERS_NAME+":customerTypes";
    public static final String REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME=REDIS_CUSTOMERS_NAME+":enterpriseApplication";
    public static final String REDIS_CUSTOMER_NAME=REDIS_CUSTOMERS_NAME+":customer";

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    /**
     * 获取客户类型
     * @return
     */
    public List<CustomerType> getCustomerTypes(){
        boolean hasKey=redisService.exists(REDIS_CUSTOMER_TYPES_NAME);
        List<CustomerType> customerTypes;
        if(hasKey){
            customerTypes= (List<CustomerType>) redisService.get(REDIS_CUSTOMER_TYPES_NAME);
            log.info("从redis中获取客户类型列表"+customerTypes);
        }else{
            log.info("从数据库获取客户类型列表");
            customerTypes = customerMapper.getCustomerTypes();
            log.info("将客户类型列表存入reids"+customerTypes);
            redisService.set(REDIS_CUSTOMER_TYPES_NAME,customerTypes);
        }
        return customerTypes;
    }

    /**
     * 获取企业客户申请列表
     * @param isFetchAll
     * @return
     */
    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications(boolean isFetchAll) {
        List<EnterpriseCustomerApplication> applications=new ArrayList<>();
        if(isFetchAll){
            applications = customerMapper.getAllEnterpriseCustomerApplications();
        }else{
            applications = customerMapper.getLast3MonthsEnterpriseCustomerApplications(TimeUtil.getCurrentTimestamp() - (long) 1000 * 60 * 60 * 24 * 90);
         }
        for(EnterpriseCustomerApplication enterpriseCustomerApplication:applications){
            enterpriseCustomerApplication.getEnterprise().setBusinessLicense(new Photo());
            enterpriseCustomerApplication.getApplicant().setAvatar(new Photo());
        }
        return applications;
    }

    /**
     * 企业客户申请开始审核
     * @param uid
     */
    public void startEnterpriseCustomerApplication(int uid) {
        customerMapper.startEnterpriseCustomerApplication(uid);
    }

    /**
     * 根据uid获取企业客户申请详细信息
     * @param uid
     * @return
     */
    public EnterpriseCustomerApplication getEnterpriseCustomerApplication(int uid) {
        boolean hasKey=redisService.exists(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
        EnterpriseCustomerApplication enterpriseCustomerApplication;
        if(hasKey){
            enterpriseCustomerApplication= (EnterpriseCustomerApplication) redisService.get(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
            log.info("从redis获取企业客户申请信息"+enterpriseCustomerApplication);
        }else{
            log.info("从数据库获取企业客户申请信息");
            enterpriseCustomerApplication= customerMapper.getEnterpriseCustomerApplication(uid);
            enterpriseCustomerApplication.getApplicant().setPassword(null);
            log.info("将企业客户申请信息存入redis"+enterpriseCustomerApplication);
            redisService.set(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid,enterpriseCustomerApplication);
        }
        return enterpriseCustomerApplication;
    }

    /**
     * 通过企业客户申请
     * @param uid
     */
    public void approveEnterpriseCustomerApplication(int uid) {
        customerMapper.approveEnterpriseCustomerApplication(uid);
        boolean hasKey=redisService.exists(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
        if(hasKey){
            log.info("更新redis中企业客户申请状态为通过");
            EnterpriseCustomerApplication enterpriseCustomerApplication= (EnterpriseCustomerApplication) redisService.get(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
            enterpriseCustomerApplication.setStatus(CommConstants.EnterpriseCustomerApplication.APPROVE);
            redisService.set(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid,enterpriseCustomerApplication);
        }
    }

    /**
     * 拒绝企业客户申请
     * @param uid
     */
    public void rejectEnterpriseCustomerApplication(int uid) {
        customerMapper.rejectEnterpriseCustomerApplication(uid);
        boolean hasKey=redisService.exists(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
        if(hasKey){
            log.info("更新redis中企业客户申请状态为拒绝");
            EnterpriseCustomerApplication enterpriseCustomerApplication= (EnterpriseCustomerApplication) redisService.get(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid);
            enterpriseCustomerApplication.setStatus(CommConstants.EnterpriseCustomerApplication.REJECT);
            redisService.set(REDIS_ENTERPRISE_CUSTOMER_APPLICATION_NAME+":"+uid,enterpriseCustomerApplication);
        }
    }

    /**
     * 获取客户列表
     * @return
     */
    public List<Customer> getCustomers() {
        List<Customer> customers = customerMapper.getCustomers();
        for(Customer customer:customers){
            customer.setPassword(null);
            customer.setAvatar(null);
        }
        return customers;
    }

    //将客户升级为超级vip
    public Customer setSuperVIP(int uid) {
        customerMapper.setSuperVIP(uid);
        Customer customer = customerMapper.getCustomerByUid(uid);
        customer.setAvatar(null);
        customer.setPassword(null);
        boolean hasKey=redisService.exists(REDIS_CUSTOMER_NAME+":"+uid);
        if (hasKey){
            log.info("将redis中客户信息更新为超级vip");
            Customer redisCustomer= (Customer) redisService.get(REDIS_CUSTOMER_NAME+":"+uid);
            redisCustomer.setCustomerType(customerMapper.getCustomerType(3));
            redisService.set(REDIS_CUSTOMER_NAME+":"+uid,redisCustomer);
        }
        return customer;
    }

    /**
     * 从redis中获取客户信息
     * @param uid
     * @return
     */
    public Customer getRedisCustomer(int uid){
        Customer customer=null;
        boolean hasKey = redisService.exists(REDIS_CUSTOMER_NAME+":"+uid);
        if(hasKey){
            customer= (Customer) redisService.get(REDIS_CUSTOMER_NAME+":"+uid);
            log.info("从缓存获取的数据"+ customer);
        }else{
            log.info("从数据库中获取数据");
            customer = customerMapper.getCustomerByUid(uid);
            redisService.set(REDIS_CUSTOMER_NAME+":"+uid,customer);
            log.info("数据插入缓存" + customer);
        }
        Amount balance=customerMapper.getCustomerBalance(uid);
        customer.setBalance(balance);
        return customer;
    }


    /**
     * 获取客户账户余额
     * @param uid
     * @return
     */
    public Amount getCustomerBalance(int uid) {
        Customer customer = customerMapper.getCustomerByUid(uid);
        Amount balance=customer.getBalance();
        return balance;
    }

    /**
     * 扣除客户账户金额
     * @param ingot
     * @param credit
     */
    @Transactional(rollbackFor = Exception.class)
    public Amount pay(float ingot, float credit,int uid) {
        try {
            customerMapper.pay(ingot, credit, uid);
            Amount balance = customerService.getCustomerBalance(uid);
            return balance;
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }

    /**
     * 根据value改变客户账户余额
     * @param customerId
     * @param value
     * @return 账户余额
     */
    @Transactional(rollbackFor = Exception.class)
    public Amount addBalance(int customerId, float value) {
        //获取币种兑换比例
        float rate=companyService.getRechargeRate();
        float ingot=rate*value;
        customerMapper.addBalance(customerId,ingot);
        Amount customerBalance = getCustomerBalance(customerId);
        return customerBalance;
    }

    /**
     * 检查账户余额
     * @param ingot
     * @param credit
     * @param balance
     */
    public boolean checkBalance(float ingot,float credit,Amount balance){
        if (balance.getCredit() < credit || balance.getIngot() < ingot) {
            String msg = "所需金额：" + ingot + "元宝 " + credit + "积分\n";
            msg += "当前余额：" + balance.getIngot() + "元宝 " + balance.getCredit() + "积分";
            throw new GlobalException(CodeMsg.INSUFFICIENT_BALANCE(msg));
        }
        return true;
    }

    /**
     * 检查账户余额
     * @param ingot
     * @param credit
     * @param customerId
     */
    public boolean checkBalance(float ingot,float credit,int customerId){
        Amount balance = customerService.getCustomerBalance(customerId);
        if (balance.getCredit() < credit || balance.getIngot() < ingot) {
            String msg = "所需金额：" + ingot + "元宝 " + credit + "积分\n";
            msg += "当前余额：" + balance.getIngot() + "元宝 " + balance.getCredit() + "积分";
            throw new GlobalException(CodeMsg.INSUFFICIENT_BALANCE(msg));
        }
        return true;
    }

    /**
     * 为客户账户余额新增积分
     * @param customerId
     * @param credit
     */
    @Transactional(rollbackFor = Exception.class)
    public float addCredit(int customerId, float credit) {
        try {
            customerMapper.addCredit(customerId, credit);
            Customer customerByUid = customerMapper.getCustomerByUid(customerId);
            return  customerByUid.getCredit();
        }catch (Exception e){
            throw new GlobalException(CodeMsg.ADD_CREDIT_FAIL);
        }
    }

    /**
     * 根据搜索信息搜索对应客户信息
     * @param searchText
     * @return
     */
    public List<Customer> searchCustomers(String searchText) {
        List<Customer> customers=customerMapper.searchCustomers("%"+searchText+"%");
        for(Customer customer:customers){
            customer.setPassword(null);
        }
        return customers;
    }

    /**
     * 保存客户信息
     * @param customer
     */
    public void saveCustomer(Customer customer) {
        customerMapper.saveCustomer(customer);
    }

    /**
     * 增加元宝
     * @param userId
     * @param ingot
     * @return 最新元宝数
     */
    @Transactional(rollbackFor = Exception.class)
    public float addIngot(int userId, float ingot) {
        customerMapper.addIngot(userId,ingot);
        Customer customerByUid = customerMapper.getCustomerByUid(userId);
        return customerByUid.getIngot();
    }

    /**
     * 根据手机号获取客户信息
     * @param contact
     * @return
     */
    public Customer getCustomer(String contact) {
        Customer customer=customerMapper.getCustomerByContact(contact);
        return customer;
    }

    /**
     * 客户注册
     * @param customer
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(Customer customer) {
        try {
            customer.setCustomerType(CommConstants.CustomerType.REGISTER_USER);
            customerMapper.saveCustomer(customer);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.REGISTER_FAIL);
        }
    }

    /**
     * 添加消费记录
     * @param billDetail
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBillDetail(BillDetail billDetail) {
        customerMapper.saveBillDetail(billDetail);
    }

    /**
     * 获取客户的消费记录（20条）
     * @param customerId
     * @param page
     * @return
     */
    public List<BillDetail> getBillDetails(int customerId, int page) {
        List<BillDetail> billDetails=customerMapper.getBillDetails(customerId,page*20);
        return  billDetails;
    }

    /**
     * 获取客户的充值记录（20条）
     * @param customerId
     * @param page
     * @return
     */
    public List<ChargeRecord> getChargeRecords(int customerId, int page) {
        List<ChargeRecord> records=customerMapper.getChargeRecords(customerId,page*20);
        return  records;
    }
}
