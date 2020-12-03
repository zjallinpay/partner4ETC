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
        elem: '#tradeTime',
        range: "~",
        trigger: 'click'
    });

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#orderQueryTable',
            //请求地址
            url: '/manage/cjm/orderlist',
            //是否分页
            page: true,
            //请求参数
            where: {
                merchantId: $.trim($("#merchantId").val()),
                // orderNo: $.trim($("#orderNo").val()),
                // payStatus: $.trim($("#payStatus").val()),
                tradeStartTime: $("#tradeTime").val() != "" ? $("#tradeTime").val().split(" ~ ")[0] + " 00:00:00" : "",
                tradeEndTime: $("#tradeTime").val() != "" ? $("#tradeTime").val().split(" ~ ")[1] + " 23:59:59" : ""
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
                    "data": res.data.list,
                    "totalAmount": res.data.totalAmount
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
                {field: 'orderNo', title: '订单号', width: 130},
                {field: 'trxId', title: '渠道流水号', width: 180},
                {field: 'terminalNo', title: '终端号', width: 100},
                {field: 'merchantId', title: '商户号', width: 150},
                {field: 'merchantName', title: '商户名称', width: 150},
                {field: 'payTime', title: '支付时间', width: 160},
                {field: 'payType', title: '支付类型', width: 120},
                {field: 'payAmount', title: '支付金额', width: 90},
                {field: 'sourceAmount', title: '原支付金额', width: 100},
                {field: 'discountAmount', title: '折扣金额', width: 90},
                {field: 'payStatus', title: '支付状态', width: 90},
                {field: 'extInfo', title: '备注', width: 90}
            ]],
            done: function (data) {
                $("[data-field='payStatus']").children().each(function () {
                    if ($(this).text() == '0' || $(this).text() == '4') {
                        $(this).text("成功");
                    } else if ($(this).text() == '1') {
                        $(this).text("失败");
                    }
                });

                $("[data-field='payType']").children().each(function () {
                    if ($(this).text() == 'VSP301') {
                        $(this).text("快捷支付-消费");
                    } else if ($(this).text() == 'VSP302') {
                        $(this).text("快捷支付-撤销");
                    }
                });
            }
        });
    };
    //页面加载就查询列表
    // search();
    //条件查询
    $("#queryBtn").on("click", function () {
        if ($.trim($("#merchantId").val()) == "") {
            layer.alert("请输入商户号查询");
            return;
        }
        search();
    });

    form.on('submit(queryFilter)', function (data) {
        return false;
    });
});