#macro($updateExamStatus(examId,status))
UPDATE exam_paper SET status=:status WHERE id=:examId
#end

## 统计各阶段各学科的试卷总数
#macro($resconCountStatistic())
SELECT count(*) AS num, s.name AS subject_name, p.name AS phase_name, e.id, s.code FROM subject s
LEFT JOIN exam_paper e ON s.code = e.subject_code
INNER JOIN phase p ON s.phase_code = p.code
WHERE p.code > 1
GROUP BY s.code
ORDER BY s.code ASC
#end

## 统计某个学科的各状态总数
#macro($resconCountBySubject(subjectCode))
SELECT count(*) AS num, e.status FROM exam_paper e
WHERE e.subject_code = :subjectCode GROUP BY e.status
#end

## 统计试卷中题目录入和校验两种状态
#macro($resconCountQuestionStatus(ids))
SELECT count(*) num, q.status, t.id FROM exam_paper t
INNER JOIN exam_paper_question ep ON t.id = ep.exam_paper_id
INNER JOIN question q ON ep.question_id = q.id
WHERE q.del_status = 0 AND t.id IN :ids
GROUP BY q.status, t.id
#end