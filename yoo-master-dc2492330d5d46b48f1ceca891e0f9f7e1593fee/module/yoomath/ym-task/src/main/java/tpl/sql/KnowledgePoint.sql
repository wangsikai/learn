## 查找知识点数据
#macro($findKnowledgePoint())
SELECT * FROM knowledge_point where  status=0 and code > :next  ORDER BY code ASC
#end