## 获取所有有效批改人的手机号码
#macro($zyGetAllMobile())
SELECT mobile FROM correct_user WHERE status = 0 LIMIT 20
#end