<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<style>
    #Yetitle {
        margin-top: -20px;
    }
    #music{
        height: 0px;
        width:0px;
    }
    #musicPlay{
        margin-left: 990px;
    }
</style>
<script>
    $("#album").jqGrid({
        url: "${pageContext.request.contextPath}/album/selectByPage",//用来加载远程数据
        editurl: "${pageContext.request.contextPath}/banner/edit",
        datatype: "json",  //用来指定返回数据类型
        autowidth: true,//自适应父容器
        styleUI: "Bootstrap",
        subGrid:true,
        multiselect: true,
        subGridRowExpanded:function (subGridId,albumId) {
            //subGridId 这是动态表格的id,albumId 这是当前行数据的id
            //生成要作为子表格的table的id，和div的id
            var subGridIdTable=subGridId+ "table";
            var subGridIdDiv=subGridId+ "div";
            //将两个元素添加到该子表格的位置
            $("#"+subGridId).html("<table id='"+subGridIdTable+"'></table>"+
                "<div id='"+subGridIdDiv+"' style='height: 50px'></div>"
            );
            $("#"+subGridIdTable).jqGrid({
                url: "${pageContext.request.contextPath}/chapter/selectByPage?aid="+albumId,//用来加载远程数据
                editurl: "${pageContext.request.contextPath}/chapter/editChapter?aid="+albumId,
                datatype: "json",  //用来指定返回数据类型
                autowidth: true,//自适应父容器
                styleUI: "Bootstrap",
                pager: "#"+subGridIdDiv,
                multiselect: true,
                toolbar:[true,'top'],
                rowNum: 5,
                rowList: [5, 8, 10],
                viewrecords: true,
                colNames: ["ID", "标题", "大小", "时长", "上传时间", "音频", "状态"],   //表格标题
                colModel: [
                    {"name": "id", align: 'center'},
                    {"name": "title", align: 'center', editable: true, editrules: {required: true}},
                    {"name": "size", align: 'center'},
                    {"name": "duration", align: 'center'},
                    {"name": "create_date", align: 'center', editable: true, edittype: 'date', editrules: {required: true}},
                    {"name": "src", align: 'center', edittype: 'file',editable: true},
                    {
                        "name": "status", align: 'center', editable: true,
                        editrules: {required: true},
                        edittype: 'select',
                        editoptions: {
                            value: "免费:免费;付费:付费"
                        }
                    },
                ]
            }).jqGrid("navGrid", "#"+subGridIdDiv, {search:false,addtext:"添加",edittext:"修改",deltext:"删除"},
                {
                    //修改
                    //添加响应成功之后执行的
                    closeAfterEdit: true, //关闭对应的模态框
                    //获取刚刚添加的id，去修改音频的正确路径
                    afterSubmit: function (response) {
                        var id = response.responseJSON.id;
                        if (id!='-1'){
                            $.ajaxFileUpload({
                                url: "${pageContext.request.contextPath}/chapter/append",
                                fileElementId: 'src',
                                data: {id: id},
                                type: "post",
                                success: function () {
                                    jQuery("#"+subGridIdTable).trigger("reloadGrid");
                                }
                            });
                        }
                        return response;
                    }
                },
                {
                    //添加响应成功之后执行的
                    closeAfterAdd: true, //关闭对应的模态框
                    //获取刚刚添加的id，去修改音频的正确路径
                    afterSubmit: function (response) {
                        var id = response.responseJSON.id;
                        $.ajaxFileUpload({
                            url: "${pageContext.request.contextPath}/chapter/append",
                            fileElementId: 'src',
                            data: {id: id},
                            type: "post",
                            success: function () {
                                jQuery("#"+subGridIdTable).trigger("reloadGrid");
                            }
                        });
                        return response;
                    }
                },
                {
                    //删除
                });
            //添加按钮
            $("#t_"+subGridIdTable).html("<button class='btn btn-default' id='musicPlay' onclick=\"play('"+subGridIdTable+"')\"><span id ='token' class='glyphicon glyphicon-play'></span></button>"+
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
                "<button class='btn btn-default' onclick=\"DownLoad('"+subGridIdTable+"')\"><span class='glyphicon glyphicon-arrow-down'></span></button>"
            )
        },
        pager: "#pager",
        rowNum: 5,
        rowList: [5, 8, 10],
        viewrecords: true,
        height: '400px',
        colNames: ["ID", "标题", "作者", "分数", "播音员", "章节数", "专辑简介", "状态", "发行时间", "封面"],   //表格标题
        colModel: [
            {"name": "id", align: 'center'},
            {"name": "title", align: 'center', editable: true, editrules: {required: true}},
            {"name": "author", align: 'center',editable: true, editrules: {required: true}},
            {"name": "score", align: 'center',editable: true, editrules: {required: true}},
            {"name": "broadcaster", align: 'center',editable: true, editrules: {required: true}},
            {"name": "count", align: 'center',editable: true, editrules: {required: true}},
            {"name": "brief", align: 'center',editable: true, editrules: {required: true}},
            {
                "name": "status", align: 'center', editable: true,
                editrules: {required: true},
                edittype: 'select',
                editoptions: {
                    value: "免费:免费;付费:付费"
                }
            },
            {"name": "create_Date", align: 'center', editable: true, edittype: 'date', editrules: {required: true}},
            {
                "name": "img", editable: true, align: 'center', edittype: 'file',
                formatter: function (cellvalue, options, rowObject) {
                    return "<img style='width:60%;height:62px' src='${pageContext.request.contextPath}/album_img/" + cellvalue + "'/>";
                }
            }
        ]
    }).jqGrid("navGrid", "#pager", {});
    //播放
    function play(subGridIdTable) {
        // 判断 用户是否选中一行  未选中->null         选中->被选中行的id
        var gr = $("#"+subGridIdTable).jqGrid('getGridParam', 'selrow');
        if(gr == null){
            alert("请选中要播放的音频");
        }else{
            //2.jqgrid 提供的方法 根据id拿到对应的值
            var data = $("#"+subGridIdTable).jqGrid('getRowData', gr);
            var mp3 = "${pageContext.request.contextPath}/音频_upload/"+data.src;
            var status = $("#music").prop("class");
            if (status=='close'){
                $("#music").prop("src",mp3);
                $("#music").get(0).play(); //播放 mp3这个音频对象
                $("#music").prop("name",mp3);
                $("#music").prop("class","start");
                $("#token").prop("class","glyphicon glyphicon-pause");
            }else{
                var sss = $("#music").prop("name");
                if (mp3==sss){
                    $("#music").get(0).pause(); //暂停
                    $("#music").prop("class","close");
                    $("#token").prop("class","glyphicon glyphicon-play");
                }else{
                    $("#music").prop("src",mp3);
                    $("#music").get(0).play(); //播放 mp3这个音频对象
                    $("#music").prop("name",mp3);
                    $("#token").prop("class","glyphicon glyphicon-pause");
                }
            }
        }
    }
    //下载
    function DownLoad(subGridIdTable){
        // 判断 用户是否选中一行  未选中->null         选中->被选中行的id
        var gr = $("#"+subGridIdTable).jqGrid('getGridParam', 'selrow');
        if(gr == null){
            alert("请选中要下载的音频");
        }else{
            //2.jqgrid 提供的方法 根据id拿到对应的值
            var data = $("#"+subGridIdTable).jqGrid('getRowData', gr);
            window.location.href="${pageContext.request.contextPath}/chapter/download?src="+data.src;
        }
    }
</script>
<div class="page-header" id="Yetitle">
    <h1>专辑与章节管理</h1>
</div>
<div>
    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab">专辑与章节信息</a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="home">
            <div class="panel panel-default">
                <div class="panel-body">
                    <table id="album"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="pager" style="height: 50px"></div>
<audio src="" id="music"></audio>