<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<style>
    #Yetitle {
        margin-top: -20px;
    }
</style>
<script>
    $("#bannerList").jqGrid({
        url: "${pageContext.request.contextPath}/banner/selectByPage",//用来加载远程数据
        editurl: "${pageContext.request.contextPath}/banner/edit",
        datatype: "json",  //用来指定返回数据类型
        autowidth: true,//自适应父容器
        styleUI: "Bootstrap",
        pager: "#page",
        rowNum: 5,
        rowList: [5, 8, 10],
        viewrecords: true,
        height: '400px',
        multiselect: true,
        colNames: ["ID", "标题", "状态", "描述", "创建时间", "图片"],   //表格标题
        colModel: [
            {"name": "id", align: 'center'},
            {"name": "title", align: 'center', editable: true, editrules: {required: true}},
            {
                "name": "status", align: 'center', editable: true,
                editrules: {required: true},
                edittype: 'select',
                editoptions: {
                    value: "展示:展示;不展示:不展示"
                }
            },
            {"name": "other", align: 'center', editable: true, editrules: {required: true}},
            {"name": "create_date", align: 'center', editable: true, edittype: 'date', editrules: {required: true}},
            {
                "name": "img", editable: true, align: 'center', edittype: 'file',
                formatter: function (cellvalue, options, rowObject) {
                    return "<img style='width:60%;height:62px' src='${pageContext.request.contextPath}/image_upload/" + cellvalue + "'/>";
                }
            }
        ]
    }).jqGrid("navGrid", "#page", {},
        {
            //修改响应成功之后执行的
            closeAfterEdit: true, //关闭对应的模态框
            //获取刚刚添加的id，去修改图片的正确路径
            afterSubmit: function (response) {
                var id = response.responseJSON.id;
                if (response.responseJSON.token != 'ok') {
                    $.ajaxFileUpload({
                        url: "${pageContext.request.contextPath}/banner/updateSrc",
                        fileElementId: 'img',
                        data: {bannerId: id},
                        type: "post",
                        success: function () {
                            jQuery("#bannerList").trigger("reloadGrid");
                            $("#ur").show();
                            setTimeout(function () {
                                $("#ur").hide();
                            }, 3000);
                        }
                    })
                } else {
                    jQuery("#bannerList").trigger("reloadGrid");
                    $("#ur").show();
                    setTimeout(function () {
                        $("#ur").hide();
                    }, 3000);
                }
                return response;
            },
        },
        {
            //添加响应成功之后执行的
            closeAfterAdd: true, //关闭对应的模态框
            //获取刚刚添加的id，去修改图片的正确路径
            afterSubmit: function (response) {
                var id = response.responseJSON.id;
                $.ajaxFileUpload({
                    url: "${pageContext.request.contextPath}/banner/updateSrc",
                    fileElementId: 'img',
                    data: {bannerId: id},
                    type: "post",
                    success: function () {
                        jQuery("#bannerList").trigger("reloadGrid");
                        $("#ir").show();
                        setTimeout(function () {
                            $("#ir").hide();
                        }, 3000);
                    }
                });
                return response;
            }
        },{
            //删除操作
            afterComplete: function (response) {
                $("#dr").show();
                setTimeout(function () {
                    $("#dr").hide();
                }, 3000);
            }
        }
    );
    function poi() {
        $.ajax({
            url:"${pageContext.request.contextPath}/banner/outPoi",
            type:"POST",
            success:function (date) {
                alert("导出成功")
            }
        })
    }
    function inPoi() {
        $('#inbannerModel').modal('show');
        <%--$.ajax({--%>
            <%--url:"${pageContext.request.contextPath}/banner/inPoi",--%>
            <%--type:"POST",--%>
            <%--success:function (date) {--%>
                <%--alert("导出成功")--%>
            <%--}--%>
        <%--})--%>
    }
</script>
<div class="page-header" id="Yetitle">
    <h1>轮播图管理</h1>
</div>
<div class="alert alert-warning" style="display:none" id="ir">数据添加成功</div>
<div class="alert alert-success" style="display:none" id="ur">数据修改成功</div>
<div class="alert alert-success" style="display:none" id="dr">数据删除成功</div>
<div>
    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active">
            <a href="#home" aria-controls="home" role="tab" data-toggle="tab">轮播图列表</a>
        </li>
        <li role="presentation">
            <a href="javascript:void(0)" onclick="poi()">轮播图导出</a>
        </li>
        <li role="presentation">
            <a class="btn btn-default" onclick="inPoi()">轮播图导入</a>
        </li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="home">
            <div class="panel panel-default">
                <div class="panel-body">
                    <table id="bannerList"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="page" style="height: 50px"></div>
<div class="modal fade bs-example-modal" role="dialog" id=inbannerModel">
    <div class="modal-dialog modal" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">轮播图导入</h4>
            </div>
            <div class="modal-body">
                <form id="form">
                    <input type="file"/>
                    <input type="button" value="请选择要导入的Excel"/>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">退出</button>
            </div>
        </div>
    </div>
</div>