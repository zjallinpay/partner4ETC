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
        elem: '#acPaytimeStart',
        type: 'date',
        trigger: 'click'
    });

    laydate.render({
        elem: '#acPaytimeEnd',
        type: 'date',
        trigger: 'click'
    });

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#wechatDataTable',
            //请求地址
            url: '/manage/activitydatawechat/queryCondition',
            //是否分页
            page: true,
            //请求参数
            where: {
                acBatchId: $.trim($("#acBatchId").val()),
                acExchangeType: $.trim($("#acExchangeType").val()),
                acPaytimeStart: $.trim($("#acPaytimeStart").val()),
                acPaytimeEnd: $.trim($("#acPaytimeEnd").val()),
                acPaymerchantId: $.trim($("#acPaymerchantId").val())
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
                {field: 'acBatchId', width: 90, title: '批次id'},
                {field: 'acDiscountId', width: 120, title: '优惠id'},
                {field: 'acDiscountType', width: 120, title: '优惠类型'},
                {field: 'acDiscountNum', width: 120, title: '优惠金额（元）'},
                {field: 'acOrderAmount', width: 120, title: '订单总金额（元）'},
                {field: 'acExchangeType', width: 100, title: '交易类型'},
                {field: 'acPaymentId', title: '支付单号', width: 150},
                {field: 'acPaytime', title: '消耗时间', width: 120},
                {field: 'acPaymerchantId', title: '消耗商户号', width: 150},
                {field: 'acPaymerchantName', title: '商户名称', width: 100},
                {field: 'acEquimentId', title: '设备号', width: 120},
                {field: 'acBankNum', width: 300, title: '银行流水号'}
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
        $("#acExchangeType").val("");
        $("#acPaytimeStart").val("");
        $("#acPaytimeEnd").val("");
        $("#acPaymerchantId").val("");

    });


    layui.use(["upload"], function() {
        var loading =null;
        var upload = layui.upload;
        upload.render({
            elem: "#batchAddBtn",//导入id
            url: "/manage/activitydatawechat/batchImport",
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




    //导出商家
    form.on('submit(outputDataFilter)',function (data) {

       // var url = "/manage/merchants/batchOutput";

        var formData = {
            "acBatchId": $.trim($("#acBatchId").val()),
            "acExchangeType": $.trim($("#acExchangeType").val()),
            "acPaytimeStart": $.trim($("#acPaytimeStart").val()),
            "acPaytimeEnd": $.trim($("#acPaytimeEnd").val()),
            "acPaymerchantId": $.trim($("#acPaymerchantId").val())
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
        var url = "/manage/activitydatawechat/batchOutput";
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