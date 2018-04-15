##用户管理查询
#macro($zyQueryUser(userType,status,sex,phaseCode,subjectCode,accountName,name,mobile,email,schoolName,channelCode,districtCodeStr,districts,memberType,activationStatus,nowTime))
SELECT t.*,d.email,d.mobile,d.name accountName,d.activation_status
			#if(memberType == 0)
				,0 as member_type
			#elseif
				,z.member_type,z.start_at,z.end_at
			#end
	FROM(
	#if(userType == 1)
		SELECT a.id,a.user_type,c1.sex,c1.create_at,a.status,a.name,a.account_id,a.user_channel_code,c1.work_at workAt,
		 	c1.phase_code,c1.duty_code,c1.title_code,c1.school_id,0 as enter_year 
		FROM USER a
		INNER JOIN teacher c1 ON a.id = c1.id 
			#if(phaseCode)
				AND c1.phase_code = :phaseCode
			#end
			#if(sex)
				AND c1.sex = :sex
			#end
			where 1 = 1
			#if(userType)
				and a.user_type = :userType
			#end
			#if(status)
				and a.status = :status
			#end
			#if(channelCode)
				and a.user_channel_code = :channelCode
			#end
			#if(name)
				and a.name like :name
			#end
	#end
	#if(userType == 2)
		SELECT a.id,a.user_type,c2.sex,c2.create_at,a.status,a.name,a.account_id,a.user_channel_code, 0 AS workAt,
			c2.phase_code,0 as duty_code,0 as title_code,c2.school_id,c2.enter_year 
		FROM USER a
		INNER JOIN student c2 ON a.id = c2.id 
		#if(phaseCode)
			AND c2.phase_code = :phaseCode
		#end
		#if(sex)
			AND c2.sex = :sex
		#end
		where 1 = 1
		#if(userType)
			and a.user_type = :userType
		#end
		#if(status)
			and a.status = :status
		#end
		#if(channelCode)
			and a.user_channel_code = :channelCode
		#end
		#if(name)
			and a.name like :name
		#end
	#end
) t 
	INNER JOIN account d ON t.account_id = d.id 
		#if(accountName)
			AND d.name like :accountName
		#end
		#if(mobile)
			AND d.mobile like :mobile
		#end
		#if(email)
			AND d.email like :email
		#end
		#if(activationStatus)
			AND d.activation_status = :activationStatus
		#end
	#if(schoolName)
	INNER JOIN school e ON t.school_id = e.id  AND e.name like :schoolName
	#elseif(districtCodeStr)
	INNER JOIN school e ON t.school_id = e.id  AND e.district_code like :districtCodeStr
	#elseif(districts)
	INNER JOIN school e ON t.school_id = e.id  AND e.district_code in :districts
	#elseif
	LEFT JOIN school e ON t.school_id = e.id
	#end
	#if(memberType)
		#if(memberType == 0)
			WHERE t.id NOT IN (SELECT user_id FROM user_member z WHERE z.member_type != 0 and DATE_FORMAT(z.end_at,'%Y-%m-%d 23:59:59') >= :nowTime)
		#elseif(memberType != 0)
			INNER JOIN user_member z ON t.id = z.user_id AND member_type = :memberType and DATE_FORMAT(z.end_at,'%Y-%m-%d 23:59:59') >= :nowTime
		#end
	#elseif
		LEFT JOIN user_member z ON t.id = z.user_id
	#end
	ORDER BY t.id DESC 
#end


##获取当前渠道下的用户数量
#macro($getStaticChannelUserCount(code))
SELECT COUNT(id) cou,user_type FROM user WHERE user_channel_code = :code GROUP BY user_type 
#end

##通过账号id查用户
#macro($getUserByAccountId1(accountId))
SELECT * FROM user WHERE account_id = :accountId
#end

##判断渠道是否有效，存在表中
#macro($isLegalChannel(channelCode))
	SELECT count(code) FROM user_channel 
	where code = :channelCode
#end

##判断渠道和学校的对应关系是否存在
#macro($isLegalSchoolChannel(channelCode,schoolId))
	select count(1) from channel_school where channel_code =:channelCode and school_id = :schoolId
#end

## 更新用户名字
#macro($updateName(id, name))
UPDATE user SET name = :name WHERE id = :id
#end

## 临时跑学生周报告要用，后续删除
#macro($taskQueryUserByWeek())
SELECT id FROM user WHERE id < :next AND user_type = 2 
ORDER BY id DESC
#end

