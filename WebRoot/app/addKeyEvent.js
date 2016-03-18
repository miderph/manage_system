function initKeyEvent() {
	if(document.addEventListener) {
		document.addEventListener("keydown", maskBackspace, true);
	} else {
		document.attachEvent("onkeydown",maskBackspace);
	}
};

function maskBackspace(event) {
	var event = event||window.event;
	var obj = event.target||event.srcElement;
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if(keyCode == 8) {
		if(obj!=null && obj.tagName!=null && ((obj.tagName.toLowerCase()=='input' && obj.type.toLowerCase()!='checkbox') || obj.tagName.toLowerCase() == 'textarea')) {
			event.returnValue = true;
            if(Ext.getCmp(obj.id)) {
                if(Ext.getCmp(obj.id).readOnly) {
                    if(window.event) {
                        event.returnValue = false;		//or event.keyCode=0
                    } else {
                        event.preventDefault();
                    }
                }
            }
        } else {
            if(window.event) {
                event.returnValue = false;		// or event.keyCode=0
            } else {
                event.preventDefault();
            }
        }
    }
}

