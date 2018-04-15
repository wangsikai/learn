## 根据试卷id查询试卷题目
#macro($zycGetExamQuestion(examId))
SELECT epq.* FROM exam_paper_question epq WHERE epq.exam_paper_id =:examId
#end

## 根据试卷id列表查询试卷题目数量
#macro($zycFindExamQuestionCount(examIds))
SELECT t.exam_paper_id, count(*) c FROM exam_paper_question t WHERE t.exam_paper_id IN :examIds GROUP BY t.exam_paper_id
#end
