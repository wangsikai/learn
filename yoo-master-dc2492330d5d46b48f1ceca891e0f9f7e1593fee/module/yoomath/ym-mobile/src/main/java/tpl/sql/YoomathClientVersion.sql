## 查询一个版本之上所有版本
#macro($zyFindUpVersions(version,app,deviceType))
SELECT * FROM yoomath_client_version
WHERE status = 0 AND app = :app AND version_num > :version AND device_type = :deviceType ORDER BY version ASC
#end