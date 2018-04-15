## 根据班级id查询数据
#macro($findByClass(classId,times,kpCount))
SELECT t.* FROM diagno_class_latest_hk_kp t 
WHERE t.class_id = :classId AND t.knowpoint_code > 1000000000 
 AND (t.right_count + 1)/(t.do_count + 2) <= 0.6 and t.right_rate is not null 
#if(times)
	AND times = :times
#end
ORDER BY t.right_rate ASC LIMIT :kpCount
#end

## 根据班级id查询数据
#macro($findByCodes(classId,codes))
select * from diagno_class_latest_hk_kp
where class_id = :classId
	#if(codes)
		and knowpoint_code in :codes
	#end
#end


