## 查找所有到开始时间但未发布的作业
#macro($findAllNotPublish(nowtime))
SELECT * FROM homework WHERE status = 0 AND start_time <= :nowtime AND id < :next AND del_status = 0 ORDER BY id DESC
#end

## 查找所有到截止时间但未全部提交的作业
#macro($findAllNotCommit(nowtime))
SELECT * FROM homework WHERE status = 1 AND deadline <= :nowtime AND id < :next AND del_status = 0 ORDER BY id DESC
#end

## 查找所有到指定下发时间但未下发的作业(全选择题作业在截止时间下发，其余要24小时)
## @since 小优快批，2018-2-26，新流程不再使用
#macro($findAllNotIssue(nowtime, issuetime))
SELECT * FROM homework WHERE (status = 1 or status = 2) AND auto_issue=1 AND del_status = 0
 AND deadline>=:issuetime AND deadline <= :nowtime AND id < :next ORDER BY id DESC
#end

## 查找所有需要计算正确率的作业
## @since 小优快批，2018-2-26
#macro($findAllNeedCalRightRate(lastTime))
SELECT * FROM homework where del_status = 0 and right_rate IS NULL AND status=2 AND all_correct_complete=1 AND right_rate_stat_flag=0
 AND distribute_count > 0 AND id < :next ORDER BY id DESC
#end


## 更新整份作业统计正确率标记
## @since 小优快批，2018-2-26
#macro($updateHomeworkRightRateStatFlag(homeworkId, rightRateStatFlag))
update homework set right_rate_stat_flag=:rightRateStatFlag where id=:homeworkId
#end

## 根据课程ID获取作业列表
#macro($findByCourse(courseId,status,keyword,next))
SELECT * FROM homework 
WHERE course_id = :courseId
#if(status)
AND status = :status
#end
#if(keyword)
AND name LIKE :keyword
#end
#if(next)
AND id < :next
#end
ORDER BY id DESC
#end

#macro($incrCommitCount(id))
UPDATE homework SET commit_count = commit_count + 1 WHERE id = :id AND commit_count < distribute_count
#end

## 查找所有快到截止时间若干分钟的作业（并且有学生未提交作业）
#macro($findShouldDelay(nowtime,ids))
SELECT * FROM homework WHERE status = 1 AND deadline <= :nowtime AND id < :next AND del_status = 0 AND last_commit_at IS NULL
ORDER BY id DESC
#end

##作业转让,没有下发的作业更新创建人
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($homeworkTransfer(classId,newTeacherId))
	update homework set create_id = :newTeacherId where homework_class_id = :classId and status != 3 and all_correct_complete != 1
#end

##班级未下发的作业数量
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($countNotIssue(classId))
	select count(id) from homework where status != 3 and all_correct_complete != 1 and homework_class_id = :classId and del_status = 0
#end

##添加作业批改完成的学生数量
#macro($addCorrectingCount(homeworkId))
update homework set correcting_count=correcting_count+1 where id = :homeworkId and correcting_count<distribute_count
#end

##设置作业状态
#macro($updateStatus(homeworkId, status))
update homework set status=:status where id=:homeworkId
#end
