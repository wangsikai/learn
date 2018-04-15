## 
#macro($taskGetStuClassKp(classId,studentId,knowpointCode))
SELECT * FROM diagno_stu_class_kp  WHERE class_id = :classId
	#if(studentId)
		AND student_id = :studentId
	#end
	#if(knowpointCode)
		and knowpoint_code = :knowpointCode
	#end
#end

## 查询各知识点topn平均掌握率
#macro($ymTopnStu(classId,studentIds))
SELECT t.knowpoint_code, sum(t.do_count) AS do_count, sum(t.right_count) AS right_count FROM diagno_stu_class_kp t WHERE class_id = :classId AND student_id IN :studentIds
GROUP BY t.knowpoint_code
#end

##看当前表是否为空
#macro($getStuClassKpCount())
SELECT count(id) FROM diagno_stu_class_kp
#end

## 删除该学生班级数据
#macro($deleteByStuId(studentId,classId))
delete from diagno_stu_class_kp where student_id = :studentId and class_id =:classId
#end
