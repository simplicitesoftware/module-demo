//----------------------------------------------------
// Client-side logic for order business object
//----------------------------------------------------
var DemoOrder = typeof DemoOrder !== "undefined" ? DemoOrder : (function($) {
	var resp = typeof $ui !== "undefined";

	// Helper to dynamically change unit price when selecting product (also done on server side)
	if (resp) {
		// Responsive UI hook
		Simplicite.UI.hooks.DemoOrder = function(o, cbk) {
			try {
				o.locals.ui.form.onload = function(ctn, obj) {
					var f = $ui.getUIField(ctn, obj, "demoOrdPrdId.demoPrdUnitPrice");
					f.ui.on("change", function() {
						$ui.getUIField(ctn, obj, "demoOrdUnitPrice").ui.val(f.ui.val());
					});
				};
			} catch(e) { console.error(e.message); } finally { cbk && cbk(); }
		};
	} else {
		// Legacy UI hook
		window.demoOrdPrdId_changed = function() {
			setFieldValue("demoOrdUnitPrice", getFieldValue("demoOrdPrdId.demoPrdUnitPrice"));
		};
	}
})(jQuery);
