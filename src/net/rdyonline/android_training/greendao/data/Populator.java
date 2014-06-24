package net.rdyonline.android_training.greendao.data;

import net.rdyonline.android_training.greendao.DbHelper;
import net.rdyonline.android_training.orm.dao.DaoSession;

public abstract class Populator {

	protected DaoSession mSession;
	
	public Populator() {
		mSession = DbHelper.getInstance().getDaoSession();
	}
	
	public abstract void populateData();
	public abstract void deleteData();
}
