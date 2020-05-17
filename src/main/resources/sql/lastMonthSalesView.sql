create view lastMonthSalesView
as
select p.uid as productId,s.sales from product p LEFT JOIN (SELECT productId,sum(`number`) as sales FROM `orderProduct` A,`orders` B where A.orderId=B.uid and B.orderTime>UNIX_TIMESTAMP(NOW())*1000-1000*60*60*24*30 GROUP BY productId) s on p.uid=s.productId;