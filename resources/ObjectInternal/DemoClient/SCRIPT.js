//----------------------------------------------------
// Client-side logic for customer business object
//----------------------------------------------------

/* global google */

var DemoClient = DemoClient || (() => {
	// Action function
	function map(obj) {
		$ui.loadScript({
			url: Simplicite.GOOGLE_MAPS_JS_URL,
			onload: () => {
				const _val = name => $ui.getUIField(null, obj, name).ui.val();
				const rowId = _val('row_id');
				if (rowId && rowId != '0') {
					const c = _val('demoCliCoords');
					if (c !== '') {
						const latlgn = c.replace(';', ',').split(',');
						const pos = new google.maps.LatLng(latlgn[0], latlgn[1]);
						const map = new google.maps.Map($('#client-map').show()[0], { center: pos, zoom: 13, mapTypeId: google.maps.MapTypeId.ROADMAP });
						const mrk = new google.maps.Marker({ position: pos, map: map });
						const name = $.escapeHTML(_val('demoCliFirstname') + ' ' + _val('demoCliLastname'));
						const addr = $.escapeHTML(_val('demoCliAddress1') + ', ' + _val('demoCliZipCode') + ' ' + _val('demoCliCity') + ', ' + _val('demoCliCountry'));
						const inf = new google.maps.InfoWindow({ content: '<div style="width: 200px; height: 75px;"><b>' + name + '</b><br/>' + addr + '</div>' });
						google.maps.event.addListener(mrk, 'click', () => { inf.open(map, mrk); });
					}
					return false;
				}
			}
		});
	}

	return { map: map };
})();