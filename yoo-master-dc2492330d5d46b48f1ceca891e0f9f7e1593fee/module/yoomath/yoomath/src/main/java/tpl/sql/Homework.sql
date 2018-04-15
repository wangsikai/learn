## 获取某个老师待处理的作业列表
#macro($zyFindTodoHomeworks(createId,courseIds,homeworkClassIds,status,limit))
SELECT * FROM homework WHERE create_id = :createId AND del_status = 0
#if(courseIds)
 AND course_id IN (:courseIds)
#end
#if(homeworkClassIds)
 AND homework_class_id IN (:homeworkClassIds)
#end
AND status IN (:status) ORDER BY status ASC,id DESC LIMIT :limit
#end

## 查询作业列表
#macro($zyQuery(createId,status,courseId,courseIds,homeworkClassId,homeworkClassIds))
SELECT * FROM homework t WHERE del_status = 0
#if(createId)
 AND t.create_id = :createId 
#end
#if(courseId)
 AND t.course_id = :courseId
#end
#if(courseIds)
 AND t.course_id IN (:courseIds)
#end
#if(homeworkClassId)
 AND t.homework_class_id = :homeworkClassId
#end
#if(homeworkClassIds)
 AND t.homework_class_id IN (:homeworkClassIds)
#end
ORDER BY t.status ASC,t.id DESC
#end

## 查询作业列表(教师移动端)
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($zyQueryForMobile(createId,status,homeworkClassId,homeworkClassIds,endTime,issueKey))
SELECT t.* FROM homework t
INNER JOIN homework_class h on h.id = t.homework_class_id
WHERE t.del_status = 0
#if(createId)
 AND t.create_id = :createId 
#end
#if(homeworkClassId)
 AND t.homework_class_id = :homeworkClassId
#end
#if(homeworkClassIds)
 AND t.homework_class_id IN (:homeworkClassIds)
#end
#if(status)
  #if(issueKey)
  		#if(createId)
 			and t.all_correct_complete = 1 and t.issue_at > h.start_at
 		#elseif
 			and t.all_correct_complete = 1
		#end
  #elseif
  		and t.status in :status
  #end
#elseif
	#if(createId)
 	AND (t.status != 3 or (t.all_correct_complete = 1 and t.issue_at > h.start_at))
	#end
#end
#if(endTime)
 AND t.create_at < :endTime
#end
ORDER BY FIELD(t.status,2,1,0,3),t.start_time DESC
#end

## 查询作业(包含假期作业 type=1 普通作业，type=2为假期作业)
#macro($zyQueryUnionHolidayHomework(createId,status,holidayHkStatus,courseId,courseIds,homeworkClassId,homeworkClassIds,type))
SELECT hk.id,hk.type FROM 
(
 #if(type == 0 || type == 1)
 SELECT id,'1' AS TYPE,create_at,create_id,del_status,homework_class_id,STATUS FROM homework WHERE status in (:status) 
 #end
 #if(type == 0)
 UNION ALL 
 #end
 #if(type == 0 || type == 2)
 SELECT id,'2' AS TYPE,create_at,create_id,del_status,homework_class_id,if(status=2,3,status) as STATUS FROM holiday_homework 
 WHERE status in (:holidayHkStatus)
 #end
)
 AS hk WHERE hk.create_id=:createId AND hk.del_status = 0 AND hk.homework_class_id 
IN (:homeworkClassIds) ORDER BY hk.STATUS ASC ,hk.id DESC 
#end

##查询教师作业跟踪
#macro($zyQueryHomeworkTracking(createId,homeworkClassIds))
SELECT hk.id,hk.type FROM 
(
 SELECT id,'1' AS TYPE,status,del_status,create_id,homework_class_id FROM homework WHERE status in (1,2)
 UNION ALL 
 SELECT id,'2' AS TYPE,if(status=2,3,status) as STATUS,del_status,create_id,homework_class_id FROM holiday_homework WHERE status =1
 )
AS hk WHERE hk.create_id=:createId AND  hk.del_status = 0 AND hk.homework_class_id 
IN (:homeworkClassIds)
ORDER BY hk.type DESC,hk.STATUS DESC ,hk.id DESC 
#end


##查询教师作业跟踪 统计需要批改和正在进行
#macro($zyQueryHomeworkTrackingCount(createId,homeworkClassIds))
SELECT hk.status,count(1) as count FROM 
(
 SELECT status,del_status,create_id,homework_class_id FROM homework WHERE status in (1,2)
 UNION ALL 
 SELECT if(status=2,3,status) as STATUS,del_status,create_id,homework_class_id FROM holiday_homework WHERE status =1
 )
AS hk WHERE hk.create_id=:createId AND hk.del_status = 0 AND hk.homework_class_id 
IN (:homeworkClassIds) GROUP BY hk.status
#end



## 计算某个老师某门课程的作业数量[统计用,其他地方勿用]
#macro($zyCountHomework(createId,courseId,classId))
SELECT count(*) FROM homework WHERE create_id = :createId AND del_status = 0
#if(courseId)
AND course_id = :courseId
#end
#if(classId)
AND homework_class_id = :classId
#end
#end

## 计算某个老师某门课程的DOING作业数量[统计用,其他地方勿用]
#macro($zyCountDoingHomework(createId,courseId,classId))
SELECT count(*) FROM homework WHERE create_id = :createId AND del_status = 0
#if(courseId)
AND course_id = :courseId
#end
#if(classId)
AND homework_class_id = :classId
#end
AND (status = 1)
#end

## 计算某个老师某门课程已下发作业的完成率[统计用,其他地方勿用]
## since 教师端v1.3.0 2017-7-5，班级整体统计不再计算已退出班级的学生
#macro($zyCountIssuedHomeworkCompletionRate(classId))
##SELECT SUM(distribute_count) distribute_count,SUM(commit_count) commit_count FROM homework WHERE del_status = 0
###if(courseId)
##AND course_id = :courseId
###end
###if(classId)
##AND homework_class_id = :classId
###end
##AND status = 3

## @since 小优快批，2018-3-9，改为已批改完成的作业
SELECT SUM(tb.distribute_count) AS distribute_count, SUM(tb.commit_count) AS commit_count FROM
(
SELECT COUNT(sh.id) AS distribute_count,0 AS commit_count FROM student_homework sh
INNER JOIN homework h ON h.id=sh.homework_id AND (h.status=3 or h.all_correct_complete = 1) AND h.del_status=0
INNER JOIN homework_class hc ON hc.id=h.homework_class_id AND hc.id=:classId
INNER JOIN homework_student_class hsc ON hsc.class_id=hc.id AND hsc.student_id=sh.student_id AND hsc.status=0
WHERE sh.del_status=0
UNION 
SELECT 0 AS distribute_count,COUNT(sh.id) AS commit_count FROM student_homework sh
INNER JOIN homework h ON h.id=sh.homework_id AND (h.status=3 or h.all_correct_complete = 1) AND h.del_status=0
INNER JOIN homework_class hc ON hc.id=h.homework_class_id AND hc.id=:classId
INNER JOIN homework_student_class hsc ON hsc.class_id=hc.id AND hsc.student_id=sh.student_id AND hsc.status=0
WHERE sh.del_status=0 AND sh.status=2 AND sh.submit_at IS NOT NULL AND sh.stu_submit_at IS NOT NULL
) tb
#end

##根据习题IDs获取查询作业
#macro($zyFindByExercise(teacherId,exerciseIds,exerciseId))
SELECT * FROM homework WHERE create_id = :teacherId
#if(exerciseIds)
	AND exercise_id IN (:exerciseIds)
	AND status != 0
#end
#if(exerciseId)
	AND exercise_id = :exerciseId
#end
ORDER BY id DESC
#if(exerciseId)
	LIMIT 1
#end
#end

##根据班级ID 获取班级近N次作业
#macro($zyGetLastHks(classId,limit))
SELECT * FROM homework WHERE homework_class_id = :classId AND (status=3 or all_correct_complete = 1) AND del_status = 0 AND right_rate IS NOT NULL ORDER BY id DESC LIMIT :limit
#end

##根据班级ID集合 获取班级近N次作业
#macro($zyMGetLastHks(classIds,limit))
SELECT * FROM homework WHERE homework_class_id in (:classIds) AND (status=3 or all_correct_complete = 1) AND del_status = 0 AND right_rate IS NOT NULL ORDER BY id DESC LIMIT :limit
#end

##获取某个班级上一个未订正的作业
#macro($zyFindLastNotCorrect(classId))
SELECT * FROM homework 
WHERE 
	homework_class_id = :classId 
AND del_status = 0
ORDER BY id DESC LIMIT 1
#end


##更新被订正的作业
#macro($zyUpdateCorrectedHomework(id))
UPDATE homework SET corrected = 1 WHERE id = :id 
#end


##更新作业的最后提交时间
#macro($zyUpdateLastCommitAt(id,autoEffectiveCount,nowDate))
UPDATE homework 
SET last_commit_at = :nowDate,commit_count = commit_count + :autoEffectiveCount 
WHERE id = :id AND last_commit_at IS NULL
#end

##更新作业状态，已下发不能删除
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($updateHomework(homeworkId,teacherId))
UPDATE homework SET del_status = 1 WHERE id = :homeworkId AND create_id = :teacherId and status != 3 and all_correct_complete != 1
#end

## 查询需要全部提交后统计的作业
#macro($zyQueryAfterLastCommitStat())
SELECT * FROM homework 
WHERE 
	del_status = 0 
AND after_lastcommit_stat = 0
AND last_commit_at IS NOT NULL
AND (status = 1 OR status = 2)
ORDER BY id ASC
#end

## 更新作业最后提交标记
## @since 小悠快批，2018-2-26，不再使用该标记
#macro($zyUdpateAfterLastCommitStat(id))
UPDATE homework SET after_lastcommit_stat = 1 WHERE id = :id AND after_lastcommit_stat = 0
#end

## 查询发布中的作业
#macro($zyQueryPublishHomework())
SELECT * FROM homework 
WHERE 
	del_status = 0 
AND status = 1
ORDER BY id ASC
#end

## 搜索历史作业
#macro($queryHomeworkHistory(userId,sectionCode,sectionName,beginTime,endTime,textBookCode,classId,isDesc,isSearchOnlineQuestions))
SELECT 
	DISTINCT(t.id) AS id, t.`create_id`,t.`exercise_id`,t.`homework_class_id`,t.`name`,t.`right_rate`,t.`homework_time`,t.`start_time`,t.`deadline`,t.`course_id`,t.`create_at`,t.`issue_at`,t.`last_commit_at`,t.`status`,t.`correcting_type`,t.`textbook_code`,t.`distribute_count`,t.`commit_count`,t.`correcting_count`,t.`question_count`,t.`difficulty`,t.`right_count`,t.`wrong_count`,t.`meta_knowpoints`,t.`last_issued`,t.`after_lastcommit_stat`,t.`corrected`,t.`del_status`,t.`man_status`,t.`half_wrong_count`,t.`has_question_answering`,t.knowledge_points,t.question_knowledge_points,t.auto_issue  
FROM(
		SELECT hk.* FROM homework hk INNER JOIN exercise e ON e.id = hk.exercise_id AND e.book_id IS null
		#if(sectionCode)
		AND e.section_code =:sectionCode
		#end
		#if(textBookCode)
		AND e.text_code =:textBookCode
		#end
		#if(sectionName)
		LEFT JOIN section s ON s.code =e.section_code 
		INNER JOIN homework_metaknow hm ON hm.homework_id=hk.id
		INNER JOIN meta_knowpoint mk ON hm.meta_code =mk.code
		#end
		WHERE 1=1 
		#if(sectionName)
		AND
		((s.name LIKE :sectionName ) OR (hk.name LIKE :sectionName) OR ( mk.name like :sectionName)
		#if(isSearchOnlineQuestions==true)
		OR (e.section_code is NULL)
		#end
		)
		#end
		#if(beginTime)
		AND hk.create_at>:beginTime
		#end
		#if(endTime)
		AND hk.create_at<:endTime
		#end
		AND hk.create_id=:userId
		#if(classId)
		AND hk.homework_class_id=:classId
		#end
		AND hk.status !=0 AND hk.del_status !=1
	UNION ALL 
		SELECT hk.* FROM homework hk INNER JOIN exercise e ON e.id = hk.exercise_id AND e.book_id IS NOT NULL 
		#if(sectionCode)
		AND e.section_code =:sectionCode
		#end
		#if(textBookCode)
		AND e.text_code =:textBookCode
		#end
		#if(sectionName)
		LEFT JOIN book_catalog b ON b.id =e.section_code 
		INNER JOIN homework_metaknow hm ON hm.homework_id=hk.id
		INNER JOIN meta_knowpoint mk ON hm.meta_code =mk.code
		#end
		WHERE 1=1 
		#if(sectionName)
		AND
		((b.name LIKE :sectionName ) OR (hk.name LIKE :sectionName) OR ( mk.name like :sectionName)
		#if(isSearchOnlineQuestions==true)
		OR (e.section_code is NULL)
		#end
		)
		#end
		#if(beginTime)
		AND hk.create_at>:beginTime
		#end
		#if(endTime)
		AND hk.create_at<:endTime
		#end
		AND hk.create_id=:userId
		#if(classId)
		AND hk.homework_class_id=:classId
		#end
		AND hk.status !=0 AND hk.del_status !=1
	) t
ORDER BY t.create_at
#if(isDesc==true)
DESC
#end
#end

## 更新作业的相关统计
#macro($zyUpdateHomeworkStat(hkId,homeworkTime,hkRightRate,rightCount,wrongCount,halfWrongCount))
UPDATE homework 
SET right_rate = :hkRightRate,homework_time = :homeworkTime,right_count = :rightCount,wrong_count = :wrongCount,half_wrong_count = :halfWrongCount
WHERE id = :hkId
#end

## 设置开始时间为当前
#macro($zySetStartTimeNow(hkId,teacherId,nowDate))
UPDATE homework 
SET start_time = :nowDate
WHERE id = :hkId
#end

##获取时间范围内布置并且下发的作业
#macro($listByTime(beginTime,endTime,createId,clazzId))
SELECT * FROM homework WHERE create_at>:beginTime AND create_at<:endTime AND deadline>:beginTime AND deadline<:endTime
AND create_id=:createId AND homework_class_id=:clazzId AND status =3
#end

##查询作业v2.0.3(web v2.0)
#macro($queryHomeworkWeb2(createId,status,homeworkClassIds,homeworkClassId,keys,bt,et,secname,line,issueKey))
select t.* FROM homework t 
inner join exercise e on e.id = t.exercise_id
inner join homework_class h on h.id = t.homework_class_id
where t.del_status = 0
#if(createId)
 AND t.create_id = :createId
#end
#if(status)
  #if(issueKey)
  		and t.status = 3 and t.issue_at > h.start_at
  #elseif
  		and t.status in :status
  #end
#elseif
 AND (t.status != 3 or (t.status = 3 and t.issue_at > h.start_at))
#end
#if(homeworkClassIds)
	AND t.homework_class_id in (:homeworkClassIds)
#end
#if(homeworkClassId)
	AND t.homework_class_id =:homeworkClassId
#end
#if(bt)
	AND (t.deadline > :bt)
#end
#if(et)
	AND (t.start_time < :et)
#end
#if(keys)
 AND (
	EXISTS (
		select 1 from homework_metaknow hm
		inner join meta_knowpoint mk on mk.code=hm.meta_code and REPLACE(mk.name,' ','') like :keys
		where hm.homework_id=t.id
	)
	or
	EXISTS (
		select 1 from section st where st.code = e.section_code and REPLACE(st.name,' ','') like :keys
	)
	or REPLACE(t.name,' ','') like :keys
 )
#end
#if(secname)
	AND (
	EXISTS (
		select 1 from section st where st.code = e.section_code and st.name like :secname
	)
	OR
	EXISTS (
		SELECT 1 FROM book_catalog bc WHERE bc.id=e.section_code AND e.book_id IS NOT NULL AND bc.name LIKE :secname
	)
	)
#end
#if(line == true)
	AND (e.section_code is NULL or e.section_code = 0)
#end
 ORDER BY FIELD(t.STATUS,2,0,1,3), t.id DESC
#end

##获取最早发布的作业时间
#macro($getFirstCreateAt(teacherId, homeworkClassIds))
SELECT h1.start_time as ct FROM homework h1 
	WHERE h1.create_id=:teacherId AND h1.del_status = 0
#if(homeworkClassIds)
	AND h1.homework_class_id in (:homeworkClassIds)
#end
	ORDER BY h1.start_time ASC LIMIT 1
#end


##获取某个班级在一定时间范围内下发的第一个作业
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($getFirstIssuedHomeworkInMonth(beginTime,endTime,classId))
SELECT * FROM homework 
WHERE issue_at > :beginTime AND issue_at < :endTime AND del_status = 0 AND (status = 3 and all_correct_complete = 1) ORDER BY issue_at ASC LIMIT 1
#end


##查询作业v2.3.0
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($getIssuedHomework(createId,limt))
select t.* FROM homework t INNER JOIN homework_class hc 
on t.homework_class_id =hc.id AND hc.status = 0 
 where t.create_id=:createId AND t.del_status = 0 AND (t.status = 3 and t.all_correct_complete = 1)
ORDER BY  t.id DESC LIMIT :limt
#end

##通过作业查询本次作业的薄弱知识点（不高于50%的,新知识点）
#macro($queryWeakByCodes(hkId,knowledges))
SELECT distinct b.knowledge_code FROM homework_question a
INNER JOIN question_knowledge b ON a.question_id = b.question_id
WHERE homework_id = :hkId AND right_rate <= 50
	#if(knowledges)
		and b.knowledge_code in (:knowledges)
	#end
#end

##通过作业查询本次作业的薄弱知识点（不高于50%的,旧知识点）
#macro($queryWeakByOldCodes(hkId,knowledges))
SELECT DISTINCT b.meta_code FROM homework_question a
INNER JOIN question_metaknow b ON a.question_id = b.question_id
WHERE homework_id = :hkId AND right_rate <= 50
	#if(knowledges)
		AND b.meta_code IN (:knowledges)
	#end
#end

##查询作业中没有自动批改的不同类型的题目数
#macro($QueryFill(hkId))
SELECT c.type,COUNT(1) COUNT FROM homework a 
INNER JOIN homework_question b ON b.homework_id = a.id
INNER JOIN question c ON b.question_id = c.id
WHERE a.id = :hkId
GROUP BY c.type 
#end

## 查找所有教师布置过的作业count
#macro($countByOriginalCreateId(originalCreateId))
SELECT count(*) FROM homework WHERE original_create_id = :originalCreateId
#end

## 查询作业列表(教师移动端)
#macro($zyQueryForMobile2(createId,status,homeworkClassId,homeworkClassIds,endTime,issueKey))
SELECT t.* FROM homework t
INNER JOIN homework_class h on h.id = t.homework_class_id
WHERE t.del_status = 0
#if(createId)
 AND t.create_id = :createId
#end
#if(homeworkClassId)
 AND t.homework_class_id = :homeworkClassId
#end
#if(homeworkClassIds)
 AND t.homework_class_id IN (:homeworkClassIds)
#end
#if(status)
  #if(issueKey)
  		and t.status = 3 and t.issue_at > h.start_at
  #elseif
  		and t.status in :status
  #end
#elseif
	#if(createId)
 	 AND (t.status != 3 or (t.status = 3 and t.issue_at > h.start_at))
	#end
#end
#if(endTime)
 AND t.create_at < :endTime
#end
ORDER BY FIELD(t.status,2,0,1,3),t.start_time DESC
#end

##删除班级后删除作业，不可用状态不删除
#macro($deleteAllHomeworkByClazz(homeworkClassId,teacherId))
UPDATE homework SET del_status = 2 WHERE homework_class_id = :homeworkClassId and del_status != 1
#end

##查询所有作业
#macro($queryAllHomeworkByClazz(homeworkClassId))
SELECT * FROM homework 
WHERE homework_class_id = :homeworkClassId
and del_status != 1
#end

## 查询最新作业
#macro($queryLastestByTeacher(createId,status))
SELECT * FROM homework h 
INNER JOIN homework_class hc ON h.homework_class_id = hc.id
WHERE h.create_id = :createId AND h.del_status = 0
#if(status)
 AND h.status IN (:status)
#end
ORDER BY h.create_at DESC
LIMIT 1
#end

## 查询作业列表(教师移动端)
#macro($zyQueryForMobile3(createId,status,homeworkClassId,
homeworkClassIds,endTime,nowTime,homeworkStatus))
SELECT distinct t.* FROM homework t
INNER JOIN homework_class h on h.id = t.homework_class_id
WHERE t.del_status = 0
#if(createId)
 AND t.create_id = :createId
#end
#if(homeworkClassId)
 AND t.homework_class_id = :homeworkClassId
#end
#if(homeworkClassIds)
 AND t.homework_class_id IN (:homeworkClassIds)
#end
#if(status)
 AND t.status in (:status)
#end
#if(endTime)
 AND t.create_at < :endTime
#end
#if(homeworkStatus)
	#if(homeworkStatus == 1)
		AND t.status = 1
	#end
	#if(homeworkStatus == 2)
		AND t.status = 2
	#end
	#if(homeworkStatus == 3)
 		AND t.tobe_corrected = 1
	#end
#end
ORDER BY FIELD(t.status,0,1,2,3),t.start_time DESC,t.id DESC
#end

## 查询未批改作业(教师移动端)
#macro($queryStudentCorrectCount(createId,homeworkIds))
select DISTINCT(h.id) from homework h
LEFT JOIN student_homework sh on h.id = sh.homework_id
LEFT JOIN student_homework_question shq on sh.id = shq.student_homework_id
where h.create_id = :createId
and shq.result = 0
and h.id IN (:homeworkIds)
#end

## 查询教师首页作业列表
#macro($zyQueryForMobileIndex(createId,status,
homeworkClassIds,endTime,nowTime,homeworkStatus))
SELECT DISTINCT t.* FROM homework t
INNER JOIN homework_class h on h.id = t.homework_class_id
WHERE t.del_status = 0
#if(createId)
 AND t.create_id = :createId
#end
#if(homeworkClassIds)
 AND t.homework_class_id IN (:homeworkClassIds)
#end
 AND 
 (
 	t.status in (0, 1)
 	OR (t.status = 2 AND t.tobe_corrected = 1)
 )
#if(endTime)
 AND t.create_at < :endTime
#end
ORDER BY FIELD(t.status,0,1,2),t.start_time DESC,t.id DESC
#end

##查询作业v2.0.3(小优快批)
#macro($queryHomeworkWeb3(createId,current,isOver,tobeCorrected,homeworkClassIds,homeworkClassId,keys,bt,et,secname,line,issueKey))
select t.* FROM homework t 
inner join exercise e on e.id = t.exercise_id
inner join homework_class h on h.id = t.homework_class_id
where t.del_status = 0
#if(createId)
 AND t.create_id = :createId
#end
#if(current)
  AND t.start_time < :current AND t.deadline > :current AND t.commit_count != t.distribute_count
#end
#if(isOver)
	AND t.commit_count = t.distribute_count
#end
#if(tobeCorrected)
	AND t.tobe_corrected = 1
#end
#if(homeworkClassIds)
	AND t.homework_class_id in (:homeworkClassIds)
#end
#if(homeworkClassId)
	AND t.homework_class_id =:homeworkClassId
#end
#if(bt)
	AND (t.deadline > :bt)
#end
#if(et)
	AND (t.start_time < :et)
#end
#if(keys)
 AND (
	EXISTS (
		select 1 from homework_metaknow hm
		inner join meta_knowpoint mk on mk.code=hm.meta_code and REPLACE(mk.name,' ','') like :keys
		where hm.homework_id=t.id
	)
	or
	EXISTS (
		select 1 from section st where st.code = e.section_code and REPLACE(st.name,' ','') like :keys
	)
	or REPLACE(t.name,' ','') like :keys
 )
#end
#if(secname)
	AND (
	EXISTS (
		select 1 from section st where st.code = e.section_code and st.name like :secname
	)
	OR
	EXISTS (
		SELECT 1 FROM book_catalog bc WHERE bc.id=e.section_code AND e.book_id IS NOT NULL AND bc.name LIKE :secname
	)
	)
#end
#if(line == true)
	AND (e.section_code is NULL or e.section_code = 0)
#end
 ORDER BY FIELD(t.STATUS,0,1,2,3), t.id DESC
#end