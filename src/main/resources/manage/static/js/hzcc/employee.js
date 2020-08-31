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
            elem: '#employeeTable',
            //请求地址
            url: '/manage/employee/list',
            //是否分页
            page: true,
            //请求参数
            where: {
                branchId: $.trim($("#branchId").val()),
                branchName: $.trim($("#branchName").val()),
                status: $.trim($("#status").val()),
                employeeId: $.trim($("#employeeId").val()),
                employeeName: $.trim($("#employeeName").val()),
                tlCustId: $.trim($("#tlCustId").val())
            },
            //分页信息
            request: {
                pageName: 'pageNum',
                limitName: 'pageSize'
            },
            //处理返回参数
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.list
                };
            },
            //设置返回的属性值，依据此值进行解析
            response: {
                statusName: 'code',
                statusCode: "00000",
                msgName: 'msg',
                dataName: 'data'
            },
            done: function () {
                $("tbody td[data-field='status']").children().each(function (index, val) {
                    if ($(this).text() == "0") {
                        $(this).text("有效");
                    } else if ($(this).text() == "1") {
                        $(this).text("禁用");
                    }
                })
            },
            //每页展示的条数
            limits: [5, 10, 20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'branchId', title: '公司编号'},
                {field: 'branchName', title: '公司名称', width: 200},
                {field: 'employeeId', title: '员工编号'},
                {field: 'employeeName', title: '员工姓名'},
                {field: 'status', title: '状态'},
                {field: 'tlCustId', title: '通联商户号', width: 200},
                {field: 'kid', title: '主键', hide: true},
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

    $("#addBtn").on("click", function () {
        // $("#addForm").find("input[name='branchId']").attr('disabled',true);
        // $("#addForm").find("input[name='tlCustId']").attr('disabled',true);
        $("#addForm").find("input[name='branchId']").val("");
        $("#addForm").find("input[name='branchName']").val("");
        $("#addForm").find("input[name='employeeId']").val("");
        $("#addForm").find("input[name='employeeName']").val("");
        $("#addForm").find("input[name='tlCustId']").val("");
        openModal("新增", "addForm");
    });

    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;
        //审核
        if (obj.event === 'edit') {
            //form表单初始化
            form.val("editFilter", {
                "kid": myData.kid,
                "branchId": myData.branchId,
                "branchName": myData.branchName,
                "employeeId": myData.employeeId,
                "employeeName": myData.employeeName,
                "tlCustId": myData.tlCustId
            });
            //打开模态框
            openModal("编辑", "editForm");
        } else if (obj.event === 'forbidden') {
            var btnName = $(this).html();
            var status;
            if (btnName == "禁用") {
                status = "1";
            } else {
                status = "0";
            }
            var index = layer.confirm("确定置为" + btnName + "吗？", function () {
                $.ajax({
                    url: '/manage/employee/status',
                    type: 'post',
                    data: {
                        kid: myData.kid,
                        status: status
                    },
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

    //监听form表单提交事件 防止页面跳转
    form.on('submit(addFilter)', function (data) {
            $.ajax({
                url: '/manage/employee/add',
                type: 'post',
                data: {
                    branchId: $.trim($("#addForm").find("input[name='branchId']").val()),
                    branchName: $.trim($("#addForm").find("input[name='branchName']").val()),
                    employeeId: $.trim($("#addForm").find("input[name='employeeId']").val()),
                    employeeName: $.trim($("#addForm").find("input[name='employeeName']").val()),
                    tlCustId: $.trim($("#addForm").find("input[name='tlCustId']").val())
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == "00000") {
                        var index = layer.alert("保存成功", function () {
                            layer.closeAll();
                            search();
                        });
                    } else {
                        layer.alert(data.msg);
                    }
                },
                error: function () {
                    layer.alert("新增失败，请重试！");
                }
            });
        return false;
    });
    form.on('submit(editFilter)', function (data) {
        $.ajax({
            url: '/manage/employee/edit',
            type: 'post',
            data: {
                kid: $.trim($("#editForm").find("input[name='kid']").val()),
                branchId: $.trim($("#editForm").find("input[name='branchId']").val()),
                branchName: $.trim($("#editForm").find("input[name='branchName']").val()),
                employeeId: $.trim($("#editForm").find("input[name='employeeId']").val()),
                employeeName: $.trim($("#editForm").find("input[name='employeeName']").val()),
                tlCustId: $.trim($("#editForm").find("input[name='tlCustId']").val())
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == "00000") {
                    var index = layer.alert("编辑成功", function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function () {
                layer.alert("编辑失败，请重试！");
            }
        });
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
            area: ['500px', '400px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1
        });
    }
});
