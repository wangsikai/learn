## 查询最大坑号 _index :坑位序号字段
#macro($getCoinsGoodsMaxSequence(_index,virtualGoodsType,nowTime))
	select
		#if(_index ==0)
		max(sequence0)
		#end
		#if(_index ==1)
		max(sequence1)
		#end
		#if(_index ==2)
		max(sequence2)
		#end
	from coins_goods cg ,goods g
	where status = 2 
	and cg.id = g.id
	and g.sales_time <= :nowTime
	and g.soldout_time >= :nowTime
	#if(virtualGoodsType)
		and  coins_goods_type = :virtualGoodsType
	#end
#end

## 分页查询金币商品
#macro($queryCoinsGoods(virtualGoodsType,userType,nowTime))
	(select cg.*,g.create_at,0 as ts
	#if(userType==1)
		,sequence0 as seqtemp
	#elseif(userType == 2)
		,sequence1 as seqtemp
	#elseif(userType == 4)
		,sequence2 as seqtemp
	#else
		,999 as seqtemp
	#end
	from coins_goods cg ,goods g
	where  cg.id = g.id
	#if(userType==1)
		and user_type in (1,7)
	#end
	#if(userType == 2)
		and user_type in (2,7)
	#end
	#if(userType == 4)
		and user_type in (4,7)
	#end
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
	and status = 2
	and g.sales_time <= :nowTime
	and g.soldout_time >=:nowTime
	)
UNION ALL 
	(select cg.* ,g.create_at,1 as ts,999 as seqtemp
	from coins_goods cg ,goods g
	where cg.id = g.id
	#if(userType==1)
		and user_type in (1,7)
	#end
	#if(userType == 2)
		and user_type in (2,7)
	#end
	#if(userType == 4)
		and user_type in (4,7)
	#end
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
	and status = 2 and g.sales_time > :nowTime
	)
UNION ALL
	(select cg.* ,g.create_at,2 as ts,999 as seqtemp
	from coins_goods cg ,goods g
	where cg.id = g.id
	#if(userType==1)
		and user_type in (1,7)
	#end
	#if(userType == 2)
		and user_type in (2,7)
	#end
	#if(userType == 4)
		and user_type in (4,7)
	#end
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
	and status = 2 and  g.soldout_time < :nowTime
	)
UNION ALL
	(select cg.* ,g.create_at,3 as ts,999 as seqtemp
	from coins_goods cg ,goods g
	where cg.id = g.id
	#if(userType==1)
		and user_type in (1,7)
	#end
	#if(userType == 2)
		and user_type in (2,7)
	#end
	#if(userType == 4)
		and user_type in (4,7)
	#end
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
	and status not in (2,3)
	) order by ts asc,seqtemp asc,create_at desc
#end

## 分页查询金币商品 senhao.wang 2017.3.21
#macro($queryCoinsGoods2(id,userType,nowTime))
	(SELECT c.* ,d.create_at,0 as ts
	#if(userType==1)
		,b.sequence0 as seqtemp
	#elseif(userType == 2)
		,b.sequence1 as seqtemp
	#elseif(userType == 4)
		,b.sequence2 as seqtemp
	#else
		,999 as seqtemp
	#end
FROM coins_goods_group a 
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id
	INNER JOIN goods d ON b.goods_id = d.id
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
	and c.status = 2
	and d.sales_time <= :nowTime
	and d.soldout_time >=:nowTime
	)

UNION ALL 
	(SELECT c.* ,d.create_at,1 as ts,999 as seqtemp
	FROM coins_goods_group a 
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id
	INNER JOIN goods d ON b.goods_id = d.id
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
	and c.status = 2
	and d.sales_time > :nowTime
	)

UNION ALL 
	(SELECT c.* ,d.create_at,2 as ts,999 as seqtemp
		FROM coins_goods_group a 
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id
	INNER JOIN goods d ON b.goods_id = d.id
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
	and c.status = 2
	and d.soldout_time < :nowTime
	)

UNION ALL 
	(SELECT c.* ,d.create_at,3 as ts,999 as seqtemp
	FROM coins_goods_group a 
	INNER JOIN coins_goods_group_goods b ON a.id = b.group_id
	INNER JOIN coins_goods c ON b.goods_id = c.id
	INNER JOIN goods d ON b.goods_id = d.id
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
	and c.status not in (2,3)
	)
	order by ts asc,seqtemp asc,create_at desc
#end

## 分页查询金币商品
#macro($batchMoveSequence(_index,virtualGoodsType,oldSequence,nowTime))
	update coins_goods cg ,goods g
	#if(_index ==0)
		set sequence0 = sequence0-1
	#end
	#if(_index ==1)
		set sequence1 = sequence1-1
	#end
	#if(_index ==2)
		set sequence2 = sequence2-1
	#end
	where cg.id = g.id
	and status = 2
	and g.sales_time <= :nowTime
	and g.soldout_time >=:nowTime
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
	#if(_index ==0)
		and user_type in (1,7)
		and  sequence0 > :oldSequence
	#end
	#if(_index ==1)
		and user_type in (2,7)
		and sequence1 > :oldSequence
	#end
	#if(_index ==2)
		and user_type in (4,7)
		and sequence2  > :oldSequence
	#end
#end

#macro($getSalingGoodsCount(virtualGoodsType,userType,nowTime))
	select count(g.id)
	from coins_goods cg ,goods g
	
	where  1=1 and cg.id = g.id
	#if(userType==1)
		and user_type in (1,7)
	#end
	#if(userType == 2)
		and user_type in (2,7)
	#end
	#if(userType == 4)
		and user_type in (4,7)
	#end
	and status = 2
	and g.sales_time <= :nowTime
	and g.soldout_time >=:nowTime
	#if(virtualGoodsType)
		and coins_goods_type = :virtualGoodsType
	#end
#end


## 查询出上架商品sequence为null商品
#macro($getSalingGoodsOfNullSequence(_index,virtualGoodsType,nowTime))
	select cg.*
	from coins_goods cg ,goods g
	where cg.id = g.id
	and status = 2
	and g.sales_time <= :nowTime
	and g.soldout_time >=:nowTime
	and coins_goods_type = :virtualGoodsType
	#if(_index ==0)
		and user_type in(1,7)	
		and sequence0  is null
	#end
	#if(_index ==1)
		and user_type in(2,7)	
		and sequence1 is null
	#end
	#if(_index ==2)
		and user_type in(4,7)
		and sequence2  is null
	#end
	order by g.create_at asc
#end

##查询出自动下架的商品
#macro($getSequenceOffGoods(_index,virtualGoodsType,nowTime))
	select cg.*
	from coins_goods cg ,goods g
	where cg.id = g.id
	and status = 2
	and g.soldout_time < :nowTime
	and coins_goods_type = :virtualGoodsType
	#if(_index ==0)
		and user_type in(1,7)
		and  sequence0  is not null order by sequence0 desc
	#end
	#if(_index ==1)
		and user_type in(2,7)
		and sequence1 is not null order by sequence1 desc
	#end
	#if(_index ==2)
		and user_type in(4,7)
		and sequence2  is not null order by sequence2 desc
	#end
	
#end

## dic=0 上移,dic=1下移,获取最近的序列号，上移或者下移的
#macro($getExchangeSeqGoods(dic,userType,sequence,virtualGoodsType,nowTime))
	select cg.*
	from coins_goods cg ,goods g
	where cg.id = g.id
	and status = 2
	and g.sales_time <= :nowTime
	and g.soldout_time >=:nowTime
	and coins_goods_type = :virtualGoodsType
	#if(userType ==1)
		and user_type in(1,7)
		#if(dic == 0)
			and sequence0 <:sequence
			order by sequence0 desc
		#end
		#if(dic == 1)
			and sequence0 > :sequence
			order by sequence0 asc
		#end
	#end
	#if(userType ==2)
		and user_type in(2,7)
		#if(dic == 0)
			and sequence1 <:sequence
			order by sequence1 desc
		#end
		#if(dic == 1)
			and sequence1 > :sequence
			order by sequence1 asc
		#end
	#end
	#if(userType ==4)
		and user_type in(4,7)
		#if(dic == 0)
			and sequence2 <:sequence
			order by sequence2 desc
		#end
		#if(dic == 1)
			and sequence2 > :sequence
			order by sequence2 asc
		#end
	#end
	limit 1
#end