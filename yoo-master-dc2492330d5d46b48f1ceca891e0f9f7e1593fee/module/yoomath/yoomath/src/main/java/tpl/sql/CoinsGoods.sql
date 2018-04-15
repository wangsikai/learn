## 获取所有金币商品
#macro($listAll(userType,limit,nowDate))
SELECT a.* FROM coins_goods a INNER JOIN goods b ON a.id = b.id
WHERE (a.user_type = :userType OR a.user_type = 7) 
AND a.status = 2 AND b.sales_time <= :nowDate AND b.soldout_time >= :nowDate AND coins_goods_type <= 1
#if(userType == 1)
order by a.sequence0 ASC
#end
#if(userType == 2)
order by a.sequence1 ASC
#end
#if(userType == 4)
order by a.sequence2 ASC
#end
LIMIT :limit
#end


## 根据组别id获取所有金币商品
#macro($listAllByGroup(groupIds,userType,limit,nowDate))
SELECT a.* FROM coins_goods a INNER JOIN goods b ON a.id = b.id
INNER JOIN coins_goods_group_goods gg ON a.id = gg.goods_id
INNER JOIN coins_goods_group cg ON cg.id = gg.group_id
WHERE (a.user_type = :userType OR a.user_type = 7)
AND a.status = 2 AND b.sales_time <= :nowDate AND b.soldout_time >= :nowDate
AND cg.id IN :groupIds
#if(userType == 1)
order by a.sequence0 ASC
#end
#if(userType == 2)
order by a.sequence1 ASC
#end
#if(userType == 4)
order by a.sequence2 ASC
#end
LIMIT :limit
#end
