## 根据key0获取
#macro($findByKey(product,key0,keys))
SELECT * FROM parameter where status=0 AND product = :product
#if(key0)
AND key0 = :key0
#end
#if(keys)
AND key0 IN (:keys)
#end
#end

## 查询所有
#macro($listAll())
SELECT * FROM parameter where  status=0
#end



