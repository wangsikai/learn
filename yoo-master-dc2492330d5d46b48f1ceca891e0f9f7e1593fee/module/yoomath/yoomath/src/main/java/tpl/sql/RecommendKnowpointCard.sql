## 知识点卡片推送信息
#macro($zyGetRecommendCard(userId))
select rkc.* from recommend_knowpoint_card rkc INNER JOIN  knowledge_point_card cpc on rkc.knowpointcard_id =cpc.id and 
cpc.del_status=0 and cpc.check_status=2
where rkc.user_id=:userId and rkc.status=0 LIMIT 1
#end

## 知识点卡片推送结束的最后一期
#macro($zyGetLastRecommendCard(userId))
select rkc.* from  recommend_knowpoint_card rkc INNER JOIN  knowledge_point_card cpc on rkc.knowpointcard_id =cpc.id and cpc.del_status=0 
and cpc.check_status=2 where rkc.user_id=:userId  and rkc.status=1 ORDER BY rkc.create_at LIMIT 1
#end