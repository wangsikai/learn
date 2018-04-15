#macro($getTextBook(code))
SELECT * FROM textbook WHERE code =:code and status =0
#end


#macro($getTextBooks(codes))
SELECT * FROM textbook WHERE code in :codes and status =0
#end

