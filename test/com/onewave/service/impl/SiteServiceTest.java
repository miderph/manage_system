package com.onewave.service.impl;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.onewave.backstage.model.Site;
import com.onewave.backstage.service.SiteService;
import com.onewave.common.base.TestBase;

public class SiteServiceTest extends TestBase {
	
	@Autowired  @Qualifier("siteService")
	private SiteService siteService;
	
	
	@Test
    public void testSave() {
		long beforeCount = siteService.countAll();
        
		Site site = generateRandomModel();
		
		siteService.save(site);
        
		long afterCount = siteService.countAll();
        
        assertEquals(beforeCount + 1, afterCount);
        
    }
	
	
	@Test
    public void testUpdate() {
        
		Site site = generateRandomModel();
		
		siteService.save(site);
		
        String expectedDescription = "123234";
        
        site.setName(expectedDescription);
        
        siteService.update(site);
        
        String actualDescription = siteService.findById(site.getId()).getName();
        
        assertEquals(expectedDescription, actualDescription);
        
    }
	
	@Test
    public void testDelete() {

        long beforeCount = siteService.countAll();
        
        Site site = generateRandomModel();
        
        siteService.save(site);
        
        siteService.delete(site.getId());
        
        long afterCount = siteService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
    public void testDeleteById() {

        long beforeCount = siteService.countAll();
        
        Site site = generateRandomModel();
        
        siteService.save(site);
        
        siteService.delete(site.getId());
        
        long afterCount = siteService.countAll();
        
        assertEquals(beforeCount, afterCount);
        
    }
	
	@Test
	public void testFindAll() {
		
		Site site = generateRandomModel();
		
		siteService.save(site);
		site = siteService.findById(site.getId());
        
        List<Site> modelList = siteService.findAll();
        List<String> idList = new ArrayList<String>();
        for(int i=0; i<modelList.size(); i++) {
        	String id = modelList.get(i).getId();
        	idList.add(id);
        }
        
        assertThat(idList, hasItem(site.getId()));
        
	}
	
	@Test
	public void testfindAllPaging() {
		Site site = generateRandomModel();
		
		long beforeCount = siteService.countAll();
		
		siteService.save(site);
		
		long afterCount = siteService.countAll();
		
		List<Site> modelList = siteService.findAll(0, 5);
		
		boolean flag = (modelList.size() >= 1) && (afterCount > beforeCount);
		
        assertEquals(true, flag);
	}
	
	AtomicInteger counter = new AtomicInteger();
	
	public Site generateRandomModel() {
		
		long key = System.nanoTime() + counter.addAndGet(1);
		
		Site site = new Site();
		site.setId(key+"");
		site.setActive_time(new Date());
		site.setDeactive_time(new Date());
		site.setCreate_time(new Date());
		site.setModify_time(new Date());
		
		return site;
		
	}

}
