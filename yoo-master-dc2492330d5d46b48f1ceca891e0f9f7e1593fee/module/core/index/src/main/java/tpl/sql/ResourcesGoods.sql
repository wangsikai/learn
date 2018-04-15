#macro($getExamPaperGoodsByExamId(examId))
SELECT * FROM resources_goods WHERE type = 0 AND resources_id = :examId  AND status!=3
#end


#macro($mgetExamPaperGoodsByExamIds(examIds))
SELECT * FROM resources_goods WHERE type = 0 AND resources_id in (:examIds) AND status!=3
#end
