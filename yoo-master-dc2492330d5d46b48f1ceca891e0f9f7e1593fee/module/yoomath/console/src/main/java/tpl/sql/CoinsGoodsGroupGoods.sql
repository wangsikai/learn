##通过商品id获取商品组id
#macro($getGroupIdByGoodsId(goodsId))
	SELECT group_id FROM coins_goods_group_goods WHERE  goods_id = :goodsId
#end

##批量获取商品组Id
#macro($getGroupIdsByGoodsIds(goodsIds))
	SELECT * FROM coins_goods_group_goods WHERE  goods_id in :goodsIds
#end

##清空序号
#macro($clearSequence(goodsIds))
	UPDATE coins_goods_group_goods SET sequence = null
	WHERE  goods_id = :goodsId
#end

##通过商品id获取商品组对象
#macro($getGroupGoodsByGoodsId(goodsId))
	SELECT * FROM coins_goods_group_goods WHERE  goods_id = :goodsId
#end


##通过商品组和用户类型获取当前上架商品最大的序号
#macro($getMaxSequence(groupId,userType))
	SELECT 
		#if(userType ==1)
		max(sequence0)
		#end
		#if(userType ==2)
		max(sequence1)
		#end
		#if(userType ==4)
		max(sequence2)
		#end
	FROM coins_goods_group_goods 
	WHERE  group_id = :groupId
	#if(userType ==1)
		and sequence0  is not null
	#end
	#if(userType ==2)
		and sequence1 is not null
	#end
	#if(userType ==4)
		and sequence2  is not null
	#end
#end


##更新上移序号
#macro($zycUpMove(upMoveId,sequence,userType))
	update coins_goods_group_goods set 
	#if(userType ==1)
		 sequence0 =:sequence
	#end
	#if(userType ==2)
		sequence1 =:sequence
	#end
	#if(userType ==4)
		sequence2 =:sequence
	#end
	where goods_id = :upMoveId
#end

##更新下移序号
#macro($zycDownMove(downMoveId,sequence,userType))
	update coins_goods_group_goods set 
	#if(userType ==1)
		 sequence0 =:sequence
	#end
	#if(userType ==2)
		sequence1 =:sequence
	#end
	#if(userType ==4)
		sequence2 =:sequence
	#end
	where goods_id = :downMoveId
#end

##查询到了上架时间Sequence还为空
#macro($QuerySequenceGroundGoodsOfNull(groupId,userType,nowTime))
	SELECT b.*
	FROM coins_goods_group a 
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id
	INNER JOIN goods d ON b.goods_id = d.id
	WHERE a.id = :groupId 
	#if(userType==1)
		AND c.user_type IN (1,7)
		and b.sequence0  is null
	#end
	#if(userType == 2)
		AND c.user_type IN (2,7)
		and b.sequence1 is null
	#end
	#if(userType == 4)
		AND c.user_type IN (4,7)
		and b.sequence2  is null
	#end
	and c.status = 2
	and d.sales_time <= :nowTime
	and d.soldout_time >= :nowTime
#end

##处理已经下架的
#macro($dealSequenceOffGoods(groupId,userType,nowTime))
	update coins_goods_group_goods a 
	inner join coins_goods b on a.goods_id = b.id
	inner join goods c on a.goods_id = c.id
	set
	#if(userType==1)
		a.sequence0 = null
	#end
	#if(userType == 2)
		a.sequence1 = null
	#end
	#if(userType == 4)
		a.sequence2 = null
	#end
	where a.group_id = :groupId
	#if(userType==1)
		and a.sequence0 is not null
	#end
	#if(userType == 2)
		and a.sequence1 is not null
	#end
	#if(userType == 4)
		and a.sequence2 is not null
	#end
	and b.status = 2
	and c.soldout_time < :nowTime
#end