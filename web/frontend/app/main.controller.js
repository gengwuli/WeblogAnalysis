(function() {
    'use strict';

    angular
        .module('app')
        .controller('MainController', MainController);

    MainController.$inject = ['$scope', 'api'];

    function MainController($scope, api) {
        var vm = this;
        
        var myChart = echarts.init(document.getElementById('main'));
        var myChart1 = echarts.init(document.getElementById('main1'));
        vm.getAbout = function() { api.getAbout(); }
        vm.getBlack = function() { api.getBlack(); }
        vm.getCass = function() { api.getCass(); }
        vm.getHive = function() { api.getHive(); }
        vm.getHdoop = function() { api.getHdoop(); }
        vm.getHive = function() { api.getHive(); }
        vm.getZkp = function() { api.getZkp(); }
        vm.getMaht = function() { api.getMaht(); }

        var option = {
            title: {
                text: 'Host reference counts'
            },
            tooltip: { // Option config. Can be overwrited by series or data
                formatter: function(params, ticket, callback) {
                    console.log(params)
                    var year = 2016;
                    var month = params.name.substring(0, 2);
                    var day = params.name.substring(2, 4);
                    var hour = params.name.substring(5, 7);
                    var date = new Date(year, month, day, hour);
                    var formatdate = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate() + " " + date.getHours() + ":" + "00"
                    var top;
                    if (params.seriesName === "top1") {
                        top = option.refs.data[params.dataIndex * 3]
                    } else if (params.seriesName === "top2") {
                        top = option.refs.data[params.dataIndex * 3 + 1]
                    } else {
                        top = option.refs.data[params.dataIndex * 3 + 2]
                    }
                    var res = formatdate
                    res += '<br/>' + params.seriesName + ' : ' + top + '<br/>' + 'counts : ' + params.value;
                    return res;
                }
            },
            refs: {
                data: []
            },
            legend: {},
            xAxis: {},
            yAxis: {},
            series: []
        };

        vm.getDistinctIp = function() {
        	vm.b1 = true; vm.b2 = false;
            api.getDistinctIp().$promise.then(function(result) {
            	var x = []
	            var y = []
                console.log(result);
                result.distIp.forEach(function(d, i) {
                    x.push(d.hour);
                    y.push(d.sum);
                })
                var option = {
                    title: {
                        text: 'Independent Ips'
                    },
                    tooltip: {
                        formatter: function(params, ticket, callback) {
                            console.log(params)
                            var year = 2016;
                            var month = params.name.substring(0, 2);
                            var day = params.name.substring(2, 4);
                            var hour = params.name.substring(5, 7);
                            var date = new Date(year, month, day, hour);
                            var formatdate = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate() + " " + date.getHours() + ":" + "00"
                            var top;
                            var res = formatdate
                            res += '<br/>' + 'new visits : ' + params.value;
                            return res;
                        }
                    },
                    legend: {
                        data: ['visits']
                    },
                    xAxis: {
                        data: x
                    },
                    yAxis: {},
                    series: [{
                        name: 'visits',
                        type: 'line',
                        data: y
                    }]
                };
                option.xAxis.axisLabel = {
                    rotate: 90,
                    formatter: function(value) {
                        return value.split(' ')[1]
                    }
                }
                myChart1.setOption(option);
            });

        }

        vm.getRefHostTopn = function() {
        	vm.b1 = false; vm.b2 = true;
            api.getRefhostTopn().$promise.then(function(result) {
                var count = 0;
                var x = []
                var y1 = []
                var y2 = []
                var y3 = []
                var z = []
                result.topn.forEach(function(d, i) {
                    z.push(d.ref_host)
                    console.log(count % 3, d.ref_host)
                    if (count % 3 == 0) {
                        x.push(d.hour);
                        y1.push(parseInt(d.ref_host_cnts, 10));

                    } else if (count % 3 == 1) {
                        y2.push(parseInt(d.ref_host_cnts, 10));
                    } else {
                        y3.push(parseInt(d.ref_host_cnts, 10));
                    }
                    count++;
                })
                option.xAxis.data = x;
                option.xAxis.axisLabel = {
                    rotate: 90,
                    formatter: function(value) {
                        return value.split(' ')[1]
                    }
                }
                option.series.length = 0;
                option.series.push({ name: 'top1', type: 'bar', stack: "hello", data: y1, showAllSymbol: true });
                option.series.push({ name: 'top2', type: 'bar', stack: "hello", data: y2, showAllSymbol: true });
                option.series.push({ name: 'top3', type: 'bar', stack: "hello", data: y3, showAllSymbol: true });
                option.refs.data = z;
                myChart.setOption(option);
            })
        }
    }
})()
