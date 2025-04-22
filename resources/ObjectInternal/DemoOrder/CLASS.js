//----------------------------------------------------
// Client-side logic for order business object
//----------------------------------------------------

Simplicite.UI.BusinessObjects.DemoOrder = class extends Simplicite.UI.BusinessObject {
	onLoadForm(ctn, obj, p) {
		super.onLoadForm(ctn, obj, p);

		// Dynamic client-side logic (overridden anyway by server-side logic at save)

		const pup = $ui.getUIField(ctn, obj, 'demoOrdPrdId.demoPrdUnitPrice').ui;
		const up = $ui.getUIField(ctn, obj, 'demoOrdUnitPrice').ui;
		const q = $ui.getUIField(ctn, obj, 'demoOrdQuantity').ui;
		const t = $ui.getUIField(ctn, obj, 'demoOrdTotal').ui;
		const v = $ui.getUIField(ctn, obj, 'demoOrdVAT').ui;

		const calcTotal = () => {
			t.val(q.val() * up.val());
			v.val(t.val() * parseFloat($grant.sysparams.DEMO_VAT) / 100);
		};

		// Change unit price if product is changed
		pup.on('change', () => {
			up.val(pup.val());
			calcTotal();
		});

		// recalculate total if quantity changes
		q.on('change', calcTotal);
	}
};