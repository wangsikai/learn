#macro($getGoodsByResourcesId(resourcesId))
SELECT * FROM resources_goods WHERE resources_id=:resourcesId and status!=3
#end


#macro($mgetGoods(resourcesIds))
SELECT * FROM resources_goods WHERE resources_id in (:resourcesIds) and status!=3
#end
