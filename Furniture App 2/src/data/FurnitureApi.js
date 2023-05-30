const url = 'http://localhost:3500';

export const FurnitureApi = {

    // CREATE operations
    addCustomer: async (custData) => {
        const allCustomers = await FurnitureApi.getCustomers();

        // make sure the username isn't taken
        for (let customer of allCustomers) {
            if (custData.username === customer.username) {
                return false;
            }
        }
        
        const custUrl = url + '/customers';
        const postOp = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(custData)
        }
        await fetch(custUrl, postOp);
        return true;
    },

    addOrder: async(cust) => {
        const custUrl = url + '/customers/' + cust.id;

        const putOp = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(cust)
        }
        await fetch(custUrl, putOp);
        return true;
    },

    addItem: async (itemData) => {
        const itemUrl = url + '/items';
        const postOption = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(itemData)
        }

        await fetch(itemUrl, postOption);
        return true;
    },



    // READ operations
    getItems: async () => {
        const allItems = url + '/items';
        const response = await fetch(allItems);
        const items = await response.json();
        return items;
    },

    getItemsByQueryString: async (str) => {
        const allItems = url + '/items?q=' + str;
        const response = await fetch(allItems);
        const items = await response.json();
        return items;
    },

    getCustomers: async() => {
        const custUrl = url + '/customers';
        const response = await fetch(custUrl);
        const customers = await response.json();
        return customers;
    },

    getCustomerById: async(id) => {
        const custUrl = url + '/customers/' + id;
        const response = await fetch(custUrl);
        const customers = await response.json();
        return customers;
    },

    getItemById: async (id) => {
        const allItems = url + '/items/' + id;
        const response = await fetch(allItems);
        const item = await response.json();
        return item;
    },

    login: async(u, p) => {
        const allCustomers = await FurnitureApi.getCustomers();

        for (let customer of allCustomers) {
            if (u === customer.username && p === customer.password) {
                return customer;
            }
        }
        return null;
    },



    // UPDATE operations
    updateItemById: async (id, itemData) => {
        const itemUrl = url + '/items/' + id;
        const putOption = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(itemData)
        }

        await fetch(itemUrl, putOption);
        return true;
    },





    // DELETE operations
    // none yet...
}