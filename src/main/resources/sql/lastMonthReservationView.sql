create view lastMonthReservationView
as
select b.uid as boxId, t.number from shopBox b LEFT JOIN(SELECT boxId, count(boxId) as number FROM `reservation` r,orders o where r.orderId=o.uid and o.orderTime>UNIX_TIMESTAMP(NOW())*1000-1000*60*60*24*30 GROUP BY boxId) t on b.uid=t.boxId;