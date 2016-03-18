package com.onewave.backstage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onewave.backstage.dao.MenuDao;
import com.onewave.backstage.model.Cont;
import com.onewave.backstage.model.ContProvider;
import com.onewave.backstage.model.ContVideo;
import com.onewave.backstage.model.Img;
import com.onewave.backstage.model.Menu;
import com.onewave.backstage.model.MenuStructType;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.RelaMenuAndCont;
import com.onewave.backstage.model.Site;
import com.onewave.backstage.service.ContService;
import com.onewave.backstage.service.ContVideoService;
import com.onewave.backstage.service.ImgService;
import com.onewave.backstage.service.MenuService;
import com.onewave.backstage.service.RelaMenuAndContService;
import com.onewave.backstage.service.RoleService;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	private static final Logger logger = Logger
			.getLogger(MenuServiceImpl.class);

	@Autowired
	@Qualifier("menuDao")
	private MenuDao menuDao;
	@Autowired
	@Qualifier("contProviderService")
	private ContProviderServiceImpl contProviderService;
	@Autowired
	@Qualifier("contService")
	private ContService contService;
	@Autowired
	@Qualifier("contVideoService")
	private ContVideoService contVideoService;
	@Autowired
	@Qualifier("relaMenuAndContService")
	private RelaMenuAndContService relaMenuAndContService;
	@Autowired
	@Qualifier("imgService")
	private ImgService imgService;
   @Autowired
   @Qualifier("roleService")
   private RoleService roleService;


   public boolean save(Menu menu) {
		menu.setCreate_time(new Date());
		menu.setModify_time(new Date());
		return menuDao.save(menu);
	}

	public boolean update(Menu menu) {
		menu.setModify_time(new Date());
		return menuDao.update(menu);
	}

	public boolean delete(Menu menu) {
		return menuDao.delete(menu);
	}

	public boolean delete(String id) {
		return menuDao.deleteById(id);
	}

	public int countAll() {

		return menuDao.countAll();
	}

	public Menu findById(String id) {
		return menuDao.findById(id);
	}

	public List<Menu> findAll() {
		return menuDao.findAll();
	}

	public List<Menu> findAll(int firstResult, int maxResults) {
		return menuDao.findAll(firstResult, maxResults);
	}

	public List<Menu> findMenu(String site_id, long parent_id, int status) {
		return menuDao.findMenu(site_id, parent_id, status);
	}
	
	public List<Menu> findMenu(String site_ids, int parent_id, int status, String name, String ignore_id){
		return menuDao.findMenu(site_ids, parent_id, status, name,ignore_id);
	}
	
	public List<Menu> findMenu(String site_ids, int parent_id, String name) {
		return menuDao.findMenu(site_ids, parent_id, name);
	}

	public List<MenuStructType> querySiteStatus() {
		return menuDao.querySiteStatus();
	}

	public List<ContProvider> queryContProviderBySiteId(String siteId) {
		// TODO Auto-generated method stub
		return menuDao.queryContProviderBySiteId(siteId);
	}

	public long findMaxSerialBySite_idAndParent_id(String site_id,
			String parent_id) {
		// TODO Auto-generated method stub
		return menuDao.findMaxSerialBySite_idAndParent_id(site_id, parent_id);
	}

	public long modifyBetweenSerialByParentId(String site_id, String parentId,
			long smallSerial, long largeSerial, String sign) {
		return menuDao.modifyBetweenSerialByParentId(site_id, parentId,
				smallSerial, largeSerial, sign);
	}
	@Override
	public boolean copySite(String sourceSiteId, String targetSiteId) {
		try {
			if(StringUtils.isEmpty(sourceSiteId) || StringUtils.isEmpty(targetSiteId))
				return false;
			List<Menu> menus = menuDao.findAll(" site_id='"+sourceSiteId+"' and parent_id = 0 ", null);
			for(Menu menu : menus){
				Map<String,List> map = new HashMap<String,List>();
				copymenu(menu.getId(), targetSiteId, "", "",1,map);
			}
		} catch (Exception e) {
			logger.error(e, e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean copymenu(String sourceMenuId, String targetSiteId,String targetParentId, String targetMenuName) {
		Map<String,List> map = new HashMap<String,List>();
		return copymenu(sourceMenuId, targetSiteId, targetParentId, targetMenuName,1,map);
	}
	
	/**
	 * 链接栏目： 可复用的栏目，属于特殊站点site_id=0
	 * 栏目快捷方式：是一种特殊栏目，指向链接栏目的，属于普通站点
	 * sourceMenuId不能为空
	 * targetSiteId，targetParentId 不能同时为空
	 * targetParentId = -1时，targetSiteId 不能为空
	 * preData 待回调更新的混排栏目集合,
	 */
	public boolean copymenu(String sourceMenuId, String targetSiteId,String targetParentId, String targetMenuName,int step,Map<String,List> preData ) {
		try {
			//空校验,
			int i_targSiteId = -1;
			try {
				i_targSiteId = Integer.parseInt(targetSiteId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isEmpty(sourceMenuId) 
					|| (StringUtils.isEmpty(targetSiteId) && StringUtils.isEmpty(targetParentId) 
					|| (i_targSiteId < 0 && StringUtils.isEmpty(targetParentId)) ))
				return false;
			if("-1".equals(targetParentId)&& StringUtils.isEmpty(targetSiteId))
				return false;
			//1 不能复制到本身及子栏目下
			if(sourceMenuId.equals(targetParentId) || menuDao.isChild(sourceMenuId, targetParentId))
				return false;
			// 新建menu，复制源menu信息
			Menu sourceMenu = menuDao.findById(sourceMenuId);
			
			//2 栏目快捷方式，不能复制到"链接栏目站点" 下
			if("2".equals(sourceMenu.getIs_shortcut()) && Site.LINK_SITE_ID.equals(targetSiteId))
				return false;
			Menu targetPMenu = menuDao.findById(targetParentId);
			//3 栏目快捷方式，不能复制到“链接栏目”下
			if("2".equals(sourceMenu.getIs_shortcut()) && targetPMenu!=null && Site.LINK_SITE_ID.equals(targetPMenu.getSite_id()))
				return false;
			//4 栏目  不能复制   到"栏目快捷方式"下
			if(targetPMenu!=null  && "2".equals(targetPMenu.getIs_shortcut()))
				return false;
			
			
			Menu targetMenu = new Menu();
			BeanUtils.copyProperties(sourceMenu, targetMenu);
			targetMenu.setId(null);
			if (!StringUtils.isEmpty(targetMenuName)) {
				targetMenu.setTitle(targetMenuName);
			}
			if (!(StringUtils.isEmpty(targetSiteId))) {
				targetMenu.setSite_id(targetSiteId);
			}
			if (!StringUtils.isEmpty(targetParentId) && !"-1".equals(targetParentId)) {
				targetMenu.setParent_id(targetParentId);
			}else{
				targetMenu.setParent_id("0");
			}
			//本次复制的最上层栏目禁用
			if(step==1){
				targetMenu.setStatus(-1);
			}
			//栏目复制
			String targetMenuId = menuDao.saveAndReturnId(targetMenu);
			if(Integer.parseInt(targetMenuId) < 1)
				return false;
			
			//复制混排栏目内容 zl_cont zl_cont_video zl_rela_menu_and_cont
			if(preData.keySet().contains(sourceMenuId)){
				List objects = preData.get(sourceMenuId);
				if(objects!=null){
					RelaMenuAndCont menuCont = (RelaMenuAndCont)objects.get(0);
					Cont cont = (Cont) objects.get(1);
					if(menuCont!=null&& cont!=null){
						ContVideo contVideo = contVideoService.findById(cont.getId());
						if(contVideo != null){
							ContVideo newVideo = new ContVideo();
							BeanUtils.copyProperties(contVideo,newVideo);
							newVideo.setC_id(targetMenuId);
							contVideoService.save(newVideo);
						}
						cont.setId(targetMenuId);
						contService.save(cont);
						menuCont.setC_id(targetMenuId);
						relaMenuAndContService.save(menuCont);
						preData.remove(sourceMenuId);
					}
				}
			}
			

			// 复制栏目下编排内容信息
			// 特殊收集混排栏目
			List<RelaMenuAndCont> menuContList = relaMenuAndContService.findAllForMAR(sourceMenuId);
			String ids = "";
			Map<String,Cont> contMap = new HashMap<String,Cont>();
			Map<String, RelaMenuAndCont> cid_relaMap = new HashMap<String, RelaMenuAndCont>();
			for(RelaMenuAndCont menuCont: menuContList){
				ids += menuCont.getC_id() + ",";
				cid_relaMap.put(menuCont.getC_id(), menuCont);
			}
			if(!StringUtils.isEmpty(ids)){
				ids = ids.substring(0, ids.length()-1);
			}
			List<Cont> contList = contService.findAllInIds(ids);
			for(Cont cont : contList){
				contMap.put(cont.getId(), cont);
			}
			RelaMenuAndCont targetMenuCont = null;
			for (RelaMenuAndCont menuCont : menuContList) {
				targetMenuCont = new RelaMenuAndCont();
				BeanUtils.copyProperties(menuCont, targetMenuCont);
				targetMenuCont.setId(null);
				targetMenuCont.setMenu_id(targetMenuId);
				/*
				 * 混排栏目zl_cont id  "等于"  对应的栏目 zl_menu id
				 * 混排栏目复制：1) 子栏目复制，2 ) 混排栏目内容  zl_cont zl_cont_video复制 c_id = new_menu_id     3) rela_menu_and_cont复制，c_id = new_menu_id
				*/
				Cont cont = contMap.get(menuCont.getC_id());
				//17 混排栏目
				if(cont != null && 17==cont.getType()){
					//收集  targetMenuCont，cont，待获取new c_id 
					List<Object> objects = new ArrayList<Object>();
					objects.add(targetMenuCont); 
					objects.add(cont);
					preData.put(menuCont.getC_id(), objects);
				}else{
					relaMenuAndContService.save(targetMenuCont);
				}
			}
			//复制栏目图片
			List<Img> imgs = imgService.findAll(sourceMenuId,"0");//0 栏目   1 内容   2 截图
			for(Img img : imgs){
				Img newImg = new Img();
				BeanUtils.copyProperties(img, newImg);
				newImg.setId(null);
				newImg.setTarget_id(targetMenuId);
				imgService.save(newImg);
			}
			
			// 获取源栏目下子栏目
			List<Menu> subMenuList = menuDao.findMenu(sourceMenu.getSite_id(), Integer
					.valueOf(sourceMenuId).intValue(), null);

			// 复制子栏目
			for (Menu subMenu : subMenuList) {
				 copymenu(subMenu.getId(), targetSiteId, targetMenuId,
						null,++step,preData);
			}
		} catch (Exception e) {
			logger.error("copy menu exception",e);
			return false;
		}
		return true;
	}

	@Override
	public List<Menu> findByShortCutId(String id) {
		// TODO Auto-generated method stub
		return menuDao.findByShortCutId(id);
	}
	
	@Override
   public List<Menu> querySubMenuForMar(long id){
		// TODO Auto-generated method stub
		return menuDao.querySubMenuForMar(id);
	}
   public RoleService getRoleService() {
      return roleService;
   }

   public void setRoleService(RoleService roleService) {
      this.roleService = roleService;
   }

}
