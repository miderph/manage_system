Ext.define('app.view.module.column.PreviewImgWindow', {
	extend: 'Ext.window.Window',
	alias: 'widget.previewimg-window',
	
	reference: 'previewimg_window',
	closable: true,
	closeAction: 'hide',
	resizable: false,
	modal: true,
	draggable: true,
	autoShow: true,
	/*width: 720,
	height: 520,*/
	scrollable: true,
	title: '图片预览',
	glyph: 0xf007,
	layout: 'fit',
	initComponent: function() {
		
		this.maxHeight = Ext.getBody().getHeight() - 20;
		this.width = 1010;
		
		this.items = [{
			xtype: 'form',
			bodyPadding: 10,
			items: [{
				xtype: 'radiogroup',
				fieldLabel: '推荐位图片类型',
				margin: '10, 0, 0, 0',
				enableFocusableContainer: false,
				hideLabel: true,
				flex: 1,
				items: [
	                {boxLabel: '横图', name: 'c_img_rec_postion', inputValue:'1'},
				    {boxLabel: '竖图', name: 'c_img_rec_postion', inputValue:'0'},
	    			{boxLabel: '小方图', name: 'c_img_rec_postion', inputValue:'2'},
	    			{boxLabel: '四格图', name: 'c_img_rec_postion', inputValue:'3'},
	    			{boxLabel: '六格图', name: 'c_img_rec_postion', inputValue:'4'}
	            ]
			}, {
				xtype: 'container',
				layout: 'hbox',
				margin: '10, 0, 0, 0',
				defaultType: 'datefield',
				items: [{
					xtype: 'image',
					src: Ext.BLANK_IMAGE_URL,
					bind: {src: '{c_img_url}'},
					style: 'height: 100px; width: 200px;',
					margin: '0, 0, 0, 5'
				}, {
					xtype: 'image',
					src: Ext.BLANK_IMAGE_URL,
					bind: {src: '{c_img_little_url}'},
					style: 'height: 200px; width: 150px;',
					margin: '0, 0, 0, 5'
				}, {
					xtype: 'image',
					src: Ext.BLANK_IMAGE_URL,
					bind: {src: '{c_img_icon_url}'},
					style: 'height: 100px; width: 100px;',
					margin: '0, 0, 0, 5'
				}, {
					xtype: 'image',
					src: Ext.BLANK_IMAGE_URL,
					bind: {src: '{c_img_4_squares_url}'},
					style: 'height: 200px; width: 200px;',
					margin: '0, 0, 0, 5'
				}, {
					xtype: 'image',
					src: Ext.BLANK_IMAGE_URL,
					bind: {src: '{c_img_6_squares_url}'},
					style: 'height: 200px; width: 300px;',
					margin: '0, 0, 0, 5'
				}]
			}]
		}];
		
		this.buttons = [{
			text: '取消',
			scope: this,
			handler: function() {
				this.hide();
			}
		}];
		this.listeners = {
		    	hide: function(self, eOpts) {
		    		//console.log("onhide(),begin:self=" + self+",this.myviewModel"+this.getViewModel());
		    		this.getViewModel().set('c_img_url', "app/resources/images/loading.JPG");
		    		this.getViewModel().set('c_img_little_url', "app/resources/images/loading.JPG");
		    		this.getViewModel().set('c_img_icon_url', "app/resources/images/loading.JPG");
		    		this.getViewModel().set('c_img_4_squares_url', "app/resources/images/loading.JPG");
		    		this.getViewModel().set('c_img_6_squares_url', "app/resources/images/loading.JPG");
		    		//console.log("onhide(),end:self=" + self);
		    	}
		};
		this.callParent();
	},
	
	items:[],
	buttonAlign: 'center'
});