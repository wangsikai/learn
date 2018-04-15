##获取知识点卡片
#macro($getByCode(knowpointCode))
SELECT * FROM knowpoint_card where knowpoint_code=:knowpointCode
#end