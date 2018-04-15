## 班级月度统计

##查询
#macro($query(subjectCode,classId,calDate,classIds))
select t.* from class_statistics_report t where t.subject_code=:subjectCode
#if(classId)
	AND t.class_id=:classId
#end
#if(classIds)
	AND t.class_id in (:classIds)
#end
#if(calDate)
	AND t.cal_date=:calDate
#end
#end

##查询指定最小年月的班级集合
#macro($getClazzByMinDate(classIds,minDate,maxDate))
select distinct(t.class_id) from class_statistics_report t
 where t.class_id in :classIds AND t.cal_date>=:minDate AND t.cal_date<=:maxDate
 order by t.class_id DESC
#end

##查询指定最小年月的年月集合
#macro($getDatesByMinDate(clazzId, minDate,maxDate))
select distinct date_format(t.cal_date, '%Y-%m') cal_date, t.version from class_statistics_report t
 where t.cal_date>=:minDate AND t.cal_date<=:maxDate
#if(clazzId)
 AND t.class_id=:clazzId
#end
 order by t.cal_date DESC
#end

##查询老师班级下最新的学情报告月份
#macro($getLastest(classId))
select MAX(date_format(t.cal_date, '%Y-%m')) from class_statistics_report t
 where t.class_id=:classId
#end

##获取该班级布置过作业的最大章节
#macro($getMaxSection(textbookCode,clazzId))
    SELECT MAX(left(b.section_code,10))  FROM diagno_class_kp a 
  INNER JOIN knowledge_section b ON a.knowpoint_code = b.knowledge_code 
  WHERE CHAR_LENGTH(a.knowpoint_code) =10 AND a.class_id = :classId
  AND b.section_code like :textbookCode
#end

##根据条件查询报告是否存在
#macro($existReport(classId,calDate))
SELECT count(*) FROM class_statistics_report
WHERE class_id = :classId AND cal_date = :calDate
#end