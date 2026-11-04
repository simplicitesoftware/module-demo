import Simplicite from "simplicite";
import type { Container, UIBusinessObject, UIField, FormParam } from "simplicite";

/**
 * Order business object
 * @class
 */
Simplicite.UI.BusinessObjects.DemoOrder = class extends Simplicite.UI.BusinessObject {
    /** @override */
    override onLoadForm(ctn: Container, obj: UIBusinessObject, params: FormParam) {
        super.onLoadForm(ctn, obj, params);

        // Note that this client-side "live" logic will be overridden
        // anyway by server-side logic at save

        const pup: UIField|undefined = $ui.getUIField(ctn, obj, 'demoOrdPrdId.demoPrdUnitPrice').ui;
        const up: UIField|undefined = $ui.getUIField(ctn, obj, 'demoOrdUnitPrice').ui;
        const q: UIField|undefined = $ui.getUIField(ctn, obj, 'demoOrdQuantity').ui;
        const t: UIField|undefined = $ui.getUIField(ctn, obj, 'demoOrdTotal').ui;
        const v: UIField|undefined = $ui.getUIField(ctn, obj, 'demoOrdVAT').ui;

        const calcTotal = () => {
            t?.val((q?.val() as number || 0) * (up?.val() as number || 0));
            v?.val((t?.val() as number || 0) * ($grant.sysparams?.DEMO_VAT as number || 0) / 100);
        };

        // Change unit price if product is changed
        pup?.on('change', () => {
            up?.val(pup?.val() || 0);
            calcTotal();
        });

        // recalculate total if quantity changes
        q?.on('change', calcTotal);
    }
};