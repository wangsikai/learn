## 获取活动配置信息
#macro($getActivity(code))
SELECT * FROM imperial_exam_activity WHERE code = :code AND period = 2
#end
