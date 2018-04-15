##获取商品组列表
#macro($zycList())
	SELECT * FROM coins_goods_group WHERE  status = 0 order by sequence
#end

##删除商品组
#macro($zycDelGroup(id))
	UPDATE coins_goods_group SET status = 1 WHERE id = :id
#end

##查询当前商品组下已上架的个数
#macro($publishCountInGroup(id,userType,nowTime))
	SELECT COUNT(1) FROM coins_goods_group a
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id AND c.status =2
	INNER JOIN goods d ON b.goods_id = d.id AND sales_time < :nowTime AND soldout_time > :nowTime
	WHERE a.id = :id
	#if(userType==1)
		AND c.user_type IN (1,7)
	#end
	#if(userType == 2)
		AND c.user_type IN (2,7)
	#end
	#if(userType == 4)
		AND c.user_type IN (4,7)
	#end
#end

##更新商品组上移的序号
#macro($zycUpMove(upMoveId,sequence))
	update coins_goods_group 
	set sequence = :sequence
	where id = :upMoveId
#end

##更新商品组下移的序号
#macro($zycDownMove(upMoveId,sequence))
	update coins_goods_group 
	set sequence = :sequence
	where id = :downMoveId
#end

##获取商品组最大序号
#macro($getMaxSequence())
	select Max(sequence) from coins_goods_group
#end