package com.dotoyo.dsform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.bo.FormDataRequestBo;
import com.dotoyo.dsform.dao.inter.IFormDao;
import com.dotoyo.dsform.dao.inter.IFormModelDao;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.FormElementRequestBo;
import com.dotoyo.ims.dsform.allin.FormRequestBo;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameUtils;
import com.dotoyo.ims.dsform.allin.IFormSv;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * @author xieshh
 * 
 */
public class FormSv extends AbstractService implements IFormSv {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FormSv.class));
	private IFormDao dao;

	private IFormModelDao modelDao;

	public IFormDao getDao() {
		return dao;
	}

	public void setDao(IFormDao dao) {
		this.dao = dao;
	}

	public IFormModelDao getModelDao() {
		return modelDao;
	}

	public void setModelDao(IFormModelDao modelDao) {
		this.modelDao = modelDao;
	}

	public FormRequestBo getFormRequestBo(FormRequestBo bo) throws Exception {
		IFormModelDao formDao = (IFormModelDao) FrameUtils.getService(
				IFormModelDao.class, null);
		Map<String, Object> form = formDao.queryFormByCode(bo.getFormCode());
		if (form == null) {
			// 记录不存在
			Map<String, String> map = new HashMap<String, String>();
			map.put("condition", bo.getFormCode());
			FrameException bte = new FrameException("3000000000000002", map);
			throw bte;
		}
		bo.setTiltle((form.get("TITLE") + "").trim());
		String formId = form.get("ID") + "";
		List<Map<String, Object>> elements = formDao
				.queryFormElementByReportId(formId);
		if (elements != null) {
			bo = getFormRequestBo(bo, form, elements);
		}

		return bo;
	}

	private FormRequestBo getFormRequestBo(FormRequestBo bo,
			Map<String, Object> report, List<Map<String, Object>> elements)
			throws Exception {
		List<FormElementRequestBo> columnList = bo.getElList();
		for (int i = 0; i < elements.size(); i++) {
			Map<String, Object> element = elements.get(i);
			FormElementRequestBo elBo = new FormElementRequestBo();
			String col = element.get("COL") + "";
			String code = element.get("CODE") + "";
			if (StringsUtils.isEmpty(code)) {
				code = col;
				elBo.setItemCode(code);
			}
			elBo.setItemCode(code);
			elBo.setElType(element.get("THIS_TYPE") + "");

			// elBo.setTitle(element.get("NAME") + "");
			String temp = element.get("NAME") + "";
			if (StringsUtils.isEmpty(temp)) {
				temp = code;
			}
			elBo.setTitle(temp);
			//
			temp = element.get("IS_REQUIR") + "";
			if (!StringsUtils.isEmpty(temp)) {
				elBo.setIsRequir(temp);
			}
			//
			temp = element.get("IS_WRITE") + "";
			if (!StringsUtils.isEmpty(temp)) {
				elBo.setIsWrite(temp);
			}
			//
			temp = element.get("VALID_TYPE") + "";
			if (!StringsUtils.isEmpty(temp)) {
				elBo.setValidType(temp);
			}
			//
			temp = element.get("TIP") + "";
			if (!StringsUtils.isEmpty(temp)) {
				elBo.setTip(temp);
			}
			//
			elBo.setFormCode(bo.getFormCode());
			columnList.add(elBo);
		}
		return bo;
	}

	public static void main(String[] args) throws Exception {
		IFormSv sv = (IFormSv) FrameUtils.getService(IFormSv.class, null);
		FormRequestBo bo = new FormRequestBo();
		bo.setFormCode("testForm");
		bo = sv.getFormRequestBo(bo);
		log.debug(bo);
	}

	public FormDataRequestBo getFormDataRequestBo(FormDataRequestBo bo)
			throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> row = dao.getOnlyRow(condition);
		bo.setRow(row);
		return bo;
	}

	public Map<String, Object> getFormElement(FormElementRequestBo bo)
			throws Exception {
		IFormModelDao formDao = (IFormModelDao) FrameUtils.getService(
				IFormModelDao.class, null);
		Map<String, Object> form = formDao.queryFormByCode(bo.getFormCode());
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("formId", form.get("ID"));
		condition.put("code", bo.getItemCode());
		Map<String, Object> element = formDao.queryFormElement(condition);
		if (element == null) {
			// 记录不存在
			FrameException bte = new FrameException("3000000000000002",
					null);
			throw bte;
		}
		return element;
	}

	public List<Map<String, Object>> getFormElement(String code)
			throws Exception {
		IFormModelDao formDao = (IFormModelDao) FrameUtils.getService(
				IFormModelDao.class, null);
		Map<String, Object> form = formDao.queryFormByCode(code);
		List<Map<String, Object>> element = formDao
				.queryFormElementByFormId(form.get("ID").toString());
		if (element == null) {
			// 记录不存在
			FrameException bte = new FrameException("3000000000000002",
					null);
			throw bte;
		}
		return element;
	}

	public List<Map<String, Object>> queryFormElementByFormId(String code)
			throws Exception {
		IFormModelDao formDao = (IFormModelDao) FrameUtils.getService(
				IFormModelDao.class, null);
		return formDao.queryFormElementByFormId(code);

	}

	public List<Map<String, Object>> getFormElementParam(String thisType,
			String elementId) throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("code", elementId);
		condition.put("state", "1");
		condition.put("thisType", thisType);
		List<Map<String, Object>> row = modelDao
				.queryFormElementParam(condition);
		return row;
	}

	public Map<String, Object> queryFormElementById(String id) {
		return modelDao.queryFormElementById(id);
	}

	public Map<String, Object> queryFormById(String id) {
		return dao.getFormById(id);
	}

	public int updateForm(Map<String, String> map) {
		return dao.updateForm(map);
	}

	public int updateFormElm(Map<String, String> map) {
		return dao.updateFormElm(map);
	}
	
	public List<Map<String, Object>> queryFormElementByFormCode(String formCode) {
		return modelDao.queryFormElementByFormCode(formCode);
	}

	public List<Map<String, Object>> queryFormEleParByEleIdAndType(String elementId, String type) throws Exception {
		return modelDao.queryFormEleParByEleIdAndType(elementId,type);
	}
}
