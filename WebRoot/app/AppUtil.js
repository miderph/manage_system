AppUtil = {};

AppUtil.ChoiceData = [];

/*
    	AppUtil.showChoiceWindow({
			view: this,
			btn: btn,
			mWin: this.lookupReference('role_window'),
			viewModel: this.mainViewModel,
			storeRef: 'siteAllStore',
			store: 'app.store.SiteAllStore',
			win_title: '选择站点',
			url: 'site/query_all.do',
			d_key: 'siteArr',
			d_id: 's_id',
			d_name: 's_name'
		});
		
		查找是否已存在，顺序为：
		     1，按d_key，找到后直接返回
             2，按viewModel和storeRef，找到后，并解析、缓存【AppUtil.parserChoiceStore(p, store)】，然后返回
             3，按store，找到后重新获取数据，并解析、缓存【AppUtil.parserChoiceStore(p, store)】，然后返回
             4，以上都不存在时，将重新获取数据【AppUtil.requestChoiceData(p)】，并解析、缓存，然后返回
                         注意：重新获取数据时url将覆盖store的proxy.url
 * */
AppUtil.showChoiceWindow = function(p) {
	if(AppUtil.ChoiceData[p.d_key]) {
		var allData = AppUtil.ChoiceData[p.d_key];
		AppUtil.choiceWindow(p.view, p.btn, p.win_title, allData);
	} else {
		
		var store = null;
		if(p.viewModel) {
			var store = p.viewModel.get(p.storeRef);
		}
		
		if(store != null && store != undefined && Ext.isObject(store)) {
			AppUtil.parserChoiceStore(p, store);
		} else {
			if(p.store) {	//Ext.data.Store优先于Ajax请求
				p.mWin.mask('正在获取数据...');
				var store = Ext.create(p.store, {proxy:{url:p.url},
					listeners: {
						load: function(self, records, successful) {
							p.mWin.unmask();
							p.viewModel.set(p.storeRef, store);
							AppUtil.parserChoiceStore(p, store);
						}
					}
				})
			} else {
				AppUtil.requestChoiceData(p);
			}
		}

	}
}

AppUtil.parserChoiceStore = function(p, store) {
	
	if(!Ext.isObject(p)) return;
	
	var allData = [];
	var id = p.d_id;
	var name = p.d_name;
	store.each(function(record, index, total) {
		allData.push({
			text: record.raw[name],
			value: record.raw[id]
		});
	});
	
	AppUtil.ChoiceData[p.d_key] = allData;
	
	AppUtil.choiceWindow(p.view, p.btn, p.win_title, allData);
}

AppUtil.choiceWindow = function(view, btn, title, data) {
	var win = view.lookupReference('checkboxgroup_window');
	var ids = view.lookupReference(btn.rValueName);
	var checkedData = [];
	
	var ids_v = ids.getValue();
	if(ids_v != null && ids_v != undefined && ids_v.length > 0) {
		checkedData = ids_v.split(',');
	}
	
	
	var names = view.lookupReference(btn.rTextName);
	
	var joinStr = '\n';
	if(names.getXType() === "textfield") {
		joinStr = ','
	}
	
	if (win) {
		win.destroy();
    }
	
	win = Ext.create('app.ux.window.CheckboxGroupWindow', {
    	width: 600,
    	title: title,
    	allData: data,
    	checkedData: checkedData,
    	onSubmit: function(w, vArr, tArr) {
    		ids.setValue(vArr.join(','));
        	names.setValue(tArr.join(joinStr));
    		w.destroy();
    	},
    	onCancel: function(w) {
    		w.destroy();
    	}
    });
	
	view.getView().add(win);
	
	win.show();
}

AppUtil.requestChoiceData = function(p) {
	
	if(!Ext.isObject(p)) return;
	
	p.mWin.mask('正在获取数据...');
	
	Ext.Ajax.request({
		url: p.url,
		async: true,
		scope: this,
		timeout: 5*60*1000,
		success: function(resp){
			var respJson = Ext.JSON.decode(resp.responseText);
			
			var data = respJson.data;
			
			var allData = [];
			var id = p.d_id;
			var name = p.d_name;
			Ext.Array.each(data, function(item, index) {
				if(item[id] > -1) {
					allData.push({
						text: item[name],
						value: item[id]
					});
				}
			});
			
			AppUtil.ChoiceData[p.d_key] = allData;
			AppUtil.choiceWindow(p.view, p.btn, p.win_title, allData);
			p.mWin.unmask();
	    },
	    failure: function() {
	    	AppUtil.ChoiceData[p.d_key] = [];
			AppUtil.choiceWindow(p.view, p.btn, p.win_title, []);
	    	win.unmask();
	    }
	});
}

AppUtil.previewHtml = function(show_url) {
						//var show_url = link_url_hf.getValue();
						if(show_url!=null && show_url!=''&& show_url!='http://') {
							//cont_linkUrl_window.show();
							//Ext.get('main').dom.src = show_url;
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

AppUtil.crudHotInfo = function(controler, c_id, c_name, c_type) {
	//console.log("------selected c_id="+c_id);

	if(c_id == -1) {
		Ext.toast({
				html: '请选择一项',
				title: '提示',
				saveDelay: 10,
				align: 'tr',
				closable: true,
				width: 200,
				useXAxis: true,
				slideInDuration: 500
			});
	} else {
		var win = controler.lookupReference('cs_hotinfolist_window');
		
    	if (!win) {
            win = Ext.create('app.view.module.contsales.ContHotInfoListWindow', {
            	onController: controler,
            	c_id: c_id,
            	c_name: c_name,
            	c_type: c_type
            });
            controler.getView().add(win);
        }
    	win.c_id = c_id;
    	win.c_name = c_name;
    	win.c_type = c_type;

    	win.setTitle('【'+c_name+'|'+c_id+'】的促销信息列表');
    	
    	var store = win.down('gridpanel').getStore();
    	store.proxy.extraParams.c_id = c_id;
		store.proxy.extraParams.c_name = c_name;
		
    	win.show();
    	store.removeAll();
		store.load();
	}
	
}