create view orderCurrentTime
as
select orderId,max(time) time from orderStatus group by orderId;
# 本视图用于获取当前订单的时间戳