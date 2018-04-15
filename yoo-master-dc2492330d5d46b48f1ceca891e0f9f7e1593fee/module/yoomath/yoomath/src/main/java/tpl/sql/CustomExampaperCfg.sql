## 获取所有有效批改人的手机号码
#macro($findByPaper(examPaperId))
SELECT *  FROM custom_exampaper_cfg  WHERE custom_exampaper_id=:examPaperId
#end