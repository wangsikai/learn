## 根据班级id查找数据
#macro($findByClassId(classId,times))
SELECT t.* FROM diagno_class_latest_hk t WHERE t.class_id = :classId 
ORDER BY t.start_time ASC
#if(times)
	limit 0,:times
#end
#end

## 根据作业id集合查询作业对应的提交率
#macro($getSubmitRate(homeworkIds))
SELECT round(commit_count*100/distribute_count) submitRate,id,homework_class_id FROM homework where id in :homeworkIds
#end