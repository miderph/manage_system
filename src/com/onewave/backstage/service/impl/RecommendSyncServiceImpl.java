package com.onewave.backstage.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.zhilink.tools.InitManager;
import net.zhilink.tools.XMLSender;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.onewave.backstage.dao.RecommendSyncDao;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContAppstore;
import com.onewave.backstage.model.RecommendSync;
import com.onewave.backstage.service.ContAppstoreService;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.RecommendSyncService;

@Service("recommendSyncService")
public class RecommendSyncServiceImpl implements RecommendSyncService {
	
	protected static final Logger logger = Logger.getLogger(RecommendSyncServiceImpl.class);
	@Autowired
    @Qualifier("recommendSyncDao")
	private RecommendSyncDao recommendSyncDao;
	@Autowired
    @Qualifier("contService")
	private ContService contService;
	@Autowired
	@Qualifier("contAppstoreService")
	private ContAppstoreService contAppstoreService;
	
	@Override
	public int countAll() {
		return recommendSyncDao.countAll();
	}

	@Override
	public boolean delete(RecommendSync stbModel) {
		return recommendSyncDao.delete(stbModel);
	}

	@Override
	public boolean delete(String id) {
		return recommendSyncDao.deleteById(id);
	}

	@Override
	public List<RecommendSync> findAll() {
		return recommendSyncDao.findAll();
	}

	@Override
	public List<RecommendSync> findAll(int firstResult, int maxResults) {
		return recommendSyncDao.findAll(firstResult, maxResults);
	}

	@Override
	public RecommendSync findById(String id) {
		return recommendSyncDao.findById(id);
	}

	@Override
	public List<RecommendSync> findAllByNames(String names) {
		return recommendSyncDao.findAll(" name in ( " + names
				+ " ) and sp_code='0001' ", "");
	}
	
	@Override
	public List<RecommendSync> findAllByCids(String c_ids) {
		return recommendSyncDao.findAll(" c_id in ( " + c_ids
				+ " ) and sp_code='0001' ", "");
	}

	@Override
	public boolean save(RecommendSync stbModel) {
		return recommendSyncDao.save(stbModel);
	}

	@Override
	public boolean update(RecommendSync stbModel) {
		return recommendSyncDao.update(stbModel);
	}

	public boolean syncAdd(String ids) throws Exception{
		
		JSONArray contents = new JSONArray();

		List<Cont> apps = contService.findAllInIds(ids);
		Map<String,Cont> apps_map = new HashMap<String,Cont>();
		String names = "";
		for (Cont app : apps) {
			names += "'" + app.getName().replace("'", "") + "'" + ",";
			apps_map.put(app.getName().replace("'", ""), app);
		}
		names = names.substring(0, names.length() - 1);
		List<RecommendSync> syncs = findAllByNames(names);
		Map<String, RecommendSync> maps = new HashMap<String, RecommendSync>();
		for (RecommendSync sync : syncs) {
			maps.put(sync.getName(), sync);
		}
		int i = 1;
		for (Cont app : apps) {
			if (i > 100) break;
			JSONObject content = new JSONObject();
			content.put("opType", getOpType(app, maps));
			content.put("syncName", app.getName());
			content.put("gameInfo", app.getDescription());
			content.put("gameLocalPath", getGameLocalPath(app));
			contents.put(content);
			i++;
		}
		String result = post(contents);
		return result_parse(result,apps_map);
	}

	private boolean result_parse(String result,Map<String, Cont> apps_map)
			throws JSONException {
		JSONObject res = new JSONObject(result);
		int authresult = res.getInt("authresult");
		if (authresult == 1) {
			JSONArray conts = res.getJSONArray("content");
			String opType = "";
			for (int c = 0; c < conts.length(); c++) {
				try{
					JSONObject cont = conts.getJSONObject(c);
					opType = cont.getString("opType");
					if ("add".equalsIgnoreCase(opType)
							|| "edit".equalsIgnoreCase(opType)) {
						// 根据名字更新,edit时只更新 result =1的
						saveOrUpdateRecommendSync(cont,apps_map);
					}else if("delete".equalsIgnoreCase(opType)){
						deleteRecommendSync(cont);
					}
				}catch(Exception e){
					logger.error(e);
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean syncDelete(String c_ids) throws Exception{
		if(StringUtils.isEmpty(c_ids)) return false;
		JSONArray contents = new JSONArray();
		List<RecommendSync> sync_list = findAllByCids(c_ids);
		int i =1;
		for(RecommendSync sync: sync_list){
			if(i> 100) break;
			try{
				JSONObject content = new JSONObject();
				content.put("opType", "delete");
				content.put("syncName", sync.getName());
				content.put("gameInfo", "");
				content.put("gameLocalPath", "");
				contents.put(content);
			}catch(Exception e){
				logger.error(e);
			}
			i++;
		}
		String result = post(contents);
		return result_parse(result,null);
		
	}

	private String post(JSONArray contents) throws Exception {
		//String url= "http://175.152.116.115:8080/smiletvpublic/GameUpdateServer";
		String url = InitManager.getSync_url();// "http://localhost:8080/QDGH_manageService/1.txt";
		String username =InitManager.getSync_username() ;//"wodou";
		String password = InitManager.getSync_password();//"wd101295@(*";
		String randomstr = UUID.randomUUID().toString();
		Map<String, String> params = new HashMap<String, String>();
		String mixpassword = DigestUtils.md5Hex(randomstr + DigestUtils.md5Hex(password));
		JSONObject datacontent = new JSONObject();
		datacontent.put("SyncDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
		datacontent.put("postType", "GameRecommendSync");
		datacontent.put("content", contents);
		params.put("username", username);
		params.put("mixpassword", mixpassword);
		params.put("randomstr", randomstr);
		params.put("datacontent", datacontent.toString());
		String result = XMLSender.post(url, params);
		return result;
	}

	private void saveOrUpdateRecommendSync(JSONObject cont,Map<String,Cont> apps_map) throws JSONException {
		if(cont==null ) return ;
		
		List<RecommendSync> syncs= findAllByNames("'"+cont.getString("syncName")+"'");
		Cont app = apps_map.get(cont.getString("syncName"));
		if (syncs == null || syncs.size()==0) {
			RecommendSync sync  = new RecommendSync();
			sync.setName(cont.getString("syncName"));
			if(app !=null) 
				sync.setC_id(app.getId());
			sync.setStatus(cont.getInt("result"));
			sync.setCreate_time(new Date());
			sync.setUpdate_time(new Date());
			sync.setSp_code("0001");
			sync.setExtra_params(getGameLocalPath(app));
			save(sync);
		} else {
			if (cont.getInt("result") == 1) {
				for(RecommendSync sync : syncs){
					sync.setStatus(cont.getInt("result"));
					if(app !=null) 
						sync.setC_id(app.getId());
					sync.setUpdate_time(new Date());
					sync.setExtra_params(getGameLocalPath(app));
					update(sync);
				}
			}
		}
	}
	
	private void deleteRecommendSync(JSONObject cont) throws JSONException{
		if(cont==null ) return ;
		if (cont.getInt("result") == 1 || cont.getInt("result")== -3) {
			List<RecommendSync> syncs= findAllByNames("'"+cont.getString("syncName")+"'");
			if (syncs != null && syncs.size()>0) {
				for(RecommendSync sync : syncs){
					delete(sync);
				}
			}
		}
	}

	private String getOpType(Cont app, Map<String, RecommendSync> maps) {
		RecommendSync sync = maps.get(app.getName());
		// 1成功，-1失败，-2失败原因是记录重复，-3失败原因是记录不存在
		if (sync != null && (sync.getStatus() == 1 || sync.getStatus() == -2)) {
			return "edit";
		}
		return "add";
	}

	private String getGameLocalPath(Cont cont) throws JSONException {
		
		JSONObject content = new JSONObject();
		content.put("contentid", cont.getId());
		content.put("contentname", cont.getName());
		content.put("contenttype", cont.getType());
		if(cont.getType()==7){
			ContAppstore app = contAppstoreService.findById(cont.getId());
			if(app!=null){
				content.put("package_name", app.getPackage_name());
			}
		}
		JSONObject json = new JSONObject();
		json.put("content", content);
		return json.toString();
	}
	public static String encoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
		return newStr;
	}
	public  static void main(String args[]){
		String  md5 = DigestUtils.md5Hex("wd101295@(*");
		md5 = DigestUtils.md5Hex("a87f885b-39bc-49ce-a9e5-4407eac2a055"+"592c062c2b5a7d8bea8fa76b5ec12ba1");
		System.out.println(md5);
	}
}
