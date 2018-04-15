#macro($getByOperate(examId,types,limit))
SELECT h.* FROM exam_paper_history h WHERE h.exam_paper_id =:examId AND h.type in (:types)
ORDER BY h.create_at DESC limit :limit
#end

#macro($mgetByOperate(examIds,types))
SELECT h.* FROM exam_paper_history h WHERE h.exam_paper_id in :examIds AND h.type in (:types)
GROUP BY h.exam_paper_id,h.creata_at
ORDER BY h.create_at ASC
#end


