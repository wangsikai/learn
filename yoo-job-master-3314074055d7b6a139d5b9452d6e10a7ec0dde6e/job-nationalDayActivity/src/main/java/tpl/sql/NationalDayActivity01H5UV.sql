#macro($nda01UpdateUV(h5,userId,latestViewAt,viewAtL))
UPDATE national_day_activity_01_h5uv
SET latest_view_at = :latestViewAt,view_count = view_count + 1
WHERE user_id = :userId AND h5 = :h5 AND view_at = :viewAtL
#end