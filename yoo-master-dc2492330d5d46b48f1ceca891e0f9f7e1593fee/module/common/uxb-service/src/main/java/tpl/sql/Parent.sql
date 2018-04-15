## 更新签名介绍
#macro($updateIntroduce(userId,introduce))
UPDATE parent SET introduce = :introduce WHERE id = :userId
#end