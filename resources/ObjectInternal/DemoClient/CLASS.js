//----------------------------------------------------
// Client-side logic for order business object
//----------------------------------------------------

/* global L */

Simplicite.UI.BusinessObjects.DemoClient = class extends Simplicite.UI.BusinessObject {
	// Action function
	map() {
		// A Promise to load the Leaflet library
		const loadLeafLet = new Promise((resolve, _reject) => {
			$ui.loadParts([
				{
					name:'leaflet-css',
					url: `${$root}/scripts/leaflet/leaflet.css`,
					type: 'CSS'
				},
				{
					name:'leaflet-js',
					url: `${$root}/scripts/leaflet/leaflet.js`,
				}
			], resolve);
		});

		// A Promise to get the client's coordinates and data in a convenient format
		const loadData = new Promise((resolve, reject) => {
			let data = {};
			data.row_id = $ui.getUIField(null, this, 'row_id').ui.val();
			if (data.row_id && data.row_id != '0') {
				const coords = $ui.getUIField(null, this, 'demoCliCoords').ui.val().replace(';', ',').split(',');
				data.latitude = coords[0];
				data.longitude = coords[1];
				data.name = $.escapeHTML(
					$ui.getUIField(null, this, 'demoCliFirstname').ui.val() + ' ' +
					$ui.getUIField(null, this, 'demoCliLastname').ui.val()
				);
				data.address = $.escapeHTML(
					$ui.getUIField(null, this, 'demoCliAddress1').ui.val() + ', ' +
					$ui.getUIField(null, this, 'demoCliZipCode').ui.val() + ' ' +
					$ui.getUIField(null, this, 'demoCliCity').ui.val() + ', ' +
					$ui.getUIField(null, this, 'demoCliCountry').ui.val()
				);
				resolve(data);
			}
			else{
				reject();
			}
		});

		// A Promise to get the Map settings
		const getSettings = new Promise(res => $ui.grant.getParameter(s => res(JSON.parse(s)), 'MAP_SETTINGS'));

		Promise
			.all([loadData, getSettings, loadLeafLet])
			.then(([data, mapSettings])=>{
				// Show the container where the map is displayed
				$('#client-map').show();

				// Add a map to that container, centered on the client's coords
				var map = L.map('client-map').setView([data.latitude,data.longitude], 13);

				// Hide the attribution prefix (Leaflet branding on the map)
				map.attributionControl.setPrefix(false);

				// Configure the map with the tile layer defined in the settings
				L.tileLayer(mapSettings.tileLayer, {
				    maxZoom: mapSettings.maxZoom,
				    attribution: mapSettings.attribution
				}).addTo(map);

				// Add a marker on the map
				var marker = L.marker([data.latitude, data.longitude]).addTo(map);

				// Show popup with extra info on marker click
				marker.bindPopup(`<b>${data.name}</b><br>${data.address}`);
			});
	}
};
