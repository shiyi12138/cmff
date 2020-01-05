<%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="../boot/js/jquery-2.2.1.min.js"></script>
    <script src="../echarts/echarts.min.js"></script>
    <script src="../echarts/china.js"></script>
    <!--goEasy的依赖-->
    <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.3.js"></script>
    <script type="text/javascript">
       $(function () {
           month();
           main();
           address();
       })
       function main() {
           // 基于准备好的dom，初始化echarts实例
           var myChart = echarts.init(document.getElementById('main'));

           // 指定图表的配置项和数据
           option = {
               title:{
                   left: 'center',
                   text:'最近七天内用户注册人数'
               },
               tooltip: {},
               legend: {
                   data:['人数'],
                   left: 'left'
               },
               xAxis: {
                   type: 'category',
                   data: ['一天内', '二天内', '三天内', '四天内', '五天内', '六天内', '七天内']
               },
               yAxis: {
                   type: 'value'
               },
               series: [{
                   name:'人数',
                   type: 'bar'
               }]
           };

           // 使用刚指定的配置项和数据显示图表。
           myChart.setOption(option);
           $.ajax({
               url:"${pageContext.request.contextPath}/echarts/timeNum",
               datatype:"json",
               success:function (date) {
                   myChart.setOption({
                       series : [
                           {
                               data:date
                           }
                       ]
                   });
               }
           });
           var goEasy = new GoEasy({
               host:'hangzhou.goeasy.io',
               appkey: "BS-3fabe9c3379f42a2a04dc502f0babaab", //替换为您的应用appkey(这个是只订阅)
           });
           goEasy.subscribe({
               channel: "seven", //替换为您自己的channel（这个名字是自定义的，表示订阅的通道，和推送端一致即可）
               onMessage: function (message) {
                   var list=message.content;
                   var parse = JSON.parse(list);
                   //修改7天图
                   myChart.setOption({
                       series : [
                           {
                               data:parse
                           }
                       ]
                   });
               }
           });
       }
        function month() {
            // 基于准备好的dom，初始化echarts实例
            var myMonth = echarts.init(document.getElementById('month'));

            // 指定图表的配置项和数据
            option = {
                title:{
                    left: 'center',
                    text:'1~12月份用户注册人数'
                },
                tooltip: {},
                legend: {
                    data:['人数'],
                    left: 'left'
                },
                xAxis: {
                    type: 'category',
                    data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name:'人数',
                    type: 'line'
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myMonth.setOption(option);
            $.ajax({
                url:"${pageContext.request.contextPath}/echarts/monthNum",
                datatype:"json",
                success:function (date) {
                    myMonth.setOption({
                        series : [
                            {
                                data:date
                            }
                        ]
                    });
                }
            });
            var goEasy = new GoEasy({
                host:'hangzhou.goeasy.io',
                appkey: "BS-3fabe9c3379f42a2a04dc502f0babaab", //替换为您的应用appkey(这个是只订阅)
            });
            goEasy.subscribe({
                channel: "month", //替换为您自己的channel（这个名字是自定义的，表示订阅的通道，和推送端一致即可）
                onMessage: function (message) {
                    var list=message.content;
                    var parse = JSON.parse(list);
                    //修改7天图
                    myMonth.setOption({
                        series : [
                            {
                                data:parse
                            }
                        ]
                    });
                }
            });
        }
        function address() {
            // 基于准备好的dom，初始化echarts实例
            var myAddress = echarts.init(document.getElementById('address'));
            option = {
                title : {
                    text: '用户地理分布图',
                    left: 'center'
                },
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data:['人数']
                },
                visualMap: {
                    min: 0,
                    max: 2500,
                    left: 'left',
                    top: 'bottom',
                    text:['高','低'],           // 文本，默认为数值文本
                    calculable : true
                },
                toolbox: {
                    show: true,
                    orient : 'vertical',
                    left: 'right',
                    top: 'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                series : [
                    {
                        name: '人数',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        }
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myAddress.setOption(option);
            $.ajax({
                url:"${pageContext.request.contextPath}/echarts/totleCount",
                datatype:"json",
                success:function (date) {
                    myAddress.setOption({
                        series : [
                            {
                                data:date
                            }
                        ]

                });
                }
            })
        }
    </script>
</head>
<body>
    <div id="main" style="margin:0 auto;width: 700px;height:400px;"></div>
    <div id="month" style="margin:0 auto;width: 1000px;height:400px;"></div>
    <div id="address" style="margin:0 auto;width: 800px;height:400px;"></div>
</body>
</html>