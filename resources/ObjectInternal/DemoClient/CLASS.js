//----------------------------------------------------
// Client-side logic for order business object
//----------------------------------------------------

/* global google */

Simplicite.UI.BusinessObjects.DemoClient = class extends Simplicite.UI.BusinessObject {
	// Action function
	map() {
		$ui.loadScript({
			url: Simplicite.GOOGLE_MAPS_JS_URL,
			onload: () => {
				const rowId = $ui.getUIField(null, this, 'row_id').ui.val();
				if (rowId && rowId != '0') {
					const c = $ui.getUIField(null, this, 'demoCliCoords').ui.val();
					if (c !== '') {
						const latlgn = c.replace(';', ',').split(',');
						const pos = new google.maps.LatLng(latlgn[0], latlgn[1]);
						const map = new google.maps.Map($('#client-map').show()[0], {
							center: pos, zoom: 13, mapId: `demo_client_map_${rowId}`,
							mapTypeId: google.maps.MapTypeId.ROADMAP
						});
						const mrk = new google.maps.marker.AdvancedMarkerElement({ position: pos, map: map });
						const name = $.escapeHTML(
							$ui.getUIField(null, this, 'demoCliFirstname').ui.val() + ' ' +
							$ui.getUIField(null, this, 'demoCliLastname').ui.val()
						);
						const addr = $.escapeHTML(
							$ui.getUIField(null, this, 'demoCliAddress1').ui.val() + ', ' +
							$ui.getUIField(null, this, 'demoCliZipCode').ui.val() + ' ' +
							$ui.getUIField(null, this, 'demoCliCity').ui.val() + ', ' +
							$ui.getUIField(null, this, 'demoCliCountry').ui.val())
						;
						const inf = new google.maps.InfoWindow({
							content: `<div style="color: #000; width: 200px;"><b>${name}</b><br/>${addr}</div>`
						});
						google.maps.event.addListener(mrk, 'click', () => { inf.open(map, mrk); });
						return true;
					}
					return false;
				}
			}
		});
	}
};