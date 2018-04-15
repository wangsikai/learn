#macro($getTopicsByExam(examId))
SELECT ept.* FROM exam_paper_topic ept WHERE ept.exam_paper_id =:examId ORDER BY sequence ASC
#end

#macro($getTopicsByExams(examIds))
SELECT ept.* FROM exam_paper_topic ept WHERE ept.exam_paper_id in :examIds GROUP BY exam_paper_id,sequence ORDER BY sequence ASC
#end

#macro($updateTopic(examTopicId,examTopicName,sequence))
UPDATE exam_paper_topic ept SET ept.name=:examTopicName
WHERE ept.id=:examTopicId
#end