package com.dotoyo.ims.dsform.allin;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class FrameCenter extends AbstractFrame implements IFrameCenter {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCenter.class));

	protected static FrameCenter instance = null;

	private ThreadLocal<String> local = new ThreadLocal<String>();

	protected FrameCenter() {

	}

	public static FrameCenter getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameCenter();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCurrentCenter() throws Exception {
		String centerId = local.get();
		if (StringsUtils.isEmpty(centerId)) {
			IFrameAuthority fa = FrameFactory.getAuthorityFactory(null);
			UserLoginBo bo = fa.getLoginUser(null);
			if (bo == null) {
				return "";
			}
			return bo.getCenter();
		} else {
			return centerId;
		}
	}

	public void setCurrentCenter(String centerId) throws Exception {
		local.set(centerId);
	}

	public String getTableSubNameByMonth() throws Exception {

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		return String.format("%s%s", year, month);

	}

	@Override
	public String getCenterByBusiId(String busiId) throws Exception {
		CenterModel model = new CenterModel();
		model.setId(busiId);
		IFrameService fsv = FrameFactory.getServiceFactory(null);

		ICenterSv sv = (ICenterSv) fsv.getService("com.dotoyo.frame.center.service.inter.ICenterSv");
		model = sv.getCenterById(busiId);
		if (model != null) {
			return model.getCenter();
		}
		return "";
	}

}
