## 查找一个账户的密保问题
#macro($findByAccountId(accountId))
SELECT * FROM account_password_question WHERE account_id = :accountId ORDER BY id
#end


## 根据账号ID删除密保问题
#macro($deleteByAccount(accountId))
DELETE FROM account_password_question WHERE account_id = :accountId
#end
