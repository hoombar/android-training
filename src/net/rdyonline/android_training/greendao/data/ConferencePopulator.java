package net.rdyonline.android_training.greendao.data;

import net.rdyonline.android_training.orm.Conference;
import net.rdyonline.android_training.orm.dao.ConferenceDao;

public class ConferencePopulator extends Populator {

	@Override
	public void populateData() {
		ConferenceDao dao = mSession.getConferenceDao();
		
		Conference entity = new Conference();
		entity.setCapacity(1000L);
		entity.setName("Demo conference");
		
		dao.insert(entity);
	}

	@Override
	public void deleteData() {
		mSession.getConferenceDao().deleteAll();
	}

}
