## 根据知识点查询所有知识点卡片
#macro($findByKnowpointCode(code,cardStatus))
SELECT * FROM knowledge_point_card t WHERE t.knowpoint_code = :code
#if(cardStatus)
AND t.check_status = :cardStatus
#end
AND t.del_status < 2
#end

## 根据多个知识点查找知识点卡片
#macro($findByKnowpointCodes(codes, cardStatus))
SELECT t.* FROM knowledge_point_card t WHERE t.knowpoint_code IN :codes
#if(cardStatus)
AND t.check_status = :cardStatus
#end
AND t.del_status < 2
#end

## 更新知识点卡片状态(启用,禁用等)
#macro($updateStatus(id,status))
UPDATE knowledge_point_card SET del_status = :status WHERE id = :id
#end

## 删除知识卡片
#macro($delete(id))
UPDATE knowledge_point_card SET del_status = 2 WHERE id = :id
#end

## 更新知识点卡片校验状态(草稿、未校验、通过等)
#macro($updateCardStatus(id,status))
UPDATE knowledge_point_card SET check_status = :status WHERE id = :id
#end

## 各状态统计
#macro($statusCount(code))
SELECT count(*) AS c, check_status AS s FROM knowledge_point_card WHERE del_status = 0 AND knowpoint_code LIKE :code GROUP BY check_status
#end

## 统计题目数量
#macro($questionCount(code))
SELECT sum(question_count) AS qcount FROM knowledge_point_card WHERE del_status = 0 AND knowpoint_code LIKE :code
#end
