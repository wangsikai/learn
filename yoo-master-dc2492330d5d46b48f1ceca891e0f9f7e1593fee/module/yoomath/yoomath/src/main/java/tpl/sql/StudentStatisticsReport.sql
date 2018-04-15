## 学生月度统计

##查询
#macro($query(subjectCode,studentId,classId,calDate,stuIds))
select t.* from student_statistics_report t where 1=1
#if(subjectCode)
	AND t.subject_code=:subjectCode
#end
#if(classId)
	AND t.class_id=:classId
#end
#if(stuIds)
	AND t.student_id in (:stuIds)
#end
#if(calDate)
	AND t.cal_date=:calDate
#end
#end

##查询指定最小年月的班级集合
#macro($getClazzByMinDate(subjectCode,studentId,minDate,maxDate,buy))
select distinct(t.class_id) from student_statistics_report t
 where t.student_id=:studentId
#if(minDate)
 AND t.cal_date>=:minDate
#end
#if(maxDate)
 AND t.cal_date<=:maxDate
#end
#if(subjectCode)
 AND t.subject_code=:subjectCode
#end
#if(buy)
 AND t.buy=:buy
#end
 order by t.class_id DESC
#end

##查询指定最小年月的年月集合
#macro($getDatesByMinDate(subjectCode,studentId,clazzId, minDate,maxDate,buy))
select distinct(date_format(t.cal_date, '%Y-%m')) from student_statistics_report t
 where t.student_id=:studentId
#if(minDate)
 AND t.cal_date>=:minDate
#end
#if(maxDate)
 AND t.cal_date<=:maxDate
#end
#if(subjectCode)
 AND t.subject_code=:subjectCode
#end
#if(clazzId)
 AND t.class_id=:clazzId
#end
#if(buy)
 AND t.buy=:buy
#end
 order by t.cal_date DESC
#end

##根据条件查询报告是否存在
#macro($existReport(studentId,calDate))
SELECT count(*) FROM student_statistics_report
WHERE student_id = :studentId AND cal_date = :calDate
#end