CREATE VIEW totalSalesView
  AS
SELECT p.uid as productId,s.sales from product p LEFT JOIN (select productId, sum(productId) as sales from orderProduct GROUP BY productId) s on p.uid=s.productId;
