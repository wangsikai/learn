##获取需要生成纸质报告的记录
#macro($findDataProductingList(statusList))
	select * from student_paper_report_record where status in :statusList
#end

##获取需要生成PDF的记录
#macro($findDataToFileList(status))
	select * from student_paper_report_record where status=:status
#end

##更新状态
#macro($successFile(recordIds, status))
	update student_paper_report_record set status=:status where id in(:recordIds)
#end