#macro($latestVersionLog(limit))
SELECT * FROM yoomath_version_log WHERE status = 0 ORDER BY publish_at DESC limit :limit
#end
