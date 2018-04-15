#macro($findMetaKnowKnow(knowPointCode,metaCode))
SELECT * FROM metaknow_know WHERE 1=1
#if(knowPointCode)
	AND know_point_code = :knowPointCode
#end
#if(metaCode)
	AND meta_code = :metaCode
#end
#end