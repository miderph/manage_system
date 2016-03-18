Ext.define('app.view.module.contsales.ContSalesWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-sales-window',
	
	reference: 'cont_sales_window',
	
	uses: [
	       'app.store.ColumnShortCutStore',
	       'app.ux.form.MultiTextField',
	       'app.ux.form.MultiTypeUserGroup',
	       'app.ux.form.MultiImgGroup',
	       'app.ux.form.ColumnShortcutTextField',
	       'app.view.module.contsales.contSalesSnapshotsPanel',
	       'app.ux.form.PreviewFileField'],
	
    closable: true,
	closeAction: 'hide',
	resizable: true,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	scrollable: true,
	title: '添加资产',
//	y: 10,
	glyph: 0xf007,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.callParent();
	},
	items:[{
		xtype: 'form',
		bodyPadding: 20,
//		referenceHolder: true,
		fieldDefaults: {
	        labelWidth: 120,
			labelAlign: 'right'
	    },
		items: [{
			xtype: 'multiimggroup',
	        title: '图片信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'imginfo',
			margin: '10, 2, 0, 0'
		}, {
			xtype: 'fieldset',
			title: '缩略图信息',
			collapsible: true,
			defaultType: 'textfield',
			fieldName: 'imginfo',
			hidden: true,
			bind: {
				hidden: '{!hasImgInfo}'
			},
			margin: '10, 2, 0, 0',
			items: [{
				xtype: 'contsalessnapshotspanel',
				name: 'contSalesSnapshotsPanel',
				bind: {
					hidden: '{!hasImgInfo}'
				}
			}]
		}, {
			xtype: 'textfield',
			name: 'cs_base_info',
			value: 1,
			bind: {
				//value: '{cs_base_info}'
			},
			hidden: true
		}, {
			xtype: 'fieldset',
	        title: '基本信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'baseinfo',
	        hidden: true,
	        bind: {
	        	hidden: '{!hasBaseInfo}'
	        },
	        items: [{
				name: 'c_id',
				hidden: true,
				value: '-1',
				listeners: {
					change: function(combo, newValue, oldValue, eOpts ) {
						var form = this.up('form');
						var contsalessnapshotspanel = form.down("contsalessnapshotspanel");
						contsalessnapshotspanel.reload(newValue);
					}
				}
			}, {
				name: 'pinyin',
				hidden: true,
				value: ''
			}, {
				xtype: 'container',
				layout: 'hbox',
				items: [{
					xtype: 'checkboxfield',
					name: 'is_locked',
					fieldLabel: '基本信息锁定',
					value: 0,
					inputValue:1,
					flex: 1
				}, {
					xtype: 'textfield',
					name: 'cv_alias',
					fieldLabel: '商品别名',
					flex: 1,
					emptyText: '输入商品别名'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'c_name',
					fieldLabel: '*商品名称',
					allowBlank: false,
					flex: 1,
					emptyText: '输入商品名称'
				}, {
					xtype: 'datefield',
					name: 'active_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*生效时间',
					value: new Date(),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'c_status',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contStatusStore}'
					},
					fieldLabel: '*商品状态',
					emptyText: '选择商品状态'
				}, {
					xtype: 'datefield',
					name: 'deactive_time',
					allowBlank: false,
					flex: 1,
					fieldLabel: '*失效时间',
					value: Ext.Date.add(new Date(), Ext.Date.YEAR, 50),
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'provider_id',
					allowBlank: false,
					displayField: 'cp_name',
					valueField:'cp_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contProviderByAuthStore}'
					},
					fieldLabel: '*提供商',
					emptyText: '选择提供商'
				},{
					xtype: 'textfield',
					name: 'cs_cp_name',
					fieldLabel: '提供商-正标题',
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'cs_shop_id',
					allowBlank: true,
					displayField: 'name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contShopStore}'
					},
					fieldLabel: '商铺',
					emptyText: '选择商铺'
				},{
					xtype: 'textfield',
					name: 'cs_sub_cp_name',
					fieldLabel: '提供商-副标题',
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'cs_channel_id',
					allowBlank: true,
					displayField: 'cp_name',
					valueField:'cp_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contProviderByAuthStore}'
					},
					fieldLabel: '内容渠道(数据来源)',
					emptyText: '选择内容渠道（复用提供商信息）'
				},{
					xtype: 'textfield',
					name: 'cs_key_words',
					fieldLabel: '关键词',
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				defaultType: 'textfield',
				items: [{
					xtype: 'textfield',
					name: 'cs_fake_price',
					fieldLabel: '市场价格',
					vtype: 'intfloat',
					value: 0,
					flex: 1,
					listeners: {
						change: function(tf, newValue, oldValue, eOpts) {
							var saleComp = this.up('form').query('textfield[name=cs_sale_price]');
							var salePrice = 0;
							if(saleComp.length > 0) {
								saleComp = saleComp[0];
								salePrice = saleComp.getValue();
							}
							
							var dv = 0;
							if(newValue > 0 && salePrice > 0) {
								var dv = String(parseFloat(salePrice) * 10 / parseFloat(newValue));
								if (dv.length > 4) {
									dv = dv.substring(0, 4);
								}
							}
							
							var dComp = this.up('form').query('textfield[name=cs_disaccount]');
							if(dComp.length > 0) {
								dComp = dComp[0];
								dComp.setValue(dv);
							}
						}
					}
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				defaultType: 'textfield',
				items: [{
					xtype: 'textfield',
					name: 'cs_sale_price',
					fieldLabel: '销售价格',
					vtype: 'intfloat',
					value: 0,
					flex: 1,
					listeners: {
						change: function(tf, newValue, oldValue, eOpts) {
							var fakeComp = this.up('form').query('textfield[name=cs_fake_price]');
							var fakePrice = 0;
							if(fakeComp.length > 0) {
								fakeComp = fakeComp[0];
								fakePrice = fakeComp.getValue();
							}
							
							var dv = 0;
							if(newValue > 0 && fakePrice > 0) {
								var dv = String(parseFloat(newValue) * 10 / parseFloat(fakePrice));
								if (dv.length > 4) {
									dv = dv.substring(0, 4);
								}
							}
							
							var dComp = this.up('form').query('textfield[name=cs_disaccount]');
							if(dComp.length > 0) {
								dComp = dComp[0];
								dComp.setValue(dv);
							}
						}
					}
				},{
					xtype: 'textfield',
					name: 'cs_disaccount',
					fieldLabel: '折扣（自动计算）',
					readOnly: true,
					value: 0,
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				defaultType: 'textfield',
				items: [{
					name: 'cs_post_price',
					fieldLabel: '运费',
					vtype: 'intfloat',
					value: 0,
					flex: 1
				},{
					xtype: 'checkboxfield',
					name: 'bitmask_price',
					fieldLabel: '支持拍下改价',
					value: 0,
					inputValue:1,
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cs_sales_no',
					fieldLabel: '商品编号',
					flex: 1
				},{
					xtype: 'displayfield',
					name: 'cs_right_space',
					flex: 1,
			        value: ''
				}]
			}, {
				xtype: 'multitextfield',
				valueName: 'cs_pay_type_ids',
				textName: 'cs_pay_type_names',
				fieldLabel: '支付方式',
				emptyText: '选择支付方式',
				margin: '10, 0, 10, 0',
				onHandler: 'onPayType'
			}, {
						xtype: 'combobox',editable: false,

				name: 'superscript_id',
				displayField: 'mar_s_contName',
				valueField:'mar_s_contId',
				queryMode: 'local',
				anchor: '100%',
				bind: {
					store: '{contSuperscriptStore}'
				},
				fieldLabel: '角标内容',
				listConfig: {
					itemTpl: ['<div data-qtip="{mar_s_contIntro}">{mar_s_contId} {mar_s_contName} {mar_s_contType} {mar_s_contProvider}</div>']
				}
			}, {
				name: 'cv_play_url',
				fieldLabel: '视频播放地址',
				margin: '10, 0, 0, 0',
				emptyText: '请输入视频播放地址',
				anchor: '100%'
			}, {
				name: 'cs_cp_url',
				fieldLabel: '商品页面地址',
				margin: '10, 0, 0, 0',
				emptyText: '请输入商品页面地址',
				anchor: '100%'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 0, 0',
				anchor: '100%',
				name: 'cs_price_desc',
				fieldLabel: '价格区间(将替换销售价格显示的位置)',
				emptyText: '输入价格区间'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 0, 0',
				anchor: '100%',
				name: 'cs_hot_info',
				fieldLabel: '商品信息(2.19.3版本版本已废弃，2.19.2版本显示在播放器靠上位置)',
				emptyText: '输入商品信息'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 10, 0',
				anchor: '100%',
				name: 'cs_gift',
				fieldLabel: '促销信息(2.19.3版本将合并供应商和商铺的促销信息后一起返回，显示在播放器靠上位置，当设置了分渠道促销信息时，此字段无效)',
				emptyText: '输入促销信息'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 0, 0',
				anchor: '100%',
				name: 'c_video_seg_time',
				fieldLabel: '视频的打点信息(srt字幕格式)',
				emptyText: '输入打点信息'
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 0, 0',
				anchor: '100%',
				name: 'cs_detail_pic_list',
				fieldLabel: '详情图片列表(每行一个图片地址,行首是#号表明当前行是注释)',
				emptyText: '请粘贴详情图片列表'
			}, {
				xtype: 'fieldset',
					title: '服务信息（将拼在一起返回给客户端，无数据时不显示）',
					collapsible: true,
					defaultType: 'textfield',
					fieldName: 'serviceinfo',
					margin: '10, 2, 0, 0',
					items: [{
						name: 'cs_sum_stock',
						fieldLabel: '库存数量',
						vtype: 'intfloat',
						value: '',
						flex: 1
					},{
						name: 'cs_sum_sale',
						fieldLabel: '销量',
						vtype: 'intfloat',
						value: '',
						flex: 1
					},{
						xtype: 'textareafield',
						margin: '10, 0, 10, 0',
						anchor: '100%',
						name: 'cs_post_desc',
						fieldLabel: '运费(描述)',
						emptyText: '输入运费(描述)信息'
					},{
						xtype: 'textareafield',
						margin: '10, 0, 0, 0',
						anchor: '100%',
						name: 'cs_service_desc',
						fieldLabel: '服务(描述)',
						emptyText: '输入服务(描述)信息'
					}]
			},{
				xtype: 'multitypeusergroup',
		        title: '数据过滤（需选择测试组）',
		        collapsible: true,
		        defaultType: 'textfield',
		        fieldName: 'usergroupinfo',
				margin: '10, 2, 0, 0'
			}]
		}, {
			xtype: 'textfield',
			name: 'cs_img_info',
			value: 1,
			bind: {
				//value: '{cs_img_info}'
			},
			hidden: true
		}]
	}],
	buttonAlign: 'center',
	buttons: [{
		text: '提交',
		handler: 'onSubmit'
	}, {
		text: '取消',
		handler: 'onCancel'
	}],
	listeners: {
		resize: function(win, width, height) {
			var bodyH = Ext.getBody().getHeight();
			var y = (bodyH-height)/2;
			win.setY(y);
			win.setMaxHeight(bodyH-20);
		},
    	hide: function(self, eOpts) {
    		console.log("onhide(),begin:self=" + self+",this.myviewModel"+this.getViewModel());
    		this.getViewModel().set('c_img_url', "app/resources/images/loading.JPG");
    		this.getViewModel().set('c_img_little_url', "app/resources/images/loading.JPG");
    		this.getViewModel().set('c_img_icon_url', "app/resources/images/loading.JPG");
    		this.getViewModel().set('c_img_4_squares_url', "app/resources/images/loading.JPG");
    		this.getViewModel().set('c_img_6_squares_url', "app/resources/images/loading.JPG");
    		console.log("onhide(),end:self=" + self);
    	},
		show: function(win) {
//			var viewModel = this.getViewModel();
//			console.log('cw cs_base_info: ' + viewModel.get('cs_base_info'));
//	    	console.log('cw cs_img_info: ' + viewModel.get('cs_img_info'));
//			var hasBaseInfo = false;
//			var hasImgInfo = false;
//			if(viewModel.get('cs_base_info') == 1) {
//				hasBaseInfo = true;
//			}
//			if(viewModel.get('cs_img_info') == 1) {
//				hasImgInfo = true;
//			}
//			if (!hasBaseInfo && !hasImgInfo){
//				viewModel.set('cs_base_info', 1);
//				viewModel.set('cs_img_info', 1);
//				hasBaseInfo = true;
//				hasImgInfo = true;
//			}
//			console.log('cw hasBaseInfo: ' + hasBaseInfo);
//	    	console.log('cw hasImgInfo: ' + hasImgInfo);
//
//			this.query('textfield[name=c_name]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=c_status]')[0].setDisabled(!hasBaseInfo);
//			this.query('combobox[name=provider_id]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=active_time]')[0].setDisabled(!hasBaseInfo);
//			this.query('datefield[name=deactive_time]')[0].setDisabled(!hasBaseInfo);
//			
//			this.query('combobox[name=c_img_plat_group]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_active_time]')[0].setDisabled(!hasImgInfo);
//			this.query('datefield[name=c_img_deactive_time]')[0].setDisabled(!hasImgInfo);
		}
	}
});