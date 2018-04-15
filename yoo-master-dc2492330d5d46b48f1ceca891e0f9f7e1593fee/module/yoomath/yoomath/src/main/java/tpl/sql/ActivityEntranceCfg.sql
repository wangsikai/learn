## 根据app获取活动入口配置信息
#macro($findByApp(app))
SELECT * FROM activity_entrance_cfg WHERE status = 0 AND app = :app
#end
