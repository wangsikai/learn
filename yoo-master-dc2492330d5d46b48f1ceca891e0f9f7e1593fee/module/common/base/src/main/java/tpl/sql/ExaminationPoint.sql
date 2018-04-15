## 根据code列表查找数据
#macro($findAll(ids))
SELECT t.* FROM examination_point t WHERE
t.status = 0
#if(codes)
AND t.id IN :ids
#end
#end