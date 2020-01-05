<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<style>
    #Yetitle {
        margin-top: -20px;
    }
    #edit{
        margin-left: 1100px;
    }
</style>
<script>
    var editor=null;
    $(function () {
         KindEditor.create('#myContent',{
             filePostName:'img',       //上传是后台接收的名字
             uploadJson:'${pageContext.request.contextPath}/article/uploadImg', //上传后台的路径
             allowFileManager:true,    //是否展示 图片空间
             fileManagerJson:"${pageContext.request.contextPath}/article/getAllImgs",
             width:'850px',
             height:'500px',
             resizeType:0,
             afterCreate : function() {
                 this.sync();
             },
             afterBlur:function(){
                 this.sync();
             }
         });
        editor=KindEditor.create('#updateContent',{
            filePostName:'img',       //上传是后台接收的名字
            uploadJson:'${pageContext.request.contextPath}/article/uploadImg', //上传后台的路径
            allowFileManager:true,    //是否展示 图片空间
            fileManagerJson:"${pageContext.request.contextPath}/article/getAllImgs",
            width:'850px',
            height:'500px',
            resizeType:0,
            afterCreate : function() {
                this.sync();
            },
            afterBlur:function(){
                this.sync();
            }
        });
    })
    $("#articleList").jqGrid({
        url: "${pageContext.request.contextPath}/article/selectByPage",//用来加载远程数据
        editurl: "${pageContext.request.contextPath}/article/edit",
        datatype: "json",  //用来指定返回数据类型
        autowidth: true,//自适应父容器
        styleUI: "Bootstrap",
        pager: "#page",
        rownumbers:true,
        rowNum: 5,
        rowList: [5, 8, 10],
        viewrecords: true,
        height: '400px',
        toolbar:[true,"top"],
        multiselect: true,
        colNames: ["ID", "标题", "作者", "上师id", "发布时间", "状态"],   //表格标题
        colModel: [
            {"name": "id", align: 'center'},
            {"name": "title", align: 'center', editable: true, editrules: {required: true}},
            {"name": "author", align: 'center', editable: true, editrules: {required: true}},
            {"name": "guru_Id", align: 'center', editable: true, editrules: {required: true}},
            {"name": "create_Date", align: 'center', editable: true, edittype: 'date', editrules: {required: true}},
            {
                "name": "status", align: 'center', editable: true,
                editrules: {required: true},
                edittype: 'select',
                editoptions: {
                    value: "上架:上架;下架:下架"
                }
            }
        ]
    }).jqGrid("navGrid", "#page", {add:false,edit:false,search:false},
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
        }
    );
    $("#t_articleList").html(
        "<a id='edit'><span class='glyphicon glyphicon-th-list'></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
        "<a id='teach'><span class='glyphicon glyphicon-pencil'></span></a>"
    );
    function add() {
        $('#myModel').modal('show');
    }
    function insert() {
        var serialize = $("#myForm").serialize();
        $.ajax({
            url:"${pageContext.request.contextPath}/article/insert",
            data:serialize,
            type:"post",
            datatype:"json",
            success:function (date) {
                //刷新表格
                jQuery("#articleList").trigger("reloadGrid");
               alert(date.toten);
            }
        })
    }
    $("#teach").click(function () {
        var gr = $("#articleList").jqGrid('getGridParam', 'selrow');
        if(gr == null){
            alert("请选中要修改的文章");
        }else{
            $.ajax({
                url:"${pageContext.request.contextPath}/article/selectById",
                data:"id="+gr,
                type:"post",
                datatype:"json",
                success:function (date) {
                    $("#teachContent").empty();
                    $("#teachContent").append($("<div>"+date.content+"</div>"));
                    $('#MyteachModel').modal('show');
                }
            })
        }
    })
    //数据回显
    $("#edit").click(function () {
        var gr = $("#articleList").jqGrid('getGridParam', 'selrow');
        if(gr == null){
            alert("请选中要修改的文章");
        }else{
            $.ajax({
                url:"${pageContext.request.contextPath}/article/selectById",
                data:"id="+gr,
                type:"post",
                datatype:"json",
                success:function (date) {
                    $("#exampleid1").val(date.id);
                    $("#exampleInputName1").val(date.title);
                    $("#exampleInputEmail1").val(date.author);
                    $("#exampleInputTime1").val(date.create_Date);
                    $("#inputStatus1").val(date.status);
                    editor.html(date.content);
                    $('#updateModel').modal('show');
                }
            })
        }
    })
    //修改
    function update() {
        var serialize = $("#myForm2").serialize();
        $.ajax({
            url:"${pageContext.request.contextPath}/article/update",
            data:serialize,
            type:"post",
            datatype:"json",
            success:function (date) {
                jQuery("#articleList").trigger("reloadGrid");
            }
        })
    }
</script>
<div class="page-header" id="Yetitle">
    <h1>文章管理</h1>
</div>
<div>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active">
            <a href="#home" aria-controls="home" role="tab" data-toggle="tab">文章信息</a>
        </li>
        <li role="presentation">
            <a class="btn btn-default" onclick="add()">添加文章</a>
        </li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="home">
            <div class="panel panel-default">
                <div class="panel-body">
                    <table id="articleList"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="page" style="height: 50px"></div>
<div class="modal fade  bs-example-modal-lg" role="dialog" id="myModel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">添加文章</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline" id="myForm">
                    <div class="form-group">
                        <label for="exampleInputName2">标题</label>
                        <input type="text" name="title" class="form-control" id="exampleInputName2">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail2">作者</label>
                        <input type="text" name="author" class="form-control" id="exampleInputEmail2">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputTime">发布时间</label>
                        <input type="date" name="create_Date" class="form-control" id="exampleInputTime">
                    </div>
                    <div class="form-group">
                        <label for="inputStatus">状态</label>
                        <select class="form-control" id="inputStatus" name="status">
                            <option value="上架">上架</option>
                            <option value="下架">下架</option>
                        </select>
                    </div>
                    <%--<div class="form-group">--%>
                        <%--<label for="inputTitle" class="col-sm-2 control-label">标题</label>--%>
                        <%--<div class="col-sm-6">--%>
                            <%--<input type="text" class="form-control" id="inputTitle">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label for="inputAutoer" class="col-sm-2 control-label">作者</label>--%>
                        <%--<div class="col-sm-6">--%>
                            <%--<input type="text" class="form-control" id="inputAutoer">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label for="inputTime" class="col-sm-2 control-label">发布时间</label>--%>
                        <%--<div class="col-sm-6">--%>
                            <%--<input type="date" class="form-control" id="inputTime">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                        <%--<label for="inputStatus" class="col-sm-2 control-label">状态</label>--%>
                        <%--<div class="col-sm-6">--%>
                            <%--<select class="form-control" id="inputStatus">--%>
                                <%--<option value="上架">上架</option>--%>
                                <%--<option value="下架">下架</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <textarea id="myContent" name="content"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="insert()">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<div class="modal fade  bs-example-modal-lg" role="dialog" id="updateModel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">修改文章</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline" id="myForm2">
                    <div class="form-group hidden">
                        <label for="exampleid1">id</label>
                        <input type="text" name="id" class="form-control" id="exampleid1">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputName1">标题</label>
                        <input type="text" name="title" class="form-control" id="exampleInputName1">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputEmail1">作者</label>
                        <input type="text" name="author" class="form-control" id="exampleInputEmail1">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputTime1">发布时间</label>
                        <input type="date" name="create_Date" class="form-control" id="exampleInputTime1">
                    </div>
                    <div class="form-group">
                        <label for="inputStatus1">状态</label>
                        <select class="form-control" id="inputStatus1" name="status">
                            <option value="上架">上架</option>
                            <option value="下架">下架</option>
                        </select>
                    </div>
                    <%--<div class="form-group">--%>
                    <%--<label for="inputTitle" class="col-sm-2 control-label">标题</label>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<input type="text" class="form-control" id="inputTitle">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="inputAutoer" class="col-sm-2 control-label">作者</label>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<input type="text" class="form-control" id="inputAutoer">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="inputTime" class="col-sm-2 control-label">发布时间</label>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<input type="date" class="form-control" id="inputTime">--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="form-group">--%>
                    <%--<label for="inputStatus" class="col-sm-2 control-label">状态</label>--%>
                    <%--<div class="col-sm-6">--%>
                    <%--<select class="form-control" id="inputStatus">--%>
                    <%--<option value="上架">上架</option>--%>
                    <%--<option value="下架">下架</option>--%>
                    <%--</select>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <textarea id="updateContent" name="content"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="update()">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<div class="modal fade  bs-example-modal-lg" role="dialog" id="MyteachModel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">文章内容</h4>
            </div>
            <div class="modal-body">
                <div id="teachContent"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">退出</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>