//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到

function initDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询时间起（创建时间）
        laydate.render({
            elem: '#tradeDateQuery',
            type: 'date',
            range: '~',
            format: 'yyyy-MM-dd'
        });
    });
}

var layer = null;
layui.use(['table', 'element', 'laypage', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;

    initDate();//初始化时间控件
    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#credentialsTable',
            //请求地址
            url: '/manage/employee/orderList',
            toolbar: '#toolbarDemo',//开启头部工具栏
            defaultToolbar: [],//隐藏右侧图标，defaultToolbar: ['filter','print','exports']
            //是否分页
            page: true,
            //请求参数
            where: {
                customerName: $.trim($("#customerNameQuery").val()),
                employeeName: $.trim($("#employeeNameQuery").val()),
                status: $.trim($("#statusQuery").val()),
                branchName: $.trim($("#branchNameQuery").val()),
                orderNo: $.trim($("#orderNoQuery").val()),
                tradeType: $.trim($("#tradeTypeQuery").val()),
                startTime: $("#tradeDateQuery").val() == "" || $("#tradeDateQuery").val() ==undefined ? "" : $("#tradeDateQuery").val().substr(0, 10) + " 00:00:00",//查询创建时间起
                endTime: $("#tradeDateQuery").val() == "" || $("#tradeDateQuery").val() == undefined ? "" : $("#tradeDateQuery").val().substr(13, 11) + " 24:00:00"//查询创建时间止
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
                {type: 'checkbox'},
                {field: 'custName', title: '客户名称'},
                {field: 'branchName', title: '公司名称'},
                {field: 'payType', title: '交易类型'},
                {field: 'payStatus', title: '处理状态'},
                {field: 'orderNo', title: '订单号'},
                {field: 'employeeName', title: '员工名'},
                {field: 'payTime', title: '提交时间'},
                {field: 'updateTime', title: '完成时间'},
                {field: 'payAmount', title: '交易金额'},
                {field: 'fee', title: '手续费'},
                {fixed: 'right', title: '操作', toolbar: '#operator'}
            ]],
            done: function () {
            }
        });
    };
    //页面加载就查询列表
    search();
    //条件查询
    $("#queryBtn").on("click", function () {
        search();
    });


    //监听table行工具事件 如详情、编辑、删除操作
    table.on('tool(tableFilter)', function (obj) {
        //获取所在行的数据
        var myData = obj.data;
        //审核
        if (obj.event === 'details') {
            //打开模态框
            openModal("订单详情", "detailsForm",myData);
        }
    });

    //头工具栏事件
    table.on('toolbar(tableFilter)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'printCredentials':
                var checkData = checkStatus.data;
                if(checkData.length == 0){
                    layer.alert("请选择打印记录!");
                }
                if(checkData.length > 0){
                    for(var i=0; i<checkData.length; i++){
                        document.getElementById('printIframe').contentWindow.iframePrint(checkData[i]);
                    }
                }
                // var LODOP; //声明为全局变量
                // LODOP=getLodop();
                // LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单一");
                // LODOP.SET_PRINT_STYLE("FontSize",18);
                // LODOP.SET_PRINT_STYLE("Bold",1);
                // LODOP.ADD_PRINT_TEXT(50,231,260,39,"打印页面部分内容");
                // LODOP.ADD_PRINT_HTM(88,200,350,600,document.getElementById("form1").innerHTML);

                break;
            case 'exportData':
                var ins1 = table.render({
                    elem: '#data_export',
                    url: '/manage/employee/credentials/export', //数据接口
                    method: 'get',
                    title: '交易结果查询表',
                    //请求参数
                    where: {
                        customerName: $.trim($("#customerNameQuery").val()),
                        employeeName: $.trim($("#employeeNameQuery").val()),
                        status: $.trim($("#statusQuery").val()),
                        branchName: $.trim($("#branchNameQuery").val()),
                        orderNo: $.trim($("#orderNoQuery").val()),
                        tradeType: $.trim($("#tradeTypeQuery").val()),
                        startTime: $("#tradeDateQuery").val() == "" ? "" : $("#tradeDateQuery").val().substr(0, 10) + " 00:00:00",//查询创建时间起
                        endTime: $("#tradeDateQuery").val() == "" ? "" : $("#tradeDateQuery").val().substr(13, 11) + " 00:00:00"//查询创建时间止
                    },
                    cols: [[
                        {field: 'custName', title: '客户名称'},
                        {field: 'branchName', title: '公司名称'},
                        {field: 'payType', title: '交易类型'},
                        {field: 'status', title: '处理状态'},
                        {field: 'orderNo', title: '订单号'},
                        {field: 'employeeName', title: '员工名'},
                        {field: 'payTime', title: '提交时间'},
                        {field: 'updateTime', title: '完成时间'},
                        {field: 'amount', title: '交易金额'},
                        {field: 'fee', title: '手续费'},
                        {fixed: 'right', title: '操作', toolbar: '#operator'}
                    ]],

                    done: function (res, curr, count) {

                        // exportData = res.list;
                        // table.exportFile(ins1.config.id, exportData, 'xls');
                        var data = res.list;

                        var excel = layui.excel;
                        // console.log(res);
                        // 重点！！！如果后端给的数据顺序和映射关系不对，请执行梳理函数后导出
                        data = excel.filterExportData(data, [
                            'trxId'
                            ,'tlCustId'
                            , 'merchantName'
                            , 'branchName'
                            ,'termNo'
                            , 'updateTime'
                            ,'updateDate'
                            , 'payType'
                            , 'acct'
                            , 'acctType'
                            , 'payAmount'
                            ,'fee'
                            ,'payStatus'
                            ,'orderNo'
                            ,'employeeId'
                            ,'employeeName'
                            ,'custName'
                            ,'custPhone'
                        ]);
                        // 重点2！！！一般都需要加一个表头，表头的键名顺序需要与最终导出的数据一致
                        data.unshift({
                            trxId:"交易单号",
                            tlCustId:"商户号",
                            merchantName: "商户名称",
                            branchName: "门店名称",
                            termNo:"终端号",
                            updateTime: "完成时间",
                            updateDate:"完成日期",
                            payType: "交易类型",
                            acct: "交易账号",
                            acctType: "卡类别",
                            payAmount: "交易金额(元)",
                            fee:"手续费",
                            payStatus: "处理状态",
                            orderNo: "订单号",
                            employeeId:"员工编号",
                            employeeName: "员工姓名",
                            custName: "客户名称",
                            custPhone:"手机号"
                        });

                        var timestart = Date.now();
                        var nowTime = new Date().Format("yyyyMMddHHmmss");
                        excel.exportExcel(data, '交易结果查询_'+ nowTime +'.xlsx', 'xlsx');
                        //	var timeend = Date.now();

                        //	var spent = (timeend - timestart) / 1000;
                        //	layer.alert('单纯导出耗时 '+spent+' s');
                    }
                });
                break;
        };
    });

    //监听form表单提交事件 防止页面跳转
    form.on('submit(queryFilter)', function (data) {
        return false;
    });

    var contentData = null;//展示数据暂存
    //打开模态框
    function openModal(operateName, modalName,myData) {
        contentData = myData;
        top.layer.open({
            title: operateName,
            content: '/manage/hzcc/credentialsContent.html',
            area: ['900px', '500px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 2,
            success: function (layero, index) {    //成功获得加载changefile.html时，预先加载，将值从父窗口传到 子窗口
                var _body = parent.layer.getChildFrame('body', index);
                _body.find("#trxIdDetail").val(contentData.trxId);
                _body.find("#merchantInfoDetail").val(contentData.tlCustId);
                _body.find("#merchantNameDetail").val(contentData.branchName);
                _body.find("#branchNameDetail").val(contentData.branchName);
                _body.find("#submitTimeDetail").val(contentData.payTime);
                _body.find("#finishTimeDetail").val(contentData.updateTime);
                _body.find("#tradeTypeDetail").val(contentData.payType);
                _body.find("#tradeCountDetail").val(contentData.acct);
                _body.find("#cardTypeDetail").val(contentData.acctType);
                _body.find("#statusDetail").val(contentData.payStatus);
                _body.find("#tradeAmountDetail").val(contentData.payAmount);
                _body.find("#feeDetail").val(contentData.fee);
                _body.find("#termNoDetail").val(contentData.termNo);
                _body.find("#orderNoDetail").val(contentData.orderNo);
                var remark = "员工编号:"+contentData.employeeId+"#"+"员工姓名:"+contentData.employeeName+"#"
                    +"客户名称:"+contentData.custName+"#"+"手机号码:"+contentData.custPhone;
                _body.find("#remarkDetail").val(remark);
                layui.form.render();
            },
        });
    }

    //导出改为单独的事件，每次点击导出才会执行
    $("#exportData").click(function () {

    })


    $("#printCredentials").click(function (oper) {

        // if (oper < 10){
            bdhtml=window.document.body.innerHTML;//获取当前页的html代码
            sprnstr="<!--startprint"+"1"+"-->";//设置打印开始区域
            eprnstr="<!--endprint"+"1"+"-->";//设置打印结束区域
            prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html
            prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html
            window.document.body.innerHTML=prnhtml;
            window.print();

            window.document.body.innerHTML=bdhtml;
        // } else {
        //     window.print();
        // }
    })


    /**
     *  获取当前格式的日期
     * @param fmt
     * @returns {*}
     * @constructor
     */
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
});
