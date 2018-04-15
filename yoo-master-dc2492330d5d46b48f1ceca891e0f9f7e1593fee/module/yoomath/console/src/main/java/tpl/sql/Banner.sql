##广告位列表
#macro($list())
SELECT * FROM banner
#end

##清空广告位
#macro($delBanner())
delete FROM banner
#end

##查询广告位列表(数据组合在页面处理)
#macro($queryBannerList(app,location))
	SELECT a.* FROM (
			SELECT * FROM banner WHERE 1=1 
				#if(app)
					AND app = :app
				#else
					AND app is null
				#end
				#if(location)
					AND location = :location
				#end
				AND STATUS =2 ORDER BY sequence ASC,create_at DESC
		) a
		
		UNION ALL
		
	SELECT b.* FROM (
			SELECT * FROM banner WHERE 1=1 
				#if(app)
					AND app = :app
				#else
					AND app is null
				#end
				#if(location)
					AND location = :location
				#end
			AND STATUS in (0,1) ORDER BY FIELD(STATUS,0,1), create_at DESC
	   ) b
#end

##获取banner对应的个数
#macro($bannerCount(app,location))
SELECT * FROM banner WHERE  location =:location AND status IN (0,2)
	#if(app)
		AND app = :app
	#else
		AND app is null
	#end
#end
