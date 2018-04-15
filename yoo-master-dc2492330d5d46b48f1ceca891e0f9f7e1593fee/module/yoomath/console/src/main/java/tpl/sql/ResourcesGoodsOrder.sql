##查询订单总数
#macro($zycTotalResourcesGoodsOrders(examName,examCode,resourceCategoryCode,phaseCode,type,status,starAt,endAt))
SELECT COUNT(*) as count,pay_mod,COALESCE(SUM(total_price),0) as totalPrice FROM resources_goods_order rgo where type=:type and status=:status
#if(starAt)
	and order_at > :starAt
#end
#if(endAt)
	and order_at < :endAt
#end
    and EXISTS (
SELECT rg.id from resources_goods rg INNER JOIN exam_paper ep 
ON rg.resources_id=ep.id 
#if(examName)
 AND ep.name LIKE :examName
#end 
#if(examCode)
 AND ep.id = :examCode
#end 
#if(resourceCategoryCode)
 AND ep.resource_category_code = :resourceCategoryCode 
 #end
#if(phaseCode)
 AND ep.phase_code = :phaseCode
 #end
where rgo.goods_id = rg.id)
 GROUP BY  pay_mod
#end


##批量查询试卷订单数
#macro($zycmGetGoodsOrdersCount(examName,examCode,resourceCategoryCode,phaseCode,type,status,starAt,endAt))
SELECT CONCAT(t.id,'') as id,t.name,g.price,g.price_rmb,rg.category ,COUNT(t.id) as totalOrders,SUM(CASE rgo.pay_mod WHEN 1 THEN rgo.total_price ELSE 0 END) AS totalPriceRMB, 
SUM(CASE rgo.pay_mod WHEN 2 THEN rgo.total_price ELSE 0 END) AS totalPrice FROM exam_paper t 
INNER JOIN resources_goods rg ON rg.resources_id=t.id 
INNER JOIN goods g ON g.id=rg.id
INNER JOIN resources_goods_order rgo ON rgo.goods_id=rg.id  and rgo.type=:type and rgo.status=:status 
#if(starAt)
	and order_at > :starAt
#end
#if(endAt)
	and order_at < :endAt
#end
where 1=1
#if(examCode)
 AND t.id = :examCode
#end 
#if(examName)
 AND t.name LIKE :examName
#end 
#if(resourceCategoryCode)
 AND t.resource_category_code = :resourceCategoryCode 
 #end
 #if(phaseCode)
 AND t.phase_code = :phaseCode
 #end
GROUP BY t.id
#end


