## 获取活动配置信息
#macro($getActivity(code))
SELECT * FROM exam_activity_001 WHERE code = :code
#end