create view totalReservationView
as
select b.uid as boxId, t.number from shopBox b LEFT JOIN(SELECT boxId, count(boxId) as number FROM `reservation` GROUP BY boxId) t on b.uid=t.boxId;