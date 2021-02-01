//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;


 /*   laydate.render({
        elem: '#ablestartTime',
        type: 'datetime',
        trigger: 'click'
    });

    laydate.render({
        elem: '#ableendTime',
        type: 'datetime',
        trigger: 'click'
    });
*/

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#orgTable',
            //请求地址
            url: '/manage/organ/queryCondition',
            //是否分页
            page: true,
            //请求参数
            where: {
                ogName: $.trim($("#ogName").val()),
                ogType: $.trim($("#ogType").val())
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
                {field: 'ogId', width: 90, title: '机构ID'},
                {field: 'ogName', width: 150, title: '机构名称'},
                {field: 'ogType', width: 120, title: '机构类型'},
                {field: 'remark', title: '备注', width: 150},
                {fixed: 'right', title: '操作', toolbar: '#operator'}
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
        $("#ogName").val("");
        $("#ogType").val("");
    });




    //新增项目
    $("#addBtn").on("click", function () {
        openModal("新增合作机构", "addForm");
        $("#addForm").find("input[name='ogName']").attr('readonly',false);
        $("#addForm")[0].reset();
        form.render();
    });


    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;



        //编辑
        if (obj.event === 'edit') {
            openModal("编辑机构", "addForm");
            $("#addForm").find("input[name='ogName']").val(myData.ogName);
            $("#addForm").find("input[name='ogName']").attr('readonly',true);

            $("#addForm").find("input[name='ogType']").val(myData.ogType);

            $("#addForm").find("input[name='remark']").val(myData.remark);

            $("#addForm").find("input[name='ogId']").val(myData.ogId);

            form.render();
        }

        //删除
        if (obj.event === 'delete') {
            var getUrl='/manage/organ/deteleById?ogId='+myData.ogId;
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
    form.on('submit(saveOrganFilter)',function (data) {
        window.console.info("开始处理");
        var formData = new FormData(document.getElementById("addForm"));
        var url = '/manage/organ/saveOrUpdata';
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