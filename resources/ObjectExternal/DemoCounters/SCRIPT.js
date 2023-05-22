var DemoCounters = (function() {
	function render(params, data) {
		$('#democounters-clients-label').text(data.clients);
		$('#democounters-clients-value').text(data.nb_clients || 0);
		$('#democounters-orders-label').text(data.orders);
		$('#democounters-orders-value').text(data.nb_orders || 0);
		$('#democounters-contacts-label').text(data.contacts);
		$('#democounters-contacts-value').text(data.nb_contacts || 0);
	}

	return { render: render };
})();
