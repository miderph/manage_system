package com.onewave.backstage.model;

import java.io.Serializable;

import com.onewave.common.annotation.IId;
import com.onewave.common.annotation.ITable;

@SuppressWarnings("serial")
@ITable(name = "zl_cont_sales")
public class ContentSalesBean implements Serializable {

	@IId
	private String c_id;
	private String name;
	private String provider_id;

	private String hot_info;
	//市场价格（原价）
	private String fake_price;
	//销售价格（卖出价）
	private String sale_price;
	private String price_desc;
	//实际价格（成本价）
	private String real_price;
	private String post_price;
	private String disaccount;
	private String key_words;
	private String cp_name;
	private String sub_cp_name;
	private String sales_no;
	private String pay_type_ids;
   private String sum_stock;
   private String gift;
   private String sum_sale;
   private String post_desc;
   private String service_desc;
   private String bitmask_price;
   private String url;
   private String shop_id;
   private String channel_id;

   private String detail_pic_file;

   
   public String getShop_id() {
	   return shop_id;
   }

   public void setShop_id(String shop_id) {
	   this.shop_id = shop_id;
   }

   public String getChannel_id() {
	   return channel_id;
   }

   public void setChannel_id(String channel_id) {
	   this.channel_id = channel_id;
   }
   
   public String getUrl() {
	   return url;
   }

   public void setUrl(String url) {
	   this.url = url;
   }

   
   public String getDetail_pic_file() {
      return detail_pic_file;
   }

   public void setDetail_pic_file(String detail_pic_file) {
      this.detail_pic_file = detail_pic_file;
   }

   @SuppressWarnings("serial")
	@ITable(name = "zl_sales_pay_type")
	public static class PayType implements Serializable {
		@IId
		private String id;
		private String name;
		private String pay_type;
		private String has_qrcode;
		private String service_hotline;
		private String description;
		private String icon_url;

		public String getIcon_url() {
			return icon_url;
		}

		public void setIcon_url(String icon_url) {
			this.icon_url = icon_url;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}

		public String getHas_qrcode() {
			return has_qrcode;
		}

		public void setHas_qrcode(String has_qrcode) {
			this.has_qrcode = has_qrcode;
		}

		public String getService_hotline() {
			return service_hotline;
		}

		public void setService_hotline(String service_hotline) {
			this.service_hotline = service_hotline;
		}
	}

	public String getSub_cp_name() {
		return sub_cp_name;
	}

	public void setSub_cp_name(String sub_cp_name) {
		this.sub_cp_name = sub_cp_name;
	}

	public String getSales_no() {
		return sales_no;
	}

	public void setSales_no(String sales_no) {
		this.sales_no = sales_no;
	}

	public String getC_id() {
		return c_id;
	}

	public void setC_id(String c_id) {
		this.c_id = c_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public String getHot_info() {
		return hot_info;
	}

	public void setHot_info(String hot_info) {
		this.hot_info = hot_info;
	}

	public String getFake_price() {
		return fake_price;
	}

	public void setFake_price(String fake_price) {
		this.fake_price = fake_price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}
	public String getPrice_desc() {
		return price_desc;
	}

	public void setPrice_desc(String price_desc) {
		this.price_desc = price_desc;
	}
	public String getReal_price() {
		return real_price;
	}

	public void setReal_price(String real_price) {
		this.real_price = real_price;
	}

	public String getPost_price() {
		return post_price;
	}

	public void setPost_price(String post_price) {
		this.post_price = post_price;
	}

	public String getDisaccount() {
		return disaccount;
	}

	public void setDisaccount(String disaccount) {
		this.disaccount = disaccount;
	}

	public String getKey_words() {
		return key_words;
	}

	public void setKey_words(String key_words) {
		this.key_words = key_words;
	}

	public String getCp_name() {
		return cp_name;
	}

	public void setCp_name(String cp_name) {
		this.cp_name = cp_name;
	}

	public String getPay_type_ids() {
		return pay_type_ids;
	}

	public void setPay_type_ids(String pay_type_ids) {
		this.pay_type_ids = pay_type_ids;
	}

	public String getSum_stock() {
		return sum_stock;
	}

	public void setSum_stock(String sum_stock) {
		this.sum_stock = sum_stock;
	}
   public String getGift() {
      return gift;
   }

   public void setGift(String gift) {
      this.gift = gift;
   }

   public String getSum_sale() {
      return sum_sale;
   }

   public void setSum_sale(String sum_sale) {
      this.sum_sale = sum_sale;
   }

   public String getPost_desc() {
      return post_desc;
   }

   public void setPost_desc(String post_desc) {
      this.post_desc = post_desc;
   }

   public String getService_desc() {
      return service_desc;
   }

   public void setService_desc(String service_desc) {
      this.service_desc = service_desc;
   }

   public String getBitmask_price() {
      return bitmask_price;
   }

   public void setBitmask_price(String bitmask_price) {
      this.bitmask_price = bitmask_price;
   }
}
