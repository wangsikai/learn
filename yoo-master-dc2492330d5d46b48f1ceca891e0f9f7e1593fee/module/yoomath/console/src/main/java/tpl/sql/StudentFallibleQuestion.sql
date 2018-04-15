## word导出用查询
#macro($zyExportQuery(studentId, sectionCodes))
SELECT t.id as id,t.question_id as qid,t2.section_code as scode,t2.textbook_code as tcod FROM student_fallible_question t
 INNER JOIN question_section t2 ON t.question_id = t2.question_id AND SUBSTRING(t2.section_code, 1, 10) IN (:sectionCodes)
 INNER JOIN question q ON q.id = t.question_id AND q.type IN (1,2,3,5)
 WHERE t.student_id=:studentId AND t.status = 0 AND t.mistake_num > 0
 GROUP BY SUBSTRING(t2.section_code, 1, 10),t.question_id
 ORDER BY t.id ASC
#end

 
## 获取学生错题统计
#macro($getStudentSFCount(studentIds, sectionCodes))
SELECT COUNT(a.coun) AS ct,a.stuid AS sid,a.mx as mx,a.mt as mt FROM (
SELECT COUNT(t1.id) coun,t1.question_id,t1.student_id AS stuid,SUBSTRING(t2.section_code, 1, 10) AS sec1,
MAX(t1.id) AS mx,MAX(t1.update_at) as mt
FROM student_fallible_question t1 
INNER JOIN question q ON q.id=t1.question_id AND q.type IN (1,2,3,5)
INNER JOIN question_section t2 ON t1.question_id = t2.question_id AND SUBSTRING(t2.section_code, 1, 10) IN (:sectionCodes)
WHERE t1.student_id in (:studentIds)  AND t1.status = 0 AND t1.mistake_num > 0 
GROUP BY SUBSTRING(t2.section_code, 1, 10), t1.question_id, t1.student_id
) a GROUP BY a.stuid
#end