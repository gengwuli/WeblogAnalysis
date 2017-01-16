;
(function() {
	angular.module('app')
		.constant('apiURL', 'http://172.16.249.139:3001')
		.factory('api', apiService);

	apiService.$inject = ['$http', '$resource', 'apiURL'];

	function apiService($http, $resource, apiURL) {
		$http.defaults.withCredentials = true;

		return $resource(apiURL + '/:endpoint/:user/:id', { user: '@user', id: '@id'}, {
			getRefhostTopn: {method : 'GET', params: {endpoint: ''}},
			getAbout: {method : 'GET', params: {endpoint: 'about'}},
			getBlack: {method : 'GET', params: {endpoint: 'black-ip-list'}},
			getCass:  {method : 'GET', params: {endpoint: 'cassandra-clustor'}},
			getHive:  {method : 'GET', params: {endpoint: 'finance-rhive-repurchase'}},
			getHdoop: {method : 'GET', params: {endpoint: 'hadoop-family-roadmap'}},
			getHive:  {method : 'GET', params: {endpoint: 'hadoop-hive-intro'}},
			getZkp:   {method : 'GET', params: {endpoint: 'hadoop-zookeeper-intro'}},
			getMaht:  {method : 'GET', params: {endpoint: 'hadoop-mahout-roadmap'}},
			getDistinctIp:  {method : 'GET', params: {endpoint: 'distinctIp'}}
		})
	}

})()


// option = {
//     tooltip : {         // Option config. Can be overwrited by series or data
//         formatter: function (params,ticket,callback) {
//             console.log(params)
//             var res = 'Function formatter : <br/>' + params[0].name;
//             for (var i = 0, l = params.length; i < l; i++) {
//                 res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
//             }
//             setTimeout(function (){
//                 // 仅为了模拟异步回调
//                 callback(ticket, res);
//             }, 1000)
//             return 'loading';
//         }
//         //formatter: "Template formatter: <br/>{b}<br/>{a}:{c}<br/>{a1}:{c1}"
//     },
//     toolbox: {
//         show : true,
//         feature : {
//             mark : {show: true},
//             dataView : {show: true, readOnly: false},
//             magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
//             restore : {show: true},
//             saveAsImage : {show: true}
//         }
//     },
//     calculable : true,
//     xAxis : {
//         data : ['周一','周二','周三','周四','周五','周六','周日']
//     },
//     yAxis : {
//         type : 'value'
//     },
//     series : [
//         {
//             name:'坐标轴触发1',
//             type:'bar',
//             data:[
//                 {value:320, extra:'Hello~'},
//                 332, 301, 334, 390, 330, 320
//             ]
//         },
//         {
//             name:'坐标轴触发2',
//             type:'bar',
//             data:[862, 1018, 964, 1026, 1679, 1600, 157]
//         },
//         {
//             name:'数据项触发1',
//             type:'bar',
//             tooltip : {             // Series config.
//                 trigger: 'item',
//                 backgroundColor: 'black',
//                 position : [0, 0],
//                 formatter: "Series formatter: <br/>{a}<br/>{b}:{c}"
//             },
//             stack: '数据项',
//             data:[
//                 120, 132,
//                 {
//                     value: 301,
//                     itemStyle: {normal: {color: 'red'}},
//                     tooltip : {     // Data config.
//                         backgroundColor: 'blue',
//                         formatter: "Data formatter: <br/>{a}<br/>{b}:{c}"
//                     }
//                 },
//                 134, 90,
//                 {
//                     value: 230,
//                     tooltip: {show: false}
//                 },
//                 210
//             ]
//         },
//         {
//             name:'数据项触发2',
//             type:'bar',
//             tooltip : {
//                 show : false,
//                 trigger: 'item'
//             },
//             stack: '数据项',
//             data:[150, 232, 201, 154, 190, 330, 410]
//         }
//     ]
// };
//                     