package net.rdyonline.android_training.greendao.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.rdyonline.android_training.orm.Room;
import net.rdyonline.android_training.orm.Speaker;
import net.rdyonline.android_training.orm.Timeslot;
import net.rdyonline.android_training.orm.dao.TimeslotDao;

public class TimeslotPopulator extends Populator {

	@Override
	public void populateData() {
		List<Speaker> speakers = mSession.getSpeakerDao().loadAll();
		List<Room> rooms = mSession.getRoomDao().loadAll();

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		endTime.add(Calendar.HOUR_OF_DAY, 1);

		int i = 1;
		do {
			if (i % rooms.size() == 0) {
				startTime.add(Calendar.HOUR_OF_DAY, 1);
				endTime.add(Calendar.HOUR_OF_DAY, 1);
			}
			addTimeslot(startTime.getTime(), endTime.getTime(),
					rooms.get(i % rooms.size()),
					speakers.get(i % speakers.size()));
			
			i++;
		} while (i < 50);
	}

	private void addTimeslot(Date startTime, Date endTime, Room room,
			Speaker speaker) {
		TimeslotDao dao = mSession.getTimeslotDao();

		Timeslot entity = new Timeslot();
		entity.setStartTime(startTime);
		entity.setEndTime(endTime);
		entity.setSpeaker(speaker.getId());
		entity.setRoom(room.getId());

		dao.insert(entity);
	}

	@Override
	public void deleteData() {
		mSession.getTimeslotDao().deleteAll();
	}

}
