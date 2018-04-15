#macro($versionQuery())
SELECT * FROM yoomath_version_log WHERE status in (0,1) ORDER BY publish_at DESC
#end
