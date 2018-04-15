##通过易错难点查询对应的例题
#macro($findListByFallId(id))
SELECT * FROM teachassist_pc_fd_example WHERE teachassist_pc_fallibledifficult_id =:id
#end

##通过易错难点id删除对应的例题
#macro($delByFallId(id))
DELETE FROM teachassist_pc_fd_example WHERE teachassist_pc_fallibledifficult_id =:id
#end

##通过易错难点列表查询对应的例题
#macro($findListByFallIds(ids))
SELECT * FROM teachassist_pc_fd_example WHERE teachassist_pc_fallibledifficult_id IN :ids
#end