//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;



    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#agreementTable',
            //请求地址
            url: '/manage/agreement/queryCondition',
            //是否分页
            page: true,
            //请求参数
            where: {
                argId: $.trim($("#argId").val()),
                coopOrgtype: $.trim($("#coopOrgtype").val()),
                argName: $.trim($("#argName").val())
            },
            //分页信息
            request: {
                pageName: 'pageNo',
                limitName: 'pageSize'
            },
            //处理返回参数
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.count,
                    "data": res.data
                };
            },
            //设置返回的属性值，依据此值进行解析
            response: {
                statusName: 'code',
                statusCode: "00000",
                msgName: 'msg',
                dataName: 'data'
            },
            //每页展示的条数
            limits: [5, 10, 20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'argId', width: 90, title: '协议ID'},
                {field: 'argName', width: 200, title: '协议名称'},
                {field: 'coopOrgtype', width: 120, title: '合作机构类型'},
                {field: 'createTime', width: 130, title: '添加日期'},
                {
                    field: 'activityFile',
                    title: '附件材料',
                    templet: '<div>{{# if( d.argFile == null){ }} 无    {{# } else { }}<a class="iconfont icon-chakanbaogao" style="margin-left: 3%; color: #2196F3;border-radius: 5px; cursor: pointer;text-decoration: underline;" target="_blank" lay-event="downloadattch">  下载 </a>{{# } }}</div>'
                },
                {fixed: 'right', title: '操作', toolbar: '#operator', width: 120}
            ]]
        });
    };
    //页面加载就查询列表
    search();

    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });

    //重置参数
    $("#resetBtn").on("click", function () {
        $("#argId").val("");
        $("#coopOrgtype").val("");
        $("#argName").val("");
    });




    //新增活动
    $("#addBtn").on("click", function () {
        openModal("新增协议", "addForm");
        $("#addForm").find("input[name='argName']").attr('readonly',false);
        $("#addForm")[0].reset();
        form.render();
    });



    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;

        //下载附件
        if (obj.event ==='downloadattch'){
            window.location.href = "/manage/agreement/downloadArgFile?argId="+myData.argId;
        }
        //编辑
        if (obj.event === 'edit') {
            openModal("编辑协议", "addForm");
            $("#addForm").find("input[name='argName']").attr('readonly',true);
            $("#addForm").find("input[name='argName']").val(myData.argName);

            $("#addForm").find("input[name='coopOrgtype']").val(myData.coopOrgtype);
            $("#addForm").find("input[name='argId']").val(myData.argId);
            form.render();
        }

        //删除
        if (obj.event === 'delete') {
            var getUrl='/manage/agreement/deteleById?argId='+myData.argId;
            var index = layer.confirm("确定要删除吗？", function () {
                $.ajax({
                    url: getUrl,
                    type: 'get',
                    data: '',
                    dataType: 'json',
                    success: function (data) {
                        layer.close(index);
                        if (data.code == "00000") {
                            search();
                        } else {
                            layer.alert(data.msg);
                        }
                    },
                    error: function () {
                        layer.alert("操作失败，请重试！");
                    }
                });
            })
        }

    });




    //提交新增编辑表单
    form.on('submit(saveActivityFilter)',function (data) {
        window.console.info("开始处理");

        var formData = new FormData(document.getElementById("addForm"));
        var url = '/manage/agreement/saveOrUpdata';
        $.ajax({
            url: url,
            type: 'post',
            contentType: false,
            processData: false,
            data: formData,
            dataType: 'json',
            success: function (data) {
                if (data.code == "00000") {
                    layer.alert("操作成功",function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function () {
                layer.alert("操作失败，请重试！");
            }
        });

        return false;
    });



    //监听form表单提交事件 防止页面跳转
    form.on('submit(formFilter)', function (data) {
        return false;
    });
    form.on('submit(queryFilter)', function (data) {
        return false;
    });

    //打开模态框
    function openModal(operateName, modalName) {
        layer.open({
            title: operateName,
            content: $('#' + modalName),
            area: ['850px', '450px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1
        });
    }

    function postFile(params){ //params是post请求需要的参数，url是请求url地址
        var url = "/manage/activity/downloadActFile";
        var form = document.createElement("form");
        form.style.display = 'none';
        form.action = url;
        form.method = "post";
        form.enctype="multipart/form-data";
        document.body.appendChild(form);
        for (var key in params) {
            var input = document.createElement("input");
            input.type = "hidden";
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }
        form.submit();
        form.remove();
    }


});