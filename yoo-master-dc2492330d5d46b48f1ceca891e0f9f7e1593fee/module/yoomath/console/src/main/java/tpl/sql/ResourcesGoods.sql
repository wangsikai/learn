#macro($zycGetGoodsByResourcesId(resourcesId))
SELECT * FROM resources_goods WHERE resources_id=:resourcesId AND  status!=3
#end

#macro($zycMgetGoods(resourcesIds))
SELECT * FROM resources_goods WHERE resources_id in (:resourcesIds)  AND  status!=3
#end

##查询资源商品
#macro($zycGetResourcesGoods(key,examCode,category,phaseCode))
select rg.* FROM resources_goods rg INNER JOIN  exam_paper ep 
on rg.resources_id=ep.id 
#if(key)
and ep.name like :key
#end
#if(category)
and ep.resource_category_code = :category
#end
#if(phaseCode)
and ep.phase_code = :phaseCode
#end
#if(examCode)
where rg.resources_id=:examCode AND rg.status!=3
#end
#end
