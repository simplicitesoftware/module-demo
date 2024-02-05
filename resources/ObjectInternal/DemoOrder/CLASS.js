//----------------------------------------------------
// Client-side logic for order business object
//----------------------------------------------------
Simplicite.UI.BusinessObjects.DemoOrder = class extends Simplicite.UI.BusinessObject {
	onLoadForm(ctn, obj, p) {
		super.onLoadForm(ctn, obj, p);

		// Helper to dynamically change unit price when selecting product (also done on server side)
		var up = $ui.getUIField(ctn, obj, "demoOrdPrdId.demoPrdUnitPrice").ui;
		up.on("change", () => $ui.getUIField(ctn, obj, "demoOrdUnitPrice").ui.val(up.val()));
	}
};