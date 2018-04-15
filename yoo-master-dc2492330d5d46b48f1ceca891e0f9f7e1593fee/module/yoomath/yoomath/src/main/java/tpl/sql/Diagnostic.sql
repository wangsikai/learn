## 根据教材查询全国数据
#macro($getByTextbook(code))
SELECT t.* FROM diagno t WHERE t.textbook_code = :code
#end