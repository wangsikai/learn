#macro($getPhaseByCode(code,codes))
SELECT * FROM phase WHERE status = 0
#if(code)
AND code = :code
#end
#if(codes)
AND code IN (:codes)
#end
ORDER BY sequence ASC,code ASC
#end

#macro($findAllPhase())
SELECT * FROM phase WHERE status=0 ORDER BY sequence ASC,code ASC
#end
