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
            elem: '#discountConfigTable',
            //请求地址
            url: '/manage/discountConfig/list',
            //是否分页
            page: true,
            //请求参数
            where: {
                discountId: $.trim($("#discountId").val()),
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
                {field: 'discountId', title: '活动号'},
                {field: 'discountName', title: '活动名称'},
                {field: 'status', title: '活动状态'},
                {field: 'discountstartDate', title: '优惠开始时间'},
                {field: 'discountendDate', title: '优惠结束时间'},
                {field: 'discountRate', title: '折扣比例'},
                {field: 'singlemaxDiscount', title: '单笔最大折扣'},
                {field: 'discountmaxTimes', title: '参与次数限制'},
                {field: 'remark', title: '备注'},
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
        // $("#addForm").find("input[name='kid']").val("");
        $("#addForm").find("input[name='discountId']").val("");
        $("#addForm").find("input[name='discountName']").val("");
        $("#addForm").find("input[name='discountstartDate']").val("");
        $("#addForm").find("input[name='discountendDate']").val("");
        $("#addForm").find("input[name='discountRate']").val("");
        $("#addForm").find("input[name='singlemaxDiscount']").val("");
        $("#addForm").find("input[name='discountmaxTimes']").val("");
        $("#addForm").find("input[name='remark']").val("");
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
                "discountId": myData.discountId,
                "discountName": myData.discountName,
                "discountstartDate": myData.discountendDate,
                "discountendDate": myData.discountendDate,
                "discountRate": myData.discountRate,
                "singlemaxDiscount": myData.singlemaxDiscount,
                "discountmaxTimes": myData.discountmaxTimes,
                "remark": myData.remark,
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
                    url: '/manage/discountConfig/status',
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
            url: '/manage/discountConfig/add',
            type: 'post',
            data: {
                // kid: $.trim($("#editForm").find("input[name='kid']").val()),
                discountId: $.trim($("#addForm").find("input[name='discountId']").val()),
                discountName: $.trim($("#addForm").find("input[name='discountName']").val()),
                discountstartDate: $.trim($("#addForm").find("input[name='discountstartDate']").val()),
                discountendDate: $.trim($("#addForm").find("input[name='discountendDate']").val()),
                discountRate: $.trim($("#addForm").find("input[name='discountRate']").val()),
                singlemaxDiscount: $.trim($("#addForm").find("input[name='singlemaxDiscount']").val()),
                discountmaxTimes: $.trim($("#addForm").find("input[name='discountmaxTimes']").val()),
                remark: $.trim($("#addForm").find("input[name='remark']").val()),
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
        var mydata = data.kid;
        $.ajax({
            url: '/manage/discountConfig/edit',
            type: 'post',
            data: {
                kid: $.trim($("#editForm").find("input[name='kid']").val()),
                discountId: $.trim($("#editForm").find("input[name='discountId']").val()),
                discountName: $.trim($("#editForm").find("input[name='discountName']").val()),
                discountstartDate: $.trim($("#editForm").find("input[name='discountstartDate']").val()),
                discountendDate: $.trim($("#editForm").find("input[name='discountendDate']").val()),
                discountRate: $.trim($("#editForm").find("input[name='discountRate']").val()),
                singlemaxDiscount: $.trim($("#editForm").find("input[name='singlemaxDiscount']").val()),
                discountmaxTimes: $.trim($("#editForm").find("input[name='discountmaxTimes']").val()),
                remark: $.trim($("#editForm").find("input[name='remark']").val()),
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
