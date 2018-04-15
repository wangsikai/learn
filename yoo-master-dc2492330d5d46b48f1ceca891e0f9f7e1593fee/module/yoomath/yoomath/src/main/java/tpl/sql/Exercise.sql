## 根据条件获取老师最近一次作业相关的练习
#macro($zyFindLatestOne(teacherId,textbookCode,textbookExerciseId,bookId,sectionCode,filterTextbookExerciseId,filterBookId))
SELECT * FROM exercise WHERE create_id = :teacherId
#if(textbookCode)
AND text_code = :textbookCode
#end
#if(textbookExerciseId)
AND textbook_exercise_id = :textbookExerciseId
#end
#if(filterBookId == 0)
AND ISNULL(book_id)
#end
#if(bookId)
AND book_id = :bookId
#end
#if(sectionCode)
AND section_code = :sectionCode
#end
#if(filterTextbookExerciseId == 1)
AND textbook_exercise_id IS NOT NULL
#end
ORDER BY update_at DESC LIMIT 1
#end


## 根据条件获取老师最近一次作业相关的练习(智能)
#macro($zyFindNoBookIdLatestOne(teacherId,textbookCode,textbookExerciseId,bookId,sectionCode,filterTextbookExerciseId))
SELECT * FROM exercise WHERE create_id = :teacherId
#if(textbookCode)
AND text_code = :textbookCode
#end
#if(textbookExerciseId)
AND textbook_exercise_id = :textbookExerciseId
#end
#if(bookId)
AND book_id = :bookId
#end
#if(sectionCode)
AND section_code = :sectionCode
#end
#if(filterTextbookExerciseId == 1)
AND textbook_exercise_id IS NOT NULL
#end
ORDER BY update_at DESC LIMIT 1
#end

## 计算某个老师使用此预置习题页当天已经布置过几次
#macro($zyCountTodayExercise(teacherId,textbookExerciseId,sectionCode))
SELECT count(id) FROM exercise WHERE create_id = :teacherId 
#if(textbookExerciseId)
AND textbook_exercise_id = :textbookExerciseId 
#end
#if(sectionCode)
AND section_code = :sectionCode
#end
AND date(create_at) = curdate()
#end

## 查询历史作业的对应习题页(作业状态为下发状态)
#macro($zyQuery(teacherId,sectionCode))
SELECT t1.* FROM exercise t1 INNER JOIN homework t2 ON t1.id = t2.exercise_id
WHERE t1.create_id = :teacherId AND t1.section_code = :sectionCode AND t2.status !=0 AND t1.textbook_exercise_id IS NOT NULL
ORDER BY t1.id DESC
#end