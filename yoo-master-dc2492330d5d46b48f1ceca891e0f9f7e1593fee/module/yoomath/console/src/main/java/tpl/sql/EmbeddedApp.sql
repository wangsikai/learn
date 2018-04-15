#macro($zycList(app,location))
SELECT * FROM embedded_app WHERE location = :location
#if(app == -1)
	AND app IS NULL
#end
#if(app != -1)
	AND app = :app
#end
ORDER BY sequence ASC
#end
