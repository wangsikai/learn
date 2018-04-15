## 根据groupId列表查询所有组与商品对应关系
#macro($findByGroups(groupIds,userType))
SELECT t.* FROM coins_goods_group_goods t 
 WHERE t.group_id IN :groupIds
 #if(userType == 1)
  order by t.sequence0 ASC
 #end
 #if(userType == 2)
  order by t.sequence1 ASC
 #end
 #if(userType == 4)
  order by t.sequence2 ASC
 #end
#end