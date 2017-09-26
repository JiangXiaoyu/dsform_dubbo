package com.dotoyo.dsform.frame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.IModel;
import com.dotoyo.dsform.model.MessageBo;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractFrame;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameUtils;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameMessage;
import com.dotoyo.ims.dsform.allin.PageListModel;
import com.dotoyo.ims.dsform.allin.WebUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FrameMessage extends AbstractFrame implements IFrameMessage {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameMessage.class));

	protected static FrameMessage instance = null;

	protected FrameMessage() {

	}

	public static FrameMessage getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameMessage();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	public Map<String, Object> getReqMessage(MessageBo bo, String msg)
			throws Exception {
		if (!StringsUtils.isEmpty(msg)) {
			if ("json".equals(bo.getReqType())) {
				return getReqMessage4json(bo, msg);
			} else if ("xml".equals(bo.getReqType())) {
				// 未实现
				FrameException bte = new FrameException("3000000000000001",
						null);
				throw bte;
			} else {
				// 不支持
				FrameException bte = new FrameException("3000000000000001",
						null);
				throw bte;
			}

		} else {

			return null;
		}
	}

	private Map<String, Object> getReqMessage4json(MessageBo bo, String msg)
			throws Exception {

		JSONObject json = JSONObject.fromObject(msg);
		@SuppressWarnings("unchecked")
		Map<String, Object> ret = (Map<String, Object>) json;
		return ret;

	}

	public String getRespMessage(MessageBo bo, Map<String, Object> param)
			throws Exception {
		if (param != null) {
			if ("json".equals(bo.getRespType())) {
				if (param instanceof JSONObject) {
					return getRespMessage4json(bo, (JSONObject) param);
				} else {
					// 未实现
					FrameException bte = new FrameException("3000000000000001",
							null);
					throw bte;
				}

			} else if ("xml".equals(bo.getRespType())) {
				// 未实现
				FrameException bte = new FrameException("3000000000000001",
						null);
				throw bte;
			} else {
				// 不支持
				FrameException bte = new FrameException("3000000000000001",
						null);
				throw bte;
			}

		} else {

			return getRespMessage4Json().toString();
		}

	}

	private String getRespMessage4json(MessageBo bo, JSONObject param)
			throws Exception {

		if (param instanceof JSONObject) {
			JSONObject json = (JSONObject) param;
			JSONObject ret = getRespMessage4Json();
			ret.put("body", json);
			return ret.toString();
		} else {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}
	}
	
	public String getRespMessage(MessageBo bo, JSONArray param)
			throws Exception {
		if (param instanceof JSONArray) {
			JSONArray array = (JSONArray) param;
			JSONObject ret = getRespMessage4Json();
			ret.put("body", array);
			return ret.toString();
		} else {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}
	}

	/**
	 * 把报文字串变为对象
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getReqMessage(HttpServletRequest req)
			throws Exception {
		//
		MessageBo bo = new MessageBo();
		bo.setReqCode(req.getAttribute("reqCode") + "");
		bo.setReqType("json");
		bo.setRespType("json");
		//
		InputStream inputStream = null;
		Reader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			inputStream = req.getInputStream();
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {

				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {

				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {

				}
			}
		}
		if (sb.length() <= 0) {
			sb.append("{}");
		}
		String str = sb.toString();
		str = URLDecoder.decode(URLDecoder.decode(str, "utf-8"), "utf-8");
		log.debug(str);

		return getReqMessage(bo, str);
	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, PageListModel model)
			throws Exception {

		if ("json".equals(bo.getRespType())) {

			return getRespMessage4Json(bo, model);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}

	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private String getRespMessage4Json(MessageBo bo, PageListModel model)
			throws Exception {

		JSONObject ret = getRespMessage4JsonList();

		if (model.getCount() > 0 && model.getCount() < 10000) {
			if (model.getModelList() != null) {
				ret.put("total", model.getCount());

				List<IModel> list = model.getModelList();
				JSONArray array = null;
				if (list instanceof JSONArray) {
					array = (JSONArray) list;

				} else {
					array = new JSONArray();
					for (int i = 0; i < list.size(); i++) {
						IModel m = list.get(i);
						JSONObject jobj = WebUtils.modelToJson(m);
						array.add(jobj);
					}
				}
				ret.put("rows", array);
			}
		}

		return ret.toString();
	}

	protected JSONObject getRespMessage4JsonList() throws Exception {

		JSONObject ret = new JSONObject();
		ret.put("code", "0000000000000000");
		ret.put("msg", FrameUtils.getWords("0000000000000000", "", null));
		ret.put("total", 0);
		ret.put("rows", "[]");
		return ret;
	}

	protected JSONObject getRespMessage4Json() throws Exception {
		JSONObject ret = new JSONObject();
		ret.put("code", "0000000000000000");
		ret.put("msg", "成功");
		ret.put("body", "{}");
		return ret;
	}

	public String getRespMessage4List(MessageBo bo, Throwable e)
			throws Exception {
		if ("json".equals(bo.getRespType())) {

			return getRespMessage4jsonList(bo, e);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}
	}

	private String getRespMessage4jsonList(MessageBo bo, Throwable e)
			throws Exception {

		JSONObject ret = getRespMessage4JsonList();
		ret.put("code", "3000000000000000");
		if (e instanceof FrameException) {
			FrameException new_name = (FrameException) e;
			ret.put("code", new_name.getCode());
		}
		ret.put("msg", e.getMessage());
		return ret.toString();
	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, String msg, long count)
			throws Exception {
		if ("json".equals(bo.getRespType())) {

			return getRespMessage4json(bo, msg, count);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}

	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private String getRespMessage4json(MessageBo bo, String msg, long count)
			throws Exception {
		JSONObject ret = new JSONObject();
		ret.put("code", "0000000000000000");
		ret.put("msg", FrameUtils.getWords("0000000000000000", "", null));
		ret.put("rows", msg);
		ret.put("total", count);
		return ret.toString();

	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, List<Map<String, String>> list,
			long total) throws Exception {
		if ("json".equals(bo.getRespType())) {

			return getRespMessage4json(bo, list, total);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}

	}

	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private String getRespMessage4json(MessageBo bo,
			List<Map<String, String>> list, long total) throws Exception {
		JSONObject ret = new JSONObject();
		ret.put("total", total);
		ret.put("rows", list);
		ret.put("code", "0000000000000000");
		ret.put("msg", "");
		return ret.toString();

	}

	@Override
	public String getRespMessage(MessageBo bo, JSONObject json, String code,
			String msg) throws Exception {
		if ("json".equals(bo.getRespType())) {

			return getRespMessage4json(bo, json, code, msg);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}

	}

	private String getRespMessage4json(MessageBo bo, JSONObject json,
			String code, String msg) throws Exception {
		JSONObject ret = getRespMessage4Json();
		ret.put("code", code);
		ret.put("msg", msg);
		ret.put("body", json);
		return ret.toString();
	}

	public String getRespMessage(MessageBo bo, IModel model) throws Exception {
		if ("json".equals(bo.getRespType())) {

			return getRespMessage4json(bo, model);
		} else if ("xml".equals(bo.getRespType())) {
			// 未实现
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		} else {
			// 不支持
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}

	}

	private String getRespMessage4json(MessageBo bo, IModel model)
			throws Exception {
		JSONObject ret = getRespMessage4Json();
		ret.put("body", WebUtils. modelToJson(model));
		return ret.toString();
	}

	public String getRespMessage(MessageBo bo) throws Exception {
		JSONObject ret = getRespMessage4Json();
		return ret.toString();
	}

	public String getRespMessage(MessageBo bo, Throwable e) throws Exception {
		JSONObject retJson = getRespMessage4Json();
		if (e instanceof FrameException) {
			FrameException et = (FrameException) e;
			retJson.put("code", et.getCode());
		} else {
			retJson.put("code", "3000000000000000");
		}
		retJson.put("msg", e == null ? "" : e.getMessage());
		return retJson.toString();
	}
	public String getRespMessage(MessageBo bo,Throwable e,JSONObject json)
	throws Exception{
		JSONObject retJson = getRespMessage4Json();
		if (e instanceof FrameException) {
			FrameException et = (FrameException) e;
			retJson.put("code", et.getCode());
		} else {
			retJson.put("code", "3000000000000000");
		}
		retJson.put("msg", e == null ? "" : e.getMessage());
		retJson.put("body", json);
		return retJson.toString();
	}
}
