//Ext.define('app.view.oldview.contAppstorePanel', {
//	extend : 'Ext.panel.Panel',
//	alias : 'widget.contAppstore',
//	initComponent : function() {
//		var contAppPanel_form;
//		var contAppBasePanel_form;
//		var app_snapshotimg_form;
//		var contApkSnapshotImgPanel_form;
//		var contAppImgPanel_form;
//		// function contAppFormPanelIsenable(isEditable)
//
//		var shortcut_contid_form = new Ext.form.TextField({// 保留
//			xtype : 'textfield',
//			fieldLabel : '角标内容',
//			allowBlank : true,
//			blankText : '',
//			emptyText : '点击《选择》选择角标内容',
//			readOnly : true,
//			disabled : true,
//			editable : false,
//			// id:'superscript_name',
//			name : 'superscript_name',
//			width : 580
//		});
//
//		var base_locked_checkbox = new Ext.form.Checkbox({
//					// id:'base_locked',
//					name : 'base_locked',
//					width : 260,// 宽度
//					// height:compHeight,
//					align : 'left',
//					fieldLabel : '基本信息锁定',
//					checked : false,
//					disabled : true
//				});
//		var button_select_shortcut_contid = new Ext.Button({
//					xtype : 'button',
//					disabled : true,
//					text : '选择角标',
//					handler : function() {
//						WinUtil_selectSuperScript.showWindows('角标内容选择',
//								"superscript_name", "superscript_id",
//								contAppBasePanel_form);
//						// Utils.showCheckboxGroup(contprovideramenuroviderJsonStore,'providerId','providerName','选择提供商',"can_switchtv_pids","can_switchtv_names","contproviderForm");
//					}
//				});
//		var shortcut_contid_id_hf = new Ext.form.TextField({
//					name : 'superscript_id',
//					// id:'superscript_id',
//					hidden : true,
//					hideLabel : true
//				});
//
//		Ext.QuickTips.init();
//
//		/*-----获取浏览器宽和高*/
//		var widthSize = document.body.clientWidth - 207;
//		var heightSize = document.body.clientHeight;
//
//		var siteStructureHeightSize = heightSize - 53;
//		var siteStructureHeightSize_F11 = heightSize + heightSize * 0.1;
//		var F11_booble = false;
//
//		/*-----搜索条件变量-----*/
//		var pgSize = 20; // 分页数
//		var contProviderId = '';
//		var contType = '7';
//		var contStatus = '';
//		var contName = '';
//		var contId_tmp = 0;
//		var app_img_active_time = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					// id: 'app_img_active_time_1',
//					name : 'app_img_active_time_1',
//					fieldLabel : '*图片生效时间',
//					// width:160,
//					// height:250,
//					allowBlank : false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择图片生效时间...',
//					emptyText : '请选择图片生效时间...',
//					value : '2010-01-01 00:00:00'
//				});
//
//		var app_img_deactive_time = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					name : 'app_img_deactive_time_1',
//					// id: 'app_img_deactive_time_1',
//					fieldLabel : '*图片失效时间',
//					// width:160,
//					// height:250,
//					allowBlank : false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择图片失效时间...',
//					emptyText : '请选择图片失效时间...',
//					value : '2050-01-01 00:00:00'
//				});
//
//		var app_img_platgroup_JsonStore = new Ext.data.JsonStore({
//					fields : [{
//								name : 'plat_groupId',
//								type : 'number'
//							}, 'plat_groupName'],
//					autoLoad : true,
//					proxy : {
//						type : 'ajax',
//						url : 'contvideo/plat_group_cont_video.do?siteId=-1',
//						reader : {
//							root : 'plat_group_data'
//						}
//					}
//				});
//
//		var app_img_platgroup_form = new Ext.form.ComboBox({
//					fieldLabel : '*所属平台',
//					emptyText : '请选择所属平台...',
//					baseCls : 'x-plain',
//					xtype : 'combo',
//					// id: 'app_img_platgroup',
//					name : 'plat_groupId',
//					hiddenName : 'plat_groupId',
//					// width:160,
//					maxHeight : 180,
//					hideLabel : false,
//					allowBlank : false,
//					disabled : true,
//					displayField : 'plat_groupName',
//					valueField : 'plat_groupId',
//					editable : false,
//					// readOnly:true,
//					queryMode : 'local',
//					triggerAction : 'all',
//					store : app_img_platgroup_JsonStore
//				});
//		var img_locked_checkbox = new Ext.form.Checkbox({
//					// id:'base_locked',
//					name : 'img_locked',
//					width : 260,// 宽度
//					// height:compHeight,
//					align : 'left',
//					fieldLabel : '图片信息锁定',
//					checked : false,
//					disabled : true
//
//				});
//		var app_img_window = new Ext.Window({
//			title : '图片预览',
//			resizable : false,
//			width : 900,
//			height : 500,
//			autoScroll : true,
//			modal : true,
//			closable : true,
//			closeAction : 'hide',
//			items : [{
//				baseCls : 'x-plain',
//				items : [{
//					xtype : 'box',
//					id : 'app_img_window',
//					border : false,
//					fieldLabel : "预览图片",
//					autoEl : {
//						height : 430,
//						tag : 'img',
//						src : Ext.BLANK_IMAGE_URL
//					},
//					listeners : {
//						loadexception : function() {
//							Ext.getCmp("app_img_window").getEl().dom.src = 'app/resources/images/empty.JPG';
//						}
//					}
//				}]
//			}],
//			buttons : [{
//				text : '取消',
//				handler : function() {
//					app_img_window.hide();
//					Ext.getCmp("app_img_window").getEl().dom.src = Ext.BLANK_IMAGE_URL;
//				}
//			}]
//		});
//
//		var app_big_img_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 380,
//			allowBlank : false,
//			disabled : true,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传横图',
//			labelAlign : 'right',
//			name : 'c_img_file',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//
//		});
//
//		var app_big_img_upload_btn = new Ext.Button({
//					text : '上传',
//					disabled : true,
//					name : 'app_big_img_upload_btn',
//					handler : function() {
//						if (app_big_img_fileUpload_form.getForm().isValid()) {
//							var id = cont_id_form.getValue();
//							Ext.getCmp("app_big_img_filename")
//									.setText(app_big_img_fileUploadField.value);
//
//							app_big_img_fileUpload_form.getForm().submit({
//								url : 'img/img_file_upload.do?tag=c_img_file&obj_id='
//										+ id
//										+ '&date='
//										+ Ext.util.Format.date(new Date(),
//												"YmdHis") + '&contTypeId=7',
//								waitMsg : '正在上传...',
//								success : function(form, action) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传成功",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//
//									var result = Ext
//											.decode(action.response.responseText);
//									Ext.getCmp("app_img_url")
//											.setValue(result.img_url);
//									Ext.getCmp("app_img_url_show")
//											.setValue(result.img_url_show);
//									app_img_big.getEl().dom.src = 'app/resources/images/loading.JPG';
//									app_img_big.getEl().dom.src = result.img_url_show;
//								},
//								failure : function(form, action) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传失败",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//								}
//							});
//						}
//					}
//				});
//		var app_img_big = new Ext.Component({
//					xtype : 'box',
//					name : 'app_img_big',
//					width : 40,
//					height : 30,
//
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_big_img_fileUpload_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			layout : 'column',
//			border : false,
//			items : [{
//				layout : 'column',
//				items : [{
//							columnWidth : 0.7,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : app_big_img_fileUploadField
//						}, {
//							columnWidth : 0.15,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_img_url_show")
//											.getValue();
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "图片不存在，无法预览",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}]
//						}, {
//							columnWidth : 0.15,
//							border : false,
//							items : [app_img_big]
//						}]
//			}],
//			buttons : [{
//						id : 'app_big_img_filename',
//						disabled : true,
//						baseCls : 'x-plain',
//						style : 'color:#ff0000;font-size: 13px'
//					}, app_big_img_upload_btn, {
//						disabled : true,
//						baseCls : 'x-plain'
//					}]
//		});
//
//		var app_little_img_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 380,
//			allowBlank : false,
//			disabled : true,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传竖图',
//			name : 'c_img_little_file',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//		});
//
//		var app_little_img_upload_btn = new Ext.Button({
//			text : '上传',
//			disabled : true,
//			name : 'app_little_img_upload_btn',
//			handler : function() {
//				if (app_little_img_fileUpload_form.getForm().isValid()) {
//					var id = cont_id_form.getValue();
//					Ext.getCmp("app_little_img_filename")
//							.setText(app_little_img_fileUploadField.value);
//
//					app_little_img_fileUpload_form.getForm().submit({
//						url : 'img/img_file_upload.do?tag=c_img_little_file&obj_id=' + id
//								+ '&date='
//								+ Ext.util.Format.date(new Date(), "YmdHis")
//								+ '&contTypeId=7',
//						waitMsg : '正在上传...',
//						success : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传成功",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//
//							var result = Ext
//									.decode(action.response.responseText);
//							Ext.getCmp("app_img_url_little")
//									.setValue(result.img_url);
//							Ext.getCmp("app_img_url_little_show")
//									.setValue(result.img_url_show);
//							app_img_little.getEl().dom.src = 'app/resources/images/loading.JPG';
//							app_img_little.getEl().dom.src = result.img_url_show;
//						},
//						failure : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传失败",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//						}
//					});
//				}
//			}
//		});
//		var app_img_little = new Ext.Component({
//					xtype : 'box',
//					name : 'app_img_little',
//					// width : 40,
//					height : 30,
//
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_little_img_fileUpload_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			layout : 'column',
//			border : false,
//			items : [{
//				layout : 'column',
//				items : [{
//							columnWidth : .7,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : app_little_img_fileUploadField
//						}, {
//							columnWidth : .15,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_img_url_little_show")
//											.getValue();
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "图片不存在，无法预览",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}]
//						}, {
//							columnWidth : .15,
//							border : false,
//							items : [app_img_little]
//						}]
//			}],
//			buttons : [{
//						id : 'app_little_img_filename',
//						disabled : true,
//						baseCls : 'x-plain',
//						style : 'color:#ff0000;font-size: 13px'
//					}, app_little_img_upload_btn, {
//						disabled : true,
//						baseCls : 'x-plain'
//					}]
//		});
//
//		var app_icon_img_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 380,
//			allowBlank : false,
//			disabled : true,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传小方形图',
//			name : 'c_img_icon_file',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//		});
//
//		var app_icon_img_upload_btn = new Ext.Button({
//					text : '上传',
//					disabled : true,
//					name : 'app_icon_img_upload_btn',
//					handler : function() {
//						if (app_icon_img_fileUpload_form.getForm().isValid()) {
//							var id = cont_id_form.getValue();
//							Ext
//									.getCmp("app_icon_img_filename")
//									.setText(app_icon_img_fileUploadField.value);
//
//							app_icon_img_fileUpload_form.getForm().submit({
//								url : 'img/img_file_upload.do?tag=c_img_icon_file&obj_id='
//										+ id
//										+ '&date='
//										+ Ext.util.Format.date(new Date(),
//												"YmdHis") + '&contTypeId=7',
//								waitMsg : '正在上传...',
//								success : function(form, action) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传成功",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//
//									var result = Ext
//											.decode(action.response.responseText);
//									Ext.getCmp("app_img_url_icon")
//											.setValue(result.img_url);
//									Ext.getCmp("app_img_url_icon_show")
//											.setValue(result.img_url_show);
//									app_img_icon.getEl().dom.src = 'app/resources/images/loading.JPG';
//									app_img_icon.getEl().dom.src = result.img_url_show;
//								},
//								failure : function(form, action) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传失败",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//								}
//							});
//						}
//					}
//				});
//		var app_img_icon = new Ext.Component({
//					xtype : 'box',
//					name : 'app_img_icon',
//					// width : 40,
//					height : 30,
//
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_icon_img_fileUpload_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			layout : 'column',
//			border : false,
//			items : [{
//				layout : 'column',
//				items : [{
//							columnWidth : .7,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : app_icon_img_fileUploadField
//						}, {
//							columnWidth : .15,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_img_url_icon_show")
//											.getValue();
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "图片不存在，无法预览",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}]
//						}, {
//							columnWidth : .15,
//							border : false,
//							items : [app_img_icon]
//						}]
//			}],
//			buttons : [{
//						id : 'app_icon_img_filename',
//						disabled : true,
//						baseCls : 'x-plain',
//						style : 'color:#ff0000;font-size: 13px'
//					}, app_icon_img_upload_btn, {
//						disabled : true,
//						baseCls : 'x-plain'
//					}]
//		});
//		var app_4_squares_img_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 380,
//			allowBlank : false,
//			disabled : true,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传四方图',
//			name : 'c_img_4_squares_file',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//		});
//
//		var app_4_squares_img_upload_btn = new Ext.Button({
//			text : '上传',
//			disabled : true,
//			name : 'app_4_squares_img_upload_btn',
//			handler : function() {
//				if (app_4_squares_img_fileUpload_form.getForm().isValid()) {
//					var id = cont_id_form.getValue();
//					Ext.getCmp("app_4_squares_img_filename")
//							.setText(app_4_squares_img_fileUploadField.value);
//
//					app_4_squares_img_fileUpload_form.getForm().submit({
//						url : 'img/img_file_upload.do?tag=c_img_4_squares_file&obj_id=' + id
//								+ '&date='
//								+ Ext.util.Format.date(new Date(), "YmdHis")
//								+ '&contTypeId=7',
//						waitMsg : '正在上传...',
//						success : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传成功",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//
//							var result = Ext
//									.decode(action.response.responseText);
//							Ext.getCmp("app_img_url_4_squares")
//									.setValue(result.img_url);
//							Ext.getCmp("app_img_url_4_squares_show")
//									.setValue(result.img_url_show);
//							app_img_4_squares.getEl().dom.src = 'app/resources/images/loading.JPG';
//							app_img_4_squares.getEl().dom.src = result.img_url_show;
//						},
//						failure : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传失败",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//						}
//					});
//				}
//			}
//		});
//		var app_img_4_squares = new Ext.Component({
//					xtype : 'box',
//					name : 'app_img_4_squares',
//					// width : 40,
//					height : 30,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_4_squares_img_fileUpload_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			layout : 'column',
//			border : false,
//			items : [{
//				layout : 'column',
//				items : [{
//							columnWidth : .7,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : app_4_squares_img_fileUploadField
//						}, {
//							columnWidth : .15,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_img_url_4_squares_show")
//											.getValue();
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "图片不存在，无法预览",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}]
//						}, {
//							columnWidth : .15,
//							border : false,
//							items : [app_img_4_squares]
//						}]
//			}],
//			buttons : [{
//						id : 'app_4_squares_img_filename',
//						disabled : true,
//						baseCls : 'x-plain',
//						style : 'color:#ff0000;font-size: 13px'
//					}, app_4_squares_img_upload_btn, {
//						disabled : true,
//						baseCls : 'x-plain'
//					}]
//		});
//
//		var app_6_squares_img_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 380,
//			allowBlank : false,
//			disabled : true,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传六方图',
//			name : 'c_img_6_squares_file',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//		});
//
//		var app_6_squares_img_upload_btn = new Ext.Button({
//			text : '上传',
//			disabled : true,
//			name : 'app_6_squares_img_upload_btn',
//			handler : function() {
//				if (app_6_squares_img_fileUpload_form.getForm().isValid()) {
//					var id = cont_id_form.getValue();
//					Ext.getCmp("app_6_squares_img_filename")
//							.setText(app_6_squares_img_fileUploadField.value);
//
//					app_6_squares_img_fileUpload_form.getForm().submit({
//						url : 'img/img_file_upload.do?tag=c_img_6_squares_file&obj_id=' + id
//								+ '&date='
//								+ Ext.util.Format.date(new Date(), "YmdHis")
//								+ '&contTypeId=7',
//						waitMsg : '正在上传...',
//						success : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传成功",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//
//							var result = Ext
//									.decode(action.response.responseText);
//							Ext.getCmp("app_img_url_6_squares")
//									.setValue(result.img_url);
//							Ext.getCmp("app_img_url_6_squares_show")
//									.setValue(result.img_url_show);
//							app_img_6_squares.getEl().dom.src = 'app/resources/images/loading.JPG';
//							app_img_6_squares.getEl().dom.src = result.img_url_show;
//						},
//						failure : function(form, action) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片上传失败",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//						}
//					});
//				}
//			}
//		});
//		var app_img_6_squares = new Ext.Component({
//					xtype : 'box',
//					name : 'app_img_6_squares',
//					// width : 40,
//					height : 30,
//
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_6_squares_img_fileUpload_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			layout : 'column',
//			border : false,
//			items : [{
//				layout : 'column',
//				items : [{
//							columnWidth : .7,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : app_6_squares_img_fileUploadField
//						}, {
//							columnWidth : .15,
//							// layout:'form',
//							baseCls : 'x-plain',
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_img_url_6_squares_show")
//											.getValue();
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "图片不存在，无法预览",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}]
//						}, {
//							columnWidth : .15,
//							border : false,
//							items : [app_img_6_squares]
//						}]
//			}],
//			buttons : [{
//						id : 'app_6_squares_img_filename',
//						disabled : true,
//						baseCls : 'x-plain',
//						style : 'color:#ff0000;font-size: 13px'
//					}, app_6_squares_img_upload_btn, {
//						disabled : true,
//						baseCls : 'x-plain'
//					}]
//		});
//
//		var app_apk_fileUploadField = new Ext.form.FileUploadField({
//					xtype : 'fileuploadfield',
//					height : 20,
//					width : 550,
//					//allowBlank : false,
//					disabled : true,
//					blankText : 'APK地址不能为空，请选择一个APK',
//					emptyText : '请选择一个APK',
//					regex : /^.*(.apk|.APK)$/,
//					regexText : '请选择APK文件（文件扩展名必须为.apk）',
//					fieldLabel : '*上传APK',
//					name : 'imgFile',
//					buttonText : '',
//					baseCls : 'x-plain',
//					buttonConfig : {
//						iconCls : 'upload-icon'
//					}
//				});
//
//		var _interval;
//
//		var app_apk_fileUpload_form = new Ext.FormPanel({
//					fileUpload : true,
//					baseCls : 'x-plain',
//					defaults : {
//						baseCls : 'x-plain'
//					},
//					buttonAlign : 'center',
//					width : 580,
//					layout : 'column',
//					border : false,
//					items : [{
//								layout : 'column',
//								items : [{
//											columnWidth : .7,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [app_apk_fileUploadField]
//										}, {
//											columnWidth : .3,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : []
//										}]
//							}],
//					buttons : [{
//						text : '上传',
//						disabled : false,
//						// id:'app_apk_upload_btn',
//						name : 'app_apk_upload_btn',
//						handler : function() {
//							if (app_apk_fileUpload_form.getForm().isValid()) {
//								var id = cont_id_form.getValue();
//								// msgWin.show();
//								Ext.MessageBox.show({
//											title : '提示',
//											msg : "上传进度",
//											progressText : '0%',
//											width : 300,
//											progress : true,
//											closable : true
//										});
//								app_apk_fileUpload_form.getForm().submit({
//									url : 'files/file_upload.do?obj_id='
//											+ id
//											+ '&type=apk&date='
//											+ Ext.util.Format.date(new Date(),
//													"YmdHis"),
//									// waitMsg:'正在上传...',
//									timeout : 1800000,
//									success : function(form, action) {
//										clearInterval(_interval);
//										downloadurl_window.hide();
//										var result = Ext
//												.decode(action.response.responseText);
//										if (!result.success) {
//											Ext.MessageBox.show({
//														title : "错误提示",
//														msg : result.info,
//														width : 110,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										} else {
//											cont_app_download_url_form
//													.setValue(result.apk_url);
//											cont_app_version_form
//													.setValue(result.version);
//											cont_app_version_code_form
//													.setValue(result.version_code);
//											cont_app_capacity_form
//													.setValue(result.file_size);
//											cont_app_package_name_form
//													.setValue(result.package_name);
//											cont_app_md5_form
//													.setValue(result.md5sum);
//											app_download_url_grid_store.load();
//
//											Ext.MessageBox.show({
//														title : "提示",
//														msg : "APK上传成功",
//														width : 110,
//														buttons : Ext.Msg.OK
//													});
//										}
//									},
//									failure : function(form, action) {
//										clearInterval(_interval);
//										var msg = "未知错误！";
//										if (action != null && action.result != null && action.result.info != null){
//											msg = action.result.info;
//										}
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "APK上传失败!" + msg,
//													width : 110,
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//
//									}
//								});
//								clearInterval(_interval);
//								_interval = setInterval(showProgress, 2000);
//							}
//						}
//					}]
//				});
//		var zeroRepeat = 0;
//		function showProgress() {
//			var id = cont_id_form.getValue();
//			Ext.Ajax.request({
//						url : 'files/file_upload_progress.do?obj_id=' + id,
//						method : 'get',
//						timeout : 60000,
//						success : function(data, options) {
//							var i = (data.responseText.substring(0,
//									data.responseText.length - 1))
//									/ 100;
//							Ext.MessageBox.updateProgress(i, data.responseText);
//							if (data.responseText == '100%') {
//								clearInterval(_interval);
//								Ext.MessageBox.updateProgress(i, "信息提取中...");
//								return;
//							} else if (data.responseText == '0%') {
//								zeroRepeat++;
//								if (zeroRepeat > 500) {
//									clearInterval(_interval);
//									zeroRepeat = 0;
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "APK上传失败!!",
//												width : 110,
//												buttons : Ext.Msg.OK,
//												icon : Ext.Msg.ERROR
//											});
//								}
//							}
//						},
//						failure : function(data, options) {
//							clearInterval(_interval);
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "APK上传失败!",
//										width : 110,
//										buttons : Ext.Msg.OK
//									});
//						}
//					});
//		}
//
//		var app_img_intro_form = new Ext.form.TextArea({
//					// layout:'form',
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textarea',
//					// id: 'app_img_intro',
//					name : 'app_img_intro',
//					value : '',
//					disabled : true,
//					height : 50,
//					width : 440,
//					fieldLabel : '图片描述',
//					emptyText : '请对图片进行描述',
//					maxLength : 200,
//					maxLengthText : '不能超过200个字符'
//				});
//
//		var oncheck = function(el, checked) {
//			if (checked) {
//				Ext.getCmp("app_img_isurlused").setValue(el.inputValue);
//				// alert(Ext.getCmp("app_img_isurlused").getValue());
//				for (var ii = 0; ii < 5; ii++) {
//					var radio = contAppImgPanel_form.form
//							.findField("app_img_isurlused_rb" + ii);
//					if (this != radio) {
//						radio.setValue(false);
//					}
//				}
//			}
//		}
//
//		var app_img_isurlused_radiogroup = new Ext.form.RadioGroup({
//					xtype : 'radiogroup',
//					fieldLabel : '推荐位使用的图类型',
//					labelWidth : 120,
//					// hideLabel:true,
//					width : 586,
//					name : "app_img_isurlused_radiogroup",
//					disabled : true,
//					items : [{
//								boxLabel : '横图',
//								name : "app_img_isurlused_rb1",
//								inputValue : 1,
//								listeners : {
//									change : oncheck
//								}
//							}, {
//								boxLabel : '竖图',
//								name : "app_img_isurlused_rb0",
//								inputValue : 0,
//								listeners : {
//									change : oncheck
//								}
//							}, {
//								boxLabel : '小方图',
//								name : "app_img_isurlused_rb2",
//								inputValue : 2,
//								listeners : {
//									change : oncheck
//								}
//							}, {
//								boxLabel : '四格图',
//								name : "app_img_isurlused_rb3",
//								inputValue : 3,
//								listeners : {
//									change : oncheck
//								}
//							}, {
//								boxLabel : '六格图',
//								name : "app_img_isurlused_rb4",
//								inputValue : 4,
//								listeners : {
//									change : oncheck
//								}
//							}]
//				});
//		contAppImgPanel_form = new Ext.FormPanel({
//			title : '资产图片',
//			frame : true,
//			border : true,
//			collapsible : true,
//			titleCollapse : 'true',
//			fieldDefaults : {
//				labelAlign : 'right'
//			},
//			width : 586,
//			autoHeight : true,
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			items : [
//					{
//						layout : 'column',
//						items : [{
//									columnWidth : .5,
//									// layout:'form',
//									baseCls : 'x-plain',
//									items : [{
//												xtype : 'hidden',
//												name : 'app_img_id',
//												id : 'app_img_id',
//												value : '-1'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url',
//												id : 'app_img_url'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_show',
//												id : 'app_img_url_show'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_little',
//												id : 'app_img_url_little'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_little_show',
//												id : 'app_img_url_little_show'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_icon',
//												id : 'app_img_url_icon'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_icon_show',
//												id : 'app_img_url_icon_show'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_4_squares',
//												id : 'app_img_url_4_squares'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_4_squares_show',
//												id : 'app_img_url_4_squares_show'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_6_squares',
//												id : 'app_img_url_6_squares'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_url_6_squares_show',
//												id : 'app_img_url_6_squares_show'
//											}, {
//												xtype : 'hidden',
//												name : 'app_img_isurlused',
//												id : 'app_img_isurlused'
//											}, app_img_active_time, {
//												xtype : 'panel',
//												baseCls : 'x-plain',
//												height : 5
//											}, app_img_platgroup_form]
//								}, {
//									columnWidth : .5,
//									// layout:'form',
//									baseCls : 'x-plain',
//									items : [app_img_deactive_time, {
//												xtype : 'panel',
//												baseCls : 'x-plain',
//												height : 5
//											}, img_locked_checkbox]
//								}]
//					},
//					{
//						xtype : 'label',
//						text : '注意：新增软件信息时，需要先保存后再上传横图和竖图！',
//						style : 'background-color:#ffff00;font-size: 13px; margin-left: 50px;'
//					}, app_img_isurlused_radiogroup,
//					app_big_img_fileUpload_form,
//					app_little_img_fileUpload_form,
//					app_icon_img_fileUpload_form,
//					app_4_squares_img_fileUpload_form,
//					app_6_squares_img_fileUpload_form, app_img_intro_form]
//		});
//		var contApkPanel_form = new Ext.Panel({
//			title : '方式1：APK本地文件',
//			frame : true,
//			collapsible : true,
//			animate : true,
//			border : true,
//			titleCollapse : 'true',
//			fieldDefaults : {
//				labelAlign : 'right'
//			},
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			width : 586,
//			autoHeight : true,
//			items : [{
//				layout : 'column',
//				items : [{
//					bodyStyle : 'padding:12 0 0 0',
//					// layout:'form',
//					baseCls : 'x-plain',
//					style : 'text-align:center',
//					items : [{
//						xtype : 'label',
//						text : '注意：新增软件信息时，需要先保存后再上传APK！',
//						style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
//					}, app_apk_fileUpload_form]
//				}]
//			}]
//		});
//		var apk_http_sand_tower = new Ext.form.Checkbox({
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textfield',
//					// id: 'update_url_mod',
//					name : 'apk_http_sand_tower',
//					// width: 150,
//					fieldLabel : '沙塔cdn（url地址不含防盗链）',
//					maxLength : 1000
//				});
//		var apk_http_url_mod = new Ext.form.TextField({
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textfield',
//					// id: 'update_url_mod',
//					name : 'update_url_360',
//					//allowBlank : false,
//					width : 550,
//					fieldLabel : '*http地址',
//					blankText : 'htt地址不能为空',
//					emptyText : '请输入一个http地址',
//					maxLength : 1000,
//					maxLengthText : '不能超过1000个字符'
//				});
//		var contApkHttpPanel_form = new Ext.FormPanel({
//			title : '方式2：http地址',
//			scrollable : true,
//			frame : true,
//			collapsible : true,
//			animate : true,
//			border : true,
//			titleCollapse : true,
//			fieldDefaults : {
//				labelAlign : 'right'
//			},
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			// id:'contApkHttpPanel_form',
//			name : 'contApkHttpPanel_form',
//			width : 586,
//			autoHeight : true,
//			items : [{
//				xtype : 'label',
//				text : '注意：新增软件信息时，需要先保存后再确认从http上传到服务器！',
//				style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
//			}, , apk_http_url_mod, apk_http_sand_tower],
//			buttons : [{
//				xtype : 'button',
//				text : '从http上传',
//				handler : function() {
//					var c_id = cont_id_form.getValue();
//					var sand_tower_cdn = apk_http_sand_tower.getValue() == true
//							? 1
//							: 0;
//					var appDownloadUrl = apk_http_url_mod.getValue();
//					Ext.MessageBox.wait('处理中，请稍后...');
//					Ext.Ajax.request({
//								url : 'apps/apk_http_download.do',
//								params : {
//									c_id : c_id,
//									sand_tower : sand_tower_cdn,
//									appDownloadUrl : appDownloadUrl
//								},
//								success : function(response) {
//									Ext.MessageBox.hide();
//									downloadurl_window.hide();
//									var result = Ext
//											.decode(response.responseText);
//									if (!result.success) {
//										Ext.MessageBox.show({
//													title : "错误提示",
//													msg : result.info,
//													width : 200,
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//									} else {
//										cont_app_download_url_form
//												.setValue(result.apk_url);
//										cont_app_version_form
//												.setValue(result.version);
//										cont_app_version_code_form
//												.setValue(result.version_code);
//										cont_app_capacity_form
//												.setValue(result.file_size);
//										cont_app_package_name_form
//												.setValue(result.package_name);
//										cont_app_md5_form
//												.setValue(result.md5sum);
//										temp_url_expire_time_mod
//												.setValue(result.temp_url_expire_time);
//										upgrate_temp_url_mod
//												.setValue(result.apk_url);
//
//										app_download_url_grid_store.load();
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "从http上传到服务器成功",
//													width : 200,
//													buttons : Ext.Msg.OK
//												});
//									}
//								},
//								failure : function(response) {
//									Ext.MessageBox.hide();
//									if (response == null) {
//										Ext.MessageBox.show({
//													title : "错误提示",
//													msg : "网络错误",// 从http上传到服务器失败
//													width : 200,
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//									} else {
//										var result = Ext
//												.decode(response.responseText);
//										if (!result.success) {
//											Ext.MessageBox.show({
//														title : "错误提示",
//														msg : result.info,
//														width : 200,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									}
//								}
//							});
//				}
//			}]
//
//		});
//		// ****360云盘升级地址*begin******************************************************************************
//		var url_360_id_mod = new Ext.form.TextField({
//					xtype : 'textfield',
//					name : 'url_360_id',
//					hidden : true,
//					hideLabel : true
//				});
//		var update_url_mod_360 = new Ext.form.TextField({
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textfield',
//					// id: 'update_url_mod',
//					name : 'update_url_360',
//					allowBlank : false,
//					width : 550,
//					fieldLabel : '*360分享地址',
//					maxLength : 1000,
//					maxLengthText : '不能超过1000个字符'
//				});
//		var update_url_share_password_360 = new Ext.form.TextField({
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textfield',
//					// id: 'share_password',
//					name : 'share_password_360',
//					allowBlank : true,
//					width : 550,
//					fieldLabel : '360分享密码',
//					maxLength : 100,
//					maxLengthText : '不能超过100个字符'
//				});
//
//		var upgrate_temp_url_mod = new Ext.form.TextArea({
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textarea',
//					// id: 'upgrate_temp_url_mod',
//					name : 'upgrate_temp_url_mod',
//					// allowBlank: false,
//					readOnly : true,
//					// disabled:true,
//					height : 50,
//					width : 550,
//					fieldLabel : '360临时地址',
//					maxLength : 500,
//					maxLengthText : '不能超过500个字符'
//				});
//
//		var temp_url_expire_time_mod = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					// id: 'temp_url_expire_time_mod',
//					name : 'temp_url_expire_time_mod',
//					fieldLabel : '360临时地址过期时间',
//					width : 550,
//					// height:250,
//					// allowBlank: false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					// renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择开始时间',
//					emptyText : '请选择开始时间'
//				});
//		var modify_updateurl_360_form = new Ext.FormPanel({
//			title : '方式3：360云盘分享地址',
//			scrollable : true,
//			frame : true,
//			collapsible : true,
//			animate : true,
//			border : true,
//			titleCollapse : true,
//			fieldDefaults : {
//				labelAlign : 'right'
//			},
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			// id:'modify_updateurl_360_form',
//			name : 'modify_updateurl_360_form',
//			width : 586,
//			autoHeight : true,
//			items : [{
//				layout : 'column',
//				items : [{
//					bodyStyle : 'padding:12 0 0 0',
//					// layout:'form',
//					baseCls : 'x-plain',
//					style : 'text-align:center',
//					items : [
//							{
//								xtype : 'label',
//								text : '注意：新增软件信息时，需要先保存后再确认从云盘上传到服务器！',
//								style : 'background-color:#ffff00;font-size: 13px,margin-left: 50px;'
//							}, , url_360_id_mod, update_url_mod_360,
//							update_url_share_password_360,
//							temp_url_expire_time_mod, upgrate_temp_url_mod]
//				}]
//			}],
//			buttons : [{
//				xtype : 'button',
//				text : '从云盘上传',
//				handler : function() {
//					var c_id = cont_id_form.getValue();
//					var id = url_360_id_mod.getValue();
//					var appDownloadUrl = update_url_mod_360.getValue();
//					var pwd = update_url_share_password_360.getValue();
//					Ext.MessageBox.wait('处理中，请稍后...');
//					Ext.Ajax.request({
//								url : 'apps/cloud_download.do',
//								params : {
//									id : id,
//									c_id : c_id,
//									appDownloadUrl : appDownloadUrl,
//									sharePasswd : pwd
//								},
//								success : function(response) {
//									Ext.MessageBox.hide();
//									downloadurl_window.hide();
//									var result = Ext
//											.decode(response.responseText);
//									if (!result.success) {
//										Ext.MessageBox.show({
//													title : "错误提示",
//													msg : result.info,
//													width : 200,
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//									} else {
//										cont_app_download_url_form
//												.setValue(result.apk_url);
//										cont_app_version_form
//												.setValue(result.version);
//										cont_app_version_code_form
//												.setValue(result.version_code);
//										cont_app_capacity_form
//												.setValue(result.file_size);
//										cont_app_package_name_form
//												.setValue(result.package_name);
//										cont_app_md5_form
//												.setValue(result.md5sum);
//										temp_url_expire_time_mod
//												.setValue(result.temp_url_expire_time);
//										upgrate_temp_url_mod
//												.setValue(result.apk_url);
//
//										app_download_url_grid_store.load();
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "从云盘上传到服务器成功",
//													width : 200,
//													buttons : Ext.Msg.OK
//												});
//									}
//								},
//								failure : function(response) {
//									Ext.MessageBox.hide();
//									if (response == null) {
//										Ext.MessageBox.show({
//													title : "错误提示",
//													msg : "网络错误",// 从云盘上传到服务器失败
//													width : 200,
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//									} else {
//										var result = Ext
//												.decode(response.responseText);
//										if (!result.success) {
//											Ext.MessageBox.show({
//														title : "错误提示",
//														msg : result.info,
//														width : 200,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									}
//								}
//							});
//				}
//			}]
//		});
//
//		var app_download_url_grid_store = new Ext.data.JsonStore({
//					pageSize : pgSize,
//					fields : ['id', 'c_id', 'app_name', 'package_name',
//							'provider_id', 'version', 'version_code', 'site',
//							'capacity', 'md5sum', 'add_time', 'create_time',
//							'modify_time', 'download_url', 'url_type',
//							'url_type_desc', 'upgrade_temp_url',
//							'temp_url_exepire_time', 'share_password'],
//					proxy : {
//						type : 'ajax',
//						url : 'apps/query_app_download_url.do',
//						reader : {
//							totalProperty : "results",
//							root : "datastr",
//							idProperty : 'id'
//						},
//						extraParams : {
//							c_id : 0
//						}
//					}
//				});
//
//		var downloadurl_window = new Ext.Window({
//					title : '上传apk(任选一种上传方式)',
//					resizable : false,
//					width : 600,
//					height : 550,
//					autoHeight : true,
//					autoScroll : true,
//					modal : true,
//					closable : true,
//					closeAction : 'hide',
//					items : [contApkPanel_form, contApkHttpPanel_form,
//							modify_updateurl_360_form],
//					buttons : [{
//								text : '关闭',
//								handler : function() {
//									downloadurl_window.hide();
//								}
//							}]
//				});
//		var contAppPanel_downloadurl_new = function() {
//			var c_id = cont_id_form.getValue();
//			if (c_id == null || c_id <= 0) {
//				Ext.MessageBox.show({
//							title : "提示",
//							msg : "请先选择一个资产",
//							width : 160,
//							buttons : Ext.Msg.OK
//						});
//			} else {
//				downloadurl_window.show();
//			}
//		}
//		var contAppPanel_downloadurl_download = function() {
//			var rows = app_download_url_grid.getSelectionModel().getSelection();// 返回值为
//																				// Record
//																				// 数组
//			if (rows.length == 1) {
//				var show_url = rows[0].get('upgrade_temp_url');
//
//				if (show_url != null && show_url != '') {
//					pic = window.open(show_url, "a1");
//					pic.document.execCommand("SaveAs");
//				} else {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "APK不存在，无法下载",
//								width : 180,
//								buttons : Ext.Msg.OK,
//								icon : Ext.Msg.ERROR
//							});
//				}
//			} else {
//				Ext.MessageBox.show({
//							title : "提示",
//							msg : "请先选择要下载的一项",
//							width : 160,
//							buttons : Ext.Msg.OK
//						});
//			}
//		}
//		var contAppPanel_downloadurl_delete = function() {
//			var rows = app_download_url_grid.getSelectionModel().getSelection();// 返回值为
//																				// Record
//																				// 数组
//			if (rows.length == 1) {
//				var urlID = rows[0].get('id');
//				Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//					if (btn == 'yes') {
//						Ext.Ajax.request({
//									url : 'apps/delete_download.do?id=' + urlID,
//									waitMsg : '正在提交,请稍等',
//									success : function(response, opts) {
//										var result = Ext
//												.decode(response.responseText);
//										if (!result.success) {
//											Ext.Msg.show({
//														title : '错误提示',
//														msg : result.info,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										} else {
//											app_download_url_grid_store.load();
//										}
//									},
//									failure : function(response, opts) {
//										Ext.Msg.show({
//													title : '错误提示',
//													msg : '删除时发生错误!',
//													buttons : Ext.Msg.OK,
//													icon : Ext.Msg.ERROR
//												});
//									}
//								});
//					}
//				});
//			} else {
//				Ext.MessageBox.show({
//							title : "提示",
//							msg : "请先选择删除的一项",
//							width : 160,
//							buttons : Ext.Msg.OK
//						});
//			}
//		}
//
//		var app_download_url_grid = new Ext.grid.GridPanel({
//					frame : true,
//					loadMask : {
//						msg : '数据加载中...'
//					},
//					width : 586,
//					height : 300,
//					collapsible : true,
//					titleCollapse : 'true',
//					title : '下载地址列表',
//					iconCls : 'icon-grid',
//					bodyStyle : 'width:100%',
//					viewConfig : {
//						forceFit : true
//					},
//					stripeRows : true,
//					enableHdMenu : false,
//					store : app_download_url_grid_store,
//					autoHeight : false,
//					selModel : {
//						selType : 'checkboxmodel'
//					},
//					columns : [{
//								header : "ID",
//								width : 80,
//								dataIndex : 'id'
//							}, {
//								header : "应用资产ID",
//								width : 60,
//								dataIndex : 'c_id',
//								hidden : true
//							}, {
//								header : '应用名称',
//								width : 100,
//								dataIndex : 'app_name',
//								hidden : true
//							}, {
//								header : "下载地址",
//								width : 80,
//								dataIndex : 'download_url'
//							}, {
//								header : "应用类型",
//								width : 80,
//								dataIndex : 'c_type',
//								hidden : true
//							}, {
//								header : "包名",
//								width : 80,
//								dataIndex : 'package_name',
//								hidden : true
//							}, {
//								header : "ProviderId",
//								width : 80,
//								dataIndex : 'provider_id'
//							}, {
//								header : "VersionName",
//								width : 80,
//								dataIndex : 'version'
//							}, {
//								header : "VersionCode",
//								width : 80,
//								dataIndex : 'version_code'
//							}, {
//								header : "网站",
//								width : 80,
//								dataIndex : 'site'
//							}, {
//								header : "大小",
//								width : 80,
//								dataIndex : 'capacity'
//							}, {
//								header : "MD5值",
//								width : 80,
//								dataIndex : 'md5sum'
//							}, {
//								header : "上架时间",
//								width : 80,
//								dataIndex : 'add_time'
//							}, {
//								header : "创建时间",
//								width : 80,
//								dataIndex : 'create_time'
//							}, {
//								header : "更新时间",
//								width : 80,
//								dataIndex : 'modify_time'
//							}, {
//								header : "地址类型",
//								width : 80,
//								dataIndex : 'url_type',
//								hidden : true
//							}, {
//								header : "地址类型",
//								width : 80,
//								dataIndex : 'url_type_desc'
//							}, {
//								header : "升级临时地址",
//								width : 80,
//								dataIndex : 'upgrade_temp_url'
//							}, {
//								header : "升级临时地址过期时间",
//								width : 80,
//								dataIndex : 'temp_url_exepire_time'
//							}, {
//								header : "分享密码",
//								width : 80,
//								dataIndex : 'share_password'
//							}],
//					tbar : [{
//								text : '上传',
//								handler : contAppPanel_downloadurl_new,
//								scope : this
//							}, {
//								text : '删除',
//								handler : contAppPanel_downloadurl_delete,
//								scope : this
//							}, {
//								text : '下载apk',
//								handler : contAppPanel_downloadurl_download,
//								scope : this
//							}],
//					bbar : new Ext.PagingToolbar({
//								pageSize : pgSize,
//								// width: 358,
//								store : app_download_url_grid_store,
//								displayInfo : true,
//								displayMsg : '第 {0}-- {1}条    共 {2}条',
//								emptyMsg : '没有记录'
//							})
//				});
//
//		// app_download_url_grid_store.load({ // =======翻页时分页参数
//		// params:{
//		// start: 0,
//		// limit: pgSize,
//		// c_id: 0,
//		// }
//		// });
//		// ****360云盘升级地址*end**************************************************************************
//
//		var cont_id_form = new Ext.form.TextField({// 保留
//			xtype : 'textfield',
//			fieldLabel : '内容ID',
//			allowBlank : true,
//			blankText : '',
//			// emptyText:'',
//			readOnly : true,
//			disabled : true,
//			editable : false,
//			// id:'contAppId_temp',
//			name : 'contAppId_temp',
//			width : 260
//		});
//		var cont_app_name_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' *软件名称',
//					allowBlank : false,
//					blankText : '请输入软件名称...',
//					emptyText : '请输入软件名称...',
//					readOnly : false,
//					disabled : true,
//					// id:'cont_app_name_f',
//					name : 'cont_app_name_f',
//					width : 260
//				});
//
//		var cont_app_alias_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' 软件别名',
//					blankText : '请输入软件别名...',
//					emptyText : '请输入软件别名...',
//					allowBlank : true,
//					readOnly : false,
//					disabled : true,
//					// id:'cont_app_alias',
//					name : 'cont_app_alias',
//					width : 260
//				});
//
//		var cont_video_statusStore = new Ext.data.JsonStore({
//
//					fields : ['s_id', 's_name'],
//					autoLoad : true,
//					proxy : {
//						type : 'ajax',
//						url : 'status/query_for_cont.do',
//						reader : {
//							root : 'data'
//						}
//					}
//				});
//
//		var cont_app_status_form = new Ext.form.ComboBox({
//					fieldLabel : '*软件状态',
//					emptyText : '请选择软件状态...',
//					baseCls : 'x-plain',
//					xtype : 'combo',
//					name : 'contStatusId',
//					hiddenName : 'contStatusId',
//					width : 260,
//					maxHeight : 180,
//					hideLabel : false,
//					allowBlank : false,
//					disabled : true,
//					displayField : 's_name',
//					valueField : 's_id',
//					editable : false,
//					// readOnly:true,
//					mode : 'local',
//					triggerAction : 'all',
//					store : cont_video_statusStore
//				});
//
//		var appProviderJsonStore = new Ext.data.JsonStore({
//
//					fields : ['providerId', 'providerName'],
//					autoLoad : true,
//					proxy : {
//						type : 'ajax',
//						url : 'menus/provider_menu.do?siteId=-1&auth=1',
//						reader : {
//							root : 'provider_data'
//						}
//					}
//				});
//
//		var cont_app_provider_form = new Ext.form.ComboBox({
//					fieldLabel : '*提供商',
//					emptyText : '请选择提供商...',
//					baseCls : 'x-plain',
//					xtype : 'combo',
//					name : 'providerId',
//					hiddenName : 'providerId',
//					width : 260,
//					maxHeight : 180,
//					hideLabel : false,
//					allowBlank : false,
//					disabled : true,
//					displayField : 'providerName',
//					valueField : 'providerId',
//					editable : false,
//					// readOnly:true,
//					queryMode : 'local',
//					triggerAction : 'all',
//					store : appProviderJsonStore
//				});
//
//		var contAppActiveTime = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					// id: 'contAppactivetime1',
//					name : 'contAppactivetime1',
//					fieldLabel : '*生效时间',
//					width : 260,
//					// height:250,
//					allowBlank : false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择生效时间',
//					emptyText : '请选择生效时间',
//					value : '2010-01-01 00:00:00'
//				});
//
//		var contAppDeactiveTime = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					name : 'contAppdeactivetime1',
//					// id: 'contAppdeactivetime1',
//					fieldLabel : '*失效时间',
//					width : 260,
//					// height:250,
//					allowBlank : false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择失效时间',
//					emptyText : '请选择失效时间',
//					value : '2050-01-01 00:00:00'
//				});
//
//		var cont_app_staff_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' 开发人员',
//					allowBlank : true,
//					readOnly : false,
//					disabled : true,
//					// id:'cont_app_staff',
//					name : 'cont_app_staff',
//					width : 260
//				});
//
//		var cont_app_version_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' 软件版本',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					// id:'cont_app_version',
//					name : 'cont_app_version',
//					width : 260
//				});
//		var cont_app_version_code_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' 软件版本编码',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					// id:'cont_app_version_code',
//					name : 'cont_app_version_code',
//					width : 260
//				});
//		var cont_app_tags_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : '软件标签',
//					allowBlank : true,
//					blankText : '软件标签',
//					readOnly : false,
//					disabled : true,
//					// id:'tags',
//					name : 'tags',
//					width : 260
//				});
//
//		var cont_app_capacity_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : ' 软件大小(M)',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					value : '0',
//					// id:'cont_app_capacity',
//					name : 'cont_app_capacity',
//					width : 260
//				});
//
//		var cont_add_time_form = new Ext.form.DateField({
//					// layout:'form',
//					baseCls : 'x-plain',
//					name : 'cont_add_time',
//					// id: 'cont_add_time',
//					fieldLabel : '*上架时间',
//					width : 260,
//					// height:250,
//					allowBlank : false,
//					disabled : true,
//					// readOnly: true,
//					grow : true,
//					altFormats : 'Y-m-d H:i:s',
//					format : 'Y-m-d H:i:s',
//					blankText : '请选择上架时间',
//					emptyText : '请选择上架时间'
//				});
//
//		var cont_app_package_name_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : '软件包名',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					value : '',
//					// id:'cont_app_package_name',
//					name : 'cont_app_package_name',
//					width : 380
//				});
//
//		var cont_app_md5_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : 'MD5串',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					value : '',
//					// id:'cont_app_md5',
//					name : 'cont_app_md5',
//					width : 380
//				});
//
//		var cont_app_download_url_form = new Ext.form.TextField({
//					xtype : 'textfield',
//					fieldLabel : '下载地址',
//					allowBlank : true,
//					blankText : '上传apk后自动获取',
//					readOnly : true,
//					disabled : true,
//					value : '',
//					// id:'cont_app_download_url',
//					name : 'cont_app_download_url',
//					width : 380
//				});
//
//		var cont_app_discription_form = new Ext.form.TextArea({
//					// layout:'form',
//					baseCls : 'x-plain',
//					bodyStyle : 'padding:6 0 0 100',
//					xtype : 'textarea',
//					// id: 'cont_app_discription',
//					name : 'cont_app_discription',
//					value : '',
//					disabled : true,
//					height : 100,
//					width : 586,
//					fieldLabel : '软件描述',
//					maxLength : 200,
//					maxLengthText : '不能超过200个字符'
//				});
//
//		// 应用截图开始
//		var snapshotimg_locked_checkbox = new Ext.form.Checkbox({
//					// id:'base_locked',
//					name : 'snapshotimg_locked',
//					// width:160,//宽度
//					// height:compHeight,
//					align : 'left',
//					fieldLabel : '截图锁定',
//					checked : false,
//					disabled : true
//				});
//		/*-----定义截图下拉列表-----*/
//		var app_snapshotimg_combo = Ext.create('Ext.form.field.ComboBox', {
//			fieldLabel : '已上传截图',
//			// height:20,
//			labelWidth : 80,
//			width : 360,
//			hideLabel : false,
//			labelAlign : 'right',
//			displayField : 'app_img_url',
//			valueField : 'app_img_id',
//			disabled : false,
//			editable : false,
//			// readOnly:true,
//			queryMode : 'local',
//			triggerAction : 'all',
//			emptyText : '请选择一个截图预览',
//
//			store : new Ext.data.JsonStore({
//				fields : ['app_img_id', 'app_img_url', 'app_img_url_show',
//						'snapshotimg_locked'],
//				autoLoad : false,
//				proxy : {
//					type : 'ajax',
//					url : "apps/query_app_snapshotimg.do",
//					reader : {
//						root : 'datastr',
//						idProperty : 'app_img_id'
//					},
//					extraParams : {
//						id : contId_tmp
//					}
//				},
//
//				listeners : {
//					select : {
//						fn : function(combo, value, opts) {
//							alert(value.data.app_img_id);
//							alert(value.data.app_img_url_show);
//						}
//					},
//					load : function(oStore, ayRecords, successful, oOptions) {
//						// var records = store.getRange(0, 1);
//						// alert('loaded successfully');
//						for (var ii = 0; ii < 5 && ii < ayRecords.length; ii++) {
//							var imgObj = null;
//							switch (ii) {
//								case 0 :
//									imgObj = app_snapshotimg0;
//									break;
//								case 1 :
//									imgObj = app_snapshotimg1;
//									break;
//								case 2 :
//									imgObj = app_snapshotimg2;
//									break;
//								case 3 :
//									imgObj = app_snapshotimg3;
//									break;
//								case 4 :
//									imgObj = app_snapshotimg4;
//									break;
//							}
//							imgObj.getEl().dom.src = 'app/resources/images/loading.JPG';
//							var record = ayRecords[ii];
//							imgObj.getEl().dom.src = record
//									.get('app_img_url_show');
//							// alert(record.get('app_img_url_show'));
//						}
//						for (var ii = ayRecords.length; ii < 5; ii++) {
//							var imgObj = null;
//							switch (ii) {
//								case 0 :
//									imgObj = app_snapshotimg0;
//									break;
//								case 1 :
//									imgObj = app_snapshotimg1;
//									break;
//								case 2 :
//									imgObj = app_snapshotimg2;
//									break;
//								case 3 :
//									imgObj = app_snapshotimg3;
//									break;
//								case 4 :
//									imgObj = app_snapshotimg4;
//									break;
//							}
//							imgObj.getEl().dom.src = 'app/resources/images/empty.JPG';
//						}
//						for (var ii = 0; ii < 5 && ii < ayRecords.length; ii++) {
//							var record = ayRecords[ii];
//							snapshotimg_locked_checkbox.setValue(record
//									.get('snapshotimg_locked'));
//						}
//					}
//				}
//			})
//		});
//
//		app_snapshotimg_combo.on('select', function(combo, records, eOpts) {
//					Ext.getCmp("app_snapshotimg_id").setValue(this.getValue());
//					Ext.getCmp("app_snapshotimg_url_show")
//							.setValue(records.data.app_img_url_show);
//				});
//
//		var app_snapshotimg_fileUploadField = new Ext.form.FileUploadField({
//			xtype : 'fileuploadfield',
//			height : 20,
//			width : 500,
//			allowBlank : false,
//			disabled : false,
//			blankText : '图片地址不能为空，请选择一张图片',
//			emptyText : '请选择一张本地图片',
//			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
//			regexText : '请选择图片文件',
//			fieldLabel : '上传新截图',
//			name : 'snapshot',
//			buttonText : '',
//			buttonConfig : {
//				iconCls : 'upload-icon'
//			}
//		});
//
//		var app_snapshotimg_delete_btn = new Ext.Button({
//			xtype : 'button',
//			text : '删除截图',
//			// width: 120,
//			// height: 30,
//			name : 'app_snapshotimg_delete_btn',
//			handler : function() {
//				var id = Ext.getCmp("app_snapshotimg_id").getValue();
//
//				if (id == null || id == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "请先选择一个截图",
//								width : 110,
//								buttons : Ext.Msg.OK
//							});
//				} else {
//					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//						if (btn == 'yes') {
//							Ext.Ajax.request({
//										url : 'img/img_delete.do?tag=delete&obj_id='
//												+ id
//												+ '&date='
//												+ Ext.util.Format.date(
//														new Date(), "YmdHis"),
//										waitMsg : '正在提交,请稍等',
//										success : function(response) {
//											app_snapshotimg_combo.setValue("");
//											app_snapshotimg_combo.store
//													.reload();// 刷新
//											Ext.MessageBox.show({
//														title : "提示",
//														msg : "删除成功",
//														width : 110,
//														buttons : Ext.Msg.OK
//													});
//										},
//										failure : function() {
//											Ext.MessageBox.show({
//														title : "提示",
//														msg : "删除失败",
//														width : 110,
//														buttons : Ext.Msg.OK
//													});
//										}
//									});
//						}
//					});
//				}
//			}
//		});
//
//		var app_img_locked_btn = new Ext.Button({
//			text : '保存锁定状态',
//			disabled : true,
//			name : 'app_img_locked_btn',
//			handler : function() {
//				targetId = cont_id_form.getValue(), useType = 2, locked = snapshotimg_locked_checkbox
//						.getValue() == true ? 1 : 0, Ext.Ajax.request({
//							url : 'img/img_updatelocked.do?targetId=' + targetId
//									+ '&useType=' + useType + '&locked='
//									+ locked,
//							waitMsg : '正在提交,请稍等',
//							success : function(response) {
//								var result = Ext.decode(response.responseText);
//								Ext.Msg.show({
//											title : '温馨提示',
//											msg : result.message,
//											buttons : Ext.Msg.OK,
//											icon : Ext.Msg.OK
//										});
//							},
//							failure : function(response) {
//								Ext.Msg.show({
//											title : '错误提示',
//											msg : '操作时发生错误!',
//											buttons : Ext.Msg.OK,
//											icon : Ext.Msg.ERROR
//										});
//							}
//						});
//
//			}
//		});
//		var app_snapshotimg_upload_btn = new Ext.Button({
//					text : '上传',
//					disabled : true,
//					name : 'app_snapshotimg_upload_btn',
//					handler : function() {
//						if (app_snapshotimg_form.getForm().isValid()) {
//							var id = cont_id_form.getValue();
//							var locked = snapshotimg_locked_checkbox.getValue() == true
//									? 1
//									: 0;
//							app_snapshotimg_form.getForm().submit({
//								url : 'img/img_file_upload.do?tag=snapshot&obj_id='
//										+ id
//										+ '&date='
//										+ Ext.util.Format.date(new Date(),
//												"YmdHis")
//										+ '&contTypeId=7&locked=' + locked,
//								waitMsg : '正在上传...',
//								success : function(form, action) {
//									app_snapshotimg_combo.store.load({
//												params : {
//													id : id
//												}
//											});// 刷新
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传成功",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//
//									var result = Ext
//											.decode(action.response.responseText);
//									// Ext.getCmp("app_snapshotimg_url").setValue(result.img_url);
//									// Ext.getCmp("app_snapshotimg_url_show").setValue(result.img_url_show);
//
//								},
//								failure : function(form, action) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "图片上传失败",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//								}
//							});
//						}
//					}
//				});
//		var app_snapshotimg0 = new Ext.Component({
//					xtype : 'box',
//					name : 'img0',
//					width : 100,
//					height : 100,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_snapshotimg1 = new Ext.Component({
//					xtype : 'box',
//					name : 'img1',
//					width : 100,
//					height : 100,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_snapshotimg2 = new Ext.Component({
//					xtype : 'box',
//					name : 'img2',
//					width : 100,
//					height : 100,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_snapshotimg3 = new Ext.Component({
//					xtype : 'box',
//					name : 'img3',
//					width : 100,
//					height : 100,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		var app_snapshotimg4 = new Ext.Component({
//					xtype : 'box',
//					name : 'img4',
//					width : 100,
//					height : 100,
//					fieldLabel : "预览图片",
//					autoEl : {
//						tag : 'img',
//						src : 'app/resources/images/empty.JPG'
//					}
//				});
//		app_snapshotimg_form = new Ext.FormPanel({
//			fileUpload : true,
//			baseCls : 'x-plain',
//			border : false,
//			fieldDefaults : {
//				labelAlign : 'right',
//				labelWidth : 70
//			},
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			buttonAlign : 'center',
//			width : 580,
//			height : 220,
//			bodyStyle : 'padding:5 0 0 10',
//			items : [
//					{
//						layout : 'column',
//						width : 580,
//						items : [{
//							columnWidth : 0.65,
//							baseCls : 'x-plain',
//							width : 480,
//							items : [app_snapshotimg_combo,
//									snapshotimg_locked_checkbox]
//						}, {
//							columnWidth : 0.3,
//							baseCls : 'x-plain',
//							width : 100,
//							items : [{
//								xtype : 'button',
//								text : '预览图片',
//								// width: 120,
//								// height: 30,
//								handler : function() {
//									var show_url = Ext
//											.getCmp("app_snapshotimg_url_show")
//											.getValue();
//									// app_snapshotimg_combo
//									if (show_url != null && show_url != '') {
//										app_img_window.show();
//										Ext.getCmp("app_img_window").getEl().dom.src = 'app/resources/images/loading.JPG';
//										Ext.getCmp("app_img_window").getEl().dom.src = show_url;
//									} else {
//										Ext.MessageBox.show({
//													title : "提示",
//													msg : "请先选择一个截图",
//													width : 180,
//													buttons : Ext.Msg.OK
//												});
//									}
//								}
//							}, app_snapshotimg_delete_btn, app_img_locked_btn]
//						}]
//					}, app_snapshotimg0, app_snapshotimg1, app_snapshotimg2,
//					app_snapshotimg3, app_snapshotimg4,
//					app_snapshotimg_fileUploadField],
//			buttons : [app_snapshotimg_upload_btn]
//		});
//
//		contApkSnapshotImgPanel_form = new Ext.Panel({
//			title : '软件截图',
//			frame : true,
//			collapsible : true,
//			animate : true,
//			titleCollapse : 'true',
//			fieldDefaults : {
//				labelAlign : 'right'
//			},
//			width : 586,
//			autoHeight : true,
//			defaults : {
//				baseCls : 'x-plain'
//			},
//			items : [{
//				xtype : 'label',
//				text : '注意：新增软件信息时，需要先保存后再上传截图！',
//				style : 'background-color:#ffff00;font-size: 13px; margin-left: 50px'
//			}, app_snapshotimg_form, {
//				xtype : 'hidden',
//				name : 'app_snapshotimg_id',
//				id : 'app_snapshotimg_id'
//			}, {
//				xtype : 'hidden',
//				name : 'app_snapshotimg_url_show',
//				id : 'app_snapshotimg_url_show'
//			}]
//		});
//		// 应用截图结束
//		var contAppFileInfoPanel_form = new Ext.FormPanel({
//					title : '版本信息(无需录入，上传apk后自动提取)',
//					frame : true,
//					collapsible : true,
//					animate : true,
//					border : true,
//					titleCollapse : 'true',
//					fieldDefaults : {
//						labelAlign : 'right'
//					},
//					defaults : {
//						baseCls : 'x-plain'
//					},
//					// columnWidth:.7,
//					// name:'contAppFileInfoPanel_form',
//					width : 586,
//					autoHeight : true,
//					items : [cont_app_package_name_form, cont_app_version_form,
//							cont_app_version_code_form, cont_app_capacity_form,
//							cont_app_md5_form, cont_app_download_url_form]
//				});
//		contAppBasePanel_form = new Ext.FormPanel({
//					title : '基本信息',
//					frame : true,
//					collapsible : true,
//					animate : true,
//					border : true,
//					titleCollapse : 'true',
//					fieldDefaults : {
//						labelAlign : 'right'
//					},
//					defaults : {
//						baseCls : 'x-plain'
//					},
//					// columnWidth:.7,
//					// name:'contAppBasePanel_form',
//					width : 586,
//					autoHeight : true,
//					items : [{
//								xtype : 'panel',
//								baseCls : 'x-plain',
//								height : 10
//							}, {
//								layout : 'column',
//								bodyPadding : 10,
//								items : [{
//											columnWidth : .5,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [
//													// {xtype:'hidden',name:'contAppId_temp',id:'contAppId_temp'},
//													cont_id_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_app_name_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_app_status_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_app_staff_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_app_tags_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, base_locked_checkbox, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}]
//										}, {
//											columnWidth : .5,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [cont_app_alias_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_app_provider_form,
//													{
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, contAppActiveTime, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, contAppDeactiveTime, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, cont_add_time_form, {
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}]
//										}]
//							}, {
//								layout : 'column',
//								items : [{
//											columnWidth : .8,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [{
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}, shortcut_contid_form]
//										}, {
//											columnWidth : .2,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [{
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													},
//													button_select_shortcut_contid]
//										}]
//							}, {
//								layout : 'column',
//								items : [{
//											columnWidth : .8,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [{
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													},
//													cont_app_discription_form]
//										}, {
//											columnWidth : .2,
//											// layout:'form',
//											baseCls : 'x-plain',
//											items : [{
//														xtype : 'panel',
//														baseCls : 'x-plain',
//														height : 5
//													}]
//										}]
//							}, {
//								bodyStyle : 'padding:12 0 0 0',
//								// layout:'form',
//								baseCls : 'x-plain',
//								items : [contAppFileInfoPanel_form]
//							}, shortcut_contid_id_hf]
//				});
//		Ext.define('Cont.ContAppstore', {
//					extend : 'Ext.data.Model',
//					fields : [{
//								name : 'cont_app_name_f',
//								mapping : 'cont_app_name_f',
//								type : 'string'
//							}, {
//								name : 'cont_app_alias',
//								mapping : 'cont_app_alias',
//								type : 'string'
//							}, {
//								name : 'contStatusId',
//								mapping : 'contStatusId',
//								type : 'string'
//							}, {
//								name : 'tags',
//								mapping : 'tags',
//								type : 'string'
//							}, {
//								name : 'providerId',
//								mapping : 'providerId',
//								type : 'string'
//							}, {
//								name : 'superscript_id',
//								mapping : 'superscript_id',
//								type : 'string'
//							}, {
//								name : 'superscript_name',
//								mapping : 'superscript_name',
//								type : 'string'
//							}, {
//								name : 'contAppactivetime1',
//								mapping : 'contAppactivetime1',
//								type : 'string'
//							}, {
//								name : 'base_locked',
//								mapping : 'base_locked',
//								type : 'string'
//							}, {
//								name : 'contAppdeactivetime1',
//								mapping : 'contAppdeactivetime1',
//								type : 'string'
//							}, {
//								name : 'cont_app_package_name',
//								mapping : 'cont_app_package_name',
//								type : 'string'
//							}, {
//								name : 'cont_app_discription',
//								mapping : 'cont_app_discription',
//								type : 'string'
//							}, {
//								name : 'cont_app_staff',
//								mapping : 'cont_app_staff',
//								type : 'string'
//							}, {
//								name : 'cont_app_version',
//								mapping : 'cont_app_version',
//								type : 'string'
//							}, {
//								name : 'cont_app_version_code',
//								mapping : 'cont_app_version_code',
//								type : 'string'
//							}, {
//								name : 'cont_app_capacity',
//								mapping : 'cont_app_capacity',
//								type : 'string'
//							}, {
//								name : 'cont_add_time',
//								mapping : 'cont_add_time',
//								type : 'string'
//							}, {
//								name : 'cont_app_md5',
//								mapping : 'cont_app_md5',
//								type : 'string'
//							}, {
//								name : 'cont_app_download_url',
//								mapping : 'cont_app_download_url',
//								type : 'string'
//							}, {
//								name : 'contAppId_temp',
//								mapping : 'contAppId_temp',
//								type : 'string'
//							}, {
//								name : 'app_img_active_time_1',
//								mapping : 'app_img_active_time_1',
//								type : 'string'
//							}, {
//								name : 'app_img_deactive_time_1',
//								mapping : 'app_img_deactive_time_1',
//								type : 'string'
//							}, {
//								name : 'img_locked',
//								mapping : 'img_locked',
//								type : 'string'
//							}, {
//								name : 'app_img_intro',
//								mapping : 'app_img_intro',
//								type : 'string'
//							}, {
//								name : 'app_img_id',
//								mapping : 'app_img_id',
//								type : 'string'
//							}, {
//								name : 'app_img_url',
//								mapping : 'app_img_url',
//								type : 'string'
//							}, {
//								name : 'app_img_url_little',
//								mapping : 'app_img_url_little',
//								type : 'string'
//							}, {
//								name : 'app_img_url_icon',
//								mapping : 'app_img_url_icon',
//								type : 'string'
//							}, {
//								name : 'app_img_url_4_squares',
//								mapping : 'app_img_url_4_squares',
//								type : 'string'
//							}, {
//								name : 'app_img_url_6_squares',
//								mapping : 'app_img_url_6_squares',
//								type : 'string'
//							}, {
//								name : 'app_img_url_show',
//								mapping : 'app_img_url_show',
//								type : 'string'
//							}, {
//								name : 'app_img_url_little_show',
//								mapping : 'app_img_url_little_show',
//								type : 'string'
//							}, {
//								name : 'app_img_url_icon_show',
//								mapping : 'app_img_url_icon_show',
//								type : 'string'
//							}, {
//								name : 'app_img_url_4_squares_show',
//								mapping : 'app_img_url_4_squares_show',
//								type : 'string'
//							}, {
//								name : 'app_img_url_6_squares_show',
//								mapping : 'app_img_url_6_squares_show',
//								type : 'string'
//							}, {
//								name : 'cont_app_download_url_show',
//								mapping : 'cont_app_download_url_show',
//								type : 'string'
//							}, {
//								name : 'app_img_isurlused',
//								mapping : 'app_img_isurlused',
//								type : 'string'
//							}, {
//								name : 'plat_groupId',
//								mapping : 'plat_groupId',
//								type : 'string'
//							}]
//				});
//		var button_cont_app_save = new Ext.Button({
//			text : "保 存",
//			disabled : true,
//			handler : function() {
//
//				var id = cont_id_form.getValue();
//
//				// 获取软件名称
//				var appName = cont_app_name_form.getValue();
//				if (appName == null || appName == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "软件名称不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				// 获取软件别名
//				var appAlias = cont_app_alias_form.getValue();
//				var base_locked = base_locked_checkbox.getValue() == true
//						? 1
//						: 0;
//				var img_locked = img_locked_checkbox.getValue() == true ? 1 : 0;
//
//				// 获取软件状态
//				var appStatus = cont_app_status_form.getValue();
//				if (appStatus == null || appStatus == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "软件状态不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				// 获取软件提供商
//				var appProvider = cont_app_provider_form.getValue();
//				if (appProvider == null || appProvider == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "提供商不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				// 获取开始时间
//				var activeTime = contAppActiveTime.getValue();
//				if (activeTime == null || activeTime == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "生效时间不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				// 获取截止时间
//				var deactiveTime = contAppDeactiveTime.getValue();
//				if (deactiveTime == null || deactiveTime == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "失效时间不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				if (activeTime > deactiveTime) {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "失效时间不能比生效时间早",
//								width : 180,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				// 获取软件大小
//				var appCapacity = cont_app_capacity_form.getValue();
//				// if(appCapacity==null || appCapacity=='') {
//				// Ext.MessageBox.show({
//				// title:"提示",
//				// msg:"软件大小不能为空",
//				// width:130,
//				// buttons:Ext.Msg.OK
//				// });
//				//	
//				// return null;
//				// }
//
//				// 获取软件版本
//				var appVersion = cont_app_version_form.getValue();
//				var appVersionCode = cont_app_version_code_form.getValue();
//				var cont_appTags = cont_app_tags_form.getValue();
//				// if(appVersion==null || appVersion=='') {
//				// Ext.MessageBox.show({
//				// title:"提示",
//				// msg:"软件版本不能为空",
//				// width:130,
//				// buttons:Ext.Msg.OK
//				// });
//				//	
//				// return null;
//				// }
//				var superscript_id = shortcut_contid_id_hf.getValue();
//				var appStaff = cont_app_staff_form.getValue();
//
//				// 获取截止时间
//				var addTime = cont_add_time_form.getValue();
//				if (addTime == null || addTime == '') {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "上架时间不能为空",
//								width : 130,
//								buttons : Ext.Msg.OK
//							});
//
//					return null;
//				}
//
//				var appPackageName = cont_app_package_name_form.getValue();
//				// if(appPackageName==null || appPackageName=='') {
//				// Ext.MessageBox.show({
//				// title:"提示",
//				// msg:"软件包名不能为空",
//				// width:130,
//				// buttons:Ext.Msg.OK
//				// });
//				//	
//				// return null;
//				// }
//
//				var appMd5 = cont_app_md5_form.getValue();
//
//				var appDownloadUrl = cont_app_download_url_form.getValue();
//				// if(appDownloadUrl==null || appDownloadUrl=='') {
//				// Ext.MessageBox.show({
//				// title:"提示",
//				// msg:"下载地址不能为空",
//				// width:130,
//				// buttons:Ext.Msg.OK
//				// });
//				//	
//				// return null;
//				// }
//
//				var appDiscription = cont_app_discription_form.getValue();
//
//				var app_img_id_value = "-1";
//
//				var app_img_active_time_value = "2012-12-12 01:00:00";
//				var app_img_deactive_time_value = "2112-12-12 01:00:00";
//				var app_img_isurlused_value = "2";
//				var app_img_intro_form_value = "";
//				var plat_groupId = '-1000';
//				var app_img_url_value = "";
//				var app_img_url_little_value = "";
//				var app_img_url_icon_value = "";
//				// **************************************************
//				var app_img_url_4_squares_value = "";
//				var app_img_url_6_squares_value = "";
//				// **************************************************
//				if (id != "-1") {
//					app_img_id_value = Ext.getCmp("app_img_id").getValue();
//					app_img_url_value = Ext.getCmp("app_img_url").getValue();
//					app_img_url_little_value = Ext.getCmp("app_img_url_little")
//							.getValue();
//					app_img_url_icon_value = Ext.getCmp("app_img_url_icon")
//							.getValue();
//					// ********************************************************************
//					app_img_url_4_squares_value = Ext
//							.getCmp("app_img_url_4_squares").getValue();
//					app_img_url_6_squares_value = Ext
//							.getCmp("app_img_url_6_squares").getValue();
//					// ********************************************************************
//					app_img_isurlused_value = Ext.getCmp("app_img_isurlused")
//							.getValue();
//
//					if (app_img_url_value != ""
//							|| app_img_url_little_value != ""
//							|| app_img_url_icon_value != ""
//							|| app_img_url_4_squares_value != ""
//							|| app_img_url_6_squares_value != "") {
//						app_img_active_time_value = app_img_active_time
//								.getValue();
//						if (app_img_active_time_value == null
//								|| app_img_active_time_value == '') {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片生效不能为空",
//										width : 130,
//										buttons : Ext.Msg.OK
//									});
//
//							return null;
//						}
//
//						app_img_deactive_time_value = app_img_deactive_time
//								.getValue();
//						if (app_img_deactive_time_value == null
//								|| app_img_deactive_time_value == '') {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片失效不能为空",
//										width : 130,
//										buttons : Ext.Msg.OK
//									});
//
//							return null;
//						}
//
//						if (app_img_active_time_value > app_img_deactive_time_value) {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片失效时间不能比生效时间早",
//										width : 180,
//										buttons : Ext.Msg.OK
//									});
//
//							return null;
//						}
//
//						app_img_intro_form_value = app_img_intro_form
//								.getValue();
//
//						plat_groupId = app_img_platgroup_form.getValue();
//						if (plat_groupId == null || plat_groupId == '') {
//							Ext.MessageBox.show({
//										title : "提示",
//										msg : "图片平台不能为空",
//										width : 130,
//										buttons : Ext.Msg.OK
//									});
//
//							return null;
//						}
//
//					}
//				}
//				Ext.MessageBox.wait('处理中，请稍后...');
//				Ext.Ajax.request({
//							url : 'apps/save_update_cont_app.do',
//							params : {
//								id : id,
//								appName : appName,
//								appAlias : appAlias,
//								appStatus : appStatus,
//								appProvider : appProvider,
//								activeTime : activeTime,
//								deactiveTime : deactiveTime,
//								appCapacity : appCapacity,
//								appVersion : appVersion,
//								appVersionCode : appVersionCode,
//								tags : cont_appTags,
//								superscript_id : superscript_id,
//								appStaff : appStaff,
//								addTime : addTime,
//								appPackageName : appPackageName,
//								appMd5 : appMd5,
//								appDownloadUrl : appDownloadUrl,
//								appDiscription : appDiscription,
//								base_locked : base_locked,
//								img_locked : img_locked,
//								app_img_active_time : app_img_active_time_value,
//								app_img_deactive_time : app_img_deactive_time_value,
//								app_img_intro_form : app_img_intro_form_value,
//								app_img_id : app_img_id_value,
//								app_img_url : app_img_url_value,
//								app_img_url_little : app_img_url_little_value,
//								app_img_url_icon : app_img_url_icon_value,
//								app_img_url_4_squares : app_img_url_4_squares_value,
//								app_img_url_6_squares : app_img_url_6_squares_value,
//								plat_groupId : plat_groupId,
//								app_img_isurlused : app_img_isurlused_value
//							},
//							success : function(response) {
//								Ext.MessageBox.hide();
//								var result = Ext.decode(response.responseText);
//								cont_id_form.setValue(result.id);
//								contAppFormPanelIsenable(true);
//								if (result.success) {
//									Ext.MessageBox.show({
//												title : "提示",
//												msg : "保存成功",
//												width : 110,
//												buttons : Ext.Msg.OK
//											});
//									contAppPanel_grid_store.load();
//									app_download_url_grid_store.proxy.extraParams = {c_id : result.id };
//								} else {
//									Ext.MessageBox.show({
//												title : "错误提示",
//												msg : result.info,
//												width : 110,
//												buttons : Ext.Msg.OK,
//												icon : Ext.Msg.ERROR
//											});
//								}
//							},
//							failure : function() {
//								Ext.MessageBox.hide();
//								Ext.MessageBox.show({
//											title : "提示",
//											msg : "保存失败",
//											width : 110,
//											buttons : Ext.Msg.OK,
//											icon : Ext.Msg.ERROR
//										});
//							}
//						});
//			}
//		});
//		contAppPanel_form = new Ext.FormPanel({
//					title : '应用软件详情',
//					frame : true,
//					collapsible : true,
//					titleCollapse : 'true',
//					animate : true,
//					border : true,
//					fieldDefaults : {
//						labelAlign : 'right'
//					},
//					buttonAlign : 'center',
//					width : 600,
//					autoHeight : true,
//					autoscoll : true,
//					reader : new Ext.data.JsonReader({
//								model : 'Cont.ContAppstore',
//								success : 'success',
//								root : 'siteStructure_data'
//							}),
//					items : [contApkSnapshotImgPanel_form,
//							contAppBasePanel_form, app_download_url_grid,
//							contAppImgPanel_form],
//					buttons : [button_cont_app_save, {
//								disabled : true,
//								baseCls : 'x-plain',
//								height : 100
//							}/* ,catalog_img_button */]
//				});
//
//		function getContAppDetail(contID, isEnable) {
//
//			contAppPanel_form.form.load({
//						url : 'apps/query_cont_app_detail.do?id=' + contID,
//						method : 'GET',
//						waitMsg : '正在载入数据...',
//						success : function(form, action) {
//							app_download_url_grid_store.proxy.extraParams = {
//								c_id : contID
//							};
//							app_download_url_grid_store.load({
//										params : {
//											start : 0,
//											limit : pgSize
//										}
//									}); // 加载数据
//
//							contAppFormPanelIsenable(isEnable);
//						},
//						failure : function(form, action) {
//						}
//					});
//		}
//
//		var contAppPanel_grid_store = new Ext.data.JsonStore({
//					pageSize : pgSize,
//					fields : ['contID', 'contName', 'contType', 'contStatus',
//							'contDescription', 'modify_time',
//							'cont_superscript'],
//					proxy : {
//						type : 'ajax',
//						url : 'apps/query_cont_app.do?auth=1',
//						reader : {
//							totalProperty : "results",
//							root : "datastr",
//							idProperty : 'contID'
//						},
//						extraParams : {
//							contType : contType,
//							contStatus : contStatus,
//							contName : contName
//						},
//						actionMethods : {
//							read : 'POST'
//						}
//					}
//				});
//		function contAppFormPanelIsenable(isEditable) {
//			var id = cont_id_form.getValue();
//			var isNewRecord = id == "-1";
//			var isDisabled = !isEditable;
//			var isImgDisabled = isDisabled || isNewRecord;
//
//			app_img_big.getEl().dom.src = 'app/resources/images/empty.JPG';
//			app_img_little.getEl().dom.src = 'app/resources/images/empty.JPG';
//			app_img_icon.getEl().dom.src = 'app/resources/images/empty.JPG';
//			app_img_4_squares.getEl().dom.src = 'app/resources/images/empty.JPG';
//			app_img_6_squares.getEl().dom.src = 'app/resources/images/empty.JPG';
//			// 推荐位使用的图类型
//			for (var ii = 0; ii < 5; ii++) {
//				var radio = contAppImgPanel_form.form
//						.findField("app_img_isurlused_rb" + ii);
//				radio.setValue(false);
//				radio.setDisabled(isImgDisabled);
//			}
//			Ext.getCmp("app_big_img_filename").setText('');
//			Ext.getCmp("app_little_img_filename").setText('');
//			Ext.getCmp("app_icon_img_filename").setText('');
//			Ext.getCmp("app_4_squares_img_filename").setText('');
//			Ext.getCmp("app_6_squares_img_filename").setText('');
//			snapshotimg_locked_checkbox.setValue(false);
//
//			if (!isNewRecord)// 默认显示小图片框
//			{
//				app_snapshotimg_combo.setValue("");
//				app_snapshotimg_combo.store.load({
//							params : {
//								date : Ext.util.Format.date(new Date(),
//										"YmdHis"),
//								id : id
//							}
//						});
//
//				var show_url = Ext.getCmp("app_img_url_show").getValue();
//				if (show_url != null && show_url != '') {
//					app_img_big.getEl().dom.src = 'app/resources/images/loading.JPG';
//					app_img_big.getEl().dom.src = show_url;
//				}
//				show_url = Ext.getCmp("app_img_url_little_show").getValue();
//				if (show_url != null && show_url != '') {
//					app_img_little.getEl().dom.src = 'app/resources/images/loading.JPG';
//					app_img_little.getEl().dom.src = show_url;
//				}
//				show_url = Ext.getCmp("app_img_url_icon_show").getValue();
//				if (show_url != null && show_url != '') {
//					app_img_icon.getEl().dom.src = 'app/resources/images/loading.JPG';
//					app_img_icon.getEl().dom.src = show_url;
//				}
//
//				show_url = Ext.getCmp("app_img_url_4_squares_show").getValue();
//				if (show_url != null && show_url != '') {
//					app_img_4_squares.getEl().dom.src = 'app/resources/images/loading.JPG';
//					app_img_4_squares.getEl().dom.src = show_url;
//				}
//				show_url = Ext.getCmp("app_img_url_6_squares_show").getValue();
//				if (show_url != null && show_url != '') {
//					app_img_6_squares.getEl().dom.src = 'app/resources/images/loading.JPG';
//					app_img_6_squares.getEl().dom.src = show_url;
//				}
//				show_url = Ext.getCmp("app_img_isurlused").getValue();
//				var obj_radio = contAppImgPanel_form.form
//						.findField("app_img_isurlused_rb" + show_url);
//				if (obj_radio != null) {
//					obj_radio.setValue(true);
//				}
//			}
//			if (isEditable) {
//				if (isNewRecord) {
//					app_snapshotimg_combo.store.removeAll();
//
//					// 初始化基本信息录入选项
//					cont_app_status_form.setValue("11");
//					contAppActiveTime.setValue(Ext.util.Format.date(new Date(),
//							"Y-m-d H:i:s"));
//					contAppDeactiveTime.setValue("2050-01-01 00:00:00");
//				} else {
//					// 初始化图片录入选项
//					if (null == app_img_active_time.getValue()) {
//						app_img_active_time.setValue(Ext.util.Format.date(
//								new Date(), "Y-m-d H:i:s"));
//					}
//					if (null == app_img_deactive_time.getValue()) {
//						app_img_deactive_time.setValue("2050-01-01 00:00:00");
//					}
//					if (null == app_img_platgroup_form.getValue()
//							|| '' == app_img_platgroup_form.getValue()) {
//						app_img_platgroup_form.setValue("1");
//					}
//				}
//			}
//			cont_id_form.setDisabled(isDisabled);
//			cont_app_name_form.setDisabled(isDisabled);
//			cont_app_alias_form.setDisabled(isDisabled);
//			cont_app_status_form.setDisabled(isDisabled);
//			cont_app_provider_form.setDisabled(isDisabled);
//			contAppActiveTime.setDisabled(isDisabled);
//			contAppDeactiveTime.setDisabled(isDisabled);
//			cont_app_capacity_form.setDisabled(isDisabled);
//			base_locked_checkbox.setDisabled(isDisabled);
//			cont_app_version_form.setDisabled(isDisabled);
//			cont_app_version_code_form.setDisabled(isDisabled);
//			cont_app_tags_form.setDisabled(isDisabled);
//			shortcut_contid_form.setDisabled(isDisabled);
//			button_select_shortcut_contid.setDisabled(isDisabled);
//			cont_app_staff_form.setDisabled(isDisabled);
//			cont_add_time_form.setDisabled(isDisabled);
//			cont_app_package_name_form.setDisabled(isDisabled);
//			cont_app_md5_form.setDisabled(isDisabled);
//			cont_app_download_url_form.setDisabled(isDisabled);
//			cont_app_discription_form.setDisabled(isDisabled);
//			button_cont_app_save.setDisabled(isDisabled);
//
//			// 图片
//			app_img_platgroup_form.setDisabled(isImgDisabled);
//			img_locked_checkbox.setDisabled(isImgDisabled);
//			app_big_img_fileUploadField.setDisabled(isImgDisabled);
//			app_little_img_fileUploadField.setDisabled(isImgDisabled);
//			app_icon_img_fileUploadField.setDisabled(isImgDisabled);
//			app_4_squares_img_fileUploadField.setDisabled(isImgDisabled);
//			app_6_squares_img_fileUploadField.setDisabled(isImgDisabled);
//			app_apk_fileUploadField.setDisabled(isImgDisabled);
//			app_img_intro_form.setDisabled(isImgDisabled);
//			app_img_active_time.setDisabled(isImgDisabled);
//			app_img_deactive_time.setDisabled(isImgDisabled);
//			snapshotimg_locked_checkbox.setDisabled(isImgDisabled);
//			app_big_img_upload_btn.setDisabled(isImgDisabled);
//			app_little_img_upload_btn.setDisabled(isImgDisabled);
//			app_icon_img_upload_btn.setDisabled(isImgDisabled);
//			app_4_squares_img_upload_btn.setDisabled(isImgDisabled);
//			app_6_squares_img_upload_btn.setDisabled(isImgDisabled);
//			// Ext.getCmp("app_apk_upload_btn").setDisabled(isImgDisabled);
//			app_snapshotimg_upload_btn.setDisabled(isImgDisabled);
//			app_img_locked_btn.setDisabled(isImgDisabled);
//			app_snapshotimg_delete_btn.setDisabled(isImgDisabled);
//			app_img_isurlused_radiogroup.setDisabled(isImgDisabled);
//			app_snapshotimg_fileUploadField.setDisabled(isImgDisabled);
//		}
//
//		var contAppPanel_grid_new = function() {
//			contAppPanel_form.getForm().reset();
//			cont_id_form.setValue("-1");
//			Ext.getCmp("app_img_id").setValue("-1");
//			contAppFormPanelIsenable(true);
//
//			app_download_url_grid_store.load({ // =======翻页时分页参数
//				params : {
//					start : 0,
//					limit : pgSize,
//					c_id : 0
//				}
//			});
//		}
//
//		var contAppPanel_grid_modify = function() {
//			Ext.getCmp("app_img_id").setValue("-1");
//			Ext.getCmp("app_snapshotimg_id").setValue("");
//			Ext.getCmp("app_snapshotimg_url_show").setValue("");
//			var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为 Record 数组
//			if (rows.length == 1) {
//				contAppPanel_form.setTitle('应用软件详情【' + rows[0].get('contID')+","+rows[0].get('contName') +"】");
//
//				var contID = rows[0].get('contID');
//				contID_tmp = contID;
//				getContAppDetail(contID, true);
//			} else {
//				Ext.MessageBox.show({ // 弹出对话框警告
//					title : "提示",
//					msg : "请选择一项，再进行修改",
//					width : 180,
//					buttons : Ext.Msg.OK
//				});
//			}
//			// ********************************************************************************************************
//			// var selectedObj =
//			// contAppPanel_grid.getSelectionModel().getSelected();
//			// Ext.getCmp("app_img_id").setValue("-1");
//			// if(selectedObj == undefined) {
//			// Ext.MessageBox.show({
//			// title:"提示",
//			// msg:"请选择一项，然后进行修改",
//			// width:180,
//			// buttons:Ext.Msg.OK
//			// });
//			// } else {
//			// var contID = selectedObj.data['contID'];
//			// getContAppDetail(contID, true);
//			// }
//		}
//
//		var contAppPanel_grid_delete = function() {
//			// ********************************************************************************************************
//			{
//				var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为  Record  数组
//				if (rows.length == 1) {
//					var contID = rows[0].get('contID');
//					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//						if (btn == 'yes') {
//							Ext.Ajax.request({
//										url : 'apps/delete_cont_app.do?id=' + contID,
//										waitMsg : '正在提交,请稍等',
//										success : function(response) {
//											var result = Ext
//													.decode(response.responseText);
//											// alert("result.success="+result.success);
//											if (!result.success) {
//												Ext.Msg.show({
//															title : '错误提示',
//															msg : result.info,
//															buttons : Ext.Msg.OK,
//															icon : Ext.Msg.ERROR
//														});
//											} else {
//												contAppFormPanelIsenable(false);
//												Utils.checkStorePageNo(contAppPanel_grid_store, 1);
//												contAppPanel_grid_store.load();
//												app_download_url_grid_store
//														.load({ // =======翻页时分页参数
//															params : {
//																start : 0,
//																limit : pgSize,
//																c_id : ''
//															}
//														});
//											}
//										},
//										failure : function(response) {
//											Ext.Msg.show({
//														title : '错误提示',
//														msg : '删除时发生错误!',
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									});
//						}
//					});
//				} else {
//					Ext.MessageBox.show({
//								title : "提示",
//								msg : "请先选择删除的一项",
//								width : 160,
//								buttons : Ext.Msg.OK
//							});
//				}
//			}
//			// ********************************************************************************************************
//
//			// var selectedObj =
//			// contAppPanel_grid.getSelectionModel().getSelected();
//			// if(selectedObj == undefined) {
//			// Ext.MessageBox.show({
//			// title:"提示",
//			// msg:"请先选择删除的一项",
//			// width:160,
//			// buttons:Ext.Msg.OK
//			// });
//			// } else {
//			// var contID = selectedObj.data['contID'];
//			//			
//			// Ext.MessageBox.confirm('提示框', '您确定要进行该操作？',function(btn){
//			// if(btn=='yes'){
//			// Ext.Ajax.request({
//			// url:'delete_cont_app.do?id=' + contID,
//			// success:function() {
//			// contAppFormPanelIsenable(false);
//			// contAppPanel_grid_store.reload();
//			// },
//			// failure:function() {
//			// Ext.Msg.show({
//			// title:'错误提示',
//			// msg:'删除时发生错误!',
//			// buttons:Ext.Msg.OK,
//			// icon:Ext.Msg.ERROR
//			// });
//			// }
//			// });
//			// }
//			// });
//			// }
//
//		}
//
//		var contAppPanel_grid_detail = function() {
//			// ********************************************************************************************************
//			{
//				var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为
//																				// Record
//																				// 数组
//				if (rows.length == 1) {
//					var contID = rows[0].get('contID');
//					contID_tmp = contID;
//					getContAppDetail(contID, false);
//				} else {
//					Ext.MessageBox.show({ // 弹出对话框警告
//						title : "提示",
//						msg : "请选择一项，在查看详情",
//						width : 180,
//						buttons : Ext.Msg.OK
//					});
//				}
//			}
//			// ********************************************************************************************************
//
//			// var selectedObj =
//			// contAppPanel_grid.getSelectionModel().getSelected();
//			// if(selectedObj == undefined) {
//			// Ext.MessageBox.show({
//			// title:"提示",
//			// msg:"请选择一项，在查看详情",
//			// width:160,
//			// buttons:Ext.Msg.OK
//			// });
//			// } else {
//			// var contID = selectedObj.data['contID'];
//			// getContAppDetail(contID, false);
//			// }
//		}
//		// *********************************************************************
//		var contVideoPanel_grid_setsuperscript = function() {
//			var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为
//																			// Record
//																			// 数组
//			if (rows.length == 0) {
//				Ext.MessageBox.alert('警告', '最少选择一条记录，进行设置!');
//			} else {
//				// Ext.MessageBox.confirm('提示框', '您确定要进行角标设置操作？',function(btn){
//				// if(btn=='yes'){
//				var ids = "";
//				if (rows) {
//					for (var i = 0; i < rows.length; i++) {
//						ids = ids + rows[i].get('contID') + ',';
//					}
//					if (ids.length > 0) {
//						ids = ids.substring(0, ids.length - 1);
//					}
//					WinUtil_setSuperScript
//							.showSuperscriptWindows('角标内容选择', ids);
//				}
//				// }
//				// });
//			} // 弹出对话框警告
//		}
//		var contVideoPanel_grid_deletesuperscript = function() {
//			{
//				var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为
//																				// Record
//																				// 数组
//				if (rows.length == 0) {
//					Ext.MessageBox.alert('警告', '最少选择一条记录，进行设置!');
//				} else {
//					var ids = "";
//					for (var i = 0; i < rows.length; i++) {
//						ids = ids + rows[i].get('contID') + ',';
//					}
//					if (ids.length > 0) {
//						ids = ids.substring(0, ids.length - 1);
//					}
//					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//						if (btn == 'yes') {
//							Ext.Ajax.request({
//										url : 'contvideo/delete_cont_superscript.do?ids='
//												+ ids,
//										waitMsg : '正在提交,请稍等',
//										success : function(response) {
//											contAppPanel_grid_store.load();
//											var result = Ext
//													.decode(response.responseText);
//											if (result.success) {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "删除角标成功",
//															width : 110,
//															buttons : Ext.Msg.OK,
//															icon : Ext.Msg.ERROR
//														});
//
//											} else {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "删除角标失败!",
//															width : 110,
//															buttons : Ext.Msg.OK
//														});
//											}
//										},
//										failure : function() {
//											Ext.Msg.show({
//														title : '错误提示',
//														msg : '删除角标失败!',
//														width : 110,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									});
//						}
//					});
//				}
//			}
//		}
//		var recommend_sync_add = function() {
//			{
//				var rows = contAppPanel_grid.getSelectionModel().getSelection();// 返回值为
//																				// Record
//																				// 数组
//				if (rows.length == 0) {
//					Ext.MessageBox.alert('警告', '最少选择一条记录!');
//				} else {
//					var ids = "";
//					for (var i = 0; i < rows.length; i++) {
//						ids = ids + rows[i].get('contID') + ',';
//					}
//					if (ids.length > 0) {
//						ids = ids.substring(0, ids.length - 1);
//					}
//					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//						if (btn == 'yes') {
//							Ext.Msg.wait('处理中');
//							Ext.Ajax.request({
//										url : 'recommend_syncs/add.do?ids='
//												+ ids,
//										waitMsg : '正在提交,请稍等',
//										success : function(response) {
//											recommend_sync_grid_store.load();
//											var result = Ext
//													.decode(response.responseText);
//											if (result.success) {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "同步完成",
//															width : 110,
//															buttons : Ext.Msg.OK,
//															icon : Ext.Msg.ERROR
//														});
//
//											} else {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "同步失败!",
//															width : 110,
//															buttons : Ext.Msg.OK
//														});
//											}
//										},
//										failure : function() {
//											Ext.Msg.show({
//														title : '错误提示',
//														msg : '同步失败!',
//														width : 110,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									});
//						}
//					});
//				}
//			}
//		}
//		var recommend_sync_delete = function() {
//			{
//				var rows = recommend_sync_grid.getSelectionModel()
//						.getSelection();// 返回值为 Record 数组
//				if (rows.length == 0) {
//					Ext.MessageBox.alert('警告', '最少选择一条记录!');
//				} else {
//					var ids = "";
//					for (var i = 0; i < rows.length; i++) {
//						ids = ids + rows[i].get('c_id') + ',';
//					}
//					if (ids.length > 0) {
//						ids = ids.substring(0, ids.length - 1);
//					}
//					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
//						if (btn == 'yes') {
//							Ext.Msg.wait('处理中');
//							Ext.Ajax.request({
//										url : 'recommend_syncs/delete.do?ids='
//												+ ids,
//										waitMsg : '正在提交,请稍等',
//										success : function(response) {
//											recommend_sync_grid_store.load();
//											var result = Ext
//													.decode(response.responseText);
//											if (result.success) {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "删除同步完成",
//															width : 110,
//															buttons : Ext.Msg.OK,
//															icon : Ext.Msg.ERROR
//														});
//
//											} else {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "删除同步失败!",
//															width : 110,
//															buttons : Ext.Msg.OK
//														});
//											}
//										},
//										failure : function() {
//											Ext.Msg.show({
//														title : '错误提示',
//														msg : '删除同步失败!',
//														width : 110,
//														buttons : Ext.Msg.OK,
//														icon : Ext.Msg.ERROR
//													});
//										}
//									});
//						}
//					});
//				}
//			}
//		}
//		var superscriptUtil = {
//
//			showSuperscriptWindows : function(title, ids) {
//				var superscriptPgSize = 100; // 分页数
//
//				var superscript_cont_s_grid_store = new Ext.data.Store({
//							pageSize : pgSize,
//							fields : ['mar_s_contId', 'mar_s_contName',
//									'mar_s_contType', 'mar_s_contProvider',
//									'mar_s_contIntro'],
//							proxy : {
//								type : 'ajax',
//								url : 'query_cont_by_role.do',
//								// url: 'query_cont_by_role_for_mar.do?auth=1'
//								reader : {
//									totalProperty : "results",
//									root : "datastr",
//									idProperty : 'mar_s_contId'
//								}
//							}
//						});
//
//				superscript_cont_s_grid_store.load({ // =======翻页时分页参数
//					params : {
//						start : 0,
//						limit : superscriptPgSize
//					}
//				});
//
//				// var superscript_cont_s_grid_sm = new
//				// Ext.grid.CheckboxSelectionModel();
//
//				var superscript_cont_grid = new Ext.grid.GridPanel({
//							store : superscript_cont_s_grid_store,
//							autoHeight : false,
//
//							columns : [{
//										header : "ID",
//										width : 90,
//										sortable : true,
//										dataIndex : 'mar_s_contId'
//									}, {
//										header : '名称',
//										width : 130,
//										sortable : true,
//										dataIndex : 'mar_s_contName'
//									}, {
//										header : "类型",
//										width : 110,
//										sortable : true,
//										dataIndex : 'mar_s_contType'
//									}, {
//										header : "提供商",
//										width : 110,
//										sortable : true,
//										dataIndex : 'mar_s_contProvider'
//									}, {
//										header : "描述",
//										width : 180,
//										sortable : true,
//										dataIndex : 'mar_s_contIntro'
//									}],
//							selModel : {
//								selType : 'checkboxmodel'
//							},
//							width : 720,
//							height : 450,
//							frame : true,
//							loadMask : {
//								msg : '数据加载中...'
//							},
//							title : '角标列表',
//							iconCls : 'icon-grid',
//							bbar : new Ext.PagingToolbar({
//										pageSize : superscriptPgSize,
//										width : 720,
//										store : superscript_cont_s_grid_store,
//										displayInfo : true,
//										displayMsg : '第 {0}-- {1}条    共 {2}条',
//										emptyMsg : '没有记录'
//									})
//						});
//
//				var setsuperscriptPanel = new Ext.FormPanel({
//							bodyStyle : 'padding:0 0 0 5',
//							width : 730,
//							// layout:'form',
//							// height:365,
//							items : superscript_cont_grid
//						});
//				var setsuperscriptwinform = new Ext.FormPanel({
//					border : true,
//					frame : true,
//					labelAlign : "right",
//					buttonAlign : 'right',
//					layout : 'column',
//					width : 780,
//					height : 540,
//					items : [{
//								width : 750,
//								height : 450,
//								// layout:'form',
//								baseCls : 'x-plain',
//								items : [setsuperscriptPanel]
//							}],
//					buttons : [{
//						xtype : 'button',
//						text : '确定',
//						handler : function() {
//							// Ext.Msg.alert("消息", "选中状态的id为:" + id.toString() +
//							// name.toString());
//							var rows = superscript_cont_grid
//									.getSelectionModel().getSelection();
//							var contId = '';
//							if (!(rows.length == 1)) {
//								Ext.MessageBox.confirm('警告',
//										'请选择一条记录!,不选、多选默认为空', function(btn) {
//											if (btn == 'yes') {
//												if (rows) {
//													submintsuperscript();
//												}
//											}
//										});
//							} else {
//								Ext.MessageBox.confirm('提示框', '您确定选择该条记录？',
//										function(btn) {
//											if (btn == 'yes') {
//												contId = rows[0]
//														.get('mar_s_contId');
//												submintsuperscript()
//											}
//										});
//							}
//							function submintsuperscript() {
//								Ext.Ajax.request({
//											url : 'contvideo/superscript_cont_video.do',
//											params : {
//												ids : ids,
//												contId : contId
//											},
//											success : function(response) {
//												var result = Ext
//														.decode(response.responseText);
//												if (result.success) {
//													Ext.MessageBox.show({
//																title : "提示",
//																msg : "设置角标成功",
//																width : 110,
//																buttons : Ext.Msg.OK
//															});
//													contAppPanel_grid_store
//															.load();
//												} else {
//													Ext.MessageBox.show({
//																title : "提示",
//																msg : "设置角标失败",
//																width : 110,
//																buttons : Ext.Msg.OK,
//																icon : Ext.Msg.ERROR
//															});
//												}
//											},
//											failure : function() {
//												Ext.MessageBox.show({
//															title : "提示",
//															msg : "设置角标失败",
//															width : 110,
//															buttons : Ext.Msg.OK,
//															icon : Ext.Msg.ERROR
//														});
//											}
//										});
//
//								setsuperscriptWindows.destroy();
//							}
//						}
//
//					}, {
//						xtype : 'button',
//						text : '取消',
//						handler : function() {
//							setsuperscriptWindows.destroy();
//						}
//					}]
//				});
//				var setsuperscriptWindows = new Ext.Window({
//							modal : true,
//							layout : 'fit',
//							title : '选择角标',
//							width : 780,
//							height : 540,
//							plain : true,
//							items : [setsuperscriptwinform]
//						});
//				setsuperscriptWindows.show();
//			}
//		}
//
//		// var contAppPanel_grid_sm = new Ext.grid.CheckboxSelectionModel();
//		var contAppPanel_grid = new Ext.grid.GridPanel({
//					frame : true,
//					loadMask : {
//						msg : '数据加载中...'
//					},
//					width : 440,
//					height : 365,
//					collapsible : true,
//					titleCollapse : 'true',
//					title : '应用资产列表',
//					iconCls : 'icon-grid',
//					viewConfig : {
//						forceFit : true
//					},
//					stripeRows : true,
//					enableHdMenu : false,
//					store : contAppPanel_grid_store,
//					autoHeight : false,
//					columns : [{
//								header : "ID",
//								width : 100,
//								sortable : true,
//								dataIndex : 'contID'
//							}, {
//								header : '名称',
//								width : 150,
//								sortable : true,
//								dataIndex : 'contName'
//							}, {
//								header : "状态",
//								width : 60,
//								sortable : true,
//								dataIndex : 'contStatus'
//							}, {
//								header : "类型",
//								width : 50,
//								sortable : true,
//								dataIndex : 'contType'
//							}, {
//								header : "修改时间",
//								width : 150,
//								sortable : true,
//								dataIndex : 'modify_time'
//							}, {
//								header : "角标",
//								width : 100,
//								sortable : true,
//								dataIndex : 'cont_superscript'
//							}, {
//								header : "描述",
//								width : 200,
//								sortable : true,
//								dataIndex : 'contDescription'
//							}],
//					selModel : {
//						selType : 'checkboxmodel',
//						mode : 'SIMPLE'
//					},
//					// sm: new Ext.grid.RowSelectionModel({
//					// singleSelect:true
//					// }),
//					tbar : [{
//								text : '新建',
//								handler : contAppPanel_grid_new,
//								scope : this
//							}, {
//								text : '修改',
//								handler : contAppPanel_grid_modify,
//								scope : this
//							}, {
//								text : '删除',
//								handler : contAppPanel_grid_delete,
//								scope : this
//							}, {
//								text : '详情',
//								handler : contAppPanel_grid_detail,
//								scope : this
//							}, {
//								text : '设置角标',
//								handler : contVideoPanel_grid_setsuperscript
//							}, {
//								text : '删除角标',
//								handler : contVideoPanel_grid_deletesuperscript
//							}, {
//								text : '同步',
//								handler : recommend_sync_add
//							}],
//					bbar : new Ext.PagingToolbar({
//								pageSize : pgSize,
//								width : 440,
//								store : contAppPanel_grid_store,
//								displayInfo : true,
//								displayMsg : '第 {0}-- {1}条    共 {2}条',
//								emptyMsg : '没有记录'
//							})
//				});
//
//		contAppPanel_grid_store.load({ // =======翻页时分页参数
//			params : {
//				start : 0,
//				limit : pgSize
//			}
//		});
//
//		var recommend_sync_grid_store = new Ext.data.JsonStore({
//					pageSize : pgSize,
//					fields : ['id', 'c_id', 'name', 'status', 'create_time',
//							'update_time', 'extra_params'],
//					proxy : {
//						type : 'ajax',
//						url : 'recommend_syncs/query.do?auth=1',
//						reader : {
//							totalProperty : "results",
//							root : "datastr",
//							idProperty : 'id'
//						},
//						actionMethods : {
//							read : 'POST'
//						}
//					}
//				});
//
//		var recommend_sync_grid = new Ext.grid.GridPanel({
//					frame : true,
//					loadMask : {
//						msg : '数据加载中...'
//					},
//					width : 440,
//					height : 365,
//					collapsible : true,
//					titleCollapse : 'true',
//					title : '已同步应用资产列表',
//					iconCls : 'icon-grid',
//					bodyStyle : 'width:100%',
//					viewConfig : {
//						forceFit : true
//					},
//					stripeRows : true,
//					enableHdMenu : false,
//					store : recommend_sync_grid_store,
//					autoHeight : false,
//					columns : [{
//								header : 'c_id',
//								width : 80,
//								sortable : true,
//								dataIndex : 'c_id'
//							}, {
//								header : "名称",
//								width : 50,
//								sortable : true,
//								dataIndex : 'name'
//							}, {
//								header : "状态",
//								width : 30,
//								sortable : true,
//								dataIndex : 'status'
//							}, {
//								header : "创建时间",
//								width : 50,
//								sortable : true,
//								dataIndex : 'create_time'
//							}, {
//								header : "修改时间",
//								width : 150,
//								sortable : true,
//								dataIndex : 'update_time'
//							}, {
//								header : "扩展参数",
//								width : 200,
//								sortable : true,
//								dataIndex : 'extra_params'
//							}],
//					selModel : {
//						selType : 'checkboxmodel'
//					},
//					// sm: new Ext.grid.RowSelectionModel({
//					// singleSelect:true
//					// }),
//					tbar : [{
//								text : '删除同步',
//								handler : recommend_sync_delete
//							}],
//					bbar : new Ext.PagingToolbar({
//								pageSize : pgSize,
//								width : 440,
//								store : recommend_sync_grid_store,
//								displayInfo : true,
//								displayMsg : '第 {0}-- {1}条    共 {2}条',
//								emptyMsg : '没有记录'
//							})
//				});
//
//		recommend_sync_grid_store.load({ // =======翻页时分页参数
//			params : {
//				start : 0,
//				limit : pgSize
//			}
//		});
//
//		/*-----定义组下拉列表，用以搜索组下的相应栏目-----*/
//		var contApp_provider_search_combo = new Ext.form.ComboBox({
//					fieldLabel : '*提供商1',
//					emptyText : '请选择提供商...',
//					baseCls : 'x-plain',
//					xtype : 'combo',
//					name : 'providerId',
//					hiddenName : 'providerId',
//					// width:160,
//					maxHeight : 180,
//					hideLabel : true,
//					allowBlank : false,
//					disabled : false,
//					displayField : 'providerName',
//					valueField : 'providerId',
//					editable : false,
//					// readOnly:true,
//					queryMode : 'local',
//					triggerAction : 'all',
//					store : appProviderJsonStore
//				});
//
//		contApp_provider_search_combo.on('select', function() {
//
//					if (contProviderId == '-1') {
//						search_button.disable();
//					} else {
//						search_button.enable();
//						contProviderId = this.getValue();
//					}
//
//				});
//
//		var contApp_status_combo = new Ext.form.ComboBox({
//					baseCls : 'x-plain',
//					xtype : 'combo',
//					maxHeight : 120,
//					hideLabel : true,
//					displayField : 's_name',
//					valueField : 's_id',
//					editable : false,
//					// readOnly:true,
//					queryMode : 'local',
//					triggerAction : 'all',
//					emptyText : '请选择状态',
//					store : cont_video_statusStore
//				});
//
//		contApp_status_combo.on('select', function() {
//					contStatus = this.getValue();
//				});
//
//		var getComboDisplay = function(combo) {
//			var value = combo.getValue();
//			var valueField = combo.valueField;
//			var record;
//			combo.getStore().each(function(r) {
//						if (r.data[valueField] == value) {
//							record = r;
//							return false;
//						}
//					});
//
//			return record ? record.get(combo.displayField) : "";
//		};
//
//		/*-----定义搜索方法,用于重置搜索条件，并重新加载树，以实现按条件搜索相应栏目-----*/
//		var search_method = function() {
//			// contName =
//			// Ext.String.trim(Ext.getCmp('cont_app_name').getValue());
//			contName = Ext.String.trim(cont_app_name_textfield.getValue());
//			var titlePrefix = "【"
//					+ getComboDisplay(contApp_provider_search_combo) + "】";
//			contAppPanel_grid.setTitle('应用资产列表' + titlePrefix);
//
//			contAppPanel_grid_store.proxy.extraParams = {
//				providerId : contProviderId,
//				contType : contType,
//				contStatus : contStatus,
//				contName : contName
//			};
//			contAppPanel_grid_store.load({ // =======翻页时分页参数
//				params : {
//					start : 0,
//					limit : pgSize
//				}
//			});
//		};
//
//		/*-----搜索按钮-----*/
//		var search_button = new Ext.Button({
//					// baseCls:'x-plain',
//					xtype : 'button',
//					// disabled:true,
//					text : '搜 索',
//					style : 'background-color:#99BBE8',
//					handler : search_method
//				});
//
//		/*-----定义监听方法，用于监听键盘上的F11键，当焦点在panel中时，按F11键，以此来适配panel的高度-----*/
//		var keyF11Event = function() {
//			if (F11_booble == false) {
//				creatingSiteStructurePanel
//						.setHeight(siteStructureHeightSize_F11);
//				F11_booble = true;
//			} else if (F11_booble == true) {
//				creatingSiteStructurePanel.setHeight(siteStructureHeightSize);
//				F11_booble = false;
//			}
//		}
//		var contAppstorePanel_leftlayout = new Ext.FormPanel({
//					// layout:'form',
//					border : false,
//					width : 440,
//					items : [contAppPanel_grid, recommend_sync_grid]
//				});
//
//		var contAppstorePanel_layout = new Ext.Panel({
//					layout : 'column',
//					items : [contAppstorePanel_leftlayout,
//							// {xtype:'panel',baseCls:'x-plain',width:18},
//							contAppPanel_form]
//				});
//
//		var cont_app_name_textfield = new Ext.form.TextField({
//					baseCls : 'x-plain',
//					xtype : 'textfield',
//					fieldLabel : '包名或软件名称(模糊)',
//					hideLabel : true,
//					emptyText : '包名或软件名称(模糊)',
//					// id:'cont_app_name',
//					name : 'cont_app_name'
//				});
//
//		/*-----总panel，集合所有的原件-----*/
//		var rootPanel = new Ext.Panel({
//					plain : true,
//					frame : true,
//					// layout:'column',
//					animate : true,
//					autoScroll : true,
//					// containerScroll:true,
//					collapsible : true,
//					height : siteStructureHeightSize,
//					// renderTo:'contAppstore-basic',
//					bodyStyle : 'padding:10 0 10 0',
//					defaults : {
//						baseCls : 'x-plain'
//					},
//					tbar : [{
//								xtype : 'panel',
//								baseCls : 'x-plain'
//							}, '按条件搜索软件资产：', {
//								xtype : 'panel',
//								baseCls : 'x-plain'
//							}, contApp_provider_search_combo, {
//								xtype : 'panel',
//								baseCls : 'x-plain'
//							}, contApp_status_combo, {
//								xtype : 'panel',
//								baseCls : 'x-plain'
//							}, cont_app_name_textfield, {
//								xtype : 'panel',
//								baseCls : 'x-plain'
//							}, search_button],
//					items : [contAppstorePanel_layout],
//					keys : [{
//								key : [122],
//								fn : keyF11Event,
//								scope : this
//							}]
//				});
//
//		this.items = [rootPanel];
//		this.callParent();
//	}
//});
