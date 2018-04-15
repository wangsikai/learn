##查询试卷收藏夹
#macro($queryFavorite(key,createId,goodsType,districtCode,districtNull,resourceCategoryCode,isRecommend,status,year,orderBy))
select rgf.* from resources_goods_favorite rgf INNER JOIN exam_paper ep2 on rgf.resources_id=ep2.id  where rgf.create_id =:createId  
 #if(goodsType)
 AND rgf.type=:goodsType
 #end
AND EXISTS 
(select rg.id from resources_goods rg INNER JOIN exam_paper  ep 
 ON rg.resources_id=ep.id  
    AND ep.status=2
 #if(districtNull == 0)
    AND ISNULL(ep.district_code)
 #else(districtNull == 1)
    AND ep.district_code=:districtCode
 #end
 #if(otherYear == 1)
    AND ep.year <=:year
 #else(otherYear == 2)
    AND ep.year=:year
 #end   
 #if(key)
    AND ((ep.name LIKE :key) OR (ep.school_id in (select s.id from school s where s.name LIKE :key)))
 #end 
 #if(resourceCategoryCode)
 AND ep.resource_category_code in :resourceCategoryCode 
 #end
 #if(isRecommend)
 AND rg.category=:isRecommend
 #end
 #if(status)
 AND rg.status=:status 
 #end
 where rg.id=rgf.resources_goods_id)
 #if(orderBy==1)
 ORDER BY ep2.difficulty DESC
 #else(orderBy==2)
 ORDER BY ep2.difficulty ASC
 #else(orderBy==3)
 ORDER BY rgf.create_at DESC
 #else(orderBy==4)
 ORDER BY rgf.create_at ASC
 #end
#end

##通过用户,商品查询收藏
#macro($getFavoriteIdByUserId(createId,goodsId))
select id from  resources_goods_favorite  where create_id=:createId 
#if(goodsId)
 AND resources_goods_id =:goodsId
#end
#end

##通过用户,商品查询收藏
#macro($getFavoriteIdByResourcesId(createId,resourcesId))
select id from  resources_goods_favorite  where create_id=:createId 
and resources_id=:resourcesId 
#end

##通过用户,疲劳商品查询收藏
#macro($mgetFavoriteIdByResourcesId(createId,resourcesIds))
select * from  resources_goods_favorite  where create_id=:createId 
and resources_id in :resourcesIds 
#end



##通过用户,删除商品
#macro($deleteFavoriteById(createId,id))
delete from resources_goods_favorite  where create_id=:createId and id =:id
#end


