## 根据组卷id查询题型
#macro($getTopicsByExamPaperId(examPaperId))
SELECT cet.* FROM custom_exampaper_topic cet WHERE cet.custom_exampaper_id =:examPaperId ORDER BY sequence ASC
#end