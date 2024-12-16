var DemoCounters = DemoCounters || (() => {
	return {
		render: (params, data) => {
			$('#democounters-clients').click(() => { $ui.displayList(null, 'DemoClient'); });
			$('#democounters-clients .label').text(data.clients);
			$('#democounters-clients .value').text(data.nb_clients || 0);

			$('#democounters-orders').click(() => { $ui.displayList(null, 'DemoOrder'); });
			$('#democounters-orders .label').text(data.orders);
			$('#democounters-orders .value').text(data.nb_orders || 0);

			$('#democounters-contacts').click(() => { $ui.displayList(null, 'DemoContact'); });
			$('#democounters-contacts .label').text(data.contacts);
			$('#democounters-contacts .value').text(data.nb_contacts || 0);
		}
	};
})();
