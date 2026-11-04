import Simplicite from "simplicite";
import type { KeyObject } from "simplicite";

/**
 * Customer object
 * @class
 */
Simplicite.UI.BusinessObjects.DemoClient = class extends Simplicite.UI.BusinessObject {
    // Action function
    public async map(): Promise<void> {
        const L = await $factory.Leaflet();

        const data: KeyObject = {};
        data.row_id = $ui.getUIField(null, this, 'row_id')?.ui?.val();
        if (data.row_id && data.row_id != '0') {
            const coords = ($ui.getUIField(null, this, 'demoCliCoords')?.ui?.val() as string || '').replace(';', ',').split(',');
            data.latitude = coords[0];
            data.longitude = coords[1];
            data.name = $.escapeHTML(
                $ui.getUIField(null, this, 'demoCliFirstname')?.ui?.val() + ' ' +
                $ui.getUIField(null, this, 'demoCliLastname')?.ui?.val()
            );
            data.address = $.escapeHTML(
                $ui.getUIField(null, this, 'demoCliAddress1')?.ui?.val() + ', ' +
                $ui.getUIField(null, this, 'demoCliZipCode')?.ui?.val() + ' ' +
                $ui.getUIField(null, this, 'demoCliCity')?.ui?.val() + ', ' +
                $ui.getUIField(null, this, 'demoCliCountry')?.ui?.val()
            );
        }

        const settings = JSON.parse(await $ui.grant?.getParameter('MAP_SETTINGS') as string || '{}');

        $('#client-map').show();

        const map = L.map('client-map').setView([ data.latitude, data.longitude ], 13);
        map.attributionControl.setPrefix(false);
        L.tileLayer(settings.tileLayer, {
            maxZoom: settings.maxZoom,
            attribution: settings.attribution
        }).addTo(map);
        const marker = L.marker([data.latitude, data.longitude]).addTo(map);
        marker.bindPopup(`<b>${data.name}</b><br/>${data.address}`);
    }
};