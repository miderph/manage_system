Ext.define('app.view.module.customer.GoodsOrderToolbar', {
	extend: 'Ext.toolbar.Toolbar',
    
	alias: 'widget.goods-order-toolbar',
	
	uses: ['app.ux.form.GoodsMenuGridBox'],
	
	items: ['<div style="font-weight: bolder; font-size: 18px; color:#00F;">订单信息</div>', '->', '搜索商品：', {
		xtype: 'goods-menu-gridbox',
		reference: 'goods_order_search_text',
		width: 600
	}, {
		xtype: 'button',
		text: '搜索',
		glyph: 0xf002,
		width: 60,
		margin: '0, 0, 0, 10',
		handler: function() {
			var toolbar = this.up('goods-order-toolbar');
			
			var sf = toolbar.query('goods-menu-gridbox')[0];
			
			if(!Ext.isEmpty(sf.getValue())) {
				sf.menu.showAt(sf.getX(), sf.getY() + sf.getHeight());
    			sf.giStore.proxy.extraParams.c_name = sf.getValue();
	    		sf.giStore.load();
    		}
		}
	}, '->']
});