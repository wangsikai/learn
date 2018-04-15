##根据学校ID，状态获取对应的school_book count
#macro($getSchoolBookByStatus(schoolId,status))
SELECT count(*) FROM school_book where school_id =:schoolId and status =:status
#end
