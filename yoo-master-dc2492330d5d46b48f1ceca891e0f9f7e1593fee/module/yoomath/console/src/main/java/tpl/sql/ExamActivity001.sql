## 获取活动配置信息
#macro($csGetActivity(code))
SELECT * FROM exam_activity_001 WHERE code = :code
#end