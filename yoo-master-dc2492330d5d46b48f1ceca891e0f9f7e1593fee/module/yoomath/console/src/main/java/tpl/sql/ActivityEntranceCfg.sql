## 根据app获取活动入口配置信息
#macro($zycFindByApp(app))
SELECT * FROM activity_entrance_cfg WHERE app = :app
#end
