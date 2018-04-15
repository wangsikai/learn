##查询所有的知识点
#macro($findAllPoint())
SELECT * FROM knowledge_review WHERE status = 0
#end