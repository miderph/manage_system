package com.onewave.backstage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.onewave.common.annotation.IField;
import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

/**
 * StatusDict of the concrete class
 * 
 * @author liuhaidi
 * 
 */
@SuppressWarnings("serial")
@ITable(name = "zl_cont_provider")
public class ContProvider implements Serializable {

	@IId
	private String id;
	private String name;
	private String description;
	private String site_id;
	private String isdefault;
	
	private String is_apk_prior;
	private String cont_provider_id;
	private String cont_provider_ids;
	private String chn_provider_id;
	private String epg_provider_ids;
	
	private String epg_priority;
	private String xmpp_index;
	private String need_check_uap;
	private String can_switchtv;
	private String can_playvideo;
	
	private String can_download;
	private String can_recording;
	private String can_timeshift;
	private String can_playback;
	private String p_status;
	
	private String can_switchtv_pids;
	private String can_playvideo_pids;
	private String can_download_pids;
	private String can_recording_pids;
	private String can_timeshift_pids;
	
	
	private String can_playback_pids;
	private String stb_prefix;
	@IField(save = false, update=false) 
	private String stb_prefix_ids;
	@IField(save = false, update=false) 
	private String stb_prefix_names;
	@IField(update=false)
	private Date create_time;
	private Date modify_time;
	
	private String icon_url;
	private String hot_info;
	private String pay_type_ids;


   public String getIcon_url() {
      return icon_url;
   }

   public void setIcon_url(String icon_url) {
      this.icon_url = icon_url;
   }

   public String getHot_info() {
      return hot_info;
   }

   public void setHot_info(String hot_info) {
      this.hot_info = hot_info;
   }

	public String getPay_type_ids() {
		return pay_type_ids;
	}

	public void setPay_type_ids(String pay_type_ids) {
		this.pay_type_ids = pay_type_ids;
	}
	//中间字段，不持久化
	public String getStb_prefix_ids() {
		return stb_prefix_ids;
	}

	public void setStb_prefix_ids(String stbPrefixIds) {
		stb_prefix_ids = stbPrefixIds;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	
	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modifyTime) {
		modify_time = modifyTime;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public String getIs_apk_prior() {
		return is_apk_prior;
	}

	public void setIs_apk_prior(String is_apk_prior) {
		this.is_apk_prior = is_apk_prior;
	}
	public String getCont_provider_id() {
		return cont_provider_id;
	}

	public void setCont_provider_id(String contProviderId) {
		cont_provider_id = contProviderId;
	}

	public String getCont_provider_ids() {
		return cont_provider_ids;
	}

	public void setCont_provider_ids(String contProviderIds) {
		cont_provider_ids = contProviderIds;
	}

	public String getChn_provider_id() {
		return chn_provider_id;
	}

	public void setChn_provider_id(String chnProviderId) {
		chn_provider_id = chnProviderId;
	}

	public String getEpg_provider_ids() {
		return epg_provider_ids;
	}

	public void setEpg_provider_ids(String epgProviderIds) {
		epg_provider_ids = epgProviderIds;
	}

	public String getEpg_priority() {
		return epg_priority;
	}

	public void setEpg_priority(String epgPriority) {
		epg_priority = epgPriority;
	}

	public String getXmpp_index() {
		return xmpp_index;
	}

	public void setXmpp_index(String xmppIndex) {
		xmpp_index = xmppIndex;
	}

	public String getCan_switchtv() {
		return can_switchtv;
	}

	public void setCan_switchtv(String canSwitchtv) {
		can_switchtv = canSwitchtv;
	}

	public String getCan_playvideo() {
		return can_playvideo;
	}

	public void setCan_playvideo(String canPlayvideo) {
		can_playvideo = canPlayvideo;
	}

	public String getCan_download() {
		return can_download;
	}

	public void setCan_download(String canDownload) {
		can_download = canDownload;
	}

	public String getCan_recording() {
		return can_recording;
	}

	public void setCan_recording(String canRecording) {
		can_recording = canRecording;
	}

	public String getCan_timeshift() {
		return can_timeshift;
	}

	public void setCan_timeshift(String canTimeshift) {
		can_timeshift = canTimeshift;
	}

	public String getP_status() {
		return p_status;
	}

	public void setP_status(String pStatus) {
		p_status = pStatus;
	}


	public String getCan_playback() {
		return can_playback;
	}

	public void setCan_playback(String canPlayback) {
		can_playback = canPlayback;
	}

	public String getCan_switchtv_pids() {
		return can_switchtv_pids;
	}

	public void setCan_switchtv_pids(String canSwitchtvPids) {
		can_switchtv_pids = canSwitchtvPids;
	}

	public String getCan_playvideo_pids() {
		return can_playvideo_pids;
	}

	public void setCan_playvideo_pids(String canPlayvideoPids) {
		can_playvideo_pids = canPlayvideoPids;
	}

	public String getCan_download_pids() {
		return can_download_pids;
	}

	public void setCan_download_pids(String canDownloadPids) {
		can_download_pids = canDownloadPids;
	}

	public String getCan_recording_pids() {
		return can_recording_pids;
	}

	public void setCan_recording_pids(String canRecordingPids) {
		can_recording_pids = canRecordingPids;
	}

	public String getCan_timeshift_pids() {
		return can_timeshift_pids;
	}

	public void setCan_timeshift_pids(String canTimeshiftPids) {
		can_timeshift_pids = canTimeshiftPids;
	}

	public String getCan_playback_pids() {
		return can_playback_pids;
	}

	public void setCan_playback_pids(String canPlaybackPids) {
		can_playback_pids = canPlaybackPids;
	}

	public String getStb_prefix() {
		return stb_prefix;
	}

	public void setStb_prefix(String stbPrefix) {
		stb_prefix = stbPrefix;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getNeed_check_uap() {
		return need_check_uap;
	}

	public void setNeed_check_uap(String needCheckUap) {
		need_check_uap = needCheckUap;
	}
	
	private List<String> getStbPrefixArray() {
		String strs[] = {};
		if(!StringUtils.isEmpty(stb_prefix)){
			stb_prefix = stb_prefix.replaceAll("/\\*.*?\\*/", "").toUpperCase();
			strs = stb_prefix.split(",");
		}
		
		List<String> arrays = new ArrayList<String>();
		for(String s : strs){
			arrays.add(s);
		}
		return arrays;
	}
	
	public void updateStbPrefix(String prefix){
		if(!StringUtils.isEmpty(prefix)){
			prefix = prefix.toUpperCase();
			List<String> arrays = getStbPrefixArray();
	   	 	if(!arrays.contains(prefix)){
	   	 		arrays.add(prefix);
	   	 	}
	   	 	stb_prefix=StringUtils.join(arrays,",");
		}
		
	}
	
	public void removeStbPrefix(String prefix){
		if(!StringUtils.isEmpty(prefix)){
			prefix = prefix.toUpperCase();
			List<String> arrays = getStbPrefixArray();
			if(arrays.contains(prefix)){
				arrays.remove(prefix);
			}
			stb_prefix = StringUtils.join(arrays, ",");
		}
	}

	public String getStb_prefix_names() {
		return stb_prefix_names;
	}

	public void setStb_prefix_names(String stb_prefix_names) {
		this.stb_prefix_names = stb_prefix_names;
	}
}
