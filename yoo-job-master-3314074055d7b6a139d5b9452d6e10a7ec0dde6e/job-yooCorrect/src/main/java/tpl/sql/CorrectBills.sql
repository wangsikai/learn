##查询
#macro($queryByCorrectQuestionIds(correctQuestionIds))
SELECT * FROM correct_bills
WHERE biz_id IN (:correctQuestionIds)
#end

#macro($query(correctUserId))
SELECT * FROM correct_bills b WHERE b.user_id=:correctUserId AND b.bill_type=0 order by b.create_at asc
#end

#macro($getCorrectErrorCount(correctUserId,correctQuestionIds))
SELECT count(1) FROM correct_bills b WHERE b.user_id=:correctUserId AND b.bill_type=2 AND b.biz_id in(:correctQuestionIds)
#end