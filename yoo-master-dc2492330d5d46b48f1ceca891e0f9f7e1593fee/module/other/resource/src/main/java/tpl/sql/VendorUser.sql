#macro($getVendorUserR(vendorId,userType,status))
SELECT * FROM vendor_user WHERE vendor_id =:vendorId
#if(userType)
 AND type in (:userType)
#end
#if(status)
 AND status in (:status)
#end
 order by status asc, id ASC
#end

#macro($getVendorUserCountR(vendorId,status,userType))
SELECT count(*) FROM vendor_user WHERE vendor_id =:vendorId
#if(status)
AND status = :status 
#end
#if(userType)
AND type = :userType 
#end
#end

