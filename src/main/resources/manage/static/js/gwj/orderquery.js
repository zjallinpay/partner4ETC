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
            url: '/manage/trade/list',
            //是否分页
            page: true,
            //请求参数
            where: {
                orderNo: $.trim($("#orderNo").val()),
                tenantName: $.trim($("#tenantName").val()),
                period: $.trim($("#period").val()),
                areaId: $.trim($("#areaId").val()),
                stall: $.trim($("#stall").val()),
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
                {field: 'amount', title: '支付金额', width: 90},
                {field: 'payType', title: '支付类型', width: 90},
                {field: 'payTime', title: '支付时间', width: 160},
                {field: 'area', title: '区域', width: 130},
                {field: 'stall', title: '摊位', width: 130},
                {field: 'rentStartTime', title: '开始时间', width: 110},
                {field: 'rentEndTime', title: '结束时间', width: 110},
                {field: 'period', title: '租赁周期', width: 130},
                {field: 'continueRent', title: '续租状态', width: 130},
                {field: 'tenantName', title: '承租人', width: 80},
                {field: 'tenantPhone', title: '手机号', width: 130},
                {field: 'trxId', title: '渠道流水号', width: 200}
                // {fixed: 'right', title: '操作', toolbar: '#operator', width: 130}
            ]],
            done: function (data) {
                //展示汇总金额
                $("#summary").text("汇总金额(元)：" + data.totalAmount);
            }
        });
    };
    //页面加载就查询列表
    search();
    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });

    form.on('submit(queryFilter)', function (data) {
        return false;
    });
});