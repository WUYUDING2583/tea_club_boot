create view lastMonthSalesView
as
SELECT productId,sum(productId) as sales FROM `orderProduct` A,`orders` B where A.orderId=B.uid and B.orderTime>UNIX_TIMESTAMP(NOW())*1000-1000*60*60*24*30 GROUP BY productId;