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
            elem: '#qrCodeTable',
            //请求地址
            url: '/manage/qrCode/getMchtInfo',
            //是否分页
            page: true,
            //请求参数
            where: {
                mchtId: $.trim($("#mchtId").val()),
                mchtName: $.trim($("#mchtName").val())
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

            //每页展示的条数
            limits: [5, 10, 20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'mchtId', title: '商户号',width: 180},
                {field: 'mchtName', title: '商户名称', width: 200},
                {field: 'appId', title: 'APPID',width: 100},
                {field: 'appKey', title: 'APPKEY',width: 120},
                {field: 'partnerModel', title: '合作方模式',width: 120},
                {field: 'orgId', title: '合作方编号',width: 120},
                {field: 'status', title: '状态',width: 100},
                {field: 'updateTime', title: '创建时间',width: 100},
                {title: 'right', title: '操作', toolbar: '#qrCodeToolbar',width: 200}
            ]],
            done: function () {
                $("tbody td[data-field='status']").children().each(function (index, val) {
                    if ($(this).text() == "1") {
                        $(this).text("有效");
                    } else if ($(this).text() == "0") {
                        $(this).text("禁用");
                    }
                })
                $("tbody td[data-field='partnerModel']").children().each(function (index, val) {
                    if ($(this).text() == "1") {
                        $(this).text("是");
                    } else if ($(this).text() == "0") {
                        $(this).text("否");
                    }
                })
            }
        });
    };
    //页面加载就查询列表
    search();
    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });

    $("#addBtn").on("click", function () {
        openModal("新增", "addForm");
        $("#addForm")[0].reset();
        form.render();
    });


    layui.use(["upload"], function() {
        var upload = layui.upload;//主要是这个
        upload.render({
            elem: "#multipartFile",//导入id
            url: "/manage/qrCode/batchAdd",
            field:'multipartFile',
            size: '2048',
            accept: "file",
            exts: 'xls|xlsx',
            method:'post',
            contentType:'multipart/form-data;charset=utf-8',
            done: function (result) {
                if (result.code == "00000") {
                    layer.alert("批导成功，共"+result.data+"条。");
                } else {
                    layer.alert("批导失败："+result.msg);
                }
                search();
            }
        });
    });
    //模板下载
    $("#tempdown").on("click", function () {
        window.location.href = "/manage/qrCode/downloadTemplate";
    });

    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        var lineData = obj.data;
        //下载文件
        if (obj.event === 'download') {
            window.location.href = "/manage/qrCode/downloadQrCodeImg?mchtId="+lineData.mchtId;
        }
        //编辑商户
        if (obj.event=='edit'){
            openModal("编辑商户", "addForm");
            $("#addForm").find("input[name='mchtId']").val(lineData.mchtId);
            $("#addForm").find("input[name='mchtName']").val(lineData.mchtName);
            $("#addForm").find("input[name='appId']").val(lineData.appId);
            $("#addForm").find("input[name='appKey']").val(lineData.appKey);
            $("#addForm").find("input[name='partnerModel']").val(lineData.partnerModel);
            $("#addForm").find("input[name='orgId']").val(lineData.orgId);
            $("#addForm").find("input[name='option']").val('edit');
        }
        if (obj.event=='forbidden'){
            lineData.status = '0';
            $.ajax({
                url: '/manage/qrCode/edit',
                type: 'post',
                contentType:'application/json',
                dataType: 'json',
                data: JSON.stringify(lineData),
                success:function (data) {
                    if (data.code='00000'){
                        layer.alert("禁用成功");
                    } else {
                        layer.alert("禁用失败");
                    }
                    search();
                }

            });
        }

        if (obj.event=='open'){
            lineData.status = '1';
            $.ajax({
                url: '/manage/qrCode/edit',
                type: 'post',
                contentType:'application/json',
                dataType: 'json',
                data: JSON.stringify(lineData),
                success:function (data) {
                    if (data.code='00000'){
                        layer.alert("启用成功",function () {
                            layer.closeAll();
                        });
                    } else {
                        layer.alert("启用失败");
                    }
                    search();
                }

            });
        }
    });


    form.on('submit(queryFilter)', function (data) {
        return false;
    });

    //打开模态框
    function openModal(operateName, modalName) {
        layer.open({
            title: operateName,
            content: $('#' + modalName),
            area: ['500px', '500px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1
        });
    }

    form.on('submit(saveMerchantFilter)',function (data) {
        window.console.info("开始处理");
        var params = data.field;
        var url = '/manage/qrCode/add';
        if (params.option=='edit')
            url = '/manage/qrCode/edit';
        $.ajax({
            type:'post',
            url:url,
            contentType :'application/json',
            data:JSON.stringify(params),
            dataType:'json',
            success:function (res) {
                window.console.info(JSON.stringify(res));
                if (res.code=="00000"){
                    layer.alert("操作成功",function () {
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert("操作失败："+res.msg);
                }
            }
        });
        return false;
    });

    // $('#cancelAdd').on('click',function () {
    //     layer.closeAll();
    // })


});
