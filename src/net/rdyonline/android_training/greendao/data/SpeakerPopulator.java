package net.rdyonline.android_training.greendao.data;

import net.rdyonline.android_training.orm.Speaker;
import net.rdyonline.android_training.orm.dao.SpeakerDao;

public class SpeakerPopulator extends Populator {

	public static class Specialism {
		public static int dagger = 1;
		public static int rope = 2;
		public static int leadPiping = 3;
		public static int candleStick = 4;
		public static int revolver = 5;
		public static int wrench = 6;
	}
	
	@Override
	public void populateData() {
		addSpeaker("Col.", "Mustard", Specialism.dagger);
		addSpeaker("Prof.", "Plum", Specialism.rope);
		addSpeaker("Miss.", "Scarlett", Specialism.leadPiping);
		
		addSpeaker("Mr.", "Green", Specialism.candleStick);
		addSpeaker("Mrs.", "Peacock", Specialism.revolver);
		addSpeaker("Mrs.", "White", Specialism.wrench);
	}
	
	private void addSpeaker(String fname, String lname, int specialism) {
		SpeakerDao dao = mSession.getSpeakerDao();
		
		Speaker entity = new Speaker();
		entity.setFname(fname);
		entity.setLname(lname);
		entity.setSpecialism(specialism);
		
		dao.insert(entity);
	}

	@Override
	public void deleteData() {
		mSession.getSpeakerDao().deleteAll();
	}
}
