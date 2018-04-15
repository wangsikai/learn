## 更新配置
#macro($csUpateParameters(pKey,pValue))
update parameter set value=:pValue where status =0 and key0=:pKey and product=1
#end
