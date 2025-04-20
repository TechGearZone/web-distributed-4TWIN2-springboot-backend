const { Eureka } = require('eureka-js-client');

const client = new Eureka({
    instance: {
        app: 'CUSTOMER-SERVICE',
        instanceId: 'customer-service-nodejs:3000',
        hostName: 'localhost', // Or your container/service hostname
        ipAddr: '127.0.0.1',
        port: {
            '$': 3000, // Your Express server port
            '@enabled': true,
        },
        vipAddress: 'customer-service',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/',
    },
});

module.exports = client;
