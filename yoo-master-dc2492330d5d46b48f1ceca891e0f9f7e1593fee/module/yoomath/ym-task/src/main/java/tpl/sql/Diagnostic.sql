## 根据textbookcode查找
#macro($ymGet(textbookCode))
SELECT t.* FROM diagno t WHERE t.textbook_code = :textbookCode
#end

## 根据textbookcode查找
#macro($ymGets(textbookCodes))
SELECT t.* FROM diagno t WHERE t.textbook_code in (:textbookCodes)
#end