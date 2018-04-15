#macro($listEnable(app,location,locations,nowDate))
SELECT * FROM banner WHERE
1=1
#if(location)
AND location = :location
#end
#if(locations)
AND location IN :locations
#end
 AND
	(
		(
			status = 0 AND ((start_at <= :nowDate AND start_at IS NOT NULL) AND (end_at >= :nowDate AND end_at IS NOT NULL))
		)
		OR
		(
			status = 2 AND ((start_at <= :nowDate OR start_at IS NULL) AND (end_at >= :nowDate OR end_at IS NULL))
		)
	)
	#if(app == -1)
		AND app IS NULL
	#end
	#if(app != -1)
		AND app = :app
	#end
ORDER BY sequence ASC
#end