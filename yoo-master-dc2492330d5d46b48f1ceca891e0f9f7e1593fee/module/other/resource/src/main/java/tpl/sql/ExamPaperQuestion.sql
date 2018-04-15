#macro($getExamPaperByExamId(examId))
SELECT epq.* FROM exam_paper_question epq WHERE epq.exam_paper_id =:examId 
#end

#macro($deletExamQuestions(examId))
DELETE epq.* FROM exam_paper_question epq WHERE epq.exam_paper_id =:examId
#end

#macro($updateScoreByTopic(examTopicId,score))
UPDATE exam_paper_question epq SET epq.score=:score WHERE epq.topic_id=:examTopicId
#end

#macro($editScore(examId,questionId,score))
UPDATE exam_paper_question epq SET epq.score=:score WHERE epq.question_id=:questionId and exam_paper_id=:examId
#end

##删除试卷习题后，更新习题学校属性
#macro($removeQuestionSchool(questionIds))
UPDATE question set school_id = 0 where id in (:questionIds)
#end

## 取得一份试卷的题目数据
#macro(resconFindLastQuestion(id))
SELECT t.* FROM exam_paper_question t INNER JOIN question q ON q.id = t.question_id
WHERE t.exam_paper_id = :id ORDER BY t.sequence ASC
#end

##删除试卷与习题之间的关联
#macro($deleteQuestionFromExam(questionId))
DELETE FROM exam_paper_question WHERE question_id=:questionId
#end

##根据习题ID更新试卷的平均难度
#macro($updateExamAvgDifficultyByQuestion(questionId))
UPDATE exam_paper t
 INNER JOIN (
  SELECT ROUND(AVG(q.difficulty),2) AS avgdiff,t.id FROM exam_paper t
  INNER JOIN exam_paper_question eq ON eq.exam_paper_id=t.id
  INNER JOIN question q ON q.id=eq.question_id AND q.status=2
  WHERE t.status=2 AND
  EXISTS (
	SELECT 1 FROM exam_paper t2
	INNER JOIN exam_paper_question eq2 ON eq2.exam_paper_id=t2.id AND eq2.question_id=:questionId
	WHERE t2.id=t.id
  )
  GROUP BY t.id
 ) tb ON tb.id = t.id
 SET t.difficulty=tb.avgdiff
#end

## 替换习题
#macro($changeQuestion(examId, oldQuestionId, newQuestionId))
update exam_paper_question set question_id=:newQuestionId where exam_paper_id=:examId and question_id=:oldQuestionId
#end

## V3知识点转换-查询未转换试卷习题个数
#macro($findExamNoV3Counts(examPaperIds))
select count(1) as cont,t.exam_paper_id from exam_paper_question t
inner join question q on q.id=t.question_id and q.status=2 and q.del_status=0 AND q.same_show_id IS NULL
LEFT JOIN question_knowledge_review kr ON kr.question_id=t.question_id
LEFT JOIN question_knowledge_sync ks ON ks.question_id=t.question_id
WHERE kr.question_id IS NULL and ks.question_id is null and t.exam_paper_id in (:examPaperIds)
group by t.exam_paper_id
#end

## V3知识点转换-查询未转换试卷习题个数
#macro($findExamNoV3Count(examPaperId))
select count(1) from exam_paper_question t
inner join question q on q.id=t.question_id and q.status=2 and q.del_status=0 AND q.same_show_id IS NULL
LEFT JOIN question_knowledge_review kr ON kr.question_id=t.question_id
LEFT JOIN question_knowledge_sync ks ON ks.question_id=t.question_id
WHERE kr.question_id IS NULL and ks.question_id is null and t.exam_paper_id=:examPaperId
#end