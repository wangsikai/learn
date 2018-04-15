##根据学生的id和章节码得到章节的掌握情况
#macro($ymGetBySectionCodeAndStudentId(studentId,sectionCode))
SELECT t.* FROM student_exercise_section t WHERE t.student_id = :studentId AND t.section_code = :sectionCode
#end

##根据学生的id和章节码得到章节的掌握情况
#macro($ymGetBySectionCodesAndStudentId(studentId,sectionCodes))
SELECT t.* FROM student_exercise_section t WHERE t.student_id = :studentId AND t.section_code IN :sectionCodes
#end

##根据学生id及章节码查找
#macro($taskGetBySection(userId,sectionCode))
SELECT t.* FROM student_exercise_section t WHERE t.student_id = :userId AND t.section_code = :sectionCode
#end

##根据学生id及章节码列表查找
#macro($taskGetBySections(userId,sectionCodes,lastMonth))
SELECT t.* FROM student_exercise_section t WHERE t.student_id = :userId AND t.section_code IN :sectionCodes
#if(lastMonth)
AND t.last_month =:lastMonth
#end
#end

## 根据班级及章节查询学生的掌握情况
#macro($taskFindByClassIdAndSectionCode(classId,sectionCode,lastMonth,curMonth))
select t.* from student_exercise_section t
INNER JOIN student s ON t.student_id = s.id
INNER JOIN homework_student_class h ON h.student_id = s.id
WHERE h.class_id = :classId AND h.status = 0 AND t.section_code LIKE :sectionCode
#if(lastMonth)
AND t.last_month =:lastMonth
#end
#if(curMonth)
AND t.cur_month =:curMonth
#end
#end