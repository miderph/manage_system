Ext.define('app.view.module.contapp.ContAppDownloadUrlListWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.ca-downloadurl-window',
	
	reference: 'ca_downloadurl_window',
	
    closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	width: 720,
	height: 520,
	scrollable: true,
	title: '下载地址列表',
	glyph: 0xf007,
	layout: 'fit',
	ca_id:-1,
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		
		this.controller = this.onController;
		
		var store = Ext.create('app.store.ContAppDownloadUrlStore');

		var pgSize = store.getPageSize(); // 分页数
		var app_apk_fileUploadField = new Ext.form.FileUploadField({
			xtype : 'fileuploadfield',
			height : 20,
			width : 550,
			blankText : 'APK地址不能为空，请选择一个APK',
			emptyText : '请选择一个APK',
			regex : /^.*(.apk|.APK)$/,
			regexText : '请选择APK文件（文件扩展名必须为.apk）',
			fieldLabel : '*上传APK',
			name : 'imgFile',
			buttonText : '',
			baseCls : 'x-plain',
			buttonConfig : {
				iconCls : 'upload-icon'
			}
		});

		var _interval;

		var app_apk_fileUpload_form = new Ext.FormPanel({
			fileUpload : true,
			baseCls : 'x-plain',
			defaults : {
				baseCls : 'x-plain'
			},
			buttonAlign : 'center',
			width : 580,
			layout : 'column',
			border : false,
			items : [{
				layout : 'column',
				items : [{
					columnWidth : .7,
					// layout:'form',
					baseCls : 'x-plain',
					items : [app_apk_fileUploadField]
				}, {
					columnWidth : .3,
					// layout:'form',
					baseCls : 'x-plain',
					items : []
				}]
			}],
			buttons : [{
				text : '上传',
				disabled : false,
				// id:'app_apk_upload_btn',
				name : 'app_apk_upload_btn',
				handler : function() {
					if (app_apk_fileUpload_form.getForm().isValid()) {
						var c_id = cont_id_form.getValue();
						// msgWin.show();
						Ext.MessageBox.show({
							title : '提示',
							msg : "上传进度",
							progressText : '0%',
							width : 300,
							progress : true,
							closable : true
						});
						app_apk_fileUpload_form.getForm().submit({
							url : 'files/file_upload.do?obj_id='
								+ c_id
								+ '&type=apk&date='
								+ Ext.util.Format.date(new Date(),
								"YmdHis"),
								// waitMsg:'正在上传...',
								timeout : 1800000,
								success : function(form, action) {
									clearInterval(_interval);
									downloadurl_window.hide();
									var result = Ext.decode(action.response.responseText);
									if (!result.success) {
										Ext.MessageBox.show({
											title : "错误提示",
											msg : result.info,
											width : 110,
											buttons : Ext.Msg.OK,
											icon : Ext.Msg.ERROR
										});
									} else {
										app_download_url_grid_store.reload();
										//console.log("--:this.appGridStore="+this.up("ca-downloadurl-window"));//this.appGridStore.reload()

										Ext.MessageBox.show({
											title : "提示",
											msg : "APK上传成功",
											width : 110,
											buttons : Ext.Msg.OK
										});
									}
								},
								failure : function(form, action) {
									clearInterval(_interval);
									var msg = "未知错误！";
									if (action != null && action.result != null && action.result.info != null){
										msg = action.result.info;
									}
									Ext.MessageBox.show({
										title : "提示",
										msg : "APK上传失败!" + msg,
										width : 110,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});

								}
						});
						clearInterval(_interval);
						_interval = setInterval(showProgress, 2000);
					}
				}
			}]
		});
		var zeroRepeat = 0;
		function showProgress() {
			var c_id = cont_id_form.getValue();
			Ext.Ajax.request({
				url : 'files/file_upload_progress.do?obj_id=' + c_id,
				method : 'get',
				timeout : 60000,
				success : function(data, options) {
					var i = (data.responseText.substring(0,
							data.responseText.length - 1))
							/ 100;
					Ext.MessageBox.updateProgress(i, data.responseText);
					if (data.responseText == '100%') {
						clearInterval(_interval);
						Ext.MessageBox.updateProgress(i, "信息提取中...");
						return;
					} else if (data.responseText == '0%') {
						zeroRepeat++;
						if (zeroRepeat > 500) {
							clearInterval(_interval);
							zeroRepeat = 0;
							Ext.MessageBox.show({
								title : "提示",
								msg : "APK上传失败!!",
								width : 110,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
						}
					}
				},
				failure : function(data, options) {
					clearInterval(_interval);
					Ext.MessageBox.show({
						title : "提示",
						msg : "APK上传失败!",
						width : 110,
						buttons : Ext.Msg.OK
					});
				}
			});
		}
		var cont_id_form = new Ext.form.TextField({// 保留
			xtype : 'textfield',
			fieldLabel : '内容ID',
			allowBlank : true,
			blankText : '',
			readOnly : true,
			hidden : true,
			editable : false,
			// id:'contAppId_temp',
			name : 'c_id',
			width : 260
		});
		var contApkPanel_form = new Ext.Panel({
			title : '方式1：APK本地文件',
			frame : true,
			collapsible : true,
			animate : true,
			border : true,
			titleCollapse : 'true',
			fieldDefaults : {
				labelAlign : 'right'
			},
			defaults : {
				baseCls : 'x-plain'
			},
			width : 586,
			autoHeight : true,
			items : [{
				layout : 'column',
				items : [{
					bodyStyle : 'padding:12 0 0 0',
					// layout:'form',
					baseCls : 'x-plain',
					style : 'text-align:center',
					items : [{
						xtype : 'label',
						text : '注意：新增软件信息时，需要先保存后再上传APK！',
						style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
					}, app_apk_fileUpload_form]
				}]
			}]
		});
		var apk_http_sand_tower = new Ext.form.Checkbox({
			baseCls : 'x-plain',
			bodyStyle : 'padding:6 0 0 100',
			xtype : 'textfield',
			// id: 'update_url_mod',
			name : 'apk_http_sand_tower',
			// width: 150,
			fieldLabel : '沙塔cdn（url地址不含防盗链）',
			maxLength : 1000
		});
		var apk_http_url_mod = new Ext.form.TextField({
			baseCls : 'x-plain',
			bodyStyle : 'padding:6 0 0 100',
			xtype : 'textfield',
			// id: 'update_url_mod',
			name : 'update_url_360',
			//allowBlank : false,
			width : 550,
			fieldLabel : '*http地址',
			blankText : 'htt地址不能为空',
			emptyText : '请输入一个http地址',
			maxLength : 1000,
			maxLengthText : '不能超过1000个字符'
		});
		var contApkHttpPanel_form = new Ext.FormPanel({
			title : '方式2：http地址',
			scrollable : true,
			frame : true,
			collapsible : true,
			animate : true,
			border : true,
			titleCollapse : true,
			fieldDefaults : {
				labelAlign : 'right'
			},
			defaults : {
				baseCls : 'x-plain'
			},
			buttonAlign : 'center',
			// id:'contApkHttpPanel_form',
			name : 'contApkHttpPanel_form',
			width : 586,
			autoHeight : true,
			items : [{
				xtype : 'label',
				text : '注意：新增软件信息时，需要先保存后再确认从http上传到服务器！',
				style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
			}, , apk_http_url_mod, apk_http_sand_tower],
			buttons : [{
				xtype : 'button',
				text : '从http上传',
				handler : function() {
					var c_id = cont_id_form.getValue();
					var sand_tower_cdn = apk_http_sand_tower.getValue() == true ? 1 : 0;
					var appDownloadUrl = apk_http_url_mod.getValue();
					Ext.MessageBox.wait('处理中，请稍后...');
					Ext.Ajax.request({
						url : 'apps/apk_http_download.do',
						params : {
							c_id : c_id,
							sand_tower : sand_tower_cdn,
							appDownloadUrl : appDownloadUrl
						},
						success : function(response) {
							Ext.MessageBox.hide();
							downloadurl_window.hide();
							var result = Ext
							.decode(response.responseText);
							if (!result.success) {
								Ext.MessageBox.show({
									title : "错误提示",
									msg : result.info,
									width : 200,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							} else {
								app_download_url_grid_store.load();
								Ext.MessageBox.show({
									title : "提示",
									msg : "从http上传到服务器成功",
									width : 200,
									buttons : Ext.Msg.OK
								});
							}
						},
						failure : function(response) {
							Ext.MessageBox.hide();
							if (response == null) {
								Ext.MessageBox.show({
									title : "错误提示",
									msg : "网络错误",// 从http上传到服务器失败
									width : 200,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							} else {
								var result = Ext
								.decode(response.responseText);
								if (!result.success) {
									Ext.MessageBox.show({
										title : "错误提示",
										msg : result.info,
										width : 200,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
								}
							}
						}
					});
				}
			}]

		});
		// ****360云盘升级地址*begin******************************************************************************
		var url_360_id_mod = new Ext.form.TextField({
			xtype : 'textfield',
			name : 'url_360_id',
			hidden : true,
			hideLabel : true
		});
		var update_url_mod_360 = new Ext.form.TextField({
			baseCls : 'x-plain',
			bodyStyle : 'padding:6 0 0 100',
			xtype : 'textfield',
			// id: 'update_url_mod',
			name : 'update_url_360',
			allowBlank : false,
			width : 550,
			fieldLabel : '*360分享地址',
			maxLength : 1000,
			maxLengthText : '不能超过1000个字符'
		});
		var update_url_share_password_360 = new Ext.form.TextField({
			baseCls : 'x-plain',
			bodyStyle : 'padding:6 0 0 100',
			xtype : 'textfield',
			// id: 'share_password',
			name : 'share_password_360',
			allowBlank : true,
			width : 550,
			fieldLabel : '360分享密码',
			maxLength : 100,
			maxLengthText : '不能超过100个字符'
		});

		var upgrate_temp_url_mod = new Ext.form.TextArea({
			baseCls : 'x-plain',
			bodyStyle : 'padding:6 0 0 100',
			xtype : 'textarea',
			// id: 'upgrate_temp_url_mod',
			name : 'upgrate_temp_url_mod',
			// allowBlank: false,
			readOnly : true,
			// disabled:true,
			height : 50,
			width : 550,
			fieldLabel : '360临时地址',
			maxLength : 500,
			maxLengthText : '不能超过500个字符'
		});

		var temp_url_expire_time_mod = new Ext.form.DateField({
			// layout:'form',
			baseCls : 'x-plain',
			// id: 'temp_url_expire_time_mod',
			name : 'temp_url_expire_time_mod',
			fieldLabel : '360临时地址过期时间',
			width : 550,
			// height:250,
			// allowBlank: false,
			disabled : true,
			// readOnly: true,
			grow : true,
			// renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
			altFormats : 'Y-m-d H:i:s',
			format : 'Y-m-d H:i:s',
			blankText : '请选择开始时间',
			emptyText : '请选择开始时间'
		});
		var modify_updateurl_360_form = new Ext.FormPanel({
			title : '方式3：360云盘分享地址',
			scrollable : true,
			frame : true,
			collapsible : true,
			animate : true,
			border : true,
			titleCollapse : true,
			fieldDefaults : {
				labelAlign : 'right'
			},
			defaults : {
				baseCls : 'x-plain'
			},
			buttonAlign : 'center',
			// id:'modify_updateurl_360_form',
			name : 'modify_updateurl_360_form',
			width : 586,
			autoHeight : true,
			items : [{
				layout : 'column',
				items : [{
					bodyStyle : 'padding:12 0 0 0',
					// layout:'form',
					baseCls : 'x-plain',
					style : 'text-align:center',
					items : [{
						xtype : 'label',
						text : '注意：新增软件信息时，需要先保存后再确认从云盘上传到服务器！',
						style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
					}, , url_360_id_mod, update_url_mod_360,
					update_url_share_password_360,
					temp_url_expire_time_mod, upgrate_temp_url_mod]
				}]
			}],
			buttons : [{
				xtype : 'button',
				text : '从云盘上传',
				handler : function() {
					var c_id = cont_id_form.getValue();
					var id = url_360_id_mod.getValue();
					var appDownloadUrl = update_url_mod_360.getValue();
					var pwd = update_url_share_password_360.getValue();
					Ext.MessageBox.wait('处理中，请稍后...');
					Ext.Ajax.request({
						url : 'apps/cloud_download.do',
						params : {
							id : id,
							c_id : c_id,
							appDownloadUrl : appDownloadUrl,
							sharePasswd : pwd
						},
						success : function(response) {
							Ext.MessageBox.hide();
							downloadurl_window.hide();
							var result = Ext
							.decode(response.responseText);
							if (!result.success) {
								Ext.MessageBox.show({
									title : "错误提示",
									msg : result.info,
									width : 200,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							} else {
								cont_app_download_url_form
								.setValue(result.apk_url);
								cont_app_version_form
								.setValue(result.version);
								cont_app_version_code_form
								.setValue(result.version_code);
								cont_app_capacity_form
								.setValue(result.file_size);
								cont_app_package_name_form
								.setValue(result.package_name);
								cont_app_md5_form
								.setValue(result.md5sum);
								temp_url_expire_time_mod
								.setValue(result.temp_url_expire_time);
								upgrate_temp_url_mod
								.setValue(result.apk_url);

								app_download_url_grid_store.load();
								Ext.MessageBox.show({
									title : "提示",
									msg : "从云盘上传到服务器成功",
									width : 200,
									buttons : Ext.Msg.OK
								});
							}
						},
						failure : function(response) {
							Ext.MessageBox.hide();
							if (response == null) {
								Ext.MessageBox.show({
									title : "错误提示",
									msg : "网络错误",// 从云盘上传到服务器失败
									width : 200,
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							} else {
								var result = Ext
								.decode(response.responseText);
								if (!result.success) {
									Ext.MessageBox.show({
										title : "错误提示",
										msg : result.info,
										width : 200,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
								}
							}
						}
					});
				}
			}]
		});

		var app_download_url_grid_store = new Ext.data.JsonStore({
			pageSize : pgSize,
			fields : ['id', 'c_id', 'app_name', 'package_name',
					'provider_id', 'version', 'version_code', 'site',
					'capacity', 'md5sum', 'add_time', 'create_time',
					'modify_time', 'download_url', 'url_type',
					'url_type_desc', 'upgrade_temp_url',
					'temp_url_exepire_time', 'share_password'],
			proxy : {
				type : 'ajax',
				url : 'apps/query_app_download_url.do',
				reader : {
					totalProperty : "results",
					root : "datastr",
					idProperty : 'id'
				},
				extraParams : {
					c_id : 0
				}
			}
		});

		var downloadurl_window = new Ext.Window({
			title : '上传apk(任选一种上传方式)',
			resizable : false,
			width : 600,
			height : 550,
			autoHeight : true,
			autoScroll : true,
			modal : true,
			closable : true,
			closeAction : 'hide',
			items : [cont_id_form,contApkPanel_form, contApkHttpPanel_form,
					modify_updateurl_360_form],
			buttons : [{
						text : '关闭',
						handler : function() {
							downloadurl_window.hide();
						}
					}]
		});
		var contAppPanel_downloadurl_new = function() {
			var c_id = this.ca_id;
			cont_id_form.setValue(c_id);
			downloadurl_window.show();
		}
		var contAppPanel_downloadurl_download = function() {
			var rows = app_download_url_grid.getSelectionModel().getSelection();// 返回值为Record数组
			if (rows.length == 1) {
				var show_url = rows[0].get('upgrade_temp_url');

				if (show_url != null && show_url != '') {
					pic = window.open(show_url, "a1");
					pic.document.execCommand("SaveAs");
				} else {
					Ext.MessageBox.show({
								title : "提示",
								msg : "APK不存在，无法下载",
								width : 180,
								buttons : Ext.Msg.OK,
								icon : Ext.Msg.ERROR
							});
				}
			} else {
				Ext.MessageBox.show({
							title : "提示",
							msg : "请先选择要下载的一项",
							width : 160,
							buttons : Ext.Msg.OK
						});
			}
		}
		var contAppPanel_downloadurl_delete = function() {
			var rows = app_download_url_grid.getSelectionModel().getSelection();// 返回值为Record数组
			if (rows.length == 1) {
				var urlID = rows[0].get('id');
				Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
							url : 'apps/delete_download.do?id=' + urlID,
							waitMsg : '正在提交,请稍等',
							success : function(response, opts) {
								var result = Ext
								.decode(response.responseText);
								if (!result.success) {
									Ext.Msg.show({
										title : '错误提示',
										msg : result.info,
										buttons : Ext.Msg.OK,
										icon : Ext.Msg.ERROR
									});
								} else {
									app_download_url_grid_store.load();
								}
							},
							failure : function(response, opts) {
								Ext.Msg.show({
									title : '错误提示',
									msg : '删除时发生错误!',
									buttons : Ext.Msg.OK,
									icon : Ext.Msg.ERROR
								});
							}
						});
					}
				});
			} else {
				Ext.MessageBox.show({
							title : "提示",
							msg : "请先选择删除的一项",
							width : 160,
							buttons : Ext.Msg.OK
						});
			}
		}
		var default_downloadurl_form = new Ext.form.TextField({// 保留
			xtype : 'textfield',
			fieldLabel : '正式环境默认下载地址',
			allowBlank : true,
			blankText : '',
			readOnly : true,
			editable : false,
			value: 'http://tv.duolebo.com/apkDownload/?c='+this.ca_id,
			labelAlign : 'right',
			labelWidth : 150,
			width : 500
		});

		var app_download_url_grid = new Ext.grid.GridPanel({
			xtype: 'gridpanel',
			store : app_download_url_grid_store,
			selModel : {
				selType : 'checkboxmodel',
				mode : 'SIMPLE'
				//mode : 'SINGLE'
			},
			columns : [{
				header : "ID",
				width : 80,
				dataIndex : 'id'
			}, {
				header : "应用资产ID",
				width : 80,
				dataIndex : 'c_id'
			}, {
				header : '应用名称',
				width : 100,
				dataIndex : 'app_name'
			}, {
				header : "下载地址/分享地址",
				width : 160,
				dataIndex : 'download_url'
			}, {
				header : "临时地址",
				width : 160,
				dataIndex : 'upgrade_temp_url'
			}, {
				header : "应用类型",
				width : 80,
				dataIndex : 'c_type',
				hidden : true
			}, {
				header : "包名",
				width : 80,
				dataIndex : 'package_name',
				hidden : true
			}, {
				header : "ProviderId",
				width : 80,
				dataIndex : 'provider_id'
			}, {
				header : "VersionName",
				width : 80,
				dataIndex : 'version'
			}, {
				header : "VersionCode",
				width : 80,
				dataIndex : 'version_code'
			}, {
				header : "网站",
				width : 80,
				dataIndex : 'site'
			}, {
				header : "大小",
				width : 80,
				dataIndex : 'capacity'
			}, {
				header : "MD5值",
				width : 80,
				dataIndex : 'md5sum'
			}, {
				header : "上架时间",
				width : 80,
				dataIndex : 'add_time'
			}, {
				header : "创建时间",
				width : 80,
				dataIndex : 'create_time'
			}, {
				header : "更新时间",
				width : 80,
				dataIndex : 'modify_time'
			}, {
				header : "地址类型",
				width : 80,
				dataIndex : 'url_type',
				hidden : true
			}, {
				header : "地址类型",
				width : 80,
				dataIndex : 'url_type_desc'
			}, {
				header : "临时地址过期时间",
				width : 80,
				dataIndex : 'temp_url_exepire_time'
			}, {
				header : "分享密码",
				width : 80,
				dataIndex : 'share_password'
			}],
			tbar : [{
				text : '上传',
		    	iconCls: 'add',
				handler : contAppPanel_downloadurl_new,
				scope : this
			}, {
				text : '删除',
		    	iconCls: 'delete',
				handler : contAppPanel_downloadurl_delete,
				scope : this
			}, {
				text : '下载apk',
				handler : contAppPanel_downloadurl_download,
				scope : this
			}, ' ', '-', ' ', default_downloadurl_form],
            viewConfig: {
            	stripeRows: true,
            	enableTextSelection:true
            },
			bbar : new Ext.PagingToolbar({
				pageSize : pgSize,
				// width: 358,
				store : app_download_url_grid_store,
				displayInfo : true,
				displayMsg : '第 {0}-- {1}条    共 {2}条',
				emptyMsg : '没有记录'
			})
		});
		
		this.items = [app_download_url_grid];
		
		this.buttons = [{
			text: '关闭',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		
		this.callParent();
	},
	items:[],
	buttonAlign: 'center'
});