package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.*;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.*;
import com.yuyi.tea.service.interfaces.NoticeService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserFaceInfoService userFaceInfoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WebSocketMINAServer webSocketMINAServer;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopBoxService shopBoxService;

    @Autowired
    private ArticleService articleService;

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

    @GetMapping("/admin/customers/{uid}")
    public Customer getCustomer(@PathVariable int uid){
        Customer customer = customerService.getRedisCustomer(uid);
        customer.setPassword(null);
        customer.setEnterpriseCustomerApplications(null);
        customer.setOrders(null);
        return customer;
    }

    /**
     * 若type==face
     * 根据user_face_info的uid获取客户信息
     * 若此face有对应的customer则返回
     * 否则返回null
     * 若type==search
     * uid为customer id，
     * 据此返回对应customer
     * @param uid
     * @param type
     * @return
     */
    @GetMapping("/mobile/customer/{uid}/{type}")
    public Customer fetchCustomerByFaceUid(@PathVariable int uid,@PathVariable String type){
        switch (type) {
            case "face":
                FaceUserInfo faceUserInfo = userFaceInfoService.getFaceUserInfo(uid);
                if (faceUserInfo.getCustomer() != null) {
                    Customer customer = faceUserInfo.getCustomer();
                    customer.setPassword(null);
                    //获取该用户最近3个月的订单
                    long startDate = TimeUtil.getNDayAgoStartTime(90);
                    long endDate = TimeUtil.getNDayAgoStartTime(-1);
                    TimeRange timeRange = new TimeRange(startDate, endDate);
                    List<Order> orders = orderService.getOrdersByCustomer(customer.getUid(), timeRange);
                    customer.setOrders(orders);
                    return customer;
                }
                break;
            case "search":
                Customer customer=customerService.getRedisCustomer(uid);
                customer.setPassword(null);
                //获取该用户最近3个月的订单
                long startDate = TimeUtil.getNDayAgoStartTime(90);
                long endDate = TimeUtil.getNDayAgoStartTime(-1);
                TimeRange timeRange = new TimeRange(startDate, endDate);
                List<Order> orders = orderService.getOrdersByCustomer(customer.getUid(), timeRange);
                customer.setOrders(orders);
                return customer;
        }
        throw new GlobalException(CodeMsg.NON_REGISTER_CUSTOMER);
    }

    /**
     * 根据搜索信息搜索对应客户信息
     * @param searchText
     * @return
     */
    @GetMapping("/mobile/search/{searchText}")
    public List<Customer> searchCustomer(@PathVariable String searchText){
        List<Customer> customers=customerService.searchCustomers(searchText);
        return customers;
    }

    @PostMapping("/mobile/register/{faceId}")
    public Customer mobileRegister(@PathVariable int faceId,@RequestBody Customer customer){
        //保存客户信息
        customerService.saveCustomer(customer);
        //将人脸信息和客户信息匹配
        userFaceInfoService.matchCustomer(faceId,customer.getUid());
        FaceUserInfo faceUserInfo = userFaceInfoService.getFaceUserInfo(faceId);
        Customer faceUserInfoCustomer = faceUserInfo.getCustomer();
//        faceUserInfoCustomer.setAvatar(new Photo(faceUserInfo.getFace()));
        return faceUserInfoCustomer;
    }

    /**
     * 新增元宝
     * @param userId
     * @param ingot
     * @return
     */
    @GetMapping("/mp//balance/ingot/add/{userId}/{ingot}")
    public float addIngot(@PathVariable int userId,@PathVariable float ingot){
        float updatedIngot=customerService.addIngot(userId,ingot);
        return updatedIngot;
    }

    /**
     * 新增积分
     * @param userId
     * @param credit
     * @return
     */
    @GetMapping("/mp//balance/credit/add/{userId}/{credit}")
    public float addCredit(@PathVariable int userId,@PathVariable float credit){
        float updatedCredit=customerService.addCredit(userId,credit);
        return updatedCredit;
    }

    /**
     * 获取客户的消费记录（20条）
     * @param customerId
     * @param page
     * @return
     */
    @GetMapping("/mp/bills/{customerId}/{page}")
    public List<BillDetail> getMpBillDetails(@PathVariable int customerId,@PathVariable int page){
        List<BillDetail> billDetails=customerService.getBillDetails(customerId,page);
        return billDetails;
    }
    /**
     * 获取客户的充值记录（20条）
     * @param customerId
     * @param page
     * @return
     */
    @GetMapping("/mp/charge/{customerId}/{page}")
    public List<ChargeRecord> getMpChargeRecords(@PathVariable int customerId,@PathVariable int page){
        List<ChargeRecord> records=customerService.getChargeRecords(customerId,page);
        return records;
    }

    /**
     * 活动赠送金额
     * @param customerId
     * @param amount
     * @return
     */
    @PutMapping("/mp/activity/present/{customerId}")
    @Transactional(rollbackFor = Exception.class)
    public Amount activityPresentMoney(@PathVariable int customerId,@RequestBody Amount amount){
        customerService.addIngot(customerId,amount.getIngot());
        customerService.addCredit(customerId,amount.getCredit());
        //保存通知至数据库
        long timestamp = TimeUtil.getCurrentTimestamp();
        Notification notification = CommConstants.Notification.ACTIVITY_PRESENT("阅读", amount,timestamp , customerId);
        noticeService.saveNotification(notification);
        //添加账单记录
        Customer customer = customerService.getRedisCustomer(customerId);
        BillDetail billDetail=new BillDetail(timestamp,amount.getIngot(),amount.getCredit(),CommConstants.BillDescription.PRESENT,customer);
        customerService.saveBillDetail(billDetail);
        //发送通知
        Customer redisCustomer = customerService.getRedisCustomer(customerId);
        try {
            webSocketMINAServer.sendInfo(redisCustomer.getContact());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Amount customerBalance = customerService.getCustomerBalance(customerId);
        return customerBalance;
    }

    /**
     * 微信小程序搜索
     * @param value
     * @return
     */
    @GetMapping("/mp/search")
    public SearchResult search(String value){
        List<Product> products=productService.search(value);
        List<ShopBox> boxes=shopBoxService.search(value);
        List<Article> articles=articleService.search(value);
        for(Product product:products){
            product.setActivityRules(null);
            product.setShop(null);
            product.setProductDetails(null);
            product.setActivities(null);
            product.setSales(null);
            List<Photo> photos=new ArrayList<>();
            photos.add(product.getPhotos().get(0));
            product.setPhotos(photos);
        }
        for(ShopBox shopBox:boxes){
            shopBox.setInfos(null);
            shopBox.setShop(new Shop(shopBox.getShop().getUid()));
            shopBox.setReservations(null);
            List<Photo> photos=new ArrayList<>();
            photos.add(shopBox.getPhotos().get(0));
            shopBox.setPhotos(photos);
        }
        for(Article article:articles){
            article.setTags(null);
        }
        SearchResult result=new SearchResult(articles,products,boxes);
        return result;
    }

}
