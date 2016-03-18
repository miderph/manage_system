package com.onewave.backstage.model;

import java.io.Serializable;

import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@SuppressWarnings("serial")
@ITable(name = "zl_cont_video_file")
public class ContentVideoFileBean implements Serializable {

	@IId
	private String id;
   private String c_id;
   private String order_num;
   private String bit_rate;
   private String provider_id;
   private String rate_tag;
   private String rate_tag_eng;
   private String play_url;
   private String title;

   public String getTitle() {
      return title;
   }
   public void setTitle(String title) {
      this.title = title;
   }
   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getC_id() {
      return c_id;
   }
   public void setC_id(String c_id) {
      this.c_id = c_id;
   }
   public String getOrder_num() {
      return order_num;
   }
   public void setOrder_num(String order_num) {
      this.order_num = order_num;
   }
   public String getBit_rate() {
      return bit_rate;
   }
   public void setBit_rate(String bit_rate) {
      this.bit_rate = bit_rate;
   }
   public String getProvider_id() {
      return provider_id;
   }
   public void setProvider_id(String provider_id) {
      this.provider_id = provider_id;
   }
   public String getRate_tag() {
      return rate_tag;
   }
   public void setRate_tag(String rate_tag) {
      this.rate_tag = rate_tag;
   }
   public String getRate_tag_eng() {
      return rate_tag_eng;
   }
   public void setRate_tag_eng(String rate_tag_eng) {
      this.rate_tag_eng = rate_tag_eng;
   }
   public String getPlay_url() {
      return play_url;
   }
   public void setPlay_url(String play_url) {
      this.play_url = play_url;
   }
   
}
