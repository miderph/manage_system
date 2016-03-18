Ext.define('app.view.module.contvideo.ContVideoWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.cont-video-window',
	
	reference: 'cont_video_window',
	
	uses: [
	       'app.store.ColumnShortCutStore',
	       'app.ux.form.MultiTextField',
	       'app.ux.form.MultiTypeUserGroup',
	       'app.ux.form.MultiImgGroup',
	       'app.ux.form.ColumnShortcutTextField',
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
		fieldDefaults: {
	        labelWidth: 120,
			labelAlign: 'right'
	    },
		items: [{
			xtype: 'textfield',
			name: 'cv_img_info',
			value: 1,
			bind: {
				//value: '{cv_img_info}'
			},
			hidden: true
		}, {
			xtype: 'multiimggroup',
	        title: '图片信息',
	        collapsible: true,
	        defaultType: 'textfield',
	        fieldName: 'imginfo',
			margin: '10, 2, 0, 0'
		}, {
			xtype: 'textfield',
			name: 'cv_base_info',
			value: 1,
			bind: {
				//value: '{cv_base_info}'
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
				xtype: 'textfield',
				name: 'c_id',
				hidden: true,
				value: '-1'
			}, {
				xtype: 'textfield',
				name: 'link_url',
				hidden: true,
				value: ''
			}, {
				xtype: 'textfield',
				name: 'zip_download_url_show',
				hidden: true,
				value: '',
				listeners: {
					change: function(combo, newValue, oldValue, eOpts ) {
						var form = this.up('form');
						var btnDownload = form.query('button[fieldName=downloadHtmlZip]')[0];
						//console.log('oldValue: ' + oldValue+',newValue: ' + newValue);
							
						var show_url = newValue;
						if (show_url!=null && show_url!='') {
							btnDownload.setDisabled(false);
						}
						else{
							btnDownload.setDisabled(true);
						}
					}
				}
			},{
				xtype: 'container',
				layout: 'hbox',
				defaultType: 'textfield',
				items: [{
					xtype: 'checkboxfield',
					name: 'is_locked',
					fieldLabel: '基本信息锁定',
					value: 0,
					inputValue:1,
					flex: 1
				}, {
					name: 'cv_alias',
					fieldLabel: '资产别名',
					flex: 1,
					emptyText: '输入资产别名'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				defaultType: 'textfield',
				margin: '10, 0, 0, 0',
				items: [{
					name: 'c_name',
					fieldLabel: '*资产名称',
					allowBlank: false,
					flex: 1,
					emptyText: '输入资产名称'
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
					fieldLabel: '*资产状态',
					emptyText: '选择资产状态'
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
				}, {
							xtype: 'combobox',editable: false,

					name: 'cv_prog_type',
					allowBlank: false,
					displayField: 'progTypeName',
					valueField:'progTypeId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoProgTypeStore}'
					},
					fieldLabel: '*节目类型',
					emptyText: '选择节目类型'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'c_type',
					allowBlank: false,
					displayField: 's_name',
					valueField:'s_id',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{contTypeStore}'
					},
					fieldLabel: '*资产类型',
					emptyText: '选择资产类型',
					listeners: {
						change: function(combo, newValue, oldValue, eOpts ) {
							var form = this.up('form');
							var cont_video_play_url = form.query('textfield[name=cv_play_url]')[0];
							var button_url_link = form.query('button[text=预览页面]')[0];
							var contZipPanel = form.query('fieldset[fieldName=htmlZip]')[0];
							//console.log('oldValue: ' + oldValue+',newValue: ' + newValue);

							var cont_video_type = newValue;
							if(cont_video_type==4 || cont_video_type==13 || cont_video_type==16){
					            //修改fieldLabel
								if (cont_video_type==16)
									cont_video_play_url.setFieldLabel('关于预览地址:');
								else
									cont_video_play_url.setFieldLabel('静态链接地址:');
								button_url_link.show();
								contZipPanel.show();
								isAllowBlank = true;
							}else{
						 		//修改fieldLabel
								cont_video_play_url.setFieldLabel('视频播放地址:');
								button_url_link.hide();
								contZipPanel.hide();
								isAllowBlank = false;
							}
							var isAllowBlank = cont_video_type==3 || cont_video_type==8 || cont_video_type==9
							                   || cont_video_type==10 || cont_video_type==11 || cont_video_type==12;
                            isAllowBlank = !isAllowBlank;
							form.query('combobox[name=cv_prog_type]')[0].allowBlank = isAllowBlank;
							form.query('combobox[name=cv_cont_type]')[0].allowBlank = isAllowBlank;
							form.query('combobox[name=cv_region]')[0].allowBlank = isAllowBlank;
							form.query('combobox[name=cv_quality_type]')[0].allowBlank = isAllowBlank;
							form.query('combobox[name=cv_has_volume]')[0].allowBlank = isAllowBlank;
						}
					}
				}, {
							xtype: 'combobox',editable: false,

					name: 'cv_cont_type',
					allowBlank: false,
					displayField: 'conttypeName',
					valueField:'conttypeId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoContTypeStore}'
					},
					fieldLabel: '*视频类型',
					emptyText: '选择视频类型'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'ad_type',
					displayField: 'contAdTypeName',
					valueField:'contAdTypeId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoAdTypeStore}'
					},
					fieldLabel: '广告类型',
					emptyText: '选择广告类型'
				}, {
							xtype: 'combobox',editable: false,

					name: 'cv_region',
					allowBlank: false,
					displayField: 'regionName',
					valueField:'regionId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoRegionStore}'
					},
					fieldLabel: '*制片地区',
					emptyText: '选择制片地区'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cv_duration',
					fieldLabel: '视频时长（秒）',
					vtype: 'naturalnum',
					flex: 1
				}, {
							xtype: 'combobox',editable: false,

					name: 'cv_quality_type',
					allowBlank: false,
					displayField: 'qualityTypeName',
					valueField:'qualityTypeId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoQualityStore}'
					},
					fieldLabel: '*清晰度',
					emptyText: '选择清晰度'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cv_director',
					fieldLabel: '导演',
					flex: 1
				}, {
							xtype: 'combobox',editable: false,

					name: 'cv_has_volume',
					allowBlank: false,
					displayField: 'hasVolumeName',
					valueField:'hasVolumeId',
					queryMode: 'local',
					flex: 1,
					bind: {
						store: '{videoHasVolumeStore}'
					},
					fieldLabel: '*是否有剧集',
					emptyText: '选择是否有剧集'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cv_actors',
					fieldLabel: '演员',
					flex: 1
				}, {
					xtype: 'textfield',
					name: 'cv_vol_total',
					fieldLabel: '总集数',
					vtype: 'naturalnum',
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cv_screenwriter',
					fieldLabel: '编剧',
					flex: 1
				}, {
					xtype: 'datefield',
					name: 'cv_vol_update_time',
					flex: 1,
					fieldLabel: '下集更新时间',
					value: new Date(),
					allowBlank: true,
					format: 'Y-m-d H:i:s'
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
					xtype: 'textfield',
					name: 'cv_language',
					fieldLabel: '语言',
					flex: 1
				}, {
					xtype: 'textfield',
					name: 'cv_year',
					fieldLabel: '上映年份',
					vtype: 'naturalnum',
					maxLength: 4,
					allowBlank: true,
					flex: 1
				}]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				items: [{
							xtype: 'combobox',editable: false,

					name: 'superscript_id',
					displayField: 'mar_s_contName',
					valueField:'mar_s_contId',
					queryMode: 'local',
					bind: {
						store: '{contSuperscriptStore}'
					},
					fieldLabel: '角标内容',
					flex: 1,
					listConfig: {
						itemTpl: ['<div data-qtip="{mar_s_contIntro}">{mar_s_contId} {mar_s_contName} {mar_s_contType} {mar_s_contProvider}</div>']
					}
				},{
					xtype: 'textfield',
					name: 'cv_month',
					fieldLabel: '上映月份',
					vtype: 'naturalnum',
					maxLength: 2,
					allowBlank: true,
					flex: 1
				}]
			}, {
				xtype: 'textareafield',
				margin: '10, 0, 10, 0',
				anchor: '100%',
				name: 'c_description',
				fieldLabel: '资产描述',
				emptyText: '请对资产进行描述'
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
		        items: [{
		        	xtype: 'textfield',
		        	name: 'cv_play_url',
		        	fieldLabel: '视频播放地址',
		        	emptyText: '请输入视频播放地址',
		        	flex: 1
		        	//,anchor: '100%'
		        }, {
			    	xtype:'button',
			    	text:'预览页面',
			    	//width: 120,
					//height: 35,
					handler: function() {
						var form = this.up('form');
						var link_url_hf = form.query("textfield[name=link_url]")[0];
						var show_url = link_url_hf.getValue();
						if(show_url!=null && show_url!=''&& show_url!='http://') {
							var width = 500;
							var height = 300;
							var top = (document.body.clientHeight-height)/2;
							var left = (document.body.clientWidth-width)/2;
							var params = 'height='+height+',width='+width+',top='+top+',left='+left+',toolbar=yes,menubar=yes,scrollbars=yes, resizable=yes,location=yes, status=yes';
							window.open(show_url,'预览页面',params);
						} else {
							Ext.MessageBox.show({
								title:"提示",
								msg:"链接不存在，无法预览",
								width:180,
								buttons:Ext.Msg.OK
							});
						}
			    	}
		        }]
			},{
				xtype: 'fieldset',
				title: 'HTML页面zip压缩包',
				collapsible: true,
				defaultType: 'textfield',
				fieldName: 'htmlZip',
				hidden: true,
				margin: '10, 0, 0, 0',
				items: [{xtype:'label',margin: '10, 0, 0, 0',text:'注意：新增Html页面信息时，需要先保存后再上传ZIP包！',style:'background-color:#ffff00;font-size: 13px; margin-left: 50px;'},
				        {xtype:'label',margin: '10, 0, 0, 0',text:'ZIP包可以包含子目录，html文件优先返回顺序为index.html、about.html、其他html文件！',style:'background-color:#ffff00;font-size: 13px; margin-left: 50px;'},
				        {
				        	xtype: 'form',
				        	layout: 'hbox',
				        	name: 'contZipPanel_form',
				        	items: [{
				        		xtype: 'filefield',
				        		name: 'c_html_file',
				        		fieldLabel: '页面zip包',
				        		buttonText: '选择zip文件',
				        		regex:/^.*(.zip|.ZIP)$/,
				                regexText:'请选择ZIP文件',
				        		labelWidth: 120,
				        		msgTarget: 'side',
				        		allowBlank: true,
				        		flex: 1,
								listeners: {
									change: function(self, v) {
	                                    var zip_fileUpload_form = this.up('form');
										var btnUpload = zip_fileUpload_form.query('button[fieldName=zip_upload_btn]')[0];
										if(!(v.toLowerCase().endsWith(".zip"))){///if(!(?:zip|ZIP)$/i.test(v))){
			    							Ext.MessageBox.show({
			    								title:"提示",
			    								msg:"请选择一个zip文件！",
			    								width:250,
			    								buttons:Ext.Msg.OK
			    							});
											btnUpload.setDisabled(true);
										}
										else
											btnUpload.setDisabled(false);
									}
								}
				        	},{
				        		xtype:'button',
				                text:'上传',
				                disabled: true,
				                fieldName:'zip_upload_btn',
				                handler:function(){
	    							var cont_form = this.up('form').up('form');
			                        var id_comp = cont_form.query("textfield[name=c_id]")[0];
			                        var cont_video_play_url = cont_form.query("textfield[name=cv_play_url]")[0];
			                        var link_url_hf = cont_form.query("textfield[name=link_url]")[0];
			                        var zip_download_url_show = cont_form.query("textfield[name=zip_download_url_show]")[0];
									var btnUpload = cont_form.query('button[fieldName=zip_upload_btn]')[0];
									var fileUpload = cont_form.query('filefield[name=c_html_file]')[0];
			                        var id = id_comp.getValue();

                                    if (id == null || id == 0 || id == -1){
		    							Ext.MessageBox.show({
		    								title:"提示",
		    								msg:"请先保存后再上传ZIP包！",
		    								width:250,
		    								buttons:Ext.Msg.OK
		    							});
                                    	return;
                                    }
                                    if (fileUpload.getValue() == ""){
		    							Ext.MessageBox.show({
		    								title:"提示",
		    								msg:"请先选择一个zip文件！",
		    								width:250,
		    								buttons:Ext.Msg.OK
		    							});
		    							btnUpload.setDisabled(true);
                                    	return;                                    	
                                    }

                            		var _interval;
                            		var zeroRepeat = 0;
                            		function showProgress(){
                            	        Ext.Ajax.request( {  
                            	           url : 'files/file_upload_progress?obj_id='+id,    
                            	           method : 'get', 
                            	           timeout : 60000,
                            	           success: function(data, options){ 
                            					  var i = (data.responseText.substring(0,data.responseText.length-1))/100;
                            					  Ext.MessageBox.updateProgress(i,data.responseText );
                            		              if(data.responseText =='100%'){  
                            		                clearInterval(_interval); 
                            		                Ext.MessageBox.updateProgress(i,"信息提取中...");
                            		                return;  
                            		              }else if(data.responseText == '0%'){
                            		            	  zeroRepeat ++;
                            		            	  if(zeroRepeat> 500){
                            		            		  clearInterval(_interval); 
                            		            		  zeroRepeat = 0;
                            		            		  Ext.MessageBox.show({
                            								title:"提示",
                            								msg:"APK上传失败!!",
                            								width:110,
                            								buttons:Ext.Msg.OK
                            							});
                            		            	  }
                            		             }
                            	            },
                            	            failure: function(data, options){
                            	            	clearInterval(_interval); 
                            	            	 Ext.MessageBox.show({
                            						title:"提示",
                            						msg:"APK上传失败!",
                            						width:110,
                            						buttons:Ext.Msg.OK
                            					});
                            	            }
                            	    	});  
                            		}
                                    
                                    var zip_fileUpload_form = this.up('form');
			                        if(zip_fileUpload_form.getForm().isValid()){
				                		//msgWin.show();
				                		Ext.MessageBox.show({
				    						title:'提示',
				    						msg:"上传进度",
				    						progressText:'0%',
				    						width:300,
				    						progress:true,
				    						closable:true
				    					});
				                		zip_fileUpload_form.getForm().submit({
				                			url:'files/file_upload.do?obj_id='+id+'&type=zip&date='+Ext.util.Format.date(new Date(),"YmdHis"),
				    	                    //waitMsg:'正在上传...',
				                			timeout: 1800000 ,
				    	                    success:function(form, action) {
				                				clearInterval(_interval); 
				                				var msg=action.result.info;
				    							Ext.MessageBox.show({
				    								title:"提示",
				    								msg:msg,
				    								width:250,
				    								buttons:Ext.Msg.OK
				    							});
				    							cont_video_play_url.setValue(action.result.zip_url);
				    							link_url_hf.setValue(action.result.zip_url_show);
				    							zip_download_url_show.setValue(action.result.zip_download_url_show);
				    							btnUpload.setDisabled(true);
				    						},
				    						failure:function(form,action) {
				    							var msg=action.result.info; 
				    							clearInterval(_interval); 
				    							Ext.MessageBox.show({
				    								title:"提示",
				    								msg:msg,
				    								width:250,
				    								buttons:Ext.Msg.OK
				    							});
				    						}
				    	                });
				                		_interval = setInterval(showProgress, 2000); 
				                    }
				                }
				            },{
				        		xtype:'button',
				        		text:'下载ZIP',
				                disabled: true,
								fieldName: 'downloadHtmlZip',
				        		handler: function() {
				        			var form = this.up('form').up('form');
				        			var zip_download_url_show = form.query('textfield[name=zip_download_url_show]')[0];

				        			var show_url = zip_download_url_show.getValue();

				        			if(show_url!=null && show_url!='') {
				        				pic = window.open(show_url,"a1");
				        				pic.document.execCommand("SaveAs");
				        			} else {
				        				Ext.MessageBox.show({
				        					title:"提示",
				        					msg:"ZIP不存在，无法下载",
				        					width:180,
				        					buttons:Ext.Msg.OK
				        				});
				        			}
				        		}
				        	}
				        	]}]
			},{
				xtype: 'multitypeusergroup',
		        title: '数据过滤（需选择测试组）',
		        collapsible: true,
		        defaultType: 'textfield',
		        fieldName: 'usergroupinfo',
				margin: '10, 2, 0, 0'
			}]
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
		show: function(win) {
//			var viewModel = this.getViewModel();
//			console.log('cw cv_base_info: ' + viewModel.get('cv_base_info'));
//	    	console.log('cw cv_img_info: ' + viewModel.get('cv_img_info'));
//			var hasBaseInfo = false;
//			var hasImgInfo = false;
//			if(viewModel.get('cv_base_info') == 1) {
//				hasBaseInfo = true;
//			}
//			if(viewModel.get('cv_img_info') == 1) {
//				hasImgInfo = true;
//			}
//			if (!hasBaseInfo && !hasImgInfo){
//				viewModel.set('cv_base_info', 1);
//				viewModel.set('cv_img_info', 1);
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