## 获取某个学生某条题目的答题历史记录
#macro($zyFindByQuestionId(studentId,questionId,limit))
SELECT * FROM student_question_answer 
WHERE student_id = :studentId AND question_id = :questionId
ORDER BY id DESC LIMIT :limit
#end

## 获取某个学生不同题目分组后的前N条答题历史记录
#macro($zyFindByQuestionIdGroup(studentId, limit, questionIds))
SELECT t.* FROM student_question_answer t
 WHERE t.student_id = :studentId and t.create_at is not null 
 #if(questionIds)
 AND t.question_id in (:questionIds)
 #end
 AND :limit > (SELECT COUNT(*) FROM student_question_answer WHERE question_id=t.question_id  
 AND student_id = t.student_id AND id > t.id)
 order by id DESC
#end

## 根据题目id以及学生id查询具体此题做题情况
#macro($zyFindStudentCondition(studentId,questionIds))
SELECT
  u.question_id,
  sum(ifnull(u.do_count, 0)) do_count,
  sum(ifnull(u.wrong_people, 0)) wrong_people,
  sum(ifnull(u.wrong_count, 0)) wrong_count
FROM (
SELECT
  z.question_id,
  CASE z.vn
  WHEN 1
    THEN z.c
  END
  AS do_count,
  CASE z.vn
  WHEN 2
  THEN z.c
  END
  AS wrong_people,
  CASE z.vn
  WHEN 3
    THEN z.c
  END
    AS wrong_count
FROM (
  SELECT count(t.question_id) c, t.question_id, 1 AS vn FROM student_question_answer t
  WHERE t.question_id IN :questionIds AND t.student_id = :studentId GROUP BY t.question_id

  UNION

  SELECT count(m.question_id) c, m.question_id, 2 AS vn FROM (
    SELECT t.question_id FROM student_question_answer t
    WHERE t.question_id IN :questionIds AND t.result = 2 GROUP BY t.question_id, t.student_id) m
    GROUP BY m.question_id

  UNION

  SELECT count(t.question_id) c, t.question_id, 3 AS vn FROM student_question_answer t WHERE t.question_id IN :questionIds AND
  t.result = 2 AND t.student_id = :studentId GROUP BY t.question_id

) z) u GROUP BY u.question_id;
#end