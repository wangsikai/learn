## 查询客户端版本信息
#macro($queryVersion(app,deviceType))
select * from yoomath_client_version where status != 2
#if(app)
AND app = :app
#end
#if(deviceType)
AND device_type = :deviceType
#end
ORDER BY id DESC
#end

## 验证版本名称是否已经存在(包括删除的)
#macro($versionCount(version,app,deviceType))
select count(id) from yoomath_client_version where version =:version and status !=2 AND app = :app AND device_type = :deviceType
#end

## 获取最大的版本号
#macro($isMaxVersionNum(app,deviceType))
select MAX(version_num) from yoomath_client_version where status !=2 AND app = :app AND device_type = :deviceType
#end

## 查找发布的最新app版本信息
#macro($findOpenLatestVersion(app,deviceType))
SELECT t.* FROM yoomath_client_version t WHERE t.status = 0 AND t.app = :app AND t.device_type = :deviceType LIMIT 1
#end