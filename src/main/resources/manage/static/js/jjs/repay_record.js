//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form', 'laydate'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    laydate.render({
        elem: '#tradeTime',
        range: "~",
        trigger: 'click'
    });

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#repayRecordTable',
            //请求地址
            url: '/manage/jjs/repay/record',
            //是否分页
            page: true,
            //请求参数
            where: {
                batchNo: $.trim($("#batchNo").val()),
                hldName: $.trim($("#hldName").val()),
                status: $.trim($("#status").val()),
                tradeStartTime: $("#tradeTime").val() != "" ? $("#tradeTime").val().split(" ~ ")[0] : "",
                tradeEndTime: $("#tradeTime").val() != "" ? $("#tradeTime").val().split(" ~ ")[1] : ""
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
                $("tbody td[data-field='result']").children().each(function (index, val) {
                    if ($(this).text() == "0000") {
                        $(this).text("成功");
                    } else if ($(this).text() == "0001") {
                        $(this).text("失败");
                    } else if ($(this).text() == "0002") {
                        $(this).text("已提交");
                    } else if ($(this).text() == "1111") {
                        $(this).text("已受理");
                    } else {
                        $(this).text("");
                    }
                })
            },
            //每页展示的条数
            limits: [10, 20, 50],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'batchNo', title: '批次号'},
                {field: 'traceNum', title: '流水号'},
                {field: 'hldName', title: '姓名'},
                {field: 'cerNum', title: '身份证号'},
                {field: 'acctNum', title: '银行卡号'},
                {field: 'tradeAmount', title: '兑付金额(元)'},
                {field: 'transDate', title: '兑付时间'},
                {field: 'result', title: '兑付结果'},
                {field: 'remark', title: '备注'}
            ]]
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