## 获取用户所有的礼包数量
#macro($csCountGift(userId,code))
SELECT count(*) FROM exam_activity_001_user_q WHERE user_id=:userId and activity_code =:code
#end

## 获取用户所有的礼包
#macro($csGetGifts(userId,code))
SELECT * FROM exam_activity_001_user_q WHERE user_id=:userId and activity_code =:code
#end



