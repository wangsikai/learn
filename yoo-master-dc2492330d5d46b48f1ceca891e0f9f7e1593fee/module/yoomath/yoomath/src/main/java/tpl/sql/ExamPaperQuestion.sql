## 根据试卷id查询试卷题目
#macro($zyGetExamQuestion(examId))
SELECT epq.* FROM exam_paper_question epq 
inner join question q on q.id=epq.question_id AND q.status=2 AND q.del_status=0 AND q.type in (1,3,5)
WHERE epq.exam_paper_id =:examId
#end

## 根据试卷id列表查询试卷题目数量
#macro($findExamQuestionCount(examIds))
SELECT t.exam_paper_id, count(*) c FROM exam_paper_question t 
inner join question q on q.id=t.question_id AND q.status=2 AND q.del_status=0 AND q.type in (1,3,5)
WHERE t.exam_paper_id IN :examIds GROUP BY t.exam_paper_id
#end

## 根据试卷id查询可导出的试卷题目（只取单选、多选、简答）
#macro($zyListExportQuestionsByExampaper(examId))
SELECT epq.* FROM exam_paper_question epq
 INNER JOIN question q on q.id=epq.question_id AND q.status=2 AND q.del_status=0 AND q.type in (1,3,5)
 WHERE epq.exam_paper_id =:examId order by epq.sequence ASC
#end

## 根据查询书卷平均难度
#macro($findExamQuestionDifficulty(examIds))
SELECT t.exam_paper_id,round(sum(q.difficulty)/count(*),2) difficulty FROM exam_paper_question t 
INNER JOIN question q on t.question_id=q.id AND q.status=2 AND q.del_status=0 AND q.type in (1,3,5)
WHERE t.exam_paper_id =:examIds GROUP BY t.exam_paper_id
#end