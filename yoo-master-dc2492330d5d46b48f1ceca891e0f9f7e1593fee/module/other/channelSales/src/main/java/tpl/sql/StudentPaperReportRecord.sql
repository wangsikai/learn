## 根据班级查询最新一条数据
#macro($getLatestRecord(classId))
SELECT t.* FROM student_paper_report_record t
WHERE t.class_id=:classId ORDER BY t.create_at DESC LIMIT 1
#end

## 查询未读取的记录条数
#macro($countChannelNotRead(channelCode))
SELECT COUNT(t.id) FROM student_paper_report_record t
INNER JOIN user_channel uc ON uc.con_user_id=t.operator AND uc.code=:channelCode
where t.status = 2 and t.download_count is null
#end

## 查询生成记录
#macro($queryRecords(channelCode))
SELECT t.* FROM student_paper_report_record t
INNER JOIN user_channel uc ON uc.con_user_id=t.operator AND uc.code=:channelCode
where t.status = 2
order by t.create_at DESC
#end

## 设置记录已读状态
#macro($readRecords(channelCode))
UPDATE student_paper_report_record t
INNER JOIN user_channel uc ON uc.con_user_id=t.operator AND uc.code=:channelCode
SET t.download_count=0
WHERE t.status = 2 AND t.download_count is null
#end

## 下载计数
#macro($dowloadRecord(recordId))
UPDATE student_paper_report_record t set download_count=download_count+1 where id=:recordId
#end