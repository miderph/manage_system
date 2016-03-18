Ext.define('app.view.module.contsales.contSalesSnapshotsPanel', {
	extend : 'Ext.container.Container',
	alias : 'widget.contsalessnapshotspanel',
	initComponent : function() {
		var app_snapshotimg_form;
		var c_id = '-1';

		// 应用截图开始
		var app_img_window = new Ext.Window({
			title : '图片预览',
			resizable : false,
			width : 900,
			height : 500,
			autoScroll : true,
			modal : true,
			closable : true,
			closeAction : 'hide',
			items : [{
				baseCls : 'x-plain',
				items : [{
					xtype : 'box',
					id : 'app_img_window',
					border : false,
					fieldLabel : "预览图片",
					autoEl : {
						height : 430,
						tag : 'img',
						src : Ext.BLANK_IMAGE_URL
					},
					listeners : {
						loadexception : function() {
							Ext.getCmp("app_img_window").getEl().dom.src = 'app/resources/images/empty.JPG';
						}
					}
				}]
			}],
			buttons : [{
				text : '取消',
				handler : function() {
					app_img_window.hide();
					Ext.getCmp("app_img_window").getEl().dom.src = Ext.BLANK_IMAGE_URL;
				}
			}]
		});
		var snapshotimg_locked_checkbox = new Ext.form.Checkbox({
			// id:'base_locked',
			name : 'snapshotimg_locked',
			// width:160,//宽度
			// height:compHeight,
			labelWidth:120,
			align : 'left',
			fieldLabel : '所有截图锁定',
			checked : false,
			disabled : false
		});
		/*-----定义截图下拉列表-----*/
		var app_snapshotimg_combo = Ext.create('Ext.form.field.ComboBox', {
			fieldLabel : '已上传截图',
			// height:20,
			labelWidth : 80,
			width : 360,
			hideLabel : false,
			labelAlign : 'right',
			displayField : 'app_img_url',
			valueField : 'app_img_id',
			disabled : false,
			editable : false,
			// readOnly:true,
			queryMode : 'local',
			triggerAction : 'all',
			emptyText : '请选择一个截图预览',

			store : new Ext.data.JsonStore({
				fields : ['app_img_id', 'app_img_url', 'app_img_url_show', 'snapshotimg_locked'],
				autoLoad : false,
				proxy : {
					type : 'ajax',
					url : "apps/query_app_snapshotimg.do",
					reader : {
						root : 'datastr',
						idProperty : 'app_img_id'
					},
					params : {
						id : c_id
					}
				},

				listeners : {
					select : {
						
						fn : function(combo, value, opts) {
							alert(value.data.app_img_id);
							alert(value.data.app_img_url_show);
						}
					},
					fn : function(combo, value, opts) {
						alert(value.data.app_img_id);
						alert(value.data.app_img_url_show);
					},
					load : function(oStore, ayRecords, successful, oOptions) {
						// var records = store.getRange(0, 1);
						// alert('loaded successfully');
						for (var ii = 0; ii < 5 && ii < ayRecords.length; ii++) {
							var imgObj = null;
							switch (ii) {
							case 0 :
								imgObj = app_snapshotimg0;
								break;
							case 1 :
								imgObj = app_snapshotimg1;
								break;
							case 2 :
								imgObj = app_snapshotimg2;
								break;
							case 3 :
								imgObj = app_snapshotimg3;
								break;
							case 4 :
								imgObj = app_snapshotimg4;
								break;
							}
							imgObj.getEl().dom.src = 'app/resources/images/loading.JPG';
							var record = ayRecords[ii];
							imgObj.getEl().dom.src = record
							.get('app_img_url_show');
							// alert(record.get('app_img_url_show'));
						}
						for (var ii = ayRecords.length; ii < 5; ii++) {
							var imgObj = null;
							switch (ii) {
							case 0 :
								imgObj = app_snapshotimg0;
								break;
							case 1 :
								imgObj = app_snapshotimg1;
								break;
							case 2 :
								imgObj = app_snapshotimg2;
								break;
							case 3 :
								imgObj = app_snapshotimg3;
								break;
							case 4 :
								imgObj = app_snapshotimg4;
								break;
							}
							imgObj.getEl().dom.src = 'app/resources/images/empty.JPG';
						}
						for (var ii = 0; ii < 5 && ii < ayRecords.length; ii++) {
							var record = ayRecords[ii];
							snapshotimg_locked_checkbox.setValue(record
									.get('snapshotimg_locked'));
						}
					}
				}
			})
		});

		app_snapshotimg_combo.on('select', function(combo, records, eOpts) {
			Ext.getCmp("app_snapshotimg_id").setValue(this.getValue());
			Ext.getCmp("app_snapshotimg_url_show")
			.setValue(records.data.app_img_url_show);
		});

		var app_snapshotimg_fileUploadField = new Ext.form.FileUploadField({
			xtype : 'fileuploadfield',
			height : 20,
			width : 500,
			allowBlank : true,
			disabled : false,
			blankText : '图片地址不能为空，请选择一张图片',
			emptyText : '请选择一张本地图片',
			regex : /^.*(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/,
			regexText : '请选择图片文件',
			fieldLabel : '上传新截图',
			name : 'snapshot',
			buttonText : '',
			buttonConfig : {
				iconCls : 'upload-icon'
			},
			listeners: {
				change: function(self, v) {
					app_snapshotimg_upload_btn.setDisabled(false);
				}
			}
		});

		var app_snapshotimg_delete_btn = new Ext.Button({
			xtype : 'button',
			text : '删除截图',
			// width: 120,
			// height: 30,
			name : 'app_snapshotimg_delete_btn',
			handler : function() {
				var id = Ext.getCmp("app_snapshotimg_id").getValue();

				if (id == null || id == '') {
					Ext.MessageBox.show({
						title : "提示",
						msg : "请先选择一个截图",
						width : 110,
						buttons : Ext.Msg.OK
					});
				} else {
					Ext.MessageBox.confirm('提示框', '您确定要进行该操作？', function(btn) {
						if (btn == 'yes') {
							Ext.Ajax.request({
								url : 'img/img_delete.do?tag=delete&obj_id='
									+ id
									+ '&date='
									+ Ext.util.Format.date(
											new Date(), "YmdHis"),
											waitMsg : '正在提交,请稍等',
											success : function(response) {
												app_snapshotimg_combo.setValue("");
												app_snapshotimg_combo.store
												.reload();// 刷新
												Ext.MessageBox.show({
													title : "提示",
													msg : "删除成功",
													width : 110,
													buttons : Ext.Msg.OK
												});
											},
											failure : function() {
												Ext.MessageBox.show({
													title : "提示",
													msg : "删除失败",
													width : 110,
													buttons : Ext.Msg.OK
												});
											}
							});
						}
					});
				}
			}
		});

		var app_img_locked_btn = new Ext.Button({
			text : '保存锁定状态',
			disabled : false,
			name : 'app_img_locked_btn',
			handler : function() {
				var targetId = c_id;
				var useType = 2;
				var locked = snapshotimg_locked_checkbox.getValue() == true ? 1 : 0;
				Ext.Ajax.request({
					url : 'img/img_updatelocked.do?targetId=' + targetId
					+ '&useType=' + useType + '&locked='
					+ locked,
					waitMsg : '正在提交,请稍等',
					success : function(response) {
						var result = Ext.decode(response.responseText);
						Ext.Msg.show({
							title : '温馨提示',
							msg : result.message,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.OK
						});
					},
					failure : function(response) {
						Ext.Msg.show({
							title : '错误提示',
							msg : '操作时发生错误!',
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.ERROR
						});
					}
				});

			}
		});
		var app_snapshotimg_upload_btn = new Ext.Button({
			text : '上传',
			disabled : true,
			name : 'app_snapshotimg_upload_btn',
			handler : function() {
				if (app_snapshotimg_form.getForm().isValid()) {
					var id = c_id;
					var locked = snapshotimg_locked_checkbox.getValue() == true
					? 1
							: 0;
					app_snapshotimg_form.getForm().submit({
						url : 'img/img_file_upload.do?tag=snapshot&obj_id='
							+ id
							+ '&date='
							+ Ext.util.Format.date(new Date(),
							"YmdHis")
							+ '&contTypeId=7&locked=' + locked,
							waitMsg : '正在上传...',
							success : function(form, action) {
								app_snapshotimg_combo.store.load({
									params : {
										id : id
									}
								});// 刷新
								Ext.MessageBox.show({
									title : "提示",
									msg : "图片上传成功",
									width : 110,
									buttons : Ext.Msg.OK
								});

								var result = Ext
								.decode(action.response.responseText);
								// Ext.getCmp("app_snapshotimg_url").setValue(result.img_url);
								// Ext.getCmp("app_snapshotimg_url_show").setValue(result.img_url_show);
								app_snapshotimg_upload_btn.setDisabled(true);
							},
							failure : function(form, action) {
								Ext.MessageBox.show({
									title : "提示",
									msg : "图片上传失败",
									width : 110,
									buttons : Ext.Msg.OK
								});
							}
					});
				}
			}
		});
		var app_snapshotimg0 = new Ext.Component({
			xtype : 'box',
			name : 'img0',
			width : 100,
			height : 100,
			fieldLabel : "预览图片",
			autoEl : {
				tag : 'img',
				src : 'app/resources/images/empty.JPG'
			}
		});
		var app_snapshotimg1 = new Ext.Component({
			xtype : 'box',
			name : 'img1',
			width : 100,
			height : 100,
			fieldLabel : "预览图片",
			autoEl : {
				tag : 'img',
				src : 'app/resources/images/empty.JPG'
			}
		});
		var app_snapshotimg2 = new Ext.Component({
			xtype : 'box',
			name : 'img2',
			width : 100,
			height : 100,
			fieldLabel : "预览图片",
			autoEl : {
				tag : 'img',
				src : 'app/resources/images/empty.JPG'
			}
		});
		var app_snapshotimg3 = new Ext.Component({
			xtype : 'box',
			name : 'img3',
			width : 100,
			height : 100,
			fieldLabel : "预览图片",
			autoEl : {
				tag : 'img',
				src : 'app/resources/images/empty.JPG'
			}
		});
		var app_snapshotimg4 = new Ext.Component({
			xtype : 'box',
			name : 'img4',
			width : 100,
			height : 100,
			fieldLabel : "预览图片",
			autoEl : {
				tag : 'img',
				src : 'app/resources/images/empty.JPG'
			}
		});
		app_snapshotimg_form = new Ext.FormPanel({
			fileUpload : true,
			baseCls : 'x-plain',
			border : false,
			fieldDefaults : {
				labelAlign : 'right',
				labelWidth : 70
			},
			defaults : {
				baseCls : 'x-plain'
			},
			buttonAlign : 'center',
			width : 580,
			height : 220,
			bodyStyle : 'padding:5 0 0 10',
			setCID:function(cid){
				c_id = cid;
			},
			items : [{
				xtype : 'label',
				text : '注意：新增资产信息时，需要先保存后再上传截图/缩略图！',
				style : 'background-color:#ffff00;font-size: 13px; margin-left: 50px'
			}, app_snapshotimg_form, {
				xtype : 'hidden',
				name : 'app_snapshotimg_id',
				id : 'app_snapshotimg_id'
			}, {
				xtype : 'hidden',
				name : 'app_snapshotimg_url_show',
				id : 'app_snapshotimg_url_show'
			},
			{
				layout : 'column',
				width : 580,
				items : [{
					columnWidth : 0.65,
					baseCls : 'x-plain',
					width : 480,
					items : [app_snapshotimg_combo,
					         snapshotimg_locked_checkbox]
				}, {
					columnWidth : 0.3,
					baseCls : 'x-plain',
					width : 100,
					items : [{
						xtype : 'button',
						text : '预览图片',
						// width: 120,
						// height: 30,
						handler : function() {
							var show_url = Ext
							.getCmp("app_snapshotimg_url_show")
							.getValue();
							// app_snapshotimg_combo
							if (show_url != null && show_url != '') {
								app_img_window.show();
								Ext.getCmp("app_img_window").getEl().dom.src = 'app/resources/images/loading.JPG';
								Ext.getCmp("app_img_window").getEl().dom.src = show_url;
							} else {
								Ext.MessageBox.show({
									title : "提示",
									msg : "请先选择一个截图",
									width : 180,
									buttons : Ext.Msg.OK
								});
							}
						}
					}, app_snapshotimg_delete_btn, app_img_locked_btn]
				}]
			}, app_snapshotimg0, app_snapshotimg1, app_snapshotimg2,
			app_snapshotimg3, app_snapshotimg4,
			app_snapshotimg_fileUploadField],
			buttons : [app_snapshotimg_upload_btn]
		});

		// 应用截图结束

		this.items = [app_snapshotimg_form];
		this.callParent();
	},
	reload: function(cid){
		this.down("form").setCID(cid);
		var store = this.down("combobox[valueField=app_img_id]").store;
		store.removeAll();
		//store.proxy.params = { id : cid };
		store.load({params:{ id : cid }});
	}
});
