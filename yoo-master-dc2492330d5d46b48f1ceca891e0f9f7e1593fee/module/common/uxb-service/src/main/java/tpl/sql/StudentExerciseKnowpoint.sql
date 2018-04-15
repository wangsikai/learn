## 根据知识点码查找
#macro($zyGetByCode(code,studentId))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.student_id = :studentId AND t.knowpoint_code = :code
#end

## 根据多个知识点查找
#macro($zyGetByCodes(codes,studentId))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.student_id = :studentId AND t.knowpoint_code IN :codes
#end

## 根据多个新知识点查找
#macro($zyGetNewByCodes(codes,studentId))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.student_id = :studentId AND t.knowpoint_code IN :codes
#end

## 根据知识点code及班级id查找
#macro($zyGetByClass(codes,classId))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.student_id IN
(SELECT student_id FROM homework_student_class WHERE class_id = :classId AND status = 0)
#if(codes)
 AND t.knowpoint_code IN :codes
#end
#end