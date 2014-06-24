package net.rdyonline.android_training.greendao.data;

import net.rdyonline.android_training.orm.Conference;
import net.rdyonline.android_training.orm.Room;
import net.rdyonline.android_training.orm.dao.RoomDao;

public class RoomPopulator extends Populator {

	@Override
	public void populateData() {
		// assign them all to the first conference (example data..)
		Conference conf = mSession.getConferenceDao().loadAll().get(0);
		
		addRoom(conf, 100, "Kitchen");
		addRoom(conf, 100, "Ball Room");
		addRoom(conf, 100, "Conservatory");
		addRoom(conf, 100, "Dining room");
		addRoom(conf, 100, "Library");
	}
	
	private void addRoom(Conference conference, long capacity, String name) {
		RoomDao dao = mSession.getRoomDao();
		
		Room entity = new Room();
		entity.setConference(conference.getId());
		entity.setCapacity(capacity);
		entity.setName(name);
		
		dao.insert(entity);
	}

	@Override
	public void deleteData() {
		mSession.getRoomDao().deleteAll();
	}
}
