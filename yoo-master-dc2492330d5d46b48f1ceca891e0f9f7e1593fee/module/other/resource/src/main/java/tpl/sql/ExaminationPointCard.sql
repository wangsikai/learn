## 根据知识点查找卡片
#macro($findByEP(epId))
SELECT * FROM examination_point_card t WHERE t.examination_point_id = :epId AND del_status = 0
#end

## 更新考点卡片校验状态(草稿、未校验、通过等)
#macro($updateCardStatus(id,status))
UPDATE examination_point_card SET check_status = :status WHERE id = :id
#end

## 考点典型题数量
#macro($questionCount(subjectCode))
SELECT sum(t.question_count) FROM examination_point t
WHERE t.subject_code = :subjectCode AND t.status = 0
#end

## 各状态统计
#macro($statusCount(code))
SELECT count(*) AS c, t.check_status AS s FROM examination_point_card t
INNER JOIN examination_point ep ON ep.id = t.examination_point_id
 WHERE t.del_status < 2 AND ep.subject_code = :code AND ep.status = 0 GROUP BY t.check_status
#end