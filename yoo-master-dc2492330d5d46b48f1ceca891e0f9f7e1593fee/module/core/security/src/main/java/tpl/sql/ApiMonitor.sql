## 根据api查找
#macro($findByApi(api))
SELECT * FROM api_monitor WHERE api = :api
#end

## 查找所有
#macro($getAll())
SELECT * FROM api_monitor
#end

## 更新
#macro($update(api,requestTime,failTime,costTime,updateAt))
UPDATE api_monitor 
SET 
	request_time = request_time + :requestTime,
	fail_time = fail_time + :failTime,
	cost_time = cost_time + :costTime,
	update_at = :updateAt
WHERE api = :api
#end

## 更新平均耗时
#macro($updateAvgCostTime())
UPDATE api_monitor SET avg_cost_time = cost_time/request_time;
#end