CREATE VIEW totalSalesView
  AS
SELECT productId,sum(productId) as sales FROM `orderProduct` GROUP BY productId;
