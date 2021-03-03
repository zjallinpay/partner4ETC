//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form', 'laydate'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;


    //日期控件
    laydate.render({
        elem: '#createTimeStart',
        type: 'date',
        trigger: 'click'
    });

    laydate.render({
        elem: '#createTimeEnd',
        type: 'date',
        trigger: 'click'
    });

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#allinpayDataTable',
            //请求地址
            url: '/manage/activitydataAllinPay/queryCondition',
            //是否分页
            page: true,
            //请求参数
            where: {
                acBatchId: $.trim($("#acBatchId").val()),
                acMerchantName: $.trim($("#acMerchantName").val()),
                createTimeStart: $.trim($("#createTimeStart").val()),
                createTimeEnd: $.trim($("#createTimeEnd").val()),
                acMerchantId: $.trim($("#acMerchantId").val()),
                acExchangeStatues: $.trim($("#acExchangeStatues").val())
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
                {field: 'acBatchId', width: 90, title: '活动编号'},
                {field: 'acPayNum', width: 150, title: '交易号'},
                {field: 'acPayNumex', width: 150, title: '交易号（补充）'},
                {field: 'acMerchantId', width: 120, title: '商户号'},
                {field: 'acMerchantName', width: 120, title: '门店名称'},
                {field: 'acEquimentId', width: 120, title: '终端号'},
                {field: 'acBankName', title: '发卡行', width: 150},
                {field: 'acAccountType', title: '账号类型', width: 120},
                {field: 'acAccountNum', title: '账号', width: 150},
                {field: 'acExchangeType', title: '交易类型', width: 100},
                {field: 'acExchangeStatues', title: '交易状态', width: 120},
                {field: 'acExchangeSourcenum', width: 100, title: '交易初始金额'},
                {field: 'acExchangeRealnum', width: 100, title: '交易实付金额'},
                {field: 'acDiscountNum', width: 100, title: '交易优惠金额'},
                {field: 'acOrganPaynum', width: 100, title: '合作方出资额'},
                {field: 'acMerchantPaynum', width: 100, title: '商户出资额'},
                {field: 'acCooporganId', width: 120, title: '合作方id'},
                {field: 'createTime', width: 120, title: '创建时间'}
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
        $("#acMerchantName").val("");
        $("#createTimeStart").val("");
        $("#createTimeEnd").val("");
        $("#acMerchantId").val("");
        $("#acExchangeStatues").val("");
    });


    layui.use(["upload"], function() {
        var loading =null;
        var upload = layui.upload;
        upload.render({
            elem: "#batchAddBtn",//导入id
            url: "/manage/activitydataAllinPay/batchImport",
            field:'multipartFile',
            size: '20480',
            accept: "file",
            exts: 'xls|xlsx|csv',
            method:'post',
            contentType:'multipart/form-data;charset=utf-8',
            choose: function(obj){
                loading=layer.load(1, {
                    shade: false
                });
            },
            done: function (result) {
                layer.close(loading);
                if (result.code == "00000") {
                    layer.alert("批导成功，共"+result.data+"条。");
                } else {
                    layer.alert("批导失败："+result.msg);
                }
                search();
            }
        });
    });


    //数据清除
    $("#deleteAll").on("click", function () {
        var getUrl='/manage/activitydataAllinPay/deleteByActId?barchId='+$("#acBatchId").val();
        var index = layer.confirm("确定要清除数据吗？", function () {
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
    });

    //导出商家
    form.on('submit(outputDataFilter)',function (data) {

       // var url = "/manage/merchants/batchOutput";

        var formData ={
            acBatchId: $.trim($("#acBatchId").val()),
            acMerchantName: $.trim($("#acMerchantName").val()),
            createTimeStart: $.trim($("#createTimeStart").val()),
            createTimeEnd: $.trim($("#createTimeEnd").val()),
            acMerchantId: $.trim($("#acMerchantId").val()),
            acExchangeStatues: $.trim($("#acExchangeStatues").val())
        }
        window.console.info("开始处理"+JSON.stringify(formData));
        postExcelFile(formData);
    });



    //监听form表单提交事件 防止页面跳转
    form.on('submit(formFilter)', function (data) {
        return false;
    });
    form.on('submit(queryFilter)', function (data) {
        return false;
    });



    function postExcelFile(params){ //params是post请求需要的参数，url是请求url地址
        var url = "/manage/activitydataAllinPay/batchOutput";
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