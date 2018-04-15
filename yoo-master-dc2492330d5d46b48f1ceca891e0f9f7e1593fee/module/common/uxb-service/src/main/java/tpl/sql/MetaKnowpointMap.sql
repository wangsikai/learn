## 根据父类结点找寻子节点
#macro($findByParent(code))
SELECT * FROM meta_knowpoint_map t WHERE t.pcode = :code
#end

## 根据多个父节点找寻子节点
#macro($findByParents(codes))
SELECT * FROM meta_knowpoint_map t WHERE t.pcode IN :codes
#end