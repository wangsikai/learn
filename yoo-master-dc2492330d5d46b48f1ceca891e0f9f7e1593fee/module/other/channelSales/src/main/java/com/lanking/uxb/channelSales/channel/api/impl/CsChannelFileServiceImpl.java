package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.ChannelFile;
import com.lanking.uxb.channelSales.channel.api.CsChannelFileService;
import com.lanking.uxb.channelSales.channel.form.ChannelFileForm;

/**
 * 渠道资料接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
@Service
@Transactional(readOnly = true)
public class CsChannelFileServiceImpl implements CsChannelFileService {
	@Autowired
	@Qualifier("ChannelFileRepo")
	Repo<ChannelFile, Long> channelFileRepo;

	@Override
	@Transactional
	public ChannelFile save(ChannelFileForm form, long userId) {
		ChannelFile file = null;
		Date date = new Date();
		if (form.getId() != null) {
			file = channelFileRepo.get(form.getId());
		}
		if (file == null) {
			file = new ChannelFile();
			file.setCreateAt(date);
			file.setCreateId(userId);
		}

		file.setCoverId(form.getCoverId());
		file.setName(form.getName());
		file.setUri(form.getUri());
		file.setUpdateAt(date);
		file.setUpdateId(userId);
		channelFileRepo.save(file);
		return file;
	}

	@Override
	public List<ChannelFile> findAll() {
		return channelFileRepo.find("$findAll").list();
	}

	@Override
	@Transactional
	public void deleteFile(long fid) {
		ChannelFile file = channelFileRepo.get(fid);
		if (file != null) {
			channelFileRepo.delete(file);
		}
	}
}
