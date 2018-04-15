#macro($nda01UpdatePVUV(h5,pv,uv,viewAtL))
UPDATE national_day_activity_01_h5pvuv
SET pv = pv + :pv,uv = uv + :uv
WHERE h5 = :h5 AND view_at = :viewAtL
#end