#macro($getCounterDetail(biz,bizId,otherBiz,otherBizId))
SELECT * FROM counter_detail 
WHERE biz = :biz AND biz_id = :bizId AND other_biz = :otherBiz AND other_biz_id = :otherBizId
#end

#macro($getCounterDetailsByOtherBizId(biz,bizId,otherBiz,otherBizIds))
SELECT * FROM counter_detail 
WHERE biz = :biz AND biz_id = :bizId AND other_biz = :otherBiz AND other_biz_id in (:otherBizIds)
#end

#macro($getCounterDetailsByBizId(biz,bizIds,otherBiz,otherBizId))
SELECT * FROM counter_detail 
WHERE biz = :biz AND biz_id in (:bizIds) AND other_biz = :otherBiz AND other_biz_id = :otherBizId
#end

#macro($queryCounterDetailsByBizId(biz,bizIds,otherBiz,otherBizId,orderColumn,isAsc,count))
SELECT * FROM counter_detail WHERE 1=1
#if(biz)
 AND biz = :biz 
#end
#if(bizIds)
 AND biz_id in (:bizIds)
#end
#if(otherBiz)
 AND other_biz =:otherBiz
#end
#if(otherBizId)
 AND other_biz_id = :otherBizId
#end
#if(count)
 #if(count == 'COUNTER_1') AND count_1 > 0 #end
 #if(count == 'COUNTER_2') AND count_2 > 0 #end
 #if(count == 'COUNTER_3') AND count_3 > 0 #end
 #if(count == 'COUNTER_4') AND count_4 > 0 #end
 #if(count == 'COUNTER_5') AND count_5 > 0 #end
 #if(count == 'COUNTER_6') AND count_6 > 0 #end
 #if(count == 'COUNTER_7') AND count_7 > 0 #end
 #if(count == 'COUNTER_8') AND count_8 > 0 #end
 #if(count == 'COUNTER_9') AND count_9 > 0 #end
 #if(count == 'COUNTER_10') AND count_10 > 0 #end
 #if(count == 'COUNTER_20') AND count_20 > 0 #end
 #if(count == 'COUNTER_21') AND count_21 > 0 #end
 #if(count == 'COUNTER_22') AND count_22 > 0 #end
 #if(count == 'COUNTER_23') AND count_23 > 0 #end
 #if(count == 'COUNTER_24') AND count_24 > 0 #end 
#end
#if(orderColumn)
 order by 
 #if(orderColumn == 'name') name #end
 #if(orderColumn == 'update_at_1') update_at_1 #end
 #if(orderColumn == 'update_at_2') update_at_2 #end
 #if(orderColumn == 'update_at_3') update_at_3 #end
 #if(orderColumn == 'update_at_4') update_at_4 #end
 #if(orderColumn == 'update_at_5') update_at_5 #end
 #if(orderColumn == 'update_at_6') update_at_6 #end
 #if(orderColumn == 'update_at_7') update_at_7 #end
 #if(orderColumn == 'update_at_8') update_at_8 #end
 #if(orderColumn == 'update_at_9') update_at_9 #end
 #if(orderColumn == 'update_at_10') update_at_10 #end
 #if(orderColumn == 'update_at_20') update_at_20 #end
 #if(orderColumn == 'update_at_21') update_at_21 #end
 #if(orderColumn == 'update_at_22') update_at_22 #end
 #if(orderColumn == 'update_at_23') update_at_23 #end
 #if(orderColumn == 'update_at_24') update_at_24 #end
 #if(isAsc == true) ASC
 #else DESC #end
#end
#end