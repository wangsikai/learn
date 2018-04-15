#macro($getCredentialByPersonId(uid, type,product))
SELECT * FROM credential where uid=:uid AND product=:product
#if(type)
 AND type=:type
#end
#end

#macro($getCredentialByAccountId(accountId, type,product))
SELECT * FROM credential where account_id=:accountId AND product=:product
#if(type)
 AND type=:type
#end
#end

#macro($getCredentialByUserId(userId,type,product))
SELECT t.* FROM credential t INNER JOIN account a ON a.id = t.account_id INNER JOIN user u ON u.account_id = a.id
WHERE u.id = :userId
  #if(type)
      AND t.type = :type
  #end
  #if(product)
      AND t.product = :product
  #end
#end

## 删除凭证
#macro($deleteCredential(accountId, type,product))
DELETE FROM credential where account_id = :accountId AND type=:type AND product=:product
#end
