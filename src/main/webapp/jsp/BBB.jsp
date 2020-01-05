<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="../boot/js/jquery-2.2.1.min.js"></script>
    <script src="../kindeditor/kindeditor-all-min.js"></script>
    <script src="../kindeditor/lang/zh-CN.js"></script>
    <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.3.js"></script>
    <script>
        KindEditor.ready(function(K) {
            window.editor = K.create('#bbb',{
                afterCreate : function() {
                    this.sync();
                },
                afterBlur:function(){
                    this.sync();
                }
            });
        });
        $(function () {
            var goEasy = new GoEasy({
                host:'hangzhou.goeasy.io',
                appkey: "BC-002a017172624d18834aaa69679edeb5", //替换为您的应用appkey
            });
            goEasy.subscribe({
                channel: "aaa", //替换为您自己的channel
                onMessage: function (message) {
                    var m = new Date().getMinutes()
                    var s = new Date().getSeconds()
                    var h = new Date().getHours()
                    var time = h + ":" + m + ":" + s;
                    //打印到输出页面
                    $("#show").append("<p style=\"margin-left: 120px;color:  hotpink\">悟空"+time+"</p>"+
                        "<p style=\"margin-left: 160px;color: #1b6d85\">"+message.content+"</p>")
                }
            });
            $("#send").click(function () {
                //展示到聊天框中
                var m = new Date().getMinutes()
                var s = new Date().getSeconds()
                var h = new Date().getHours()
                var time = h + ":" + m + ":" + s;
                html = editor.html();
                $("#show").append("<p id=\"3\" style=\"margin-left: 580px;color: #00bbff\">我"+time+"</p>"+
                    "<p style=\"margin-left: 620px;color: #1b6d85\">"+html+"</p>")
                goEasy.publish({
                    channel: "bbb", //替换为您自己的channel
                    message: html //替换为您想要发送的消息内容
                });
                editor.html("");
            })
        })
    </script>
</head>
<body>
  <div style="float: left">
      <div id="show" style="width: 700px; overflow-y:auto;height: 300px;border: 1px solid burlywood"></div>
      <textarea id="bbb" name="content" style="width:700px;height:300px;">
      </textarea>
      <button id="send" style="margin-left: 650px;width: 50px;height: 30px">发送</button>
  </div>
</body>
</html>