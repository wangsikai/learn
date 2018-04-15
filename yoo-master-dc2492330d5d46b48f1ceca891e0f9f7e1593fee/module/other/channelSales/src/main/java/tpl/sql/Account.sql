## 根据登录名列表查找数据
#macro($csFindByNames(names))
SELECT t.* FROM account t WHERE t.name IN :names AND t.status = 0
#end