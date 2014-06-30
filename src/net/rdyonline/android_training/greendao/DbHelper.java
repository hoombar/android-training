package net.rdyonline.android_training.greendao;

import net.rdyonline.android_training.AndroidTraining;
import net.rdyonline.android_training.orm.dao.DaoMaster;
import net.rdyonline.android_training.orm.dao.DaoMaster.DevOpenHelper;
import net.rdyonline.android_training.orm.dao.DaoSession;

/***
 * The DaoSession is always returned from this class
 * We use this to get the individual Dao session objects
 * 
 * @author rdy
 */
public class DbHelper {
	
	private static DbHelper instance;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	private final static String DB_NAME = "androidtraining.db";

	public synchronized static DbHelper getInstance() {
		if (instance == null) {
			instance = new DbHelper();
		}
		return instance;
	}

	public DbHelper() {
		DevOpenHelper devOpenHelper = new DevOpenHelper(
				AndroidTraining.getAppContext(), DB_NAME, null);

		// note that the first time this is called, any update/create
		// operations will be performed
		daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
		daoSession = daoMaster.newSession();
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

	public DaoMaster getDaoMaster() {
		return daoMaster;
	}
}
